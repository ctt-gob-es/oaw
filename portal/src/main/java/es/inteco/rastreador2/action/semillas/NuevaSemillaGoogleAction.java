package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.NuevaSemillaGoogleForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GeneradorGoogle;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Set;
import java.util.StringTokenizer;

public class NuevaSemillaGoogleAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_GOOGLE);

        try (Connection c = DataBaseManager.getConnection()) {

            if (CrawlerUtils.hasAccess(request, "google.results.seed")) {
                NuevaSemillaGoogleForm nuevaSemillaGoogleForm = (NuevaSemillaGoogleForm) form;

                if (isCancelled(request)) {
                    if (request.getParameter(Constants.BOTON_SEMILLA_GOOGLE) != null) {
                        return (mapping.findForward(Constants.VOLVER_CARGA_MENU));
                    } else {
                        return (mapping.findForward(Constants.VOLVER));
                    }
                }

                request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));

                //Primera vez que se entra
                if (request.getParameter(Constants.CONTROL) == null) {
                    nuevaSemillaGoogleForm.setNombreSemilla("");
                    nuevaSemillaGoogleForm.setPaginas("2");
                    nuevaSemillaGoogleForm.setQuery("");
                    return mapping.findForward(Constants.VOLVER);
                }

                ActionErrors errors = nuevaSemillaGoogleForm.validate(mapping, request);
                if (errors == null || errors.isEmpty()) {
                    if (errors == null) {
                        errors = new ActionErrors();
                    }

                    if (SemillaDAO.existSeed(c, nuevaSemillaGoogleForm.getNombreSemilla(), Constants.ID_LISTA_ALL)) {
                        errors.add("nombreDuplicado", new ActionMessage("mensaje.error.nombre.semilla.duplicado"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }

                    StringTokenizer st = new StringTokenizer(nuevaSemillaGoogleForm.getQuery(), " ");
                    StringBuilder qu = new StringBuilder();
                    while (st.hasMoreTokens()) {
                        qu.append("+").append(st.nextToken());
                    }
                    //qu = qu.substring(1);
                    String query = qu.substring(1);

                    GeneradorGoogle gg = new GeneradorGoogle();

                    Set<String> s;
                    String listaUrls = "";
                    for (int i = 0; i < Integer.parseInt(nuevaSemillaGoogleForm.getPaginas()); i++) {
                        s = gg.busca(query, i);
                        for (int j = 0; j < s.size(); j++) {
                            if (listaUrls.isEmpty()) {
                                listaUrls = listaUrls + s.toArray(new String[s.size()])[j];
                            } else {
                                listaUrls = listaUrls + ";" + s.toArray(new String[s.size()])[j];
                            }
                        }
                    }
                    SemillaDAO.insertList(c, Constants.ID_LISTA_SEMILLA, nuevaSemillaGoogleForm.getNombreSemilla(), listaUrls, nuevaSemillaGoogleForm.getCategoria().getId(), null, null);

                    ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.semilla.generada", "volver.nueva.semilla.google");
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
    }

}