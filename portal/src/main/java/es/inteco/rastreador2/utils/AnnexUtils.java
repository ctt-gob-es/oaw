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
import org.apache.struts.util.MessageResources;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

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

    public static void createAnnex(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
        FileWriter writer = null;
        final Connection c = DataBaseManager.getConnection();
        try {
            writer = getFileWriter(idOperation, "anexo_paginas.xml");
            final ContentHandler hd = getContentHandler(writer);
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
            for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
                if (categoryForm != null) {
                    for (SiteForm siteForm : categoryForm.getSiteFormList()) {
                        if (siteForm != null) {
                            hd.startElement("", "", "portal", null);
                            final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));

                            writeTag(hd, "nombre", siteForm.getName());
                            writeTag(hd, "categoria", semillaForm.getCategoria().getName());
                            writeTag(hd, "depende_de", semillaForm.getDependencia());

                            hd.startElement("", "", "paginas", null);
                            for (PageForm pageForm : siteForm.getPageList()) {
                                if (pageForm != null) {
                                    hd.startElement("", "", "pagina", null);

                                    writeTag(hd, "url", pageForm.getUrl());
                                    writeTag(hd, "puntuacion", pageForm.getScore());
                                    writeTag(hd, "adecuacion", ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));

                                    hd.endElement("", "", "pagina");
                                }
                            }
                            hd.endElement("", "", "paginas");

                            hd.endElement("", "", "portal");
                        }
                    }
                }
            }

            hd.endElement("", "", "resultados");
            hd.endDocument();
        } catch (Exception e) {
            Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
            DataBaseManager.closeConnection(c);
        }
    }

    private static FileWriter getFileWriter(final Long idOperation, final String filename) throws IOException {
        final PropertiesManager pmgr = new PropertiesManager();
        final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + filename);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
        }
        return new FileWriter(file);
    }

    private static ContentHandler getContentHandler(final FileWriter writer) throws IOException {
        final XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat("XML", "UTF-8", true));
        return serializer.asContentHandler();
    }

    private static void writeTag(final ContentHandler contentHandler, final String tagName, final String text) throws SAXException {
        contentHandler.startElement("", "", tagName, null);
        if (text != null) {
            contentHandler.characters(text.toCharArray(), 0, text.length());
        }
        contentHandler.endElement("", "", tagName);
    }

    private static Map<Long, TreeMap<String, ScoreForm>> createAnnexMap(final Long idObsExecution) {
        final Map<Long, TreeMap<String, ScoreForm>> seedMap = new HashMap<>();
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();
            final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryFormFromExecution(c, idObsExecution);
            final ObservatorioRealizadoForm executedObservatory = ObservatorioDAO.getFulfilledObservatory(c, observatoryForm.getId(), idObsExecution);
            final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, observatoryForm.getId(), Constants.NO_PAGINACION, executedObservatory.getFecha(), false);

            final List<ObservatoryForm> observatoryFormList = new ArrayList<>();
            for (ObservatorioRealizadoForm orForm : observatoriesList) {
                observatoryFormList.add(ObservatoryExportManager.getObservatory(orForm.getId()));
            }

            for (ObservatoryForm observatory : observatoryFormList) {
                for (CategoryForm category : observatory.getCategoryFormList()) {
                    for (SiteForm siteForm : category.getSiteFormList()) {
                        final ScoreForm scoreForm = new ScoreForm();
                        scoreForm.setLevel(siteForm.getLevel());
                        scoreForm.setTotalScore(new BigDecimal(siteForm.getScore()));
                        TreeMap<String, ScoreForm> seedInfo = new TreeMap<>();
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

    private static String changeLevelName(final String name, final MessageResources messageResources) {
        if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.aa.old"))) {
            return messageResources.getMessage("resultados.anonimos.num.portales.aa");
        } else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.nv.old"))) {
            return messageResources.getMessage("resultados.anonimos.num.portales.nv");
        } else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.a.old"))) {
            return messageResources.getMessage("resultados.anonimos.num.portales.a");
        } else {
            return "";
        }
    }

    public static void createAnnex2Ev(final MessageResources request, final Long idObsExecution, final Long idOperation) throws Exception {
        final FileWriter writer = getFileWriter(idOperation, "anexo_portales.xml");
        final ContentHandler hd = getContentHandler(writer);
        hd.startDocument();
        hd.startElement("", "", "resultados", null);

        Connection c = null;
        try {
            final Map<Long, TreeMap<String, ScoreForm>> annexmap = createAnnexMap(idObsExecution);
            c = DataBaseManager.getConnection();
            for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
                final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
                if (semillaForm.getId() != 0) {
                    hd.startElement("", "", "portal", null);

                    writeTag(hd, "nombre", semillaForm.getNombre());
                    writeTag(hd, "categoria", semillaForm.getCategoria().getName());
                    writeTag(hd, "depende_de", semillaForm.getDependencia());
                    writeTag(hd, "semilla", semillaForm.getListaUrls().get(0));

                    for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
                        final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
                        writeTag(hd, "puntuacion_" + executionDateAux, entry.getValue().getTotalScore().toString());
                        writeTag(hd, "adecuacion_" + executionDateAux, changeLevelName(entry.getValue().getLevel(), request));
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

    public static void createMultilanguageAnnexes(final Long idObsExecution, final Long idOperation) {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            Logger.putLog("Creando anexo portales multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguageSiteAnnex(conn, idObsExecution, idOperation, false);
            Logger.putLog("Creando anexo portales completo multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguageSiteAnnex(conn, idObsExecution, idOperation, true);
            Logger.putLog("Creando anexo páginas multilingüismo", AnnexUtils.class, Logger.LOG_LEVEL_INFO);
            createMultilanguagePageAnnex(conn, idObsExecution, idOperation);
        } catch (Exception e) {
            Logger.putLog("Error al conectar o recuperar datos de la BBDD al crear los XML de resultados", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    private static void createMultilanguagePageAnnex(final Connection conn, final Long idObsExecution, final Long idOperation) {
        FileWriter writer = null;
        try {
            writer = getFileWriter(idOperation, "anexo_paginas_multilanguage_completo.xml");
            final ContentHandler hd = getContentHandler(writer);
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, idObsExecution, Constants.COMPLEXITY_SEGMENT_NONE);
            for (Long id : listExecutionsIds) {
                List<AnalysisForm> analysisList = AnalysisManager.getObservatoryAnalysisList(id);
                if (analysisList != null && !analysisList.isEmpty()) {
                    hd.startElement("", "", "portal", null);

                    final FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(conn, Long.parseLong(analysisList.get(0).getIdCrawling()));

                    writeTag(hd, "nombre", fulfilledCrawlingForm.getSeed().getNombre());
                    writeTag(hd, "categoria", fulfilledCrawlingForm.getSeed().getCategoria().getName());
                    writeTag(hd, "depende_de", fulfilledCrawlingForm.getSeed().getDependencia());

                    hd.startElement("", "", "paginas", null);

                    for (AnalysisForm analysisForm : analysisList) {
                        hd.startElement("", "", "pagina", null);

                        writeTag(hd, "url", analysisForm.getUrl());

                        final List<LanguageFoundForm> languagesFoundList = MultilanguageUtils.sortLanguagesFoundForm(analysisForm.getLanguagesFound());
                        for (LanguageFoundForm languageFoundForm : languagesFoundList) {
                            hd.startElement("", "", "lenguaje", null);

                            writeTag(hd, "nombre", languageFoundForm.getLanguage().getName());
                            writeTag(hd, "url_destino", languageFoundForm.getHref());
                            writeTag(hd, "cod_idioma", languageFoundForm.getDeclarationLang() != null ? languageFoundForm.getDeclarationLang() : "No declarado");
                            writeTag(hd, "idioma_texto", languageFoundForm.getLanguageSuspected().getName() != null ? languageFoundForm.getLanguageSuspected().getName() : "Sin texto");
                            writeTag(hd, "traduccion", languageFoundForm.isCorrect() ? "Correcta" : "Incorrecta");

                            hd.endElement("", "", "lenguaje");
                        }

                        hd.endElement("", "", "pagina");
                    }

                    hd.endElement("", "", "paginas");

                    hd.endElement("", "", "portal");
                } else {
                    final String noResults = "Portal: " + RastreoDAO.getFullfilledCrawlingExecution(conn, id).getSeed().getNombre() + " Sin Resultados";
                    writeTag(hd, "portal", noResults);
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

    private static void createMultilanguageSiteAnnex(final Connection conn, final Long idObsExecution, final Long idOperation, boolean complet) {
        FileWriter writer = null;
        try {
            if (!complet) {
                writer = getFileWriter(idOperation, "anexo_portales_multilanguage.xml");
            } else {
                writer = getFileWriter(idOperation, "anexo_portales_multilanguage_completo.xml");
            }
            final ContentHandler hd = getContentHandler(writer);
            hd.startDocument();
            hd.startElement("", "", "resultados", null);

            final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, idObsExecution, Constants.COMPLEXITY_SEGMENT_NONE);
            for (Long id : listExecutionsIds) {
                final List<AnalysisForm> analysisList = AnalysisManager.getObservatoryAnalysisList(id);
                if (analysisList != null && !analysisList.isEmpty()) {
                    hd.startElement("", "", "portal", null);

                    final FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(conn, Long.parseLong(analysisList.get(0).getIdCrawling()));

                    writeTag(hd, "nombre", fulfilledCrawlingForm.getSeed().getNombre());
                    final String seed = fulfilledCrawlingForm.getSeed().getListaUrlsString().split(";")[0];
                    writeTag(hd, "url_semilla", seed);
                    writeTag(hd, "segmento", fulfilledCrawlingForm.getSeed().getCategoria().getName());
                    writeTag(hd, "depende_de", fulfilledCrawlingForm.getSeed().getDependencia());

                    final List<SiteTranslationInformationForm> translationLanguagesFormList = MultilanguageUtils.getPortalTraductionInformation(analysisList, complet);

                    for (SiteTranslationInformationForm tLanguage : translationLanguagesFormList) {
                        hd.startElement("", "", "lenguaje", null);

                        writeTag(hd, "nombre", tLanguage.getName());
                        final String noTraducidas = tLanguage.getNoTranslationPercentage().toString() + "%";
                        writeTag(hd, "no_traducidas", noTraducidas);
                        final String bienTraducidas = tLanguage.getCorrectTranslationPercentage().toString() + "%";
                        writeTag(hd, "bien_traducidas", bienTraducidas);
                        final String malTraducidas = tLanguage.getNoCorrectTranslationPercentage().toString() + "%";
                        writeTag(hd, "mal_traducidas", malTraducidas);

                        if (complet) {
                            final String codigoIdioma = tLanguage.getDeclarationGreen().toString() + "%";
                            writeTag(hd, "codigo_idioma", codigoIdioma);
                            final String noCodigoIdioma = tLanguage.getDeclarationRed().toString() + "%";
                            writeTag(hd, "no_codigo_idioma", noCodigoIdioma);
                            final String trTexto = tLanguage.getTextTranslationGreen().toString() + "%";
                            writeTag(hd, "traduccion_texto", trTexto);
                            final String noTrTexto = tLanguage.getTextTranslationRed().toString() + "%";
                            writeTag(hd, "no_traduccion_texto", noTrTexto);
                            final String traduccion = tLanguage.getTranslationGreen().toString() + "%";
                            writeTag(hd, "traduccion", traduccion);
                            final String noTraduccion = tLanguage.getTranslationRed().toString() + "%";
                            writeTag(hd, "no_traduccion", noTraduccion);
                        }

                        hd.endElement("", "", "lenguaje");
                    }

                    hd.endElement("", "", "portal");
                } else {
                    final String noResults = "Portal: " + RastreoDAO.getFullfilledCrawlingExecution(conn, id).getSeed().getNombre() + " Sin Resultados";
                    writeTag(hd, "portal", noResults);
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