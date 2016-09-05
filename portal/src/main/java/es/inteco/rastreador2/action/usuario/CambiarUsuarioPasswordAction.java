package es.inteco.rastreador2.action.usuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.usuario.ModificarUsuarioPassForm;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class CambiarUsuarioPasswordAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String deMenu = request.getParameter(Constants.DE_MENU);

        if (deMenu != null) {
            // Marcamos el menú
            request.getSession().setAttribute(Constants.MENU, Constants.MENU_PASSWORD);
        } else {
            // Marcamos el menú usuarios
            request.getSession().setAttribute(Constants.MENU, Constants.MENU_USERS);
        }

        try {
            if (CrawlerUtils.hasAccess(request, "change.password")) {
                HttpSession sesion = request.getSession();

                if (isCancelled(request)) {
                    if (deMenu != null) {
                        return (mapping.findForward(Constants.VOLVER_CARGA));
                    } else {
                        request.setAttribute(Constants.DE_PASS, "si");
                        String roleType = request.getParameter(Constants.ROLE_TYPE);
                        return getCancelForward(mapping, roleType);
                    }
                }

                //nos conectamos
                PropertiesManager pmgr = new PropertiesManager();


                try (Connection con = DataBaseManager.getConnection()) {
                    String idUsuario = request.getParameter(Constants.USER);
                    ModificarUsuarioPassForm modificarUsuarioPassForm = (ModificarUsuarioPassForm) form;
                    modificarUsuarioPassForm.setIdUsuario(idUsuario);

                    String roleType = request.getParameter(Constants.ROLE_TYPE);
                    modificarUsuarioPassForm.setRoleType(roleType);

                    //comprobamos de donde viene
                    String accion = request.getParameter(Constants.ACTION);

                    if (deMenu != null) {
                        modificarUsuarioPassForm.setUsername((String) request.getSession().getAttribute(Constants.USER));
                        DatosForm datosForm = LoginDAO.getUserDataByName(con, modificarUsuarioPassForm.getUsername());
                        idUsuario = datosForm.getId();
                        request.setAttribute(Constants.DE_MENU, "si");
                    } else {
                        DatosForm datosForm = LoginDAO.getUserDataById(con, Long.valueOf(idUsuario));
                        modificarUsuarioPassForm.setUsername(datosForm.getUsuario());
                    }

                    if (accion != null && accion.equals(Constants.ACCION_MODIFICAR)) {
                        ActionErrors errors = modificarUsuarioPassForm.validate(mapping, request);

                        if (errors.isEmpty()) {
                            if (deMenu != null && !LoginDAO.existUserWithKey(con, modificarUsuarioPassForm.getPasswold(), Long.valueOf(idUsuario))) {
                                errors.add("errorDistintos", new ActionMessage("pass.Incorrecto"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            if (modificarUsuarioPassForm.getPassword().equals(modificarUsuarioPassForm.getPassword2())) {
                                LoginDAO.updatePassword(con, modificarUsuarioPassForm, Long.valueOf(idUsuario));
                            } else {
                                errors.add("errorDistintos", new ActionMessage("pass.distintos"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            sesion.setAttribute(Constants.PASS, modificarUsuarioPassForm.getPassword());
                            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.pass.admin.cambiado", modificarUsuarioPassForm.getUsername());
                            String volver;
                            if (deMenu != null) {
                                volver = pmgr.getValue("returnPaths.properties", "volver.index.admin");
                            } else {
                                volver = getBackButton(roleType, idUsuario);
                            }
                            request.setAttribute("mensajeExito", mensaje);
                            request.setAttribute("accionVolver", volver);
                            return mapping.findForward(Constants.EXITO2);
                        } else {
                            ActionForward forward = new ActionForward();
                            forward.setPath(mapping.getInput());
                            forward.setRedirect(true);
                            saveErrors(request.getSession(), errors);
                            return (forward);
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Excepion: ", CambiarUsuarioPasswordAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw e;
                }
                return mapping.findForward(Constants.EXITO);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ActionForward getCancelForward(ActionMapping mapping, String roleType) {
        ActionForward forward;

        PropertiesManager pmgr = new PropertiesManager();
        if (roleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.client"))) {
            forward = new ActionForward(mapping.findForward(Constants.VOLVER_CARGA_CLIENTE));
        } else if (roleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.observatory"))) {
            forward = new ActionForward(mapping.findForward(Constants.VOLVER_CARGA_OBSERVATORIO));
        } else {
            forward = new ActionForward(mapping.findForward(Constants.VOLVER_CARGA_SISTEMA));
        }

        forward.setPath(forward.getPath() + "?" + Constants.ROLE_TYPE + "=" + roleType);
        return forward;
    }

    private String getBackButton(String roleType, String user) {
        String backButton;

        PropertiesManager pmgr = new PropertiesManager();
        if (roleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.client"))) {
            backButton = pmgr.getValue("returnPaths.properties", "volver.modificar.usuario.client").replace("{0}", roleType).replace("{1}", user);
        } else if (roleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.observatory"))) {
            backButton = pmgr.getValue("returnPaths.properties", "volver.modificar.usuario.observatory").replace("{0}", roleType).replace("{1}", user);
        } else {
            backButton = pmgr.getValue("returnPaths.properties", "volver.modificar.usuario.system").replace("{0}", roleType).replace("{1}", user);
        }

        return backButton;
    }

}
