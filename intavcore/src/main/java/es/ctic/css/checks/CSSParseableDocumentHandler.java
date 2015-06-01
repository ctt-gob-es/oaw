package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CSSParseError;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.OAWCSSVisitor;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.List;

/**
 * Clase para comprobar si un código CSS es parseable o no (se ajusta a la gramática CSS)
 */
public class CSSParseableDocumentHandler extends OAWCSSVisitor {

    public CSSParseableDocumentHandler(CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(final Node node, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty()) {
            resource = cssResource;
            final CollectingCSSParseErrorHandler errorHandler = new CollectingCSSParseErrorHandler();
            CSSReader.setDefaultParseErrorHandler(errorHandler);
            final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30);
            CSSVisitor.visitCSS(aCSS, this);
            for (CSSParseError cssParseError : errorHandler.getAllParseErrors()) {
                getProblems().add(createCSSParserError(cssParseError));
            }

        }
        return problems;
    }

    private CSSProblem createCSSParserError(final CSSParseError cssParseError) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        if ( cssParseError.getFirstSkippedToken()!=null ) {
            cssProblem.setLineNumber(cssParseError.getFirstSkippedToken().getBeginLine());
            cssProblem.setColumnNumber(cssParseError.getFirstSkippedToken().getBeginColumn());
        }

        cssProblem.setSelector("");
        cssProblem.setTextContent(cssParseError.getErrorMessage());

        return cssProblem;
    }
}
