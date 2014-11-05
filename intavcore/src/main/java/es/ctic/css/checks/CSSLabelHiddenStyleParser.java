package es.ctic.css.checks;

import cz.vutbr.web.css.*;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.StyleMap;
import es.ctic.css.CSSAnalyzer;
import es.ctic.css.CSSProblem;
import es.ctic.css.CSSResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class CSSLabelHiddenStyleParser implements CSSAnalyzer {

    @Override
    public List<CSSProblem> evaluate(final Node node, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        final Document document = node.getOwnerDocument();
        final StyleSheet styleSheet;
        try {
            styleSheet = CSSFactory.getUsedStyles(document, "utf-8", new URL("http://www2.fundacionctic.org"), new MediaSpecAll());
            final Analyzer analyzer = new Analyzer(styleSheet);
            final StyleMap styleMap = analyzer.evaluateDOM(document, new MediaSpecAll(), true);
            final NodeList labels = document.getElementsByTagName("label");
            for (int i = 0; i < labels.getLength(); i++) {
                // TODO: Check que el label está asociado
                // TODO: Check que el componente asociado no tiene title o aria-label
                final Element labelElement = (Element) labels.item(i);
                final NodeData nodeData = styleMap.get(labelElement);
                for (String propertyName : nodeData.getPropertyNames()) {
                    final CSSProperty cssProperty = nodeData.getProperty(propertyName);
                    final Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);
                    if (cssProperty == CSSProperty.Left.length) {
                        TermLength value = nodeData.getValue(TermLength.class, propertyName, true);
                        if ( value.getValue()<-3000 ) {
                            cssProblems.add(createCSSProblem(labelElement, declaration));
                        }
                    } else if (cssProperty == CSSProperty.Display.NONE) {
                        cssProblems.add(createCSSProblem(labelElement, declaration));
                    } else if (cssProperty == CSSProperty.Visibility.HIDDEN) {
                        cssProblems.add(createCSSProblem(labelElement, declaration));
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return cssProblems;
    }

    protected CSSProblem createCSSProblem(final Element element, final Declaration declaration) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        cssProblem.setLineNumber(declaration.getSource().getLine());
        cssProblem.setColumnNumber(declaration.getSource().getPosition());
        cssProblem.setSelector("");
        cssProblem.setElement(element);

        return cssProblem;
    }

}
