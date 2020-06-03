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
/*
Copyright ©2005, University of Toronto. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a 
copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Adaptive Technology Resource Centre, University of Toronto
130 St. George St., Toronto, Ontario, Canada
Telephone: (416) 978-4360
*/
package ca.utoronto.atrc.tile.accessibilitychecker;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class TableNode.
 */
public class TableNode {
	/** The node. */
	private Node node;
	/** The row span. */
	private int rowSpan;
	/** The col span. */
	private int colSpan;
	/** The is span cell. */
	private boolean isSpanCell;
	/** The is header cell. */
	private boolean isHeaderCell;
	/** The is empty cell. */
	private boolean isEmptyCell;

	/**
	 * Instantiates a new table node.
	 */
	public TableNode() {
		rowSpan = 0;
		colSpan = 0;
		node = null;
		isSpanCell = false;
		isHeaderCell = false;
		isHeaderCell = false;
	}

	/**
	 * Instantiates a new table node.
	 *
	 * @param spanCell the span cell
	 */
	public TableNode(boolean spanCell) {
		rowSpan = 0;
		colSpan = 0;
		node = null;
		isSpanCell = true;
		isHeaderCell = false;
	}

	/**
	 * Instantiates a new table node.
	 *
	 * @param node the node
	 */
	public TableNode(Node node) {
		try {
			rowSpan = Integer.valueOf(((Element) node).getAttribute("rowSpan"));
		} catch (Exception e) {
			rowSpan = 0;
		}
		try {
			colSpan = Integer.valueOf(((Element) node).getAttribute("colSpan"));
		} catch (Exception e) {
			colSpan = 0;
		}
		this.node = node;
		isSpanCell = false;
	}

	/**
	 * Checks if is span cell.
	 *
	 * @return true, if is span cell
	 */
	public boolean isSpanCell() {
		return isSpanCell;
	}

	/**
	 * Sets the span cell.
	 *
	 * @param isSpanCell the new span cell
	 */
	public void setSpanCell(boolean isSpanCell) {
		this.isSpanCell = isSpanCell;
	}

	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 *
	 * @param node the new node
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Gets the row span.
	 *
	 * @return the row span
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * Sets the row span.
	 *
	 * @param rowSpan the new row span
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	/**
	 * Gets the col span.
	 *
	 * @return the col span
	 */
	public int getColSpan() {
		return colSpan;
	}

	/**
	 * Sets the col span.
	 *
	 * @param colSpan the new col span
	 */
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	/**
	 * Checks if is header cell.
	 *
	 * @return true, if is header cell
	 */
	public boolean isHeaderCell() {
		return isHeaderCell;
	}

	/**
	 * Sets the header cell.
	 *
	 * @param isHeaderCell the new header cell
	 */
	public void setHeaderCell(boolean isHeaderCell) {
		this.isHeaderCell = isHeaderCell;
	}

	/**
	 * Checks if is empty cell.
	 *
	 * @return the isEmptyCell
	 */
	public boolean isEmptyCell() {
		return isEmptyCell;
	}

	/**
	 * Sets the empty cell.
	 *
	 * @param isEmptyCell the isEmptyCell to set
	 */
	public void setEmptyCell(boolean isEmptyCell) {
		this.isEmptyCell = isEmptyCell;
	}
}
