package es.inteco.crawler.sexista.modules.commons.dto;

/**
 * Clase CategoryExcepcionDto.
 *
 * @author jfiz
 */
public class CategoryExcepcionDto extends CommonExcepcionDto {

    /**
     * Identificador de la categoria a la que pertenece.
     */
    private int idCategoria;

    /**
     * Obtiene el valor del campo idCategoria.<br>
     *
     * @return el campo idCategoria
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Fija el valor para el campo idCategoria.<br>
     *
     * @param idCategoria el valor de idCategoria a fijar
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

}
