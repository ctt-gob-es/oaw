package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.cuentausuario.NuevaCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.cuentausuario.RastreoClienteForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class RastreoClienteAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "new.crawler")) {
                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                PropertiesManager pmgr = new PropertiesManager();
                RastreoClienteForm rastreoClienteForm = (RastreoClienteForm) form;
                //para saber si viene de inicio o del formulario
                String accionFor = request.getParameter(Constants.ACCION_FOR);

                Connection c = null;
                Connection con = null;
                try {
                    c = DataBaseManager.getConnection();
                    con = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));

                    CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
                    // Cargamos las cuentas de cliente
                    cargarCuentasUsuarioForm = CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, Constants.NO_PAGINACION);
                    request.setAttribute(Constants.LISTADO_CUENTAS_CLIENTE, cargarCuentasUsuarioForm.getListadoCuentasUsuario());

                    // Cargamos los cartuchos
                    DatosForm userData = LoginDAO.getUserData(c, (String) request.getSession().getAttribute(Constants.USER));
                    request.setAttribute(Constants.LISTADO_CARTUCHOS, LoginDAO.getUserCartridge(c, Long.valueOf(userData.getId())));

                    //Cargamos las normas
                    request.setAttribute(Constants.LISTADO_NORMAS, DAOUtils.getNormas(con, false));

                    if (accionFor != null) {
                        ActionErrors errors = rastreoClienteForm.validate(mapping, request);
                        if (errors == null || errors.isEmpty()) {

                            if (RastreoDAO.existeRastreo(c, rastreoClienteForm.getNombre())) {
                                errors.add("errorObligatorios", new ActionMessage("rastreo.duplicado"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.NUEVO_RASTREO_CLIENTE);
                            }

                            NuevaCuentaUsuarioForm cuentaUsuario = CuentaUsuarioDAO.getUserAccount(c, Long.valueOf(rastreoClienteForm.getIdCuenta()));

                            InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                            insertarRastreoForm.setCuenta_cliente(Long.valueOf(rastreoClienteForm.getIdCuenta()));
                            insertarRastreoForm.setCartucho(rastreoClienteForm.getCartucho());
                            if (insertarRastreoForm.getCartucho().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
                                insertarRastreoForm.setNormaAnalisis(rastreoClienteForm.getNormaAnalisis());
                            }
                            insertarRastreoForm.setCodigo(rastreoClienteForm.getNombre());
                            insertarRastreoForm = CrawlerUtils.insertarDatosAutomaticosCU(insertarRastreoForm, cuentaUsuario, "");
                            insertarRastreoForm.setId_semilla(cuentaUsuario.getIdSeed());
                            insertarRastreoForm.setId_lista_rastreable(cuentaUsuario.getIdCrawlableList());
                            insertarRastreoForm.setId_lista_no_rastreable(cuentaUsuario.getIdNoCrawlableList());
                            insertarRastreoForm.setActive(true);
                            RastreoDAO.insertarRastreo(c, insertarRastreoForm, false);

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
                } catch (Exception e) {
                    Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", RastreoClienteAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
                    DataBaseManager.closeConnection(con);
                }
                return mapping.findForward(Constants.NUEVO_RASTREO_CLIENTE);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

}