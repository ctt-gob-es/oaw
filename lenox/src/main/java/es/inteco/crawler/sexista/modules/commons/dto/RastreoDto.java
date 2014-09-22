package es.inteco.crawler.sexista.modules.commons.dto;

import es.inteco.crawler.sexista.modules.commons.util.Crono;

/**
 * Clase RastreoDto.
 *
 * @author jbernal
 */
public class RastreoDto {
    /**
     * Campo crono.
     */
    private Crono crono = null;

    /**
     * Campo idRastreo.
     */
    private Long idRastreo;

    /**
     * Campo usuario.
     */
    private String usuario;

    /**
     * Campo fecha.
     */
    private String fecha;

    /**
     * Campo tiempo.
     */
    private Long tiempo;

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
     * Obtiene el valor del campo tiempo.<br>
     *
     * @return el campo tiempo
     */
    public Long getTiempo() {
        return tiempo;
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
     * Fija el valor para el campo tiempo.<br>
     *
     * @param tiempo el vlor de tiempo a fijar
     */
    public void setTiempo(Long tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Constructor por defecto.<br>
     */
    public RastreoDto() {
        crono = new Crono();
        crono.inicializa();
    }

    /**
     * M&eacute;todo getTiempoCrono.
     * Calcula el tiempo desde que se creo el Dto
     *
     * @return Tiempo desde que se creo
     */
    public Long getTiempoCrono() {
        Long tiempoCrono = 0L;
        if (null != crono) {
            tiempoCrono = crono.getTime();
        }
        return tiempoCrono;
    }

}
