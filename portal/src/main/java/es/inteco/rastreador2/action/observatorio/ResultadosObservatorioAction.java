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

    private ActionForward deleteObservatoryExecutedSeed(HttpServletRequest request, ActionMapping mapping) {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            RastreoDAO.borrarRastreoRealizado(c, Long.parseLong(request.getParameter(Constants.ID)));
            PropertiesManager pmgr = new PropertiesManager();
            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.ejecutada.eliminada");
            String volver = pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados.primarios.seeds").replace("{0}", request.getParameter(Constants.ID_OBSERVATORIO)).replace("{1}", request.getParameter(Constants.ID_EX_OBS)).replace("{2}", request.getParameter(Constants.ID_CARTUCHO));
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO2);
        } catch (Exception e) {
            Logger.putLog("Excepcion al borrar la ejecucion de la semilla del Observatorio.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward showDeleteSeedExecution(HttpServletRequest request, ActionMapping mapping) {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            request.setAttribute(Constants.SEMILLA_FORM, SemillaDAO.getSeedById(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA))));
            return mapping.findForward(Constants.CONFIRMACION_DELETE);
        } catch (Exception e) {
            Logger.putLog("Exceción al recuperar los datos de la semilla.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward deleteSeedConfirmation(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
                request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
            }
            final String idSemilla = request.getParameter(Constants.SEMILLA);
            c = DataBaseManager.getConnection();
            final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
            request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
        } catch (Exception e) {
            Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return mapping.findForward(Constants.CONFIRMACION2);
    }

    public ActionForward deleteCrawlerSeed(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            String idSemilla = request.getParameter(Constants.SEMILLA);
            String confirmacion = request.getParameter(Constants.CONFIRMACION);
            c = DataBaseManager.getConnection();
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
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
    }

    public ActionForward throwCrawlerSeedExecution(ActionMapping mapping, HttpServletRequest request) throws Exception {
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

    private void lanzarRastreo(Connection c, String idCrawling) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, idCrawling);
        dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));

        // Cargamos los dominios introducidos en el archivo de semillas
        int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
        dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, false));

        dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, true));
        dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
        dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));

        dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) dcrForm.getId_rastreo()));

        if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
            dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
        }

        DatosForm userData = LoginDAO.getUserData(c, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"));

        Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, null, Long.valueOf(userData.getId()));

        CrawlerJob crawlerJob = new CrawlerJob();
        crawlerJob.makeCrawl(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null));
    }

    public ActionForward getSeeds(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        SemillaForm semillaForm = (SemillaForm) form;

        Long idObservatoryExecution = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            PropertiesManager pmgr = new PropertiesManager();

            int numResultA = ObservatorioDAO.countResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);
            request.setAttribute(Constants.OBSERVATORY_SEED_LIST, ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResultA, pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"), pagina, Constants.PAG_PARAM));
        } catch (Exception e) {
            Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.OBSERVATORY_SEED_LIST);
    }


    public ActionForward getFulfilledObservatories(ActionMapping mapping, HttpServletRequest request) throws Exception {

        Long observatoryId = Long.valueOf(request.getParameter(Constants.OBSERVATORY_ID));

        Connection c = null;
        //Para mostrar todos los Rastreos del Sistema
        try {
            c = DataBaseManager.getConnection();

            int numResult = ObservatorioDAO.countFulfilledObservatories(c, observatoryId);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(c, observatoryId, (pagina - 1), null));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
    }

    public ActionForward getAnnexes(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));

            final Long idOperation = System.currentTimeMillis();

            AnnexUtils.createAnnex(request, idObsExecution, idOperation);
            AnnexUtils.createAnnex2Ev(CrawlerUtils.getResources(request), idObsExecution, idOperation);

            final PropertiesManager pmgr = new PropertiesManager();
            final String zipPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexos.zip";
            ZipUtils.generateZip(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation.toString(), zipPath, true);

            CrawlerUtils.returnFile(zipPath, response, "application/zip", true);

            FileUtils.deleteDir(new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation));

            return null;
        } catch (Exception e) {
            Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            ActionErrors errors = new ActionErrors();
            errors.add("usuarioDuplicado", new ActionMessage("data.export"));
            saveErrors(request, errors);
            return getFulfilledObservatories(mapping, request);
        }
    }

}
