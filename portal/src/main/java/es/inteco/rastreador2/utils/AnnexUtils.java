package es.inteco.rastreador2.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.database.export.SiteTranslationInformationForm;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.PageForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.manager.ObservatoryExportManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class AnnexUtils {

    private AnnexUtils() {
    }

    //Anexos sin iteraciones *************************************************************************************

    public static void createAnnex(HttpServletRequest request, Long idObsExecution, Long idOperation) throws Exception {
        FileWriter writer = null;
        Connection c = DataBaseManager.getConnection();
        try {
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);

            PropertiesManager pmgr = new PropertiesManager();
            File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexo_paginas.xml");
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
            }
            writer = new FileWriter(file);

            ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);

            XMLSerializer serializer = new XMLSerializer(writer, of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
                for (SiteForm siteForm : categoryForm.getSiteFormList()) {
                    hd.startElement("", "", "portal", null);

                    hd.startElement("", "", "nombre", null);
                    hd.characters(siteForm.getName().toCharArray(), 0, siteForm.getName().length());
                    hd.endElement("", "", "nombre");

                    SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));

                    hd.startElement("", "", "categoria", null);
                    hd.characters(semillaForm.getCategoria().getName().toCharArray(), 0, semillaForm.getCategoria().getName().length());
                    hd.endElement("", "", "categoria");

                    hd.startElement("", "", "depende_de", null);
                    if (semillaForm.getDependencia() != null) {
                        hd.characters(semillaForm.getDependencia().toCharArray(), 0, semillaForm.getDependencia().length());
                    }
                    hd.endElement("", "", "depende_de");

                    hd.startElement("", "", "paginas", null);

                    for (PageForm pageForm : siteForm.getPageList()) {
                        hd.startElement("", "", "pagina", null);

                        hd.startElement("", "", "url", null);
                        hd.characters(pageForm.getUrl().toCharArray(), 0, pageForm.getUrl().length());
                        hd.endElement("", "", "url");

                        hd.startElement("", "", "puntuacion", null);
                        hd.characters(pageForm.getScore().toCharArray(), 0, pageForm.getScore().length());
                        hd.endElement("", "", "puntuacion");

                        hd.startElement("", "", "adecuacion", null);
                        hd.characters(ObservatoryUtils.getValidationLevel(request, pageForm.getLevel()).toCharArray(), 0, ObservatoryUtils.getValidationLevel(request, pageForm.getLevel()).length());
                        hd.endElement("", "", "adecuacion");

                        hd.endElement("", "", "pagina");
                    }
                    hd.endElement("", "", "paginas");

                    hd.endElement("", "", "portal");
                }
            }

            hd.endElement("", "", "resultados");
            hd.endDocument();
        } catch (IOException e) {
            Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } catch (SAXException e) {
            Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
            DataBaseManager.closeConnection(c);
        }
    }

    /*public static void createAnnex2E(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        FileWriter writer = null;
        try {
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);

            writer = new FileWriter("/usr/share/anexos/anexo_portales.xml");

            XMLSerializer serializer = new XMLSerializer(writer, of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            List<ObservatoryEvaluationForm> siteEvaluationList = new ArrayList<ObservatoryEvaluationForm>();
            String semilla = "";

            for (int i = 0; i < pageExecutionList.size(); i++) {
                ObservatoryEvaluationForm evaluationForm = pageExecutionList.get(i);

                if (StringUtils.isEmpty(semilla)) {
                    semilla = evaluationForm.getUrl();
                }

                if ((i == 0) || (!evaluationForm.getCrawlerExecutionId().equals(pageExecutionList.get(i - 1).getCrawlerExecutionId()))) {
                    hd.startElement("", "", "portal", null);

                    hd.startElement("", "", "nombre", null);
                    hd.characters(evaluationForm.getSeed().getName().toCharArray(), 0, evaluationForm.getSeed().getName().length());
                    hd.endElement("", "", "nombre");

                    hd.startElement("", "", "categoria", null);
                    hd.characters(evaluationForm.getSeed().getCategory().toCharArray(), 0, evaluationForm.getSeed().getCategory().length());
                    hd.endElement("", "", "categoria");

                    hd.startElement("", "", "depende_de", null);
                    hd.characters(evaluationForm.getSeed().getDependence().toCharArray(), 0, evaluationForm.getSeed().getDependence().length());
                    hd.endElement("", "", "depende_de");
                }

                siteEvaluationList.add(evaluationForm);

                if ((i == pageExecutionList.size() - 1) || (!evaluationForm.getCrawlerExecutionId().equals(pageExecutionList.get(i + 1).getCrawlerExecutionId()))) {

                    if (siteEvaluationList.size() > 0) {
                        ScoreForm scoreForm = IntavUtils.generateScores(request, siteEvaluationList);

                        hd.startElement("", "", "semilla", null);
                        hd.characters(semilla.toCharArray(), 0, semilla.length());
                        hd.endElement("", "", "semilla");

                        hd.startElement("", "", "puntuacion", null);
                        hd.characters(scoreForm.getTotalScore().toString().toCharArray(), 0, scoreForm.getTotalScore().toString().length());
                        hd.endElement("", "", "puntuacion");

                        hd.startElement("", "", "adecuacion", null);
                        hd.characters(IntavUtils.getValidationLevel(scoreForm, request).toCharArray(), 0, IntavUtils.getValidationLevel(scoreForm, request).length());
                        hd.endElement("", "", "adecuacion");
                    }

                    hd.endElement("", "", "portal");

                    siteEvaluationList = new ArrayList<ObservatoryEvaluationForm>();
                    semilla = "";
                }
            }
            hd.endElement("", "", "resultados");
            hd.endDocument();
        } catch (IOException e) {
            Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } catch (SAXException e) {
            Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }*/

    //Anexos con iteraciones *************************************************************************************

	/*public static Map<Long, Map<String, ScoreForm>> createAnnexMap(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList){
        Connection c = null;
		Connection conn = null;
		PropertiesManager pmgr = new PropertiesManager();
		
		Map<Long, Map<String, ScoreForm>> seedMap = new HashMap<Long, Map<String, ScoreForm>>();
		
		try{
			c = DataBaseManager.getConnection();
			conn = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));
			
			//Semillas
			List<Long> initialSeedList = new ArrayList<Long>();
			for (ObservatoryEvaluationForm oeForm : pageExecutionList){
				if (!initialSeedList.contains(Long.valueOf(oeForm.getSeed().getId()))){
					initialSeedList.add(Long.valueOf(oeForm.getSeed().getId()));
				}
			}
			//Observatorios asociados y fechas de estos
			Map<Long, String> observatoryDateMap = new HashMap<Long, String>();
			Long idExecution = pageExecutionList.get(0).getObservatoryExecutionId();
			Long idObservatory = ObservatorioDAO.getObservatoryFormFromExecution(c, idExecution).getId();
			List<ObservatorioRealizadoForm> fulfilledObsList = ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION, null);
			for (ObservatorioRealizadoForm orForm : fulfilledObsList){
				observatoryDateMap.put(orForm.getId(), orForm.getFechaStr());
			}
			
			int i = 0;
			for(Long idSeed :initialSeedList) {
				List<Long> executedCrawlerList = new ArrayList<Long>();
				//lista de ids de todas las ejecuciones del rastreo asociado a una semilla de un observatorio
				executedCrawlerList = ObservatorioDAO.getSeedExecutionsFromObservatory(c, String.valueOf(idSeed), idObservatory);
				Collections.reverse(executedCrawlerList);
				Map<String, ScoreForm> mapCrawler = new HashMap<String, ScoreForm>();
				for (Long executedCrawler : executedCrawlerList){
					Evaluator evaluator = new Evaluator();
					idExecution = ObservatorioDAO.getIdExecutionObservatoryFromExCrawler(c, executedCrawler);
					List<ObservatoryEvaluationForm> siteEvaluationList = new ArrayList<ObservatoryEvaluationForm>();
					String methodology = ObservatorioDAO.getMethodology(c, idExecution);
					ArrayList<Analysis> analisisList = AnalisisDatos.getAnalysisByTracking(executedCrawler, IntavConstants.NO_PAGINATION, request);
					if (analisisList != null && !analisisList.isEmpty()){	
						for (Analysis analisis : analisisList){
							Evaluation evaluation = evaluator.getObservatoryAnalisisDB(conn, analisis.getCode(), EvaluatorUtils.getDocList());
							ObservatoryEvaluationForm seedEvaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false);
							siteEvaluationList.add(seedEvaluationForm);	
						}
						ScoreForm scoreForm = IntavUtils.generateScores(request, siteEvaluationList);
						if (observatoryDateMap.get(idExecution) != null){
							mapCrawler.put(observatoryDateMap.get(idExecution), scoreForm);
						}
					}
				}
				seedMap.put(idSeed, mapCrawler);
				System.out.println("SEMILLA N: " + i++);
			}
		}catch (Exception e) {
			Logger.putLog("Error al recuperar las semillas del Observatorio al crear el anexo", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}finally{
			DataBaseManager.closeConnection(c);
			DataBaseManager.closeConnection(conn);
		}
		
		return seedMap;
	}*/

    public static Map<Long, TreeMap<String, ScoreForm>> createAnnexMap(HttpServletRequest request, Long idObsExecution) {

        Map<Long, TreeMap<String, ScoreForm>> seedMap = new HashMap<Long, TreeMap<String, ScoreForm>>();
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();
            ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryFormFromExecution(c, idObsExecution);
            ObservatorioRealizadoForm executedObservatory = ObservatorioDAO.getFulfilledObservatory(c, observatoryForm.getId(), idObsExecution);
            List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, observatoryForm.getId(), Constants.NO_PAGINACION, executedObservatory.getFecha(), false);

            List<ObservatoryForm> observatoryFormList = new ArrayList<ObservatoryForm>();
            for (ObservatorioRealizadoForm orForm : observatoriesList) {
                observatoryFormList.add(ObservatoryExportManager.getObservatory(orForm.getId()));
            }

            for (ObservatoryForm observatory : observatoryFormList) {
                for (CategoryForm category : observatory.getCategoryFormList()) {
                    for (SiteForm siteForm : category.getSiteFormList()) {
                        TreeMap<String, ScoreForm> seedInfo = new TreeMap<String, ScoreForm>();
                        ScoreForm scoreForm = new ScoreForm();
                        scoreForm.setLevel(siteForm.getLevel());
                        scoreForm.setTotalScore(new BigDecimal(siteForm.getScore()));
                        if (seedMap.get(Long.valueOf(siteForm.getIdCrawlerSeed())) != null) {
                            seedInfo = seedMap.get(Long.valueOf(siteForm.getIdCrawlerSeed()));
                        }
                        seedInfo.put(observatory.getDate(), scoreForm);
                        seedMap.put(Long.valueOf(siteForm.getIdCrawlerSeed()), seedInfo);
                    }
                }
            }

        } catch (Exception e) {
            Logger.putLog("Error al recuperar las semillas del Observatorio al crear el anexo", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return seedMap;
    }

    private static String changeLevelName(String name, HttpServletRequest request) {
        if (name.equalsIgnoreCase(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.aa.old"))) {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.aa");
        } else if (name.equalsIgnoreCase(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.nv.old"))) {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.nv");
        } else if (name.equalsIgnoreCase(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.a.old"))) {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.a");
        } else {
            return "";
        }
    }

    public static void createAnnex2Ev(HttpServletRequest request, Long idObsExecution, Long idOperation) throws Exception {

        FileWriter writer = null;

        OutputFormat of = new OutputFormat("XML", "UTF-8", true);

        PropertiesManager pmgr = new PropertiesManager();
        File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexo_portales.xml");
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
        }
        writer = new FileWriter(file);

        XMLSerializer serializer = new XMLSerializer(writer, of);
        ContentHandler hd = serializer.asContentHandler();
        hd.startDocument();
        hd.startElement("", "", "resultados", null);

        Connection c = null;
        try {
            Map<Long, TreeMap<String, ScoreForm>> annexmap = createAnnexMap(request, idObsExecution);
            c = DataBaseManager.getConnection();
            for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
                SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
                if (semillaForm.getId() != 0) {
                    hd.startElement("", "", "portal", null);

                    hd.startElement("", "", "nombre", null);
                    hd.characters(semillaForm.getNombre().toCharArray(), 0, semillaForm.getNombre().length());
                    hd.endElement("", "", "nombre");

                    hd.startElement("", "", "categoria", null);
                    hd.characters(semillaForm.getCategoria().getName().toCharArray(), 0, semillaForm.getCategoria().getName().length());
                    hd.endElement("", "", "categoria");

                    hd.startElement("", "", "depende_de", null);
                    if (semillaForm.getDependencia() != null) {
                        hd.characters(semillaForm.getDependencia().toCharArray(), 0, semillaForm.getDependencia().length());
                    }
                    hd.endElement("", "", "depende_de");

                    hd.startElement("", "", "semilla", null);
                    hd.characters(semillaForm.getListaUrls().get(0).toCharArray(), 0, semillaForm.getListaUrls().get(0).length());
                    hd.endElement("", "", "semilla");

                    for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
                        String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
                        hd.startElement("", "", "puntuacion_" + executionDateAux, null);
                        hd.characters(entry.getValue().getTotalScore().toString().toCharArray(), 0, entry.getValue().getTotalScore().toString().length());
                        hd.endElement("", "", "puntuacion_" + executionDateAux);

                        hd.startElement("", "", "adecuacion_" + executionDateAux, null);
                        hd.characters(changeLevelName(entry.getValue().getLevel(), request).toCharArray(), 0, changeLevelName(entry.getValue().getLevel(), request).length());
                        hd.endElement("", "", "adecuacion_" + executionDateAux);
                    }
                    hd.endElement("", "", "portal");
                }
            }
            hd.endElement("", "", "resultados");
            hd.endDocument();
        } catch (Exception e) {
            Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
            DataBaseManager.closeConnection(c);
        }

    }

    public static void createMultilanguageAnnexes(Long idObsExecution, Long idOperation) {
        Connection c = null;
        Connection conn = null;
        PropertiesManager pmgr = new PropertiesManager();
        try {
            conn = DataBaseManager.getConnection();
            c = DataBaseManager.getConnection();
            Logger.putLog("Creando anexo portales multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguageSiteAnnex(conn, idObsExecution, idOperation, false);
            Logger.putLog("Creando anexo portales completo multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguageSiteAnnex(conn, idObsExecution, idOperation, true);
            Logger.putLog("Creando anexo páginas multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguagePageAnnex(conn, idObsExecution, idOperation);
        } catch (Exception e) {
            Logger.putLog("Error al conectar o recuperar datos de la BBDD al crear los XML de resultados", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
        }
    }


    private static void createMultilanguagePageAnnex(Connection conn, Long idObsExecution, Long idOperation) {
        FileWriter writer = null;
        PropertiesManager pmgr = new PropertiesManager();
        try {
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);

            File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexo_paginas_multilanguage_completo.xml");

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new FileWriter(file);

            XMLSerializer serializer = new XMLSerializer(writer, of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, idObsExecution, Constants.COMPLEXITY_SEGMENT_NONE);
            for (Long id : listExecutionsIds) {
                List<AnalysisForm> analysisList = AnalysisManager.getObservatoryAnalysisList(id);
                if (analysisList != null && !analysisList.isEmpty()) {
                    hd.startElement("", "", "portal", null);

                    FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(conn, Long.parseLong(analysisList.get(0).getIdCrawling()));

                    hd.startElement("", "", "nombre", null);
                    hd.characters(fulfilledCrawlingForm.getSeed().getNombre().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getNombre().length());
                    hd.endElement("", "", "nombre");

                    hd.startElement("", "", "categoria", null);
                    hd.characters(fulfilledCrawlingForm.getSeed().getCategoria().getName().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getCategoria().getName().length());
                    hd.endElement("", "", "categoria");

                    hd.startElement("", "", "depende_de", null);
                    if (fulfilledCrawlingForm.getSeed().getDependencia() != null) {
                        hd.characters(fulfilledCrawlingForm.getSeed().getDependencia().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getDependencia().length());
                    }
                    hd.endElement("", "", "depende_de");

                    hd.startElement("", "", "paginas", null);

                    for (AnalysisForm analysisForm : analysisList) {
                        hd.startElement("", "", "pagina", null);

                        hd.startElement("", "", "url", null);
                        hd.characters(analysisForm.getUrl().toCharArray(), 0, analysisForm.getUrl().length());
                        hd.endElement("", "", "url");
                        List<LanguageFoundForm> languagesFoundList = MultilanguageUtils.sortLanguagesFoundForm(analysisForm.getLanguagesFound());
                        for (LanguageFoundForm languageFoundForm : languagesFoundList) {
                            hd.startElement("", "", "lenguaje", null);

                            hd.startElement("", "", "nombre", null);
                            hd.characters(languageFoundForm.getLanguage().getName().toCharArray(), 0, languageFoundForm.getLanguage().getName().length());
                            hd.endElement("", "", "nombre");

                            hd.startElement("", "", "url_destino", null);
                            hd.characters(languageFoundForm.getHref().toCharArray(), 0, languageFoundForm.getHref().length());
                            hd.endElement("", "", "url_destino");

                            hd.startElement("", "", "cod_idioma", null);
                            if (languageFoundForm.getDeclarationLang() != null) {
                                hd.characters(languageFoundForm.getDeclarationLang().toCharArray(), 0, languageFoundForm.getDeclarationLang().length());
                            } else {
                                hd.characters("No declarado".toCharArray(), 0, "No declarado".length());
                            }
                            hd.endElement("", "", "cod_idioma");

                            hd.startElement("", "", "idioma_texto", null);
                            if (languageFoundForm.getLanguageSuspected().getName() != null) {
                                hd.characters(languageFoundForm.getLanguageSuspected().getName().toCharArray(), 0, languageFoundForm.getLanguageSuspected().getName().length());
                            } else {
                                hd.characters("Sin texto".toCharArray(), 0, "Sin texto".length());
                            }
                            hd.endElement("", "", "idioma_texto");

                            hd.startElement("", "", "traduccion", null);
                            if (languageFoundForm.isCorrect()) {
                                hd.characters("Correcta".toCharArray(), 0, "Correcta".length());
                            } else {
                                hd.characters("Incorrecta".toCharArray(), 0, "Incorrecta".length());
                            }
                            hd.endElement("", "", "traduccion");

                            hd.endElement("", "", "lenguaje");
                        }

                        hd.endElement("", "", "pagina");
                    }

                    hd.endElement("", "", "paginas");

                    hd.endElement("", "", "portal");
                } else {
                    hd.startElement("", "", "portal", null);
                    String noResults = "Portal: " + RastreoDAO.getFullfilledCrawlingExecution(conn, id).getSeed().getNombre() + " Sin Resultados";
                    hd.characters(noResults.toCharArray(), 0, noResults.length());
                    hd.endElement("", "", "portal");
                }
            }

            hd.endElement("", "", "resultados");
            hd.endDocument();

        } catch (Exception e) {
            Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    private static void createMultilanguageSiteAnnex(Connection conn, Long idObsExecution, Long idOperation, boolean complet) {
        FileWriter writer = null;
        PropertiesManager pmgr = new PropertiesManager();
        try {
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);

            File file = null;
            if (!complet) {
                file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexo_portales_multilanguage.xml");
            } else {
                file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexo_portales_multilanguage_completo.xml");
            }

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new FileWriter(file);

            XMLSerializer serializer = new XMLSerializer(writer, of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, idObsExecution, Constants.COMPLEXITY_SEGMENT_NONE);
            for (Long id : listExecutionsIds) {
                List<AnalysisForm> analysisList = AnalysisManager.getObservatoryAnalysisList(id);
                if (analysisList != null && !analysisList.isEmpty()) {
                    hd.startElement("", "", "portal", null);

                    FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(conn, Long.parseLong(analysisList.get(0).getIdCrawling()));

                    hd.startElement("", "", "nombre", null);
                    hd.characters(fulfilledCrawlingForm.getSeed().getNombre().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getNombre().length());
                    hd.endElement("", "", "nombre");

                    String seed = fulfilledCrawlingForm.getSeed().getListaUrlsString().split(";")[0];
                    hd.startElement("", "", "url_semilla", null);
                    hd.characters(seed.toCharArray(), 0, seed.length());
                    hd.endElement("", "", "url_semilla");

                    hd.startElement("", "", "segmento", null);
                    hd.characters(fulfilledCrawlingForm.getSeed().getCategoria().getName().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getCategoria().getName().length());
                    hd.endElement("", "", "segmento");

                    hd.startElement("", "", "depende_de", null);
                    if (fulfilledCrawlingForm.getSeed().getDependencia() != null) {
                        hd.characters(fulfilledCrawlingForm.getSeed().getDependencia().toCharArray(), 0, fulfilledCrawlingForm.getSeed().getDependencia().length());
                    }
                    hd.endElement("", "", "depende_de");

                    List<SiteTranslationInformationForm> translationLanguagesFormList = MultilanguageUtils.getPortalTraductionInformation(analysisList, complet);

                    for (SiteTranslationInformationForm tLanguage : translationLanguagesFormList) {
                        hd.startElement("", "", "lenguaje", null);

                        hd.startElement("", "", "nombre", null);
                        hd.characters(tLanguage.getName().toCharArray(), 0, tLanguage.getName().length());
                        hd.endElement("", "", "nombre");

                        hd.startElement("", "", "no_traducidas", null);
                        String noTraducidas = tLanguage.getNoTranslationPercentage().toString() + "%";
                        hd.characters(noTraducidas.toCharArray(), 0, noTraducidas.length());
                        hd.endElement("", "", "no_traducidas");

                        hd.startElement("", "", "bien_traducidas", null);
                        String bienTraducidas = tLanguage.getCorrectTranslationPercentage().toString() + "%";
                        hd.characters(bienTraducidas.toCharArray(), 0, bienTraducidas.length());
                        hd.endElement("", "", "bien_traducidas");

                        hd.startElement("", "", "mal_traducidas", null);
                        String malTraducidas = tLanguage.getNoCorrectTranslationPercentage().toString() + "%";
                        hd.characters(malTraducidas.toCharArray(), 0, malTraducidas.length());
                        hd.endElement("", "", "mal_traducidas");

                        if (complet) {
                            hd.startElement("", "", "codigo_idioma", null);
                            String codigoIdioma = tLanguage.getDeclarationGreen().toString() + "%";
                            hd.characters(codigoIdioma.toCharArray(), 0, codigoIdioma.length());
                            hd.endElement("", "", "codigo_idioma");

                            hd.startElement("", "", "no_codigo_idioma", null);
                            String noCodigoIdioma = tLanguage.getDeclarationRed().toString() + "%";
                            hd.characters(noCodigoIdioma.toCharArray(), 0, noCodigoIdioma.length());
                            hd.endElement("", "", "no_codigo_idioma");

                            hd.startElement("", "", "traduccion_texto", null);
                            String trTexto = tLanguage.getTextTranslationGreen().toString() + "%";
                            hd.characters(trTexto.toCharArray(), 0, trTexto.length());
                            hd.endElement("", "", "traduccion_texto");

                            hd.startElement("", "", "no_traduccion_texto", null);
                            String noTrTexto = tLanguage.getTextTranslationRed().toString() + "%";
                            hd.characters(noTrTexto.toCharArray(), 0, noTrTexto.length());
                            hd.endElement("", "", "no_traduccion_texto");

                            hd.startElement("", "", "traduccion", null);
                            String traduccion = tLanguage.getTranslationGreen().toString() + "%";
                            hd.characters(traduccion.toCharArray(), 0, traduccion.length());
                            hd.endElement("", "", "traduccion");

                            hd.startElement("", "", "no_traduccion", null);
                            String noTraduccion = tLanguage.getTranslationRed().toString() + "%";
                            hd.characters(noTraduccion.toCharArray(), 0, noTraduccion.length());
                            hd.endElement("", "", "no_traduccion");

                        }

                        hd.endElement("", "", "lenguaje");
                    }

                    hd.endElement("", "", "portal");
                } else {
                    hd.startElement("", "", "portal", null);
                    String noResults = "Portal: " + RastreoDAO.getFullfilledCrawlingExecution(conn, id).getSeed().getNombre() + " Sin Resultados";
                    hd.characters(noResults.toCharArray(), 0, noResults.length());
                    hd.endElement("", "", "portal");
                }
            }

            hd.endElement("", "", "resultados");
            hd.endDocument();

        } catch (Exception e) {
            Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Excepción", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

}