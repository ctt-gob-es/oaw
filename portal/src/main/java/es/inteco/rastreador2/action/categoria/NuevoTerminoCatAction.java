package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.NuevoTerminoCatForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.NormalizarCats;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NuevoTerminoCatAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "new.term")) {

                NuevoTerminoCatForm nuevoTerminoCatForm = (NuevoTerminoCatForm) form;
                HttpSession sesion = request.getSession();

                String id_categoria = request.getParameter(Constants.ID_CATEGORIA);

                if (isCancelled(request)) {
                    ActionForward oldForward = mapping.findForward(Constants.VOLVER_CARGA);
                    ActionForward newForward = new ActionForward();
                    newForward.setPath(oldForward.getPath() + "?idcat=" + id_categoria);
                    newForward.setRedirect(true);
                    return (newForward);
                }

                nuevoTerminoCatForm.setId_categoria(Integer.parseInt(id_categoria));
                sesion.setAttribute(Constants.ID_CATEGORIA_TERMINO, id_categoria);

                //comprobamos de donde viene
                String accionnuevo = request.getParameter(Constants.ACCION);
                if (accionnuevo == null || accionnuevo.trim().isEmpty()) {
                    //le mandamos al formulario
                    return mapping.findForward(Constants.EXITO_NUEVO);
                } else {
                    final ActionErrors errors = nuevoTerminoCatForm.validate(mapping, request);
                    if (errors != null && !errors.isEmpty()) {
                        ActionForward forward = new ActionForward();
                        forward.setPath(mapping.getInput() + "?" + Constants.ID_CATEGORIA + "=" + id_categoria + "&");
                        forward.setRedirect(true);
                        saveErrors(request.getSession(), errors);
                        return (forward);
                    }

                    //Comprobamos que el término usa caracteres correctos
                    ComprobadorCaracteres cc = new ComprobadorCaracteres(nuevoTerminoCatForm.getTermino());
                    if (!cc.isNombreValido()) {
                        errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }
                    String accion = request.getParameter(Constants.ACCION);
                    nuevoTerminoCatForm.setAccion(accion);
                    PropertiesManager pmgr = new PropertiesManager();
                    Connection c = DataBaseManager.getConnection();
                    try {
                        int id_termino = CategoriasDAO.existTerm(c, nuevoTerminoCatForm.getTermino());
                        //Comprobamos en que categorías existe el término
                        List<Integer> ids_c = new ArrayList<Integer>();
                        if (id_termino != -1) {
                            ids_c = CategoriasDAO.termCategories(c, id_termino);
                        }

                        //En caso de que se quiera meter el mismo término en
                        //la misma categoría
                        Iterator<Integer> it = ids_c.iterator();
                        while (it.hasNext()) {
                            int i = it.next();
                            if (i == nuevoTerminoCatForm.getId_categoria()) {
                                errors.add("errorObligatorios", new ActionMessage("termino.duplicado"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }
                        }
                        String[] terminosArray = new String[1];
                        float[] pesosArray = new float[1];

                        terminosArray[0] = nuevoTerminoCatForm.getTermino();
                        pesosArray[0] = Float.parseFloat(nuevoTerminoCatForm.getPorcentaje());

                        CategoriasDAO.insertTerm(c, Integer.parseInt(id_categoria), terminosArray, pesosArray);

                        NormalizarCats.normaliza(Integer.parseInt(id_categoria));

                    } catch (Exception e) {
                        Logger.putLog("Exception: ", NuevoTerminoCatAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    } finally {
                        DataBaseManager.closeConnection(c);
                    }

                    //metemos el idcategoria en sesion para que lo recupere el modificarCategoria.do
                    String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.termino.categoria.insertado");
                    String volver = pmgr.getValue("returnPaths.properties", "volver.modificar.categoria").replace("{0}", id_categoria);
                    request.setAttribute("mensajeExito", mensaje);
                    request.setAttribute("accionVolver", volver);
                    return mapping.findForward(Constants.EXITO);
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