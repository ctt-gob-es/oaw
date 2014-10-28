package es.ctic.css.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.ColorValues;
import es.ctic.css.CSSDocumentHandler;
import es.ctic.css.utils.CSSSACUtils;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;

import java.awt.*;

/**
 *
 */
public class CSSColorContrastDocumentHandler extends CSSDocumentHandler {

    private LexicalUnit backgroundProperty;
    private LexicalUnit foregroundProperty;

    public CSSColorContrastDocumentHandler(final Parser parser, final CheckCode checkCode) {
        super(parser, checkCode);
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
        }
    }

    private void checkColorContrast() {
        final Color foreground = obtainColor(foregroundProperty);
        final Color background = obtainColor(backgroundProperty);

        // Calculamos la luminosidad de primer y segundo plano
        final double foregroundLuminance = obtainLuminance(foreground);
        final double backgroundLuminance = obtainLuminance(background);
        // Calculamos la diferencia de contraste
        final double contrastRatio = obtainContrastRatio(foregroundLuminance, backgroundLuminance);

        // TODO: Controlar el tamaño de fuente
        if (contrastRatio < 3) {
            getProblems().add(createCSSProblem("Ratio color:background - " + contrastRatio));
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
        final float RsRGB = (float) color.getRed() / 255;
        final float GsRGB = (float) color.getGreen() / 255;
        final float BsRGB = (float) color.getBlue() / 255;

        final double R = RsRGB <= 0.03928 ? RsRGB / 12.92 : Math.pow(((RsRGB + 0.055) / 1.055), 2.4);
        final double G = GsRGB <= 0.03928 ? GsRGB / 12.92 : Math.pow(((GsRGB + 0.055) / 1.055), 2.4);
        final double B = BsRGB <= 0.03928 ? BsRGB / 12.92 : Math.pow(((BsRGB + 0.055) / 1.055), 2.4);

        return 0.2126 * R + 0.7152 * G + 0.0722 * B;
    }

    private double obtainContrastRatio(double colorLuminance, double backgroundLuminance) {
        // Obtenemos el valor mayor y menor para calcular el ratio
        final double L1 = Math.max(colorLuminance, backgroundLuminance);
        final double L2 = Math.min(colorLuminance, backgroundLuminance);

        // Calculamos el constraste
        double contrastRatio = (L1 + 0.05) / (L2 + 0.05);
        // Redondeamos a 2 decimales
        return Math.round(contrastRatio * 100) / 100d;
    }
}
