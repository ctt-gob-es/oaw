/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.css.checks;

import cz.vutbr.web.css.*;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.StyleMap;
import es.gob.oaw.css.CSSAnalyzer;
import es.gob.oaw.css.CSSProblem;
import es.gob.oaw.css.CSSResource;
import es.inteco.common.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase para comprobar si la etiqueta (elemento label) de un control de formulario está oculta mediante CSS y es el único elemento que proporciona el "name" del control (no dispone de title ni aria-label)
 */
public class CSSLabelHiddenStyleParser implements CSSAnalyzer {

    private final Document document;

    public CSSLabelHiddenStyleParser(final Document document) {
        this.document = document;
    }

    @Override
    public List<CSSProblem> evaluate(final org.dom4j.Document dom4jDocument, final List<CSSResource> cssResources) {
        final List<CSSProblem> cssProblems = new ArrayList<>();
        try {
            CSSFactory.setAutoImportMedia(new MediaSpecNone());
            final StyleSheet styleSheet = CSSFactory.getUsedStyles(document, "utf-8", new URL(String.valueOf(document.getDocumentElement().getUserData("url"))), new MediaSpecAll());
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
                            final TermLength value = nodeData.getValue(TermLength.class, propertyName, true);
                            if (value.getUnit() == TermNumeric.Unit.px && value.getValue() < -3000) {
                                cssProblems.add(createCSSProblem(labelElement, declaration));
                            } else if (value.getUnit() == TermNumeric.Unit.em && value.getValue() <= -900) {
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
        } catch (Exception e) {
            Logger.putLog("Error al comprobar CSSLabelHiddenStyleParser: " + e.getMessage(), CSSLabelHiddenStyleParser.class, Logger.LOG_LEVEL_WARNING);
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
                return !disponeAtributo(title) && !disponeAtributo(ariaLabel);
            }
        }
        return false;
    }

    private CSSProblem createCSSProblem(final Element element, final Declaration declaration) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        cssProblem.setLineNumber(declaration.getSource().getLine());
        cssProblem.setColumnNumber(declaration.getSource().getPosition());
        cssProblem.setSelector("");
        cssProblem.setElement(element);

        return cssProblem;
    }

    private boolean disponeAtributo(final String atributo) {
        return atributo!=null && !atributo.trim().isEmpty();
    }
}
