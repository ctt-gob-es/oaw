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

/**
 * The Class ChartForm.
 */
public class ChartForm {

    /** The title. */
    private String title;
    
    /** The column title. */
    private String columnTitle;
    
    /** The row title. */
    private String rowTitle;
    
    /** The color. */
    private String color;
    
    /** The data set. */
    private DefaultCategoryDataset dataSet;
    
    /** The show colums labels. */
    private boolean showColumsLabels;
    
    /** The is fixed color bars. */
    private boolean isFixedColorBars;
    
    /** The is tridimensional. */
    private boolean isTridimensional;
    
    /** The is series chart. */
    private boolean isSeriesChart;
    
    /** The is one pixel graph. */
    private boolean isOnePixelGraph;
    
    /** The fixed legend. */
    private boolean fixedLegend;
    
    /** The percentage. */
    private boolean percentage;
    
    /** The print legend. */
    private boolean printLegend;
    
    /** The round label position. */
    private boolean roundLabelPosition;
    
    /** The gridline. */
    private boolean gridline;
    
    /** The x. */
    private int x;
    
    /** The y. */
    private int y;

    /**
	 * Instantiates a new chart form.
	 *
	 * @param dataSet            the data set
	 * @param isTridimensional   the is tridimensional
	 * @param isSeriesChart      the is series chart
	 * @param isOnePixelGraph    the is one pixel graph
	 * @param percentage         the percentage
	 * @param printLegend        the print legend
	 * @param roundLabelPosition the round label position
	 * @param gridline           the gridline
	 * @param x                  the x
	 * @param y                  the y
	 * @param color              the color
	 */
    public ChartForm(DefaultCategoryDataset dataSet, boolean isTridimensional, boolean isSeriesChart,
                     boolean isOnePixelGraph, boolean percentage, boolean printLegend, boolean roundLabelPosition,
                     boolean gridline, int x, int y, String color) {
        this("", "", "", dataSet, isTridimensional, isSeriesChart, isOnePixelGraph, percentage, printLegend, roundLabelPosition, gridline, x, y, color);
    }

    /**
	 * Instantiates a new chart form.
	 *
	 * @param title              the title
	 * @param columnTitle        the column title
	 * @param rowTitle           the row title
	 * @param dataSet            the data set
	 * @param isTridimensional   the is tridimensional
	 * @param isSeriesChart      the is series chart
	 * @param isOnePixelGraph    the is one pixel graph
	 * @param percentage         the percentage
	 * @param printLegend        the print legend
	 * @param roundLabelPosition the round label position
	 * @param gridline           the gridline
	 * @param x                  the x
	 * @param y                  the y
	 * @param color              the color
	 */
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

    /**
	 * Gets the title.
	 *
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Gets the column title.
	 *
	 * @return the column title
	 */
    public String getColumnTitle() {
        return columnTitle;
    }

    /**
	 * Sets the column title.
	 *
	 * @param columnTitle the new column title
	 */
    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    /**
	 * Gets the row title.
	 *
	 * @return the row title
	 */
    public String getRowTitle() {
        return rowTitle;
    }

    /**
	 * Sets the row title.
	 *
	 * @param rowTitle the new row title
	 */
    public void setRowTitle(String rowTitle) {
        this.rowTitle = rowTitle;
    }

    /**
	 * Gets the data set.
	 *
	 * @return the data set
	 */
    public DefaultCategoryDataset getDataSet() {
        return dataSet;
    }

    /**
	 * Sets the data set.
	 *
	 * @param dataSet the new data set
	 */
    public void setDataSet(DefaultCategoryDataset dataSet) {
        this.dataSet = dataSet;
    }

    /**
	 * Checks if is tridimensional.
	 *
	 * @return true, if is tridimensional
	 */
    public boolean isTridimensional() {
        return isTridimensional;
    }

    /**
	 * Sets the tridimensional.
	 *
	 * @param isTridimensional the new tridimensional
	 */
    public void setTridimensional(boolean isTridimensional) {
        this.isTridimensional = isTridimensional;
    }

    /**
	 * Checks if is series chart.
	 *
	 * @return true, if is series chart
	 */
    public boolean isSeriesChart() {
        return isSeriesChart;
    }

    /**
	 * Sets the series chart.
	 *
	 * @param isSeriesChart the new series chart
	 */
    public void setSeriesChart(boolean isSeriesChart) {
        this.isSeriesChart = isSeriesChart;
    }

    /**
	 * Checks if is one pixel graph.
	 *
	 * @return true, if is one pixel graph
	 */
    public boolean isOnePixelGraph() {
        return isOnePixelGraph;
    }

    /**
	 * Sets the one pixel graph.
	 *
	 * @param isOnePixelGraph the new one pixel graph
	 */
    public void setOnePixelGraph(boolean isOnePixelGraph) {
        this.isOnePixelGraph = isOnePixelGraph;
    }

    /**
	 * Checks if is percentage.
	 *
	 * @return true, if is percentage
	 */
    public boolean isPercentage() {
        return percentage;
    }

    /**
	 * Sets the percentage.
	 *
	 * @param percentage the new percentage
	 */
    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    /**
	 * Gets the color.
	 *
	 * @return the color
	 */
    public String getColor() {
        return color;
    }

    /**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
    public void setColor(String color) {
        this.color = color;
    }

    /**
	 * Sets the one color.
	 *
	 * @param color the new one color
	 */
    public void setOneColor(String color) {
        this.color = color;
    }

    /**
	 * Checks if is prints the legend.
	 *
	 * @return true, if is prints the legend
	 */
    public boolean isPrintLegend() {
        return printLegend;
    }

    /**
	 * Sets the prints the legend.
	 *
	 * @param printLegend the new prints the legend
	 */
    public void setPrintLegend(boolean printLegend) {
        this.printLegend = printLegend;
    }

    /**
	 * Checks if is round label position.
	 *
	 * @return true, if is round label position
	 */
    public boolean isRoundLabelPosition() {
        return roundLabelPosition;
    }

    /**
	 * Sets the round label position.
	 *
	 * @param roundLabelPosition the new round label position
	 */
    public void setRoundLabelPosition(boolean roundLabelPosition) {
        this.roundLabelPosition = roundLabelPosition;
    }

    /**
	 * Checks if is gridline.
	 *
	 * @return true, if is gridline
	 */
    public boolean isGridline() {
        return gridline;
    }

    /**
	 * Sets the gridline.
	 *
	 * @param gridline the new gridline
	 */
    public void setGridline(boolean gridline) {
        this.gridline = gridline;
    }

    /**
	 * Gets the x.
	 *
	 * @return the x
	 */
    public int getX() {
        return x;
    }

    /**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
    public void setX(int x) {
        this.x = x;
    }

    /**
	 * Gets the y.
	 *
	 * @return the y
	 */
    public int getY() {
        return y;
    }

    /**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
    public void setY(int y) {
        this.y = y;
    }

    /**
	 * Checks if is fixed legend.
	 *
	 * @return true, if is fixed legend
	 */
    public boolean isFixedLegend() {
        return fixedLegend;
    }

    /**
	 * Sets the fixed legend.
	 *
	 * @param fixedLegend the new fixed legend
	 */
    public void setFixedLegend(boolean fixedLegend) {
        this.fixedLegend = fixedLegend;
    }

    /**
	 * Checks if is fixed color bars.
	 *
	 * @return true, if is fixed color bars
	 */
    public boolean isFixedColorBars() {
        return isFixedColorBars;
    }

    /**
	 * Sets the fixed color bars.
	 *
	 * @param isFixedColorBars the new fixed color bars
	 */
    public void setFixedColorBars(boolean isFixedColorBars) {
        this.isFixedColorBars = isFixedColorBars;
    }

    /**
	 * Checks if is show colums labels.
	 *
	 * @return true, if is show colums labels
	 */
    public boolean isShowColumsLabels() {
        return showColumsLabels;
    }

    /**
	 * Sets the show colums labels.
	 *
	 * @param showcolumsLabels the new show colums labels
	 */
    public void setShowColumsLabels(boolean showcolumsLabels) {
        this.showColumsLabels = showcolumsLabels;
    }
}
