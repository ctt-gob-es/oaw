package es.inteco.rastreador2.lenox.dto;

import es.inteco.crawler.sexista.modules.commons.dto.RastreoDto;


/**
 * Dto de Rastreo extendido que hereda del Dto de Rastreo.
 *
 * @author psanchez
 */

public class RastreoExtDto extends RastreoDto {

    /**
     * Campo numTerminosLocalizados.
     */
    private Integer numTerminosLocalizados;

    /**
     * Campo loginUsuario.
     */
    private String loginUsuario;

    /**
     * Campo numTerminosOcurrentes.
     */
    private Integer numTerminosOcurrentes;

    /**
     * Campo numTerminosGravedad.
     */
    private Integer numTerminosGravedad;

    /**
     * Campo numTerminosPrioridadAlta.
     */
    private Integer numTerminosPrioridadAlta;

    /**
     * Campo numTerminosPrioridadMedia.
     */
    private Integer numTerminosPrioridadMedia;

    /**
     * Campo numTerminosPrioridadBaja.
     */
    private Integer numTerminosPrioridadBaja;

    /**
     * Campo entidad
     */
    private String entidad;

    /**
     * Campo gravedad.
     */
    private String gravedad;

    public RastreoExtDto() {
        this.numTerminosGravedad = 0;
        this.numTerminosLocalizados = 0;
        this.numTerminosOcurrentes = 0;
        this.numTerminosPrioridadAlta = 0;
        this.numTerminosPrioridadBaja = 0;
        this.numTerminosPrioridadMedia = 0;
    }


    /**
     * Obtiene el valor del campo loginUsuario.<br>
     *
     * @return el campo loginUsuario
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }

    /**
     * Obtiene el valor del campo numTerminosLocalizados.<br>
     *
     * @return el campo numTerminosLocalizados
     */
    public Integer getNumTerminosLocalizados() {
        return numTerminosLocalizados;
    }

    /**
     * Obtiene el valor del campo numTerminosOcurrentes.<br>
     *
     * @return el campo numTerminosOcurrentes
     */
    public Integer getNumTerminosOcurrentes() {
        return numTerminosOcurrentes;
    }

    /**
     * Fija el valor para el campo loginUsuario.<br>
     *
     * @param loginUsuario el vlor de loginUsuario a fijar
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    /**
     * Fija el valor para el campo numTerminosLocalizados.<br>
     *
     * @param numTerminosLocalizados el vlor de numTerminosLocalizados a fijar
     */
    public void setNumTerminosLocalizados(Integer numTerminosLocalizados) {
        this.numTerminosLocalizados = numTerminosLocalizados;
    }

    /**
     * Fija el valor para el campo numTerminosOcurrentes.<br>
     *
     * @param numTerminosOcurrentes el vlor de numTerminosOcurrentes a fijar
     */
    public void setNumTerminosOcurrentes(Integer numTerminosOcurrentes) {
        this.numTerminosOcurrentes = numTerminosOcurrentes;
    }

    /**
     * Obtiene el valor del campo numTerminosGravedad.<br>
     *
     * @return el campo numTerminosGravedad
     */
    public Integer getNumTerminosGravedad() {
        return numTerminosGravedad;
    }

    /**
     * Fija el valor para el campo numTerminosGravedad.<br>
     *
     * @param numTerminosGravedad el valor de numTerminosGravedad a fijar
     */
    public void setNumTerminosGravedad(Integer numTerminosGravedad) {
        this.numTerminosGravedad = numTerminosGravedad;
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

    /**
     * Obtiene el valor del campo numTerminosPrioridadAlta.<br>
     *
     * @return el campo numTerminosPrioridadAlta
     */
    public Integer getNumTerminosPrioridadAlta() {
        return numTerminosPrioridadAlta;
    }

    /**
     * Fija el valor para el campo numTerminosPrioridadAlta.<br>
     *
     * @param numTerminosPrioridadAlta el valor de numTerminosPrioridadAlta a fijar
     */
    public void setNumTerminosPrioridadAlta(Integer numTerminosPrioridadAlta) {
        this.numTerminosPrioridadAlta = numTerminosPrioridadAlta;
    }

    /**
     * Obtiene el valor del campo numTerminosPrioridadMedia.<br>
     *
     * @return el campo numTerminosPrioridadMedia
     */
    public Integer getNumTerminosPrioridadMedia() {
        return numTerminosPrioridadMedia;
    }

    /**
     * Fija el valor para el campo numTerminosPrioridadMedia.<br>
     *
     * @param numTerminosPrioridadMedia el valor de numTerminosPrioridadMedia a fijar
     */
    public void setNumTerminosPrioridadMedia(Integer numTerminosPrioridadMedia) {
        this.numTerminosPrioridadMedia = numTerminosPrioridadMedia;
    }

    /**
     * Obtiene el valor del campo numTerminosPrioridadBaja.<br>
     *
     * @return el campo numTerminosPrioridadBaja
     */
    public Integer getNumTerminosPrioridadBaja() {
        return numTerminosPrioridadBaja;
    }

    /**
     * Fija el valor para el campo numTerminosPrioridadBaja.<br>
     *
     * @param numTerminosPrioridadBaja el valor de numTerminosPrioridadBaja a fijar
     */
    public void setNumTerminosPrioridadBaja(Integer numTerminosPrioridadBaja) {
        this.numTerminosPrioridadBaja = numTerminosPrioridadBaja;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

}
