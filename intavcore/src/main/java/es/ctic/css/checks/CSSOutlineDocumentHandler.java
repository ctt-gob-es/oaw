package es.ctic.css.checks;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.writer.CSSWriterSettings;
import es.ctic.css.OAWCSSVisitor;
import es.ctic.css.utils.CSSSACUtils;

import javax.annotation.Nonnull;

/**
 * Clase para comprobar si mediante CSS se elimina la marca al elemento que contiene el foco y no se proporciona la información mediante técnicas alternativas.
 */
public class CSSOutlineDocumentHandler extends OAWCSSVisitor {

    private boolean focusHidden;
    private boolean alternativeOutlineMethod;

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
            } else if (isAlternativeOutlineMethod(cssDeclaration.getProperty(), CSSSACUtils.parseLexicalValue(getValue(cssDeclaration)))) {
                alternativeOutlineMethod = true;
            }
        }
    }

    private boolean isAlternativeOutlineMethod(final String cssProperty, final String cssValue) {
        if ("border".equals(cssProperty)) {
            return !isZero(cssValue);
        } else if ("background-color".equals(cssProperty)) {
            return true;
        } else if ("box-shadow".equals(cssProperty)) {
            return true;
        } else if ("color".equals(cssProperty)) {
            return true;
        } else if ("text-decoration".equals(cssProperty)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEndStyleRule(@Nonnull CSSStyleRule cssStyleRule) {
        if (focusHidden && !alternativeOutlineMethod) {
            getProblems().add(createCSSProblem("", outlineDeclaration));
        }
        focusHidden = false;
        alternativeOutlineMethod = false;
        outlineDeclaration = null;
    }

    private boolean isZero(final String value) {
        return value != null && (value.equals("0") || value.contains("none"));
    }

    private boolean isFocusPseudoClass() {
        if (currentStyleRule != null) {
            final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
            return selector.contains(":focus");
        } else {
            return false;
        }
    }

}
