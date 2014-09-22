package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.database.export.SiteTranslationInformationForm;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ValidationLanguageForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import org.apache.struts.util.LabelValueBean;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class ResultadosAnonimosObservatorioMultilanguageUtils {

    //GENERATE GRAPHIC METHODS
    private static int x = 0;
    private static int y = 0;

    static {
        PropertiesManager pmgr = new PropertiesManager();
        x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
        y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
    }

    private ResultadosAnonimosObservatorioMultilanguageUtils() {
    }

    public static int generateGlobalGraphics(HttpServletRequest request, String filePath, String colors) throws Exception {
        return createCommonGraphics(request, filePath, Constants.COMPLEXITY_SEGMENT_NONE, colors);
    }

    public static int generateCategoryGraphics(HttpServletRequest request, String filePath, long idCategory, String colors) throws Exception {
        return createCommonGraphics(request, filePath, idCategory, colors);
    }

    public static int createCommonGraphics(HttpServletRequest request, String filePath, long idCategory, String colors) throws Exception {
        String executionId = request.getParameter(Constants.ID);

        Map<Integer, List<AnalysisForm>> results = getGlobalResultData(executionId, idCategory);
        String category = "";
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                category = ObservatorioDAO.getCategoryById(c, idCategory).getName();
            }
        } catch (Exception e) {
            Logger.putLog("Exception en createCommonGraphics", ResultadosAnonimosObservatorioMultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        List<AnalysisForm> pageExecutionHomeList = results.get(Constants.MULTILANGUAGE_HOME);
        List<AnalysisForm> pageExecutionInternalPageList = results.get(Constants.MULTILANGUAGE_INTERNAL_PAGES);

        if ((pageExecutionHomeList != null && !pageExecutionHomeList.isEmpty()) && (pageExecutionInternalPageList != null && !pageExecutionInternalPageList.isEmpty())) {
            if (!pageExecutionHomeList.isEmpty()) {
                List<SiteTranslationInformationForm> infoList = MultilanguageUtils.getPortalTraductionInformation(pageExecutionHomeList, true);

                String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.home");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.home", category);
                }
                String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.home.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_LANG_HOME_INFO, getLanguagesGraphic(request, title, file, pageExecutionHomeList, colors));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.home");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.home", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.home.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_HOME_VAL_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_TOTAL_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.home");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.declaration.home", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.home.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_HOME_DEC_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_DECLARATION_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.home");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.translation.home", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.home.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_HOME_TR_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_TRANSLATION_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.home");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.correct.link.home", category);
                }

                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.home.filename") + ".jpg";
                getTraductionPercentageLanguagesGraphic(request, title, file, infoList);
                request.setAttribute(Constants.MULTILANGUAGE_HOME_LINK_TR_INFO, infoList);
            }

            if (!pageExecutionInternalPageList.isEmpty()) {
                List<SiteTranslationInformationForm> infoList = MultilanguageUtils.getPortalTraductionInformation(pageExecutionInternalPageList, true);
                String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.inner");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.inner", category);
                }
                String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.inner.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_LANG_INPG_INFO, getLanguagesGraphic(request, title, file, pageExecutionInternalPageList, colors));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.inner");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.inner", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.inner.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_INPG_VAL_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_TOTAL_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.inner");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.declaration.inner", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.inner.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_INPG_DEC_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_DECLARATION_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.inner");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.translation.inner", category);
                }

                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.inner.filename") + ".jpg";
                request.setAttribute(Constants.MULTILANGUAGE_INPG_TR_INFO, getLanguagesValidityGraphic(request, title, file, infoList, Constants.MULTILANGUAGE_TRANSLATION_VALIDATION));

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.inner");
                if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                    title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.segment.stacked.correct.link.inner", category);
                }
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.inner.filename") + ".jpg";
                getTraductionPercentageLanguagesGraphic(request, title, file, infoList);
                request.setAttribute(Constants.MULTILANGUAGE_INPG_LINK_TR_INFO, infoList);
            }

            //String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.compare.failed.pages");
            //String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.compare.failed.pages.filename") + categoryIdStr + ".jpg";

            return Constants.OBSERVATORY_HAVE_RESULTS;
        } else {
            return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
        }
    }

    // TODO: ¿Para que sirve este método?
    public static int generateEvolutionGraphics(HttpServletRequest request, String filePath, String colors) throws Exception {
        return Constants.OBSERVATORY_HAVE_RESULTS;
    }

    private static Map<Integer, List<AnalysisForm>> getGlobalResultData(String executionId, long idCategory) throws Exception {
        Map<Integer, List<AnalysisForm>> results = new HashMap<Integer, List<AnalysisForm>>();
        results.put(Constants.MULTILANGUAGE_HOME, new ArrayList<AnalysisForm>());
        results.put(Constants.MULTILANGUAGE_INTERNAL_PAGES, new ArrayList<AnalysisForm>());

        Connection c = null;
        Connection conn = null;

        try {
            c = DataBaseManager.getConnection();
            PropertiesManager pmgr = new PropertiesManager();
            conn = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.multilanguage"));
            List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(c, Long.parseLong(executionId), idCategory);
            if (listExecutionsIds != null && !listExecutionsIds.isEmpty()) {
                Map<Long, List<AnalysisForm>> multilanguageResultList = AnalysisManager.getObservatoryAnalysis(conn, listExecutionsIds);
                for (Map.Entry<Long, List<AnalysisForm>> multilanguageResultEntry : multilanguageResultList.entrySet()) {
                    results.get(Constants.MULTILANGUAGE_HOME).add(multilanguageResultEntry.getValue().get(0));
                    for (int i = 1; i < multilanguageResultEntry.getValue().size(); i++) {
                        results.get(Constants.MULTILANGUAGE_INTERNAL_PAGES).add(multilanguageResultEntry.getValue().get(i));
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioMultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
        }

        return results;
    }

    public static List<LabelValueBean> getLanguagesGraphic(HttpServletRequest request,
                                                           String title, String filePath, List<AnalysisForm> analysisList, String colors) throws Exception {

        File file = new File(filePath);
        List<LabelValueBean> results = new ArrayList<LabelValueBean>();
        Map<String, BigDecimal> resultData = new HashMap<String, BigDecimal>();
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        if (!analysisList.isEmpty()) {
            List<LanguageForm> languages = AnalysisManager.getLanguages(true);

            for (LanguageForm language : languages) {
                resultData.put(language.getName(), BigDecimal.ZERO);
            }

            for (AnalysisForm analysisForm : analysisList) {
                if (analysisForm.getLanguagesFound() != null && !analysisForm.getLanguagesFound().isEmpty()) {
                    List<String> countedLangList = new ArrayList<String>();
                    for (LanguageFoundForm languageFoundForm : analysisForm.getLanguagesFound()) {
                        if (resultData.get(languageFoundForm.getLanguage().getName()) != null && !countedLangList.contains(languageFoundForm.getLanguage().getName())) {
                            resultData.put(languageFoundForm.getLanguage().getName(), resultData.get(languageFoundForm.getLanguage().getName()).add(BigDecimal.ONE));
                            countedLangList.add(languageFoundForm.getLanguage().getName());
                        }
                    }
                }
            }

            LabelValueBean labelValueBean;
            for (LanguageForm key : languages) {
                BigDecimal value = resultData.get(key.getName()).divide(new BigDecimal(analysisList.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                dataSet.addValue(value, "", GraphicsUtils.parseLevelLabel(key.getName(), request));
                labelValueBean = new LabelValueBean();
                labelValueBean.setLabel(key.getName());
                labelValueBean.setValue(value.toString().replace(".00", "") + "%");
                results.add(labelValueBean);
            }
        }

        //Si no existe la gráfica, la creamos
        if (!file.exists()) {
            String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mult.row");
            String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.language");
            GraphicsUtils.createBarChart(dataSet, title, rowTitle, columnTitle, colors, false, true, true, filePath, "No hay resultados", request, x, y);
        }
        return results;
    }

    public static void createTranslationStackedChart(HttpServletRequest request, String filePath, String title, DefaultCategoryDataset dataSet) throws Exception {
        File file = new File(filePath);
        PropertiesManager pmgr = new PropertiesManager();

        //Si no existe la gráfica, la creamos
        if (!file.exists()) {
            String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mult.row");
            ChartForm chartForm = new ChartForm(title, "", rowTitle, dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.multilanguage.three.colors"));
            chartForm.setRoundLabelPosition(true);
            GraphicsUtils.createStackedBarChart(chartForm, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos"), filePath);
        }
    }

    public static void getTraductionPercentageLanguagesGraphic(HttpServletRequest request,
                                                               String title, String filePath, List<SiteTranslationInformationForm> infoList) throws Exception {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        for (SiteTranslationInformationForm infoForm : infoList) {
            dataSet.addValue(infoForm.getNoTranslationPercentage(), CrawlerUtils.getResources(request).getMessage("multilanguage.corret.translation.legend.nt"), infoForm.getName());
            dataSet.addValue(infoForm.getNoCorrectTranslationPercentage(), CrawlerUtils.getResources(request).getMessage("multilanguage.corret.translation.legend.mt"), infoForm.getName());
            dataSet.addValue(infoForm.getCorrectTranslationPercentage(), CrawlerUtils.getResources(request).getMessage("multilanguage.corret.translation.legend.bt"), infoForm.getName());
        }

        createTranslationStackedChart(request, filePath, title, dataSet);
    }

	/*static SiteTranslationInformationForm fixResults(SiteTranslationInformationForm infoForm){

		if (infoForm.getCorrectTranslationPercentage().add(infoForm.getNoCorrectTranslationPercentage().add(infoForm.getNoTranslationPercentage())).compareTo(new BigDecimal(100)) < 0){
			infoForm.setCorrectTranslationPercentage(infoForm.getCorrectTranslationPercentage().add(new BigDecimal(1)));
		}else if (infoForm.getCorrectTranslationPercentage().add(infoForm.getNoCorrectTranslationPercentage().add(infoForm.getNoTranslationPercentage())).compareTo(new BigDecimal(100)) > 0){
			if (infoForm.getNoTranslationPercentage().compareTo(new BigDecimal(0)) != 0){
				infoForm.setNoTranslationPercentage(infoForm.getNoTranslationPercentage().subtract(new BigDecimal(1)));
			}else if (infoForm.getNoCorrectTranslationPercentage().compareTo(new BigDecimal(0)) != 0){
				infoForm.setNoCorrectTranslationPercentage(infoForm.getNoCorrectTranslationPercentage().subtract(new BigDecimal(1)));
			}else{
				infoForm.setCorrectTranslationPercentage(infoForm.getCorrectTranslationPercentage().subtract(new BigDecimal(1)));
			}
		}
		
		if (((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(0)) != 0) && 
				((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100))) != 0){
			if ((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100)) < 0){
				infoForm.setDeclarationGreen(infoForm.getDeclarationGreen().add(new BigDecimal(1)));
			}else if ((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100)) > 0){
				if (infoForm.getDeclarationRed().compareTo(new BigDecimal(0)) > 0){
					infoForm.setDeclarationRed(infoForm.getDeclarationRed().subtract(new BigDecimal(1)));
				}else if (infoForm.getDeclarationGreen().compareTo(new BigDecimal(0)) > 0){
					infoForm.setDeclarationGreen(infoForm.getDeclarationGreen().subtract(new BigDecimal(1)));
				}
			}
		}
		
		if (((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(0)) != 0) &&
			((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100))) != 0){
			if ((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100)) < 0){
				infoForm.setTextTranslationGreen(infoForm.getTextTranslationGreen().add(new BigDecimal(1)));
			}else if ((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100)) > 0){
				if (infoForm.getTextTranslationRed().compareTo(new BigDecimal(0)) > 0){
					infoForm.setTextTranslationRed(infoForm.getTextTranslationRed().subtract(new BigDecimal(1)));
				}else if (infoForm.getTextTranslationGreen().compareTo(new BigDecimal(0)) > 0){
					infoForm.setTextTranslationGreen(infoForm.getTextTranslationGreen().subtract(new BigDecimal(1)));
				}
			}
		}
		
		if (((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(0)) != 0) && 
				((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100))) != 0){
			if ((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100)) < 0){
				infoForm.setTranslationGreen(infoForm.getTranslationGreen().add(new BigDecimal(1)));
			}else if ((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100)) > 0){
				if (infoForm.getTranslationRed().compareTo(new BigDecimal(0)) > 0){
					infoForm.setTranslationRed(infoForm.getTranslationRed().subtract(new BigDecimal(1)));
				}else if (infoForm.getTranslationGreen().compareTo(new BigDecimal(0)) > 0){
					infoForm.setTranslationGreen(infoForm.getTranslationGreen().subtract(new BigDecimal(1)));
				}
			}
		}
		
		return infoForm;
	}*/

    public static List<ValidationLanguageForm> getLanguagesValidityGraphic(HttpServletRequest request,
                                                                           String title, String filePath, List<SiteTranslationInformationForm> infoList, int validationType) throws Exception {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        List<ValidationLanguageForm> validationLanguageFormList = new ArrayList<ValidationLanguageForm>();
        List<LanguageForm> languages = AnalysisManager.getLanguages(true);

        BigDecimal greenValue = null;
        BigDecimal redValue = null;

        //Lenguajes ordenados para las gráficas.
        for (LanguageForm languageForm : languages) {
            for (SiteTranslationInformationForm infoForm : infoList) {
                if (infoForm.getName().equals(languageForm.getName())) {
                    if (validationType == Constants.MULTILANGUAGE_TOTAL_VALIDATION) {
                        greenValue = infoForm.getTranslationGreen();
                        redValue = infoForm.getTranslationRed();
                    } else if (validationType == Constants.MULTILANGUAGE_DECLARATION_VALIDATION) {
                        greenValue = infoForm.getDeclarationGreen();
                        redValue = infoForm.getDeclarationRed();
                    } else if (validationType == Constants.MULTILANGUAGE_TRANSLATION_VALIDATION) {
                        greenValue = infoForm.getTextTranslationGreen();
                        redValue = infoForm.getTextTranslationRed();
                    }
                    dataSet.addValue(redValue, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.invalid.language"), languageForm.getName());
                    dataSet.addValue(greenValue, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.valid.language"), languageForm.getName());
                    ValidationLanguageForm validationLangForm = new ValidationLanguageForm();
                    validationLangForm.setLanguage(languageForm.getName());
                    validationLangForm.setGreenPercentage(String.valueOf(greenValue).replace(".00", "") + "%");
                    validationLangForm.setRedPercentage(String.valueOf(redValue).replace(".00", "") + "%");
                    validationLanguageFormList.add(validationLangForm);
                    break;
                }
            }
        }

        createStackedValidInvalidChart(request, title, filePath, dataSet);

        return validationLanguageFormList;
    }

    public static List<ValidationLanguageForm> getPortalLanguagesValidity(AnalysisForm analysisForm, int validationType) throws Exception {
        List<LanguageForm> languages = AnalysisManager.getLanguages(true);
        Map<String, BigDecimal> resultData = initLanguages(languages);

        List<String> countedLangList = new ArrayList<String>();
        for (LanguageFoundForm languageFoundForm : analysisForm.getLanguagesFound()) {
            if (!countedLangList.contains(languageFoundForm.getLanguage().getName())) {
                if (validationType == Constants.MULTILANGUAGE_TOTAL_VALIDATION) {
                    if (languageFoundForm.isCorrect()) {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_VALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ok").add(BigDecimal.ONE));
                    } else {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_INVALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ko").add(BigDecimal.ONE));
                    }
                } else if (validationType == Constants.MULTILANGUAGE_DECLARATION_VALIDATION) {
                    if (languageFoundForm.isCorrectDeclaration()) {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_VALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ok").add(BigDecimal.ONE));
                    } else {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_INVALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ko").add(BigDecimal.ONE));
                    }
                } else if (validationType == Constants.MULTILANGUAGE_TRANSLATION_VALIDATION) {
                    if (languageFoundForm.isCorrectTranslation()) {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_VALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ok").add(BigDecimal.ONE));
                    } else {
                        resultData.put(languageFoundForm.getLanguage().getName() + Constants.OBSERVATORY_MULTILANGUAGE_INVALID_SUFFIX, resultData.get(languageFoundForm.getLanguage().getName() + "_ko").add(BigDecimal.ONE));
                    }
                }
            }
            countedLangList.add(languageFoundForm.getLanguage().getName());
        }

        Map<String, BigDecimal> resultDataAux = createValidInvalidDataSet(resultData);
        List<ValidationLanguageForm> validationLangFormList = new ArrayList<ValidationLanguageForm>();
        for (LanguageForm languageForm : languages) {
            ValidationLanguageForm validationLangForm = new ValidationLanguageForm();
            validationLangForm.setLanguage(languageForm.getName());
            validationLangForm.setGreenPercentage(String.valueOf(resultDataAux.get(languageForm.getName() + Constants.OBSERVATORY_MULTILANGUAGE_VALID_SUFFIX)).replace(".00", "") + "%");
            validationLangForm.setRedPercentage(String.valueOf(resultDataAux.get(languageForm.getName() + Constants.OBSERVATORY_MULTILANGUAGE_INVALID_SUFFIX)).replace(".00", "") + "%");
            validationLangFormList.add(validationLangForm);
        }
        return validationLangFormList;
    }

    public static Map<String, BigDecimal> initLanguages(List<LanguageForm> languages) {
        Map<String, BigDecimal> resultData = new HashMap<String, BigDecimal>();
        for (LanguageForm language : languages) {
            resultData.put(language.getName() + Constants.OBSERVATORY_MULTILANGUAGE_VALID_SUFFIX, BigDecimal.ZERO);
            resultData.put(language.getName() + Constants.OBSERVATORY_MULTILANGUAGE_INVALID_SUFFIX, BigDecimal.ZERO);
        }

        return resultData;
    }

    public static Map<String, BigDecimal> createValidInvalidDataSet(Map<String, BigDecimal> resultData) {
        Map<String, BigDecimal> resultDataAux = new TreeMap<String, BigDecimal>();
        for (Map.Entry<String, BigDecimal> resultDataEntry : resultData.entrySet()) {
            final String keyPrefix = resultDataEntry.getKey().substring(0, resultDataEntry.getKey().length() - 3);
            final BigDecimal total = resultData.get(keyPrefix + "_ok").add(resultData.get(keyPrefix + "_ko"));
            if (total.compareTo(BigDecimal.ZERO) != 0) {
                resultDataAux.put(resultDataEntry.getKey(), resultDataEntry.getValue().divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            } else {
                resultDataAux.put(resultDataEntry.getKey(), BigDecimal.ZERO);
            }
        }
        return resultDataAux;
    }

    public static void createStackedValidInvalidChart(HttpServletRequest request, String title, String filePath, DefaultCategoryDataset dataSet) throws Exception {
        File file = new File(filePath);
        PropertiesManager pmgr = new PropertiesManager();
        //Si no existe la gráfica, la creamos
        if (!file.exists()) {
            String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mult.row");
            ChartForm chartForm = new ChartForm(title, "", rowTitle, dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.multilanguage.colors"));
            chartForm.setRoundLabelPosition(true);
            GraphicsUtils.createStackedBarChart(chartForm, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos"), filePath);
        }
    }

}