package es.inteco.crawler.sexista.core.dto.impl;

import es.inteco.crawler.sexista.core.dto.BasePagingDto;
import es.inteco.crawler.sexista.core.util.ConfigUtil;

import java.util.List;

/**
 * Clase para la paginación con DisplayTag.
 *
 * @author GPM
 * @version 1.0
 */
public class BasePagingDtoImpl extends BaseDto implements BasePagingDto {

    /**
     * Tamaño de página por defecto.
     */
    private static final int DEFAULT_PAGE_SIZE = Integer.valueOf(ConfigUtil.getConfiguracion()
            .getProperty("maxRows"));

    /**
     * Registro de inicio.
     */
    private int startRecord;

    /**
     * Máximos registros a mostrar.
     */
    private int maxRecords;

    /**
     * Total de registros a mostrar.
     */
    private int totalRecords;

    /**
     * Order, Asc or Desc (1,2).
     */
    private String order = PROPERTY_ORDER_ASC;

    /**
     * Name of the property to order .
     */
    private String propertyOrder = "";

    /**
     * Registros.
     */
    private List<Object> records;

    /**
     * Parametros de la SQL para paginar.
     */
    private Object[] parameters;

    /**
     * HQL compuesta para paginar.
     */
    private String hql;

    /**
     * Constructor.<br>
     */
    public BasePagingDtoImpl() {
        this.maxRecords = DEFAULT_PAGE_SIZE;
    }

    /**
     * Constructor.<br>
     *
     * @param maxRecords the maxRecords
     */
    public BasePagingDtoImpl(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    /**
     * Devuelve el número de registros máximos por pantalla.
     *
     * @return int
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getMaxRecords()
     */
    public int getMaxRecords() {
        return maxRecords;
    }

    /**
     * Setter.
     *
     * @param maxRecords the maxRecords
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setMaxRecords(int)
     */
    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    /**
     * Getter.
     *
     * @return List
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getRecords()
     */
    public List<Object> getRecords() {
        return records;
    }

    /**
     * Setter.
     *
     * @param records the records
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setRecords(java.util.List)
     */
    public void setRecords(List<Object> records) {
        this.records = records;
    }

    /**
     * Getter.
     *
     * @return int
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getStartRecord()
     */
    public int getStartRecord() {
        return startRecord;
    }

    /**
     * Setter.
     *
     * @param startRecord the startRecord
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setStartRecord(int)
     */
    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    /**
     * Getter.
     *
     * @return int
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getTotalRecords()
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * Setter.
     *
     * @param totalRecords the totalRecords
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setTotalRecords(int)
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * Getter.
     *
     * @return String
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getOrder()
     */
    public String getOrder() {
        return order;
    }

    /**
     * Setter.
     *
     * @param order the order
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setOrder(java.lang.String)
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Getter.
     *
     * @return String
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getPropertyOrder()
     */
    public String getPropertyOrder() {
        return propertyOrder;
    }

    /**
     * Setter.
     *
     * @param propertyOrder the propertyOrder
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto
     *      #setPropertyOrder(java.lang.String)
     */
    public void setPropertyOrder(String propertyOrder) {
        this.propertyOrder = propertyOrder;
    }

    /**
     * Getter.
     *
     * @return String
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getActualPage()
     */
    public String getActualPage() {
        int numPag = startRecord / maxRecords + 1;
        if (startRecord % maxRecords != 0) {
            numPag++;
        }

        return String.valueOf(numPag);
    }

    /**
     * Getter.
     *
     * @return Object
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getParameters()
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * Setter.
     *
     * @param parameters the parameters
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto
     *      #setParameters(java.lang.Object)
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Getter.
     *
     * @return String
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#getHql()
     */
    public String getHql() {
        return hql;
    }

    /**
     * Setter.
     *
     * @param hql the hql
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto#setHql(java.lang.String)
     */
    public void setHql(String hql) {
        this.hql = hql;
    }

    /**
     * Método para añadir orden a la HQL en caso de que fuera necesario.
     *
     * @param hql the hql
     * @see es.inteco.crawler.sexista.core.dto.BasePagingDto
     *      #appendOrderToQuery(java.lang.StringBuffer,java.lang.String)
     */
    public void appendOrderToQuery(StringBuffer hql) {
        if ((order != null)
                && ((order.equals(PROPERTY_ORDER_ASC) || order.equals(PROPERTY_ORDER_DESC)))
                && (propertyOrder != null) && (propertyOrder.length() > 0)) {

            String[] orders = propertyOrder.split(",");

            hql.append(" order by ").append(orders[0]);

            if (order.equals(PROPERTY_ORDER_ASC)) {
                hql.append(" asc");
            } else {
                hql.append(" desc");
            }

            if (orders.length > 1) {
                for (int i = 1; i < orders.length; i++) {
                    hql.append(", ").append(orders[i]);

                    if (order.equals(PROPERTY_ORDER_ASC)) {
                        hql.append(" asc");
                    } else {
                        hql.append(" desc");
                    }
                }
            }
        }
        //Seteamos el valor en el dto
        if (null != hql) {
            this.hql = hql.toString();
        } else {
            this.hql = "";
        }
    }
}
