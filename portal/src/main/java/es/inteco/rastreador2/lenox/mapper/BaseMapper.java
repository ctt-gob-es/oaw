package es.inteco.rastreador2.lenox.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase BaseMapper.
 * Clase abstracta padre de la que heredarán todas las clases Mapper
 *
 * @author GPM
 */
public abstract class BaseMapper {

    /**
     * Retorna el objeto que contiene la siguiente tupla del ResultSet.
     *
     * @param rs <b>ResultSet</b>
     * @return <b>Object</b>
     * @throws SQLException SQLException
     */
    public abstract Object extract(ResultSet rs) throws SQLException;

}
