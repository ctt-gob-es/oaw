package es.inteco.crawler.sexista.modules.commons.dto;

/**
 * Clase LocalExcepcionDto.
 *
 * @author jbernal
 */
public class LocalExcepcionDto extends CommonExcepcionDto {

    /**
     * Identificador del termino con quien se encuentra relacionado.
     */
    private int idTermino;

    /**
     * Obtiene el valor del campo idTermino.<br>
     *
     * @return el campo idTermino
     */
    public int getIdTermino() {
        return idTermino;
    }

    /**
     * Fija el valor para el campo idTermino.<br>
     *
     * @param idTermino el vlor de idTermino a fijar
     */
    public void setIdTermino(int idTermino) {
        this.idTermino = idTermino;
    }

}
