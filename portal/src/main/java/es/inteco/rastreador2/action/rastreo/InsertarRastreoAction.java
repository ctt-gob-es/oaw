package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class InsertarRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "new.crawler")) {
                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }
                HttpSession sesion = request.getSession();

                //comprobamos si viene del formulario o inicio
                String accionFor = request.getParameter(Constants.ACCION_FOR);
                //es inicio,Consigue la fecha actual.
                InsertarRastreoForm insertarRastreoForm = (InsertarRastreoForm) form;

                Connection c = null;
                Connection con = null;

                try {

                    PropertiesManager pmgr = new PropertiesManager();
                    con = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));
                    c = DataBaseManager.getConnection();

                    //Si se ha pulsado Cargar Semilla
                    if (insertarRastreoForm.getSemillaBoton() != null && insertarRastreoForm.getSemillaBoton().equals(getResources(request).getMessage(getLocale(request), "boton.semilla"))) {
                        request.setAttribute(Constants.IS_NEW, "true");
                        insertarRastreoForm.setSemillaBoton("");

                        ActionForward forward = new ActionForward(mapping.findForward(Constants.FORWARD_SEMILLA));
                        String path = forward.getPath() + "?" + Constants.IS_NEW + "=" + request.getParameter(Constants.IS_NEW);
                        forward.setPath(path);
                        forward.setRedirect(true);
                        return forward;
                    }
                    //Si viene de cargar la semilla, se guarda esta en el form
                    else if (request.getParameter(Constants.ID_SEMILLA) != null) {
                        SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA)));
                        insertarRastreoForm.setSemilla(semillaForm.getNombre());
                        insertarRastreoForm.setId_semilla(semillaForm.getId());
                        return (mapping.findForward(Constants.VOLVER));
                    }
                    //Si se ha pulsado el boton volver de la carga de semillas
                    else if ((accionFor != null && accionFor.equals(Constants.CARGAR_SEMILLA))) {
                        return (mapping.findForward(Constants.VOLVER));
                    }

                    DatosForm userData = LoginDAO.getUserData(c, (String) sesion.getAttribute(Constants.USER));

                    //Recuperamos el lenguaje
                    List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
                    for (LenguajeForm lenguajeForm : lenguajeFormList) {
                        lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                        insertarRastreoForm.getLenguajeVector().add(lenguajeForm);
                    }

                    //Recuperamos las normas
                    insertarRastreoForm.setNormaVector(DAOUtils.getNormas(con, false));

                    // Recuperamos los cartuchos
                    insertarRastreoForm.setCartuchos(LoginDAO.getUserCartridge(c, Long.valueOf(userData.getId())));

                    if (insertarRastreoForm.getCartuchos() == null || insertarRastreoForm.getCartuchos().isEmpty()) {
                        //error, no puede crear un rastreo si no hay cartuchos instalados para el usuario
                        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.error.noCartuchos");
                        String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.rastreos");
                        request.setAttribute("mensajeExito", mensaje);
                        request.setAttribute("accionVolver", volver);
                        return mapping.findForward(Constants.NO_CARTUCHO_NO_CREATE);
                    }

                    insertarRastreoForm = cargarDatosGenerales(insertarRastreoForm);

                    if (accionFor == null || accionFor.trim().equals("")) {
                        return cargarDatosInicio(mapping, insertarRastreoForm);
                    } else {

                        ActionErrors errors = insertarRastreoForm.validate(mapping, request);

                        if (errors == null || errors.isEmpty()) {
                            //Comprobamos que el rastreo usa caracteres correctos
                            ComprobadorCaracteres cc = new ComprobadorCaracteres(insertarRastreoForm.getCodigo());
                            if (!cc.isNombreValido()) {
                                errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            if (RastreoDAO.existeRastreo(c, insertarRastreoForm.getCodigo())) {
                                errors.add("errorObligatorios", new ActionMessage("rastreo.duplicado"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            insertarRastreoForm.setFecha(insertarRastreoForm.getFecha() + " " + insertarRastreoForm.getHora());

                            if (insertarRastreoForm.getListaRastreable() != null && !insertarRastreoForm.getListaRastreable().isEmpty()) {
                                //Guardamos la lista Rastreable
                                SemillaDAO.insertList(c, Constants.ID_LISTA_RASTREABLE, insertarRastreoForm.getCodigo() + "-Rastreable", insertarRastreoForm.getListaRastreable(), null, null, null);
                                Long idCrawlableList = SemillaDAO.getIdList(c, insertarRastreoForm.getCodigo() + "-Rastreable", null);
                                insertarRastreoForm.setId_lista_rastreable(idCrawlableList);
                            }

                            if (insertarRastreoForm.getListaNoRastreable() != null && !insertarRastreoForm.getListaNoRastreable().isEmpty()) {
                                //Guardamos la lista No rastreable
                                SemillaDAO.insertList(c, Constants.ID_LISTA_NO_RASTREABLE, insertarRastreoForm.getCodigo() + "-NoRastreable", insertarRastreoForm.getListaNoRastreable(), null, null, null);
                                Long idNoCrawlableList = SemillaDAO.getIdList(c, insertarRastreoForm.getCodigo() + "-NoRastreable", null);
                                insertarRastreoForm.setId_lista_no_rastreable(idNoCrawlableList);
                            }
                            insertarRastreoForm.setActive(true);

                            RastreoDAO.insertarRastreo(c, insertarRastreoForm, false);

                            request.getSession().removeAttribute("InsertarRastreoForm");
                            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.rastreo.insertado");
                            String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.rastreos");
                            request.setAttribute("mensajeExito", mensaje);
                            request.setAttribute("accionVolver", volver);
                            return mapping.findForward(Constants.EXITO);
                        } else {
                            ActionForward forward = new ActionForward();
                            forward.setPath(mapping.getInput());
                            forward.setRedirect(true);
                            saveErrors(request.getSession(), errors);
                            return (forward);
                        }
                    }
                } finally {
                    DataBaseManager.closeConnection(c);
                    DataBaseManager.closeConnection(con);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

    }


    private InsertarRastreoForm cargarDatosGenerales(InsertarRastreoForm insertarRastreoForm) {
        final PropertiesManager pmgr = new PropertiesManager();
        final Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.date.format"));
        insertarRastreoForm.setFecha(dateFormat.format(date));

        //Consigue la hora actual
        dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.hour.format"));
        insertarRastreoForm.setHora(dateFormat.format(date));

        return insertarRastreoForm;
    }

    private ActionForward cargarDatosInicio(ActionMapping mapping, InsertarRastreoForm insertarRastreoForm) {
        insertarRastreoForm.setCodigo("");
        insertarRastreoForm.setSemilla("");
        insertarRastreoForm.setCuenta_cliente(null);
        insertarRastreoForm.setProfundidad(1);
        insertarRastreoForm.setTopN(0);

        //ya estan cargados los valores iniciales.
        return mapping.findForward(Constants.VOLVER);
    }


}