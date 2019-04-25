package es.inteco.intav.datos;

/**
 * Created by mikunis on 10/20/16.
 */
public class CSSDTO {

    private final String url;
    private final String codigo;

    public CSSDTO(final String url, final String codigo) {
        this.url = url;
        this.codigo = codigo;
    }

    public String getUrl() {
        return url;
    }

    public String getCodigo() {
        return codigo;
    }
}
