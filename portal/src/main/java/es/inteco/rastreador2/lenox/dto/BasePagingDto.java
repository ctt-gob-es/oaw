package es.inteco.rastreador2.lenox.dto;

import java.util.List;

/**
 * Interfaz para la paginación con DisplayTag.
 *
 * @author GPM
 * @version 1.0
 */
public interface BasePagingDto {

    /**
     * String PROPERTY_ORDER_ASC.
     */
    public static final String PROPERTY_ORDER_ASC = "1";

    /**
     * String PROPERTY_ORDER_DESC.
     */
    public static final String PROPERTY_ORDER_DESC = "2";

    /**
     * Devuelve el número máximo de registros por página.
     *
     * @return int
     */
    public int getMaxRecords();

    /**
     * Setea el numero máximo de registros por página.
     *
     * @param maxRecords the maxRecords
     */
    public void setMaxRecords(int maxRecords);

    /**
     * Devuelve los resultados.
     *
     * @return List
     */
    public List<Object> getRecords();

    /**
     * Setea los resultados.
     *
     * @param records the records
     */
    public void setRecords(List<Object> records);

    /**
     * Devuelve la posición del resultado inicial respecto del total.
     *
     * @return int
     */
    public int getStartRecord();

    /**
     * Setea el resultado inicial respecto del total.
     *
     * @param startRecord the startRecord
     */
    public void setStartRecord(int startRecord);

    /**
     * Devuelve el total de resultados.
     *
     * @return int
     */
    public int getTotalRecords();

    /**
     * Setea el total de resultados.
     *
     * @param totalRecords the totalRecords
     */
    public void setTotalRecords(int totalRecords);

    /**
     * Devuelve el tipo de orden.
     *
     * @return String
     */
    public String getOrder();

    /**
     * Setea el tipo de orden.
     *
     * @param order the order
     */
    public void setOrder(String order);

    /**
     * Devuelve sobre que propiedad se ordena.
     *
     * @return String
     */
    public String getPropertyOrder();

    /**
     * Setea sobre que propiedad se ordena.
     *
     * @param propertyOrder the propertyOrder
     */
    public void setPropertyOrder(String propertyOrder);

    /**
     * Devuelve el número de página actual.
     *
     * @return String
     */
    public String getActualPage();

    /**
     * Devuelve los parámetros de la consulta.
     *
     * @return Object
     */
    public Object[] getParameters();

    /**
     * Setea los parámetros de la consulta.
     *
     * @param parameters the parameters
     */
    public void setParameters(Object[] parameters);

    /**
     * Devuelve la consulta.
     *
     * @return String
     */
    public String getHql();

    /**
     * Setea la consulta.
     *
     * @param hql the hql
     */
    public void setHql(String hql);

    /**
     * Método appendOrderToQuery.<br>
     *
     * @param sql StringBuilder al que se le añade "order by
     *            &lt;prefix&gt;.&lt;propertyOrder&gt; asc/desc"
     */
    public void appendOrderToQuery(StringBuffer sql);
}
