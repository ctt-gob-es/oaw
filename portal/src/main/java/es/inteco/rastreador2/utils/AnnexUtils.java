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

import static es.inteco.common.Constants.CATEGORY_NAME;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.util.MessageResources;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.PageForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.manager.ObservatoryExportManager;

/**
 * The Class AnnexUtils.
 */
public final class AnnexUtils {
	/** The Constant EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The Constant RESULTADOS_ELEMENT. */
	private static final String RESULTADOS_ELEMENT = "resultados";
	/** The Constant NOMBRE_ELEMENT. */
	private static final String NOMBRE_ELEMENT = "nombre";
	/** The Constant CATEGORIA_ELEMENT. */
	private static final String CATEGORIA_ELEMENT = "categoria";
	/** The Constant DEPENDE_DE_ELEMENT. */
	private static final String DEPENDE_DE_ELEMENT = "depende_de";
	/** The Constant PORTAL_ELEMENT. */
	private static final String PORTAL_ELEMENT = "portal";

	/**
	 * Instantiates a new annex utils.
	 */
	private AnnexUtils() {
	}
	// Anexos sin iteraciones
	// *************************************************************************************

	/**
	 * Creates the annex paginas.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @throws Exception the exception
	 */
	public static void createAnnexPaginas(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo_paginas.xml")) {
			final ContentHandler hd = getContentHandler(writer);
			hd.startDocument();
			hd.startElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT, null);
			final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
			for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
				if (categoryForm != null) {
					for (SiteForm siteForm : categoryForm.getSiteFormList()) {
						if (siteForm != null) {
							hd.startElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT, null);
							final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));
							writeTag(hd, NOMBRE_ELEMENT, siteForm.getName());
							writeTag(hd, CATEGORIA_ELEMENT, semillaForm.getCategoria().getName());
							// Multidependencia
							String dependencias = "";
							if (semillaForm.getDependencias() != null) {
								for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
									dependencias += semillaForm.getDependencias().get(i).getName();
									if (i < semillaForm.getDependencias().size() - 1) {
										dependencias += "\n";
									}
								}
							}
							writeTag(hd, DEPENDE_DE_ELEMENT, dependencias);
							hd.startElement(EMPTY_STRING, EMPTY_STRING, "paginas", null);
							for (PageForm pageForm : siteForm.getPageList()) {
								if (pageForm != null) {
									hd.startElement(EMPTY_STRING, EMPTY_STRING, "pagina", null);
									writeTag(hd, "url", pageForm.getUrl());
									writeTag(hd, "puntuacion", pageForm.getScore());
									writeTag(hd, "adecuacion", ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));
									hd.endElement(EMPTY_STRING, EMPTY_STRING, "pagina");
								}
							}
							hd.endElement(EMPTY_STRING, EMPTY_STRING, "paginas");
							hd.endElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT);
						}
					}
				}
			}
			hd.endElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT);
			hd.endDocument();
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the annex portales.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @throws Exception the exception
	 */
	public static void createAnnexPortales(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo_portales.xml")) {
			final ContentHandler hd = getContentHandler(writer);
			hd.startDocument();
			hd.startElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT, null);
			final Map<Long, TreeMap<String, ScoreForm>> annexmap = createAnnexMap(idObsExecution);
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				if (semillaForm.getId() != 0) {
					hd.startElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT, null);
					writeTag(hd, NOMBRE_ELEMENT, semillaForm.getNombre());
					writeTag(hd, CATEGORY_NAME, semillaForm.getCategoria().getName());
					// Multidependencia
					String dependencias = "";
					if (semillaForm.getDependencias() != null) {
						for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
							dependencias += semillaForm.getDependencias().get(i).getName();
							if (i < semillaForm.getDependencias().size() - 1) {
								dependencias += "\n";
							}
						}
					}
					writeTag(hd, DEPENDE_DE_ELEMENT, dependencias);
					writeTag(hd, "semilla", semillaForm.getListaUrls().get(0));
					for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
						final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
						writeTag(hd, "puntuacion_" + executionDateAux, entry.getValue().getTotalScore().toString());
						writeTag(hd, "adecuacion_" + executionDateAux, changeLevelName(entry.getValue().getLevel(), messageResources));
						writeTag(hd, "cumplimiento_" + executionDateAux, entry.getValue().getCompliance());
					}
					hd.endElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT);
				}
			}
			hd.endElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT);
			hd.endDocument();
		} catch (Exception e) {
			Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the file writer.
	 *
	 * @param idOperation the id operation
	 * @param filename    the filename
	 * @return the file writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static FileWriter getFileWriter(final Long idOperation, final String filename) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + filename);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		return new FileWriter(file);
	}

	/**
	 * Gets the content handler.
	 *
	 * @param writer the writer
	 * @return the content handler
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static ContentHandler getContentHandler(final FileWriter writer) throws IOException {
		final XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat("XML", "UTF-8", true));
		return serializer.asContentHandler();
	}

	/**
	 * Write tag.
	 *
	 * @param contentHandler the content handler
	 * @param tagName        the tag name
	 * @param text           the text
	 * @throws SAXException the SAX exception
	 */
	private static void writeTag(final ContentHandler contentHandler, final String tagName, final String text) throws SAXException {
		contentHandler.startElement(EMPTY_STRING, EMPTY_STRING, tagName, null);
		if (text != null) {
			contentHandler.characters(text.toCharArray(), 0, text.length());
		}
		contentHandler.endElement(EMPTY_STRING, EMPTY_STRING, tagName);
	}

	/**
	 * Creates the annex map.
	 *
	 * @param idObsExecution the id obs execution
	 * @return the map
	 */
	private static Map<Long, TreeMap<String, ScoreForm>> createAnnexMap(final Long idObsExecution) {
		final Map<Long, TreeMap<String, ScoreForm>> seedMap = new HashMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryFormFromExecution(c, idObsExecution);
			final ObservatorioRealizadoForm executedObservatory = ObservatorioDAO.getFulfilledObservatory(c, observatoryForm.getId(), idObsExecution);
			final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, observatoryForm.getId(), Constants.NO_PAGINACION, executedObservatory.getFecha(),
					false);
			final List<ObservatoryForm> observatoryFormList = new ArrayList<>();
			for (ObservatorioRealizadoForm orForm : observatoriesList) {
				final ObservatoryForm observatory = ObservatoryExportManager.getObservatory(orForm.getId());
				if (observatory != null) {
					observatoryFormList.add(observatory);
				}
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
						// TODO Compliance
						scoreForm.setCompliance(siteForm.getCompliance());
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al recuperar las semillas del Observatorio al crear el anexo", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return seedMap;
	}

	/**
	 * Change level name.
	 *
	 * @param name             the name
	 * @param messageResources the message resources
	 * @return the string
	 */
	private static String changeLevelName(final String name, final MessageResources messageResources) {
		if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.aa.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.aa");
		} else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.nv.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.nv");
		} else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.a.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.a");
		} else {
			return EMPTY_STRING;
		}
	}
}