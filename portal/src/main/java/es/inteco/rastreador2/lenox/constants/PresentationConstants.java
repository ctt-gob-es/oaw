package es.inteco.rastreador2.lenox.constants;

/**
 * Interfaz de constantes en la que guardaremos valores usuales.<br>
 *
 * @author GPM
 * @version 1.0
 */
public interface PresentationConstants {
    //Para los mapeos de struts
    /**
     * String FWD_OK.
     */
    public static final String FWD_OK = "OK";

    /**
     * String FWD_UNEXPECTED_EXCEPTION.
     */
    public static final String FWD_UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";

    /**
     * Nombre del atributo de sesión que tiene los errores.
     */
    public static final String ATTRIBUTE_EXCEPTION_MESSAGE = "ATTRIBUTE_EXCEPTION_MESSAGE";

    /**
     * String FWD_PAGING.
     */
    public static final String FWD_PAGING = "PAGING";

    /**
     * String FWD_OK_RELOAD.
     */
    public static final String FWD_OK_RELOAD = "OK_RELOAD";

    /**
     * String FWD_RETURN.
     */
    public static final String FWD_RETURN = "RETURN";

    //Para excluir en el DisplayTag
    /**
     * String EXCLUDED_PARAMS.
     */
    public static final String EXCLUDED_PARAMS = "ajax displayAjax display2Ajax method newSearch "
            + "firstSearch search pathToPaginatedList";

    //Datos de presentación
    /**
     * String DATE_FORMAT .
     */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    //Constantes de datos
    /**
     * Variable en la que se guarda la ruta para volver a un listado ordenado.
     */
    public static final String PATH_TO_LIST = "pathToList";

    /**
     * Variable en la que se guarda el nombre del formulario a actualizar.
     */
    public static final String SAVED_FORM = "savedForm";

    /**
     * Campos a guardar en un formulario.
     */
    public static final String FIELDS_FORM = "fieldsForm";

    /**
     * PresentationConstants SEPARADOR_CAMPO_VALOR_GUARDADO_FORM.
     */
    public static final String FIELD_SEPARATOR_EQUAL_SAVE_FORM = "0igual0";

    /**
     * PresentationConstants SEPARADOR_CAMPOS_GUARDADO_FORM.
     */
    public static final String FIELD_SEPARATOR_SAVE_FORM = "@siguiente@";

    /**
     * Constante con los caracteres permitidos para las fechas.
     */
    public static final String DATE_CHARACTERS = "0123456789/";

    public static final String LOCALE = "lang";

    public static final String LENOX_HOME = "lenoxHome";

}
