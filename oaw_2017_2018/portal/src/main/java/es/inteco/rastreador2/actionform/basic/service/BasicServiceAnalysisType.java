package es.inteco.rastreador2.actionform.basic.service;

/**
 * Created by mikunis on 1/17/17.
 */
public enum BasicServiceAnalysisType {
    URL("url"),
    CODIGO_FUENTE("código_fuente"),
    LISTA_URLS("lista_urls");

    private final String label;
    

    BasicServiceAnalysisType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

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
