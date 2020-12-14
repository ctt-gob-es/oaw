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

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
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

    /**
     * Creates the XLSX annex.
     *
     * @param messageResources the message resources
     * @param idObsExecution   the id obs execution
     * @param idOperation      the id operation
     * @throws Exception the exception
     */
    public static void createAnnexXLSX(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
        try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "anexo.xlsx")) {
            final String[] ColumnNames = new String[]{"nombre", "namecat", "depende_de", "semilla", "puntuacion_2020-03-13", "adecuacion_2020-03-13", "cumplimiento_2020-03-13", "NV_2020-02-21", "A_2020-02-21", "AA_2020-02-21", "NC_2020-02-21", "PC_2020-02-21", "TC_2020-02-21"};
            final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Hoja1");
            XSSFRow row;
            XSSFCell cell;
            int rowIndex = 0;
            int columnIndex = 0;

            //create default cell style (aligned top left and allow line wrapping)
            CellStyle defaultStyle = wb.createCellStyle();
            defaultStyle.setWrapText(true);
            defaultStyle.setAlignment(HorizontalAlignment.LEFT);
            defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);

            // Add headers
            row = sheet.createRow(rowIndex);
            for (String name : ColumnNames ) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(name);
                cell.setCellStyle(defaultStyle);
                columnIndex++;
            }

            // The sheet already has headers, so we start in the second row.
            rowIndex++;
            int categoryStarts = 0;

            for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
                categoryStarts = rowIndex;
                if (categoryForm != null) {
                    for (SiteForm siteForm : categoryForm.getSiteFormList()) {
                        if (siteForm != null) {
                            // Multidependence
                            final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));
                            String dependencias = "";
                            if (semillaForm.getDependencias() != null) {
                                for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
                                    dependencias += semillaForm.getDependencias().get(i).getName();
                                    if (i < semillaForm.getDependencias().size() - 1) {
                                        dependencias += "\n";
                                    }
                                }
                            }

                            // page per row
                            for (PageForm pageForm : siteForm.getPageList()) {
                                if (pageForm != null) {
                                    columnIndex = 0;
                                    row = sheet.createRow(rowIndex);

                                    for (String columnName : ColumnNames) {
                                        cell = row.createCell(columnIndex);

                                        int excelRowNumber = rowIndex + 1;

                                        switch (ColumnNames[columnIndex]) {
                                            case "nombre":
                                                cell.setCellValue(siteForm.getName());
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "namecat":
                                                cell.setCellValue(categoryForm.getName());
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "depende_de":
                                                cell.setCellValue(dependencias);
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "semilla":
                                                cell.setCellValue(pageForm.getUrl());
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "puntuacion_2020-03-13":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellValue(Double.parseDouble(pageForm.getScore()));
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "adecuacion_2020-03-13":
                                                cell.setCellValue(ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));

                                                break;
                                            case "cumplimiento_2020-03-13":
                                                cell.setCellValue(siteForm.getCompliance());
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "NV_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($F" + excelRowNumber + "=\"No Válido\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "A_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($F" + excelRowNumber + "=\"A\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "AA_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($F" + excelRowNumber + "=\"AA\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "NC_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($G" + excelRowNumber + "=\"No conforme\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "PC_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($G" + excelRowNumber + "=\"Parcialmente conforme\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                            case "TC_2020-02-21":
                                                cell.setCellType(CellType.NUMERIC);
                                                cell.setCellFormula("IF($G" + excelRowNumber + "=\"Plenamente conforme\",$E" + excelRowNumber + ",0)");
                                                cell.setCellStyle(defaultStyle);
                                                break;
                                        }
                                        columnIndex++;
                                    }
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
                            InsertGraphIntoSheet(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, true);
                            InsertGraphIntoSheet(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, false);
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

    private static void InsertGraphIntoSheet(XSSFWorkbook wb, XSSFSheet sheet, int categoryFirstRow, int categoryLastRow, boolean isFirst) {
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
                    new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 2, 2));

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