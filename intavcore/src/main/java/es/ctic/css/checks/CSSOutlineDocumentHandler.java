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
public class CSSOutlineDocumentHandler extends CSSDocumentHandler {

    public CSSOutlineDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public void property(final String name, final LexicalUnit lexicalUnit, boolean important) throws CSSException {
        if (isFocusPseudoClass()) {
            if ( "outline".equals(name)) {
                final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
                if ( isZero(value)) {
                    getProblems().add(createCSSProblem(name+": "+value));
                }
            } else if ("outline-width".equals(name) ) {
                final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
                if ( isZero(value) ) {
                    getProblems().add(createCSSProblem(name+": "+value));
                }
            } else if ("outline-style".equals(name) ) {
                final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
               if ( "none".equals(value)) {
                   getProblems().add(createCSSProblem(name+": "+value));
               }
            }
        }
    }

    private boolean isZero(String value) {
        return value != null && (value.startsWith("0") || value.contains("none"));
    }

    private boolean isFocusPseudoClass() {
        return selector!=null && selector.contains(":focus");
    }

}
