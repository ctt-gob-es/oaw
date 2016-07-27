package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.NuevaCategoriaForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.NormalizarCats;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NuevaCategoriaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            if (CrawlerUtils.hasAccess(request, "new.category")) {

                NuevaCategoriaForm nuevaCategoriaForm = (NuevaCategoriaForm) form;

                String accionF = request.getParameter(Constants.ACTION_FOR);
                if (accionF == null || accionF.trim().equals("")) {
                    return mapping.findForward(Constants.VOLVER);
                }

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                ActionErrors errors = nuevaCategoriaForm.validate(mapping, request);
                if (errors != null && !errors.isEmpty()) {
                    ActionForward forward = new ActionForward();
                    forward.setPath(mapping.getInput());
                    forward.setRedirect(true);
                    saveErrors(request.getSession(), errors);
                    return (forward);
                }

                if ((nuevaCategoriaForm.getTermino1().equals("") && !nuevaCategoriaForm.getPeso1().equals("")) ||
                        (nuevaCategoriaForm.getTermino2().equals("") && !nuevaCategoriaForm.getPeso2().equals("")) ||
                        (nuevaCategoriaForm.getTermino3().equals("") && !nuevaCategoriaForm.getPeso3().equals("")) ||
                        (nuevaCategoriaForm.getTermino4().equals("") && !nuevaCategoriaForm.getPeso4().equals(""))) {
                    errors.add("errorPassObligatorios", new ActionMessage("peso.incompleto"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }

                //Comprobamos que el término usa caracteres correctos
                ComprobadorCaracteres cc1 = new ComprobadorCaracteres(nuevaCategoriaForm.getTermino1());
                ComprobadorCaracteres cc2 = new ComprobadorCaracteres(nuevaCategoriaForm.getTermino2());
                ComprobadorCaracteres cc3 = new ComprobadorCaracteres(nuevaCategoriaForm.getTermino3());
                ComprobadorCaracteres cc4 = new ComprobadorCaracteres(nuevaCategoriaForm.getTermino4());

                if (!cc1.isNombreValido() || !cc2.isNombreValido() || !cc3.isNombreValido() || !cc4.isNombreValido()) {
                    errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }

                //Comprobamos que la categoría usa caracteres correctos
                ComprobadorCaracteres cc = new ComprobadorCaracteres(nuevaCategoriaForm.getNombre());
                if (!cc.isNombreValido()) {
                    errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }
                nuevaCategoriaForm.setNombre(cc.espacios());

                List<String> terminos = new ArrayList<>();
                List<Float> pesos = new ArrayList<>();

                if (!nuevaCategoriaForm.getTermino1().equals("")) {
                    terminos.add(nuevaCategoriaForm.getTermino1());
                }
                if (!nuevaCategoriaForm.getTermino2().equals("")) {
                    terminos.add(nuevaCategoriaForm.getTermino2());
                }
                if (!nuevaCategoriaForm.getTermino3().equals("")) {
                    terminos.add(nuevaCategoriaForm.getTermino3());
                }
                if (!nuevaCategoriaForm.getTermino4().equals("")) {
                    terminos.add(nuevaCategoriaForm.getTermino4());
                }

                if (!nuevaCategoriaForm.getPeso1().equals("")) {
                    pesos.add(Float.parseFloat(nuevaCategoriaForm.getPeso1()));
                }
                if (!nuevaCategoriaForm.getPeso2().equals("")) {
                    pesos.add(Float.parseFloat(nuevaCategoriaForm.getPeso2()));
                }
                if (!nuevaCategoriaForm.getPeso3().equals("")) {
                    pesos.add(Float.parseFloat(nuevaCategoriaForm.getPeso3()));
                }
                if (!nuevaCategoriaForm.getPeso4().equals("")) {
                    pesos.add(Float.parseFloat(nuevaCategoriaForm.getPeso4()));
                }

                String[] terminosArray = new String[terminos.size()];
                float[] pesosArray = new float[pesos.size()];

                Iterator<String> it01 = terminos.iterator();
                int conta = 0;
                while (it01.hasNext()) {
                    terminosArray[conta] = it01.next();
                    conta++;
                }

                Iterator<Float> it02 = pesos.iterator();
                conta = 0;
                while (it02.hasNext()) {
                    pesosArray[conta] = it02.next();
                    conta++;
                }

                //Comprobamos que no se incluyen dos términos iguales
                boolean dosIguales = false;
                for (int i = 0; i < terminosArray.length; i++) {
                    for (int j = 0; j < terminosArray.length; j++) {
                        if ((terminosArray[i].equals(terminosArray[j])) && (i != j) && (!terminosArray[i].equals(""))) {
                            dosIguales = true;
                        }
                    }
                }
                if (dosIguales) {
                    errors.add("errorObligatorios", new ActionMessage("terminos.iguales"));
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }
                PropertiesManager pmgr = new PropertiesManager();
                Connection c = DataBaseManager.getConnection();
                try {
                    //Comprobar que no existe la categoría
                    if (CategoriasDAO.existCategory(c, nuevaCategoriaForm.getNombre())) {
                        errors.add("errorObligatorios", new ActionMessage("categoria.duplicada"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        int id_c = CategoriasDAO.insertrCategory(c, nuevaCategoriaForm, terminosArray, pesosArray);
                        NormalizarCats.normaliza(id_c);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", NuevaCategoriaAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
                }

                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.insertada");
                String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.categorias");
                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
                return mapping.findForward(Constants.EXITO);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }
}