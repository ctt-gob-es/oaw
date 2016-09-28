package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.NuevaSemillaWebsForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class NuevaSemillaWebsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try (Connection c = DataBaseManager.getConnection()) {
            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        if (request.getParameter(Constants.INICIAL) == null && request.getAttribute(Constants.INICIAL) == null) {
            try (Connection c = DataBaseManager.getConnection()) {
                if (CrawlerUtils.hasAccess(request, "web.list.seed")) {
                    NuevaSemillaWebsForm nuevaSemillaWebsForm = (NuevaSemillaWebsForm) form;
                    if (isCancelled(request)) {
                        return (mapping.findForward(Constants.VOLVER));
                    }

                    ActionErrors errors = nuevaSemillaWebsForm.validate(mapping, request);
                    if (errors.isEmpty()) {
                        if (SemillaDAO.existSeed(c, nuevaSemillaWebsForm.getNombreSemilla(), Constants.ID_LISTA_ALL)) {
                            errors.add("nombreDuplicado", new ActionMessage("mensaje.error.nombre.semilla.duplicado"));
                            saveErrors(request, errors);
                            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
                        }

                        List<String> validUrls = SeedUtils.getValidUrls(nuevaSemillaWebsForm.getTa1(), true);
                        if (validUrls.size() == 0) {
                            errors.add("usuarioDuplicado", new ActionMessage("unaUrl.valida"));
                            saveErrors(request, errors);
                            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
                        }

                        SemillaDAO.insertList(c, Constants.ID_LISTA_SEMILLA, nuevaSemillaWebsForm.getNombreSemilla(), SeedUtils.getSeedUrlsForDatabase(validUrls), nuevaSemillaWebsForm.getCategoria().getId(), null, null);

                        ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.semilla.generada", "volver.listado.semillas");
                        return mapping.findForward(Constants.EXITO);
                    } else {
                        ActionForward forward = new ActionForward();
                        forward.setPath(mapping.getInput());
                        forward.setRedirect(true);
                        saveErrors(request.getSession(), errors);
                        return (forward);
                    }
                } else {
                    return mapping.findForward(Constants.NO_PERMISSION);
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            }
        } else {
            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
        }
    }

}