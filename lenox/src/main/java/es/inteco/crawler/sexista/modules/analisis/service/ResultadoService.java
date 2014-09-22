package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.ResultadoDao;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;

import java.sql.Connection;

/**
 * Servicio de tratamiento de la tabla resultados.
 */
public class ResultadoService {

    /**
     * Inyeccion del dao de resultados.
     */
    private ResultadoDao dao = null;

    /**
     * Insertamos el resultado.
     * <p/>
     * 1- Invocamos la insercion del dao
     * : dao.insert(resultadoDto);
     *
     * @param resultadoDto - Datos a insertar
     * @param con          - Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void insertResultado(ResultadoDto resultadoDto, Connection con) throws BusinessException {
        dao.insert(resultadoDto, con);
    }

    /**
     * Eliminamos los registros de resultados.
     * <p/>
     * 1- Invicamos a la eliminacion del dao
     * : dao.delete(resultadoDto);
     *
     * @param resultadoDto - Datos necesarios para la eliminacion de rastreos
     * @throws BusinessException BusinessException
     */
    public void deleteResultado(ResultadoDto resultadoDto) throws BusinessException {
        dao.delete(resultadoDto);
    }

    /**
     * Obtiene el valor del campo dao.<br>
     *
     * @return el campo dao
     */
    public ResultadoDao getDao() {
        return dao;
    }

    /**
     * Fija el valor para el campo dao.<br>
     *
     * @param dao el vlor de dao a fijar
     */
    public void setDao(ResultadoDao dao) {
        this.dao = dao;
    }

}
