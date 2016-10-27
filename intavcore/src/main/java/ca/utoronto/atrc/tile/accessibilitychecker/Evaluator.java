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

import com.opensymphony.oscache.base.NeedsRefreshException;
import es.ctic.css.CSSProblem;
import es.ctic.css.utils.CSSUtils;
import es.ctic.css.utils.ImportedCSSExtractor;
import es.inteco.common.*;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.comun.Incidencia;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.datos.IncidenciaDatos;
import es.inteco.intav.form.CheckedLinks;
import es.inteco.intav.persistence.Analysis;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class Evaluator {

    private static final String ALL_HTML_VALIDATION_ERRORS = "_ALL_ERRORS_";

    private final XPathGenerator xPathGenerator = new XPathGenerator();

    // evaluates the given file for accessibility problems
    // filename - URL of the page
    public Evaluation evaluate(final CheckAccessibility checkAccesibility, final String language) {
        try {
            // load the HTML document
            final URL url = EvaluatorUtility.openUrl(checkAccesibility.getUrl());
            if (url == null) {
                return null;
            }
            checkAccesibility.setUrl(url.toString());
        } catch (Exception e) {
            Logger.putLog("Exception: ", Evaluator.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }

        return evaluateWork(checkAccesibility, language);
    }

    // evaluates the given file for accessibility problems from the crawler application
    // filename - URL of the page
    public Evaluation evaluateContent(final CheckAccessibility checkAccesibility, final String language) throws UnsupportedEncodingException {
        return evaluateWorkFromContent(checkAccesibility, language);
    }

    private Evaluation evaluateWork(final CheckAccessibility checkAccessibility, final String language) {
        final AllChecks allChecks = EvaluatorUtility.getAllChecks();

        // create a list of checks that fulfill the given guidelines
        final Guideline guideline = EvaluatorUtility.loadGuideline(checkAccessibility.getGuidelineFile());
        final List<Integer> checksSelected = createCheckList(checkAccessibility, guideline);

        final boolean htmlValidationNeeded = EvaluatorUtils.isHtmlValidationNeeded(checksSelected);
        final boolean cssValidationNeeded = EvaluatorUtils.isCssValidationNeeded(checksSelected);

        final Document docHtml = EvaluatorUtility.loadHtmlFile(checkAccessibility, htmlValidationNeeded, cssValidationNeeded, language, false);
        if (docHtml == null) {
            return null;
        } else {
            final String cacheKey = IntavConstants.CHECKED_LINKS_CACHE_KEY + checkAccessibility.getIdRastreo();
            CheckedLinks checkedLinks = new CheckedLinks();
            if (checkAccessibility.getIdRastreo() > 0) {
                try {
                    checkedLinks = (CheckedLinks) CacheUtils.getFromCache(cacheKey);
                } catch (NeedsRefreshException e) {
                    // No hacemos nada
                }
            }
            docHtml.getDocumentElement().setUserData("checkedLinks", checkedLinks, null);
            final Evaluation evaluation = applyEvaluation(checkAccessibility, checksSelected, allChecks, docHtml, language);

            if (checkAccessibility.getIdRastreo() > 0) {
                CacheUtils.putInCache(docHtml.getDocumentElement().getUserData("checkedLinks"), cacheKey);
            }

            return evaluation;
        }
    }

    private Evaluation evaluateWorkFromContent(final CheckAccessibility checkAccessibility, final String language) throws UnsupportedEncodingException {
        final AllChecks allChecks = EvaluatorUtility.getAllChecks();
        final Guideline guideline = EvaluatorUtility.loadGuideline(checkAccessibility.getGuidelineFile());

        // create a list of checks that fulfill the given guidelines
        final List<Integer> checksSelected = createCheckList(checkAccessibility, guideline);

        final boolean htmlValidationNeeded = EvaluatorUtils.isHtmlValidationNeeded(checksSelected);
        final boolean cssValidationNeeded = EvaluatorUtils.isCssValidationNeeded(checksSelected);

        // Si se ha invocado desde el rastreador, llevará asociado un ID de rastreo
        final boolean fromCrawler = checkAccessibility.getIdRastreo() != 0;

        final InputStream inputStream;
        if (!fromCrawler) {
            inputStream = new ByteArrayInputStream(checkAccessibility.getContent().getBytes(IntavConstants.DEFAULT_ENCODING));
        } else {
            inputStream = new ByteArrayInputStream(checkAccessibility.getContent().getBytes());
        }

        final Document docHtml = EvaluatorUtility.loadHtmlFile(inputStream, checkAccessibility, htmlValidationNeeded, cssValidationNeeded, language, fromCrawler, IntavConstants.DEFAULT_ENCODING);
        if (docHtml == null) {
            return null;
        } else {
            CheckedLinks checkedLinks = new CheckedLinks();
            if (checkAccessibility.getIdRastreo() > 0) {
                try {
                    checkedLinks = (CheckedLinks) CacheUtils.getFromCache("checkedLinks_" + checkAccessibility.getIdRastreo());
                } catch (NeedsRefreshException e) {
                    // No hacemos nada
                }
            }
            docHtml.getDocumentElement().setUserData("checkedLinks", checkedLinks, null);

            final Evaluation evaluation = applyEvaluation(checkAccessibility, checksSelected, allChecks, docHtml, language);

            if (checkAccessibility.getIdRastreo() > 0) {
                CacheUtils.putInCache(docHtml.getDocumentElement().getUserData("checkedLinks"), "checkedLinks_" + checkAccessibility.getIdRastreo());
            }
            return evaluation;
        }
    }

    private Evaluation applyEvaluation(final CheckAccessibility checkAccessibility, final List<Integer> checksSelected, final AllChecks allChecks, final Document docHtml, final String language) {
        // make sure there is at least one check in our vector
        if (checksSelected.isEmpty()) {
            Logger.putLog("Error: No checks selected for target guidelines", Evaluator.class, Logger.LOG_LEVEL_INFO);
            return null;
        } else {
            try {
                final Map<String, List<Check>> elementsMap = createElementChecksLinks(checksSelected, allChecks);

                // find the HTML element and look for a 'lang' attribute
                Node nodeHTML = EvaluatorUtils.getHtmlElement(docHtml);
                nodeHTML = docHtml;

                // set the appropriate language for selected checks
                setAppropriateData(checksSelected, language);

                return getEvaluation(checkAccessibility, nodeHTML, docHtml, elementsMap);

            } catch (Exception e) {
                Logger.putLog("Exception al evaluar " + checkAccessibility.getUrl() + ": ", Evaluator.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return null;
    }


    private List<Integer> createCheckList(final CheckAccessibility checkAccessibility, final Guideline guideline) {
        final List<Integer> checksSelected = new ArrayList<>();

        //PropertiesManager pmgr = new PropertiesManager();
        //if (guideline.getFilename().equalsIgnoreCase(pmgr.getValue("intav.properties", "observatory.guideline.file"))) {
        if (guideline.getFilename().startsWith("observatorio") || guideline.getFilename().startsWith("uneER-139803")) {
            guideline.getAllObservatoryChecks(checksSelected, checkAccessibility.getLevel());
        } else {
            guideline.getAllChecks(checksSelected, checkAccessibility.getLevel());
        }

        if (checksSelected.isEmpty()) {
            for (int x = 0; x < checksSelected.size(); x++) {
                for (int y = x + 1; y < checksSelected.size(); y++) {
                    Integer ix = checksSelected.get(x);
                    Integer iy = checksSelected.get(y);
                    if (ix.intValue() == iy.intValue()) {
                        checksSelected.remove(y);
                        y--;
                    }
                }
            }
        }

        return checksSelected;
    }

    // create an index that links each element to its required checks

    private Map<String, List<Check>> createElementChecksLinks(final List<Integer> checksSelected, final AllChecks allChecks) {
        final Map<String, List<Check>> elementsMap = new HashMap<>();
        for (Integer integerSelectedCheck : checksSelected) {
            final Check check = allChecks.getCheck(integerSelectedCheck);
            if (check == null) {
                Logger.putLog("Warning: Guideline contains check ID not found in master list: " + integerSelectedCheck, Evaluator.class, Logger.LOG_LEVEL_WARNING);
            }
            // add this check to our map
            else {
                final String elementName = check.getTriggerElement();
                if ((elementName != null) && (elementName.length() != 0)) {
                    List<Check> listOfChecks = elementsMap.get(elementName);
                    if (listOfChecks == null) {
                        listOfChecks = new ArrayList<>();
                        elementsMap.put(elementName, listOfChecks);
                        listOfChecks.add(check);
                    } else {
                        // if this check is a prerequisite for any check in the list then
                        // insert it in front of that check otherwise add it at the end of the list
                        boolean checkAdded = false;
                        for (int y = 0; y < listOfChecks.size(); y++) {
                            Check tempCheck = listOfChecks.get(y);
                            if (tempCheck.isPrerequisite(check.getId())) {
                                listOfChecks.add(y, check);
                                checkAdded = true;

                                // check if this is a circular prerequisite
                                if (check.isPrerequisite(tempCheck.getId())) {
                                    Logger.putLog("Warning: circular prerequisite, checks " + tempCheck.getId() + " and " + check.getId(), Evaluator.class, Logger.LOG_LEVEL_WARNING);
                                }
                                // note: I'm not testing the following checks for circular prerequisites
                                break;
                            }
                        }
                        if (!checkAdded) { // add it at the end of the list
                            listOfChecks.add(check);
                        }
                    }
                }
            }
        }
        return elementsMap;
    }


    private Evaluation getEvaluation(final CheckAccessibility checkAccesibility, final Node nodeHTML, final Document docHtml, final Map<String, List<Check>> elementsMap) {
        final boolean isCrawling = checkAccesibility.getIdRastreo() != 0;

        final Evaluation evaluation = new Evaluation();
        evaluation.setFilename(checkAccesibility.getUrl());
        evaluation.setHtmlDoc(docHtml);
        evaluation.setBase();
        evaluation.setEntidad(checkAccesibility.getEntity());
        evaluation.setRastreo(0);
        evaluation.addGuideline(checkAccesibility.getGuidelineFile());

        // perform the evaluation
        final List<Incidencia> incidenceList = evaluateLoop(nodeHTML, evaluation, elementsMap, isCrawling);

        // perform any special tests (doctype etc.)
        evaluateSpecial(nodeHTML, evaluation, elementsMap);

        // resolve any potential problems
        evaluation.resolveProblems();

        // give each problem an ID number
        evaluation.setIdProblems();

        if (isCrawling) {
            try (Connection conn = DataBaseManager.getConnection()) {
                // create a global analysis in database
                int idAnalisis = setAnalisisDB(evaluation, checkAccesibility);
                // register DB analysis id in evaluation object
                evaluation.setIdAnalisis(idAnalisis);
                // save incidences
                IncidenciaDatos.saveIncidenceList(conn, idAnalisis, incidenceList);
            } catch (Exception e) {
                Logger.putLog("Error al guardar las incidencias en base de datos", Evaluator.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return evaluation;
    }

    private void addIncidenceToList(final Node node, final Check check, final Evaluation evaluation, final List<Incidencia> incidenceList, final boolean isCrawling) {
        final PropertiesManager properties = new PropertiesManager();
        final SimpleDateFormat format = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));
        final Problem problem = new Problem((Element) node);
        problem.setDate(format.format(new Date()));
        problem.setCheck(check);
        problem.setXpath(xPathGenerator.getXpath(node));

        evaluation.addProblem(problem);

        if (isCrawling) {
            addIncidence(evaluation, problem, incidenceList);
        }

        // should this check be run only on first occurrence?
        if (check.isFirstOccuranceOnly()) {
            evaluation.addCheckRun(check.getId());
        }
    }

    private void addIncidence(final Evaluation eval, final Problem prob, final List<Incidencia> incidenceList) {
        final Incidencia incidencia = new Incidencia();

        incidencia.setCodigoAnalisis(eval.getIdAnalisis());
        incidencia.setCodigoComprobacion(prob.getCheck().getId());
        incidencia.setCodigoLineaFuente(prob.getLineNumber());
        incidencia.setCodigoColumnaFuente(prob.getColumnNumber());
        incidencia.setCodigoFuente(es.inteco.intav.negocio.SourceManager.getSourceInfo(prob));

        incidenceList.add(incidencia);
    }


    private void addIncidence(final Evaluation eval, final Problem prob, final List<Incidencia> incidenceList, final String text) {
        final Incidencia incidencia = new Incidencia();

        incidencia.setCodigoAnalisis(eval.getIdAnalisis());
        incidencia.setCodigoComprobacion(prob.getCheck().getId());
        incidencia.setCodigoLineaFuente(prob.getLineNumber());
        incidencia.setCodigoColumnaFuente(prob.getColumnNumber());
        incidencia.setCodigoFuente(text);

        incidenceList.add(incidencia);
    }

    // Realiza la evaluación de un conjunto de checks sobre un nodo
    private void performEvaluation(final Node node, final List<Check> vectorChecks, final Evaluation evaluation, final List<Incidencia> incidenceList, final boolean isCrawling) {
        // keep track of the checks that have run (needed for prerequisites)
        final List<Integer> vectorChecksRun = new ArrayList<>();

        // Ejecutamos las comprobaciones de HTML
        performEvaluationHTMLChecks(node, vectorChecks, evaluation, incidenceList, isCrawling, vectorChecksRun);

        // Una vez acabadas las comprobaciones sobre HTML, ejecutamos las comprobaciones de CSS (que tienen estructura distinta)
        performEvaluationCSSChecks(node, vectorChecks, evaluation, incidenceList, vectorChecksRun);
    }


    private void performEvaluationHTMLChecks(Node node, List<Check> vectorChecks, Evaluation evaluation, List<Incidencia> incidenceList, boolean isCrawling, List<Integer> vectorChecksRun) {
        for (Check check : vectorChecks) {
            // Comprobamos que el check está activo
            if (check.getCheckOkCode() != CheckFunctionConstants.CHECK_STATUS_OK) {
                continue;
            }

            // ¿Los checks prerequisitos se han ejecutado con éxito?
            if (!check.prerequisitesOK(vectorChecksRun)) {
                continue;
            }

            // should check be run on first occurance only?
            if (evaluation.hasRun(check.getId())) {
                continue;
            }

            if ("css".equalsIgnoreCase(check.getTriggerElement())) {
                // Si es una comprobación de CSS se salta porque se comprueban posteriormente
                continue;
            }

            // Añadimos el check a la lista de checks ejecutados (si no es un check prerrequisito que no se imprima)
            if ((!check.getStatus().equals(String.valueOf(CheckFunctionConstants.CHECK_STATUS_PREREQUISITE_NOT_PRINT)))
                    && (!evaluation.getChecksExecuted().contains(check.getId()))) {
                evaluation.getChecksExecuted().add(check.getId());
                evaluation.setChecksExecutedStr(evaluation.getChecksExecutedStr().concat("," + check.getId()));
            }

            try {
                if (check.doEvaluation((Element) node)) {
                    // Ha pasado el check, lo metemos en la lista de checks pasados con éxito
                    vectorChecksRun.add(check.getId());
                }
            } catch (AccessibilityError ae) {
                // Añadimos los problemas de validación de código HTML
                if (check.getId() == 232) {
                    addValidationIncidences(evaluation, check, incidenceList, ALL_HTML_VALIDATION_ERRORS);
                } else if (EvaluatorUtils.isCssValidationNeeded(check.getId())) {
                    addCssValidationIncidences(evaluation, check, incidenceList);
                } else if (check.getId() == 438 || check.getId() == 439 || check.getId() == 440 || check.getId() == 441) {
                    addValidationIncidences(evaluation, check, incidenceList, String.valueOf(check.getId()));
                } else if (check.getId() == 460) {
                    addLanguageIncidences(evaluation, check, incidenceList);
                } else if (check.getId() == 455 || check.getId() == 456 || check.getId() == 457 || check.getId() == 458) {
                    addBrokenLinksIncidences(evaluation, check, incidenceList);
                } else if (check.getId() == 37) {
                    addHeaderNestingIncidences(evaluation, check, incidenceList);
                } else if (check.getId() == 434 || check.getId() == 435) {
                    addTabIndexIncidences(evaluation, check, incidenceList);
                } else {
                    // Se ha encontrado un error y se va a registrar en la base de datos
                    if (!check.getStatus().equals(String.valueOf(CheckFunctionConstants.CHECK_STATUS_PREREQUISITE_NOT_PRINT))) {
                        addIncidenceToList(node, check, evaluation, incidenceList, isCrawling);
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", Evaluator.class, Logger.LOG_LEVEL_ERROR, e);
            }
        } // end for (Check check : vectorChecks)
    }

    private void addTabIndexIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList) {
        final NodeList nodeList = evaluation.getHtmlDoc().getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                if (element.hasAttribute("tabindex")) {
                    try {
                        final int tabindex = Integer.parseInt(element.getAttribute("tabindex").trim());
                        if (tabindex > 0) {
                            final Problem problem = new Problem(element);
                            problem.setSummary(true);
                            problem.setCheck(check);
                            final Element problemTextNode = createProblemTextElement(evaluation, EvaluatorUtils.serializeXmlElement(element));
                            problem.setNode(problemTextNode);

                            evaluation.addProblem(problem);

                            addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
        }
    }

    private Element createProblemTextElement(final Evaluation evaluation, final String text) {
        final Element problemTextNode = evaluation.getHtmlDoc().createElement("problem-text");
        problemTextNode.setTextContent(text);
        return problemTextNode;
    }

    private void addHeaderNestingIncidences(Evaluation evaluation, Check check, List<Incidencia> incidenceList) {
        for (int i = 1; i < 7; i++) {
            // Obtenemos los encabezados
            final NodeList headers = evaluation.getHtmlDoc().getElementsByTagName("h" + i);
            if (headers != null && headers.getLength() > 0) {
                for (int j = 0; j < headers.getLength(); j++) {
                    final Element currentHeader = (Element) headers.item(j);
                    if (CheckUtils.compareHeadingsLevel((Element) currentHeader.getUserData("prevheader"), currentHeader) > 1) {
                        final Problem problem = new Problem(currentHeader);
                        problem.setSummary(true);
                        problem.setCheck(check);
                        final Element problemTextNode = createProblemTextElement(evaluation, EvaluatorUtils.serializeXmlElement(currentHeader, true));
                        problem.setNode(problemTextNode);

                        evaluation.addProblem(problem);

                        addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());
                    }
                }
            }
        }
    }

    private void performEvaluationCSSChecks(Node node, List<Check> vectorChecks, Evaluation evaluation, List<Incidencia> incidenceList, List<Integer> vectorChecksRun) {
        for (Check check : vectorChecks) {
            if ("html".equalsIgnoreCase(node.getNodeName()) && "css".equalsIgnoreCase(check.getTriggerElement())) {
                for (CheckCode checkCode : check.getVectorCode()) {
                    if (checkCode.getType() == CheckFunctionConstants.CODE_TYPE_FUNCTION) {
                        final List<CSSProblem> cssProblems = CSSUtils.evaluate(node.getOwnerDocument().getDocumentElement(), checkCode, evaluation.getCssResources());
                        if (cssProblems.isEmpty()) {
                            // Ha pasado el check, lo metemos en la lista de checks pasados con éxito
                            vectorChecksRun.add(check.getId());
                        } else {
                            // Se ha encontrado un error y se va a registrar en la base de datos
                            if (!check.getStatus().equals(String.valueOf(CheckFunctionConstants.CHECK_STATUS_PREREQUISITE_NOT_PRINT))) {
                                addCssIncidences(evaluation, check, incidenceList, cssProblems);
                            }
                        }
                        if (!check.getStatus().equals(String.valueOf(CheckFunctionConstants.CHECK_STATUS_PREREQUISITE_NOT_PRINT))) {
                            // Añadimos el check a la lista de checks ejecutados (si no es un check prerrequisito que no se imprima)
                            if (!evaluation.getChecksExecuted().contains(check.getId())) {
                                evaluation.getChecksExecuted().add(check.getId());
                                evaluation.setChecksExecutedStr(evaluation.getChecksExecutedStr().concat("," + check.getId()));
                            }
                        }
                    } else {
                        Logger.putLog("Error un checkcode de CSS que no es una única función", Evaluator.class, Logger.LOG_LEVEL_ERROR);
                    }
                }
            }
        }
    }

    private List<Incidencia> addBrokenLinksIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList) {
        final List<Element> brokenLinks;
        final String scope = check.getVectorCode().get(0).getFunctionAttribute1();
        if ("domain".equalsIgnoreCase(scope)) {
            brokenLinks = (List<Element>) evaluation.getHtmlDoc().getDocumentElement().getUserData("domainLinks");
        } else if ("external".equalsIgnoreCase(scope)) {
            brokenLinks = (List<Element>) evaluation.getHtmlDoc().getDocumentElement().getUserData("externalLinks");
        } else {
            brokenLinks = Collections.emptyList();
        }
        for (Element brokenLink : brokenLinks) {
            final Problem problem = new Problem(brokenLink);
            problem.setCheck(check);
            final Element problemTextNode = createProblemTextElement(evaluation, "<A href=\"" + brokenLink.getAttribute("href") + "\">" + brokenLink.getTextContent() + "</A>");
            problem.setNode(problemTextNode);

            evaluation.addProblem(problem);

            addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());
        }

        // should this check be run only on first occurrence?
        if (check.isFirstOccuranceOnly()) {
            evaluation.addCheckRun(check.getId());
        }

        return incidenceList;
    }

    private List<Incidencia> addLanguageIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList) {
        final Problem problem = new Problem();
        problem.setCheck(check);
        problem.setColumnNumber(-1);
        problem.setLineNumber(-1);
        final Document docHtml = evaluation.getHtmlDoc();
        final List<String> enWords = (List<String>) docHtml.getUserData("en_words");
        final StringBuilder textContent = new StringBuilder();
        if (!enWords.isEmpty()) {
            final Iterator<String> itr = enWords.iterator();
            textContent.append(itr.next());
            while (itr.hasNext()) {
                textContent.append(", ").append(itr.next());
            }
        }
        final Element problemTextNode = createProblemTextElement(evaluation, textContent.toString());
        problem.setNode(problemTextNode);
        problem.setSummary(true);

        evaluation.addProblem(problem);

        addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());

        return incidenceList;
    }

    private List<Incidencia> addValidationIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList, final String id) {
        final List<Incidencia> validationProblems = new ArrayList<>();

        final Element elementHtmlRoot = evaluation.getHtmlDoc().getDocumentElement();
        final List<ValidationError> vectorValidationErrors = (List<ValidationError>) elementHtmlRoot.getUserData("validationErrors");

        if (vectorValidationErrors != null) {
            for (ValidationError validationError : vectorValidationErrors) {
                if (ALL_HTML_VALIDATION_ERRORS.equalsIgnoreCase(id) || validationError.getMessageId().equalsIgnoreCase(id)) {
                    final Problem problem = new Problem();
                    problem.setCheck(check);
                    problem.setColumnNumber(validationError.getColumn());
                    problem.setLineNumber(validationError.getLine());
                    final Element problemTextNode = createProblemTextElement(evaluation, validationError.getCode());
                    problem.setNode(problemTextNode);
                    problem.setSummary(validationError.isSummary());

                    evaluation.addProblem(problem);

                    addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());

                    // should this check be run only on first occurrence?
                    if (check.isFirstOccuranceOnly()) {
                        evaluation.addCheckRun(check.getId());
                    }
                }
            }
        }

        return validationProblems;
    }

    // Añade los problemas de validación de CSS
    private List<Incidencia> addCssValidationIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList) {
        final List<Incidencia> validationProblems = new ArrayList<>();

        final Element elementHtmlRoot = evaluation.getHtmlDoc().getDocumentElement();
        final List<CssValidationError> vectorValidationErrors = (List<CssValidationError>) elementHtmlRoot.getUserData("cssValidationErrors");

        if (vectorValidationErrors != null) {
            for (CssValidationError cssValidationError : vectorValidationErrors) {
                final Problem problem = new Problem();
                problem.setCheck(check);
                problem.setColumnNumber(0);
                problem.setLineNumber(cssValidationError.getLine());
                final Element problemTextNode = createProblemTextElement(evaluation, cssValidationError.getCode());
                problem.setNode(problemTextNode);
                problem.setSummary(cssValidationError.isSummary());

                evaluation.addProblem(problem);

                addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());

                // should this check be run only on first occurrence?
                if (check.isFirstOccuranceOnly()) {
                    evaluation.addCheckRun(check.getId());
                }
            }
        }

        return validationProblems;
    }

    private void addCssIncidences(final Evaluation evaluation, final Check check, final List<Incidencia> incidenceList, final List<CSSProblem> cssProblems) {
        if (cssProblems != null) {
            for (CSSProblem cssProblem : cssProblems) {
                final Problem problem = CSSUtils.getProblemFromCSS(cssProblem, check, evaluation);

                addIncidence(evaluation, problem, incidenceList, problem.getNode().getTextContent());

                // should this check be run only on first occurrence?
                if (check.isFirstOccuranceOnly()) {
                    evaluation.addCheckRun(check.getId());
                }
            }
        }
    }

    // Añade los checks especificos de un elemento
    private void addSpecificChecks(final List<Check> checks, final Map<String, List<Check>> elementsMap, final String nameElement) {
        final List<Check> specificChecks = elementsMap.get(nameElement);
        addChecks(checks, specificChecks);
    }

    // Añade los checks generales de todos los elementos
    private void addGeneralChecks(final List<Check> checks, final Map<String, List<Check>> elementsMap) {
        // add the checks that apply to all elements
        final List<Check> allElementsChecks = elementsMap.get("*");
        addChecks(checks, allElementsChecks);
    }

    // Añade los checks específicos de CSS
    private void addCssChecks(final List<Check> checks, final Map<String, List<Check>> elementsMap) {
        // add the checks that apply to css
        final List<Check> cssChecks = elementsMap.get("css");
        addChecks(checks, cssChecks);
    }

    private void addChecks(final List<Check> checks, final List<Check> newChecks) {
        if (newChecks != null) {
            for (Check check : newChecks) {
                checks.add(check);
            }
        }
    }

    // Evalua la lista de nodos del documento
    private List<Incidencia> evaluateLoop(final Node rootNode, final Evaluation evaluation, final Map<String, List<Check>> elementsMap, final boolean isCrawling) {
        final PropertiesManager pmgr = new PropertiesManager();
        int maxNumElements = Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "intav.max.num.html.elements"));
        long time = System.currentTimeMillis();
        final List<Incidencia> incidenceList = new ArrayList<>();
        if (rootNode != null) {
            extractCSSResources(rootNode, evaluation);

            final List<Node> nodeList = EvaluatorUtils.generateNodeList(rootNode, new ArrayList<Node>(), IntavConstants.ALL_ELEMENTS);
            int counter = 0;
            for (Node node : nodeList) {
                final String nameElement = node.getNodeName().toLowerCase();
                if (counter <= maxNumElements || nameElement.equals("body") || nameElement.equals("html")) {
                    if (nameElement.equals("html") && (node.getUserData("realHtml") != null) && (node.getUserData("realHtml").equals("false"))) {
                        continue;
                    }
                    // get a list of checks for this element
                    final List<Check> checks = new ArrayList<>();
                    addSpecificChecks(checks, elementsMap, nameElement);
                    addGeneralChecks(checks, elementsMap);
                    addCssChecks(checks, elementsMap);
                    if (checks.size() > 0) {
                        performEvaluation(node, checks, evaluation, incidenceList, isCrawling);
                    }

                    counter++;
                }

            }
        }

        Logger.putLog("Tiempo de evaluación de '" + evaluation.getFilename() + "': " + (System.currentTimeMillis() - time) + " milisegundos", Evaluator.class, Logger.LOG_LEVEL_INFO);
        return incidenceList;
    }

    private void extractCSSResources(final Node rootNode, final Evaluation evaluation) {
        if (rootNode instanceof HTMLDocument) {
            final HTMLDocument rootElement = (HTMLDocument) rootNode;
            final ImportedCSSExtractor cssExtractor = new ImportedCSSExtractor();
            evaluation.setCssResources(cssExtractor.extractFromHTMLDocument(evaluation.getFilename(), rootElement));
        }
    }

    // Evaluates any special tests (doctype etc.)
    private void evaluateSpecial(final Node nodeGiven, final Evaluation evaluation, final Map<String, List<Check>> elementsMap) {
        // get a list of checks for this element
        final PropertiesManager properties = new PropertiesManager();
        final List<Check> vectorChecks = elementsMap.get("doctype");
        if (vectorChecks != null) {
            for (Check check : vectorChecks) {
                if (check.getId() == Integer.parseInt(properties.getValue("check.properties", "contHTML.decDoctype"))) {
                    final Element elementRoot = nodeGiven.getOwnerDocument().getDocumentElement();
                    final String hasDoctype = (String) elementRoot.getUserData("doctype");
                    if (hasDoctype.equals("false")) {
                        final SimpleDateFormat format = new SimpleDateFormat(properties.getValue(IntavConstants.INTAV_PROPERTIES, "complet.date.format.ymd"));
                        final Problem problem = new Problem((Element) nodeGiven);
                        problem.setDate(format.format(new Date()));
                        problem.setCheck(check);
                        problem.setXpath(xPathGenerator.getXpath(nodeGiven));

                        evaluation.addProblem(problem);
                    }
                }
            }
        }
    }

    // sets the 'appropriate' flag for all data used in the given checks
    private void setAppropriateData(final List<Integer> checksIds, final String language) {
        final AllChecks allChecks = EvaluatorUtility.getAllChecks();

        for (Integer checkId : checksIds) {
            Check check = allChecks.getCheck(checkId);
            if (check != null) {
                check.setAppropriateData(language);
            }
        }
    }

    // Saves the global information about the analysis in DataBase and return the id of the analysis
    private int setAnalisisDB(final Evaluation evaluation, final CheckAccessibility checkAccessibility) {
        try (Connection conn = DataBaseManager.getConnection()) {
            final Analysis analysis = new Analysis();

            analysis.setDate(new Date());
            analysis.setFile("");
            analysis.setUrl(evaluation.getFilename());
            analysis.setEntity(evaluation.getEntidad());
            analysis.setTracker(evaluation.getRastreo());
            analysis.setGuideline(checkAccessibility.getGuidelineFile());
            analysis.setTracker(checkAccessibility.getIdRastreo());

            analysis.setSource(checkAccessibility.getContent());

            return AnalisisDatos.setAnalisis(conn, analysis, evaluation.getCssResources());
        } catch (Exception e) {
            Logger.putLog("Error al guardar el análisis en base de datos", Evaluator.class, Logger.LOG_LEVEL_ERROR, e);
            return -1;
        }
    }

    // Gets the global information about the analysis from DataBase and returns it
    public Evaluation getAnalisisDB(final Connection conn, final long id, final Document doc, final boolean getOnlyChecks) {
        final Analysis analysis = AnalisisDatos.getAnalisisFromId(conn, id);

        if (analysis == null) {
            return null;
        }

        Evaluation eval = new Evaluation();
        eval.setIdAnalisis(id);
        eval.setHtmlDoc(doc);
        eval.setFilename(analysis.getUrl());
        eval.setEntidad(analysis.getEntity());
        eval.setRastreo(analysis.getTracker());
        eval.addGuideline(analysis.getGuideline());
        eval.setChecksExecuted(getChecksExecuted(analysis.getChecksExecutedStr()));
        eval = getIncidenciasFromAnalisisDB(conn, eval, getOnlyChecks);
        eval.resolveProblems();
        eval.setSource(analysis.getSource());

        return eval;
    }

    public Evaluation getObservatoryAnalisisDB(final Connection conn, final long id, final Document doc) {
        final Analysis analysis = AnalisisDatos.getAnalisisFromId(conn, id);

        if (analysis == null) {
            Logger.putLog("No ha sido posible recuperar los datos del análisis " + id, Evaluator.class, Logger.LOG_LEVEL_INFO);
            return null;
        }

        Evaluation eval = new Evaluation();
        eval.setIdAnalisis(id);
        eval.setHtmlDoc(doc);
        eval.setFilename(analysis.getUrl());
        eval.setEntidad(analysis.getEntity());
        eval.setRastreo(analysis.getTracker());
        eval.addGuideline(analysis.getGuideline());
        eval.setChecksExecuted(getChecksExecuted(analysis.getChecksExecutedStr()));
        if (id < 0) {
            eval = getObservatoryIncidenciasFromAnalisisDB(conn, eval);
        } else {
            eval = getIncidenciasFromAnalisisDB(conn, eval, false);
        }
        eval.resolveProblems();

        return eval;
    }

    private List<Integer> getChecksExecuted(final String checksExecutedStr) {
        final List<Integer> results = new ArrayList<>();
        if (checksExecutedStr != null) {
            final String[] checksExecutedArray = checksExecutedStr.split(",");
            for (int i = 1; i < checksExecutedArray.length; i++) {
                results.add(Integer.valueOf(checksExecutedArray[i]));
            }
        }
        return results;
    }

    // Gets the list of problems related with an analysis from DataBase
    private Evaluation getIncidenciasFromAnalisisDB(final Connection conn, final Evaluation eval, final boolean getOnlyCheck) {
        final List<Incidencia> arrlist = IncidenciaDatos.getIncidenciasFromAnalisisId(conn, eval.getIdAnalisis(), getOnlyCheck);
        final AllChecks allChecks = EvaluatorUtility.getAllChecks();

        for (Incidencia inc : arrlist) {
            final Problem problem = new Problem();
            final Check check = allChecks.getCheck(inc.getCodigoComprobacion());
            problem.setCheck(check);
            problem.setLineNumber(inc.getCodigoLineaFuente());
            problem.setColumnNumber(inc.getCodigoColumnaFuente());
            final Element nodeRoot = createProblemTextElement(eval, inc.getCodigoFuente());
            nodeRoot.setAttribute(IntavConstants.GETTING_FROM_BD, IntavConstants.TRUE);
            problem.setNode(nodeRoot);
            eval.addProblem(problem);
            eval.setIdProblems();
        }

        return eval;
    }

    private Evaluation getObservatoryIncidenciasFromAnalisisDB(final Connection conn, final Evaluation eval) {
        final List<Incidencia> arrlist = IncidenciaDatos.getObservatoryIncidenciasFromAnalisisId(conn, eval.getIdAnalisis());
        final AllChecks allChecks = EvaluatorUtility.getAllChecks();

        for (Incidencia inc : arrlist) {
            final Problem problem = new Problem();
            final Check check = allChecks.getCheck(inc.getCodigoComprobacion());
            problem.setCheck(check);
            eval.addProblem(problem);
            eval.setIdProblems();
        }

        return eval;
    }

}
