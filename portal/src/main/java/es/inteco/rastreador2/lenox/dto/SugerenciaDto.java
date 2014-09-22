package es.inteco.rastreador2.lenox.dto;

/**
 * Dto de Sugerencia.
 *
 * @author Pedro
 */

public class SugerenciaDto extends BaseDto {
    /**
     * Campo idSugerencia.
     */
    private Integer idSugerencia;
    /**
     * Campo alternativa.
     */
    private String alternativa;
    /**
     * Campo descripcion de la sugerencia.
     */
    private String descripcion;
    /**
     * Campo idTermino.
     */
    private Integer idTermino;
    /**
     * Campo r.
     */
    private Boolean r;

    /**
     * Obtiene el valor del campo idResultado.<br>
     *
     * @return el campo idResultado
     */
    public Integer getIdSugerencia() {
        return idSugerencia;
    }

    /**
     * Fija el valor para el campo idSugerencia.<br>
     *
     * @param idSugerencia el vlor de idSugerencia a fijar
     */
    public void setIdSugerencia(Integer idSugerencia) {
        this.idSugerencia = idSugerencia;
    }

    /**
     * Obtiene el valor del campo idResultado.<br>
     *
     * @return el campo idResultado
     */
    public String getAlternativa() {
        return alternativa;
    }

    /**
     * Fija el valor para el campo alternativa.<br>
     *
     * @param alternativa el vlor de alternativa a fijar
     */
    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }

    /**
     * Obtiene el valor del campo descripcion.<br>
     *
     * @return el campo descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Fija el valor para el campo descripcion.<br>
     *
     * @param descripcion el vlor de descripcion a fijar
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * Fija el valor para el campo idTermino.<br>
     *
     * @param idTermino el vlor de idTermino a fijar
     */
    public void setIdTermino(Integer idTermino) {
        this.idTermino = idTermino;
    }

    /**
     * Obtiene el valor del campo r.<br>
     *
     * @return el campo r
     */
    public Boolean getR() {
        return r;
    }

    /**
     * Fija el valor para el campo r.<br>
     *
     * @param r el vlor de r a fijar
     */
    public void setR(Boolean r) {
        this.r = r;
    }

}
