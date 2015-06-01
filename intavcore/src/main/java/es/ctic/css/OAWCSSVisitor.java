package es.ctic.css;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.*;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;
import es.inteco.common.logging.Logger;
import org.w3c.css.sac.*;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Clase básica para la comprobacion de problemas en recursos CSS.
 * Utiliza un patrón Visitor para el que define en comportamiento común para todas las comprobaciones de CSS.
 */
public class OAWCSSVisitor extends DefaultCSSVisitor implements CSSAnalyzer {

    static {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");
    }

    private final Stack<Boolean> currentMedia = new Stack<Boolean>();
    private final CheckCode checkCode;
    protected CSSStyleRule currentStyleRule;
    protected final List<CSSProblem> problems = new ArrayList<CSSProblem>();
    protected CSSResource resource;

    public OAWCSSVisitor(final CheckCode checkCode) {
        super();
        this.checkCode = checkCode;
        currentMedia.push(true);
    }

    public List<CSSProblem> evaluate(final Node node, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        for (CSSResource cssResource : cssResources) {
            cssProblems.addAll(evaluate(node, cssResource));
        }

        return cssProblems;
    }

    public List<CSSProblem> evaluate(final Node node, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty()) {
            resource = cssResource;
            CSSReader.setDefaultParseErrorHandler(new CollectingCSSParseErrorHandler());
            final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30);
            if ( aCSS!=null ) {
                CSSVisitor.visitCSS(aCSS, this);
            }
        }
        return problems;
    }

    @Override
    public void onBeginStyleRule(@Nonnull final CSSStyleRule cssStyleRule) {
        currentStyleRule = cssStyleRule;
    }

    @Override
    public void onBeginMediaRule(@Nonnull final CSSMediaRule cssMediaRule) {
        boolean media = false;
        for (CSSMediaQuery mediaQuery : cssMediaRule.getAllMediaQueries()) {
            if ("screen".equals(mediaQuery.getMedium()) || "all".equals(mediaQuery.getMedium()) || mediaQuery.getMedium() == null) {
                media = true;
            }
        }
        currentMedia.push(media);
    }

    @Override
    public void onEndMediaRule(@Nonnull final CSSMediaRule cssMediaRule) {
        currentMedia.pop();
    }

    public List<CSSProblem> getProblems() {
        return problems;
    }

    public CheckCode getCheckCode() {
        return checkCode;
    }

    protected boolean isValidMedia() {
        return currentMedia.peek();
    }

    protected CSSProblem createCSSProblem(final String textContent, final CSSDeclaration cssDeclaration) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        if (cssDeclaration != null && cssDeclaration.getSourceLocation() != null) {
            cssProblem.setLineNumber(cssDeclaration.getSourceLocation().getFirstTokenBeginLineNumber());
            cssProblem.setColumnNumber(cssDeclaration.getSourceLocation().getFirstTokenBeginColumnNumber());
        }
        cssProblem.setSelector(currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
        if (resource.getStringSource().isEmpty()) {
            cssProblem.setTextContent(textContent);
        } else {
            cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + textContent);
        }

        return cssProblem;
    }

    protected LexicalUnit getValue(final CSSDeclaration cssDeclaration) {
        try {
            final ParserFactory parserFactory = new ParserFactory();
            final Parser parser = parserFactory.makeParser();
            // Inicializamos a un ErrorHandler vacío porque por defecto imprime los errores por System.err
            parser.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(CSSParseException exception) throws CSSException {
                }

                @Override
                public void error(CSSParseException exception) throws CSSException {
                }

                @Override
                public void fatalError(CSSParseException exception) throws CSSException {
                }
            });
            final InputSource is = new InputSource();
            is.setCharacterStream(new java.io.StringReader(cssDeclaration.getExpression().getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0)));
            return parser.parsePropertyValue(is);
        } catch (Exception e) {
            Logger.putLog("Fallo an intentar parsear el valor de una propiedad", OAWCSSVisitor.class, Logger.LOG_LEVEL_WARNING, e);
        }
        return null;
    }

}
