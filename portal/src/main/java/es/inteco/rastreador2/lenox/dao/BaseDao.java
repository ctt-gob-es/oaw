package es.inteco.rastreador2.lenox.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.rastreador2.lenox.dto.BasePagingDto;
import es.inteco.rastreador2.lenox.mapper.BaseMapper;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase BaseDao.
 * Clase DAO Padre de la que heredan todas las demás clases DAO
 *
 * @author psanchez
 */
public class BaseDao {

    /**
     * Logger.
     */
    private Logger log = Logger.getRootLogger();

    /**
     * Obtiene el valor del campo log.<br>
     *
     * @return el campo log
     */
    public Logger getLog() {
        return log;
    }

    /**
     * Fija el valor para el campo log.<br>
     *
     * @param log el vlor de log a fijar
     */
    public void setLog(Logger log) {
        this.log = log;
    }

    /**
     * Ejecuta una consulta a base de datos teniendo haciendo una paginación
     * mediante el uso de querys preparadas para tal efecto. Este método es
     * para bases de datos MySQL
     *
     * @param sql        de tipo<b>String</b>. Select con los campos y parámetros que
     *                   se quieran
     * @param parameters de tipo<b>ArrayList</b>. Lista de los valores que se
     *                   sustituirán en cada parámetro
     * @param dto        Dto de búsqueda
     * @param mapper     Mapper de construcción de resultados
     * @return <b>BasePagingDto</b> con los datos obtenidos
     * @throws Exception Si falla la consulta
     */
    public BasePagingDto executePreparedQueryPag(final String sql, final List<Object> parameters,
                                                 BasePagingDto dto, BaseMapper mapper) throws Exception {

        Connection connection = null;
        ResultSet rs = null;

        //ResultSet rsTotal = null;
        PreparedStatement st = null;

        StringBuffer sqlPag = new StringBuffer("select 0 as linea, s.* from (");
        sqlPag.append(sql);
        sqlPag.append(") s limit ? , ?");

        try {
            connection = ConexionBBDD.conectar();
            st = connection.prepareStatement(sqlPag.toString());

            int i = 0;
            if (null != parameters) {
                for (i = 0; i < parameters.size(); i++) {
                    st.setObject(i + 1, parameters.get(i));
                } // end for
            }

            //Los que queremos saltarnos
            st.setObject(++i, dto.getStartRecord());
            //Tamaño de página
            st.setObject(++i, dto.getMaxRecords());

            rs = st.executeQuery();

            List<Object> records = new ArrayList<Object>();

            while (rs.next()) {
                records.add(mapper.extract(rs));
            }

            dto.setRecords(records);

            Long totalLista = this.calculoTotalLista(sql, parameters, connection);

            if (null != totalLista) {
                dto.setTotalRecords(totalLista.intValue());
            } // end if
        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(connection, st, rs);
        }

        return dto;
    } // end executePreparedQueryPag()

    /**
     * Método que se usa para obtener el número de registros de una consulta.
     *
     * @param sql        de tipo<b>String</b>. Consulta a base de datos
     * @param parameters de tipo<b>ArrayList</b>. Parametros pasados
     * @param connection de tipo<b>Connection</b>. Conexión a la base de datos
     * @return <b>Long</b> Número de registros de la consulta
     * @throws Exception Exception
     */
    protected Long calculoTotalLista(final String sql, final List<Object> parameters,
                                     final Connection connection) throws Exception {
        Long total = null;
        ResultSet rsTotal = null;
        PreparedStatement stTotal = null;

        try {
            //Cálculo del totalLista:
            StringBuffer sqlTotal = new StringBuffer("select count(*) totalLista from ( ");
            sqlTotal.append(sql);
            sqlTotal.append(" ) s");

            stTotal = connection.prepareStatement(sqlTotal.toString());

            if (null != parameters) {
                for (int i = 0; i < parameters.size(); i++) {
                    stTotal.setObject((i + 1), parameters.get(i));
                } // end for
            }

            rsTotal = stTotal.executeQuery();

            if (rsTotal.next()) {
                total = Long.valueOf(rsTotal.getLong("totalLista"));
            } // end if

        } catch (SQLException e) {
            getLog().error(e.getMessage());
            throw e;
        } finally {
            if (stTotal != null) {
                stTotal.close();
            }
            if (rsTotal != null) {
                rsTotal.close();
            }
        }
        return total;
    } // end caculoTotalLista()

}
