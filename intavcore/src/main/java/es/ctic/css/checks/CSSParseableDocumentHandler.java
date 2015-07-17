package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CSSParseError;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.OAWCSSVisitor;
import es.inteco.common.logging.Logger;
import org.dom4j.Document;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.List;

/**
 * Clase para comprobar si un código CSS es parseable o no (se ajusta a la gramática CSS)
 */
public class CSSParseableDocumentHandler extends OAWCSSVisitor {

    public CSSParseableDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(final Document document, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty()) {
            try {
                resource = cssResource;
                final CollectingCSSParseErrorHandler errorHandler = new CollectingCSSParseErrorHandler();
                final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, errorHandler);
                if (aCSS != null) {
                    CSSVisitor.visitCSS(aCSS, this);
                    for (CSSParseError cssParseError : errorHandler.getAllParseErrors()) {
                        getProblems().add(createCSSParserError(cssParseError));
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Error al intentar parsear el CSS", CSSParseableDocumentHandler.class, Logger.LOG_LEVEL_INFO);
            }
        }
        return problems;
    }

    @Override
    public void end() {
        cleanupResources();
    }

    private CSSProblem createCSSParserError(final CSSParseError cssParseError) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        if ( cssParseError.getFirstSkippedToken()!=null ) {
            cssProblem.setLineNumber(cssParseError.getFirstSkippedToken().getBeginLine());
            cssProblem.setColumnNumber(cssParseError.getFirstSkippedToken().getBeginColumn());
        }

        if ( currentStyleRule!=null ) {
            cssProblem.setSelector(currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
        } else {
            cssProblem.setSelector("");
        }
        if ( cssParseError.getLastValidToken()!=null && cssParseError.getLastValidToken().getImage()!=null && !cssParseError.getLastValidToken().getImage().trim().isEmpty()) {
            cssProblem.setTextContent(resource.getStringSource() +System.lineSeparator() + "Encontrado error de parseo en: " + cssParseError.getLastValidToken().getImage());
        } else {
            cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + cssParseError.getErrorMessage());
        }

        return cssProblem;
    }
}
