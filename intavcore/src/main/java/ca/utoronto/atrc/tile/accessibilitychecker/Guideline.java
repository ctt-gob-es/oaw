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

import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Guideline {
    String type;
    String filename;
    String name;
    String nameLong;
    String date;
    Node preamble;
    Element statusElement;
    List<GuidelineGroup> groups;
    Map<Integer, List<String>> hashtableTechniques;
    Map<Integer, List<String>> hashtableSuccessCriteria;
    Map<?, ?> hashtableIssues;
    List<String> vectorTechniques;
    Map<String, List<Integer>> hashtableTechChecks;
    Map<String, List<Integer>> hashtableScChecks;
    List<?> vectorHtmlGroups; // checks sorted by HTML groups
    List<?> vectorGuidelineGroups; // checks sorted by guideline group
    List<Check> vectorChecksWithoutSc;

    public Guideline() {
        type = "";
        filename = "";
        name = "";
        nameLong = "";
        date = "";
        statusElement = null;
        preamble = null;
        groups = new ArrayList<GuidelineGroup>();
        hashtableTechniques = new Hashtable<Integer, List<String>>();
        hashtableSuccessCriteria = new Hashtable<Integer, List<String>>();
        hashtableIssues = new Hashtable();
        vectorTechniques = new ArrayList<String>();
        hashtableTechChecks = new Hashtable<String, List<Integer>>();
        hashtableScChecks = new Hashtable<String, List<Integer>>();
        vectorHtmlGroups = new ArrayList();
        vectorGuidelineGroups = new ArrayList();
        vectorChecksWithoutSc = new ArrayList<Check>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public String getName() {
        return name;
    }

    public String getLongName() {
        return nameLong;
    }

    public String getDate() {
        return date;
    }

    public Element getStatus() {
        return statusElement;
    }

    public Node getPreamble() {
        return preamble;
    }

    public List<GuidelineGroup> getGroups() {
        return groups;
    }

    public Map<Integer, List<String>> getHashtableTechniques() {
        return hashtableTechniques;
    }

    public Map<Integer, List<String>> getHashtableSuccessCriteria() {
        return hashtableSuccessCriteria;
    }

    public List<Integer> getChecksPerSuccessCriteria(String stringScId) {
        return hashtableScChecks.get(stringScId);
    }

    public Map getHashtableIssues() {
        return hashtableIssues;
    }

    public List<String> getVectorTechniques() {
        return vectorTechniques;
    }

    public Map<String, List<Integer>> getHashtableTechChecks() {
        return hashtableTechChecks;
    }

    public List<?> getVectorGroupsHtml() {
        return vectorHtmlGroups;
    }

    public List<?> getVectorGroupsGuideline() {
        return vectorGuidelineGroups;
    }

    public void getAllChecks(List<Integer> inChecks, String level) {
        for (GuidelineGroup group : groups) {
            for (GuidelineGroup subgroup : group.getGroupsVector()) {
                for (GuidelineGroup subgroup2 : subgroup.getGroupsVector()) {
                    if (StringUtils.isNotEmpty(subgroup2.getLevel()) && level.toUpperCase().contains(subgroup2.getLevel().toUpperCase())) {
                        subgroup2.getChecks(inChecks);
                    }
                }
            }
        }
    }

    public void getAllObservatoryChecks(List<Integer> inChecks, String level) {
        for (GuidelineGroup group : groups) {
            for (GuidelineGroup subgroup : group.getGroupsVector()) {
                subgroup.getChecks(inChecks);
            }
        }
    }

    public List<Check> getChecksWithoutSc() {
        return vectorChecksWithoutSc;
    }

    public int getCountKnown() {
        int count = 0;
        for (GuidelineGroup group : groups) {
            count += group.getCount(CheckFunctionConstants.CONFIDENCE_HIGH);
        }
        return count;
    }

    public int getCountLikely() {
        int count = 0;

        for (GuidelineGroup group : groups) {
            count += group.getCount(CheckFunctionConstants.CONFIDENCE_MEDIUM);
        }
        return count;
    }

    public int getCountPotential() {
        int count = 0;

        for (GuidelineGroup group : groups) {
            count += group.getCount(CheckFunctionConstants.CONFIDENCE_CANNOTTELL);
        }
        return count;
    }

    // Returns the name of the group that the given check belongs to.
    public String getGroupIdString(int checkId) {
        for (GuidelineGroup group : groups) {
            if (group.containsCheck(checkId)) {
                return group.getName();
            }
        }
        return "";
    }

    // Returns true if the given check ID is part of this guideline.
    public boolean containsCheck(int checkId) {
        for (GuidelineGroup group : groups) {
            if (group.containsCheck(checkId)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if this guideline contains any problem in the given vector
    public boolean containsCheck(List<Problem> vectorProblems) {
        for (Problem problem : vectorProblems) {
            if (containsCheck(problem.getCheck().getId())) {
                return true;
            }
        }
        return false;
    }

    // Returns the subgroup of a given check
    public String getSubgroupFromCheck(int checkId) {
        for (GuidelineGroup group : groups) {
            for (int y = 0; y < group.getGroupsVector().size(); y++) {
                GuidelineGroup group2 = group.getGroupsVector().get(y);
                String subgroup = group2.getSubgroupFromCheck(checkId);
                if (!subgroup.equalsIgnoreCase("")) {
                    return subgroup;
                }
            }
        }
        return "";
    }

    public Guideline initialize(String xmlSource) {
        try {
            // create a DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document docGuideline = builder.parse(new InputSource(new StringReader(xmlSource)));
            Element nodeRoot = docGuideline.getDocumentElement();

            if (!read(nodeRoot)) {
                Logger.putLog("Error: No se ha podido parsear la metodología", Guideline.class, Logger.LOG_LEVEL_INFO);
                return null;
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", Guideline.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return this;
    }

    public boolean initialize(InputStream inputStream, String filename) {
        this.filename = filename;
        try {
            if (inputStream == null) {
                Logger.putLog("Error: Can't open guideline file: " + filename, Guideline.class, Logger.LOG_LEVEL_INFO);
                return false;
            }

            // create a DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document docGuideline = builder.parse(inputStream);
            Element nodeRoot = docGuideline.getDocumentElement();

            if (!read(nodeRoot)) {
                Logger.putLog("Error: Could not read guideline: " + filename, Guideline.class, Logger.LOG_LEVEL_INFO);
                return false;
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", Guideline.class, Logger.LOG_LEVEL_ERROR, e);
            return false;
        }

        return true;
    }

    private boolean read(Node node) {
        boolean foundTitle = false;
        boolean foundDate = false;
        boolean foundStatus = false;
        boolean foundType = false;

        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x).getNodeName().equalsIgnoreCase("title")) {
                if (foundTitle) {
                    Logger.putLog("Warning: guideline has more than one title!", Guideline.class, Logger.LOG_LEVEL_WARNING);
                    return false;
                } else {
                    name = DOMUtil.getChildText(childNodes.item(x));
                    nameLong = ((Element) childNodes.item(x)).getAttribute("long-name");
                    foundTitle = true;
                }
            } else if (childNodes.item(x).getNodeName().equalsIgnoreCase("date")) {
                if (foundDate) {
                    Logger.putLog("Warning: guideline has more than one date!", Guideline.class, Logger.LOG_LEVEL_WARNING);
                    return false;
                } else {
                    date = DOMUtil.getChildText(childNodes.item(x));
                    foundDate = true;
                }
            } else if (childNodes.item(x).getNodeName().equalsIgnoreCase("type")) {
                if (foundType) {
                    Logger.putLog("Warning: guideline has more than one type!", Guideline.class, Logger.LOG_LEVEL_WARNING);
                    return false;
                } else {
                    type = DOMUtil.getChildText(childNodes.item(x));
                    foundType = true;
                }
            } else if (childNodes.item(x).getNodeName().equalsIgnoreCase("preamble")) {
                preamble = childNodes.item(x);
            } else if (childNodes.item(x).getNodeName().equalsIgnoreCase("status")) {
                if (foundStatus) {
                    Logger.putLog("Warning: guideline has more than one status!", Guideline.class, Logger.LOG_LEVEL_WARNING);
                    return false;
                } else {
                    statusElement = (Element) childNodes.item(x);
                    foundStatus = true;
                }
            } else if (childNodes.item(x).getNodeName().equalsIgnoreCase("group")) {
                GuidelineGroup group = new GuidelineGroup();
                if (group.initialize(childNodes.item(x), this)) {
                    groups.add(group);
                }
            }
        }

        if (!foundType) {
            Logger.putLog("Warning: guideline has no type!", Guideline.class, Logger.LOG_LEVEL_WARNING);
            return false;
        }

        // create a list of all techniques in the guideline (WCAG specific)
        NodeList listTechniques = ((Element) node).getElementsByTagName("technique");
        for (int t = 0; t < listTechniques.getLength(); t++) {
            String stringTechId = ((Element) listTechniques.item(t)).getAttribute("id");
            // check if this technique is already in the list
            boolean bFoundIt = false;
            for (String vectorTechnique : vectorTechniques) {
                if (stringTechId.equals(vectorTechnique)) {
                    bFoundIt = true;
                    break;
                }
            }
            if (!bFoundIt) {
                vectorTechniques.add(stringTechId);
            }
        }
        return true;
    }

    public void initializeChecks(Node node, List<Integer> vectorChecks, List<Integer> noExecutedMarkChecks, List<Integer> onlyWarningChecks, String nameGroup, Map<Integer, Integer> relatedChecks) {
        AllChecks allChecks = EvaluatorUtility.getAllChecks();

        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x).getNodeName().compareToIgnoreCase("check") == 0) {

                if (!((Element) childNodes.item(x)).hasAttribute("id")) {
                    Logger.putLog("Error: guideline check has no ID!", Guideline.class, Logger.LOG_LEVEL_WARNING);
                    continue;
                }
                String stringId = ((Element) childNodes.item(x)).getAttribute("id");
                try {
                    Integer integerCheckId = Integer.valueOf(stringId);
                    vectorChecks.add(integerCheckId);

                    // Checks que no activarán la condición de grupo ejecutado
                    if (((Element) childNodes.item(x)).hasAttribute("noExecutedMark") &&
                            ((Element) childNodes.item(x)).getAttribute("noExecutedMark").equalsIgnoreCase("true")) {
                        noExecutedMarkChecks.add(integerCheckId);
                    }

                    // Checks que solo darán un aviso, y no un error
                    if (((Element) childNodes.item(x)).hasAttribute("onlyWarning") &&
                            ((Element) childNodes.item(x)).getAttribute("onlyWarning").equalsIgnoreCase("true")) {
                        onlyWarningChecks.add(integerCheckId);
                    }

                    // Checks relacionados
                    if (((Element) childNodes.item(x)).hasAttribute("relatedWith")) {
                        relatedChecks.put(integerCheckId, Integer.valueOf(((Element) childNodes.item(x)).getAttribute("relatedWith")));
                    }

                    // add this guideline to the check
                    Check check = allChecks.getCheck(integerCheckId);
                    if (check == null) {
                        Logger.putLog("Warning: initializeChecks can't find check: " + stringId, Guideline.class, Logger.LOG_LEVEL_WARNING);
                        continue;
                    }

                    // get techniques for check (WCAG specific)
                    NodeList listTechniques = ((Element) childNodes.item(x)).getElementsByTagName("technique");
                    if (listTechniques.getLength() > 0) {
                        List<String> vectorTechniques = new ArrayList<String>();

                        for (int t = 0; t < listTechniques.getLength(); t++) {
                            String stringTechId = ((Element) listTechniques.item(t)).getAttribute("id");
                            vectorTechniques.add(stringTechId);

                            List<Integer> vectorTechChecks = hashtableTechChecks.get(stringTechId);
                            if (vectorTechChecks == null) {
                                vectorTechChecks = new ArrayList<Integer>();
                                hashtableTechChecks.put(stringTechId, vectorTechChecks);
                            }
                            vectorTechChecks.add(integerCheckId);
                        }
                        hashtableTechniques.put(integerCheckId, vectorTechniques);
                    }

                    // get success criteria for check (WCAG specific)
                    NodeList listSC = ((Element) childNodes.item(x)).getElementsByTagName("success-criteria");
                    if (listSC.getLength() > 0) {
                        List<String> vectorSC = new ArrayList<String>();

                        for (int sc = 0; sc < listSC.getLength(); sc++) {
                            String stringScId = ((Element) listSC.item(sc)).getAttribute("id");
                            vectorSC.add(stringScId);

                            List<Integer> vectorScChecks = hashtableScChecks.get(stringScId);
                            if (vectorScChecks == null) {
                                vectorScChecks = new ArrayList<Integer>();
                                hashtableScChecks.put(stringScId, vectorScChecks);
                            }
                            vectorScChecks.add(integerCheckId);
                        }
                        hashtableSuccessCriteria.put(integerCheckId, vectorSC);
                    } else { // keep track of checks without SC
                        vectorChecksWithoutSc.add(check);
                    }
                } catch (NumberFormatException nfe) {
                    Logger.putLog("Error: guideline check has invalid ID:" + stringId, Guideline.class, Logger.LOG_LEVEL_ERROR, nfe);
                }
            }
        }
    }

}