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
package es.gob.oaw.css.utils;

import com.steadystate.css.parser.selectors.AndConditionImpl;
import com.steadystate.css.parser.selectors.PseudoElementSelectorImpl;
import org.w3c.css.sac.*;
import org.w3c.css.sac.helpers.ParserFactory;

/**
 * Clase con métodos de utilidad para transformar desde los tipos usados por un parseador CSS SAC
 */
public final class CSSSACUtils {

    private static final String SPACE_SEPARATOR = " ";
    private static final String COMMA_SEPARATOR = ", ";
    private static final java.text.DecimalFormat DECIMAL_FORMAT = new java.text.DecimalFormat("0.##");
    // Inicializamos un ErrorHandler vacío porque por defecto imprime los errores por System.err
    private static final ErrorHandler NO_ERROR_HANDLER = new ErrorHandler() {
        @Override
        public void warning(CSSParseException exception) throws CSSException {
            // Do nothing
        }

        @Override
        public void error(CSSParseException exception) throws CSSException {
            // Do nothing
        }

        @Override
        public void fatalError(CSSParseException exception) throws CSSException {
            // Do nothing
        }
    };

    static {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");
    }

    private CSSSACUtils() {
    }

    public static Parser getSACParser() throws Exception {
        final ParserFactory parserFactory = new ParserFactory();
        final Parser cssParser = parserFactory.makeParser();
        cssParser.setErrorHandler(NO_ERROR_HANDLER);

        return cssParser;
    }

    /**
     * Convierte un valor completo (recorre todos los valores) de una propiedad CSS de tipo LexicalUnit a una cadena
     *
     * @param lexicalUnit el valor a transformar
     * @return una cadena correspondiente al valor
     */
    public static String parseLexicalValue(LexicalUnit lexicalUnit) {
        final StringBuilder sb = new StringBuilder();
        while (lexicalUnit != null) {
            sb.append(parseSingleLexicalValue(lexicalUnit));
            lexicalUnit = lexicalUnit.getNextLexicalUnit();
            if (lexicalUnit != null) {
                if (lexicalUnit.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA) {
                    sb.append(COMMA_SEPARATOR);
                } else {
                    sb.append(SPACE_SEPARATOR);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Convierte un <strong>único</strong> valor de una propiedad CSS de tipo LexicalUnit a una cadena
     *
     * @param lexicalUnit el valor a transformar
     * @return una cadena correspondiente al valor
     */
    public static String parseSingleLexicalValue(final LexicalUnit lexicalUnit) {
        final StringBuilder sb = new StringBuilder();
        final int lexicalUnitType = lexicalUnit.getLexicalUnitType();
        switch (lexicalUnitType) {
            case LexicalUnit.SAC_INTEGER:
                sb.append(Integer.toString(lexicalUnit.getIntegerValue()));
                break;
            case LexicalUnit.SAC_REAL:
            case LexicalUnit.SAC_DIMENSION:
            case LexicalUnit.SAC_EM:
            case LexicalUnit.SAC_EX:
            case LexicalUnit.SAC_PIXEL:
            case LexicalUnit.SAC_INCH:
            case LexicalUnit.SAC_CENTIMETER:
            case LexicalUnit.SAC_MILLIMETER:
            case LexicalUnit.SAC_POINT:
            case LexicalUnit.SAC_PICA:
            case LexicalUnit.SAC_PERCENTAGE:
            case LexicalUnit.SAC_DEGREE:
            case LexicalUnit.SAC_GRADIAN:
            case LexicalUnit.SAC_RADIAN:
            case LexicalUnit.SAC_MILLISECOND:
            case LexicalUnit.SAC_SECOND:
            case LexicalUnit.SAC_HERTZ:
            case LexicalUnit.SAC_KILOHERTZ:
                sb.append(DECIMAL_FORMAT.format(lexicalUnit.getFloatValue()));
                sb.append(lexicalUnit.getDimensionUnitText());
                break;
            case LexicalUnit.SAC_IDENT:
            case LexicalUnit.SAC_STRING_VALUE:
            case LexicalUnit.SAC_UNICODERANGE:
                sb.append(lexicalUnit.getStringValue());
                break;
            case LexicalUnit.SAC_URI:
                sb.append("url(");
                sb.append(lexicalUnit.getStringValue());
                sb.append(")");
                break;
            case LexicalUnit.SAC_ATTR:
            case LexicalUnit.SAC_COUNTER_FUNCTION:
            case LexicalUnit.SAC_COUNTERS_FUNCTION:
            case LexicalUnit.SAC_RECT_FUNCTION:
            case LexicalUnit.SAC_FUNCTION:
                sb.append(lexicalUnit.getFunctionName());
                // HACK para tratar la función rgba añadida en CSS3 (Color Model) que no es reconocida por SAC
                // Si es rgba gestionamos aquí la función
                if ("rgba".equalsIgnoreCase(lexicalUnit.getFunctionName())) {
                    sb.append('(');
                    sb.append(parseLexicalValue(lexicalUnit.getParameters()));
                    sb.append(')');
                    break;
                }
                sb.append(parseLexicalValue(lexicalUnit.getParameters()));
                break;
            case LexicalUnit.SAC_RGBCOLOR:
                // SAC Parser translates #FFF in 255,255,255 so sorround it with 'rgb()'
                // OR If we want the hexadecimal format
                LexicalUnit colorUnit = lexicalUnit.getParameters();
                sb.append('#');
                // get R value from RGB
                // Si el valor es un único digito no lo "paddea" por lo que un color FF0006 lo
                // escribe como FF06, por eso se antepone un "0" mediante la función converTo2DigitsHexString
                sb.append(converTo2DigitsHexString(colorUnit.getIntegerValue()));
                // Skip COMMA_SEPARATOR
                colorUnit = colorUnit.getNextLexicalUnit();
                // get G value from RGB
                colorUnit = colorUnit.getNextLexicalUnit();
                sb.append(converTo2DigitsHexString(colorUnit.getIntegerValue()));
                // Skip COMMA_SEPARATOR
                colorUnit = colorUnit.getNextLexicalUnit();
                // get B value from RGB
                colorUnit = colorUnit.getNextLexicalUnit();
                sb.append(converTo2DigitsHexString(colorUnit.getIntegerValue()));
                break;
            case LexicalUnit.SAC_INHERIT:
                sb.append("inherit");
                break;
            case LexicalUnit.SAC_OPERATOR_COMMA:
                break;
            case LexicalUnit.SAC_SUB_EXPRESSION:
                break;
            default:
                sb.append("unknown");
                break;
        }
        return sb.toString();
    }

    /**
     * Convierte un valor entero en su representación hexadecimal en formato cadena de al menos 2 dígitos
     * (transforma los valores 1-F en 01-0F)
     *
     * @param valor entero a transformar
     * @return string de al menos dos caracteres con la representación hexadecimal del valor
     */
    private static String converTo2DigitsHexString(final int valor) {
        return (valor < 16 ? "0" : "") + Integer.toHexString(valor);
    }

    /**
     * Convierte una lista de selectores CSS a una cadena
     *
     * @param selectorList una lista de selectores
     * @return una cadena con la transcripción de los selectores
     */
    public static String buildSelector(final SelectorList selectorList) {
        if (selectorList == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(100);
        if (selectorList.getLength() > 0) {
            sb.append(buildSelector(selectorList.item(0)));
            for (int i = 1; i < selectorList.getLength(); i++) {
                sb.append(COMMA_SEPARATOR);
                sb.append(buildSelector(selectorList.item(i)));
            }
        }
        return sb.toString();
    }

    /**
     * Convierte un selector de CSS a una cadena
     *
     * @param sel el selector a convertir
     * @return una cadena con la transcripción del selector
     */
    public static String buildSelector(final Selector sel) {
        final StringBuilder selectorBuilder = new StringBuilder(60);

        switch (sel.getSelectorType()) {
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                // This selector matches only tag type
                final ElementSelector es = (ElementSelector) sel;
                if (es.getLocalName() != null) {
                    selectorBuilder.append(es.getLocalName());
                }
                break;
            case Selector.SAC_CONDITIONAL_SELECTOR:
                // This selector matches all the interesting things:
                // #myId
                // [someattr="someval"]
                // simple[role="private"]
                // myclass#myId (check this. [bshine 9.05.06])
                final ConditionalSelector cs = (ConditionalSelector) sel;
                // Take care of the simple selector part of this
                selectorBuilder.append(buildSelector(cs.getSimpleSelector()));
                selectorBuilder.append(buildCondition(cs.getCondition()));
                break;
            case Selector.SAC_DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) sel;
                selectorBuilder.append(buildSelector(ds.getAncestorSelector()));
                selectorBuilder.append(SPACE_SEPARATOR);
                selectorBuilder.append(buildSelector(ds.getSimpleSelector()));
                break;
            case Selector.SAC_CHILD_SELECTOR:
                final DescendantSelector chs = (DescendantSelector) sel;
                selectorBuilder.append(buildSelector(chs.getAncestorSelector()));
                if (chs.getSimpleSelector().getSelectorType() != Selector.SAC_PSEUDO_ELEMENT_SELECTOR) {
                    selectorBuilder.append('>');
                }
                selectorBuilder.append(buildSelector(chs.getSimpleSelector()));
                break;
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
                final SiblingSelector das = (SiblingSelector) sel;
                selectorBuilder.append(buildSelector(das.getSelector()));
                selectorBuilder.append('+');
                selectorBuilder.append(buildSelector(das.getSiblingSelector()));
                break;
            case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
                final PseudoElementSelectorImpl pses = (PseudoElementSelectorImpl) sel;
                selectorBuilder.append(':');
                selectorBuilder.append(pses.getLocalName());
                break;
            default:
                selectorBuilder.append("unknown_selector ");
                selectorBuilder.append(sel.getSelectorType());
                break;
        }

        return selectorBuilder.toString();
    }

    /**
     * Convierte una condición de un selector a una cadena
     *
     * @param condition la condicion Condition a convertir
     * @return una cadena con la transformación textual del objeto condition
     */
    public static String buildCondition(final Condition condition) {
        final StringBuilder conditionBuilder = new StringBuilder(20);
        switch (condition.getConditionType()) {
            case Condition.SAC_ID_CONDITION: /* #id */
                final AttributeCondition idCond = (AttributeCondition) condition;
                conditionBuilder.append('#');
                conditionBuilder.append(idCond.getValue());
                break;
            case Condition.SAC_ATTRIBUTE_CONDITION: // [attr] or [attr="val"] or elem[attr="val"]
                final AttributeCondition attrCond = (AttributeCondition) condition;
                final String name = attrCond.getLocalName();
                final String value = attrCond.getValue();
                conditionBuilder.append('[');
                conditionBuilder.append(name);
                if (value != null) {
                    conditionBuilder.append("=\"");
                    conditionBuilder.append(value);
                    conditionBuilder.append('"');
                }
                conditionBuilder.append(']');
                break;
            case Condition.SAC_CLASS_CONDITION: // .class
                final AttributeCondition classCond = (AttributeCondition) condition;
                conditionBuilder.append('.');
                conditionBuilder.append(classCond.getValue());
                break;
            case Condition.SAC_PSEUDO_CLASS_CONDITION: //:pseudo
                final AttributeCondition pclassCond = (AttributeCondition) condition;
                conditionBuilder.append(':');
                conditionBuilder.append(pclassCond.getValue());
                break;
            default:
                conditionBuilder.append(condition.toString());
                break;
        }
        return conditionBuilder.toString();
    }

    public static String getXPATHFromSelector(final Selector sel) {
        final StringBuilder selectorString = new StringBuilder("//");
        getXPATHFromSelector(sel, selectorString);
        return selectorString.toString().trim();
    }

    protected static void getXPATHFromSelector(final Selector sel, final StringBuilder selectorString) {
        switch (sel.getSelectorType()) {
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                // This selector matches only tag type
                final ElementSelector es = (ElementSelector) sel;
                if (es.getLocalName() != null) {
                    selectorString.append(es.getLocalName());
                } else {
                    selectorString.append('*');
                }
                break;
            case Selector.SAC_CONDITIONAL_SELECTOR:
                // This selector matches all the interesting things:
                // #myId
                // [someattr="someval"]
                // simple[role="private"]
                // myclass#myId (check this. [bshine 9.05.06])
                final ConditionalSelector cs = (ConditionalSelector) sel;
                // Take care of the simple selector part of this
                getXPATHFromSelector(cs.getSimpleSelector(), selectorString);
                selectorString.append(buildXPATHCondition(cs.getCondition()));
                break;
            case Selector.SAC_DESCENDANT_SELECTOR:
                final DescendantSelector descendantSelector = (DescendantSelector) sel;
                getXPATHFromSelector(descendantSelector.getAncestorSelector(), selectorString);
                if ( descendantSelector.getSimpleSelector().getSelectorType()!= Selector.SAC_PSEUDO_ELEMENT_SELECTOR) {
                    selectorString.append("//");
                }
                getXPATHFromSelector(descendantSelector.getSimpleSelector(), selectorString);

                break;
            case Selector.SAC_CHILD_SELECTOR:
                final DescendantSelector childSelector = (DescendantSelector) sel;
                getXPATHFromSelector(childSelector.getAncestorSelector(), selectorString);
                // Los pseudo elementos como :before se consideran tambien como
                // SAC_CHILD_SELECTOR. En esos casos no se añade la / porque
                // realmente no hay elemento hijo
                if (childSelector.getSimpleSelector().getSelectorType() != Selector.SAC_PSEUDO_ELEMENT_SELECTOR) {
                    selectorString.append('/');
                }

                getXPATHFromSelector(childSelector.getSimpleSelector(), selectorString);
                break;
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
                final SiblingSelector siblingSelector = (SiblingSelector) sel;
                getXPATHFromSelector(siblingSelector.getSelector(), selectorString);
                selectorString.append("/following-sibling::*[1]/self::");
                getXPATHFromSelector(siblingSelector.getSiblingSelector(),
                        selectorString);
                break;
            case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
                // Ignoramos 'pseudo elementos' como :first-line ¿algún otro?
                // (:before y :after se consideran SAC_CHILD_SELECTOR)
                break;
            default:
                break;
        }
    }

    protected static String buildXPATHCondition(final Condition condition) {
        final StringBuilder conditionBuilder = new StringBuilder(20);
        switch (condition.getConditionType()) {
            case Condition.SAC_ID_CONDITION: /* #id */
                final AttributeCondition idCond = (AttributeCondition) condition;
                conditionBuilder.append("[@id='");
                conditionBuilder.append(idCond.getValue());
                conditionBuilder.append("']");
                break;
            case Condition.SAC_ATTRIBUTE_CONDITION: // [attr] or [attr="val"] or
                // elem[attr="val"]
                final AttributeCondition attrCond = (AttributeCondition) condition;
                final String name = attrCond.getLocalName();
                final String value = attrCond.getValue();
                conditionBuilder.append('[');
                conditionBuilder.append('@');
                conditionBuilder.append(name);
                if (value != null) {
                    conditionBuilder.append("='");
                    conditionBuilder.append(value);
                    conditionBuilder.append('\'');
                }
                conditionBuilder.append(']');
                break;
            case Condition.SAC_CLASS_CONDITION:
                // FIXME: No comprueba el uso de múltiples valores en el attributo
                // class. Sería algo tipo:
                // [ contains(concat(" ",@class," "),concat(" ","value", " ")) ]
                final AttributeCondition classCond = (AttributeCondition) condition;
                conditionBuilder.append("[@class='");
                conditionBuilder.append(classCond.getValue());
                conditionBuilder.append("']");
                break;
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
                // Ignoramos las pseudo class y comprobaremos sobre el elemento
                break;
            case Condition.SAC_AND_CONDITION:
                // Unión de condiciones ej #main.noticia .css_1.css_2
                final AndConditionImpl andCondition = (AndConditionImpl) condition;
                conditionBuilder.append(buildXPATHCondition(andCondition.getFirstCondition()));
                conditionBuilder.append(buildXPATHCondition(andCondition.getSecondCondition()));
                break;
            default:
                conditionBuilder.append(condition.toString());
                break;
        }
        return conditionBuilder.toString();
    }

}