package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.ModificarCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.servlets.ScheduleClientAccountsServlet;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ModificarCuentaUsuarioAction extends Action {

    private Log log = LogFactory.getLog(ModificarCuentaUsuarioAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "edit.client.account")) {

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                PropertiesManager pmgr = new PropertiesManager();
                HttpSession sesion = request.getSession();
                ModificarCuentaUsuarioForm modificarCuentaUsuarioForm = (ModificarCuentaUsuarioForm) form;
                modificarCuentaUsuarioForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));

                Connection c = null;
                Connection con = null;

                try {
                    c = DataBaseManager.getConnection();
                    con = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));

                    String id_cuenta = null;
                    if (request.getParameter(Constants.DE_MENU) == null) {
                        id_cuenta = request.getParameter(Constants.ID_CUENTA);
                    } else {
                        String user = (String) sesion.getAttribute(Constants.USER);
                        id_cuenta = String.valueOf(CuentaUsuarioDAO.getAccountFromUser(c, user));
                        request.setAttribute(Constants.DE_MENU, "true");
                    }
                    request.setAttribute(Constants.ID_CUENTA, id_cuenta);

                    modificarCuentaUsuarioForm.setId_cuenta(id_cuenta);
                    modificarCuentaUsuarioForm.setCartuchosList(LoginDAO.getAllUserCartridge(c));
                    modificarCuentaUsuarioForm.setNormaV(DAOUtils.getNormas(con, false));

                    modificarCuentaUsuarioForm.setPeriodicidadVector(DAOUtils.getRecurrence(c));

                    List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
                    modificarCuentaUsuarioForm.setLenguajeVector(new ArrayList<LenguajeForm>());
                    for (LenguajeForm lenguajeForm : lenguajeFormList) {
                        lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                        modificarCuentaUsuarioForm.getLenguajeVector().add(lenguajeForm);
                    }

                    request.setAttribute(Constants.MINUTES, CrawlerUtils.getMinutes());
                    request.setAttribute(Constants.HOURS, CrawlerUtils.getHours());

                    //Comprobamos si estamos en inicio o se a pulsado al submit del formulario
                    //para ello cojemos el valEmail y miramos si esta en request
                    String esPrimera = request.getParameter(Constants.ES_PRIMERA);

                    if (esPrimera == null || esPrimera.trim().equals("")) {
                        CuentaUsuarioDAO.getAccountDatesToUpdate(c, modificarCuentaUsuarioForm);
                        modificarCuentaUsuarioForm.setNombre_antiguo(modificarCuentaUsuarioForm.getNombre());

                        if (modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id")) ||
                                modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"))) {
                            modificarCuentaUsuarioForm.setNormaAnalisisEnlaces("1");
                        } else {
                            modificarCuentaUsuarioForm.setNormaAnalisisEnlaces("0");
                        }

                        return mapping.findForward(Constants.VOLVER);

                    } else {
                        ActionErrors errors = modificarCuentaUsuarioForm.validate(mapping, request);

                        if (errors != null && !errors.isEmpty()) {
                            saveErrors(request, errors);
                            return (mapping.findForward(Constants.VOLVER));
                        } else {

                            try {
                                //Comprobamos que el nombre usa caracteres correctos
                                if (modificarCuentaUsuarioForm.getNombre() != null && !modificarCuentaUsuarioForm.getNombre().equals("")) {
                                    ComprobadorCaracteres cc = new ComprobadorCaracteres(modificarCuentaUsuarioForm.getNombre());
                                    if (!cc.isNombreValido()) {
                                        Logger.putLog("CARACTERES ILEGALES", ModificarCuentaUsuarioAction.class, Logger.LOG_LEVEL_INFO);
                                        errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                                        saveErrors(request, errors);
                                        return mapping.findForward(Constants.VOLVER);
                                    }
                                    String nombre = cc.espacios();
                                    if (!nombre.equals(modificarCuentaUsuarioForm.getNombre_antiguo())) {
                                        if (CuentaUsuarioDAO.existAccount(c, modificarCuentaUsuarioForm.getNombre())) {
                                            Logger.putLog("Usuario Duplicado", ModificarCuentaUsuarioAction.class, Logger.LOG_LEVEL_INFO);
                                            errors.add("usuarioDuplicado", new ActionMessage("usuario.duplicado"));
                                            saveErrors(request, errors);
                                            return mapping.findForward(Constants.USUARIO_DUPLICADO);
                                        }
                                    }
                                }

                                modificarCuentaUsuarioForm.setId_cuenta(id_cuenta);
                                modificarCuentaUsuarioForm.setIdSemilla(CuentaUsuarioDAO.getIdSemillaFromCA(c, Long.parseLong(id_cuenta)));
                                modificarCuentaUsuarioForm.setIdListaRastreable(CuentaUsuarioDAO.getIdLRFromCA(c, Long.parseLong(id_cuenta)));
                                modificarCuentaUsuarioForm.setIdListaNoRastreable(CuentaUsuarioDAO.getIdLNRFromCA(c, Long.parseLong(id_cuenta)));

                                if (request.getParameter(Constants.DE_MENU) != null) {
                                    CuentaUsuarioDAO.updateUAccount(modificarCuentaUsuarioForm, id_cuenta, true);
                                } else {
                                    CuentaUsuarioDAO.updateUAccount(modificarCuentaUsuarioForm, id_cuenta, false);
                                }

                                // Cambiamos los rastreos programados
                                ScheduleClientAccountsServlet.deleteJobs(Long.parseLong(id_cuenta), Constants.CLIENT_ACCOUNT_TYPE);

                                if (modificarCuentaUsuarioForm.isActivo()) {
                                    ScheduleClientAccountsServlet.scheduleJob(Long.parseLong(id_cuenta), Constants.CLIENT_ACCOUNT_TYPE);
                                }
                            } catch (Exception e) {
                                Logger.putLog("Exception: ", ModificarCuentaUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
                                throw new Exception(e);
                            }
                        }
                        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.cuenta.usuario.editada");

                        String volver = "";
                        if (request.getParameter(Constants.DE_MENU) == null) {
                            volver = pmgr.getValue("returnPaths.properties", "volver.carga.cuentas.cliente");
                        } else {
                            volver = pmgr.getValue("returnPaths.properties", "volver.index.admin");
                        }
                        request.setAttribute("mensajeExito", mensaje);
                        request.setAttribute("accionVolver", volver);
                        return mapping.findForward(Constants.EXITO);
                    }
                } catch (Exception e) {
                    log.error("Excepción genérica al crear un nuevo usuario");
                    throw new Exception(e);
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
}