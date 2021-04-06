package es.inteco.intav.datos;

/**
 * Created by mikunis on 10/20/16.
 */
public class CSSDTO {

    /** The url. */
    private final String url;
    
    /** The codigo. */
    private final String codigo;

    /**
	 * Instantiates a new cssdto.
	 *
	 * @param url    the url
	 * @param codigo the codigo
	 */
    public CSSDTO(final String url, final String codigo) {
        this.url = url;
        this.codigo = codigo;
    }

    /**
	 * Gets the url.
	 *
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
    public String getCodigo() {
        return codigo;
    }
}
