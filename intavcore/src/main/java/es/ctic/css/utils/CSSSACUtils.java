package es.ctic.css.utils;

import com.steadystate.css.parser.selectors.PseudoElementSelectorImpl;
import org.w3c.css.sac.*;

/**
 *
 */
public final class CSSSACUtils {

    private static final String SPACE_SEPARATOR = " ";
    private static final String COMMA_SEPARATOR = ", ";
    private static final java.text.DecimalFormat DECIMAL_FORMAT = new java.text.DecimalFormat("0.##");

    private CSSSACUtils() {
    }

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

    public static String parseSingleLexicalValue(final LexicalUnit lexicalUnit) {
        final StringBuilder sb = new StringBuilder();
        int lexicalUnitType = lexicalUnit.getLexicalUnitType();
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
                // SAC Parser translates #FFF in 255,255,255 so we sorround it
                // with rgb
            /*
             * sb.append("rgb("); sb.append(
             * parseLexicalValue(lexicalUnit.getParameters()));
             * sb.append(")");
             */
                // OR If we want the hexadecimal format
                LexicalUnit colorUnit = lexicalUnit.getParameters();
                sb.append('#');
                // get R value from RGB
                int valor = colorUnit.getIntegerValue();
                // Si es el valor 0 no lo duplica por lo que un color FF0000 lo
                // escribe como FF00, por eso se duplica el valor
                sb.append(valor == 0 ? "00" : Integer.toHexString(valor));
                // Skip COMMA_SEPARATOR
                colorUnit = colorUnit.getNextLexicalUnit();
                // get G value from RGB
                colorUnit = colorUnit.getNextLexicalUnit();
                valor = colorUnit.getIntegerValue();
                sb.append(valor == 0 ? "00" : Integer.toHexString(valor));
                // Skip COMMA_SEPARATOR
                colorUnit = colorUnit.getNextLexicalUnit();
                // get B value from RGB
                colorUnit = colorUnit.getNextLexicalUnit();
                valor = colorUnit.getIntegerValue();
                sb.append(valor == 0 ? "00" : Integer.toHexString(valor));
                break;
            case LexicalUnit.SAC_INHERIT:
                sb.append("inherit");
                break;
            case LexicalUnit.SAC_OPERATOR_COMMA:
                // sb.append(", ");
                break;
            case LexicalUnit.SAC_SUB_EXPRESSION:
                break;
            default:
                sb.append("unknown");
                break;
        }
        return sb.toString();
    }

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
            case Condition.SAC_CLASS_CONDITION:
                final AttributeCondition classCond = (AttributeCondition) condition;
                conditionBuilder.append('.');
                conditionBuilder.append(classCond.getValue());
                break;
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
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

}