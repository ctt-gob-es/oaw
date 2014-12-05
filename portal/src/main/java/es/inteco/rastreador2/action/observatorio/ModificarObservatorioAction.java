package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModificarObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.servlets.ScheduleObservatoryServlet;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ModificarObservatorioAction extends Action {

    private Log log = LogFactory.getLog(ModificarObservatorioAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            if (CrawlerUtils.hasAccess(request, "edit.observatory")) {
                String esPrimera = request.getParameter(Constants.ES_PRIMERA);
                String id_observatorio = request.getParameter(Constants.ID_OBSERVATORIO);

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                } else {
                    List<SemillaForm> otherObservSeedList = (List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST);
                    List<SemillaForm> addSeeds = (List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST);

                    if ((esPrimera == null || esPrimera.trim().equals("")) && (request.getParameter(Constants.PAG_PARAM) == null) && (request.getParameter(Constants.PAG_PARAM2) == null)) {
                        ModificarObservatorioForm modificarObservatorioForm = new ModificarObservatorioForm();
                        modificarObservatorioForm = loadData(modificarObservatorioForm, request, true, id_observatorio);
                        ObservatoryUtils.removeSessionAttributes(request);
                        ObservatoryUtils.putSessionAttributes(request, modificarObservatorioForm);
                        return ObservatoryUtils.returnLists(request, mapping, modificarObservatorioForm.getSemillasAnadidas(), modificarObservatorioForm.getSemillasNoAnadidas(), false);
                    } else {
                        request.setAttribute(Constants.IS_UPDATE, Constants.CONF_SI);
                        if ((request.getParameter(Constants.PAG_PARAM) != null) || (request.getParameter(Constants.PAG_PARAM2) != null)) {
                            ModificarObservatorioForm modificarObservatorioForm = new ModificarObservatorioForm();
                            modificarObservatorioForm = loadData(modificarObservatorioForm, request, false, id_observatorio);
                            if ((request.getParameter(Constants.ACTION) == null) || !(request.getParameter(Constants.ACTION)).equals(Constants.ACCION_ACEPTAR)) {
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, true);
                            } else {
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, false);
                            }
                        } else {
                            ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) form;
                            modificarObservatorioForm = loadData(modificarObservatorioForm, request, false, id_observatorio);
                            if ((modificarObservatorioForm.getButtonAction() != null) && (modificarObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar.anadir.semilla")))) {
                                modificarObservatorioForm.setButtonAction(null);
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, true);
                            } else if ((modificarObservatorioForm.getButtonAction() != null) && (modificarObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar")))) {
                                modificarObservatorioForm = (ModificarObservatorioForm) request.getSession().getAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
                                return editObservatory(mapping, modificarObservatorioForm, request);
                            } else {
                                String id_seed = request.getParameter(Constants.SEMILLA);
                                String action = (String) request.getParameter(Constants.ACTION);

                                if ((action != null) && (action.equals(Constants.ACCION_SEPARATE_SEED))) {
                                    return ObservatoryUtils.separeSeedToObservatory(request, mapping, id_seed, true, addSeeds, otherObservSeedList);
                                } else if ((action != null) && (action.equals(Constants.ACCION_ADD_SEED))) {
                                    return ObservatoryUtils.addSeedToObservatory(request, mapping, id_seed, true, addSeeds, otherObservSeedList);
                                } else if ((action != null) && (action.equals(Constants.ACCION_ACEPTAR))) {
                                    return addListToObservatory(mapping, request);
                                }
                            }
                        }
                    }
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    private ActionForward addListToObservatory(ActionMapping mapping, HttpServletRequest request) {
        ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) request.getSession().getAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
        modificarObservatorioForm.setSemillasAnadidas((List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST));
        modificarObservatorioForm.setSemillasNoAnadidas((List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST));
        request.getSession().setAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM, modificarObservatorioForm);

        return ObservatoryUtils.returnLists(request, mapping, modificarObservatorioForm.getSemillasAnadidas(), modificarObservatorioForm.getSemillasNoAnadidas(), false);
    }

    private ModificarObservatorioForm loadData(ModificarObservatorioForm modificarObservatorioForm, HttpServletRequest request, boolean esPrimera, String id_observatorio) throws Exception {
        Connection c = null;
        Connection con = null;
        PropertiesManager pmgr = new PropertiesManager();

        try {
            c = DataBaseManager.getConnection();
            con = DataBaseManager.getConnection();

            if (id_observatorio != null) {
                modificarObservatorioForm.setId_observatorio(id_observatorio);
            }
            modificarObservatorioForm.setNormaV(DAOUtils.getNormas(con, false));

            modificarObservatorioForm.setPeriodicidadVector(DAOUtils.getRecurrence(c));

            List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
            modificarObservatorioForm.setLenguajeVector(new ArrayList<LenguajeForm>());
            for (LenguajeForm lenguajeForm : lenguajeFormList) {
                lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                modificarObservatorioForm.getLenguajeVector().add(lenguajeForm);
            }

            request.setAttribute(Constants.MINUTES, CrawlerUtils.getMinutes());
            request.setAttribute(Constants.HOURS, CrawlerUtils.getHours());
            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
            request.setAttribute(Constants.CARTUCHOS_VECTOR, LoginDAO.getAllUserCartridge(c));
            request.setAttribute(Constants.TIPOS_VECTOR, ObservatorioDAO.getAllObservatoryTypes(c));

            if (esPrimera) {
                ObservatorioDAO.getObservatoryDataToUpdate(c, modificarObservatorioForm);
            }

            modificarObservatorioForm.setNombre_antiguo(modificarObservatorioForm.getNombre());
            return modificarObservatorioForm;
        } catch (Exception e) {
            log.error("Excepción genérica al modificar observatorio");
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(con);
        }
    }

    private ActionForward editObservatory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) form;
        modificarObservatorioForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));

        Connection c = null;

        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();

            String id_observatorio = request.getParameter(Constants.ID_OBSERVATORIO);
            request.setAttribute(Constants.ID_OBSERVATORIO, id_observatorio);

            final ActionErrors errors = modificarObservatorioForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward(Constants.VOLVER));
            } else {
                try {
                    //Comprobamos que el nombre usa caracteres correctos
                    if (modificarObservatorioForm.getNombre() != null && !modificarObservatorioForm.getNombre().equals("")) {
                        if (!modificarObservatorioForm.getNombre().trim().equals(modificarObservatorioForm.getNombre_antiguo())) {
                            if (CuentaUsuarioDAO.existAccount(c, modificarObservatorioForm.getNombre())) {
                                Logger.putLog("Usuario Duplicado", ModificarObservatorioAction.class, Logger.LOG_LEVEL_INFO);
                                errors.add("usuarioDuplicado", new ActionMessage("usuario.duplicado"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.USUARIO_DUPLICADO);
                            }
                        }
                    }

                    modificarObservatorioForm.setId_observatorio(id_observatorio);
                    ObservatorioDAO.updateObservatory(c, modificarObservatorioForm);

                    // Cambiamos los rastreos programados
                    ScheduleObservatoryServlet.deleteJob(Long.parseLong(modificarObservatorioForm.getId_observatorio()));

                    if (modificarObservatorioForm.isActivo()) {
                        ScheduleObservatoryServlet.scheduleJob(modificarObservatorioForm.getNombre(), Long.parseLong(modificarObservatorioForm.getId_observatorio()),
                                modificarObservatorioForm.getFecha(), RastreoDAO.getRecurrence(c, Long.parseLong(modificarObservatorioForm.getPeriodicidad())),
                                modificarObservatorioForm.getCartucho().getId());
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", ModificarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                }
            }
            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.observatorio.editado");

            String volver = pmgr.getValue("returnPaths.properties", "volver.carga.observatorio");
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO);

        } catch (Exception e) {
            log.error("Excepción genérica al modificar observatorio");
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

    }
}