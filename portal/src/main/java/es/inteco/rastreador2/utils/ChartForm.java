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

import org.jfree.data.category.DefaultCategoryDataset;

public class ChartForm {

    private String title;
    private String columnTitle;
    private String rowTitle;
    private String color;
    private DefaultCategoryDataset dataSet;
    private boolean showColumsLabels;
    private boolean isFixedColorBars;
    private boolean isTridimensional;
    private boolean isSeriesChart;
    private boolean isOnePixelGraph;
    private boolean fixedLegend;
    private boolean percentage;
    private boolean printLegend;
    private boolean roundLabelPosition;
    private boolean gridline;
    private int x;
    private int y;

    public ChartForm(DefaultCategoryDataset dataSet, boolean isTridimensional, boolean isSeriesChart,
                     boolean isOnePixelGraph, boolean percentage, boolean printLegend, boolean roundLabelPosition,
                     boolean gridline, int x, int y, String color) {
        this("", "", "", dataSet, isTridimensional, isSeriesChart, isOnePixelGraph, percentage, printLegend, roundLabelPosition, gridline, x, y, color);
    }

    public ChartForm(String title, String columnTitle, String rowTitle,
                     DefaultCategoryDataset dataSet, boolean isTridimensional, boolean isSeriesChart,
                     boolean isOnePixelGraph, boolean percentage, boolean printLegend, boolean roundLabelPosition,
                     boolean gridline, int x, int y, String color) {
        this.title = title;
        this.columnTitle = columnTitle;
        this.rowTitle = rowTitle;
        this.dataSet = dataSet;
        this.isTridimensional = isTridimensional;
        this.isSeriesChart = isSeriesChart;
        this.isOnePixelGraph = isOnePixelGraph;
        this.percentage = percentage;
        this.color = color;
        this.printLegend = printLegend;
        this.roundLabelPosition = roundLabelPosition;
        this.gridline = gridline;
        this.x = x;
        this.y = y;
        this.isFixedColorBars = false;
        this.showColumsLabels = !isOnePixelGraph;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getRowTitle() {
        return rowTitle;
    }

    public void setRowTitle(String rowTitle) {
        this.rowTitle = rowTitle;
    }

    public DefaultCategoryDataset getDataSet() {
        return dataSet;
    }

    public void setDataSet(DefaultCategoryDataset dataSet) {
        this.dataSet = dataSet;
    }

    public boolean isTridimensional() {
        return isTridimensional;
    }

    public void setTridimensional(boolean isTridimensional) {
        this.isTridimensional = isTridimensional;
    }

    public boolean isSeriesChart() {
        return isSeriesChart;
    }

    public void setSeriesChart(boolean isSeriesChart) {
        this.isSeriesChart = isSeriesChart;
    }

    public boolean isOnePixelGraph() {
        return isOnePixelGraph;
    }

    public void setOnePixelGraph(boolean isOnePixelGraph) {
        this.isOnePixelGraph = isOnePixelGraph;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOneColor(String color) {
        this.color = color;
    }

    public boolean isPrintLegend() {
        return printLegend;
    }

    public void setPrintLegend(boolean printLegend) {
        this.printLegend = printLegend;
    }

    public boolean isRoundLabelPosition() {
        return roundLabelPosition;
    }

    public void setRoundLabelPosition(boolean roundLabelPosition) {
        this.roundLabelPosition = roundLabelPosition;
    }

    public boolean isGridline() {
        return gridline;
    }

    public void setGridline(boolean gridline) {
        this.gridline = gridline;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFixedLegend() {
        return fixedLegend;
    }

    public void setFixedLegend(boolean fixedLegend) {
        this.fixedLegend = fixedLegend;
    }

    public boolean isFixedColorBars() {
        return isFixedColorBars;
    }

    public void setFixedColorBars(boolean isFixedColorBars) {
        this.isFixedColorBars = isFixedColorBars;
    }

    public boolean isShowColumsLabels() {
        return showColumsLabels;
    }

    public void setShowColumsLabels(boolean showcolumsLabels) {
        this.showColumsLabels = showcolumsLabels;
    }
}
