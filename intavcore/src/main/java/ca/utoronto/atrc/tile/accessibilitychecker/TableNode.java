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

public class TableNode {

    private Node node;
    private int rowSpan;
    private int colSpan;
    private boolean isSpanCell;
    private boolean isHeaderCell;

    public TableNode() {
        rowSpan = 0;
        colSpan = 0;
        node = null;
        isSpanCell = false;
        isHeaderCell = false;
    }

    public TableNode(boolean spanCell) {
        rowSpan = 0;
        colSpan = 0;
        node = null;
        isSpanCell = true;
        isHeaderCell = true;
    }

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

    public boolean isSpanCell() {
        return isSpanCell;
    }

    public void setSpanCell(boolean isSpanCell) {
        this.isSpanCell = isSpanCell;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public boolean isHeaderCell() {
        return isHeaderCell;
    }

    public void setHeaderCell(boolean isHeaderCell) {
        this.isHeaderCell = isHeaderCell;
    }


}

