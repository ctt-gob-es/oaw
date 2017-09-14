package es.gob.oaw.css.checks;

import com.helger.css.decl.CSSDeclaration;

import es.gob.oaw.css.OAWCSSVisitor;
import es.gob.oaw.css.utils.CSSSACUtils;

import javax.annotation.Nonnull;

/**
 * Clase para detectar si se produccen parpadeos generados desde CSS mediante text-decoration:blink o text-decoration-line:blink
 */
public class CSSBlinkDocumentHandler extends OAWCSSVisitor {

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
