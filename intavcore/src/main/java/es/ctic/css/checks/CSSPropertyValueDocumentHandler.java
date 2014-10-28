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
public class CSSPropertyValueDocumentHandler extends CSSDocumentHandler {

    private final String propertyName;

    public CSSPropertyValueDocumentHandler(final Parser parser, final CheckCode checkCode) {
        super(parser, checkCode);
        this.propertyName = checkCode.getFunctionValue().toLowerCase();
    }

    @Override
    public void property(final String name, final LexicalUnit lexicalUnit, boolean important) throws CSSException {
        if (name.equals(propertyName)) {
            final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
            System.out.println("CSSPropertyValueDocumentHandler: " + name +"="+ value);
        }
    }
}
