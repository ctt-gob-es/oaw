package es.ctic.css;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import com.steadystate.css.parser.SACParserCSS3;
import es.ctic.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.w3c.css.sac.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public abstract class CSSDocumentHandler implements DocumentHandler {

    private final List<CSSProblem> problems = new ArrayList<CSSProblem>();
    protected final Parser parser;
    protected final CheckCode checkCode;
    protected String selector;

    public CSSDocumentHandler(final Parser parser, final CheckCode checkCode) {
        this.parser = parser;
        this.checkCode = checkCode;
        this.parser.setDocumentHandler(this);
        // Inicializamos a un ErrorHandler vacío porque por defecto imprime los errores por System.err
        this.parser.setErrorHandler(new ErrorHandler() {
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
    }

    public List<CSSProblem> evaluate(final CSSResource cssResource) {
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
        cssProblem.setLineNumber(getLineNumber(parser));
        cssProblem.setColumnNumber(getColumnNumber(parser));
        cssProblem.setSelector(selector);
        cssProblem.setTextContent(textContent);


        //Element problemTextNode = evaluation.getHtmlDoc().createElement("problem-text");
        //problem.setNode(problemTextNode);
        //problem.setSummary(cssValidationError.isSummary());
        return cssProblem;
    }

    private int getLineNumber(Parser parser) {
        if (parser instanceof SACParserCSS3) {
            final SACParserCSS3 sacParserCSS3 = (SACParserCSS3) parser;
            return sacParserCSS3.token.beginLine;
        } else {
            return -1;
        }
    }

    private int getColumnNumber(final Parser parser) {
        if (parser instanceof SACParserCSS3) {
            final SACParserCSS3 sacParserCSS3 = (SACParserCSS3) parser;
            return sacParserCSS3.token.beginColumn;
        } else {
            return -1;
        }
    }

}