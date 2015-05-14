package es.ctic.css;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import com.steadystate.css.parser.SACParserCSS3;
import es.ctic.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;
import org.w3c.css.sac.*;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public abstract class CSSDocumentHandler implements DocumentHandler, CSSAnalyzer {

    static {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");
    }

    private final List<CSSProblem> problems = new ArrayList<CSSProblem>();
    protected Parser parser;
    protected CSSResource resource;
    protected final CheckCode checkCode;
    protected String selector;

    public CSSDocumentHandler(final CheckCode checkCode) {
        this.checkCode = checkCode;
        final ParserFactory parserFactory = new ParserFactory();
        try {
            parser = parserFactory.makeParser();
            parser.setDocumentHandler(this);
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
        } catch (Exception e) {
            Logger.putLog("Fallo an intentar parser documento CSS", CSSDocumentHandler.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public List<CSSProblem> evaluate(final Node node, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        for (CSSResource cssResource : cssResources) {
            cssProblems.addAll(evaluate(node, cssResource));
        }

        return cssProblems;
    }

    public List<CSSProblem> evaluate(final Node node, final CSSResource cssResource) {
        if ( !cssResource.getContent().isEmpty() ) {
            resource = cssResource;
            final InputSource is = new InputSource();
            is.setCharacterStream(new java.io.StringReader(cssResource.getContent()));
            // Si en vez un String es el enlace a la hoja de estilo usar
            //is.setURI("http://www.fundacionctic.org/sites/all/themes/ctic/css/html-reset.css");
            try {
                if (cssResource.isInline()) {
                    parser.parseStyleDeclaration(is);
                } else {
                    parser.parseStyleSheet(is);
                }
            } catch (IOException e) {
                Logger.putLog("Error al parsear código CSS", CSSDocumentHandler.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return problems;
    }

    // TODO: Implementar la lógica para los casos comunes (at-rules, imports, medias,...)
    @Override
    public void startDocument(InputSource source) throws CSSException {
    }

    @Override
    public void endDocument(InputSource source) throws CSSException {
    }

    @Override
    public void comment(String text) throws CSSException {
    }

    @Override
    public void ignorableAtRule(String atRule) throws CSSException {
    }

    @Override
    public void namespaceDeclaration(String prefix, String uri) throws CSSException {
    }

    @Override
    public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
    }

    @Override
    public void startMedia(SACMediaList media) throws CSSException {
    }

    @Override
    public void endMedia(SACMediaList media) throws CSSException {
    }

    @Override
    public void startPage(String name, String pseudoPage) throws CSSException {
    }

    @Override
    public void endPage(String name, String pseudoPage) throws CSSException {
    }

    @Override
    public void startFontFace() throws CSSException {
    }

    @Override
    public void endFontFace() throws CSSException {
    }

    @Override
    public void startSelector(SelectorList selectors) throws CSSException {
        selector = CSSSACUtils.buildSelector(selectors);
    }

    @Override
    public void endSelector(SelectorList selectors) throws CSSException {
        selector = null;
    }

    @Override
    public void property(String name, LexicalUnit value, boolean important) throws CSSException {
    }

    public List<CSSProblem> getProblems() {
        return problems;
    }

    public CheckCode getCheckCode() {
        return checkCode;
    }

    protected CSSProblem createCSSProblem(final String textContent) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        cssProblem.setLineNumber(getLineNumber());
        cssProblem.setColumnNumber(getColumnNumber());
        cssProblem.setSelector(selector);
        if ( resource!=null && !resource.isInline() && resource.getHTMLElement()!=null ) {
//            cssProblem.setElement(resource.getHTMLElement());
        }
        cssProblem.setTextContent((resource.getStringSource().isEmpty()?"":resource.getStringSource()+System.lineSeparator()) + textContent);

        return cssProblem;
    }

    private int getLineNumber() {
        if (parser instanceof SACParserCSS3) {
            return ((SACParserCSS3) parser).token.beginLine;
        } else {
            return -1;
        }
    }

    private int getColumnNumber() {
        if (parser instanceof SACParserCSS3) {
            return ((SACParserCSS3) parser).token.beginColumn;
        } else {
            return -1;
        }
    }

}