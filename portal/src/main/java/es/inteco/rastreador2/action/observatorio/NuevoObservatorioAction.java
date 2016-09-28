package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.NuevoObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.servlets.ScheduleObservatoryServlet;
import es.inteco.rastreador2.utils.*;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class NuevoObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "new.observatory")) {
                String esPrimera = request.getParameter(Constants.ES_PRIMERA);

                //Si se pulsa el boton Volver se vuelve al listado de observatorios
                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                } else {
                    List<SemillaForm> otherObsvSeedList = (List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST);
                    List<SemillaForm> addSeeds = (List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST);
                    //Si estamos en la carga inicial, se cargan los datos, se borran las listas y form de la session
                    //y se incluyen en ésta estos parametros inicializados
                    if ((esPrimera == null || esPrimera.trim().equals("")) && (request.getParameter(Constants.PAG_PARAM) == null) && (request.getParameter(Constants.PAG_PARAM2) == null)) {
                        NuevoObservatorioForm nuevoObservatorioForm = new NuevoObservatorioForm();
                        nuevoObservatorioForm = inicializeData(nuevoObservatorioForm, request);
                        ObservatoryUtils.removeSessionAttributes(request);
                        ObservatoryUtils.putSessionAttributes(request, nuevoObservatorioForm);
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        //SI NO ESTAMOS EN LA CARGA INICIAL
                        //Si estamos cambiando de página cargamos los nuevos datos de paginación
                        if ((request.getParameter(Constants.PAG_PARAM) != null) || (request.getParameter(Constants.PAG_PARAM2) != null)) {
                            NuevoObservatorioForm nuevoObservatorioForm = new NuevoObservatorioForm();
                            nuevoObservatorioForm = inicializeData(nuevoObservatorioForm, request);
                            if ((request.getParameter(Constants.ACTION) == null) || !(request.getParameter(Constants.ACTION)).equals(Constants.ACCION_ACEPTAR)) {
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObsvSeedList, true);
                            } else {
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObsvSeedList, false);
                            }

                        } else {
                            // Si no viene de pulsar un enlace de paginación entonces,
                            // Incluimos en el form que tenemos los datos iniciales (vectores de periodicidad, etc..)
                            NuevoObservatorioForm nuevoObservatorioForm = (NuevoObservatorioForm) form;
                            nuevoObservatorioForm = inicializeData(nuevoObservatorioForm, request);
                            //Si se ha pulsado el botón añadir semillas, se va a la pantalla de añadir semillas
                            if ((nuevoObservatorioForm.getButtonAction() != null) && (nuevoObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar.anadir.semilla")))) {
                                nuevoObservatorioForm.setButtonAction(null);
                                return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObsvSeedList, true);
                                //Si se ha pulsado aceptar, se inserta el observatorio en bbdd
                            } else if ((nuevoObservatorioForm.getButtonAction() != null) && (nuevoObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar")))) {
                                nuevoObservatorioForm = (NuevoObservatorioForm) request.getSession().getAttribute(Constants.NUEVO_OBSERVATORIO_FORM);
                                return insertObservatory(mapping, nuevoObservatorioForm, request);
                                //Si no, es que estamos dentro de la pantalla de añadir semillas. Recuperamos la accion
                                // y el id de la semilla que se quiere añadir o desvincular al observatorio
                            } else {
                                String accion = request.getParameter(Constants.ACTION);
                                String idSeed = request.getParameter(Constants.SEMILLA);

                                //Si se desvincula la semilla
                                if ((accion != null) && (accion.equals(Constants.ACCION_SEPARATE_SEED))) {
                                    return ObservatoryUtils.separeSeedToObservatory(request, mapping, idSeed, true, addSeeds, otherObsvSeedList);
                                    //Si se añade la semilla
                                } else if ((accion != null) && (accion.equals(Constants.ACCION_ADD_SEED))) {
                                    return ObservatoryUtils.addSeedToObservatory(request, mapping, idSeed, true, addSeeds, otherObsvSeedList);
                                    //Si acabamos ya de añadir y desvincular y queremos volver a la pantalla de crear observatorio
                                } else if ((accion != null) && (accion.equals(Constants.ACCION_ACEPTAR))) {
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
        return mapping.findForward(Constants.ERROR_PAGE);

    }

    //Se añaden las listas de semillas vinculadas y no vinculadas al objeto nuevoObservatorioForm
    private ActionForward addListToObservatory(ActionMapping mapping, HttpServletRequest request) {
        NuevoObservatorioForm nuevoObservatorioForm = (NuevoObservatorioForm) request.getSession().getAttribute(Constants.NUEVO_OBSERVATORIO_FORM);
        nuevoObservatorioForm.setAddSeeds((List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST));
        nuevoObservatorioForm.setOtherSeeds((List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST));
        request.getSession().setAttribute(Constants.NUEVO_OBSERVATORIO_FORM, nuevoObservatorioForm);

        return ObservatoryUtils.returnLists(request, mapping, nuevoObservatorioForm.getAddSeeds(), nuevoObservatorioForm.getOtherSeeds(), false);
    }


    private void newObservatoryWindowPagination(HttpServletRequest request, NuevoObservatorioForm nuevoObservatorioForm) {
        final PropertiesManager pmgr = new PropertiesManager();

        final int pagSizeNU = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatory.results.size"));
        final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
        final int resultFrom = pagSizeNU * (pagina - 1);

        request.getSession().setAttribute(Constants.OBS_PAGINATION_RESULT_FROM, resultFrom);
        request.getSession().setAttribute(Constants.OBS_PAGINATION, pagSizeNU);
        request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, nuevoObservatorioForm.getAddSeeds().size(), String.valueOf(pagSizeNU), pagina, Constants.PAG_PARAM));
    }

    private NuevoObservatorioForm inicializeData(NuevoObservatorioForm nuevoObservatorioForm, HttpServletRequest request) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            nuevoObservatorioForm.setPeriodicidadVector(DAOUtils.getRecurrence(c));

            final List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
            nuevoObservatorioForm.setLenguajeVector(new ArrayList<LenguajeForm>());
            for (LenguajeForm lenguajeForm : lenguajeFormList) {
                lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                nuevoObservatorioForm.getLenguajeVector().add(lenguajeForm);
            }

            nuevoObservatorioForm.setOtherSeeds(ObservatorioDAO.getObservatorySeeds(c));

            request.setAttribute(Constants.MINUTES, CrawlerUtils.getMinutes());
            request.setAttribute(Constants.HOURS, CrawlerUtils.getHours());
            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
            request.setAttribute(Constants.CARTUCHOS_VECTOR, LoginDAO.getAllUserCartridge(c));
            request.setAttribute(Constants.TIPOS_VECTOR, ObservatorioDAO.getAllObservatoryTypes(c));

            return nuevoObservatorioForm;
        } catch (Exception e) {
            Logger.putLog("Exception: ", NuevoObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        }
    }

    private ActionForward insertObservatory(ActionMapping mapping, NuevoObservatorioForm nuevoObservatorioForm, HttpServletRequest request) throws Exception {
        if (nuevoObservatorioForm.getAddSeeds() != null) {
            newObservatoryWindowPagination(request, nuevoObservatorioForm);
        }

        try (Connection c = DataBaseManager.getConnection()) {
            ActionErrors errors = nuevoObservatorioForm.validate(mapping, request);

            if (errors.isEmpty()) {
                if (ObservatorioDAO.existObservatory(c, nuevoObservatorioForm.getNombre())) {
                    errors.add(Constants.USUARIO_DUPLICADO, new ActionMessage("observatorio.duplicada"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }

                try {
                    nuevoObservatorioForm.setFecha(CrawlerUtils.getFechaInicio(nuevoObservatorioForm.getFechaInicio(),
                            nuevoObservatorioForm.getHoraInicio(), nuevoObservatorioForm.getMinutoInicio()));
                } catch (Exception pe) {
                    errors.add(Constants.FORMATO_FECHA_INCORRECTO, new ActionMessage("formato.fecha.incorrecta"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }

                Long idObservatorio = ObservatorioDAO.insertObservatory(c, nuevoObservatorioForm);

                if (nuevoObservatorioForm.isActivo()) {
                    // Programamos la ejecución del observatorio
                    ScheduleObservatoryServlet.scheduleJob(nuevoObservatorioForm.getNombre(), idObservatorio,
                            nuevoObservatorioForm.getFecha(), RastreoDAO.getRecurrence(c, Long.parseLong(nuevoObservatorioForm.getPeriodicidad())),
                            nuevoObservatorioForm.getCartucho().getId());
                }

                if (!idObservatorio.equals((long) 0)) {
                    ObservatoryUtils.removeSessionAttributes(request);
                    ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.observatorio", "volver.carga.observatorio");
                    ActionForward forward = new ActionForward(mapping.findForward(Constants.EXITO));
                    forward.setRedirect(true);
                    return forward;
                } else {
                    errors.add("observatorio", new ActionMessage("fallo.observatorio"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }

            } else {
                saveErrors(request, errors);
                return mapping.findForward(Constants.VOLVER);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", NuevoObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

}