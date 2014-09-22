package es.inteco.crawler.sexista.modules.informes.dto;

import es.inteco.crawler.sexista.core.dto.impl.BasePagingDtoImpl;

/**
 * Dto de Busqueda de rastreos.
 *
 * @author psanchez
 */

public class RastreosSearchDto extends BasePagingDtoImpl {

    /**
     * Campo fechaDesde.
     */
    private String fechaDesde;

    /**
     * Campo fechaHasta.
     */
    private String fechaHasta;

    /**
     * Campo nombreRastreo.
     */
    private String nombreRastreo;

    /**
     * Campo url.
     */
    private String url;
    /**
     * Campo usuario.
     */
    private String usuario;


    /**
     * Campo gravedad.
     */
    private String gravedad;

    /**
     * Obtiene el valor del campo fechaDesde.<br>
     *
     * @return el campo fechaDesde
     */
    public String getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Obtiene el valor del campo fechaHasta.<br>
     *
     * @return el campo fechaHasta
     */
    public String getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Obtiene el valor del campo nombreRastreo.<br>
     *
     * @return el campo nombreRastreo
     */
    public String getNombreRastreo() {
        return nombreRastreo;
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
     * Obtiene el valor del campo usuario.<br>
     *
     * @return el campo usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Fija el valor para el campo fechaDesde.<br>
     *
     * @param fechaDesde el vlor de fechaDesde a fijar
     */
    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * Fija el valor para el campo fechaHasta.<br>
     *
     * @param fechaHasta el vlor de fechaHasta a fijar
     */
    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * Fija el valor para el campo nombreRastreo.<br>
     *
     * @param nombreRastreo el vlor de nombreRastreo a fijar
     */
    public void setNombreRastreo(String nombreRastreo) {
        this.nombreRastreo = nombreRastreo;
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
     * Fija el valor para el campo usuario.<br>
     *
     * @param usuario el vlor de usuario a fijar
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el valor del campo gravedad.<br>
     *
     * @return el campo gravedad
     */
    public String getGravedad() {
        return gravedad;
    }

    /**
     * Fija el valor para el campo gravedad.<br>
     *
     * @param gravedad el valor de gravedad a fijar
     */
    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }


}
