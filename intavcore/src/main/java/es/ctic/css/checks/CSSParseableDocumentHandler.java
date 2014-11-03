package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.dom.Node;

import java.util.List;

/**
 *
 */
public class CSSParseableDocumentHandler extends CSSDocumentHandler implements ErrorHandler {

    public CSSParseableDocumentHandler(CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(final Node node, final CSSResource cssResource) {
        parser.setErrorHandler(this);
        return super.evaluate(node, cssResource);
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
