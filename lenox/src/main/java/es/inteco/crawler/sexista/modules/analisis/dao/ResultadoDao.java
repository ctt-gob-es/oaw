package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO de resultado.
 */
public final class ResultadoDao {

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

    //Implementamos patrón singleton para que sólo haya una instancia
    /**
     * Campo resultadoDao.
     */
    static private ResultadoDao resultadoDao = null;

    /**
     * Constructor por defecto.<br>
     */
    private ResultadoDao() {

    }

    /**
     * M&eacute;todo getInstancia.
     *
     * @return ResultadoDao.
     */
    static public ResultadoDao getInstancia() {

        if (resultadoDao == null) {
            resultadoDao = new ResultadoDao();
        }
        return resultadoDao;
    }

    /**
     * Insertamos en la tabla resultados.
     * <p/>
     * 1- Dependiendo de los parametros que llegen en el DTO, ejecutaremos:
     * : if(null != dto.getIdTermino() && null != dto.getContexto()){
     * :   sql = "INSERT INTO SEXISTA_RESULTADOS ( ID_RASTREO, URL_TERMINO, ID_TERMINO, CONTEXTO) values (?, ?, ?, ?)";
     * : }else{
     * :   sql = "INSERT INTO SEXISTA_RESULTADOS ( ID_RASTREO, URL_TERMINO) values (?, ? )";
     * : }
     * <p/>
     * 2- Parametros
     * : ps.setString(1, dto.getIdRastreo());
     * : ps.setString(2, dto.getUrlTermino());
     * : if(null != dto.getIdTermino() && null != dto.getContexto()){
     * :    ps.setInt(3, dto.getIdTermino());
     * :    ps.setString(4, dto.getContexto());
     * : }
     *
     * @param dto - Datos del registro
     * @param con - Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void insert(ResultadoDto dto, Connection con) throws BusinessException {

        PreparedStatement ps = null;
        String sql = "";

        //Diferenciamos entre las dos posibilidades de insercion disponibles
        if (null != dto.getIdTermino() && null != dto.getContexto()) {
            sql = "INSERT INTO SEXISTA_RESULTADOS ( ID_RASTREO, URL_TERMINO, EN_SINGULAR, ID_TERMINO, CONTEXTO) values (?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO SEXISTA_RESULTADOS ( ID_RASTREO, URL_TERMINO, EN_SINGULAR) values (?, ?, ?)";
        }

        //Mostramos en el log la sql a ejecutar
        getLog().info(sql);

        try {

            ps = (PreparedStatement) con.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, dto.getIdRastreo());
            ps.setString(i++, dto.getUrlTermino());
            ps.setBoolean(i++, dto.isInSingular());

            if (null != dto.getIdTermino() && null != dto.getContexto()) {
                ps.setInt(i++, dto.getIdTermino());
                ps.setString(i++, dto.getContexto());
            }

            //Ejecutamos la sql
            ps.execute();

        } catch (SQLException e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                } // end if

            } catch (SQLException e) {
                getLog().error(e.getMessage());
                throw new BusinessException(e);
            }
        }// end finally
    }

    /**
     * Eliminación de resultados.
     * <p/>
     * 1- Ejecutamos la sql
     * : StringBuilder sb = new StringBuilder();
     * : sb.append(" DELETE FROM SEXISTA_RESULTADOS WHERE ID_RASTREO = ? ");
     * : if(null != resultadoDto.getUrlTermino()){
     * :    sb.append(" AND URL_TERMINO = ?");
     * : }
     * <p/>
     * 2- Parametros
     * : ps.setString(1, resultadoDto.getIdRastreo());
     * : if(null != resultadoDto.getUrlTermino()){
     * :   ps.setString(2, resultadoDto.getUrlTermino());
     * : }
     *
     * @param resultadoDto - Datos de eliminacion
     * @throws BusinessException BusinessException
     */
    public void delete(ResultadoDto resultadoDto) throws BusinessException {

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = ConexionBBDD.conectar();

            //Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" DELETE FROM SEXISTA_RESULTADOS WHERE ID_RASTREO = ? ");

            if (null != resultadoDto.getUrlTermino()) {
                sb.append(" AND URL_TERMINO = ?");
            }

            //Mostramos en el log la sql a ejecutar
            getLog().info(sb.toString());

            ps = (PreparedStatement) con.prepareStatement(sb.toString());

            //Añadimos los parametros de seleccion
            ps.setLong(1, resultadoDto.getIdRastreo());

            //Si tenemos la url, solo eliminaremos los registros de esta iteracion
            if (null != resultadoDto.getUrlTermino()) {
                ps.setString(2, resultadoDto.getUrlTermino());
            }

            //Ejecutamos la sql
            ps.execute();

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(con, ps, null);
        }
    }

}
