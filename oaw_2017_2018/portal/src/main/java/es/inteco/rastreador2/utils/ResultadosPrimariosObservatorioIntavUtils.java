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
package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class ResultadosPrimariosObservatorioIntavUtils {

    //GENERATE GRAPHIC METHODS
    private static int x = 0;
    private static int y = 0;

    static {
        PropertiesManager pmgr = new PropertiesManager();
        x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
        y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
    }

    private ResultadosPrimariosObservatorioIntavUtils() {
    }

    public static void getGlobalAccessibilityLevelAllocationSegmentGraphic(final MessageResources messageResources,
                                                                           final List<ObservatoryEvaluationForm> pageExecutionList, final String title, final String filePath, final String noDataMess) throws IOException {
        final PropertiesManager pmgr = new PropertiesManager();
        final File file = new File(filePath);

        final Map<String, Integer> result = getResultsByLevel(pageExecutionList);

        if (!file.exists()) {
            final int total = result.get(Constants.OBS_A) + result.get(Constants.OBS_AA) + result.get(Constants.OBS_NV);

            final DefaultPieDataset dataSet = new DefaultPieDataset();

            dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_NV, messageResources), result.get(Constants.OBS_NV));
            dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_A, messageResources), result.get(Constants.OBS_A));
            dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_AA, messageResources), result.get(Constants.OBS_AA));

            GraphicsUtils.createPieChart(dataSet, title, messageResources.getMessage("observatory.graphic.page.number"), total, filePath, noDataMess, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
        }
    }

    public static Map<String, Integer> getResultsByLevel(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
        final Map<String, Integer> globalResult = new HashMap<>();
        globalResult.put(Constants.OBS_NV, 0);
        globalResult.put(Constants.OBS_A, 0);
        globalResult.put(Constants.OBS_AA, 0);

        final Map<String, List<ObservatoryEvaluationForm>> tempResult = ResultadosAnonimosObservatorioIntavUtils.getPagesByType(observatoryEvaluationList);
        for (Map.Entry<String, List<ObservatoryEvaluationForm>> tempResultEntry : tempResult.entrySet()) {
            globalResult.put(tempResultEntry.getKey(), tempResultEntry.getValue().size());
        }

        return globalResult;
    }

    public static void getScoreByPageGraphic(final MessageResources request,
                                             final List<ObservatoryEvaluationForm> pageExecutionList, final String title, final String filePath, final String noDataMess) throws IOException {
        final File file = new File(filePath);
        //Si no existe la gráfica, la creamos
        if (!file.exists()) {
            GraphicsUtils.createBarPageByLevelChart(pageExecutionList, title, "", "", filePath, noDataMess, request, x, y);
        }
    }

    public static Map<String, BigDecimal> getScoresByPage(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList) {
        TreeMap<String, BigDecimal> result = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Pattern pattern = Pattern.compile("[a-zA-Z ]*(\\d+)");

                Integer number1 = null;
                Matcher matcher1 = pattern.matcher(o1);
                if (matcher1.find() && StringUtils.isNumeric(matcher1.group(1))) {
                    number1 = Integer.parseInt(matcher1.group(1));
                }

                Integer number2 = null;
                Matcher matcher2 = pattern.matcher(o2);
                if (matcher2.find() && StringUtils.isNumeric(matcher2.group(1))) {
                    number2 = Integer.parseInt(matcher2.group(1));
                }

                return (number1 != null) ? number1.compareTo(number2) : ((number2 != null) ? -1 : 0);
            }
        });
        int pageCounter = 0;
        for (ObservatoryEvaluationForm evaluationForm : pageExecutionList) {
            result.put(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", ++pageCounter), evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1));
        }
        return result;
    }

}