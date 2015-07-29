package es.ctic.css.checks;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.writer.CSSWriterSettings;
import es.ctic.css.OAWCSSVisitor;
import es.ctic.css.utils.CSSSACUtils;
import org.w3c.css.sac.LexicalUnit;

import javax.annotation.Nonnull;

/**
 * Clase para comprobar si mediante CSS se elimina la marca al elemento que contiene el foco
 */
public class CSSOutlineDocumentHandler extends OAWCSSVisitor {

    private boolean focusHidden;
    private boolean borderVisible;
    private boolean backgroundVisible;

    private CSSDeclaration outlineDeclaration;

    // FIXME: Comprobar border y background-color
    @Override
    public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
        if (isFocusPseudoClass() && isValidMedia()) {
            if ("outline".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if (isZero(value)) {
                    focusHidden = true;
                    outlineDeclaration = cssDeclaration;
                }
            } else if ("outline-width".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if (isZero(value)) {
                    focusHidden = true;
                    outlineDeclaration = cssDeclaration;
                }
            } else if ("outline-style".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                if ("none".equals(value)) {
                    focusHidden = true;
                    outlineDeclaration = cssDeclaration;
                }
            } else if ("border".equals(cssDeclaration.getProperty())) {
                final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
                borderVisible = !isZero(value);
            } else if ("background-color".equals(cssDeclaration.getProperty())) {
                backgroundVisible = true;
            }
        }
    }

    @Override
    public void onEndStyleRule(@Nonnull CSSStyleRule cssStyleRule) {
        if ( focusHidden && !borderVisible && !backgroundVisible) {
            getProblems().add(createCSSProblem("", outlineDeclaration));
        }
        focusHidden = false;
        borderVisible = false;
        backgroundVisible = false;
        outlineDeclaration = null;
    }

    private boolean isZero(final String value) {
        return value != null && (value.startsWith("0") || value.contains("none"));
    }

    private boolean isFocusPseudoClass() {
        final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
        return selector.contains(":focus");
    }

}
