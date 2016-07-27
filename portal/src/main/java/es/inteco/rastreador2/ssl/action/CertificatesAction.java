package es.inteco.rastreador2.ssl.action;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.ssl.CertificateForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.*;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase InformesDispatchAction.
 * Action de Informes
 *
 * @author psanchez
 */
public class CertificatesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CrawlerUtils.hasAccess(request, "add.certificate")) {
            // Marcamos el menú
            request.getSession().setAttribute(Constants.MENU, Constants.MENU_CERTIFICATES);

            try {
                if (request.getParameter(Constants.ACCION) != null) {
                    if (request.getParameter(Constants.ACCION).equals(Constants.LOAD_CERTIFICATE_FORM)) {
                        return loadCertificateForm(mapping, request);
                    } else if (request.getParameter(Constants.ACCION).equals(Constants.UPLOAD_CERTIFICATE)) {
                        return uploadCertificate(mapping, form, request);
                    } else if (request.getParameter(Constants.ACCION).equals(Constants.DELETE_CERTIFICATE)) {
                        return deleteCertificate(mapping, request);
                    } else if (request.getParameter(Constants.ACCION).equals(Constants.DELETE_CONFIRMATION)) {
                        return deleteConfirmation(mapping, request);
                    }
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }

        return null;
    }

    private ActionForward loadCertificateForm(ActionMapping mapping, HttpServletRequest request) throws Exception {
        // Inicializamos el almacén de claves
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        loadKeyStore(keyStore);

        int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

        // Cargamos los certificados
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "certificates.pagination.size"));
        int resultFrom = pagSize * (pagina - 1);
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
        List<CertificateForm> certificates = new ArrayList<>();
        Enumeration<String> aliases = keyStore.aliases();

        List<CertificateForm> allCertificates = new ArrayList<>();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            allCertificates.add(getCertificateForm((X509Certificate) keyStore.getCertificate(alias), df, alias));
        }

        Collections.sort(allCertificates, new Comparator<CertificateForm>() {
            @Override
            public int compare(CertificateForm o1, CertificateForm o2) {
                return o1.getIssuer().compareToIgnoreCase(o2.getIssuer());
            }
        });

        for (int i = resultFrom; (i < keyStore.size()) && (i < resultFrom + pagSize); i++) {
            certificates.add(allCertificates.get(i));
        }

        request.setAttribute(Constants.CERTIFICATES, certificates);
        pagina = Pagination.getPage(request, Constants.PAG_PARAM);
        request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, keyStore.size(), String.valueOf(pagSize), pagina, Constants.PAG_PARAM));
        request.setAttribute(Constants.NUM_CERTIFICATES, keyStore.size());

        return mapping.findForward(Constants.CERTIFICATE_FORM);
    }

    private ActionForward uploadCertificate(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        final CertificateForm certificateForm = (CertificateForm) form;

        if (request.getParameter(Constants.ES_PRIMERA) != null && request.getParameter(Constants.ES_PRIMERA).equals(Constants.CONF_SI)) {
            return mapping.findForward(Constants.NUEVO_CERTIFICADO);
        } else {
            ActionErrors errors = certificateForm.validate(mapping, request);
            if (errors.isEmpty()) {
                // Inicializamos el almacén de claves
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                loadKeyStore(keyStore);

                // Creamos el socket ssl
                SavingTrustManager tm = getSavingTrustManager(keyStore);

                SSLSocket socket;
                try {
                    socket = createSSLSocket(tm, certificateForm);
                } catch (Exception e) {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.connecting.url"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }

                String mensaje;
                try {
                    Logger.putLog("Empezando el protocolo HandShake...", CertificatesAction.class, Logger.LOG_LEVEL_INFO);
                    socket.startHandshake();
                    socket.close();
                    Logger.putLog("No se han detectado errores, el certificado ya estaba añadido", CertificatesAction.class, Logger.LOG_LEVEL_INFO);
                    mensaje = getResources(request).getMessage(getLocale(request), "mensaje.certificado.ya.registrado");
                } catch (SSLException e) {
                    Logger.putLog("No se ha podido terminar el protocolo HandShake, se va a proceder a registrar el certificado", CertificatesAction.class, Logger.LOG_LEVEL_INFO);
                    X509Certificate[] chain = tm.getChain();
                    if (chain != null) {
                        addCertificates(chain, keyStore, certificateForm);
                        mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.certificado.insertado");
                    } else {
                        mensaje = getResources(request).getMessage(getLocale(request), "mensaje.no.hay.certificados");
                    }
                }

                PropertiesManager pmgr = new PropertiesManager();
                String volver = pmgr.getValue("returnPaths.properties", "volver.insertar.certificado");
                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
                return mapping.findForward(Constants.EXITO);
            } else {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }
    }

    private ActionForward deleteCertificate(ActionMapping mapping, HttpServletRequest request) throws Exception {
        if (isCancelled(request)) {
            return (mapping.findForward(Constants.VOLVER_CARGA));
        }

        // Inicializamos el almacén de claves
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        loadKeyStore(keyStore);

        String alias = request.getParameter(Constants.ALIAS);

        // TODO: ¿Por qué no funciona esto?
        if (keyStore.containsAlias(alias)) {
            PropertiesManager pmgr = new PropertiesManager();
            File file = new File(pmgr.getValue("certificados.properties", "truststore.path"));
            char[] passphrase = pmgr.getValue("certificados.properties", "truststore.pass").toCharArray();

            keyStore.deleteEntry(alias);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                keyStore.store(fos, passphrase);
            } catch (Exception e) {
                Logger.putLog("Exception al guardar el keyStore " + file, CertificatesAction.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }

        PropertiesManager pmgr = new PropertiesManager();
        request.setAttribute("mensajeExito", getResources(request).getMessage(getLocale(request), "mensaje.exito.certificado.borrado"));
        request.setAttribute("accionVolver", pmgr.getValue("returnPaths.properties", "volver.insertar.certificado"));
        return mapping.findForward(Constants.EXITO);
    }

    private ActionForward deleteConfirmation(ActionMapping mapping, HttpServletRequest request) throws Exception {

        // Inicializamos el almacén de claves
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        loadKeyStore(keyStore);

        String alias = request.getParameter(Constants.ALIAS);

        // TODO: ¿Por qué no funciona esto?
        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
        request.setAttribute(Constants.CERTIFICATE_FORM, getCertificateForm((X509Certificate) keyStore.getCertificate(alias), df, alias));

        return mapping.findForward(Constants.DELETE_CONFIRMATION);
    }

    private void loadKeyStore(KeyStore keyStore) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        File file = new File(pmgr.getValue("certificados.properties", "truststore.path"));
        char[] passphrase = pmgr.getValue("certificados.properties", "truststore.pass").toCharArray();
        Logger.putLog("Cargando KeyStore " + file + "...", CertificatesAction.class, Logger.LOG_LEVEL_INFO);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            keyStore.load(in, passphrase);
        } catch (Exception e) {
            Logger.putLog("Error Cargando KeyStore " + file, CertificatesAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    private SSLSocket createSSLSocket(SavingTrustManager tm, CertificateForm certificateForm) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");

        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        Logger.putLog("Abriendo la conexión a " + certificateForm.getHost() + ":" + certificateForm.getPort() + "...", CertificatesAction.class, Logger.LOG_LEVEL_INFO);
        SSLSocket socket = (SSLSocket) factory.createSocket(certificateForm.getHost(), Integer.parseInt(certificateForm.getPort()));
        socket.setSoTimeout(10000);
        return socket;
    }

    private SavingTrustManager getSavingTrustManager(KeyStore keyStore) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        return new SavingTrustManager(defaultTrustManager);
    }

    private void addCertificates(X509Certificate[] chain, KeyStore keyStore, CertificateForm certificateForm) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        File file = new File(pmgr.getValue("certificados.properties", "truststore.path"));
        char[] passphrase = pmgr.getValue("certificados.properties", "truststore.pass").toCharArray();
        for (int k = 0; k < chain.length; k++) {
            X509Certificate cert = chain[k];
            String alias = certificateForm.getHost() + "-" + (k + 1);
            keyStore.setCertificateEntry(alias, cert);

            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                keyStore.store(out, passphrase);
                Logger.putLog("Añadido al almacén de claves el certificado de " + alias, CertificatesAction.class, Logger.LOG_LEVEL_INFO);
            } catch (Exception e) {
                Logger.putLog("Error al añadidir el certificado de " + alias, CertificatesAction.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    private CertificateForm getCertificateForm(X509Certificate x509certificate, DateFormat df, String alias) {
        final CertificateForm certificateForm = new CertificateForm();

        certificateForm.setIssuer(getCNValue(x509certificate.getIssuerDN().getName()));
        certificateForm.setSubject(getCNValue(x509certificate.getSubjectDN().getName()));
        certificateForm.setVersion(String.valueOf(x509certificate.getVersion()));
        certificateForm.setValidateFrom(df.format(x509certificate.getNotBefore()));
        certificateForm.setValidateTo(df.format(x509certificate.getNotAfter()));
        certificateForm.setAlias(alias);

        return certificateForm;
    }

    private String getCNValue(String DNValue) {
        String regexp = "[CN|OU]=(.*?),";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(DNValue);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return DNValue;
        }
    }

}
