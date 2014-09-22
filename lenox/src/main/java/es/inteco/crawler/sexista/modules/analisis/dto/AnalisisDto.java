package es.inteco.crawler.sexista.modules.analisis.dto;

/**
 * Dto de Analisis.
 * <p/>
 * Contendrá los datos iniciales del analisis.
 */
public class AnalisisDto {

    /**
     * Identificador de rastreo.
     */
    private Long idRastreo;

    /**
     * Login de usuario.
     */
    private String usuario;

    /**
     * Fecha del Analisis.
     */
    private String fecha;

    /**
     * URL que se está analizando.
     */
    private String url;

    /**
     * Texto de la url.
     */
    private String contenido;

    /**
     * Obtiene el valor del campo idRastreo.<br>
     *
     * @return el campo idRastreo
     */
    public Long getIdRastreo() {
        return idRastreo;
    }

    /**
     * Obtiene el valor del campo usuario.<br>
     *
     * @return el campo usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene el valor del campo fecha.<br>
     *
     * @return el campo fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Obtiene el valor del campo url.<br>
     *
     * @return el campo url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Obtiene el valor del campo contenido.<br>
     *
     * @return el campo contenido
     */
    public String getContenido() {
        return contenido;
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
     * Fija el valor para el campo usuario.<br>
     *
     * @param usuario el vlor de usuario a fijar
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Fija el valor para el campo fecha.<br>
     *
     * @param fecha el vlor de fecha a fijar
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Fija el valor para el campo url.<br>
     *
     * @param url el vlor de url a fijar
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Fija el valor para el campo contenido.<br>
     *
     * @param contenido el vlor de contenido a fijar
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


}
