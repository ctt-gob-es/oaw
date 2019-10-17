package es.inteco.rastreador2.actionform.basic.service;

/**
 * Created by mikunis on 1/17/17.
 */
public enum BasicServiceAnalysisType {
    
    /** The url. */
    URL("url"),
    
    /** The codigo fuente. */
    CODIGO_FUENTE("código_fuente"),
    
    /** The lista urls. */
    LISTA_URLS("lista_urls");

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
        } else {
            return URL;
        }
    }
}
