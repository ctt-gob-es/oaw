package es.inteco.multilanguage.database.export;

import es.inteco.common.logging.Logger;
import es.inteco.multilanguage.common.Constants;
import es.inteco.multilanguage.dao.LanguageDAO;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.manager.BaseManager;
import es.inteco.multilanguage.persistence.*;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class ExportMultilanguageUtils {

    public static void exportObservatoryMultilanguageInfo(Map<Long, List<Long>> crawlerIdsMap, Map<Long, String> categoryNames, Map<Long, String> siteNames,
                                                          Long idObservatory, String observatoryName, Date date) throws Exception {

        try {

            //Introducimos los datos del Observatorio
            Observatory observatory = new Observatory(idObservatory);
            observatory.setName(observatoryName);
            observatory.setDate(date);
            List<Category> categories = new ArrayList<>();
            Map<String, SiteTranslationInformationForm> observatoryInfo = new HashMap<>();
            List<AnalysisForm> analysisObsList = new ArrayList<>();
            List<AnalysisForm> analysisObsListHome = new ArrayList<>();
            List<AnalysisForm> analysisObsListInternal = new ArrayList<>();
            //Introducimos las categorias
            for (Map.Entry<Long, List<Long>> crawlerIdsEntry : crawlerIdsMap.entrySet()) {
                Map<String, SiteTranslationInformationForm> categoryInfo = new HashMap<>();
                Category category = new Category(crawlerIdsEntry.getKey());
                category.setObservatory(observatory);
                category.setName(categoryNames.get(crawlerIdsEntry.getKey()));
                category.setId_category(crawlerIdsEntry.getKey());
                List<Site> sites = new ArrayList<>();
                List<AnalysisForm> analysisCatList = new ArrayList<>();
                List<AnalysisForm> analysisCatListHome = new ArrayList<>();
                List<AnalysisForm> analysisCatListInternal = new ArrayList<>();
                //Introducimos los portales
                for (Long idCrawler : crawlerIdsEntry.getValue()) {
                    List<AnalysisForm> analysisList = AnalysisManager.getAnalysisFormByExecution(idCrawler);

                    if (analysisList != null && !analysisList.isEmpty()) {
                        Site site = new Site(idCrawler);
                        site.setCategory(category);
                        site.setName(siteNames.get(idCrawler));
                        List<Page> pages = new ArrayList<>();
                        //Introducimos las páginas
                        for (AnalysisForm analysisForm : analysisList) {
                            Page page = new Page(Long.valueOf(analysisForm.getId()), analysisForm.getUrl());
                            page.setSite(site);
                            page.setResults(createPageResults(analysisForm, page));
                            pages.add(page);
                        }
                        site.setPages(pages);
                        site.setResults(createSiteResults(site, analysisList, Constants.RESULTS_TYPE_GENERAL));
                        sites.add(site);

                        analysisCatList.addAll(analysisList);
                        analysisCatListHome.add(analysisList.get(0));
                        analysisCatListInternal.addAll(analysisList);
                        analysisCatListInternal.remove(analysisList.get(0));
                    }
                }
                category.setSites(sites);

                calculateCategoryResults(categoryInfo, category, categories, analysisCatList, Constants.RESULTS_TYPE_GENERAL);
                calculateCategoryResults(categoryInfo, category, categories, analysisCatListHome, Constants.RESULTS_TYPE_HOME);
                calculateCategoryResults(categoryInfo, category, categories, analysisCatListInternal, Constants.RESULTS_TYPE_INTERNAS);

                analysisObsList.addAll(analysisCatList);
                analysisObsListHome.addAll(analysisCatListHome);
                analysisObsListInternal.addAll(analysisCatListInternal);
            }

            calculateObservatoryResults(observatoryInfo, observatory, categories, analysisObsList, Constants.RESULTS_TYPE_GENERAL);
            calculateObservatoryResults(observatoryInfo, observatory, categories, analysisObsListHome, Constants.RESULTS_TYPE_HOME);
            calculateObservatoryResults(observatoryInfo, observatory, categories, analysisObsListInternal, Constants.RESULTS_TYPE_INTERNAS);

            Logger.putLog("Se van a guardar los datos del observatorio con id " + idObservatory, ExportMultilanguageUtils.class, Logger.LOG_LEVEL_INFO);
            BaseManager.save(observatory);
        } catch (Exception e) {
            Logger.putLog("Excepcion guardando los datos del observatorio con id " + idObservatory, ExportMultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static void calculateCategoryResults(Map<String, SiteTranslationInformationForm> categoryInfo, Category category, List<Category> categories, List<AnalysisForm> analysisCatList, String type) throws Exception {
        //Agregamos los datos del portal a la categoria
        List<SiteTranslationInformationForm> categoryInfoList = MultilanguageUtils.getPortalTraductionInformation(analysisCatList, true);
        for (SiteTranslationInformationForm categoryInfoForm : categoryInfoList) {
            categoryInfo.put(categoryInfoForm.getName(), categoryInfoForm);
        }
        //Calculamos los resultados de la categoría
        List<Results> results = category.getResults();
        results.addAll(createResults(categoryInfo, category, null, type));
        category.setResults(results);
        categories.add(category);
    }

    private static void calculateObservatoryResults(Map<String, SiteTranslationInformationForm> observatoryInfo, Observatory observatory, List<Category> categories, List<AnalysisForm> analysisObsList, String type) throws Exception {
        //Agregamos los datos de la categoria al observatorio
        List<SiteTranslationInformationForm> observatotyInfoList = MultilanguageUtils.getPortalTraductionInformation(analysisObsList, true);
        for (SiteTranslationInformationForm observatoryInfoForm : observatotyInfoList) {
            observatoryInfo.put(observatoryInfoForm.getName(), observatoryInfoForm);
        }

        observatory.setCategories(categories);
        //Calculamos los datos del Observatorio
        List<Results> results = observatory.getResults();
        results.addAll(createResults(observatoryInfo, null, observatory, type));
        observatory.setResults(results);
    }

    private static List<Results> createSiteResults(Site site, List<AnalysisForm> analysisList, String type) throws Exception {
        List<SiteTranslationInformationForm> translationLanguagesFormList = MultilanguageUtils.getPortalTraductionInformation(analysisList, true);
        List<Results> results = new ArrayList<>();
        for (SiteTranslationInformationForm infoSite : translationLanguagesFormList) {
            Results result;
            if (infoSite.getNoTranslationPercentage().compareTo(new BigDecimal(100)) != 0) {
                result = new Results(LanguageDAO.getLanguage(infoSite.getName()), infoSite.getNoTranslationPercentage(),
                        infoSite.getCorrectTranslationPercentage(), infoSite.getNoCorrectTranslationPercentage(),
                        infoSite.getDeclarationGreen(), infoSite.getDeclarationRed(), infoSite.getTextTranslationGreen(),
                        infoSite.getTextTranslationRed(), infoSite.getTranslationGreen(), infoSite.getTranslationRed(), type);
            } else {
                result = new Results(LanguageDAO.getLanguage(infoSite.getName()), infoSite.getNoTranslationPercentage(),
                        infoSite.getCorrectTranslationPercentage(), infoSite.getNoCorrectTranslationPercentage(),
                        null, null, null, null, null, null, type);
            }
            result.setSite(site);
            results.add(result);
        }
        return results;
    }

    private static List<PageResult> createPageResults(AnalysisForm analysisForm, Page page) throws IllegalAccessException, InvocationTargetException {
        List<PageResult> results = new ArrayList<>();
        for (LanguageFoundForm lang : analysisForm.getLanguagesFound()) {
            Language language = new Language();
            Language languageS = new Language();
            if (lang.getLanguage() != null) {
                BeanUtils.copyProperties(language, lang.getLanguage());
            }
            if (lang.getLanguageSuspected() != null) {
                BeanUtils.copyProperties(languageS, lang.getLanguageSuspected());
            }
            PageResult result = new PageResult(lang.getDeclarationLang(), language, languageS);
            result.setPage(page);
            results.add(result);
        }
        return results;
    }

    private static List<Results> createResults(Map<String, SiteTranslationInformationForm> info, Category category, Observatory observatory, String type) {
        List<Results> results = new ArrayList<>();
        for (Map.Entry<String, SiteTranslationInformationForm> entry : info.entrySet()) {
            SiteTranslationInformationForm langInfo = entry.getValue();
            Results result = new Results(LanguageDAO.getLanguage(entry.getKey()), langInfo.getNoTranslationPercentage(),
                    langInfo.getCorrectTranslationPercentage(), langInfo.getNoCorrectTranslationPercentage(),
                    langInfo.getDeclarationGreen(), langInfo.getDeclarationRed(), langInfo.getTextTranslationGreen(),
                    langInfo.getTextTranslationRed(), langInfo.getTranslationGreen(), langInfo.getTranslationRed(), type);

            if (category != null) {
                result.setCategory(category);
            }
            if (observatory != null) {
                result.setObservatory(observatory);
            }
            results.add(result);
        }
        return results;
    }

}
