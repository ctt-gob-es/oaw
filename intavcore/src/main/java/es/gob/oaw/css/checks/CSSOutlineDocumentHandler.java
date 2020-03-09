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

import javax.annotation.Nonnull;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.writer.CSSWriterSettings;

import es.gob.oaw.css.OAWCSSVisitor;
import es.gob.oaw.css.utils.CSSSACUtils;

/**
 * Clase para comprobar si mediante CSS se elimina la marca al elemento que contiene el foco y no se proporciona la información mediante técnicas alternativas.
 */
public class CSSOutlineDocumentHandler extends OAWCSSVisitor {
	/** The focus hidden. */
	private boolean focusHidden;
	/** The alternative outline method. */
	private boolean alternativeOutlineMethod;
	/** The outline declaration. */
	private CSSDeclaration outlineDeclaration;

	/**
	 * On declaration.
	 *
	 * @param cssDeclaration the css declaration
	 */
	@Override
	public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
		if (isFocusPseudoClass() && isValidMedia()) {
			if ("outline".equals(cssDeclaration.getProperty())) {
				final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
				if (isZero(value)) {
					focusHidden = true;
					outlineDeclaration = cssDeclaration;
				}
			} else if ("outline-width".equals(cssDeclaration.getProperty())) {
				final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
				if (isZero(value)) {
					focusHidden = true;
					outlineDeclaration = cssDeclaration;
				}
			} else if ("outline-style".equals(cssDeclaration.getProperty())) {
				final String value = CSSSACUtils.parseLexicalValue(getValue(cssDeclaration));
				if ("none".equals(value)) {
					focusHidden = true;
					outlineDeclaration = cssDeclaration;
				}
			} else if (isAlternativeOutlineMethod(cssDeclaration.getProperty(), CSSSACUtils.parseLexicalValue(getValue(cssDeclaration)))) {
				alternativeOutlineMethod = true;
			}
		}
	}

	/**
	 * Checks if is alternative outline method.
	 *
	 * @param cssProperty the css property
	 * @param cssValue    the css value
	 * @return true, if is alternative outline method
	 */
	private boolean isAlternativeOutlineMethod(final String cssProperty, final String cssValue) {
		if ("border".equals(cssProperty)) {
			return !isZero(cssValue);
		} else if ("background-color".equals(cssProperty)) {
			return true;
		} else if ("box-shadow".equals(cssProperty)) {
			return true;
		} else if ("color".equals(cssProperty)) {
			return true;
		} else if ("text-decoration".equals(cssProperty)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * On end style rule.
	 *
	 * @param cssStyleRule the css style rule
	 */
	@Override
	public void onEndStyleRule(@Nonnull CSSStyleRule cssStyleRule) {
		if (focusHidden && !alternativeOutlineMethod) {
			getProblems().add(createCSSProblem("", outlineDeclaration));
		}
		focusHidden = false;
		alternativeOutlineMethod = false;
		outlineDeclaration = null;
	}

	/**
	 * Checks if is zero.
	 *
	 * @param value the value
	 * @return true, if is zero
	 */
	private boolean isZero(final String value) {
		return value != null && (value.equals("0") || value.contains("none"));
	}

	/**
	 * Checks if is focus pseudo class.
	 *
	 * @return true, if is focus pseudo class
	 */
	private boolean isFocusPseudoClass() {
		if (currentStyleRule != null) {
			final String selector = currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
			return selector.contains(":focus");
		} else {
			return false;
		}
	}
}
