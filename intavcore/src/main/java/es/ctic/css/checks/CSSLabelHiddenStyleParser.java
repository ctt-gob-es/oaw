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
 * Clase para comprobar si la etiqueta (elemento label) de un control de formulario está oculta mediante CSS y es el único elemento que proporciona el "name" del control (no dispone de title ni aria-label)
 */
public class CSSLabelHiddenStyleParser implements CSSAnalyzer {

    @Override
    public List<CSSProblem> evaluate(final Node node, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        final Document document = node.getOwnerDocument();
        final Element html = document.getDocumentElement();
        try {
            final StyleSheet styleSheet = CSSFactory.getUsedStyles(document, "utf-8", new URL(String.valueOf(html.getUserData("url"))), new MediaSpecAll());
            final Analyzer analyzer = new Analyzer(styleSheet);
            final StyleMap styleMap = analyzer.evaluateDOM(document, new MediaSpecAll(), true);
            final NodeList labels = document.getElementsByTagName("label");
            for (int i = 0; i < labels.getLength(); i++) {
                final Element labelElement = (Element) labels.item(i);
                if (isUniqueLabel(document, labelElement.getAttribute("for"))) {
                    final NodeData nodeData = styleMap.get(labelElement);
                    for (String propertyName : nodeData.getPropertyNames()) {
                        final CSSProperty cssProperty = nodeData.getProperty(propertyName);
                        final Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);
                        if (cssProperty == CSSProperty.Left.length) {
                            TermLength value = nodeData.getValue(TermLength.class, propertyName, true);
                            if (value.getValue() < -3000) {
                                cssProblems.add(createCSSProblem(labelElement, declaration));
                            }
                        } else if (cssProperty == CSSProperty.Display.NONE) {
                            cssProblems.add(createCSSProblem(labelElement, declaration));
                        } else if (cssProperty == CSSProperty.Visibility.HIDDEN) {
                            cssProblems.add(createCSSProblem(labelElement, declaration));
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
        }

        return cssProblems;
    }

    /**
     * Método que comprueba si este elemento label es la única etiqueta del control al que está asociado (es decir, que el control no tiene a su vez title o aria-label)
     *
     * @param document     Document para comprobar si existe control asociado y si dispone de otros etiquetadores (title o aria-label)
     * @param forAttribute valor del atributo for del elemento label
     * @return true si es la única etiqueta del control, false en caso contrario (includo si la etiqueta no está asociada)
     */
    private boolean isUniqueLabel(final Document document, final String forAttribute) {
        if (forAttribute != null && !forAttribute.isEmpty()) {
            final Element control = document.getElementById(forAttribute);
            if (control != null) {
                final String title = control.getAttribute("title");
                final String ariaLabel = control.getAttribute("aria-label");
                if ((title != null && !title.isEmpty()) || (ariaLabel != null && !ariaLabel.isEmpty())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
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
