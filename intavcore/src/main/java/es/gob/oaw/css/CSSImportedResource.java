package es.gob.oaw.css;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.w3c.dom.Element;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.utils.EvaluatorUtils;

/**
 * Clase para representar un recurso CSS que se ha incluido mediante @import.
 */
public class CSSImportedResource implements CSSResource {
	/** The content. */
	private final String content;
	/** The source. */
	private String source;

	/**
	 * Instantiates a new CSS imported resource.
	 *
	 * @param baseUrl   the base url
	 * @param importUrl the import url
	 */
	public CSSImportedResource(final String baseUrl, final String importUrl) {
		try {
			source = new URL(new URL(baseUrl), importUrl).toString();
		} catch (MalformedURLException e) {
			source = importUrl;
		}
		content = loadStyleSheet(baseUrl, importUrl);
	}

	/**
	 * Gets the HTML element.
	 *
	 * @return the HTML element
	 */
	@Override
	public Element getHTMLElement() {
		return null;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	@Override
	public String getContent() {
		return content;
	}

	/**
	 * Checks if is inline.
	 *
	 * @return true, if is inline
	 */
	@Override
	public boolean isInline() {
		return false;
	}

	/**
	 * Gets the string source.
	 *
	 * @return the string source
	 */
	@Override
	public String getStringSource() {
		return source;
	}

	/**
	 * Checks if is imported.
	 *
	 * @return true, if is imported
	 */
	@Override
	public boolean isImported() {
		return true;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return source.hashCode();
	}

	/**
	 * Load style sheet.
	 *
	 * @param baseUrl   the base url
	 * @param importUrl the import url
	 * @return the string
	 */
	private String loadStyleSheet(final String baseUrl, final String importUrl) {
		try {
			final URL cssUrl;
			if (URI.create(importUrl).isAbsolute()) {
				cssUrl = new URL(importUrl);
			} else {
				cssUrl = new URL(new URL(baseUrl), importUrl);
			}
			final HttpURLConnection connection = EvaluatorUtils.getConnection(cssUrl.toString(), "GET", true);
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				return StringUtils.getContentAsString(connection.getInputStream());
			}
		} catch (Exception e) {
			Logger.putLog("Error al cargar la hoja de estilo importada " + importUrl, EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING, e);
		}
		return "";
	}

	/**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CSSResource)) {
			return false;
		}
		CSSResource that = (CSSResource) o;
		return source.equals(that.getStringSource());
	}
}
