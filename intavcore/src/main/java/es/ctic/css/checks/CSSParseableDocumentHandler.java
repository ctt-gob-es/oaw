package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.Parser;

import java.util.List;

/**
 *
 */
public class CSSParseableDocumentHandler extends CSSDocumentHandler implements ErrorHandler {

    public CSSParseableDocumentHandler(Parser parser, CheckCode checkCode) {
        super(parser, checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(CSSResource cssResource) {
        parser.setErrorHandler(this);
        return super.evaluate(cssResource);
    }

    @Override
    public void error(CSSParseException exception) throws CSSException {
        getProblems().add(createCSSProblem(exception.getMessage()));
    }

    @Override
    public void fatalError(CSSParseException exception) throws CSSException {
        getProblems().add(createCSSProblem(exception.getMessage()));
    }

    @Override
    public void warning(CSSParseException exception) throws CSSException {
    }

}
