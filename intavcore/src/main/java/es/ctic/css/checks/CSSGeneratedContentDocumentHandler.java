package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import es.ctic.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;
import org.w3c.css.sac.*;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class CSSGeneratedContentDocumentHandler extends CSSDocumentHandler {

    private static final String CONTENT_PROPERTY = "content";

    public CSSGeneratedContentDocumentHandler(final Parser parser, final CheckCode checkCode) {
        super(parser,checkCode);
    }

    @Override
    public List<CSSProblem> evaluate(final CSSResource cssResource) {
        final InputSource is = new InputSource();
        is.setCharacterStream(new java.io.StringReader(cssResource.getContent()));
        try {
            // La generación de contenido desde CSS no se puede hacer con estilos en linea ya que requiere las pseudo clases
            // :before y :after
            if ( !cssResource.isInline()) {
                parser.parseStyleSheet(is);
            }
        } catch (IOException e) {
            Logger.putLog("Error al parsear código CSS", CSSDocumentHandler.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return getProblems();
    }

    @Override
    public void property(final String name, final LexicalUnit lexicalUnit, boolean important) throws CSSException {
        if (isPseudoClass() && CONTENT_PROPERTY.equals(name)) {
            final String value = CSSSACUtils.parseLexicalValue(lexicalUnit);
            final int allowedChars = Integer.parseInt(getCheckCode().getFunctionNumber());
            if (value.length()>allowedChars) {
                System.out.println("GeneratedContentCSS DH: " + name +"="+ value);
                getProblems().add(createCSSProblem(name+": "+value));
            }
        }
    }

    private boolean isPseudoClass() {
        return selector.contains(":before") || selector.contains(":after");
    }

}
