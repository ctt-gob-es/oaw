package es.inteco.intav.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.*;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.*;
import es.inteco.intav.persistence.Analysis;
import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EvaluatorUtils {

    private EvaluatorUtils() {
    }

    public static EvaluationForm generateEvaluationForm(Evaluation evaluation, String language) {
        EvaluationForm evaluationForm = new EvaluationForm();
        Guideline guideline = EvaluatorUtility.loadGuideline(evaluation.getGuidelines().get(0));

        evaluationForm.setEntity(evaluation.getEntidad());
        evaluationForm.setUrl(evaluation.getFilename());
        evaluationForm.setGuideline(guideline.getName());
        evaluationForm.setSource(evaluation.getSource());

        evaluationForm.setPriorities(new ArrayList<PriorityForm>());
        for (int i = 0; i < guideline.getGroups().size(); i++) {
            GuidelineGroup group = guideline.getGroups().get(i);
            PriorityForm priorityForm = new PriorityForm();
            priorityForm.setPriorityName(group.getName());
            priorityForm.setGuidelines(new ArrayList<GuidelineForm>());
            priorityForm.getGuidelines().addAll(getGuidelinesFromGroup(group, evaluation, language));
            boolean hasContent = false;
            if (!priorityForm.getGuidelines().isEmpty()) {
                for (GuidelineForm guidelineForm : priorityForm.getGuidelines()) {
                    if (!guidelineForm.getPautas().isEmpty()) {
                        hasContent = true;
                    }
                }
            }

            if (hasContent) {
                evaluationForm.getPriorities().add(priorityForm);
            }

            // Contamos los problemas
            countProblems(priorityForm);
        }

        return evaluationForm;
    }

    public static void countProblems(PriorityForm priorityForm) {
        PropertiesManager pmgr = new PropertiesManager();
        for (GuidelineForm guidelineForm : priorityForm.getGuidelines()) {
            for (PautaForm pautaForm : guidelineForm.getPautas()) {
                for (ProblemForm problemForm : pautaForm.getProblems()) {
                    if (problemForm.getType().equals(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "confidence.level.high"))) {
                        // priorityForm.setNumProblems(priorityForm.getNumProblems() + problemForm.getSpecificProblems().size());
                        priorityForm.setNumProblems(priorityForm.getNumProblems() + 1);
                    } else if (problemForm.getType().equals(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "confidence.level.medium"))) {
                        // priorityForm.setNumWarnings(priorityForm.getNumWarnings() + problemForm.getSpecificProblems().size());
                        priorityForm.setNumWarnings(priorityForm.getNumWarnings() + 1);
                    } else if (problemForm.getType().equals(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                        // priorityForm.setNumInfos(priorityForm.getNumInfos() + problemForm.getSpecificProblems().size());
                        priorityForm.setNumInfos(priorityForm.getNumInfos() + 1);
                    }
                }
            }
        }
    }

    // Devuelve la lista de guidelines que tienen algún problema asociado.
    private static List<GuidelineForm> getGuidelinesFromGroup(GuidelineGroup group, Evaluation evaluation, String language) {
        List<GuidelineForm> guidelines = new ArrayList<>();
        // iterate any check in the group (priority)
        for (int i = 0; i < group.getGroupsVector().size(); i++) {
            GuidelineGroup subgroup = group.getGroupsVector().get(i);
            GuidelineForm guidelineForm = new GuidelineForm();
            guidelineForm.setDescription(subgroup.getName());
            guidelineForm.setPautas(new ArrayList<PautaForm>());
            guidelineForm.setPautas(getPautasFromGroup(subgroup, evaluation, language));
            // Añadimos la pauta a la lista
            guidelines.add(guidelineForm);
        }

        return guidelines;
    }

    // Devuelve la lista de pautas que tienen algún problema asociado.
    private static List<PautaForm> getPautasFromGroup(GuidelineGroup group, Evaluation evaluation, String language) {
        List<PautaForm> pautas = new ArrayList<>();
        // iterate any check in the group (priority)
        for (int i = 0; i < group.getGroupsVector().size(); i++) {
            GuidelineGroup subgroup = group.getGroupsVector().get(i);
            List<Problem> vProblems = evaluation.getHashCheckProblem().get(subgroup.getName());

            if (vProblems != null) {
                PautaForm pautaForm = new PautaForm();
                pautaForm.setName(subgroup.getName());

                vProblems = sortVectorProblems(vProblems);

                pautaForm.setProblems(getProblemsFromGuideline(vProblems, subgroup, evaluation, language));

                // Añadimos la pauta a la lista
                pautas.add(pautaForm);
            }
        }

        return pautas;
    }

    // Devuelve los tipos de problemas asociados a una pauta concreta
    public static List<ProblemForm> getProblemsFromGuideline(List<Problem> vProblems, GuidelineGroup subgroup, Evaluation evaluation, String language) {
        final List<ProblemForm> problems = new ArrayList<>();

        int lastProblem = 0;

        final PropertiesManager pmgr = new PropertiesManager();

        // now iterate for every problem in the check
        for (int j = 0; j < vProblems.size(); j++) {
            Problem problem = vProblems.get(j);

            ProblemForm problemForm;
            if (lastProblem != problem.getCheck().getId()) {
                // Si el tipo de problema es nuevo, creamos un nuevo ProblemForm para guardar el tipo de problema
                problemForm = new ProblemForm();

                // Copyrights del W3C
                if (problem.getCheck().getId() == Integer.parseInt(pmgr.getValue(IntavConstants.CHECK_PROPERTIES, "doc.valida.especif"))) {
                    problemForm.setNote("w3c.html.copyright");
                }
                if (EvaluatorUtils.isCssValidationNeeded(problem.getCheck().getId())) {
                    problemForm.setNote("w3c.css.copyright");
                }

                problemForm.setType(problem.getCheck().getConfidenceString());
                problemForm.setCheck(String.valueOf(problem.getCheck().getId()));
                problemForm.setRationale(problem.getCheck().getRationaleString());
                problemForm.setError(problem.getCheck().getErrorString());
                if (!((Element) problem.getNode()).getAttribute(IntavConstants.GETTING_FROM_BD).equals(IntavConstants.TRUE) &&
                        (problem.getCheck().getId() == 289 || problem.getCheck().getId() == 110)) {
                    if (problem.getNode().getOwnerDocument().getDocumentElement().getUserData("doctype").equals("true")) {
                        problemForm.setRationale(problem.getCheck().getRationaleString() + "-" + problem.getNode().getOwnerDocument().getDocumentElement().getUserData("doctypeType"));
                    } else {
                        problemForm.setRationale(problem.getCheck().getRationaleString());
                    }
                }

                problemForm.setSpecificProblems(new ArrayList<SpecificProblemForm>());

                problems.add(problemForm);

                lastProblem = problem.getCheck().getId();
            } else {
                problemForm = problems.get(problems.size() - 1);
            }

            SpecificProblemForm specificProblem = new SpecificProblemForm();
            specificProblem.setLine(problem.getLineNumber()!=-1?problem.getLineNumberString():"");
            specificProblem.setColumn(problem.getColumnNumber()!=-1?problem.getColumnNumberString():"");

            if (problem.isSummary()) {
                specificProblem.setNote(getCode(problem, evaluation));
            } else {
                List<String> code = getCode(problem, evaluation);
                if (code.size() == 1 && code.get(0) != null && code.get(0).contains("<|>")) {
                    specificProblem.setMessage(getMessage(code.get(0)));
                    String codeFiltered = getCode(code.get(0));
                    List<String> source = new ArrayList<>();
                    source.add(codeFiltered);
                    specificProblem.setCode(source);
                } else {
                    specificProblem.setCode(code);
                }
            }

            if ((specificProblem.getCode() != null && !specificProblem.getCode().isEmpty()) ||
                    (specificProblem.getNote() != null && !specificProblem.getNote().isEmpty())) {
                problemForm.getSpecificProblems().add(specificProblem);
            }
        }

        return problems;
    }

    // Adds information to the document that is used to display the accessibility problem.
    public static List<String> getCode(Problem problem, Evaluation evaluation) {
        List<String> code = new ArrayList<>();

        PropertiesManager properties = new PropertiesManager();
        Check check = problem.getCheck();
        Element elementProblem = (Element) problem.getNode();
        String nameProblemElement = check.getKeyElement();

        if ("a".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("img".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("area".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("body".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("title".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("input".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("html".equals(nameProblemElement)) {
            if (check.getId() == Integer.parseInt(properties.getValue("check.properties", "doc.codIdioma.valido"))) { // valid language code
                code = getHtml(elementProblem, false, false);
            } else if (check.getId() == Integer.parseInt(properties.getValue("check.properties", "doc.valida.especif")) ||
                    check.getId() == 438 || check.getId() == 439 || check.getId() == 440 || check.getId() == 441) { // valid document
                if (evaluation != null) {
                    code.add(problem.getNode().getTextContent());
                }
            }

            if (check.getId()==455 || check.getId()==456 || check.getId()==457 || check.getId()==458) {
                code.add(problem.getNode().getTextContent());
            }

            if (check.getId() == Integer.parseInt(properties.getValue("check.properties", "ordenLect.attrDir.siIdiomPrimDI"))) { // valid "dir" attribute
                code = getHtml(elementProblem, false, false);
            }

            if (check.getId() == Integer.parseInt(properties.getValue("check.properties", "html.estructure"))) { // valid "estructure" attribute
                code = getHtmlHeaders(elementProblem);
            }

            if (check.getId() == 37) {
                code.add(problem.getNode().getTextContent());
            }

            if (check.getId() == 434 || check.getId() == 435) {
                code.add(problem.getNode().getTextContent());
            }
        } else if ("legend".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } /*else if ("doctype".equals(nameProblemElement)) {

        }*/ else if ("form".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if (("h1".equals(nameProblemElement)) || ("h2".equals(nameProblemElement)) || ("h3".equals(nameProblemElement)) ||
                ("h4".equals(nameProblemElement)) || ("h5".equals(nameProblemElement)) || ("h6".equals(nameProblemElement))) {
            if ((check.getId() == Integer.parseInt(properties.getValue("check.properties", "cabeceraH1H2.trasH1"))) ||
                    (check.getId() == Integer.parseInt(properties.getValue("check.properties", "cabeceraH1H2H3.trasH2"))) ||
                    (check.getId() == Integer.parseInt(properties.getValue("check.properties", "cabeceraH1H2H3H4.trasH3"))) ||
                    (check.getId() == Integer.parseInt(properties.getValue("check.properties", "cabeceraH1H2H3H4H5.trasH4"))) ||
                    (check.getId() == Integer.parseInt(properties.getValue("check.properties", "cabeceraH1H2H2H6.trasH5")))) {
                code = getHtml(elementProblem, false, false);
            } else {
                code = getHtml(elementProblem, true, false);
            }
        } else if ("select".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("table".equals(nameProblemElement)) {
            code = getHtml(elementProblem, false, false);
        } else if ("caption".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("p".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("color".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("validation".equals(nameProblemElement)) {
            code.add(problem.getNode().getTextContent());
        } else if ("texto".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("headers".equals(nameProblemElement)) {
            code = getHtml(elementProblem, true, false);
        } else if ("problem-text".equals(nameProblemElement)) {
            Logger.putLog("Creando código a partir de PROBLEM-TEXT", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR);
            code = Collections.singletonList(elementProblem.getTextContent());
        } else {
            if ("*".equals(check.getTriggerElement())) {
                return getHtml(elementProblem, false, false);
            } else {
                return getHtml(elementProblem, true, false);
            }
        }

        return code;
    }

    private static List<String> getHtml(Element elementGiven, boolean includeElementChildren, boolean noSangrado) {
        return getHtmlText(elementGiven, includeElementChildren, noSangrado);
    }

    private static List<String> getHtmlHeaders(Element elementGiven) {
        List<String> codigo = new ArrayList<>();
        List<Node> headersList = getNodeHeaders(elementGiven);

        for (Node node : headersList) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                codigo.addAll(getHtmlText((Element) node, true, true));
            }
        }

        return codigo;
    }

    private static List<Node> getNodeHeaders(Element elementGiven) {
        try {
            List<Node> nodeHeaders = new ArrayList<>();
            if (elementGiven != null) {
                if (("h1".equalsIgnoreCase(elementGiven.getNodeName())) ||
                        ("h2".equalsIgnoreCase(elementGiven.getNodeName())) ||
                        ("h3".equalsIgnoreCase(elementGiven.getNodeName())) ||
                        ("h4".equalsIgnoreCase(elementGiven.getNodeName())) ||
                        ("h5".equalsIgnoreCase(elementGiven.getNodeName())) ||
                        ("h6".equalsIgnoreCase(elementGiven.getNodeName()))) {
                    nodeHeaders.add(elementGiven);
                }
                NodeList nodeList = elementGiven.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getChildNodes() != null) {
                        nodeHeaders.addAll(getNodeHeaders((Element) nodeList.item(i)));
                    }
                }
            }
            return nodeHeaders;
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return Collections.emptyList();
        }
    }

    public static String serializeXmlElement(Element element, boolean includeChildren) {
        DOMImplementationLS domImplementationLS = (DOMImplementationLS) element.getOwnerDocument().getImplementation();
        LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
        if (element.getNodeType() != Node.DOCUMENT_NODE) {
            lsSerializer.getDomConfig().setParameter("well-formed", false);
            lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        }
        try {
            PropertiesManager pmgr = new PropertiesManager();
            int maxNumElements = Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "max.num.descendants.to.serialize"));
            int numDescendants = generateNodeList(element, new ArrayList<Node>(), maxNumElements).size();
            if (includeChildren && numDescendants <= maxNumElements) {
                return lsSerializer.writeToString(element);
            } else {
                return serializeOnlyElement(element);
            }
        } catch (Exception e) {
            Logger.putLog("Error al serializar el elemento " + element.getNodeName(), EvaluatorUtils.class, Logger.LOG_LEVEL_INFO);
            return element.getNodeName();
        }
    }

    // Genera recursivamente una lista de nodos del documento
    public static List<Node> generateNodeList(Node node, List<Node> nodeList, int maxNumElements) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                    (node.getNodeType() == Node.DOCUMENT_TYPE_NODE)) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateNodeList(node.getChildNodes().item(x), nodeList, maxNumElements);
                }
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    private static String serializeOnlyElement(final Element element) {
        final StringBuilder serialization = new StringBuilder("<");
        serialization.append(element.getNodeName());
        final NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            serialization.append(" ").append(attributes.item(i).getNodeName()).append("=\"").append(attributes.item(i).getNodeValue()).append("\"");
        }
        serialization.append(">");

        return serialization.toString();
    }

    public static String serializeXmlElement(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return serializeXmlElement((Element) node, false);
        } else {
            return node.getTextContent();
        }
    }

    private static List<String> getHtmlText(Element elementGiven, boolean bIncludeChildren, boolean noSangrado) {
        final PropertiesManager pmgr = new PropertiesManager();
        final int sourceMaxNumChar = Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "source.max.num.characters"));
        try {
            if (elementGiven != null) {
                final List<String> codigo = new ArrayList<>();
                final String codigoString;

                if (elementGiven.getAttribute(IntavConstants.GETTING_FROM_BD).equals(IntavConstants.TRUE)) {
                    codigoString = elementGiven.getTextContent();
                } else {
                    codigoString = serializeXmlElement(elementGiven, bIncludeChildren);
                }

                if (codigoString != null) {
                    if (!noSangrado) {
                        final String regexp = "(.*?\\\n)|(<.*?>)|([^<>]*)";
                        final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(codigoString.replaceAll("\\s{2,}",""));

                        int numChar = 0;

                        while (matcher.find()) {
                            String cadena = "";
                            if (matcher.group(1) != null) {
                                cadena = matcher.group(1).trim();
                            } else if (matcher.group(2) != null) {
                                cadena = matcher.group(2).trim();
                            } else if (matcher.group(3) != null) {
                                cadena = matcher.group(3).trim();
                            }

                            if (cadena.length() != 0) {
                                if (numChar + cadena.length() <= sourceMaxNumChar) {
                                    codigo.add(HTMLEntities.htmlAngleBrackets(cadena));
                                    numChar += cadena.length();
                                } else {
                                    int numChars = sourceMaxNumChar - numChar;
                                    try {
                                        if (numChars > 0) {
                                            codigo.add(HTMLEntities.htmlAngleBrackets(cadena.substring(0, numChars - 1)));
                                        }
                                    } catch (Exception e) {
                                        // TODO: ver si hay que hacer algo
                                    }
                                    codigo.add("...");
                                    break;
                                }
                            }
                        }
                    } else {
                        codigo.add(HTMLEntities.htmlAngleBrackets(codigoString.trim().replaceAll("\\s{2,}","")));
                    }
                }

                return codigo;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    public static Evaluation evaluate(CheckAccessibility checkAccessibility, String language) throws Exception {
        Logger.putLog("Iniciando evaluación de accesibilidad de la url: " + checkAccessibility.getUrl(), EvaluatorUtils.class, Logger.LOG_LEVEL_INFO);

        Evaluator evaluator = EvaluatorUtility.getEvaluator();

        return evaluator.evaluate(checkAccessibility, language);
    }

    public static Evaluation evaluateContent(CheckAccessibility checkAccessibility, String language) throws Exception {
        Evaluator evaluator = EvaluatorUtility.getEvaluator();
        long inicio = System.currentTimeMillis();
        Evaluation evaluation = null;
        try {
            evaluation = evaluator.evaluateContent(checkAccessibility, language);
            //Si idRastreo es 0 viene de la aplicación stand alone (no se guarda en BBDD)
            //Si idRastreo es -1 viene del WS se guarda el análisis sin rastreo asociado
            if (checkAccessibility.getIdRastreo() != 0) {
                // Venimos del rastreador así que hay que salvar los resultados
                evaluation.settevaluation(System.currentTimeMillis() - inicio);
                AnalisisDatos.endAnalysisSuccess(evaluation);
            }
        } catch (Exception e) {
            Logger.putLog("Se va a guardar el registro de error de la página " + checkAccessibility.getUrl(), EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            AnalisisDatos.setAnalysisError(checkAccessibility);
        }

        return evaluation;
    }

    public static List<AnalysisForm> getFormList(List<Analysis> listAnalysis) {
        List<AnalysisForm> formList = new ArrayList<>();
        PropertiesManager pmgr = new PropertiesManager();

        try {
            DateFormat df = new SimpleDateFormat(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "complet.date.format"));
            for (Analysis analysis : listAnalysis) {
                AnalysisForm analysisForm = new AnalysisForm();
                BeanUtils.copyProperties(analysisForm, analysis);
                analysisForm.setDate(df.format(analysis.getDate()));
                formList.add(analysisForm);
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return formList;
    }

    public static List<PageForm> createPagination(HttpServletRequest request, int numResult) {
        int currentPage = 1;
        if (request.getParameter(IntavConstants.PAG_PARAM) != null) {
            currentPage = Integer.parseInt(request.getParameter(IntavConstants.PAG_PARAM));
        }

        PropertiesManager properties = new PropertiesManager();
        List<PageForm> pageFormList = new ArrayList<>();
        String path = request.getRequestURL() + "?" + request.getQueryString();
        path = removeParameterP(path);
        int numPages = (int) Math.ceil((float) numResult / (float) Integer.parseInt(properties.getValue("intav.properties", "pagination.size")));

        for (int i = 1; i <= numPages; i++) {
            PageForm pForm = new PageForm();
            pForm.setTitle(String.valueOf(i));
            if (i != currentPage) {
                pForm.setPath(path);
            }
            pageFormList.add(pForm);
        }
        return pageFormList;
    }

    public static List<PageForm> createPagination(HttpServletRequest request, int numResult, int pagSize, int currentPage) {
        List<PageForm> pageFormList = new ArrayList<>();
        String path = request.getRequestURL() + "?" + request.getQueryString();
        path = removeParameterP(path);
        int numPages = (int) Math.ceil((float) numResult / (float) pagSize);

        for (int i = 1; i <= numPages; i++) {
            PageForm pForm = new PageForm();
            pForm.setTitle(String.valueOf(i));
            if (i != currentPage) {
                pForm.setPath(path);
            }
            pageFormList.add(pForm);
        }
        return pageFormList;
    }

    private static String removeParameterP(String parameterP) {
        return parameterP.replaceAll("&p=\\d+", "").replaceAll("&prefolP=foll+", "").replaceAll("&prefolP=prev+", "");
    }

    // Crea un objeto de tipo Documento
    public static Document getDocList() throws ParserConfigurationException {
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    public static List<String> getFramesUrl(String content) throws Exception {
        List<String> frames = new ArrayList<>();

        PropertiesManager pmgr = new PropertiesManager();
        String regexp = pmgr.getValue("intav.properties", "frame.reg.exp.matcher");

        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String frame = matcher.group(1).replaceAll("\n", "");
            if (!frames.contains(frame)) {
                frames.add(frame);
            }
        }

        return frames;

    }

    public static List<String> getFramesUrl(URL url) throws Exception {
        HttpURLConnection connection = EvaluatorUtils.getConnection(url.toString(), "GET", false);
        connection.connect();
        int responseCode = connection.getResponseCode();

        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            return getFramesUrl(StringUtils.getContentAsString(connection.getInputStream()));
        } else {
            return null;
        }
    }

    private static String getMessage(String string) {
        String regexp = IntavConstants.REGEXP_MESSAGE_FILTER_INCLUDE;

        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String getCode(String string) {
        String regexp = IntavConstants.REGEXP_MESSAGE_FILTER_EXCLUDE;

        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return string.replace(matcher.group(1), "");
        } else {
            return null;
        }
    }

    public static Node getPreviousNode(Element elementGiven) {
        Node nodePrevious = elementGiven.getPreviousSibling();
        boolean found = false;
        while (nodePrevious != null && !found) {
            if (nodePrevious.getNodeType() == Node.TEXT_NODE) {
                if (StringUtils.isOnlyBlanks(nodePrevious.getTextContent()) || nodePrevious.getTextContent().trim().replaceAll("&nbsp;", "").length() <= 1) {
                    nodePrevious = nodePrevious.getPreviousSibling();
                } else {
                    found = true;
                }
            } else if (nodePrevious.getNodeType() == Node.ELEMENT_NODE) {
                found = true;
            } else {
                nodePrevious = nodePrevious.getPreviousSibling();
            }
        }

        return nodePrevious;
    }

    public static Element getPreviousElement(Element elementGiven, boolean filterInlineElements) {
        List<String> inlineTags = new ArrayList<>();
        if (filterInlineElements) {
            PropertiesManager pmgr = new PropertiesManager();
            inlineTags = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "inline.tags.list").split(";"));
        }

        Node nodePrevious = elementGiven.getPreviousSibling();
        boolean found = false;
        while (nodePrevious != null && !found) {
            if (nodePrevious.getNodeType() == Node.ELEMENT_NODE &&
                    (!filterInlineElements || !inlineTags.contains(nodePrevious.getNodeName().toUpperCase()))) {
                found = true;
            } else {
                nodePrevious = nodePrevious.getPreviousSibling();
            }
        }

        if (nodePrevious != null) {
            return (Element) nodePrevious;
        } else {
            return null;
        }
    }

    public static Element getNextElement(Element elementGiven, boolean filterInlineElements) {
        List<String> inlineTags = new ArrayList<>();
        if (filterInlineElements) {
            PropertiesManager pmgr = new PropertiesManager();
            inlineTags = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "inline.tags.list").split(";"));
        }

        Node nodeNext = elementGiven.getNextSibling();
        boolean found = false;
        while (nodeNext != null && !found) {
            if (nodeNext.getNodeType() == Node.ELEMENT_NODE &&
                    (!filterInlineElements || !inlineTags.contains(nodeNext.getNodeName().toUpperCase()))) {
                found = true;
            } else {
                nodeNext = nodeNext.getNextSibling();
            }
        }

        if (nodeNext != null) {
            return (Element) nodeNext;
        } else {
            return null;
        }
    }

    private static Map<String, List<Integer>> initializeAspects() {
        final Map<String, List<Integer>> aspectMap = new HashMap<>();
        final PropertiesManager pmgr = new PropertiesManager();
        final List<String> aspects = Arrays.asList(pmgr.getValue("intav.properties", "observatory.key.aspects").split(","));

        for (String key : aspects) {
            aspectMap.put(key, new ArrayList<Integer>());
        }

        return aspectMap;
    }

    /**
     * Método para crear el objeto de evaluación del observatorio. Su funcionamiento se basa en
     * comparar los checks que han provocado errores en el objeto <code>evaluation</code> con los
     * grupos de checks en el objeto <code>guideline</code>. Un check que haya provocado un error
     * hará que el grupo entero se marque como erróneo (variable <code>hasProblem</code>),
     * salvo que ese check se encuentre en la lista de checks que solo deben marcar advertencias, en
     * cuyo caso solo provocará una advertencia en el grupo (variable <code>hasWarning</code>).
     * <p/>
     * También está el caso especial de algunos checks, cuya ejecución no provocará que se marque
     * la bandera de grupo ejecutado. Esto es importante por ejemplo para las verificaciones de las listas
     * en la se analizan los elementos HTML <code>&lt;P&gt;</code>, pero estos no implican por si
     * mismos que haya listas en el documento.
     * <p/>
     * Se calcula también la puntuación por aspectos, a través de un mapa que asociará cada aspecto
     * (asociado a un grupo) con una lista de puntuaciones de cada check que forma el grupo.
     *
     * @param evaluation
     * @param methodology
     * @param isDebugMode
     * @return
     */
    public static ObservatoryEvaluationForm generateObservatoryEvaluationForm(Evaluation evaluation, String methodology, boolean isDebugMode) {
        Guideline guideline;
        if (StringUtils.isEmpty(methodology)) {
            guideline = EvaluatorUtility.loadGuideline(evaluation.getGuidelines().get(0));
        } else {
            guideline = new Guideline();
            guideline = guideline.initialize(methodology);
        }
        final ObservatoryEvaluationForm evaluationForm = new ObservatoryEvaluationForm();

        evaluationForm.setEntity(evaluation.getEntidad());
        evaluationForm.setUrl(evaluation.getFilename());
        evaluationForm.setCrawlerExecutionId(evaluation.getRastreo());
        evaluationForm.setChecksFailed(evaluation.getChecksFailed());
        evaluationForm.setSource(evaluation.getSource());
        final Map<String, List<Integer>> aspects = initializeAspects();

        for (int i = 0; i < guideline.getGroups().size(); i++) {
            final GuidelineGroup levelGroup = guideline.getGroups().get(i);
            final ObservatoryLevelForm observatoryLevelForm = new ObservatoryLevelForm();
            observatoryLevelForm.setName(levelGroup.getName());
            for (int j = 0; j < levelGroup.getGroupsVector().size(); j++) {
                final GuidelineGroup suitabilityGroup = levelGroup.getGroupsVector().get(j);
                final ObservatorySuitabilityForm observatorySuitabilityForm = new ObservatorySuitabilityForm();
                observatorySuitabilityForm.setName(suitabilityGroup.getName());
                for (int k = 0; k < suitabilityGroup.getGroupsVector().size(); k++) {
                    final GuidelineGroup subgroup = suitabilityGroup.getGroupsVector().get(k);
                    final ObservatorySubgroupForm observatorySubgroupForm = new ObservatorySubgroupForm();
                    observatorySubgroupForm.setDescription(subgroup.getName());
                    observatorySubgroupForm.setAspect(subgroup.getAspect());
                    boolean hasProblem = false;
                    boolean hasWarning = false;
                    boolean executedSubgroup = false;
                    for (int l = 0; l < subgroup.getChecksVector().size(); l++) {
                        final Integer check = subgroup.getChecksVector().get(l);
                        if (evaluation.getChecksExecuted().contains(check)) {
                            if (!subgroup.getNoExecutedMarkChecks().contains(check)) {
                                executedSubgroup = true;
                            }
                            if (evaluation.failsCheck(check)) {
                                if (subgroup.getOnlyWarningChecks().contains(check)) {
                                    hasWarning = true;
                                    observatorySubgroupForm.getOnlyWarningChecks().add(check);
                                } else {
                                    hasProblem = true;
                                    observatorySubgroupForm.getFailChecks().add(check);
                                }

                                if (subgroup.getRelatedChecks().containsKey(check)) {
                                    observatorySubgroupForm.getIgnoreRelatedChecks().add(subgroup.getRelatedChecks().get(check));
                                }
                            }
                        }
                    }


                    if (evaluationForm.getCrawlerExecutionId() < 0 || isDebugMode) {
                        List<Problem> vProblems = evaluation.getHashCheckProblem().get(subgroup.getName());
                        if (vProblems != null) {
                            vProblems = sortVectorProblems(vProblems);
                            List<ProblemForm> problemsForm = getProblemsFromGuideline(vProblems, subgroup, evaluation, "es");
                            for (ProblemForm problemForm : problemsForm) {
                                if ((observatorySubgroupForm.getFailChecks().contains(Integer.parseInt(problemForm.getCheck())) ||
                                        observatorySubgroupForm.getOnlyWarningChecks().contains(Integer.parseInt(problemForm.getCheck()))) &&
                                        !observatorySubgroupForm.getIgnoreRelatedChecks().contains(Integer.parseInt(problemForm.getCheck()))) {
                                    observatorySubgroupForm.getProblems().add(problemForm);
                                }

                            }
                        }
                    }

                    addSubgroupScore(observatorySubgroupForm, subgroup, aspects, hasProblem, hasWarning, executedSubgroup);

                    observatorySuitabilityForm.getSubgroups().add(observatorySubgroupForm);
                }

                observatorySuitabilityForm.setScore(getPartialScore(observatorySuitabilityForm));
                observatoryLevelForm.getSuitabilityGroups().add(observatorySuitabilityForm);
            }

            observatoryLevelForm.setScore(getPartialScore(observatoryLevelForm));
            evaluationForm.getGroups().add(observatoryLevelForm);
        }

        evaluationForm.setScore(getEvaluationScore(evaluationForm));

        evaluationForm.setAspects(getAspectScore(aspects));

        return evaluationForm;
    }

    private static List<Problem> sortVectorProblems(List<Problem> vProblems) {
        Collections.sort(vProblems, new Comparator<Problem>() {
            public int compare(Problem p1, Problem p2) {
                return p1.getCheck().getId() - p2.getCheck().getId();
            }
        });
        return vProblems;
    }

    /**
     * Método que asigna al grupo de verificaciones, las puntuaciones definidas para el observatorio.
     * Estas puntuaciones son:
     * <dl>
     * <dt>Cero rojo:<dt>
     * <dd>
     * Se asigna si se ha detectado algún problema en el grupo, o si este grupo
     * no se ha ejecutado pero utilizarlo constituye una buena práctica (P.E. enlaces de accesibilidad
     * tienen que fallar si no hay enlaces en la página)
     * </dd>
     * <dt>Cero verde:<dt>
     * <dd>
     * Se asigna si se ha producido una advertencia y ningún problema, o si se ha analizado el grupo
     * sin errores, pero utilizar los elementos del grupo constituye una mala práctica
     * </dd>
     * <dt>Uno verde:<dt>
     * <dd>
     * Se asigna si el grupo se ha analizado y no se ha encontrado ningún error, o si el grupo no ha sido
     * analizado por no encontrarse sus elementos, y no utilizarlo constituye una buena práctica.
     * </dd>
     * <dt>No puntúa:<dt>
     * <dd>
     * Se asigna si el grupo no se ha analizado, y utilizarlo no supone ni una buena práctica ni una mala
     * práctica.
     * </dd>
     * </dl>
     *
     * @param observatorySubgroupForm
     * @param subgroup
     * @param hasProblem
     * @param hasWarning
     * @param executedSubgroup
     */
    private static void addSubgroupScore(ObservatorySubgroupForm observatorySubgroupForm, GuidelineGroup subgroup, Map<String, List<Integer>> aspects, boolean hasProblem, boolean hasWarning, boolean executedSubgroup) {
        if (hasProblem) {
            // Si existe algún problema, le damos una mala nota
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_RED_ZERO);
            addAspectScore(aspects, subgroup.getAspect(), 0);
        } else if (hasWarning) {
            // Si no existe problema, pero existe advertencia, le damos un aviso
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_GREEN_ZERO);
            addAspectScore(aspects, subgroup.getAspect(), 0);
        } else if (executedSubgroup && !subgroup.getType().equalsIgnoreCase(IntavConstants.BAD_PRACTICE)) {
            // Si no existen problemas, se han utilizado los elementos y no es una mala práctica utilizarlos,
            // le damos una buena nota
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_GREEN_ONE);
            addAspectScore(aspects, subgroup.getAspect(), 1);
        } else if (executedSubgroup && subgroup.getType().equalsIgnoreCase(IntavConstants.BAD_PRACTICE)) {
            // Si no existen problemas, se han utilizado los elementos y usarlos constituye una mala práctica,
            // le damos un cero verde
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_GREEN_ZERO);
            addAspectScore(aspects, subgroup.getAspect(), 0);
        } else if (!executedSubgroup && !subgroup.getType().equalsIgnoreCase(IntavConstants.BAD_PRACTICE)
                && !subgroup.getType().equalsIgnoreCase(IntavConstants.GOOD_PRACTICE)) {
            // Si no se ha utilizado, y utilizarlo no es una mala práctica ni buena práctica, no puntúa
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_NOT_SCORE);
        } else if (!executedSubgroup && subgroup.getType().equalsIgnoreCase(IntavConstants.BAD_PRACTICE)) {
            // Si no se ha utilizado, pero es una mala práctica utilizarlo, le damos una buena nota
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_GREEN_ONE);
            addAspectScore(aspects, subgroup.getAspect(), 1);
        } else if (!executedSubgroup && subgroup.getType().equalsIgnoreCase(IntavConstants.GOOD_PRACTICE)) {
            // Si no se ha utilizado, pero utilizarlo es una buena práctica, le damos una mala nota
            observatorySubgroupForm.setValue(IntavConstants.OBS_VALUE_RED_ZERO);
            addAspectScore(aspects, subgroup.getAspect(), 0);
        }
    }

    private static void addAspectScore(Map<String, List<Integer>> aspects, String aspectName, int value) {
        if (aspects.get(aspectName) != null) {
            aspects.get(aspectName).add(value);
        } else {
            aspects.put(aspectName, new ArrayList<Integer>());
            aspects.get(aspectName).add(value);
        }
    }


    /**
     * Se calcula la media de puntuación de los aspectos.
     *
     * @param aspects
     * @return
     */
    private static List<AspectScoreForm> getAspectScore(Map<String, List<Integer>> aspects) {
        List<AspectScoreForm> aspectScore = new ArrayList<>();
        PropertiesManager pmgr = new PropertiesManager();
        for (Map.Entry<String, List<Integer>> entry : aspects.entrySet()) {
            AspectScoreForm aspectScoreForm = new AspectScoreForm();
            aspectScoreForm.setName(entry.getKey());
            aspectScoreForm.setId(Long.valueOf(pmgr.getValue("intav.properties", entry.getKey())));
            List<Integer> scores = entry.getValue();
            if (scores.isEmpty()) {
                aspectScoreForm.setScore(new BigDecimal(IntavConstants.OBS_VALUE_ASPECT_NOT_SCORE));
            } else {
                int totalScore = 0;
                for (Integer score : scores) {
                    totalScore += score;
                }
                aspectScoreForm.setScore(calculateScore(totalScore, scores.size()));
            }
            aspectScore.add(aspectScoreForm);
        }


        return aspectScore;
    }

    /**
     * Calcula la puntuación total de la evaluación. Se suma un punto por cada <code>Uno Verde</code>
     * y se divide entre el número de verificaciones que se han tenido en cuenta.
     *
     * @param evaluationForm
     * @return
     */
    private static BigDecimal getEvaluationScore(ObservatoryEvaluationForm evaluationForm) {
        int score = 0;
        int numChecks = 0;
        for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
            for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
                for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                    if (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ONE) {
                        score++;
                        numChecks++;
                    } else if ((observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ZERO) ||
                            (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_RED_ZERO)) {
                        numChecks++;
                    }
                }
            }
        }

        try {
            return calculateScore(score, numChecks);
        } catch (Exception e) {
            Logger.putLog("Error al calcular la puntuación del observatorio", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal getPartialScore(ObservatoryLevelForm observatoryLevelForm) {
        int score = 0;
        int numChecks = 0;
        for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                if (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ONE) {
                    score++;
                    numChecks++;
                } else if ((observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ZERO) ||
                        (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_RED_ZERO)) {
                    numChecks++;
                }
            }
        }

        try {
            return calculateScore(score, numChecks);
        } catch (Exception e) {
            Logger.putLog("Error al calcular la puntuación del observatorio", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal getPartialScore(ObservatorySuitabilityForm observatorySuitabilityForm) {
        int score = 0;
        int numChecks = 0;
        for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
            if (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ONE) {
                score++;
                numChecks++;
            } else if ((observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_GREEN_ZERO) ||
                    (observatorySubgroupForm.getValue() == IntavConstants.OBS_VALUE_RED_ZERO)) {
                numChecks++;
            }
        }

        try {
            if (numChecks == 0) {
                return BigDecimal.ZERO;
            } else {
                return calculateScore(score, numChecks);
            }
        } catch (ArithmeticException ae) {
            Logger.putLog("No se ha podido calcular la puntuación", EvaluatorUtils.class, Logger.LOG_LEVEL_INFO);
            return BigDecimal.ZERO;
        } catch (Exception e) {
            Logger.putLog("Error al calcular la puntuación del observatorio", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal calculateScore(int score, int numChecks) throws ArithmeticException {
        if (numChecks != 0) {
            return new BigDecimal(score).divide(new BigDecimal(numChecks), 2, BigDecimal.ROUND_HALF_UP).
                    multiply(BigDecimal.TEN).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public static String getEntityName(List<EvaluationForm> evaList) {
        if (evaList.get(0).getEntity().contains("-")) {
            return evaList.get(0).getEntity().substring(0, evaList.get(0).getEntity().indexOf("-"));
        } else {
            return evaList.get(0).getEntity();
        }
    }

    public static String getObservatoryEntityName(List<ObservatoryEvaluationForm> evaList) {
        if (evaList.get(0).getEntity().contains("-")) {
            return evaList.get(0).getEntity().substring(0, evaList.get(0).getEntity().indexOf("-"));
        } else {
            return evaList.get(0).getEntity();
        }
    }

    public static boolean passConcurrence() {
        PropertiesManager pmgr = new PropertiesManager();
        int concurrentUsers = EvaluatorUtility.getConcurrentUsers();
        int counter = 0;
        while (concurrentUsers >= Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "max.num.concurrent.users"))) {
            try {
                Logger.putLog("Hay demasiados usuarios concurrentes, se va a esperar para atender la solicitud del usuario", EvaluatorUtils.class, Logger.LOG_LEVEL_INFO);
                concurrentUsers = EvaluatorUtility.getConcurrentUsers();

                if (counter >= Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "num.retries.check.concurrence"))) {
                    Logger.putLog("Se va a desatender la solicitud debido a que hay demasiados usuarios concurrentes", EvaluatorUtils.class, Logger.LOG_LEVEL_INFO);
                    return false;
                }

                Random r = new Random(System.currentTimeMillis());
                Thread.sleep(r.nextInt(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "time.retry.check.concurrence"))));
                counter++;
            } catch (Exception e) {
                Logger.putLog("Exception: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return true;
    }

    public static HttpURLConnection getConnection(final String url, final String method, final boolean followRedirects) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setRequestMethod(method);
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(getNaiveSSLSocketFactory());
        }
        final PropertiesManager pmgr = new PropertiesManager();
        connection.setConnectTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
        connection.setReadTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
        connection.addRequestProperty("Accept", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "method.accept.header"));
        connection.addRequestProperty("Accept-Language", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "method.accept.language.header"));
        connection.addRequestProperty("User-Agent", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "method.user.agent.header"));
        return connection;
    }

    private static SSLSocketFactory getNaiveSSLSocketFactory() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            Logger.putLog("Excepción: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    public static String getResponseCharset(final HttpURLConnection connection, final InputStream markableInputStream) throws Exception {
        String charset = IntavConstants.DEFAULT_ENCODING;
        boolean found = false;

        // Buscamos primero en las cabeceras de la respuesta
        try {
            final String header = connection.getHeaderField("Content-type");
            String charsetValue = header.substring(header.indexOf("charset"));
            charsetValue = charsetValue.substring(charsetValue.indexOf('=') + 1);
            if (StringUtils.isNotEmpty(charsetValue)) {
                charset = charsetValue;
                found = true;
            }
        } catch (Exception e) {
            // found = false;
        }

        // Si no lo hemos encontrado en las cabeceras, intentaremos buscarlo en la etiqueta <meta> correspondiente
        if (!found) {
            final String regexp = "<meta.*charset=(.*?)\"";
            final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            final Matcher matcher = pattern.matcher(StringUtils.getContentAsString(markableInputStream));
            if (matcher.find()) {
                charset = matcher.group(1);
                found = true;
            }

            // Reseteamos el InputStream para poder leerlo de nuevo más tarde
            markableInputStream.reset();
        }

        if (found && !isValidCharset(charset)) {
            charset = IntavConstants.DEFAULT_ENCODING;
        }

        return charset;
    }

    private static boolean isValidCharset(final String charset) {
        try {
            byte[] test = new byte[10];
            new String(test, charset);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getLinkText(final Element link) {
        final StringBuilder linkText = new StringBuilder();
        if (StringUtils.isNotEmpty(link.getTextContent())) {
            linkText.append(link.getTextContent().trim());
        }

        final NodeList imgs = link.getElementsByTagName("img");
        for (int i = 0; i < imgs.getLength(); i++) {
            final Element img = (Element) imgs.item(i);
            if (img.hasAttribute("alt")) {
                linkText.append(" ").append(img.getAttribute("alt"));
            }
        }
        return linkText.toString();
    }

    // returns the HTML element for this document
    public static Node getHtmlElement(Node nodeDocument) {
        NodeList childNodes = nodeDocument.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x).getNodeType() == Node.ELEMENT_NODE) {
                if (childNodes.item(x).getNodeName().compareToIgnoreCase("html") == 0) {
                    return childNodes.item(x);
                }
            }
        }
        return null;
    }

    public static String serializeGuidelineToXml(Guideline guideline) throws Exception {
        StringWriter sw = new StringWriter();
        try {
            OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
            XMLSerializer serializer = new XMLSerializer(sw, of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.startElement("", "", "guideline", null);
            hd.startElement("", "", "type", null);
            hd.characters(guideline.getType().toCharArray(), 0, guideline.getType().length());
            hd.endElement("", "", "type");

            for (int i = 0; i < guideline.getGroups().size(); i++) {
                GuidelineGroup levelGroup = guideline.getGroups().get(i);
                hd.startElement("", "", "group", null);
                hd.startElement("", "", "name", null);
                hd.characters(levelGroup.getName().toCharArray(), 0, levelGroup.getName().length());
                hd.endElement("", "", "name");

                for (int j = 0; j < levelGroup.getGroupsVector().size(); j++) {
                    GuidelineGroup suitabilityGroup = levelGroup.getGroupsVector().get(j);
                    hd.startElement("", "", "suitability", null);
                    hd.startElement("", "", "name", null);
                    hd.characters(suitabilityGroup.getName().toCharArray(), 0, suitabilityGroup.getName().length());
                    hd.endElement("", "", "name");

                    for (int k = 0; k < suitabilityGroup.getGroupsVector().size(); k++) {
                        GuidelineGroup subgroup = suitabilityGroup.getGroupsVector().get(k);
                        AttributesImpl subgroupAttributes = new AttributesImpl();
                        subgroupAttributes.addAttribute("", "", "aspect", "", subgroup.getAspect());

                        if (StringUtils.isNotEmpty(subgroup.getType())) {
                            subgroupAttributes.addAttribute("", "", "type", "", subgroup.getType());
                        }

                        hd.startElement("", "", "subgroup", subgroupAttributes);
                        hd.startElement("", "", "name", null);
                        hd.characters(subgroup.getName().toCharArray(), 0, subgroup.getName().length());
                        hd.endElement("", "", "name");

                        hd.startElement("", "", "checks", null);
                        for (int l = 0; l < subgroup.getChecksVector().size(); l++) {
                            Integer check = subgroup.getChecksVector().get(l);

                            AttributesImpl checkAttributes = new AttributesImpl();
                            checkAttributes.addAttribute("", "", "id", "", check.toString());
                            if (subgroup.getNoExecutedMarkChecks().contains(check)) {
                                checkAttributes.addAttribute("", "", "noExecutedMark", "", "true");
                            }
                            if (subgroup.getOnlyWarningChecks().contains(check)) {
                                checkAttributes.addAttribute("", "", "onlyWarning", "", "true");
                            }
                            if (subgroup.getRelatedChecks().containsKey(check)) {
                                checkAttributes.addAttribute("", "", "relatedWith", "", subgroup.getRelatedChecks().get(check).toString());
                            }

                            hd.startElement("", "", "check", checkAttributes);
                            hd.endElement("", "", "check");
                        }
                        hd.endElement("", "", "checks");
                        hd.endElement("", "", "subgroup");
                    }
                    hd.endElement("", "", "suitability");
                }
                hd.endElement("", "", "group");
            }

            hd.endElement("", "", "guideline");
            hd.endDocument();
        } catch (Exception e) {
            Logger.putLog("Error al serializar la metodología", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return sw.toString();
    }

    public static List<Element> getElementsByTagName(Document document, String tag) {
        final List<Element> elements = new ArrayList<>();

        NodeList allElements = document.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static List<Element> getElementsByTagName(Element element, String tag) {
        final List<Element> elements = new ArrayList<>();

        NodeList allElements = element.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    // Es necesaria la validación html
    public static boolean isHtmlValidationNeeded(List<Integer> checkSelected) {
        PropertiesManager pmgr = new PropertiesManager();
        return checkSelected.contains(Integer.parseInt(pmgr.getValue(IntavConstants.CHECK_PROPERTIES, "doc.valida.especif")))
                || checkSelected.contains(Integer.parseInt(pmgr.getValue(IntavConstants.CHECK_PROPERTIES, "doc.valida.especif.observatory")))
                || checkSelected.contains(440);
    }

    // Es necesaria la validación css
    public static boolean isCssValidationNeeded(List<Integer> checkSelected) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> cssValidationChecks = Arrays.asList(pmgr.getValue(IntavConstants.CHECK_PROPERTIES, "css.valida.especif").split(";"));
        for (String cssValidationCheck : cssValidationChecks) {
            if (checkSelected.contains(Integer.parseInt(cssValidationCheck))) {
                return true;
            }
        }
        return false;
    }

    // Es necesaria la validación css
    public static boolean isCssValidationNeeded(Integer check) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> cssValidationChecks = Arrays.asList(pmgr.getValue(IntavConstants.CHECK_PROPERTIES, "css.valida.especif").split(";"));
        for (String cssValidationCheck : cssValidationChecks) {
            if (check.equals(Integer.parseInt(cssValidationCheck))) {
                return true;
            }
        }
        return false;
    }

    public static Element getFirstElement(Element elementGiven, boolean filterInlineElements) {
        List<String> inlineTags = new ArrayList<>();
        if (filterInlineElements) {
            PropertiesManager pmgr = new PropertiesManager();
            inlineTags = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "inline.tags.list").split(";"));
        }

        Node nodeNext = elementGiven.getFirstChild();
        boolean found = false;
        while (nodeNext != null && !found) {
            if (nodeNext.getNodeType() == Node.ELEMENT_NODE &&
                    (!filterInlineElements || !inlineTags.contains(nodeNext.getNodeName().toUpperCase()))) {
                found = true;
            } else {
                nodeNext = nodeNext.getFirstChild();
            }
        }

        if (nodeNext != null) {
            return (Element) nodeNext;
        } else {
            return null;
        }
    }
}