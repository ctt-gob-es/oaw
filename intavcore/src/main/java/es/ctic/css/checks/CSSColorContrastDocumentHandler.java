package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.ColorValues;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.utils.CSSSACUtils;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SelectorList;

import java.awt.*;

/**
 *
 */
public class CSSColorContrastDocumentHandler extends CSSDocumentHandler {

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
            checkColorContrast();
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
        } else if ( "font-weight".equals(name)) {
            fontWeightProperty = value;
        }
    }

    private boolean needHighContrast() {
        final boolean isBold = isFontBold();
        if ( fontSizeProperty==null ) {
            return  false;
        } else  if (fontSizeProperty.getLexicalUnitType()== LexicalUnit.SAC_POINT) {
            return isBold?fontSizeProperty.getIntegerValue()<14:fontSizeProperty.getIntegerValue()<18;
        } else if (fontSizeProperty.getLexicalUnitType()== LexicalUnit.SAC_PIXEL) {
            return isBold?fontSizeProperty.getIntegerValue()<14:fontSizeProperty.getIntegerValue()<18;
        } else if (fontSizeProperty.getLexicalUnitType()== LexicalUnit.SAC_EM) {
            return isBold?fontSizeProperty.getIntegerValue()<1.2:fontSizeProperty.getFloatValue()<1.5;
        } else if (fontSizeProperty.getLexicalUnitType()== LexicalUnit.SAC_PERCENTAGE) {
            return isBold?fontSizeProperty.getIntegerValue()<120:fontSizeProperty.getFloatValue()<150;
        } else {
            return false;
        }
    }

    private boolean isFontBold() {
        if ( fontWeightProperty!=null ) {
            if ( fontWeightProperty.getLexicalUnitType()==LexicalUnit.SAC_IDENT) {
                return "bold".equalsIgnoreCase(fontWeightProperty.getStringValue());
            } else if (fontWeightProperty.getLexicalUnitType()==LexicalUnit.SAC_INTEGER) {
                return fontWeightProperty.getIntegerValue()>=400;
            }
        }
        return false;
    }

    private void checkColorContrast() {
        final Color foreground = obtainColor(foregroundProperty);
        final Color background = obtainColor(backgroundProperty);

        // Calculamos la luminosidad de primer y segundo plano
        final double foregroundLuminance = obtainLuminance(foreground);
        final double backgroundLuminance = obtainLuminance(background);
        // Calculamos la diferencia de contraste
        final double contrastRatio = obtainContrastRatio(foregroundLuminance, backgroundLuminance);

        if ( !needHighContrast() ) {
            if (contrastRatio < 3.5) {
                getProblems().add(createCSSProblem("Ratio color:background - " + contrastRatio));
            }
        } else {
            if (contrastRatio < 4.5) {
                getProblems().add(createCSSProblem("Ratio color:background - " + contrastRatio));
            }
        }
    }

    private Color obtainColor(final LexicalUnit colorValue) {
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
}
