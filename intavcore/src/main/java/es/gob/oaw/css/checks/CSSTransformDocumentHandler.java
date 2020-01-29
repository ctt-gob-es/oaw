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
import java.util.List;
import java.util.regex.Pattern;

import org.dom4j.Document;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSMediaExpression;
import com.helger.css.decl.CSSMediaQuery;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;

import es.gob.oaw.css.CSSProblem;
import es.gob.oaw.css.CSSResource;
import es.gob.oaw.css.OAWCSSVisitor;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;

/**
 * Clase para detectar si se aplican 'line-height', 'letter-spacing', 'word-spacing' con important.
 */
public class CSSTransformDocumentHandler extends OAWCSSVisitor {
	/**
	 * Evaluate.
	 *
	 * @param document     the document
	 * @param cssResources the css resources
	 * @return the list
	 */
	@Override
	public List<CSSProblem> evaluate(final Document document, final List<CSSResource> cssResources) {
		final List<CSSProblem> cssProblems = new ArrayList<>();
		for (CSSResource cssResource : cssResources) {
			cssProblems.addAll(evaluate(document, cssResource));
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
		Pattern transform90 = Pattern.compile("rotate\\s*\\(\\s*90deg\\s*\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Pattern transform270 = Pattern.compile("rotate\\s*\\(\\s*270deg\\s*\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		if (!cssResource.getContent().isEmpty()) {
			try {
				resource = cssResource;
				final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, new CollectingCSSParseErrorHandler());
				if (aCSS != null) {
					// Si encontramos las media query o los tas añadimos un error (que luego
					// gestionaremos para devolver)
					if (aCSS.hasMediaRules()) {
						List<CSSMediaRule> mediaRules = aCSS.getAllMediaRules();
						for (int i = 0; i < mediaRules.size(); i++) {
							CSSMediaRule mediaRule = mediaRules.get(i);
							// Has orientation media query
							List<CSSMediaQuery> mediaQuerys = mediaRule.getAllMediaQueries();
							if (mediaQuerys != null && !mediaQuerys.isEmpty()) {
								for (CSSMediaQuery mediaQuery : mediaQuerys) {
									List<CSSMediaExpression> expresions = mediaQuery.getAllMediaExpressions();
									if (expresions != null && !expresions.isEmpty()) {
										for (CSSMediaExpression expression : expresions) {
											if ("orientation".equalsIgnoreCase(expression.getFeature())) {
												List<CSSStyleRule> styleRules = mediaRule.getAllStyleRules();
												if (styleRules != null && !styleRules.isEmpty()) {
													for (CSSStyleRule styleRule : styleRules) {
														List<CSSDeclaration> declarations = styleRule.getAllDeclarations();
														if (declarations != null && !declarations.isEmpty()) {
															for (CSSDeclaration declaration : declarations) {
																if ("transform".equalsIgnoreCase(declaration.getProperty())) {
																	String cssExpression = StringUtils.normalizeWhiteSpaces(declaration.getExpressionAsCSSString()).trim();
																	if (!cssExpression.isEmpty() && (transform90.matcher(cssExpression).find() || transform270.matcher(cssExpression).find())) {
																		cssProblems.add(createCSSProblem("", declaration));
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					CSSVisitor.visitCSS(aCSS, this);
				}
			} catch (Exception e) {
				Logger.putLog("Error al intentar parsear el CSS", OAWCSSVisitor.class, Logger.LOG_LEVEL_INFO, e);
			}
		}
		return cssProblems;
	}
}
