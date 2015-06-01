package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.helger.css.decl.CSSDeclaration;
import es.ctic.css.OAWCSSVisitor;
import es.ctic.css.utils.CSSSACUtils;

import javax.annotation.Nonnull;

/**
 * Clase para detectar si se produccen parpadeos generados desde CSS mediante text-decoration:blink o text-decoration-line:blink
 */
public class CSSBlinkDocumentHandler extends OAWCSSVisitor {

    public CSSBlinkDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
        if (isValidMedia()) {
            if ("text-decoration".equals(cssDeclaration.getProperty())
                    || "text-decoration-line".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if ("blink".equalsIgnoreCase(value)) {
                    getProblems().add(createCSSProblem("", cssDeclaration));
                }
            }
        }
    }

}
