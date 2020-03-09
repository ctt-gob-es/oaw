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

import es.gob.oaw.css.CSSResource;
import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URLEncoder;
import java.util.*;

public class Evaluation {

    private long tevaluation;
    private long idAnalisis;
    private long rastreo;
    private String filename;
    private String filenameEncoded;
    private String entidad;
    private String base;
    private String source;

    private List<Problem> vectorProblemsAll;
    private List<Problem> vectorProblemsUnresolved;
    private List<Problem> vectorProblemsUser; // unresolved problems for the user's guidelines
    private List<Object> vectorGroupsHtml; // problems sorted into HTML groups
    private List<Object> vectorGroupsGuide; // problems sorted into guideline groups
    private Map<String, List<Problem>> hashCheckProblem; // problems sorted into individual checks groups
    private List<String> vectorGuidelines;
    private List<Integer> vectorChecksRun;
    private List<Problem> vectorProblemsSorted;
    private List<Integer> checksExecuted;
    private String checksExecutedStr;
    private List<CSSResource> cssResources;

    private Document docHtml;
    private int sortOrderSummary;

    public Evaluation() {
        entidad = "";
        filename = "";
        filenameEncoded = "";
        base = "";
        source = "";
        checksExecuted = new ArrayList<>();
        checksExecutedStr = "";
        vectorProblemsAll = new ArrayList<>();
        vectorProblemsUnresolved = new ArrayList<>();
        vectorProblemsUser = new ArrayList<>();
        vectorGroupsHtml = new ArrayList<>();
        vectorGroupsGuide = new ArrayList<>();
        hashCheckProblem = new Hashtable<>();
        vectorGuidelines = new ArrayList<>();
        vectorChecksRun = new ArrayList<>();
        vectorProblemsSorted = new ArrayList<>();
        docHtml = null;
        cssResources = Collections.emptyList();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void settevaluation(long t) {
        tevaluation = t;
    }

    public long getevaluation() {
        return tevaluation;
    }

    public Document getHtmlDoc() {
        return docHtml;
    }

    public void setHtmlDoc(Document anHtmlDoc) {
        docHtml = anHtmlDoc;
    }

    public String getFilename() {
        return filename;
    }

    public List<Integer> getChecksExecuted() {
        return checksExecuted;
    }

    public void setChecksExecuted(List<Integer> checksExecuted) {
        this.checksExecuted = checksExecuted;
    }

    public String getChecksExecutedStr() {
        return checksExecutedStr;
    }

    public void setChecksExecutedStr(String checksExecutedStr) {
        this.checksExecutedStr = checksExecutedStr;
    }

    public void setEntidad(String ent) {
        entidad = ent;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getFilenameEncoded() {
        return filenameEncoded;
    }

    public void setFilename(String name) {
        filename = name;
        if (filename != null) {
            try {
                filenameEncoded = URLEncoder.encode(filename, "UTF-8");
            } catch (Exception e) {
                Logger.putLog("Exception: ", Evaluation.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    public List<Problem> getProblems() { // all unresolved problems
        return vectorProblemsUnresolved;
    }

    public List<Problem> getProblemsUser() { // all unresolved problems for the user's guideline
        return vectorProblemsUser;
    }

    public void addProblem(Problem problem) {
        vectorProblemsAll.add(problem);
    }

    public List<Problem> getVectorProblems() {
        return vectorProblemsAll;
    }

    public void addCheckRun(int idCheck) {
        vectorChecksRun.add(idCheck);
    }

    public boolean hasRun(int idCheck) {
        for (Integer intRun : vectorChecksRun) {
            if (idCheck == intRun) {
                return true;
            }
        }
        return false;
    }

    public void setSortOrder(int sortOrder) {
        sortOrderSummary = sortOrder;
    }

    public int getSortOrder() {
        return sortOrderSummary;
    }

    public Map<String, List<Problem>> getHashCheckProblem() {
        return hashCheckProblem;
    }

    // gives each problem an ID number
    public void setIdProblems() {
        for (int x = 0; x < vectorProblemsAll.size(); x++) {
            Problem problem = vectorProblemsAll.get(x);
            problem.setId(x);
        }
    }

    public Problem getProblem(int idGiven) {
        for (final Problem problem : vectorProblemsAll) {
            if (problem.getId() == idGiven) {
                return problem;
            }
        }
        return null;
    }

    public boolean failsCheck(int idCheck) {
        for (final Problem problem : vectorProblemsAll) {
            if (problem.getCheck() != null) {
                if (problem.getCheck().getId() == idCheck) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countFails(int idCheck) {
        int count = 0;
        for (Problem problem : vectorProblemsAll) {
            if (problem.getCheck().getId() == idCheck) {
                count++;
            }
        }

        return count;
    }

    public List<Integer> getChecksFailed() {
        final List<Integer> checksFailed = new ArrayList<>();
        for (Problem problem : vectorProblemsAll) {
            if (problem.getCheck() != null) {
                if (!checksFailed.contains(problem.getCheck().getId())) {
                    checksFailed.add(problem.getCheck().getId());
                }
            }
        }
        return checksFailed;
    }

    public void addGuideline(String nameGuideline) {
        vectorGuidelines.add(nameGuideline);
    }

    public List<String> getGuidelines() {
        return vectorGuidelines;
    }

    public void setBase() {
        if (docHtml == null) {
            Logger.putLog("Warning: trying to set 'base' when HTML doc is null!", Evaluation.class, Logger.LOG_LEVEL_WARNING);
            return;
        }

        NodeList listHeads = docHtml.getElementsByTagName("head");
        if (listHeads.getLength() > 0) {
            NodeList listBases = ((Element) listHeads.item(0)).getElementsByTagName("base");
            if (listBases.getLength() > 0) {
                // use the first 'base' found
                base = ((Element) listBases.item(0)).getAttribute("href");
            }
        }
    }

    public String getBase() {
        if (base.length() > 0) {
            return base;
        }
        return filename;
    }

    // Uses the decision file to resolve problems.
    // Problems are moved from vectorProblemsAll to vectorProblemsUnresolved.
    public void resolveProblems() {
        vectorProblemsUnresolved.clear();
        vectorProblemsUnresolved.addAll(vectorProblemsAll);

        sortProblemsForGuidelines();
    }

    // Creates a list of problems for the user guidelines.
    // The list is stored in vectorProblemsUser.
    // The list of problems is further sorted into groups by HTML and guideline.
    private void sortProblemsForGuidelines() {
        vectorProblemsUser.clear();
        for (Problem problem : vectorProblemsUnresolved) {
            if (problem.getCheck() != null) {
                // does this problem belong to any of the user's guidelines?
                for (String filenameGuideline : vectorGuidelines) {
                    final Guideline guideline = EvaluatorUtility.loadGuideline(filenameGuideline);
                    if (guideline != null) {
                        if (guideline.containsCheck(problem.getCheck().getId())) {
                            vectorProblemsUser.add(problem);

                            // add check and problem to the hash
                            final String subgroup = guideline.getSubgroupFromCheck(problem.getCheck().getId());
                            if (hashCheckProblem.get(subgroup) != null) {
                                hashCheckProblem.get(subgroup).add(problem);
                            } else {
                                final List<Problem> vProblem = new ArrayList<>();
                                vProblem.add(problem);
                                hashCheckProblem.put(subgroup, vProblem);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    // Returns the number of known problems in the target guidelines.
    public int getCountKnownProblems() {
        int known = 0;
        for (Problem problem : vectorProblemsUser) {
            Check check = problem.getCheck();
            if (check.getConfidence() == CheckFunctionConstants.CONFIDENCE_HIGH) {
                known++;
            }
        }
        return known;
    }

    // Returns the number of likely problems in the target guidelines.
    public int getCountLikelyProblems() {
        int likely = 0;
        for (Problem problem : vectorProblemsUser) {
            Check check = problem.getCheck();
            if (check.getConfidence() == CheckFunctionConstants.CONFIDENCE_MEDIUM) {
                likely++;
            }
        }
        return likely;
    }

    // Returns the number of potential problems in the target guidelines.
    public int getCountPotentialProblems() {
        int potential = 0;
        for (Problem problem : vectorProblemsUser) {
            Check check = problem.getCheck();
            if (check.getConfidence() == CheckFunctionConstants.CONFIDENCE_CANNOTTELL) {
                potential++;
            }
        }
        return potential;
    }

    // Calling app will tell us the offset line number of the chunk of HTML code.
    public void applyLineOffset(int lineOffset) {
        for (Problem problem : vectorProblemsAll) {
            problem.setLineOffset(lineOffset);
        }
    }


    // Returns the number of the problem following the given problem.
    // Returns -1 if there is no problem following the given one.
    public int getNextProblem(Problem problemGiven) {
        for (int x = 0; x < vectorProblemsSorted.size(); x++) {
            Problem problem = vectorProblemsSorted.get(x);
            if (problem == problemGiven) {
                if ((x + 1) < vectorProblemsSorted.size()) {
                    problem = vectorProblemsSorted.get(x + 1);
                    return problem.getId();
                }
            }
        }
        return -1;
    }

    // Returns the number of the problem previous to the given problem.
    // Returns -1 if there is no problem previous to the given one.
    public int getPrevProblem(Problem problemGiven) {
        for (int x = 0; x < vectorProblemsSorted.size(); x++) {
            Problem problem = vectorProblemsSorted.get(x);
            if (problem == problemGiven) {
                if (x != 0) {
                    problem = vectorProblemsSorted.get(x - 1);
                    return problem.getId();
                }
                break;
            }
        }
        return -1;
    }

    // Returns the number of the decision following the given problem.
    // If there is no next decision then get the first decision.
    // Ignore any decisions that are already set to 'pass'.
    // Returns -1 if there are no other decisions.
    public int getNextDecision(Problem problemGiven) {
        // find the first problem after the given problem that requires a decision
        boolean bFoundCurrent = false;
        for (Problem problem : vectorProblemsSorted) {
            if (problem.getDecisionPass()) {
                continue;
            }

            if (bFoundCurrent) {
                Check check = problem.getCheck();
                // can user make a decision about this problem?
                int confidence = check.getConfidence();

                if (confidence != CheckFunctionConstants.CONFIDENCE_HIGH) {
                    return problem.getId();
                }
            }
            if (problem == problemGiven) {
                bFoundCurrent = true;
            }
        }

        // no problem after current, search for problem prior to given that requires a decision
        for (Problem problem : vectorProblemsSorted) {
            if (problem.getDecisionPass()) {
                continue;
            }
            Check check = problem.getCheck();
            // can user make a decision about this problem?
            int confidence = check.getConfidence();
            if (confidence != CheckFunctionConstants.CONFIDENCE_HIGH) {
                return problem.getId();
            }

            if (problem == problemGiven) {
                return -1; // no other problems require a decision
            }
        }
        return -1; // no other problems require a decision
    }

    public long getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(long idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public long getRastreo() {
        return rastreo;
    }

    public void setRastreo(long rastreo) {
        this.rastreo = rastreo;
    }

    public List<CSSResource> getCssResources() {
        return cssResources;
    }

    public void setCssResources(List<CSSResource> cssResources) {
        if (cssResources!=null) {
            this.cssResources = cssResources;
        } else {
            this.cssResources = Collections.emptyList();
        }
    }
}