package es.ctic.css.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.Check;
import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.ctic.css.CSSAnalyzer;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.checks.*;
import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Clase con métodos de utilidad para evaluar las comprobaciones de CSS
 */
public final class CSSUtils {

    private CSSUtils() {
    }

    /**
     * Método que evalua un check de CSS
     *
     * @param node
     * @param checkCode    parámetros de la comprobación (función y parámetros de configuración) @see Checkcode
     * @param cssResources lista con los recursos CSS que incluye la página @see CSSResource
     * @return una lista de los problemas encontrados @see CSSProblem
     */
    public static List<CSSProblem> evaluate(final Node node, final CheckCode checkCode, final List<CSSResource> cssResources) {
        try {
            final CSSAnalyzer cssAnalyzer = getCSSAnalyzer(checkCode);
            return cssAnalyzer.evaluate(node, cssResources);
        } catch (InstantiationException e) {
            Logger.putLog("No se ha podido instanciar CSSAnalyzer", CSSUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return Collections.emptyList();
    }

    /**
     * Método para obtener la instancia encargada de realizar la comprobación
     * @param checkCode objeto CheckCode con la información de la comprobación
     * @return una instancia de CSSAnalyzer que evaluará una comprobación de CSS
     * @throws InstantiationException
     */
    private static CSSAnalyzer getCSSAnalyzer(final CheckCode checkCode) throws InstantiationException {
        switch (checkCode.getFunctionId()) {
            case CheckFunctionConstants.FUNCTION_CSS_GENERATED_CONTENT:
                return new CSSGeneratedContentDocumentHandler(checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_COLOR_CONTRAST:
                return new CSSColorContrastDocumentHandler(checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_BLINK:
                return new CSSBlinkDocumentHandler(checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_PARSEABLE:
                return new CSSParseableDocumentHandler(checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_OUTLINE:
                return new CSSOutlineDocumentHandler(checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_LABEL_HIDDEN:
                return new CSSLabelHiddenStyleParser();
            default:
                Logger.putLog("Warning: unknown function id: " + checkCode.getFunctionId(), CSSUtils.class, Logger.LOG_LEVEL_WARNING);
                throw new InstantiationException("Error: unknown CSS function id: " + checkCode.getFunctionId());
        }
    }

    /**
     * Método que transforma un problema de CSS (CSSProblem) a un problema de accesibilidad (Problem)
     * @param cssProblem un problema encontrado en una CSS
     * @param check comprobacion Check asociada al problema
     * @param evaluation evaluación Evaluation que se está realizando
     * @return un problema Problem
     */
    public static Problem getProblemFromCSS(final CSSProblem cssProblem, final Check check, final Evaluation evaluation) {
        final PropertiesManager properties = new PropertiesManager();
        final SimpleDateFormat format = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));

        final Problem problem = new Problem();
        problem.setDate(format.format(cssProblem.getDate()));
        problem.setCheck(check);
        problem.setLineNumber(cssProblem.getLineNumber());
        problem.setColumnNumber(cssProblem.getColumnNumber());
        problem.setSummary(false);

        // Diferenciar si hay elemento asignado en CSSProblem y entonces usamos el elemento en caso contrario crear un elemento ficticio
        if (cssProblem.getElement() != null) {
            problem.setNode(cssProblem.getElement());
        } else {
            // Creamos un elemento ficticio
            final Element problemTextNode = evaluation.getHtmlDoc().createElement("problem-text");
            problemTextNode.setTextContent(cssProblem.getTextContent());
            problem.setNode(problemTextNode);
        }
        evaluation.addProblem(problem);

        return problem;
    }

}