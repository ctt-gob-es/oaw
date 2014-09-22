package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.rastreo.AmpliarRastreoForm;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.LeerXML;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class AmpliarRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            AmpliarRastreoForm ampliarRastreoForm = (AmpliarRastreoForm) form;
            PropertiesManager pmgr = new PropertiesManager();

            HttpSession sesion = request.getSession();
            if (ActionUtils.redirectToLogin(request)) {
                return mapping.findForward(Constants.LOGIN);
            }

            if (request.getParameter(Constants.OPCION_AMPLIAR) != null) {
                ampliarRastreoForm.setOpcion_ampliar(request.getParameter(Constants.OPCION_AMPLIAR));
                ampliarRastreoForm.setCod_rastreo(request.getParameter(Constants.COD_RASTREO));
                ampliarRastreoForm.setUser(request.getParameter(Constants.USER_AMPLIAR));
                ampliarRastreoForm.setPass(request.getParameter(Constants.PASS_AMPLIAR));
                sesion.setAttribute(Constants.OPCION_AMPLIAR, ampliarRastreoForm.getOpcion_ampliar());
                sesion.setAttribute(Constants.COD_RASTREO, ampliarRastreoForm.getCod_rastreo());
                sesion.setAttribute(Constants.USER_AMPLIAR, ampliarRastreoForm.getUser());
                sesion.setAttribute(Constants.PASS_AMPLIAR, ampliarRastreoForm.getPass());
            } else {
                ampliarRastreoForm.setOpcion_ampliar((String) sesion.getAttribute(Constants.OPCION_AMPLIAR));
                ampliarRastreoForm.setCod_rastreo((String) sesion.getAttribute(Constants.COD_RASTREO));
                ampliarRastreoForm.setUser((String) sesion.getAttribute(Constants.USER_AMPLIAR));
                ampliarRastreoForm.setPass((String) sesion.getAttribute(Constants.PASS_AMPLIAR));
            }


            if (sesion.getAttribute(Constants.USER).equals(ampliarRastreoForm.getUser())) {

                File f = new File(pmgr.getValue(CRAWLER_PROPERTIES, "crawl.status.bolt.path"));

                try {
                    //Implementación del cerrojo
                    while (true) {
                        if (!f.exists()) {
                            f.createNewFile();
                            Logger.putLog("CERROJO ABIERTO", AmpliarRastreoForm.class, Logger.LOG_LEVEL_INFO);
                            LeerXML lxml = new LeerXML(pmgr.getValue(CRAWLER_PROPERTIES, "crawl.status.path"));
                            String[] valores = lxml.obtenStatus(ampliarRastreoForm.getCod_rastreo());

                            if (valores[0] != null) {
                                ampliarRastreoForm.setNombre(valores[0]);
                                ampliarRastreoForm.setEstado(valores[1]);
                                ampliarRastreoForm.setBajadas(valores[2]);
                                ampliarRastreoForm.setPid(valores[3]);
                                ampliarRastreoForm.setSlot(valores[4]);
                                if (valores[5].length() > 70) {
                                    ampliarRastreoForm.setUltima_url(valores[5].substring(0, 69));
                                } else {
                                    ampliarRastreoForm.setUltima_url(valores[5]);
                                }
                            }
                            Logger.putLog("CERROJO CERRADO", AmpliarRastreoForm.class, Logger.LOG_LEVEL_INFO);
                            break;

                        } else {
                            Thread.sleep(500);
                        }
                    }
                    return mapping.findForward(Constants.EXITO);

                } catch (Exception e) {
                    Logger.putLog("EXCEPTION: ", AmpliarRastreoForm.class, Logger.LOG_LEVEL_ERROR, e);
                    return mapping.findForward(Constants.NO_PERMISSION);
                } finally {
                    f.delete();
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