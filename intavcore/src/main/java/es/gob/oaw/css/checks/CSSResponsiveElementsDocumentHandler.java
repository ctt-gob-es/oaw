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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.dom4j.Document;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;

import es.gob.oaw.css.CSSProblem;
import es.gob.oaw.css.CSSResource;
import es.gob.oaw.css.OAWCSSVisitor;
import es.gob.oaw.utils.AccesibilityDeclarationCheckUtils;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

/**
 * Clase para detectar si se aplican 'line-height', 'letter-spacing', 'word-spacing' con important.
 */
public class CSSResponsiveElementsDocumentHandler extends OAWCSSVisitor {
	/** The has media queries. */
	private boolean hasMediaQueries = false;
	/** The has grid tags. */
	private boolean hasGridTags = false;
	/** The has flexbox tags. */
	private boolean hasFlexboxTags = false;
	private int countMediaQueries;
	private int countGridTags;
	private int countFelxbox;

	/**
	 * Evaluate.
	 *
	 * @param document     the document
	 * @param cssResources the css resources
	 * @return the list
	 */
	@Override
	public List<CSSProblem> evaluate(final Document document, final List<CSSResource> cssResources) {
		countMediaQueries = 0;
		countFelxbox = 0;
		countGridTags = 0;
		final List<CSSProblem> cssProblems = new ArrayList<>();
		for (CSSResource cssResource : cssResources) {
			cssProblems.addAll(evaluate(document, cssResource));
		}
		// Si no hay ninguna
		if (countMediaQueries + countFelxbox + countGridTags == 0) {
			cssProblems.add(createCSSProblem());
		}
		return cssProblems;
	}

	/**
	 * Evaluate.
	 *
	 * @param document    the document
	 * @param cssResource the css resource
	 * @return the list
	 */
	@Override
	public List<CSSProblem> evaluate(final Document document, final CSSResource cssResource) {
		final List<CSSProblem> cssProblems = new ArrayList<>();
		if (!cssResource.getContent().isEmpty()) {
			PropertiesManager pm = new PropertiesManager();
			String[] regexpMedia = pm.getValue("check.patterns.properties", "check.479.regex.pattern.media").split(",");
			String[] regexpGrid = pm.getValue("check.patterns.properties", "check.479.regex.pattern.grid").split(",");
			String[] regexpFlexbox = pm.getValue("check.patterns.properties", "check.479.regex.pattern.flexbox").split(",");
			// Grid properties
			for (String stringPattern : regexpMedia) {
				try {
					Pattern patternMedia = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					if (patternMedia.matcher(cssResource.getContent()).find()) {
						countMediaQueries++;
						break;
					}
				} catch (Exception e) {
					Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			// Grid properties
			for (String stringPattern : regexpGrid) {
				try {
					Pattern patternGrid = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					if (patternGrid.matcher(cssResource.getContent()).find()) {
						countGridTags++;
						break;
					}
				} catch (Exception e) {
					Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			// Flexbox properties
			for (String stringPattern : regexpFlexbox) {
				try {
					Pattern patternFlexbox = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					if (patternFlexbox.matcher(cssResource.getContent()).find()) {
						countFelxbox++;
						break;
					}
				} catch (Exception e) {
					Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			try {
				resource = cssResource;
				final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, new CollectingCSSParseErrorHandler());
				if (aCSS != null) {
					// Si encontramos las media query o los tas añadimos un error (que luego
					// gestionaremos para devolver)
					if (aCSS.hasMediaRules()) {
						hasMediaQueries = true;
						countMediaQueries++;
					}
//					CSSVisitor.visitCSS(aCSS, this);
				}
			} catch (Exception e) {
				Logger.putLog("Error al intentar parsear el CSS", OAWCSSVisitor.class, Logger.LOG_LEVEL_INFO, e);
			}
//			if (!hasFlexboxTags && !hasGridTags && !hasMediaQueries) {
//				cssProblems.add(createCSSProblem());
//			}
		}
		return cssProblems;
	}

	/**
	 * On declaration.
	 *
	 * @param cssDeclaration the css declaration
	 */
	@Override
	public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
		PropertiesManager pm = new PropertiesManager();
		// check.479.regex.pattern.grid
		// check.479.regex.pattern.flexbox
		String[] regexpGrid = pm.getValue("check.patterns.properties", "check.479.regex.pattern.grid").split(",");
		String[] regexpFlexbox = pm.getValue("check.patterns.properties", "check.479.regex.pattern.flexbox").split(",");
		// Grid properties
		for (String stringPattern : regexpGrid) {
			try {
				Pattern patternGrid = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				if (patternGrid.matcher(cssDeclaration.getProperty()).find()) {
					hasGridTags = true;
					break;
				}
			} catch (Exception e) {
				Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
		// Flexbox properties
		for (String stringPattern : regexpFlexbox) {
			try {
				Pattern patternFlexbox = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				if (patternFlexbox.matcher(cssDeclaration.getProperty()).find()) {
					hasFlexboxTags = true;
					break;
				}
			} catch (Exception e) {
				Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
	}

	/**
	 * Creates the CSS problem.
	 *
	 * @return the CSS problem
	 */
	private CSSProblem createCSSProblem() {
		final CSSProblem cssProblem = new CSSProblem();
		cssProblem.setDate(new Date());
		cssProblem.setLineNumber(0);
		cssProblem.setColumnNumber(0);
		cssProblem.setSelector("");
		cssProblem.setElement(null);
		return cssProblem;
	}
}
