package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
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
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Clase que comprueba si se genera contenido de cierta longitud mediante las pseudo clases :before o :after
 */
public class CSSGeneratedContentDocumentHandler extends OAWCSSVisitor {

    private static final String CONTENT_PROPERTY = "content";

    public CSSGeneratedContentDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(final Node node, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty() && !cssResource.isInline()) {
            resource = cssResource;
            CSSReader.setDefaultParseErrorHandler(new CollectingCSSParseErrorHandler());
            final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30);
            CSSVisitor.visitCSS(aCSS, this);
        }
        return problems;
    }

    @Override
    public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
        if (isValidMedia() && isPseudoClass() && CONTENT_PROPERTY.equals(cssDeclaration.getProperty())) {
            final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
            final int allowedChars = Integer.parseInt(getCheckCode().getFunctionNumber());
            if (value.length() > allowedChars) {
                getProblems().add(createCSSProblem("", cssDeclaration));
            }
        }
    }

    private boolean isPseudoClass() {
        final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
        return selector.contains(":before") || selector.contains(":after");
    }

}
