package es.ctic.css.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.Check;
import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.checks.*;
import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class CSSUtils {

    private CSSUtils() {
    }

    /**
     * Método que evalua un check de CSS
     *
     * @param checkCode    parámetros de la comprobación (función y parámetros de configuración) @see Checkcode
     * @param cssResources lista con los recursos CSS @see CSSResource que incluye la página
     * @return una lista de los problemas encontrados @see CSSProblem
     */
    public static List<CSSProblem> evaluate(CheckCode checkCode, List<CSSResource> cssResources) {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        final ParserFactory parserFactory = new ParserFactory();
        try {
            final Parser parser = parserFactory.makeParser();
            final CSSDocumentHandler documentHandler = getDocumentHandler(parser, checkCode);
            for (CSSResource cssResource : cssResources) {
                cssProblems.addAll(documentHandler.evaluate(cssResource));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return cssProblems;
    }

    private static CSSDocumentHandler getDocumentHandler(Parser parser, CheckCode checkCode) throws InstantiationException {
        switch (checkCode.getFunctionId()) {
            case CheckFunctionConstants.FUNCTION_CSS_GENERATED_CONTENT:
                return new CSSGeneratedContentDocumentHandler(parser, checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_COLOR_CONTRAST:
                return new CSSColorContrastDocumentHandler(parser, checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_BLINK:
                return new CSSBlinkDocumentHandler(parser, checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_PARSEABLE:
                return new CSSParseableDocumentHandler(parser, checkCode);
            case CheckFunctionConstants.FUNCTION_CSS_OUTLINE:
                return new CSSOutlineDocumentHandler(parser, checkCode);
            default:
                Logger.putLog("Warning: unknown function id: " + checkCode.getFunctionId(), CSSUtils.class, Logger.LOG_LEVEL_WARNING);
                throw new InstantiationException("Error: unknown CSS function id: " + checkCode.getFunctionId());
        }
    }

    public static Problem getProblemFromCSS(final CSSProblem cssProblem, final Check check, final Evaluation evaluation) {
        PropertiesManager properties = new PropertiesManager();
        SimpleDateFormat format = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));

        final Problem problem = new Problem();
        problem.setDate(format.format(cssProblem.getDate()));
        problem.setCheck(check);
        problem.setLineNumber(cssProblem.getLineNumber());
        problem.setColumnNumber(cssProblem.getColumnNumber());

        final Element problemTextNode = evaluation.getHtmlDoc().createElement("problem-text");
        problemTextNode.setTextContent(cssProblem.getTextContent());

        problem.setNode(problemTextNode);
        problem.setSummary(false);

        evaluation.addProblem(problem);

        return problem;
    }

}