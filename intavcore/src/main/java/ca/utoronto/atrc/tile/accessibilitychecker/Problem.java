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

import es.inteco.common.properties.PropertiesManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Problem {
    private Check check;
    private Element element;
    private String date;
    private String xpath;
    private String nameElement;
    private String stringLineNumber;
    private String stringColumnNumber;
    private boolean decisionPass;
    private int lineOffset;
    private int id;
    private boolean summary;

    public Problem() {
        final PropertiesManager properties = new PropertiesManager();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));
        date = dateFormat.format(new Date());
    }

    // standard constructor using an element from the HTML document
    public Problem(Element anElement) {
        this();
        element = anElement;
        nameElement = anElement.getNodeName();
        stringLineNumber = (String) anElement.getUserData("startLine");
        stringColumnNumber = (String) anElement.getUserData("startColumn");
        check = null;
        xpath = "";
        decisionPass = false;
        lineOffset = 0;
        id = -1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String aDate) {
        date = aDate;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check aCheck) {
        check = aCheck;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String anXpath) {
        xpath = anXpath;
    }

    public String getElement() {
        return nameElement;
    }

    public void setLineOffset(int offset) {
        lineOffset = offset;
    }

    public int getLineNumber() {
        if (stringLineNumber != null) {
            int lineNumber = Integer.parseInt(stringLineNumber);
            return lineNumber - lineOffset;
        } else {
            return 0;
        }
    }

    public String getLineNumberString() {
        if (lineOffset == 0) {
            return stringLineNumber;
        } else {
            int lineNumber = Integer.parseInt(stringLineNumber);
            return Integer.toString(lineNumber - lineOffset);
        }
    }

    public int getColumnNumber() {
        if (stringColumnNumber != null) {
            return Integer.parseInt(stringColumnNumber);
        } else {
            return 0;
        }
    }

    public String getColumnNumberString() {
        return stringColumnNumber;
    }

    public Node getNode() {
        return element;
    }

    public boolean getDecisionPass() {
        return decisionPass;
    }

    public void setDecisionPass(boolean state) {
        decisionPass = state;
    }

    public void setId(int number) {
        id = number;
    }

    public int getId() {
        return id;
    }

    public void setLineNumber(int linenumber) {
        stringLineNumber = Integer.toString(linenumber);
    }

    public void setColumnNumber(int columnnumber) {
        stringColumnNumber = Integer.toString(columnnumber);
    }

    public void setNode(Element el) {
        element = el;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean isSummary) {
        this.summary = isSummary;
    }
}

