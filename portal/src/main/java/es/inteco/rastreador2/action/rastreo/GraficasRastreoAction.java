/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Locale;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class GraficasRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
                return getGraphic(request, response);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ActionForward getGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        String executionId = request.getParameter(Constants.ID);
        String crawlerId = request.getParameter(Constants.ID_RASTREO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + crawlerId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
            String title = "";
            if (graphic.equals(Constants.CRAWLER_GRAPHIC_TOTAL_RESULTS)) {
                title = getResources(request).getMessage(getLocale(request), "chart.intav.total.results");
            } else if (graphic.equals(Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS)) {
                title = getResources(request).getMessage(getLocale(request), "chart.intav.priority.warnings");
            }
            CrawlerUtils.returnFile(response, path + title + ".jpg", "image/jpeg", false);
        }

        return null;
    }

}
