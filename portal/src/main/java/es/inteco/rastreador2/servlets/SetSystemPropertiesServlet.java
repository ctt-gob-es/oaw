package es.inteco.rastreador2.servlets;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import javax.net.ssl.*;
import javax.servlet.*;
import java.io.IOException;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


public class SetSystemPropertiesServlet extends GenericServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        PropertiesManager pmgr = new PropertiesManager();
        setTrustStore(pmgr);
        setProxy(pmgr);

        setTrustingAllCerts();

        setDefaultHostnameVerifier();
    }

    private void setTrustStore(PropertiesManager pmgr) {
        String trustStorePath = pmgr.getValue(CRAWLER_PROPERTIES, "digital.certificates.path");
        Logger.putLog("Configurando el truststore en " + trustStorePath, SetSystemPropertiesServlet.class, Logger.LOG_LEVEL_INFO);
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
    }

    private void setProxy(PropertiesManager pmgr) {
        if (pmgr.getValue(Constants.INTAV_PROPERTIES, "http.proxy.active").equalsIgnoreCase(Boolean.TRUE.toString())) {
            String host = pmgr.getValue(Constants.INTAV_PROPERTIES, "http.proxy.host");
            String port = pmgr.getValue(Constants.INTAV_PROPERTIES, "http.proxy.port");
            Logger.putLog("Configurando la información del proxy en " + host + ":" + port, SetSystemPropertiesServlet.class, Logger.LOG_LEVEL_INFO);
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", port);
            System.setProperty("https.proxyHost", host);
            System.setProperty("https.proxyPort", port);
        }
    }

    private void setTrustingAllCerts() {
        Logger.putLog("Configurando la aplicación para que no valide los certificados en SSL.", SetSystemPropertiesServlet.class, Logger.LOG_LEVEL_INFO);
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            Logger.putLog("Excepción: ", SetSystemPropertiesServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private void setDefaultHostnameVerifier() {
        HostnameVerifier hv = new HostnameVerifier() {
            // No validamos el nombre de HOST
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
    }

}
