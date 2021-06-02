package es.gob.oaw.css;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.*;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;

import es.gob.oaw.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;
import org.dom4j.Document;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

/**
 * Clase básica para la comprobacion de problemas en recursos CSS.
 * Utiliza un patrón Visitor para el que define en comportamiento común para todas las comprobaciones de CSS.
 */
public class OAWCSSVisitor extends DefaultCSSVisitor implements CSSAnalyzer {

    /** The problems. */
    protected final List<CSSProblem> problems = new ArrayList<>();
    
    /** The current media. */
    private final Stack<Boolean> currentMedia = new Stack<>();
    
    /** The current style rule. */
    protected CSSStyleRule currentStyleRule;
    
    /** The resource. */
    protected CSSResource resource;
    
    /** The document. */
    private Document document;

    /**
	 * Instantiates a new OAWCSS visitor.
	 */
    public OAWCSSVisitor() {
        super();
        currentMedia.push(true);
    }

    /**
	 * Evaluate.
	 *
	 * @param document     the document
	 * @param cssResources the css resources
	 * @return the list
	 */
    public List<CSSProblem> evaluate(final Document document, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<>();
        for (CSSResource cssResource : cssResources) {
            cssProblems.addAll(evaluate(document, cssResource));
        }

        return cssProblems;
    }

    /**
	 * Evaluate.
	 *
	 * @param document    the document
	 * @param cssResource the css resource
	 * @return the list
	 */
    public List<CSSProblem> evaluate(final Document document, final CSSResource cssResource) {
        this.document = document;
        if (!cssResource.getContent().isEmpty()) {
            try {
                resource = cssResource;
                final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, new CollectingCSSParseErrorHandler());
                if (aCSS != null) {
                    CSSVisitor.visitCSS(aCSS, this);
                }
            } catch (Exception e) {
                Logger.putLog("Error al intentar parsear el CSS", OAWCSSVisitor.class, Logger.LOG_LEVEL_INFO, e);
            }
        }
        return problems;
    }

    /**
	 * On begin style rule.
	 *
	 * @param cssStyleRule the css style rule
	 */
    @Override
    public void onBeginStyleRule(@Nonnull final CSSStyleRule cssStyleRule) {
        currentStyleRule = cssStyleRule;
    }

    /**
	 * On begin media rule.
	 *
	 * @param cssMediaRule the css media rule
	 */
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

    /**
	 * On end media rule.
	 *
	 * @param cssMediaRule the css media rule
	 */
    @Override
    public void onEndMediaRule(@Nonnull final CSSMediaRule cssMediaRule) {
        currentMedia.pop();
    }

    /**
	 * End.
	 */
    @Override
    public void end() {
        filterCSSProblems();
        cleanupResources();
        super.end();
    }

    /**
	 * Cleanup resources.
	 */
    protected void cleanupResources() {
        // Aseguramos que se liberan las referencias a objetos manejados por otras librerias
        currentStyleRule = null;
        document = null;
        currentMedia.clear();
    }


    /**
	 * Gets the problems.
	 *
	 * @return the problems
	 */
    public List<CSSProblem> getProblems() {
        return problems;
    }

    /**
	 * Checks if is valid media.
	 *
	 * @return true, if is valid media
	 */
    protected boolean isValidMedia() {
        return currentMedia.empty() || currentMedia.peek();
    }

    /**
	 * Creates the CSS problem.
	 *
	 * @param textContent    the text content
	 * @param cssDeclaration the css declaration
	 * @return the CSS problem
	 */
    protected CSSProblem createCSSProblem(final String textContent, final CSSDeclaration cssDeclaration) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        if (cssDeclaration != null && cssDeclaration.getSourceLocation() != null) {
            cssProblem.setLineNumber(cssDeclaration.getSourceLocation().getFirstTokenBeginLineNumber());
            cssProblem.setColumnNumber(cssDeclaration.getSourceLocation().getFirstTokenBeginColumnNumber());
            if (resource.getStringSource().isEmpty()) {
                cssProblem.setTextContent(textContent + cssDeclaration.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
            } else {
                cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + textContent + cssDeclaration.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
            }
            if (currentStyleRule != null) {
                cssProblem.setSelector(currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
            }
        } else if (currentStyleRule != null && currentStyleRule.getSourceLocation() != null) {
            cssProblem.setSelector(currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
            cssProblem.setLineNumber(currentStyleRule.getSourceLocation().getFirstTokenBeginLineNumber());
            cssProblem.setColumnNumber(currentStyleRule.getSourceLocation().getFirstTokenBeginColumnNumber());
            if (resource.getStringSource().isEmpty()) {
                cssProblem.setTextContent(textContent + cssProblem.getSelector());
            } else {
                cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + textContent + cssProblem.getSelector());
            }
        }

        return cssProblem;
    }

    /**
	 * Gets the value.
	 *
	 * @param cssDeclaration the css declaration
	 * @return the value
	 */
    protected LexicalUnit getValue(final CSSDeclaration cssDeclaration) {
        try {
            final Parser parser = CSSSACUtils.getSACParser();
            final InputSource is = new InputSource();
            is.setCharacterStream(new java.io.StringReader(cssDeclaration.getExpression().getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0)));
            return parser.parsePropertyValue(is);
        } catch (Exception e) {
            Logger.putLog("Fallo an intentar parsear el valor de una propiedad", OAWCSSVisitor.class, Logger.LOG_LEVEL_WARNING, e);
        }
        return null;
    }

    /**
	 * Método para eliminar aquellas incidencias de CSS que no se aplican sobre la página (selectores que no se encuentran en la página).
	 *
	 * @return lista con únicamente los problemas de CSS que se aplican en esta página
	 */
    private List<CSSProblem> filterCSSProblems() {
        if (document != null) {
            final ListIterator<CSSProblem> iterator = problems.listIterator();
            try {
                final Parser cssParser = CSSSACUtils.getSACParser();
                while (iterator.hasNext()) {
                    final CSSProblem cssProblem = iterator.next();
                    if (!isSelectorUsed(cssParser, cssProblem.getSelector())) {
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Error al intentar filtrar los problemas de CSS", OAWCSSVisitor.class, Logger.LOG_LEVEL_WARNING);
            }
        }

        return problems;
    }

    /**
	 * Checks if is selector used.
	 *
	 * @param cssParser the css parser
	 * @param selectors the selectors
	 * @return true, if is selector used
	 */
    private boolean isSelectorUsed(final Parser cssParser, final String selectors) {
        if (!selectors.isEmpty()) {
            final InputSource is = new InputSource();
            is.setCharacterStream(new java.io.StringReader(selectors));
            boolean isSelectorUsed = false;
            try {
                final SelectorList selectorList = cssParser.parseSelectors(is);
                for (int i = 0; i < selectorList.getLength(); i++) {
                    final String xpath = CSSSACUtils.getXPATHFromSelector(selectorList.item(i));
                    try {
                        final List<?> nodes = document.selectNodes(xpath);
                        isSelectorUsed |= !nodes.isEmpty();
                    } catch (Exception e) {
                        Logger.putLog("Error al aplicar la expresión XPATH en el filtrado", OAWCSSVisitor.class, Logger.LOG_LEVEL_WARNING, e);
                    }
                }
            } catch (IOException e) {
                Logger.putLog("Error al comprobar si un selector de CSS se usa", OAWCSSVisitor.class, Logger.LOG_LEVEL_WARNING, e);
            }
            return isSelectorUsed;
        }
        return true;
    }

}
