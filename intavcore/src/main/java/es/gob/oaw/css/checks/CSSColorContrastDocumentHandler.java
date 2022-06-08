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

import java.awt.Color;

import javax.annotation.Nonnull;

import org.w3c.css.sac.LexicalUnit;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.opensymphony.oscache.util.StringUtil;

import ca.utoronto.atrc.tile.accessibilitychecker.ColorValues;
import es.gob.oaw.css.OAWCSSVisitor;
import es.gob.oaw.css.utils.CSSSACUtils;
import es.inteco.common.logging.Logger;

/**
 * Clase que para cada selector de CSS comprueba, si se definen, los colores de primer plano y fondo el contraste entre ambos colores según el algoritmo WCAG 2.0 ver
 * http://www.w3.org/TR/2014/NOTE-WCAG20-TECHS-20140916/G18
 */
public class CSSColorContrastDocumentHandler extends OAWCSSVisitor {
	/** The background property. */
	private LexicalUnit backgroundProperty;
	/** The foreground property. */
	private LexicalUnit foregroundProperty;
	/** The font size property. */
	private LexicalUnit fontSizeProperty;
	/** The font weight property. */
	private LexicalUnit fontWeightProperty;

	/**
	 * On end style rule.
	 *
	 * @param cssStyleRule the css style rule
	 */
	@Override
	public void onEndStyleRule(@Nonnull final CSSStyleRule cssStyleRule) {
		// Si al finalizar de procesar un bloque de declaración de estilos tenemos ambas
		// propiedades comprobamos el contraste
		if (backgroundProperty != null && foregroundProperty != null) {
			checkColorContrast();
		}
		// Inicializamos los valores
		backgroundProperty = null;
		foregroundProperty = null;
		fontSizeProperty = null;
		fontWeightProperty = null;
		super.onEndStyleRule(cssStyleRule);
	}

	/**
	 * On declaration.
	 *
	 * @param cssDeclaration the css declaration
	 */
	@Override
	public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
		if (isValidMedia()) {
			if ("color".equals(cssDeclaration.getProperty())) {
				foregroundProperty = getValue(cssDeclaration);
			} else if ("background-color".equals(cssDeclaration.getProperty())) {
				backgroundProperty = getValue(cssDeclaration);
			} else if ("background".equals(cssDeclaration.getProperty())) {
				// El valor de background-color de la propiedad background
				backgroundProperty = getValue(cssDeclaration);
			} else if ("font-size".equals(cssDeclaration.getProperty())) {
				fontSizeProperty = getValue(cssDeclaration);
			} else if ("font-weight".equals(cssDeclaration.getProperty())) {
				fontWeightProperty = getValue(cssDeclaration);
			}
		}
	}

	/**
	 * Need high contrast.
	 *
	 * @return true, if successful
	 */
	private boolean needHighContrast() {
		final boolean isBold = isFontBold();
		if (fontSizeProperty == null) {
			return false;
		} else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_POINT) {
			return isBold ? fontSizeProperty.getIntegerValue() < 14 : fontSizeProperty.getIntegerValue() < 18;
		} else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_PIXEL) {
			return isBold ? fontSizeProperty.getIntegerValue() < 19 : fontSizeProperty.getIntegerValue() < 24;
		} else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_EM) {
			return isBold ? fontSizeProperty.getIntegerValue() < 1.2 : fontSizeProperty.getFloatValue() < 1.5;
		} else if (fontSizeProperty.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
			return isBold ? fontSizeProperty.getIntegerValue() < 120 : fontSizeProperty.getFloatValue() < 150;
		} else {
			return false;
		}
	}

	/**
	 * Checks if is font bold.
	 *
	 * @return true, if is font bold
	 */
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

	/**
	 * Check color contrast.
	 */
	private void checkColorContrast() {
		try {
			// Skip color contrast if backgroun is an image only (first parameter)
			if (backgroundProperty != null && !StringUtil.isEmpty(backgroundProperty.toString()) && backgroundProperty.toString().startsWith("url")) {
				return;
			}
			final Color foreground = obtainColor(foregroundProperty);
			final Color background = obtainColor(backgroundProperty);
			// Calculamos la luminosidad de primer y segundo plano
			final double foregroundLuminance = obtainLuminance(foreground);
			final double backgroundLuminance = obtainLuminance(background);
			// Calculamos la diferencia de contraste
			final double contrastRatio = obtainContrastRatio(foregroundLuminance, backgroundLuminance);
			if (!needHighContrast()) {
				if (contrastRatio < 3) {
					getProblems().add(createCSSProblem(" (ratio:" + contrastRatio + ") ", null));
				}
			} else {
				if (contrastRatio < 4.5) {
					getProblems().add(createCSSProblem(" (ratio:" + contrastRatio + ") ", null));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al comprobar CSSColorContrastDocumentHandler: " + e.getMessage(), CSSColorContrastDocumentHandler.class, Logger.LOG_LEVEL_WARNING);
		}
	}

	/**
	 * Obtain color.
	 *
	 * @param colorValue the color value
	 * @return the color
	 * @throws NumberFormatException the number format exception
	 */
	private Color obtainColor(final LexicalUnit colorValue) throws NumberFormatException {
		if (colorValue.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
			if ("none".equalsIgnoreCase(colorValue.getStringValue())) {
				LexicalUnit lastValue = colorValue;
				LexicalUnit itrLexicalUnit = colorValue.getNextLexicalUnit();
				while (itrLexicalUnit != null) {
					lastValue = itrLexicalUnit;
					itrLexicalUnit = itrLexicalUnit.getNextLexicalUnit();
				}
				return Color.decode(CSSSACUtils.parseSingleLexicalValue(lastValue));
			} else {
				// Si es un nombre de color obtenemos su equivalente hexadecimal
				return Color.decode(ColorValues.getHexColorFromName(colorValue.getStringValue()));
			}
		} else if (colorValue.getLexicalUnitType() == LexicalUnit.SAC_URI) {
			LexicalUnit lastValue = colorValue;
			LexicalUnit itrLexicalUnit = colorValue.getNextLexicalUnit();
			while (itrLexicalUnit != null) {
				lastValue = itrLexicalUnit;
				itrLexicalUnit = itrLexicalUnit.getNextLexicalUnit();
			}
			return Color.decode(CSSSACUtils.parseSingleLexicalValue(lastValue));
		} else {
			return Color.decode(CSSSACUtils.parseSingleLexicalValue(colorValue));
		}
	}

	/**
	 * Obtain luminance.
	 *
	 * @param color the color
	 * @return the double
	 */
	private double obtainLuminance(final Color color) {
		final float rRGB = (float) color.getRed() / 255;
		final float gRGB = (float) color.getGreen() / 255;
		final float bRGB = (float) color.getBlue() / 255;
		final double r = rRGB <= 0.03928 ? rRGB / 12.92 : Math.pow(((rRGB + 0.055) / 1.055), 2.4);
		final double g = gRGB <= 0.03928 ? gRGB / 12.92 : Math.pow(((gRGB + 0.055) / 1.055), 2.4);
		final double b = bRGB <= 0.03928 ? bRGB / 12.92 : Math.pow(((bRGB + 0.055) / 1.055), 2.4);
		return (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
	}

	/**
	 * Obtain contrast ratio.
	 *
	 * @param colorLuminance      the color luminance
	 * @param backgroundLuminance the background luminance
	 * @return the double
	 */
	private double obtainContrastRatio(final double colorLuminance, final double backgroundLuminance) {
		// Obtenemos el valor mayor y menor para calcular el ratio
		final double maxLuminance = Math.max(colorLuminance, backgroundLuminance);
		final double minLuminance = Math.min(colorLuminance, backgroundLuminance);
		// Calculamos el constraste
		final double contrastRatio = (maxLuminance + 0.05) / (minLuminance + 0.05);
		// Redondeamos a 2 decimales
		return Math.round(contrastRatio * 100) / 100d;
	}
}
