package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.writer.CSSWriterSettings;
import es.ctic.css.OAWCSSVisitor;
import es.ctic.css.utils.CSSSACUtils;

import javax.annotation.Nonnull;

/**
 * Clase para comprobar si mediante CSS se elimina la marca al elemento que contiene el foco
 */
public class CSSOutlineDocumentHandler extends OAWCSSVisitor {

    public CSSOutlineDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
        if (isFocusPseudoClass() && isValidMedia()) {
            if ("outline".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if (isZero(value)) {
                    getProblems().add(createCSSProblem("", cssDeclaration));
                }
            } else if ("outline-width".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if (isZero(value)) {
                    getProblems().add(createCSSProblem("", cssDeclaration));
                }
            } else if ("outline-style".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if ("none".equals(value)) {
                    getProblems().add(createCSSProblem("", cssDeclaration));
                }
            }
        }
    }

    private boolean isZero(final String value) {
        return value != null && (value.startsWith("0") || value.contains("none"));
    }

    private boolean isFocusPseudoClass() {
        final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
        return selector.contains(":focus");
    }

}
