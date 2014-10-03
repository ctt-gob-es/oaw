package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.action.observatorio.SemillasObservatorioAction;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class SeedCategoriesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
        request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_CATEGORIES);

        try {
            if (CrawlerUtils.hasAccess(request, "categories.seed")) {
                String action = request.getParameter(Constants.ACTION);
                if (action.equals(Constants.SEED_CATEGORIES)) {
                    return getSeedCategories(mapping, request);
                } else if (action.equals(Constants.DELETE_CATEGORY_CONFIRMATION)) {
                    return deleteCategoryConfirmation(mapping, request);
                } else if (action.equals(Constants.DELETE_CATEGORY)) {
                    return deleteCategory(mapping, request);
                } else if (action.equals(Constants.NEW_SEED_CATEGORY)) {
                    return newSeedCategory(mapping, request);
                } else if (action.equals(Constants.ADD_SEED_CATEGORY)) {
                    return addSeedCategory(mapping, form, request);
                } else if (action.equals(Constants.EDIT_SEED_CATEGORY)) {
                    return editSeedCategory(mapping, request);
                } else if (action.equals(Constants.UPDATE_SEED_CATEGORY)) {
                    return updateSeedCategory(mapping, form, request);
                } else if (action.equals(Constants.VIEW_SEED_CATEGORY)) {
                    return viewSeedCategory(mapping, form, request);
                } else if (action.equals(Constants.DELETE_CATEGORY_SEED_CONFIRMATION)) {
                    return deleteSeedConfirmation(mapping, request);
                } else if (action.equals(Constants.DELETE_CATEGORY_SEED)) {
                    return deleteSeed(mapping, request);
                } else if (action.equals(Constants.NEW_CATEGORY_SEED)) {
                    return newCategorySeed(mapping, request);
                } else if (action.equals(Constants.EDIT_CATEGORY_SEED)) {
                    return editCategorySeed(mapping, request);
                } else if (action.equals(Constants.ADD_CATEGORY_SEED)) {
                    return addCategorySeed(mapping, form, request);
                } else if (action.equals(Constants.UPDATE_CATEGORY_SEED)) {
                    return updateCategorySeed(mapping, form, request);
                } else if (action.equals(Constants.GET_CATEGORY_SEEDS_FILE)) {
                    return getCategorySeedsFile(request, response);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, SeedCategoriesAction.class);
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    private ActionForward getSeedCategories(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();

            int numResult = SemillaDAO.countSeedCategories(c);
            int page = Pagination.getPage(request, Constants.PAG_PARAM);

            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, (page - 1)));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.SEED_CATEGORIES);
    }

    private ActionForward deleteCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();
            Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
            SemillaDAO.deleteSeedCategory(c, idCategory);

            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.eliminada");
            String volver = pmgr.getValue("returnPaths.properties", "volver.listado.categorias.semilla");
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private ActionForward deleteCategoryConfirmation(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            String idCategoria = request.getParameter(Constants.ID_CATEGORIA);
            c = DataBaseManager.getConnection();
            List<ObservatorioForm> observatoryFormList = new ArrayList<ObservatorioForm>();
            if (idCategoria != null) {
                observatoryFormList = ObservatorioDAO.getObservatoriesFromCategory(c, idCategoria);
            }
            request.setAttribute(Constants.OBSERVATORY_SEED_LIST, observatoryFormList);
        } catch (Exception e) {
            Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return mapping.findForward(Constants.DELETE_CATEGORY_CONFIRMATION);
    }

    private ActionForward newSeedCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
        request.setAttribute(Constants.ACTION, Constants.ADD_SEED_CATEGORY);
        return mapping.findForward(Constants.SEED_CATEGORY_FORM);
    }

    private ActionForward editSeedCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();
            if (request.getParameter(Constants.ID_CATEGORIA) != null) {
                Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
                CategoriaForm categoriaForm = SemillaDAO.getSeedCategory(c, idCategory);

                int numResult = SemillaDAO.countSeedsByCategory(c, idCategory, new SemillaForm());
                int page = Pagination.getPage(request, Constants.PAG_PARAM);

                categoriaForm.setSeeds(SemillaDAO.getSeedsByCategory(c, idCategory, page - 1, new SemillaForm()));
                request.setAttribute(Constants.CATEGORIA_FORM, categoriaForm);
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        request.setAttribute(Constants.ACTION, Constants.UPDATE_SEED_CATEGORY);
        return mapping.findForward(Constants.SEED_CATEGORY_FORM);
    }

    private ActionForward addSeedCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        if (!isCancelled(request)) {
            Connection c = null;
            PropertiesManager pmgr = new PropertiesManager();
            CategoriaForm categoriaForm = (CategoriaForm) form;

            try {
                request.setAttribute(Constants.ACTION, Constants.ADD_SEED_CATEGORY);
                ActionErrors errors = categoriaForm.validate(mapping, request);
                if (errors.isEmpty()) {
                    if (categoriaForm.getFileSeeds() == null || StringUtils.isEmpty(categoriaForm.getFileSeeds().getFileName()) ||
                            (categoriaForm.getFileSeeds().getFileName().endsWith(".xml") &&
                                    categoriaForm.getFileSeeds().getFileSize() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size")))) {

                        c = DataBaseManager.getConnection();
                        Long idSeedCategory = SemillaDAO.createSeedCategory(c, categoriaForm.getName());

                        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada", categoriaForm.getName());
                        String volver = pmgr.getValue("returnPaths.properties", "volver.listado.categorias.semilla");

                        if (categoriaForm.getFileSeeds().getFileData().length > 0) {
                            try {
                                List<SemillaForm> seeds = SeedUtils.getSeedsFromFile(categoriaForm.getFileSeeds().getInputStream());
                                SemillaDAO.saveSeedsCategory(c, seeds, idSeedCategory.toString());
                            } catch (Exception e) {
                                Logger.putLog("Error en la creación de semillas asociadas al observatorio", SeedCategoriesAction.class, Logger.LOG_LEVEL_ERROR, e);
                                mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.error.fichero.semillas", categoriaForm.getName());
                            }
                        }

                        request.setAttribute("mensajeExito", mensaje);
                        request.setAttribute("accionVolver", volver);
                        return mapping.findForward(Constants.EXITO);
                    } else if (!categoriaForm.getFileSeeds().getFileName().endsWith(".xml")) {
                        errors.add("xmlFile", new ActionMessage("no.xml.file"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    } else if (categoriaForm.getFileSeeds().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
                        errors.add("xmlFile", new ActionMessage("xml.size.error"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }
                } else {
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            return mapping.findForward(Constants.GET_SEED_CATEGORIES);
        }
        return null;
    }

    private ActionForward updateSeedCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        if (!isCancelled(request)) {
            Connection c = null;

            CategoriaForm categoriaForm = (CategoriaForm) form;

            try {
                PropertiesManager pmgr = new PropertiesManager();
                c = DataBaseManager.getConnection();

                SemillaDAO.updateSeedCategory(c, categoriaForm);

                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.editada", categoriaForm.getName());
                String volver = pmgr.getValue("returnPaths.properties", "volver.listado.categorias.semilla");

                if (categoriaForm.getFileSeeds().getFileData().length > 0) {
                    try {
                        //Semillas que recuperamos del fichero
                        List<SemillaForm> seeds = SeedUtils.getSeedsFromFile(categoriaForm.getFileSeeds().getInputStream());
                        //Semillas de la categoria
                        List<SemillaForm> oldSeeds = SemillaDAO.getSeedsByCategory(c, Long.parseLong(categoriaForm.getId()), Constants.NO_PAGINACION, new SemillaForm());
                        //Comparamos las semillas (x url)
                        compareSeeds(c, categoriaForm.getId(), seeds, oldSeeds);
                    } catch (Exception e) {
                        Logger.putLog("Error en la creación de semillas asociadas al observatorio", SeedCategoriesAction.class, Logger.LOG_LEVEL_ERROR, e);
                        mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.error.fichero.semillas", categoriaForm.getName());
                    }
                }

                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
                return mapping.findForward(Constants.EXITO);
            } catch (Exception e) {
                throw e;
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            return mapping.findForward(Constants.GET_SEED_CATEGORIES);
        }
    }

    private static void compareSeeds(Connection c, String idCategory, List<SemillaForm> newSeeds, List<SemillaForm> oldList) throws Exception {

        List<SemillaForm> repitSeeds = new ArrayList<SemillaForm>();
        List<SemillaForm> insertSeeds = new ArrayList<SemillaForm>();

        for (SemillaForm newSemillaForm : newSeeds) {
            boolean found = false;
            for (SemillaForm oldSemillaForm : oldList) {
                if (newSemillaForm.getListaUrlsString() != null && oldSemillaForm.getListaUrlsString() != null && newSemillaForm.getListaUrlsString().equalsIgnoreCase(oldSemillaForm.getListaUrlsString())) {
                    newSemillaForm.setId(oldSemillaForm.getId());
                    repitSeeds.add(newSemillaForm);
                    oldList.remove(oldSemillaForm);
                    found = true;
                    break;
                }
            }

            if (!found) {
                insertSeeds.add(newSemillaForm);
            }
        }
        List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromCategory(c, idCategory);
        //Añadimos las nuevas Semillas y los nuevos rastreos a los observatorios que correspondan
        insertSeedsFromXml(c, insertSeeds, idCategory, observatoryFormList);
        //Modificamos las que han cambiado
        updateSeedsFromXml(c, repitSeeds, observatoryFormList);
        //Borramos las semillas que no tenemos y los rastreos asociados con resultados y demás
        deleteSeedsFromXml(c, oldList);
    }

    private static void insertSeedsFromXml(Connection c, List<SemillaForm> insertSeeds, String idCategory, List<ObservatorioForm> observatoryFormList) throws Exception {
        if (insertSeeds != null && !insertSeeds.isEmpty()) {
            SemillaDAO.saveSeedsCategory(c, insertSeeds, idCategory);
            for (SemillaForm semillaForm : insertSeeds) {
                for (ObservatorioForm observatorioForm : observatoryFormList) {
                    if (observatorioForm.getCategoria() != null && Arrays.asList(observatorioForm.getCategoria()).contains(idCategory)) {
                        InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                        ObservatorioDAO.putDataToInsert(insertarRastreoForm, observatorioForm);
                        insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + semillaForm.getNombre());
                        insertarRastreoForm.setId_semilla(SemillaDAO.getIdList(c, semillaForm.getNombre(), Long.parseLong(idCategory)));
                        insertarRastreoForm.setId_observatorio(observatorioForm.getId());
                        insertarRastreoForm.setActive(true);
                        RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
                    }
                }
            }
        }
    }

    private static void updateSeedsFromXml(Connection c, List<SemillaForm> repitSeeds, List<ObservatorioForm> observatoryFormList) throws Exception {
        if (repitSeeds != null && !repitSeeds.isEmpty()) {
            SemillaDAO.updateCategorySeeds(c, repitSeeds);
            for (SemillaForm semillaForm : repitSeeds) {
                for (ObservatorioForm observatorioForm : observatoryFormList) {
                    Long crawlerId = RastreoDAO.getCrawlerFromSeedAndObservatory(c, semillaForm.getId(), observatorioForm.getId());
                    RastreoDAO.updateCrawlerName(c, observatorioForm.getNombre() + "-" + semillaForm.getNombre(), crawlerId);
                }
            }
        }
    }

    private static void deleteSeedsFromXml(Connection c, List<SemillaForm> oldList) throws Exception {
        if (oldList != null && !oldList.isEmpty()) {
            for (SemillaForm semillaForm : oldList) {
                //Al borrar la semilla ya se eliminan automáticamente los rastreos asociados y los resultados
                SemillaDAO.deleteCategorySeed(c, String.valueOf(semillaForm.getId()));
            }
        }
    }

    private ActionForward viewSeedCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        Connection c = null;

        SemillaForm searchForm = (SemillaForm) form;

        try {
            c = DataBaseManager.getConnection();
            if (request.getParameter(Constants.ID_CATEGORIA) != null) {
                Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
                CategoriaForm categoriaForm = SemillaDAO.getSeedCategory(c, idCategory);

                int numResult = SemillaDAO.countSeedsByCategory(c, idCategory, searchForm);
                int page = Pagination.getPage(request, Constants.PAG_PARAM);

                categoriaForm.setSeeds(SemillaDAO.getSeedsByCategory(c, idCategory, page - 1, searchForm));
                request.setAttribute(Constants.CATEGORIA_FORM, categoriaForm);
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.VIEW_SEED_CATEGORY);
    }

    private ActionForward deleteSeedConfirmation(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;
        try {
            String idSemilla = request.getParameter(Constants.ID_SEMILLA);
            c = DataBaseManager.getConnection();
            List<ObservatorioForm> observatoryFormList = new ArrayList<ObservatorioForm>();
            if (idSemilla != null) {
                observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
            }
            request.setAttribute(Constants.OBSERVATORY_SEED_LIST, observatoryFormList);
            SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
            request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
        } catch (Exception e) {
            Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.DELETE_CATEGORY_SEED_CONFIRMATION);
    }

    private ActionForward deleteSeed(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;
        Connection conn = null;
        try {

            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();
            conn = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));

            String idCategory = request.getParameter(Constants.ID_CATEGORIA);
            String idSemilla = request.getParameter(Constants.ID_SEMILLA);

            SemillaDAO.deleteCategorySeed(c, idSemilla);

            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.borrada");
            String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", idCategory);
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            if (conn != null) {
                conn.rollback();
            }
            Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
        }
    }

    private ActionForward newCategorySeed(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            if (isCancelled(request)) {
                return mapping.findForward(Constants.EDIT_SEED_CATEGORY);
            }
            c = DataBaseManager.getConnection();
            request.setAttribute(Constants.ACTION, Constants.ADD_CATEGORY_SEED);
            return mapping.findForward(Constants.CATEGORY_SEED_FORM);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private ActionForward editCategorySeed(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;

        if (isCancelled(request)) {
            return mapping.findForward(Constants.EDIT_SEED_CATEGORY);
        }

        try {
            c = DataBaseManager.getConnection();

            String idSeed = request.getParameter(Constants.ID_SEMILLA);
            SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSeed));
            semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace(";", "\r\n"));
            request.setAttribute(Constants.SEMILLA_FORM, semillaForm);

            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
            request.setAttribute(Constants.ACTION, Constants.UPDATE_CATEGORY_SEED);
            return mapping.findForward(Constants.CATEGORY_SEED_FORM);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private ActionForward addCategorySeed(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        SemillaForm semillaForm = (SemillaForm) form;

        if (!isCancelled(request)) {
            Connection c = null;

            try {
                PropertiesManager pmgr = new PropertiesManager();
                c = DataBaseManager.getConnection();

                semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace("\r\n", ";"));
                Long idSeed = SemillaDAO.insertList(c, 4, semillaForm.getNombre(), semillaForm.getListaUrlsString(), semillaForm.getCategoria().getId(), semillaForm.getAcronimo(), semillaForm.getDependencia());

                List<ObservatorioForm> observatoryIds = ObservatorioDAO.getObservatoriesFromCategory(c, semillaForm.getCategoria().getId());
                for (ObservatorioForm observatorioForm : observatoryIds) {
                    if (observatorioForm.getCategoria() != null && Arrays.asList(observatorioForm.getCategoria()).contains(semillaForm.getCategoria().getId())) {
                        InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                        ObservatorioDAO.putDataToInsert(insertarRastreoForm, observatorioForm);
                        insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + semillaForm.getNombre());
                        insertarRastreoForm.setId_semilla(idSeed);
                        insertarRastreoForm.setId_observatorio(observatorioForm.getId());
                        insertarRastreoForm.setActive(true);
                        RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
                    }
                }

                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.semilla");
                String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", semillaForm.getCategoria().getId());
                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
                return mapping.findForward(Constants.EXITO);
            } catch (Exception e) {
                throw e;
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            ActionForward forward = new ActionForward(mapping.findForward(Constants.EDIT_SEED_CATEGORY));
            forward.setPath(forward.getPath() + "&" + Constants.ID_CATEGORIA + "=" + semillaForm.getCategoria().getId());
            forward.setRedirect(true);
            return forward;
        }
    }

    private ActionForward updateCategorySeed(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        SemillaForm semillaForm = (SemillaForm) form;

        if (!isCancelled(request)) {
            Connection c = null;

            try {
                PropertiesManager pmgr = new PropertiesManager();
                c = DataBaseManager.getConnection();

                semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace("\r\n", ";"));
                SemillaDAO.editSeed(c, semillaForm);

                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.editada.semilla");
                String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", semillaForm.getCategoria().getId());
                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
                return mapping.findForward(Constants.EXITO);
            } catch (Exception e) {
                throw e;
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            ActionForward forward = new ActionForward(mapping.findForward(Constants.EDIT_SEED_CATEGORY));
            forward.setPath(forward.getPath() + "&" + Constants.ID_CATEGORIA + "=" + semillaForm.getCategoria().getId());
            forward.setRedirect(true);
            return forward;
        }
    }

    private ActionForward getCategorySeedsFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();

            Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
            List<SemillaForm> seeds = SemillaDAO.getSeedsByCategory(c, idCategory, Constants.NO_PAGINACION, new SemillaForm());
            SeedUtils.writeFileToResponse(response, seeds);
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }
}
