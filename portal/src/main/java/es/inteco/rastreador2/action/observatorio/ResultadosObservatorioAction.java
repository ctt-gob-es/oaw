package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.*;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ResultadosObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
                String action = request.getParameter(Constants.ACTION);
                if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
                    request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
                }
                if (action != null) {
                    if (action.equalsIgnoreCase(Constants.GET_SEEDS)) {
                        request.setAttribute(Constants.ID_CARTUCHO, request.getParameter(Constants.ID_CARTUCHO));
                        return getSeeds(mapping, form, request);
                    } else if (action.equals(Constants.ACCION_BORRAR)) {
                        return deleteCrawlerSeed(mapping, request);
                    } else if (action.equals(Constants.ACCION_LANZAR_EJECUCION)) {
                        return throwCrawlerSeedExecution(mapping, request);
                    } else if (action.equals(Constants.ACCION_CONFIRMACION_BORRAR)) {
                        return deleteSeedConfirmation(mapping, request);
                    } else if (action.equals(Constants.GET_FULFILLED_OBSERVATORIES)) {
                        request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
                        return getFulfilledObservatories(mapping, request);
                    } else if (action.equals(Constants.GET_ANNEXES)) {
                        return getAnnexes(mapping, request, response);
                    } else if (action.equals(Constants.ACCION_CONFIRMACION_BORRAR_EX_SEED)) {
                        return showDeleteSeedExecution(request, mapping);
                    } else if (action.equals(Constants.ACCION_BORRAR_EJECUCION)) {
                        return deleteObservatoryExecutedSeed(request, mapping);
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

    private ActionForward deleteObservatoryExecutedSeed(final HttpServletRequest request, final ActionMapping mapping) {
        try (Connection c = DataBaseManager.getConnection()) {
            RastreoDAO.borrarRastreoRealizado(c, Long.parseLong(request.getParameter(Constants.ID)));
            PropertiesManager pmgr = new PropertiesManager();
            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.ejecutada.eliminada");
            String volver = pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados.primarios.seeds").replace("{0}", request.getParameter(Constants.ID_OBSERVATORIO)).replace("{1}", request.getParameter(Constants.ID_EX_OBS)).replace("{2}", request.getParameter(Constants.ID_CARTUCHO));
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO2);
        } catch (Exception e) {
            Logger.putLog("Excepcion al borrar la ejecucion de la semilla del Observatorio.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward showDeleteSeedExecution(final HttpServletRequest request, final ActionMapping mapping) {
        try (Connection c = DataBaseManager.getConnection()) {
            request.setAttribute(Constants.SEMILLA_FORM, SemillaDAO.getSeedById(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA))));
            return mapping.findForward(Constants.CONFIRMACION_DELETE);
        } catch (Exception e) {
            Logger.putLog("Exceción al recuperar los datos de la semilla.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward deleteSeedConfirmation(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
            request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
        }
        final String idSemilla = request.getParameter(Constants.SEMILLA);

        try (Connection c = DataBaseManager.getConnection()) {
            final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
            request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
        } catch (Exception e) {
            Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return mapping.findForward(Constants.CONFIRMACION2);
    }

    private ActionForward deleteCrawlerSeed(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        final String idSemilla = request.getParameter(Constants.SEMILLA);
        final String confirmacion = request.getParameter(Constants.CONFIRMACION);
        try (Connection c = DataBaseManager.getConnection()) {
            if (confirmacion.equals(Constants.CONF_SI)) {
                SemillaDAO.deleteObservatorySeed(c, Long.parseLong(idSemilla), Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
                ActionMessages messages = new ActionMessages();
                messages.add("semillaEliminada", new ActionMessage("mensaje.exito.semilla.observatorio.eliminada"));
                saveErrors(request, messages);
            }

            request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.ID_OBSERVATORIO));
        } catch (Exception e) {
            Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
    }

    private ActionForward throwCrawlerSeedExecution(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;
        ActionForward forward = null;

        try {
            c = DataBaseManager.getConnection();
            c.setAutoCommit(false);
            if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
                request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
            }
            if (request.getParameter(Constants.ID_EX_OBS) != null) {
                request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
            }
            if (request.getParameter(Constants.CONFIRMACION) != null) {
                if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
                    //Recuperamos el id del rastreo
                    Long idCrawling = RastreoDAO.getCrawlerFromSeedAndObservatory(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA)), Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
                    Long idOldExecution = RastreoDAO.getExecutedCrawlerId(c, idCrawling, Long.parseLong(request.getParameter(Constants.ID_EX_OBS)));
                    //Borramos la cache
                    CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + request.getParameter(Constants.ID_EX_OBS));
                    //Lanzamos el rastreo y recuperamos el id de ejecución
                    lanzarRastreo(c, String.valueOf(idCrawling));
                    Long idNewExecution = Long.valueOf(RastreoDAO.getExecutedCrawling(c, idCrawling, Long.valueOf(request.getParameter(Constants.ID_SEMILLA))).getId());
                    //Borramos el rastreo ejecutado antigüo
                    RastreoUtils.borrarArchivosAsociados(request, c, String.valueOf(idOldExecution));
                    RastreoDAO.borrarRastreoRealizado(c, idOldExecution);
                    //Le asiganmos el nuevo a la ejecución del observatorio
                    RastreoDAO.setObservatoryExecutionToCrawlerExecution(c, Long.parseLong(request.getParameter(Constants.ID_EX_OBS)), idNewExecution);
                    forward = new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
                    String path = forward.getPath() + "&" + Constants.ID_EX_OBS + "=" + request.getParameter(Constants.ID_EX_OBS) +
                            "&" + Constants.ID_OBSERVATORIO + "=" + request.getParameter(Constants.ID_OBSERVATORIO) + "&" + Constants.ID_CARTUCHO +
                            "=" + request.getParameter(Constants.ID_CARTUCHO);
                    forward.setPath(path);
                    forward.setRedirect(true);
                    c.commit();
                } else if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_NO)) {
                    forward = new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
                }
            } else {
                String idSeed = request.getParameter(Constants.ID_SEMILLA);
                request.setAttribute(Constants.OBSERVATORY_SEED_FORM, SemillaDAO.getSeedById(c, Long.parseLong(idSeed)));
                forward = new ActionForward(mapping.findForward(Constants.FORWARD_THROW_SEED_CONFIRMATION));
                c.commit();
            }
            request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.ID_OBSERVATORIO));
        } catch (Exception e) {
            Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            if (c != null) {
                c.rollback();
            }
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return forward;
    }

    private void lanzarRastreo(final Connection c, final String idCrawling) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, idCrawling);
        dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));

        // Cargamos los dominios introducidos en el archivo de semillas
        final int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
        dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, false));

        dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, true));
        dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
        dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));

        dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) dcrForm.getId_rastreo()));

        if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
            dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
        }

        final DatosForm userData = LoginDAO.getUserDataByName(c, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"));

        final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, null, Long.valueOf(userData.getId()));

        final CrawlerJob crawlerJob = new CrawlerJob();
        crawlerJob.makeCrawl(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null));
    }

    private ActionForward getSeeds(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
        final SemillaForm semillaForm = (SemillaForm) form;
        final Long idObservatoryExecution = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));

        try (Connection c = DataBaseManager.getConnection()) {
            final PropertiesManager pmgr = new PropertiesManager();

            final int numResultA = ObservatorioDAO.countResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE);
            final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
            // Obtenemos las semillas de esa página del listado
            final List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);
            // Calculamos la puntuación media de cada semilla y la guardamos en sesion
            request.setAttribute(Constants.OBSERVATORY_SEED_LIST, ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResultA, pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"), pagina, Constants.PAG_PARAM));
        } catch (Exception e) {
            Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        }

        return mapping.findForward(Constants.OBSERVATORY_SEED_LIST);
    }

    public ActionForward getFulfilledObservatories(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        final Long observatoryId = Long.valueOf(request.getParameter(Constants.OBSERVATORY_ID));

        //Para mostrar todos los Rastreos del Sistema
        try (Connection c = DataBaseManager.getConnection()) {

            final int numResult = ObservatorioDAO.countFulfilledObservatories(c, observatoryId);
            final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(c, observatoryId, (pagina - 1), null));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
    }

    private ActionForward getAnnexes(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
            final Long idOperation = System.currentTimeMillis();

            AnnexUtils.createAnnex(CrawlerUtils.getResources(request), idObsExecution, idOperation);
            AnnexUtils.createAnnex2Ev(CrawlerUtils.getResources(request), idObsExecution, idOperation);

            final PropertiesManager pmgr = new PropertiesManager();
            final String zipPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexos.zip";
            ZipUtils.generateZipFile(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation.toString(), zipPath, true);

            CrawlerUtils.returnFile(response, zipPath, "application/zip", true);

            FileUtils.deleteDir(new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation));

            return null;
        } catch (Exception e) {
            Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            final ActionMessages errors = new ActionMessages();
            errors.add("usuarioDuplicado", new ActionMessage("data.export"));
            saveErrors(request, errors);
            return getFulfilledObservatories(mapping, request);
        }
    }

}
