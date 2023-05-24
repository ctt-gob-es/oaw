package es.inteco.rastreador2.actionform.basic.service;

/**
 * Created by mikunis on 1/17/17.
 */
public enum BasicServiceAnalysisType {
	/** The url. */
	URL("url"),
	/** The codigo fuente. */
	CODIGO_FUENTE("código_fuente"),
	/** The codigo fuente multiple. */
	CODIGO_FUENTE_MULTIPLE("multi_source_code"),
	/** The lista urls. */
	LISTA_URLS("lista_urls"),
	/** Mix */
	MIXTO("mixto");

	/** The label. */
	private final String label;

	/**
	 * Instantiates a new basic service analysis type.
	 *
	 * @param label the label
	 */
	BasicServiceAnalysisType(final String label) {
		this.label = label;
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Parses the string.
	 *
	 * @param label the label
	 * @return the basic service analysis type
	 */
	public static BasicServiceAnalysisType parseString(final String label) {
		if ("url".equalsIgnoreCase(label)) {
			return URL;
		} else if ("código_fuente".equalsIgnoreCase(label)) {
			return CODIGO_FUENTE;
		} else if ("lista_urls".equalsIgnoreCase(label)) {
			return LISTA_URLS;
		} else if ("multi_source_code".equalsIgnoreCase(label)) {
			return CODIGO_FUENTE_MULTIPLE;
		} else if ("mixto".equalsIgnoreCase(label)) {
			return MIXTO;
		} else {
			return URL;
		}
	}
}
