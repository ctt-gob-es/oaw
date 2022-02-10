package es.gob.oaw.css.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSMediaQuery;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.DoNothingCSSParseExceptionCallback;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;

import es.gob.oaw.css.CSSImportedResource;
import es.gob.oaw.css.CSSResource;
import es.gob.oaw.css.CSSStyleSheetResource;
import es.gob.oaw.css.OAWCSSVisitor;
import es.inteco.common.logging.Logger;

/**
 * Clase para extraer los recursos CSS que son importados (@import) desde otro recurso CSS.
 */
public class ImportedCSSExtractor {
	/** The Constant UTF8_BOM. */
	private static final String UTF8_BOM = "\uFEFF";

	/**
	 * Procesa un documento DOM (HTMLDocument) obtiene los recursos CSS que incluye, tanto los directamente enlazados como los que son importados (@import).
	 *
	 * @param baseURL      the base URL
	 * @param htmlDocument the html document
	 * @return una lista con los recursos CSS que son importados mediate la regla @import
	 */
	public final List<CSSResource> extractFromHTMLDocument(final String baseURL, final HTMLDocument htmlDocument) {
		final List<CSSResource> htmlCssIncludedResources = new ArrayList<>();
		final NodeList embeddedStyleSheets = htmlDocument.getElementsByTagName("style");
		if (embeddedStyleSheets != null && embeddedStyleSheets.getLength() > 0) {
			for (int i = 0; i < embeddedStyleSheets.getLength(); i++) {
				final Element styleElement = (Element) embeddedStyleSheets.item(i);
				if (validMedia(styleElement.getAttribute("media"))) {
					htmlCssIncludedResources.add(new CSSStyleSheetResource(baseURL, styleElement));
				}
			}
		}
		final NodeList linkedStyleSheets = htmlDocument.getElementsByTagName("link");
		if (linkedStyleSheets != null && linkedStyleSheets.getLength() > 0) {
			for (int i = 0; i < linkedStyleSheets.getLength(); i++) {
				final Element linkElement = (Element) linkedStyleSheets.item(i);
				if (linkElement.hasAttribute("href") && ("stylesheet".equalsIgnoreCase(linkElement.getAttribute("rel")) || "text/css".equalsIgnoreCase(linkElement.getAttribute("type")))
						&& validMedia(linkElement.getAttribute("media"))) {
					htmlCssIncludedResources.add(new CSSStyleSheetResource(baseURL, linkElement));
				}
			}
		}
		return extractImportedCSSResources(htmlCssIncludedResources);
	}

	/**
	 * Procesa una lista de recursos CSS @see CSSResource y obtiene los recursos CSS que son importados (@import) desde la lista.
	 *
	 * @param cssResources una lista de recursos CSS CSSResource
	 * @return una lista con los recursos CSS que son importados mediate la regla @import
	 */
	private List<CSSResource> extract(final List<CSSResource> cssResources) {
		final List<CSSResource> extractedCSSResources = new LinkedList<>();
		if (cssResources != null) {
			for (CSSResource cssResource : cssResources) {
				extractedCSSResources.addAll(extract(cssResource));
			}
		}
		return extractedCSSResources;
	}

	/**
	 * Extract.
	 *
	 * @param cssResource the css resource
	 * @return the list
	 */
	public List<CSSResource> extract(final CSSResource cssResource) {
		final List<CSSResource> importedCSSResources = new LinkedList<>();
		if (!cssResource.getContent().isEmpty()) {
			try {
				// La librería que se utiliza no maneja contenido UTF-8 con la marca BOM por lo que eliminamos el BOM si existe
				final String cssContent = getCSSContentWithoutBOM(cssResource.getContent());
				final CascadingStyleSheet aCSS = CSSReader.readFromString(cssContent, ECSSVersion.CSS30, new DoNothingCSSParseErrorHandler(), new DoNothingCSSParseExceptionCallback());
				if (aCSS != null && !aCSS.getAllImportRules().isEmpty()) {
					for (CSSImportRule cssImportRule : aCSS.getAllImportRules()) {
						if (isScreenMedia(cssImportRule.getAllMediaQueries())) {
							CSSImportedResource cssImportedResource = new CSSImportedResource(cssResource.getStringSource(), cssImportRule.getLocationString());
							importedCSSResources.add(cssImportedResource);
						}
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al intentar parsear el CSS: " + e.getLocalizedMessage(), OAWCSSVisitor.class, Logger.LOG_LEVEL_INFO);
			}
		}
		return importedCSSResources;
	}

	/**
	 * Elimina la marca utf-8 BOM de una cadena si esta existe.
	 *
	 * @param content una cadena que puede contener la marca BOM.
	 * @return la cadena sin la marca BOM si esta existia o la cadena original en caso contrario
	 */
	private String getCSSContentWithoutBOM(final String content) {
		if (content.startsWith(UTF8_BOM)) {
			return content.substring(1);
		} else {
			return content;
		}
	}

	/**
	 * Extract imported CSS resources.
	 *
	 * @param cssResources the css resources
	 * @return the list
	 */
	private List<CSSResource> extractImportedCSSResources(final List<CSSResource> cssResources) {
		List<CSSResource> importedCSS = extract(cssResources);
		while (!importedCSS.isEmpty()) {
			importedCSS.removeAll(cssResources);
			cssResources.addAll(importedCSS);
			if (!importedCSS.isEmpty()) {
				importedCSS = extract(importedCSS);
			}
		}
		return cssResources;
	}

	/**
	 * Checks if is screen media.
	 *
	 * @param allMediaQueries the all media queries
	 * @return true, if is screen media
	 */
	private boolean isScreenMedia(final List<CSSMediaQuery> allMediaQueries) {
		if (allMediaQueries == null || allMediaQueries.isEmpty()) {
			return true;
		} else {
			for (CSSMediaQuery allMediaQuery : allMediaQueries) {
				final String medium = allMediaQuery.getMedium();
				if ("screen".equalsIgnoreCase(medium) || "handheld".equalsIgnoreCase(medium) || "all".equalsIgnoreCase(medium)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Valid media.
	 *
	 * @param media the media
	 * @return true, if successful
	 */
	private boolean validMedia(final String media) {
		return media == null || media.trim().isEmpty() || media.toLowerCase().contains("all") || media.toLowerCase().contains("screen");
	}
}
