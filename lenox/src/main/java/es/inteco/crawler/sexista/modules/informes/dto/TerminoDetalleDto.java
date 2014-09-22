package es.inteco.crawler.sexista.modules.informes.dto;

import es.inteco.crawler.sexista.core.dto.impl.BaseDto;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.dto.SugerenciaDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto de Termino de detalle.
 *
 * @author Pedro
 */

public class TerminoDetalleDto extends BaseDto {

    public TerminoDetalleDto() {
        this.contextos = new ArrayList<ResultadoDto>();
        this.numOcurrencias = 0;
    }

    /**
     * Campo nombre.
     */
    private String nombre;

    /**
     * Campo idTermino.
     */
    private Integer idTermino;

    /**
     * Campo numOcurrencias.
     */
    private Integer numOcurrencias;

    /**
     * Campo idRastreo.
     */
    private Long idRastreo;

    /**
     * Listado de ResultadoDto.
     */
    private List<ResultadoDto> contextos;

    /**
     * Listado de SugerenciaDto.
     */
    private List<SugerenciaDto> alternativas;


    /**
     * Campo gravedad.
     */
    private String gravedad;

    /**
     * Obtiene el valor del campo alternativas.<br>
     *
     * @return el campo alternativas
     */
    public List<SugerenciaDto> getAlternativas() {
        return alternativas;
    }

    /**
     * Obtiene el valor del campo contextos.<br>
     *
     * @return el campo contextos
     */
    public List<ResultadoDto> getContextos() {
        return contextos;
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
     * Obtiene el valor del campo nombre.<br>
     *
     * @return el campo nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el valor del campo numOcurrencias.<br>
     *
     * @return el campo numOcurrencias
     */
    public Integer getNumOcurrencias() {
        return numOcurrencias;
    }

    /**
     * Fija el valor para el campo alternativas.<br>
     *
     * @param alternativas el vlor de alternativas a fijar
     */
    public void setAlternativas(List<SugerenciaDto> alternativas) {
        this.alternativas = alternativas;
    }

    /**
     * Fija el valor para el campo contextos.<br>
     *
     * @param contextos el vlor de contextos a fijar
     */
    public void setContextos(List<ResultadoDto> contextos) {
        this.contextos = contextos;
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
     * Fija el valor para el campo idtermino.<br>
     *
     * @param idtermino el vlor de idtermino a fijar
     */
    public void setIdTermino(Integer idtermino) {
        this.idTermino = idtermino;
    }

    /**
     * Fija el valor para el campo nombre.<br>
     *
     * @param nombre el vlor de nombre a fijar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Fija el valor para el campo numOcurrencias.<br>
     *
     * @param numOcurrencias el vlor de numOcurrencias a fijar
     */
    public void setNumOcurrencias(Integer numOcurrencias) {
        this.numOcurrencias = numOcurrencias;
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
