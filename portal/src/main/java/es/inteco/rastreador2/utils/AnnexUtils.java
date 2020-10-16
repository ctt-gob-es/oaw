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
import java.io.FileOutputStream;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
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
					// TODO Add seed tags
					List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
					List<EtiquetaForm> tagsDistribucion = new ArrayList<>(); // id=2
					List<EtiquetaForm> tagsTematica = new ArrayList<>();// id=1
					List<EtiquetaForm> tagsRecurrencia = new ArrayList<>();// id=3
					List<EtiquetaForm> tagsOtros = new ArrayList<>();// id=4
					if (etiquetas != null && !etiquetas.isEmpty()) {
						for (int i = 0; i < etiquetas.size(); i++) {
							EtiquetaForm tmp = etiquetas.get(i);
							if (tmp.getClasificacion() != null) {
								switch (tmp.getClasificacion().getId()) {
								case "1":
									tagsTematica.add(tmp);
									break;
								case "2":
									tagsDistribucion.add(tmp);
									break;
								case "3":
									tagsRecurrencia.add(tmp);
									break;
								case "4":
									tagsOtros.add(tmp);
									break;
								default:
									break;
								}
							}
						}
					}
					// 1
					hd.startElement("", "", Constants.XML_ETIQUETAS_TEMATICA, null);
					if (tagsTematica != null && !tagsTematica.isEmpty()) {
						for (int i = 0; i < tagsTematica.size(); i++) {
							hd.characters(tagsTematica.get(i).getName().toCharArray(), 0, tagsTematica.get(i).getName().length());
							if (i < tagsTematica.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_TEMATICA);
					// 2
					hd.startElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION, null);
					if (tagsDistribucion != null && !tagsDistribucion.isEmpty()) {
						for (int i = 0; i < tagsDistribucion.size(); i++) {
							hd.characters(tagsDistribucion.get(i).getName().toCharArray(), 0, tagsDistribucion.get(i).getName().length());
							if (i < tagsDistribucion.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION);
					// 3
					hd.startElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA, null);
					if (tagsRecurrencia != null && !tagsRecurrencia.isEmpty()) {
						for (int i = 0; i < tagsRecurrencia.size(); i++) {
							hd.characters(tagsRecurrencia.get(i).getName().toCharArray(), 0, tagsRecurrencia.get(i).getName().length());
							if (i < tagsRecurrencia.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA);
					// 4
					hd.startElement("", "", Constants.XML_ETIQUETAS_OTROS, null);
					if (tagsOtros != null && !tagsOtros.isEmpty()) {
						for (int i = 0; i < tagsOtros.size(); i++) {
							hd.characters(tagsOtros.get(i).getName().toCharArray(), 0, tagsOtros.get(i).getName().length());
							if (i < tagsOtros.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_OTROS);
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

	public static void createAnnexXLSX(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "anexo.xlsm")) {
			final String[] ColumnNames = new String[]{"", "namecat", "depende_de", "semilla", "puntuacion_2020-03-13", "adecuacion_2020-03-13", "cumplimiento_2020-03-13", "NV_2020-02-21", "A_2020-02-21", "AA_2020-02-21", "NC_2020-02-21", "PC_2020-02-21", "TC_2020-02-21"};

			final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);

			for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
				if (categoryForm != null) {
					for (SiteForm siteForm : categoryForm.getSiteFormList()) {
						if (siteForm != null) {
							Workbook wb = new XSSFWorkbook();

							Sheet sheet = wb.createSheet("Resultados");

							//create default cell style (aligned top left and allow line wrapping)
							CellStyle defaultStyle = wb.createCellStyle();
							defaultStyle.setWrapText(true);
							defaultStyle.setAlignment(HorizontalAlignment.LEFT);
							defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);

							int rowIndex = 0;
							int columnIndex = 0;

							// first row contains column names
							Row row = sheet.createRow(rowIndex);
							for (String columnName : ColumnNames) {
								row.createCell(columnIndex).setCellValue(columnName);
								columnIndex++;
							}
							rowIndex++;

							// page per row
							for (PageForm pageForm : siteForm.getPageList()) {
								if (pageForm != null) {
									columnIndex = 0;
									row = sheet.createRow(rowIndex);

									for (String columnName : ColumnNames) {
										Cell cell = row.createCell(columnIndex);

										switch (ColumnNames[columnIndex]) {
											case "":
												cell.setCellValue("name");
												cell.setCellStyle(defaultStyle);
												break;
											case "namecat":
												cell.setCellValue("namecat");
												cell.setCellStyle(defaultStyle);
												break;
											case "depende_de":
												cell.setCellValue("depende_de");
												cell.setCellStyle(defaultStyle);
												break;
											case "semilla":
												cell.setCellValue(pageForm.getUrl());
												cell.setCellStyle(defaultStyle);
												break;
											case "puntuacion_2020-03-13":
												cell.setCellValue(pageForm.getScore());
												cell.setCellStyle(defaultStyle);
												break;
											case "adecuacion_2020-03-13":
												cell.setCellValue(ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));
												cell.setCellStyle(defaultStyle);
												break;
											case "cumplimiento_2020-03-13":
												cell.setCellValue("cumplimiento_2020-03-13");
												cell.setCellStyle(defaultStyle);
												break;
											case "NV_2020-02-21":
												cell.setCellValue("NV_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
											case "A_2020-02-21":
												cell.setCellValue("A_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
											case "AA_2020-02-21":
												cell.setCellValue("AA_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
											case "NC_2020-02-21":
												cell.setCellValue("NC_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
											case "PC_2020-02-21":
												cell.setCellValue("PC_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
											case "TC_2020-02-21":
												cell.setCellValue("TC_2020-02-21");
												cell.setCellStyle(defaultStyle);
												break;
										}
										columnIndex++;
									}
								}

								rowIndex++;
							}

							// Increase width of columns to match content
							for (int i = 0; i < ColumnNames.length; i++) {
								sheet.autoSizeColumn(i);
							}

							wb.write(writer);
							wb.close();
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * Gets the file writer.
	 *
	 * @param idOperation the id operation
	 * @param filename    the filename
	 * @return the file writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static FileOutputStream getFileOutputStream(final Long idOperation, final String filename) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + filename);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		return new FileOutputStream(file);
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