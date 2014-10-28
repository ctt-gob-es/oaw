package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.utils.CSSSACUtils;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;

/**
 *
 */
public class CSSBlinkDocumentHandler extends CSSDocumentHandler {

    public CSSBlinkDocumentHandler(final Parser parser, final CheckCode checkCode) {
        super(parser, checkCode);
    }

    @Override
    public void property(final String name, final LexicalUnit lexicalUnit, boolean important) throws CSSException {
        if ("text-decoration".equals(name) || "text-decoration-line".equals(name)) {
            final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
            if ("blink".equalsIgnoreCase(value)) {
                getProblems().add(createCSSProblem(name+": "+value));
            }
        }
    }

}
