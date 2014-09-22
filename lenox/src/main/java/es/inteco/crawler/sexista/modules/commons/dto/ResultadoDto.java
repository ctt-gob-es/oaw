package es.inteco.crawler.sexista.modules.commons.dto;

/**
 * Clase ResultadoDto.
 *
 * @author jbernal
 */
public class ResultadoDto {

    /**
     * Campo idResultado.
     */
    private Integer idResultado;

    /**
     * Campo idRastreo.
     */
    private Long idRastreo;

    /**
     * Campo idTermino.
     */
    private Integer idTermino;

    /**
     * Campo termino.
     */
    private String termino;

    /**
     * Campo contexto.
     */
    private String contexto;

    /**
     * Campo gravedad.
     */
    private String gravedad;
    /**
     * Campo urlTermino.
     */
    private String urlTermino;

    /**
     * Indica si se encontro en singular el termino.
     */
    private boolean inSingular;

    /**
     * Obtiene el valor del campo idResultado.<br>
     *
     * @return el campo idResultado
     */
    public Integer getIdResultado() {
        return idResultado;
    }

    /**
     * Obtiene el valor del campo idRastreo.<br>
     *
     * @return el campo idRastreo
     */
    public Long getIdRastreo() {
        return idRastreo;
    }

    /**
     * Obtiene el valor del campo idTermino.<br>
     *
     * @return el campo idTermino
     */
    public Integer getIdTermino() {
        return idTermino;
    }

    /**
     * Obtiene el valor del campo contexto.<br>
     *
     * @return el campo contexto
     */
    public String getContexto() {
        return contexto;
    }

    /**
     * Obtiene el valor del campo urlTermino.<br>
     *
     * @return el campo urlTermino
     */
    public String getUrlTermino() {
        return urlTermino;
    }

    /**
     * Fija el valor para el campo idResultado.<br>
     *
     * @param idResultado el vlor de idResultado a fijar
     */
    public void setIdResultado(Integer idResultado) {
        this.idResultado = idResultado;
    }

    /**
     * Fija el valor para el campo idRastreo.<br>
     *
     * @param idRastreo el vlor de idRastreo a fijar
     */
    public void setIdRastreo(Long idRastreo) {
        this.idRastreo = idRastreo;
    }

    /**
     * Fija el valor para el campo idTermino.<br>
     *
     * @param idTermino el vlor de idTermino a fijar
     */
    public void setIdTermino(Integer idTermino) {
        this.idTermino = idTermino;
    }

    /**
     * Fija el valor para el campo contexto.<br>
     *
     * @param contexto el vlor de contexto a fijar
     */
    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    /**
     * Fija el valor para el campo urlTermino.<br>
     *
     * @param urlTermino el vlor de urlTermino a fijar
     */
    public void setUrlTermino(String urlTermino) {
        this.urlTermino = urlTermino;
    }

    /**
     * Obtiene el valor del campo inSingular.<br>
     *
     * @return el campo inSingular
     */
    public boolean isInSingular() {
        return inSingular;
    }

    /**
     * Fija el valor para el campo inSingular.<br>
     *
     * @param inSingular el vlor de inSingular a fijar
     */
    public void setInSingular(boolean inSingular) {
        this.inSingular = inSingular;
    }

    /**
     * @param gravedad the gravedad to set
     */
    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    /**
     * @return the gravedad
     */
    public String getGravedad() {
        return gravedad;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }
}
