package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.VerRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class VerRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.crawler")) {
                PropertiesManager pmgr = new PropertiesManager();
                HttpSession sesion = request.getSession();

                VerRastreoForm verRastreoForm = (VerRastreoForm) form;

                String id_rastreo = request.getParameter(Constants.ID_RASTREO);
                if (id_rastreo == null || id_rastreo.trim().equals("")) {
                    return mapping.findForward(Constants.LOGIN);
                }

                Connection c = null;
                Connection con = null;
                try {
                    c = DataBaseManager.getConnection();
                    con = DataBaseManager.getConnection();
                    String user = (String) request.getSession().getAttribute(Constants.USER);
                    long idRastreo = 0;
                    if (request.getParameter(Constants.ID_RASTREO) != null) {
                        idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
                    }
                    //Comprobamos que el usuario esta asociado con el rastreo que quiere ver
                    if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                        ArrayList<String> rolesesion = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
                        if (rolesesion.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.configurator.id"))) {
                            if (!RastreoDAO.rastreoValidoParaUsuario(c, Integer.parseInt(id_rastreo), (String) sesion.getAttribute(Constants.USER))) {
                                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.error.noPermisos");
                                String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.rastreos");
                                request.setAttribute("mensajeExito", mensaje);
                                request.setAttribute("accionVolver", volver);
                                return mapping.findForward(Constants.NO_RASTREO_PERMISO);
                            }
                        }
                        RastreoDAO.cargarRastreoVer(c, Integer.parseInt(id_rastreo), verRastreoForm);
                        verRastreoForm.setNormaAnalisisSt(RastreoDAO.getNombreNorma(con, verRastreoForm.getNormaAnalisis()));
                    } else {
                        return mapping.findForward(Constants.NO_PERMISSION);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", VerRastreoAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
                    DataBaseManager.closeConnection(con);
                }

                return mapping.findForward(Constants.EXITO_VER);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

}
