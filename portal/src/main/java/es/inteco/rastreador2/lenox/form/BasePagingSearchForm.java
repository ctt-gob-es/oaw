package es.inteco.rastreador2.lenox.form;

import es.inteco.rastreador2.lenox.dto.BasePagingDto;

/**
 * Form del que heredarán los formularios con búsquedas paginadas.
 *
 * @author GPM
 * @version 1.0
 */
public abstract class BasePagingSearchForm extends BaseForm {

    /**
     * Ruta que contiene los datos de la pagina y ordenación del listado.
     */
    private String pathToPaginatedList;

    /**
     * Id de la tabla DisplayTag con el listado.
     */
    private String displayTagId;

    //Getter / Setter

    /**
     * Método getPagingSearchDto.<br>
     * Este método deve sobreescribirse por si es necesaria
     * la vuelta a un listado manteniendo la paginación.
     *
     * @return PagingDto
     */
    public abstract BasePagingDto getPagingSearchDto();

    /**
     * Getter.
     *
     * @return String
     */
    public String getPathToPaginatedList() {
        return pathToPaginatedList;
    }

    /**
     * Setter.
     *
     * @param pathToPaginatedList the pathToPaginatedList
     */
    public void setPathToPaginatedList(String pathToPaginatedList) {
        this.pathToPaginatedList = pathToPaginatedList;
    }

    /**
     * Getter.
     *
     * @return String
     */
    public String getDisplayTagId() {
        return displayTagId;
    }

    /**
     * Setter.
     *
     * @param displayTagId the displayTagId
     */
    public void setDisplayTagId(String displayTagId) {
        this.displayTagId = displayTagId;
    }

}
