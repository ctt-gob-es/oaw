package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.EliminarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.VerRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ActionUtils;
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

public class EliminarRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.crawler")) {
                final EliminarRastreoForm eliminarRastreoForm = (EliminarRastreoForm) form;

                final PropertiesManager pmgr = new PropertiesManager();
                final HttpSession sesion = request.getSession();

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                //se comprueba si hay idrastreo sino pa login
                String id_rastreo = request.getParameter(Constants.ID_RASTREO);
                if (id_rastreo == null || id_rastreo.trim().equals("")) {
                    return mapping.findForward(Constants.LOGIN);
                }

                //comprobamos de dodne viene, si no tiene el confirmacion igual a si entonces hay que mostrarle la ventana de confirmacion
                String confeli = request.getParameter(Constants.CONFIRMACION);
                try (Connection c = DataBaseManager.getConnection()) {
                    String user = (String) request.getSession().getAttribute(Constants.USER);
                    long idRastreo = 0;
                    if (request.getParameter(Constants.ID_RASTREO) != null) {
                        idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
                    }
                    //Comprobamos que el usuario esta asociado con el rastreo que quiere eliminar
                    if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {

                        ArrayList<String> rolesesion = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
                        if (rolesesion.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.configurator.id"))) {
                            //comprobamos que el rastreo es valido para este usuario
                            boolean userValido = RastreoDAO.rastreoValidoParaUsuario(c, Integer.parseInt(id_rastreo), (String) sesion.getAttribute("user"));
                            if (!userValido) {
                                ActionUtils.setSuccesActionAttributes(request, "mensaje.error.noPermisos", "volver.cargar.rastreos");
                                return mapping.findForward(Constants.NO_RASTREO_PERMISO);
                            }
                        }

                        if (confeli == null || !confeli.equals(Constants.CONFIRMACION_SI)) {
                            VerRastreoForm verRastreoForm = new VerRastreoForm();
                            verRastreoForm = RastreoDAO.cargarRastreoVer(c, idRastreo, verRastreoForm);
                            if (verRastreoForm.getFecha() != null && verRastreoForm.getFecha().endsWith(".0")) {
                                verRastreoForm.setFecha(verRastreoForm.getFecha().substring(0, verRastreoForm.getFecha().length() - 2));
                            }

                            eliminarRastreoForm.setFecha(verRastreoForm.getFecha());
                            eliminarRastreoForm.setCodigo(verRastreoForm.getRastreo());
                            eliminarRastreoForm.setCartucho(verRastreoForm.getNombre_cartucho());
                            eliminarRastreoForm.setIdrastreo(id_rastreo);
                            eliminarRastreoForm.setNormaAnalisis(RastreoDAO.getNombreNorma(c, verRastreoForm.getNormaAnalisis()));

                        } else {
                            RastreoDAO.borrarRastreo(c, Integer.parseInt(id_rastreo));

                            ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.rastreo.eliminado", "volver.cargar.rastreos");
                            return mapping.findForward(Constants.EXITO);
                        }
                    } else {
                        return mapping.findForward(Constants.NO_PERMISSION);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", EliminarRastreoAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                }

                return mapping.findForward(Constants.VENTANA_CONFIRMACION);

            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }
}