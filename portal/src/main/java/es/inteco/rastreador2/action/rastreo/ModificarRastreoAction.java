package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ModificarRastreoAction extends Action {

    public static String getHoraActual() {
        //Consigue la hora actual
        Calendar horac = java.util.Calendar.getInstance();
        int h = horac.get(Calendar.HOUR_OF_DAY);
        int min = horac.get(Calendar.MINUTE);
        int seg = horac.get(Calendar.SECOND);

        String horaText = Integer.toString(h);
        String minText = Integer.toString(min);
        String segText = Integer.toString(seg);

        if (h < 10) {
            horaText = "0" + h;
        }
        if (min < 10) {
            minText = "0" + min;
        }
        if (seg < 10) {
            segText = "0" + seg;
        }
        return horaText + ":" + minText + ":" + segText;
    }

    public static String getFechaActual() {
        //Consigue la fecha actual.
        Calendar fechac = java.util.Calendar.getInstance();
        int anio = fechac.get(Calendar.YEAR);
        int mes = fechac.get(Calendar.MONTH) + 1;
        int dia = fechac.get(Calendar.DAY_OF_MONTH);

        String a = Integer.toString(anio);
        String mc = Integer.toString(mes);
        String d = Integer.toString(dia);

        if (mes < 10) {
            mc = "0" + mes;
        }
        if (dia < 10) {
            d = "0" + dia;
        }
        return a + "-" + mc + "-" + d;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "edit.crawler")) {
                if (request.getParameter(Constants.ID_RASTREO) != null) {
                    request.setAttribute(Constants.ID_RASTREO, request.getParameter(Constants.ID_RASTREO));
                }

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                HttpSession sesion = request.getSession();
                InsertarRastreoForm insertarRastreoForm = (InsertarRastreoForm) form;

                String id_rastreo = request.getParameter(Constants.ID_RASTREO);

                try (Connection c = DataBaseManager.getConnection()) {

                    request.setAttribute(Constants.ID_RASTREO, id_rastreo);
                    //Si se ha pulsado Cargar Semilla
                    if (insertarRastreoForm.getSemillaBoton() != null && insertarRastreoForm.getSemillaBoton().equals(Constants.CARGAR_SEMILLA)) {
                        request.setAttribute(Constants.IS_NEW, "false");
                        insertarRastreoForm.setSemillaBoton("");
                        ActionForward forward = new ActionForward(mapping.findForward(Constants.FORWARD_SEMILLA));
                        String path = forward.getPath() + "?" + Constants.IS_NEW + "=" + request.getParameter(Constants.IS_NEW) +
                                "&" + Constants.ID_RASTREO + "=" + id_rastreo;
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
                    if ((request.getParameter(Constants.ACCION_FOR) != null && request.getParameter(Constants.ACCION_FOR).equals(Constants.CARGAR_SEMILLA))) {
                        return (mapping.findForward(Constants.VOLVER));
                    }


                    String user = (String) request.getSession().getAttribute(Constants.USER);
                    long idRastreo = 0;
                    if (request.getParameter(Constants.ID_RASTREO) != null) {
                        idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
                    }

                    //Comprobamos que el usuario esta asociado con el rastreo que quiere modificar
                    if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                        //comprobamos si viene de inicio de submit
                        if (request.getParameter(Constants.ACCION_FOR) == null || request.getParameter(Constants.ACCION_FOR).trim().equals("")) {
                            insertarRastreoForm.setFecha(getFechaActual());
                            insertarRastreoForm.setHora(getHoraActual());

                            DatosForm userData = LoginDAO.getUserDataByName(c, (String) sesion.getAttribute(Constants.USER));

                            // Recuperamos los cartuchos
                            insertarRastreoForm.setCartuchos(LoginDAO.getUserCartridge(c, Long.valueOf(userData.getId())));

                            //Recuperamos las normas
                            insertarRastreoForm.setNormaVector(DAOUtils.getNormas(c, false));

                            //Recuperamos el lenguaje
                            List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
                            insertarRastreoForm.setLenguajeVector(new ArrayList<LenguajeForm>());
                            for (LenguajeForm lenguajeForm : lenguajeFormList) {
                                lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                                insertarRastreoForm.getLenguajeVector().add(lenguajeForm);
                            }

                            if (insertarRastreoForm.getCartuchos() == null || insertarRastreoForm.getCartuchos().isEmpty()) {
                                //error, no puede crear un rastreo si no hay cartuchos instalados para el usuario
                                ActionUtils.setSuccesActionAttributes(request, "mensaje.error.noCartuchos", "volver.cargar.rastreos");
                                return mapping.findForward(Constants.NO_CARTUCHO_NO_CREATE);
                            }

                            //comprobamos que el rastreo es valido para este usuario
                            if (!RastreoDAO.rastreoValidoParaUsuario(c, Integer.parseInt(id_rastreo), (String) sesion.getAttribute(Constants.USER))) {
                                ActionUtils.setSuccesActionAttributes(request, "mensaje.error.noPermisos", "volver.cargar.rastreos");
                                return mapping.findForward(Constants.NO_RASTREO_PERMISO);
                            }

                            insertarRastreoForm = RastreoDAO.cargarRastreo(c, Integer.parseInt(id_rastreo), insertarRastreoForm);
                            insertarRastreoForm = cargarDatos(insertarRastreoForm);

                            //Si viene de cargar la semilla, se guarda esta en el form
                            if (request.getParameter(Constants.ID_SEMILLA) != null) {
                                SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA)));
                                insertarRastreoForm.setSemilla(semillaForm.getNombre());
                                insertarRastreoForm.setId_semilla(semillaForm.getId());
                                //return(mapping.findForward(Constants.VOLVER));
                            }

                            //inicio, cogemos el del rastreo
                            insertarRastreoForm.setRastreo(insertarRastreoForm.getCodigo());
                            insertarRastreoForm.setId_rastreo(Integer.parseInt(id_rastreo));
                            return mapping.findForward(Constants.VOLVER);

                        } else {
                            ActionErrors errors = insertarRastreoForm.validate(mapping, request);
                            if (errors.isEmpty()) {
                                String rastreoAntiguo = request.getParameter(Constants.RASTREO_ANTIGUO);

                                //Comprobamos que el rastreo usa caracteres correctos
                                ComprobadorCaracteres cc = new ComprobadorCaracteres(insertarRastreoForm.getCodigo());
                                if (!cc.isNombreValido()) {
                                    errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                                    saveErrors(request, errors);
                                    return mapping.findForward(Constants.VOLVER);
                                }

                                //Comprobamos que no existe el rastreo
                                if (!rastreoAntiguo.equals(insertarRastreoForm.getCodigo()) && RastreoDAO.existeRastreo(c, insertarRastreoForm.getCodigo())) {
                                    errors.add("errorObligatorios", new ActionMessage("rastreo.duplicado"));
                                    saveErrors(request, errors);
                                    return mapping.findForward(Constants.VOLVER);
                                }
                                if (insertarRastreoForm.getCuenta_cliente() != null && insertarRastreoForm.getCuenta_cliente() != 0) {
                                    insertarRastreoForm.setCodigo(insertarRastreoForm.getCodigo() + "-" + CartuchoDAO.getApplication(c, Long.valueOf(insertarRastreoForm.getCartucho())));
                                    rastreoAntiguo += "-" + CartuchoDAO.getApplication(c, Long.valueOf(insertarRastreoForm.getCartucho()));
                                }

                                //necesitamos los ids
                                InsertarRastreoForm rastreo = new InsertarRastreoForm();
                                rastreo = RastreoDAO.cargarRastreo(c, Integer.parseInt(id_rastreo), rastreo);
                                insertarRastreoForm.setId_lista_rastreable(rastreo.getId_lista_rastreable());
                                insertarRastreoForm.setId_lista_no_rastreable(rastreo.getId_lista_no_rastreable());
                                insertarRastreoForm.setId_rastreo(Long.parseLong(id_rastreo));
                                //insertarRastreoForm.setId_semilla(SemillaDAO.getIdList(c, insertarRastreoForm.getSemilla()));

                                RastreoDAO.updateRastreo(insertarRastreoForm);

                                ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.rastreo.editado", "volver.cargar.rastreos");
                                return mapping.findForward(Constants.EXITO);

                            } else {
                                ActionForward forward = new ActionForward();
                                forward.setPath(mapping.getInput() + "?" + Constants.ID_RASTREO + "=" + insertarRastreoForm.getId_rastreo());
                                forward.setRedirect(true);
                                saveErrors(request.getSession(), errors);
                                return (forward);
                            }
                        }
                    } else {
                        return mapping.findForward(Constants.NO_PERMISSION);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", ModificarRastreoAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    request.setAttribute("InsertarRastreoForm", insertarRastreoForm);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private InsertarRastreoForm cargarDatos(InsertarRastreoForm insertarRastreoForm) {
        PropertiesManager pmgr = new PropertiesManager();
        if (insertarRastreoForm.getNormaAnalisis() != null) {
            if (insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id")) ||
                    insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"))) {
                insertarRastreoForm.setNormaAnalisisEnlaces("1");
            } else {
                insertarRastreoForm.setNormaAnalisisEnlaces("0");
            }
        }
        return insertarRastreoForm;
    }
}