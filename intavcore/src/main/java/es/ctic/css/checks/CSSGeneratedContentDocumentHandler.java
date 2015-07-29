package es.ctic.css.checks;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.OAWCSSVisitor;
import es.ctic.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;
import org.dom4j.Document;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Clase que comprueba si se genera contenido de cierta longitud mediante las pseudo clases :before o :after
 */
public class CSSGeneratedContentDocumentHandler extends OAWCSSVisitor {

    private static final String CONTENT_PROPERTY = "content";

    private final int allowedChars;

    public CSSGeneratedContentDocumentHandler(final int allowedChars) {
        this.allowedChars = allowedChars;
    }

    @Override
    public List<CSSProblem> evaluate(final Document document, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty() && !cssResource.isInline()) {
            try {
                resource = cssResource;
                final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, new CollectingCSSParseErrorHandler());
                if (aCSS != null) {
                    CSSVisitor.visitCSS(aCSS, this);
                }
            } catch (Exception e) {
                Logger.putLog("Error al intentar parsear el CSS", OAWCSSVisitor.class, Logger.LOG_LEVEL_INFO);
            }
        }
        return problems;
    }

    @Override
    public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
        if (isValidMedia() && isPseudoClass() && CONTENT_PROPERTY.equals(cssDeclaration.getProperty())) {
            final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
            if (!"none".equalsIgnoreCase(value) && !value.trim().isEmpty()) {
                if (value.length() > allowedChars) {
                    getProblems().add(createCSSProblem("", cssDeclaration));
                }
            }
        }
    }

    private boolean isPseudoClass() {
        final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
        return selector.contains(":before") || selector.contains(":after");
    }

}
