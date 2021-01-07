/* ******************************************************************************
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
 ***************************************************************************** */
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CATEGORY_NAME;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.struts.util.MessageResources;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
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
    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";
    /**
     * The Constant RESULTADOS_ELEMENT.
     */
    private static final String RESULTADOS_ELEMENT = "resultados";
    /**
     * The Constant NOMBRE_ELEMENT.
     */
    private static final String NOMBRE_ELEMENT = "nombre";
    /**
     * The Constant CATEGORIA_ELEMENT.
     */
    private static final String CATEGORIA_ELEMENT = "categoria";
    /**
     * The Constant DEPENDE_DE_ELEMENT.
     */
    private static final String DEPENDE_DE_ELEMENT = "depende_de";
    /**
     * The Constant PORTAL_ELEMENT.
     */
    private static final String PORTAL_ELEMENT = "portal";
    /**
     * Excel lines created by generation Evolution and
     * reused generating PerDependency annex.
     */
    private static HashMap<Integer, ExcelLine> excelLines;
    /**
     * Column names list created by generation Evolution and
     * reused generating PerDependency annex.
     */
    private static List<String> ColumnNames = new ArrayList<>();
    /**
     * Execution dates list created by generation Evolution and
     * reused generating PerDependency annex.
     */
    private static List<String> executionDates = new ArrayList<>();
    /**
     * Dependency names list created by generation Evolution and
     * reused generating PerDependency annex.
     */
    private static List<String> dependencies = new ArrayList<>();

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
                            StringBuilder dependencias = new StringBuilder();
                            if (semillaForm.getDependencias() != null) {
                                for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
                                    dependencias.append(semillaForm.getDependencias().get(i).getName());
                                    if (i < semillaForm.getDependencias().size() - 1) {
                                        dependencias.append("\n");
                                    }
                                }
                            }
                            writeTag(hd, DEPENDE_DE_ELEMENT, dependencias.toString());
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
                    StringBuilder dependencias = new StringBuilder();
                    if (semillaForm.getDependencias() != null) {
                        for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
                            dependencias.append(semillaForm.getDependencias().get(i).getName());
                            if (i < semillaForm.getDependencias().size() - 1) {
                                dependencias.append("\n");
                            }
                        }
                    }
                    writeTag(hd, DEPENDE_DE_ELEMENT, dependencias.toString());
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
                        for (EtiquetaForm tmp : etiquetas) {
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
                    if (!tagsTematica.isEmpty()) {
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
                    if (!tagsDistribucion.isEmpty()) {
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
                    if (!tagsRecurrencia.isEmpty()) {
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
                    if (!tagsOtros.isEmpty()) {
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

    /**
     * Creates the XLSX annex.
     *
     * @param messageResources the message resources
     * @param idObsExecution   the id obs execution
     * @param idOperation      the id operation
     * @throws Exception the exception
     */
    public static void createAnnexXLSX(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
        try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "Adecuación de SW por segmentos.xls.xlsx")) {

            final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
            final String ObservatoryFormDate = observatoryForm.getDate().substring(0,10);
            final String[] ColumnNames = new String[]{"nombre", "namecat", "depende_de", "semilla", "puntuacion_"+ObservatoryFormDate, "adecuacion_"+ObservatoryFormDate, "cumplimiento_"+ObservatoryFormDate, "NV_"+ObservatoryFormDate, "A_"+ObservatoryFormDate, "AA_"+ObservatoryFormDate, "NC_"+ObservatoryFormDate, "PC_"+ObservatoryFormDate, "TC_"+ObservatoryFormDate};

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Hoja1");
            XSSFRow row;
            XSSFCell cell;
            int rowIndex = 0;
            int columnIndex = 0;

            //create header cell style
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE .getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //create light shadow cell style
            CellStyle shadowStyle = wb.createCellStyle();
            shadowStyle.setWrapText(true);
            shadowStyle.setAlignment(HorizontalAlignment.LEFT);
            shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
            shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Add headers
            row = sheet.createRow(rowIndex);
            for (String name : ColumnNames ) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(name);
                cell.setCellStyle(headerStyle);
                columnIndex++;
            }

            // The sheet already has headers, so we start in the second row.
            rowIndex++;
            int categoryStarts;

            for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
                categoryStarts = rowIndex;
                if (categoryForm != null) {
                    for (SiteForm siteForm : categoryForm.getSiteFormList()) {
                        if (siteForm != null) {
                            // Multidependence
                            final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));
                            StringBuilder dependencias = new StringBuilder();
                            if (semillaForm.getDependencias() != null) {
                                for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
                                    dependencias.append(semillaForm.getDependencias().get(i).getName());
                                    if (i < semillaForm.getDependencias().size() - 1) {
                                        dependencias.append("\n");
                                    }
                                }
                            }
                            // page per row
                            for (PageForm pageForm : siteForm.getPageList()) {
                                if (pageForm != null) {

                                    row = sheet.createRow(rowIndex);
                                    int excelRowNumber = rowIndex + 1;

                                    // "nombre"
                                    cell = row.createCell(0);
                                    cell.setCellValue(siteForm.getName());
                                    cell.setCellStyle(shadowStyle);

                                    // "namecat"
                                    cell = row.createCell(1);
                                    cell.setCellValue(categoryForm.getName());
                                    cell.setCellStyle(shadowStyle);

                                    // "depende_de"
                                    cell = row.createCell(2);
                                    cell.setCellValue(dependencias.toString());
                                    cell.setCellStyle(shadowStyle);

                                    // "semilla"
                                    cell = row.createCell(3);
                                    cell.setCellValue(pageForm.getUrl());
                                    cell.setCellStyle(shadowStyle);

                                    // "puntuacion_" + date
                                    cell = row.createCell(4);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellValue(Double.parseDouble(pageForm.getScore()));
                                    cell.setCellStyle(shadowStyle);

                                    // "adecuacion_" + date
                                    cell = row.createCell(5);
                                    cell.setCellValue(ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));
                                    cell.setCellStyle(shadowStyle);

                                    // "cumplimiento_" + date
                                    cell = row.createCell(6);
                                    cell.setCellValue(siteForm.getCompliance());
                                    cell.setCellStyle(shadowStyle);

                                    // "NV_" + date
                                    cell = row.createCell(7);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($F" + excelRowNumber + "=\"No Válido\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    // "A_" + date
                                    cell = row.createCell(8);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($F" + excelRowNumber + "=\"A\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    // "AA_" + date
                                    cell = row.createCell(9);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($F" + excelRowNumber + "=\"AA\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    // "NC_" + date
                                    cell = row.createCell(10);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($G" + excelRowNumber + "=\"No conforme\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    // "PC_" + date
                                    cell = row.createCell(11);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($G" + excelRowNumber + "=\"Parcialmente conforme\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    // "TC_" + date
                                    cell = row.createCell(12);
                                    cell.setCellType(CellType.NUMERIC);
                                    cell.setCellFormula("IF($G" + excelRowNumber + "=\"Plenamente conforme\",$E" + excelRowNumber + ",0)");
                                    cell.setCellStyle(shadowStyle);

                                    rowIndex++;
                                }
                            }
                        }
                    }

                    // Increase width of columns to match content
                    for (int i = 0; i < ColumnNames.length; i++) {
                        sheet.autoSizeColumn(i);
                    }

                    // Create graph into the Category sheet
                    if (categoryForm.getSiteFormList().size() > 0) {
                        /*
                         * Excel allows sheet names up to 31 chars in length but other applications
                         * (such as OpenOffice) allow more. Some versions of Excel crash with names longer than 31 chars,
                         * others - truncate such names to 31 character.
                         */
                        String currentCategory = categoryForm.getName().substring(0, Math.min(categoryForm.getName().length(), 31));
                        if (wb.getSheet(currentCategory) == null) {
                            wb.createSheet(currentCategory);
                            InsertGraphIntoSheetByCategory(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, true);
                            InsertGraphIntoSheetByCategory(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, false);
                        }
                    }
                }
            }

            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            wb.write(writer);
            wb.close();
        } catch (Exception e) {
            Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    /**
     * Creates the XLSX evolution annex.
     *
     * @param messageResources the message resources
     * @param idObsExecution   the id obs execution
     * @param idOperation      the id operation
     * @throws Exception the exception
     */
    public static void createAnnexXLSX_Evolution(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
        try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "Adecuación de SW por segmentos con evolutivo.xlsx")) {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Resultados");
            XSSFRow row;
            XSSFCell cell;
            int rowIndex = 0;
            int columnIndex = 0;
            executionDates = new ArrayList<>();
            excelLines = new HashMap<>();
            ExcelLine excelLine;

            //create header cell style
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE .getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //create light shadow cell style
            CellStyle shadowStyle = wb.createCellStyle();
            shadowStyle.setWrapText(true);
            shadowStyle.setAlignment(HorizontalAlignment.LEFT);
            shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
            shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Add headers without values
            ColumnNames = new ArrayList<>();
            ColumnNames.add("nombre");
            ColumnNames.add("namecat");
            ColumnNames.add("depende_de");
            ColumnNames.add("semilla");

            row = sheet.createRow(rowIndex);
            for (String name : ColumnNames ) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(name);
                cell.setCellStyle(headerStyle);
                columnIndex++;
            }
            rowIndex++;

            final Map<Long, TreeMap<String, ScoreForm>> annexmap = createAnnexMap(idObsExecution);

            // Get all category names
            /*
             * Category names list created by generation Evolution and
             * reused generating PerDependency annex.
             */
            List<String> categories = new ArrayList<>();
            for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
                final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
                String namecat = semillaForm.getCategoria().getName();
                if (semillaForm.getId() != 0) {
                    if (!categories.contains(namecat))
                        categories.add(namecat);
                }
            }
            // Sort all category names
            Collections.sort(categories);

            // Loop to insert fixed values
            for (String currentCategory : categories) {
                for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {

                    final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
                    String namecat = semillaForm.getCategoria().getName();

                    // On each category iteration we filter the other categories.
                    if (semillaForm.getId() != 0 && namecat.equals(currentCategory)) {

                        row = sheet.createRow(rowIndex);
                        columnIndex = 0;

                        excelLine = new ExcelLine();
                        excelLine.setRowIndex(rowIndex);

                        // "nombre"
                        String name = semillaForm.getNombre();
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(name);
                        cell.setCellStyle(shadowStyle);
                        excelLine.setNombre(name);

                        // "namecat"
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(namecat);
                        cell.setCellStyle(shadowStyle);
                        excelLine.setNamecat(namecat);

                        // "depende_de"
                        // Multidependencia
                        StringBuilder dependencias = new StringBuilder();
                        if (semillaForm.getDependencias() != null) {
                            for (int i = 0; i < semillaForm.getDependencias().size(); i++) {

                                // Store all dependencies globally (we will use it in other files generation
                                if (!dependencies.contains(semillaForm.getDependencias().get(i).getName()))
                                    dependencies.add(semillaForm.getDependencias().get(i).getName());

                                dependencias.append(semillaForm.getDependencias().get(i).getName());
                                if (i < semillaForm.getDependencias().size() - 1) {
                                    dependencias.append("\n");
                                }
                            }
                        }
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(dependencias.toString());
                        cell.setCellStyle(shadowStyle);
                        excelLine.setDepende_de(dependencias.toString());

                        // "semilla"
                        String semilla = semillaForm.getListaUrls().get(0);
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(semilla);
                        cell.setCellStyle(shadowStyle);
                        excelLine.setSemilla(semilla);

                        for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
                            final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
                            if (!executionDates.contains(executionDateAux))
                                executionDates.add(executionDateAux);

                            double score = Double.parseDouble(entry.getValue().getTotalScore().toString());
                            String adequacy = changeLevelName(entry.getValue().getLevel(), messageResources);
                            String compliance = entry.getValue().getCompliance();

                            ExcelExecution execution = new ExcelExecution();
                            execution.setDate(executionDateAux);
                            execution.setScore(score);
                            execution.setAdequacy(adequacy);
                            execution.setCompliance(compliance);
                            excelLine.addExecution(execution);

                            // PUNTUACIÓN
                            // Add header if it is not already created
                            if (!ColumnNames.contains("puntuacion_" + executionDateAux)) {
                                ColumnNames.add("puntuacion_" + executionDateAux);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("puntuacion_" + executionDateAux);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(columnIndex++);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(score);
                            cell.setCellStyle(shadowStyle);

                            // ADECUACIÓN
                            // Add header if it is not already created
                            if (!ColumnNames.contains("adecuacion_" + executionDateAux)) {
                                ColumnNames.add("adecuacion_" + executionDateAux);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("adecuacion_" + executionDateAux);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(columnIndex++);
                            cell.setCellValue(adequacy);
                            cell.setCellStyle(shadowStyle);


                            // CUMPLIMIENTO
                            // Add header if it is not already created
                            if (!ColumnNames.contains("cumplimiento_" + executionDateAux)) {
                                ColumnNames.add("cumplimiento_" + executionDateAux);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("cumplimiento_" + executionDateAux);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(columnIndex++);
                            cell.setCellValue(compliance);
                            cell.setCellStyle(shadowStyle);
                        }

                        excelLines.put(rowIndex, excelLine);
                        rowIndex++;
                    }
                }
            }

            // Loop to insert executions values.
            rowIndex = 0;
            for (String currentCategory : categories) {
                for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {

                    final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());

                    // On each category iteration we filter the other categories.
                    if (semillaForm.getId() != 0 && semillaForm.getCategoria().getName().equals(currentCategory)) {
                        rowIndex++;
                        int numberOfDate = 0;
                        for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {

                            final String date = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");

                            row = sheet.getRow(rowIndex);
                            String columnFirstLetter = GetExcelColumnNameForNumber(6 + (3 * executionDates.indexOf(date)));
                            String columnSecondLetter = GetExcelColumnNameForNumber(5 + (3 * executionDates.indexOf(date)));


                            // "NV_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("NV_" + date)) {
                                ColumnNames.add("NV_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("NV_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate));
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No Válido\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            // "A_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("A_" + date)) {
                                ColumnNames.add("A_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("A_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 1);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"A\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            // "AA_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("AA_" + date)) {
                                ColumnNames.add("AA_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("AA_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 2);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"AA\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            columnFirstLetter = GetExcelColumnNameForNumber(7 + (3 * executionDates.indexOf(date)));

                            // "NC_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("NC_" + date)) {
                                ColumnNames.add("NC_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("NC_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 3);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            // "PC_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("PC_" + date)) {
                                ColumnNames.add("PC_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("PC_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 4);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Parcialmente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            // "TC_" + date
                            // Add header if it is not already created
                            if (!ColumnNames.contains("TC_" + date)) {
                                ColumnNames.add("TC_" + date);
                                XSSFRow headerRow = sheet.getRow(0);
                                XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
                                cellInHeader.setCellValue("TC_" + date);
                                cellInHeader.setCellStyle(headerStyle);
                            }
                            cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 5);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Plenamente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
                            cell.setCellStyle(shadowStyle);

                            numberOfDate++;
                        }
                    }
                }
            }

            // Loop to insert puntuation evolution.
            ColumnNames.add("evol_puntuacion_ant");
            XSSFRow headerRow = sheet.getRow(0);
            XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
            cellInHeader.setCellValue("evol_puntuacion_ant");
            cellInHeader.setCellStyle(headerStyle);
            rowIndex = 1;

            for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {

                final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());

                // On each category iteration we filter the other categories.
                if (semillaForm.getId() != 0) {

                    row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Discard rows without the last execution
                        XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
                        if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {

                            String columnFirstLetter = GetExcelColumnNameForNumber(5);
                            String columnSecondLetter = GetExcelColumnNameForNumber(5 + (3 * executionDates.size() - 3));

                            cell = row.createCell(ColumnNames.size() - 1);
                            String formula = "IF(" + columnSecondLetter + ":" + columnSecondLetter + "=\"\",\"\",IF((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<=-0.5,\"EMPEORA\",IF((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<=0.5,\"SE MANTIENE\",\"MEJORA\")))";
                            cell.setCellFormula(formula);
                            cell.setCellStyle(shadowStyle);
                        }
                    }
                }
                rowIndex++;
            }

            // Loop to insert adecuation evolution.
            ColumnNames.add("evol_adecuacion_ant");
            headerRow = sheet.getRow(0);
            cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
            cellInHeader.setCellValue("evol_adecuacion_ant");
            cellInHeader.setCellStyle(headerStyle);
            rowIndex = 1;

            for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {

                final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());

                // On each category iteration we filter the other categories.
                if (semillaForm.getId() != 0) {

                    row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Discard rows without the last execution
                        XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
                        if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {

                            String columnFirstLetter = GetExcelColumnNameForNumber(6);
                            String columnSecondLetter = GetExcelColumnNameForNumber(6 + (3 * executionDates.size() - 3));

                            cell = row.createCell(ColumnNames.size() - 1);
                            String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + annexmap.entrySet().size() + "=\"No Válido\",0,IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + annexmap.entrySet().size() + "=\"Prioridad 1\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$419=\"No Válido\",0,IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$" + annexmap.entrySet().size() + "=\"Prioridad 1\",1,3))";
                            cell.setCellFormula(formula);
                            cell.setCellStyle(shadowStyle);
                        }
                    }
                }
                rowIndex++;
            }

            int nextStartPos = InsertSummaryTable(sheet, rowIndex + 5, ColumnNames, headerStyle, shadowStyle);

            nextStartPos = InsertCategoriesTable(sheet, nextStartPos + 5, categories, headerStyle, shadowStyle, rowIndex, ColumnNames.size() - 1);

            // Insert graph sheets per category
            for (String category : categories) {

                /*
                 * Excel allows sheet names up to 31 chars in length but other applications
                 * (such as OpenOffice) allow more. Some versions of Excel crash with names longer than 31 chars,
                 * others - truncate such names to 31 character.
                 */
                String categorySheetName = category.substring(0, Math.min(category.length(), 31));

                // Search category initial and final row.
                int categoryFirstRow = 0;
                int categoryLastRow = 0;
                for (int i = 0; i < rowIndex; i++)
                {
                    row = sheet.getRow(i);
                    if (row != null)
                    {
                        cell = row.getCell(1);
                        if (categoryFirstRow == 0 && cell.getStringCellValue().equals(category))
                            categoryFirstRow = i;
                        if (categoryLastRow == 0 && !cell.getStringCellValue().equals(category) && categoryFirstRow != 0)
                            categoryLastRow = i;
                    }
                }
                if (categories.indexOf(category) == categories.size() - 1)
                    categoryLastRow = rowIndex;

                if (wb.getSheet(categorySheetName) == null && categoryFirstRow != 0 && categoryLastRow != 0) {
                    wb.createSheet(categorySheetName);
                    XSSFSheet currentSheet = wb.getSheet(categorySheetName);

                    InsertGraphIntoSheetByEvolution(wb, currentSheet, categoryFirstRow, categoryLastRow, true);
                    InsertGraphIntoSheetByEvolution(wb, currentSheet, categoryFirstRow, categoryLastRow, false);
                }
            }

            // Increase width of columns to match content
            for (int i = 0; i < ColumnNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            wb.write(writer);
            wb.close();

        } catch (Exception e) {
            Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    /**
     * Creates the XLSX evolution annex per dependency.
     * NEEDS THE EXECUTION OF createAnnexXLSX_Evolution METHOD PREVIOULY
     * TO CONSTRUCT THE DATA DICTIONARY FROM DATABASE INFO.
     *
     * @param idOperation      the id operation
     * @throws Exception the exception
     */
    public static void createAnnexXLSX_PerDependency(final Long idOperation) throws Exception {

        // Iterate through dependencies to create each file
        for (String currentDependency : dependencies)
        {
            try (FileOutputStream writer = getFileOutputStream(idOperation,"/Dependencias/" + currentDependency + ".xlsx")) {

                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Resultados");
                XSSFRow row;
                XSSFCell cell;
                int rowIndex = 0;
                int columnIndex = 0;

                //create header cell style
                CellStyle headerStyle = wb.createCellStyle();
                headerStyle.setWrapText(true);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE .getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                //create light shadow cell style
                CellStyle shadowStyle = wb.createCellStyle();
                shadowStyle.setWrapText(true);
                shadowStyle.setAlignment(HorizontalAlignment.LEFT);
                shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
                shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
                shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                row = sheet.createRow(rowIndex);
                for (int i = 0; i < ColumnNames.size() - 2; i++) {
                    cell = row.createCell(columnIndex);
                    cell.setCellValue(ColumnNames.get(i));
                    cell.setCellStyle(headerStyle);
                    columnIndex++;
                }
                rowIndex++;

                for (Map.Entry<Integer, ExcelLine> currentLine : excelLines.entrySet()) {

                    // On each dependency iteration we filter other dependencies.
                    if (currentLine.getValue().getDepende_de().contains(currentDependency)) {

                        row = sheet.createRow(rowIndex);
                        columnIndex = 0;

                        // "nombre"
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(currentLine.getValue().getNombre());
                        cell.setCellStyle(shadowStyle);

                        // "namecat"
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(currentLine.getValue().getNamecat());
                        cell.setCellStyle(shadowStyle);

                        // "depende_de"
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(currentLine.getValue().getDepende_de());
                        cell.setCellStyle(shadowStyle);

                        // "semilla"
                        cell = row.createCell(columnIndex++);
                        cell.setCellValue(currentLine.getValue().getSemilla());
                        cell.setCellStyle(shadowStyle);

                        for (String date : executionDates) {

                            cell = row.createCell(columnIndex++);
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellStyle(shadowStyle);
                            if (currentLine.getValue().HasDate(date)) {
                                cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getScore());
                            }

                            cell = row.createCell(columnIndex++);
                            cell.setCellStyle(shadowStyle);
                            if (currentLine.getValue().HasDate(date)) {
                                cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getAdequacy());
                            }

                            cell = row.createCell(columnIndex++);
                            cell.setCellStyle(shadowStyle);
                            if (currentLine.getValue().HasDate(date)) {
                                cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getCompliance());
                            }
                        }

                        rowIndex++;
                    }
                }

                // Insert NV, A and AA columns for each execution.
                for (int i = 1; i < rowIndex; i++) {

                    row = sheet.getRow(i);

                    for (int numberOfDate = 0; numberOfDate < executionDates.size(); numberOfDate++) {

                        String columnFirstLetter = GetExcelColumnNameForNumber(6 + (3 * numberOfDate));
                        String columnSecondLetter = GetExcelColumnNameForNumber(5 + (3 * numberOfDate));

                        // "NV_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate));
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"No Válido\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);

                        // "A_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 1);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"A\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);

                        // "AA_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 2);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"AA\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);

                        columnFirstLetter = GetExcelColumnNameForNumber(6 + (3 * numberOfDate) + 1);

                        // "NC_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 3);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"No conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);

                        // "PC_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 4);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"Parcialmente conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);

                        // "PC_" + date
                        cell = row.createCell(4 + (3 * executionDates.size()) + (6 * numberOfDate) + 5);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"Plenamente conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
                        cell.setCellStyle(shadowStyle);
                    }
                }

                // Increase width of columns to match content
                for (int i = 0; i < ColumnNames.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                XSSFSheet currentSheet = wb.createSheet("Grafica Adecuación");

                XSSFDrawing drawing = currentSheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, Math.max(rowIndex, 16), 40);

                XSSFChart chart = drawing.createChart(anchor);
                chart.setTitleText("Evolución de la adecuación");
                chart.setTitleOverlay(false);

                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.LEFT);

                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);

                XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
                bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
                leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
                bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
                bottomAxis.setMajorTickMark(AxisTickMark.NONE);
                bottomAxis.setPosition(AxisPosition.RIGHT);
                CTPlotArea plotArea = chart.getCTChart().getPlotArea();
                plotArea.getValAxArray()[0].addNewMajorGridlines();

                // Get agency names
                XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0),
                        new CellRangeAddress(1, rowIndex - 1, 0, 0));

                // Iterate through the executions
                for (String date : executionDates){

                    int firstSerieColumn = 4 + (executionDates.size() * 3) + (3 * executionDates.indexOf(date));

                    // First serie ("No válido" / "No Conforme")
                    FillNullCellInRange(wb.getSheetAt(0), 1, rowIndex - 1, firstSerieColumn);
                    XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                            new CellRangeAddress(1, rowIndex - 1, firstSerieColumn, firstSerieColumn));
                    XDDFChartData.Series series1 = data.addSeries(agencies, values1);
                    series1.setTitle("NV_" + date, null);
                    // Set series color
                    XDDFShapeProperties properties1 = series1.getShapeProperties();
                    if (properties1 == null) {
                        properties1 = new XDDFShapeProperties();
                    }
                    properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
                    series1.setShapeProperties(properties1);

                    // Second serie ("A" / "Parcialmente conforme")
                    FillNullCellInRange(wb.getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + 1);
                    XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                            new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + 1, firstSerieColumn + 1));
                    XDDFChartData.Series series2 = data.addSeries(agencies, values2);
                    series2.setTitle("A_" + date, null);
                    // Set series color
                    XDDFShapeProperties properties2 = series2.getShapeProperties();
                    if (properties2 == null) {
                        properties2 = new XDDFShapeProperties();
                    }
                    properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
                    series2.setShapeProperties(properties2);

                    // Third serie ("AA" / "Plenamente conforme")
                    FillNullCellInRange(wb.getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + 2);
                    XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                            new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + 2, firstSerieColumn + 2));
                    XDDFChartData.Series series3 = data.addSeries(agencies, values3);
                    series3.setTitle("AA_" + date, null);
                    // Set series color
                    XDDFShapeProperties properties3 = series3.getShapeProperties();
                    if (properties3 == null) {
                        properties3 = new XDDFShapeProperties();
                    }
                    properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
                    series3.setShapeProperties(properties3);
                }

                chart.plot(data);

                XDDFBarChartData bar = (XDDFBarChartData) data;
                bar.setBarDirection(BarDirection.COL);


                XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
                wb.write(writer);
                wb.close();

            } catch (Exception e) {
                Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
        }
    }

    private static void FillNullCellInRange(XSSFSheet sheetAt, int categoryFirstRow, int categoryLastRow, int firstSerieColumn) {
        for (int i = categoryFirstRow; i <= categoryLastRow; i++){

            XSSFRow row = sheetAt.getRow(i);

            if (row != null){
                XSSFCell cell = row.getCell(firstSerieColumn);
                if (cell == null)
                {
                    XSSFCell c =  row.createCell(firstSerieColumn);
                    c.setCellValue(-1);
                }
            }
        }
    }

    private static int InsertSummaryTable(XSSFSheet sheet, int RowStartPosition, List<String> ColumnNames, CellStyle headerStyle, CellStyle shadowStyle) {
        XSSFCell cell;
        XSSFRow row;
        // Insert Summary table.
        String columnResumeName = GetExcelColumnNameForNumber(ColumnNames.size());

        row = sheet.createRow(RowStartPosition);
        cell = row.createCell(0);
        cell.setCellValue("Datos de evolución de portales por nivel de adecuación");
        cell.setCellStyle(headerStyle);

        row = sheet.createRow(RowStartPosition + 1);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("De NV a P1");
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",1)");

        row = sheet.createRow(RowStartPosition + 2);
        cell = row.createCell(0);
        cell.setCellValue("De NV a P2");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",3)");

        row = sheet.createRow(RowStartPosition + 3);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("De P1 a P2");
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",2)");

        row = sheet.createRow(RowStartPosition + 4);
        cell = row.createCell(0);
        cell.setCellValue("De P2 a P1");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",-2)");

        row = sheet.createRow(RowStartPosition + 5);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("De P2 a NV");
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",-3)");

        row = sheet.createRow(RowStartPosition + 6);
        cell = row.createCell(0);
        cell.setCellValue("De P1 a NV");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellStyle(shadowStyle);
        cell.setCellFormula("COUNTIF(" + columnResumeName + ":" + columnResumeName + ",-1)");

        row = sheet.createRow(RowStartPosition + 7);
        cell = row.createCell(0);
        cell.setCellValue("Total cambian: ");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellFormula("SUM(B" + (RowStartPosition + 2) + ":B" + (RowStartPosition + 7) + ")");

        return RowStartPosition + 7;
    }

    private static int InsertCategoriesTable(XSSFSheet sheet, int RowStartPosition, List<String> categories,CellStyle headerStyle, CellStyle shadowStyle, int lastDataRow, int columnSourceData) {
        XSSFCell cell;
        XSSFRow row;

        // Insert Summary table.
        row = sheet.createRow(RowStartPosition);
        cell = row.createCell(0);
        cell.setCellValue("Datos de evolución de portales respecto a la primera iteración/anterior iteración (Empeora, Se mantiene, Mejora) por segmento. Diferencia 0,5 puntos");
        cell.setCellStyle(headerStyle);

        row = sheet.createRow(RowStartPosition + 1);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Segmento");
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Empeoran");
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Mejoran");
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Se mantiene");
        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);

        String dataColumn = GetExcelColumnNameForNumber(columnSourceData);

        for (int i = 0; i < categories.size(); i++)
        {
            row = sheet.createRow(RowStartPosition+ i + 2);

            cell = row.createCell(0);
            cell.setCellStyle(shadowStyle);
            cell.setCellValue(categories.get(i));

            cell = row.createCell(1);
            cell.setCellStyle(shadowStyle);
            cell.setCellFormula("COUNTIFS($B$2:$B$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"EMPEORA\")");

            cell = row.createCell(2);
            cell.setCellStyle(shadowStyle);
            cell.setCellFormula("COUNTIFS($B$2:$B$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"MEJORA\")");

            cell = row.createCell(3);
            cell.setCellStyle(shadowStyle);
            cell.setCellFormula("COUNTIFS($B$2:$B$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"SE MANTIENE\")");
        }

        // TOTAL row
        row = sheet.createRow(RowStartPosition + categories.size() + 2);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("TOTAL");

        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellFormula("SUM(B" + (RowStartPosition+3) + ":B" + (RowStartPosition + categories.size() + 2) + ")");

        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellFormula("SUM(C" + (RowStartPosition+3) + ":C" + (RowStartPosition + categories.size() + 2) + ")");

        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellFormula("SUM(D" + (RowStartPosition+3) + ":D" + (RowStartPosition + categories.size() + 2) + ")");

        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellFormula("SUM(B" + (RowStartPosition +categories.size() + 3) + ":D" + (RowStartPosition +categories.size() + 3) + ")");

        return 1;
    }

    public static String GetExcelColumnNameForNumber(int number) {
        StringBuilder sb = new StringBuilder();

        int num = number - 1;
        while (num >=  0) {
            int numChar = (num % 26)  + 65;
            sb.append((char)numChar);
            num = (num  / 26) - 1;
        }
        return sb.reverse().toString();
    }

    private static void InsertGraphIntoSheetByCategory(XSSFWorkbook wb, XSSFSheet sheet, int categoryFirstRow, int categoryLastRow, boolean isFirst) {
        if (sheet != null) {

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, isFirst ? 4 : 45, Math.max(categoryLastRow - categoryFirstRow, 16), isFirst ? 40 : 85);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText(isFirst ? "Nivel de adecuación estimado" : "Situación de cumplimiento estimada");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.LEFT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
            bottomAxis.setMajorTickMark(AxisTickMark.NONE);
            bottomAxis.setPosition(AxisPosition.RIGHT);
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            plotArea.getValAxArray()[0].addNewMajorGridlines();

            // Get agency names
            XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 0, 0));

            // First serie ("No válido" / "No Conforme")
            XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 7 : 10, isFirst ? 7 : 10));
            XDDFChartData.Series series1 = data.addSeries(agencies, values1);
            series1.setTitle(isFirst ? "No Válido" : "No Conforme", null);
            // Set series color
            XDDFShapeProperties properties1 = series1.getShapeProperties();
            if (properties1 == null) {
                properties1 = new XDDFShapeProperties();
            }
            properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
            series1.setShapeProperties(properties1);

            // Second serie ("A" / "Parcialmente conforme")
            XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 8 : 11, isFirst ? 8 : 11));
            XDDFChartData.Series series2 = data.addSeries(agencies, values2);
            series2.setTitle(isFirst ? "A" : "Parcialmente conforme", null);
            // Set series color
            XDDFShapeProperties properties2 = series2.getShapeProperties();
            if (properties2 == null) {
                properties2 = new XDDFShapeProperties();
            }
            properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
            series2.setShapeProperties(properties2);

            // Third serie ("AA" / "Plenamente conforme")
            XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 9 : 12, isFirst ? 9 : 12));
            XDDFChartData.Series series3 = data.addSeries(agencies, values3);
            series3.setTitle(isFirst ? "AA" : "Plenamente Conforme", null);
            // Set series color
            XDDFShapeProperties properties3 = series3.getShapeProperties();
            if (properties3 == null) {
                properties3 = new XDDFShapeProperties();
            }
            properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
            series3.setShapeProperties(properties3);

            chart.plot(data);

            XDDFBarChartData bar = (XDDFBarChartData) data;
            bar.setBarDirection(BarDirection.COL);
        }
    }

    private static void InsertGraphIntoSheetByEvolution(XSSFWorkbook wb, XSSFSheet currentSheet, int categoryFirstRow, int categoryLastRow, boolean isFirst)
    {
        XSSFDrawing drawing = currentSheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, isFirst ? 4 : 45, Math.max(categoryLastRow - categoryFirstRow, 16), isFirst ? 40 : 85);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(isFirst ? "Evolución de la adecuación" : "Evolución del cumplimiento estimado");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.LEFT);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
        bottomAxis.setMajorTickMark(AxisTickMark.NONE);
        bottomAxis.setPosition(AxisPosition.RIGHT);
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        plotArea.getValAxArray()[0].addNewMajorGridlines();

        // Get agency names
        XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0),
                new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 0, 0));

        // Iterate through the executions
        for (String date : executionDates){

            int firstSerieColumn = 4 + (executionDates.size() * 3) + (6 * executionDates.indexOf(date));

            // First serie ("No válido" / "No Conforme")
            FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 0 : 3));
            XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 0 : 3), firstSerieColumn + (isFirst ? 0 : 3)));
            XDDFChartData.Series series1 = data.addSeries(agencies, values1);
            series1.setTitle((isFirst ? "NV_" : "NC_") + date, null);
            // Set series color
            XDDFShapeProperties properties1 = series1.getShapeProperties();
            if (properties1 == null) {
                properties1 = new XDDFShapeProperties();
            }
            properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
            series1.setShapeProperties(properties1);

            // Second serie ("A" / "Parcialmente conforme")
            FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 1 : 4));
            XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 1 : 4), firstSerieColumn + (isFirst ? 1 : 4)));

            XDDFChartData.Series series2 = data.addSeries(agencies, values2);
            series2.setTitle((isFirst ? "A_" : "PC_") + date, null);
            // Set series color
            XDDFShapeProperties properties2 = series2.getShapeProperties();
            if (properties2 == null) {
                properties2 = new XDDFShapeProperties();
            }
            properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
            series2.setShapeProperties(properties2);

            // Third serie ("AA" / "Plenamente conforme")
            FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 2 : 5));
            XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 2 : 5), firstSerieColumn + (isFirst ? 2 : 5)));
            XDDFChartData.Series series3 = data.addSeries(agencies, values3);
            series3.setTitle((isFirst ? "AA_" : "TC_") + date, null);
            // Set series color
            XDDFShapeProperties properties3 = series3.getShapeProperties();
            if (properties3 == null) {
                properties3 = new XDDFShapeProperties();
            }
            properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
            series3.setShapeProperties(properties3);
        }

        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
    }
    /**
     * Creates the XLSX annex.
     *
     * @param messageResources the message resources
     * @param idObsExecution   the id obs execution
     * @param idOperation      the id operation
     * @throws Exception the exception
     */
    public static void createComparativeSuitabilitieXLSX(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
        /*
        try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "suitabilities.xlsx")) {
            final String[] ColumnNames = new String[]{"Grupo", "Observatorio", "Parcial", "Prioridad 1", "Prioridad 1 y 2"};
            final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Global");

            //create default cell style (aligned top left and allow line wrapping)
            CellStyle defaultStyle = wb.createCellStyle();
            defaultStyle.setWrapText(true);
            defaultStyle.setAlignment(HorizontalAlignment.LEFT);
            defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);

            int rowIndex = 0;
            int columnIndex = 0;
            XSSFRow row;
            XSSFCell cell;

            Object[][] tableData = {
                    {"Global", "AGE Mayo 2018", 28, 13, 59},
                    {"Global", "CCAA Mayo 2018", 45, 27, 28},
                    {"Global", "EELL Mayo 2018", 62, 18, 20},
                    {"Principales", "AGE Mayo 2018", 0 , 22, 78},
                    {"Principales", "CCAA Mayo 2018", 21, 26, 53},
                    {"Principales", "EELL Mayo 2018", 44, 28, 28},
                    {"Global", "AGE Noviembre 2018", 27, 14, 59},
                    {"Global", "CCAA Noviembre 2018", 45, 25, 30},
                    {"Global", "EELL Noviembre 2018", 60, 18, 22},
                    {"Principales", "AGE Noviembre 2018", 14, 9,  77},
                    {"Principales", "CCAA Noviembre 2018", 32, 26, 42},
                    {"Principales", "EELL Noviembre 2018", 45, 20, 35},
                    {"Global", "AGE Junio 2019", 25, 11, 64},
                    {"Global", "CCAA Junio 2019", 38, 30, 32},
                    {"Global", "EELL Junio 2019", 61, 16, 23},
                    {"Principales", "AGE Junio 2019", 9 , 5,  86},
                    {"Principales", "CCAA Junio 2019", 25, 30, 45},
                    {"Principales", "EELL Junio 2019", 47, 16, 37}
            };

            // Add headers
            row = sheet.createRow(rowIndex);
            for (String name : ColumnNames ) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(name);
                columnIndex++;
            }

            // [TEMPORAL] Add info manually
            rowIndex=0;
            for (Object[] dataLine : tableData) {
                row = sheet.createRow(++rowIndex);

                columnIndex = 0;
                for (Object field : dataLine) {
                    cell = row.createCell(columnIndex);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                    columnIndex++;
                }
            }
















            // determine the type of the category axis from it's first category value (value in A2 in this case)
            XDDFDataSource dataSource = null;
            CellType type = CellType.ERROR;
            row = sheet.getRow(1);
            if (row != null) {
                cell = row.getCell(0);
                if (cell != null)
                {
                    dataSource = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 10, 1, 1));
                }
            }
            if (dataSource != null) { // if no type of category axis found, don't create a chart at all
                XDDFNumericalDataSource<Double> high = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 2, 2));
                XDDFNumericalDataSource<Double> medium = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 3, 3));
                XDDFNumericalDataSource<Double> low = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 4, 4));

                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 6, 0, 16, 20);

                XSSFChart chart = drawing.createChart(anchor);
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.RIGHT);

                // bar chart

                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle("Number of defects");
                leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

                // category axis crosses the value axis between the strokes and not midpoint the strokes
                leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

                XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
                XDDFChartData.Series series1 = data.addSeries(dataSource, high);
                series1.setTitle("Parcial", new CellReference(sheet.getSheetName(), 0, 2, true, true));
                XDDFChartData.Series series2 = data.addSeries(dataSource, medium);
                series2.setTitle("Prioridad 1", new CellReference(sheet.getSheetName(), 0, 3, true, true));
                XDDFChartData.Series series3 = data.addSeries(dataSource, low);
                series3.setTitle("Prioridad 1 y 2", new CellReference(sheet.getSheetName(), 0, 4, true, true));
                chart.plot(data);

                XDDFBarChartData bar = (XDDFBarChartData) data;
                bar.setBarDirection(BarDirection.COL);

                // looking for "Stacked Bar Chart"? uncomment the following line
                bar.setBarGrouping(BarGrouping.STACKED);

                // correcting the overlap so bars really are stacked and not side by side
                chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte)100);

                solidFillSeries(data, 0, PresetColor.ORANGE);
                solidFillSeries(data, 1, PresetColor.YELLOW);
                solidFillSeries(data, 2, PresetColor.GREEN);

                // add data labels
                for (int s = 0 ; s < 3; s++) {
                    chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).addNewDLbls();
                    // chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls()
                    //        .addNewDLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.CTR);
                    chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowVal().setVal(true);
                    chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowLegendKey().setVal(false);
                    chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowCatName().setVal(false);
                    chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowSerName().setVal(false);
                }

                // line chart

                // axis must be there but must not be visible
                bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                bottomAxis.setVisible(false);
                leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setVisible(false);

                // set correct cross axis
                bottomAxis.crossAxis(leftAxis);
                leftAxis.crossAxis(bottomAxis);

                data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                chart.plot(data);
            }





















            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            wb.write(writer);
            wb.close();
        } catch (Exception e) {
            Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        */
    }

    private static void solidFillSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setFillProperties(fill);
        series.setShapeProperties(properties);
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
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

    private static class ExcelLine
    {
        private String nombre;
        private String namecat;
        private String depende_de;
        private String semilla;
        private Integer rowIndex;
        private List<ExcelExecution> executions = new ArrayList<>();

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNamecat() {
            return namecat;
        }

        public void setNamecat(String namecat) {
            this.namecat = namecat;
        }

        public String getDepende_de() {
            return depende_de;
        }

        public void setDepende_de(String depende_de) {
            this.depende_de = depende_de;
        }

        public String getSemilla() {
            return semilla;
        }

        public void setSemilla(String semilla) {
            this.semilla = semilla;
        }

        public List<ExcelExecution> getExecutions() {
            return executions;
        }

        public boolean HasDate(String dateToSearch){
            for (ExcelExecution e : executions){
                if (e.date.equals(dateToSearch)){
                    return true;
                }
            }
            return false;
        }

        public ExcelExecution GetExecutionByDate(String dateToSearch){
            for (ExcelExecution e : executions){
                if (e.date.equals(dateToSearch)){
                    return e;
                }
            }
            return null;
        }

        public void addExecution(ExcelExecution execution) {
            this.executions.add(execution);
        }

        public Integer getRowIndex() {
            return rowIndex;
        }

        public void setRowIndex(Integer rowIndex) {
            this.rowIndex = rowIndex;
        }
    }

    private static class ExcelExecution
    {
        private String date;
        private Double score;
        private String adequacy;
        private String compliance;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getScore() { return score; }

        public void setScore(Double score) {
            this.score = score;
        }

        public String getAdequacy() {
            return adequacy;
        }

        public void setAdequacy(String adequacy) {
            this.adequacy = adequacy;
        }

        public String getCompliance() { return compliance; }

        public void setCompliance(String compliance) { this.compliance = compliance; }
    }
}