package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.ColorValues;
import com.steadystate.css.parser.selectors.PseudoElementSelectorImpl;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.utils.CSSSACUtils;
import org.w3c.css.sac.*;

import java.awt.*;

/**
 *
 */
public class CSSColorContrastDocumentHandler extends CSSDocumentHandler {

    private static final String SPACE_SEPARATOR = " ";
    private static final String COMMA_SEPARATOR = ", ";

    private LexicalUnit backgroundProperty;
    private LexicalUnit foregroundProperty;
    private LexicalUnit fontSizeProperty;
    private LexicalUnit fontWeightProperty;

    public CSSColorContrastDocumentHandler(final CheckCode checkCode) {
        super(checkCode);
    }

    @Override
    public void endSelector(final SelectorList selectors) throws CSSException {
        super.endSelector(selectors);
        // Si al finalizar de procesar un bloque de declaración de estilos tenemos ambas propiedades comprobamos el contraste
        if (backgroundProperty != null && foregroundProperty != null) {
            checkColorContrast(selectors);
        }
        // Inicializamos los valores
        backgroundProperty = null;
        foregroundProperty = null;
        fontSizeProperty = null;
        fontWeightProperty = null;
    }

    @Override
    public void property(final String name, final LexicalUnit value, boolean important) throws CSSException {
        if ("color".equals(name)) {
            foregroundProperty = value;
        } else if ("background-color".equals(name)) {
            backgroundProperty = value;
        } else if ("background".equals(name)) {
            // El valor de background-color de la propiedad background
            backgroundProperty = value;
        } else if ("font-size".equals(name)) {
            fontSizeProperty = value;
        } else if ("font-weight".equals(name)) {
            fontWeightProperty = value;
        }
    }

    private boolean needHighContrast() {
        final boolean isBold = isFontBold();
        if (fontSizeProperty == null) {
            return false;
        } else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_POINT) {
            return isBold ? fontSizeProperty.getIntegerValue() < 14 : fontSizeProperty.getIntegerValue() < 18;
        } else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_PIXEL) {
            return isBold ? fontSizeProperty.getIntegerValue() < 14 : fontSizeProperty.getIntegerValue() < 18;
        } else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_EM) {
            return isBold ? fontSizeProperty.getIntegerValue() < 1.2 : fontSizeProperty.getFloatValue() < 1.5;
        } else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
            return isBold ? fontSizeProperty.getIntegerValue() < 120 : fontSizeProperty.getFloatValue() < 150;
        } else {
            return false;
        }
    }

    private boolean isFontBold() {
        if (fontWeightProperty != null) {
            if (fontWeightProperty.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
                return "bold".equalsIgnoreCase(fontWeightProperty.getStringValue());
            } else if (fontWeightProperty.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
                return fontWeightProperty.getIntegerValue() >= 400;
            }
        }
        return false;
    }

    private void checkColorContrast(SelectorList selectors) {
        try {
            final Color foreground = obtainColor(foregroundProperty);
            final Color background = obtainColor(backgroundProperty);

            // Calculamos la luminosidad de primer y segundo plano
            final double foregroundLuminance = obtainLuminance(foreground);
            final double backgroundLuminance = obtainLuminance(background);
            // Calculamos la diferencia de contraste
            final double contrastRatio = obtainContrastRatio(foregroundLuminance, backgroundLuminance);

            if (!needHighContrast()) {
                if (contrastRatio < 3.5) {
                    getProblems().add(createCSSProblem(buildSelector(selectors) + " (ratio:" + contrastRatio + ")"));
                }
            } else {
                if (contrastRatio < 4.5) {
                    getProblems().add(createCSSProblem(buildSelector(selectors) + " (ratio:" + contrastRatio + ")"));
                }
            }
        } catch (RuntimeException t) {
        }
    }

    private Color obtainColor(final LexicalUnit colorValue) throws NumberFormatException {
        if (colorValue.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            // Si es un nombre de color obtenemos su equivalente hexadecimal
            return Color.decode(ColorValues.getHexColorFromName(colorValue.getStringValue()));
        } else {
            return Color.decode(CSSSACUtils.parseSingleLexicalValue(colorValue));
        }
    }

    private double obtainLuminance(final Color color) {
        final float rRGB = (float) color.getRed() / 255;
        final float gRGB = (float) color.getGreen() / 255;
        final float bRGB = (float) color.getBlue() / 255;

        final double r = rRGB <= 0.03928 ? rRGB / 12.92 : Math.pow(((rRGB + 0.055) / 1.055), 2.4);
        final double g = gRGB <= 0.03928 ? gRGB / 12.92 : Math.pow(((gRGB + 0.055) / 1.055), 2.4);
        final double b = bRGB <= 0.03928 ? bRGB / 12.92 : Math.pow(((bRGB + 0.055) / 1.055), 2.4);

        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    private double obtainContrastRatio(double colorLuminance, double backgroundLuminance) {
        // Obtenemos el valor mayor y menor para calcular el ratio
        final double maxLuminance = Math.max(colorLuminance, backgroundLuminance);
        final double minLuminance = Math.min(colorLuminance, backgroundLuminance);

        // Calculamos el constraste
        final double contrastRatio = (maxLuminance + 0.05) / (minLuminance + 0.05);
        // Redondeamos a 2 decimales
        return Math.round(contrastRatio * 100) / 100d;
    }

    private String buildSelector(final SelectorList selectorList) {
        final StringBuilder sb = new StringBuilder(100);
        if (selectorList != null && selectorList.getLength() > 0) {
            sb.append(buildSelector(selectorList.item(0)));
            for (int i = 1; i < selectorList.getLength(); i++) {
                sb.append(COMMA_SEPARATOR);
                sb.append(buildSelector(selectorList.item(i)));
            }
        }
        return sb.toString();
    }

    private String buildSelector(final Selector sel) {
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
                if (sel instanceof PseudoElementSelectorImpl) {
                    final PseudoElementSelectorImpl pses = (PseudoElementSelectorImpl) sel;
                    selectorBuilder.append(':');
                    selectorBuilder.append(pses.getLocalName());
                }
                break;
            default:
                selectorBuilder.append("unknown_selector ");
                selectorBuilder.append(sel.getSelectorType());
                break;
        }

        return selectorBuilder.toString();
    }

    private String buildCondition(final Condition condition) {
        final StringBuilder conditionBuilder = new StringBuilder(20);
        switch (condition.getConditionType()) {
            case Condition.SAC_ID_CONDITION: /* #id */
                final AttributeCondition idCond = (AttributeCondition) condition;
                conditionBuilder.append('#');
                conditionBuilder.append(idCond.getValue());
                break;
            case Condition.SAC_ATTRIBUTE_CONDITION: // [attr] or [attr="val"] or
                // elem[attr="val"]
                final AttributeCondition attrCond = (AttributeCondition) condition;
                String name = attrCond.getLocalName();
                String value = attrCond.getValue();
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
