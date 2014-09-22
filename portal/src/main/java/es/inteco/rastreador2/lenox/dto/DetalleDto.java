package es.inteco.rastreador2.lenox.dto;

import java.util.List;

/**
 * Dto de Detalle.
 *
 * @author psanchez
 */

public class DetalleDto extends BaseDto {

    /**
     * Campo RastreoExtDto.
     */
    private RastreoExtDto rastreo;

    /**
     * Campo URL
     */
    private String url;

    /**
     * Listado de TerminoDetalleDto.
     */
    private List<TerminoDetalleDto> detalleTerminos;


    /**
     * Campo gravedad.
     */
    private String gravedad;


    public DetalleDto() {
        if (rastreo == null) {
            rastreo = new RastreoExtDto();
        }
    }

    /**
     * Obtiene el valor del campo detalleTerminos.<br>
     *
     * @return el campo detalleTerminos
     */
    public List<TerminoDetalleDto> getDetalleTerminos() {
        return detalleTerminos;
    }

    /**
     * Obtiene el valor del campo rastreo.<br>
     *
     * @return el campo rastreo
     */
    public RastreoExtDto getRastreo() {
        return rastreo;
    }

    /**
     * Fija el valor para el campo detalleTerminos.<br>
     *
     * @param detalleTerminos el vlor de detalleTerminos a fijar
     */
    public void setDetalleTerminos(List<TerminoDetalleDto> detalleTerminos) {
        this.detalleTerminos = detalleTerminos;
    }

    /**
     * Fija el valor para el campo rastreo.<br>
     *
     * @param rastreo el vlor de rastreo a fijar
     */
    public void setRastreo(RastreoExtDto rastreo) {
        this.rastreo = rastreo;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
