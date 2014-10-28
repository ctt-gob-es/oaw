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

public class Problem {
    private Check check;
    private Identifier identifier;
    private Element element;
    private String date;
    private String xpath;
    private String group;
    private String nameElement;
    private String stringLineNumber;
    private String stringColumnNumber;
    private int idDecision;
    private boolean decisionPass;
    private int lineOffset;
    private int id;
    private boolean summary;

    public Problem() {
    }

    // standard constructor using an element from the HTML document
    public Problem(Element anElement) {
        element = anElement;
        nameElement = anElement.getNodeName();
        stringLineNumber = (String) anElement.getUserData("startLine");
        stringColumnNumber = (String) anElement.getUserData("startColumn");
        identifier = (Identifier) anElement.getUserData("identifier");
        date = "";
        check = null;
        xpath = "";
        group = "";
        idDecision = -1;
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

    public Identifier getIdentifier() {
        return identifier;
    }

    public String getIdentifierEncoded() {
        return identifier.getStringEncoded();
    }

    public String getDisplayString() {
        return identifier.getDisplayString();
    }

    public String getDisplayStringWithElement() {
        return identifier.getDisplayStringWithElement();
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String aGroup) {
        group = aGroup;
    }

    public Node getNode() {
        return element;
    }

    public int getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(int number) {
        idDecision = number;
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

    public void setSummary(boolean summary) {
        this.summary = summary;
    }
}

