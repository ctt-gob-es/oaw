package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.modules.commons.dto.RastreoDto;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao de rastreo.
 */
public final class RastreoDao {

    //Implementamos patrón singleton para que sólo haya una instancia
    /**
     * Campo rastreoDao.
     */
    private static RastreoDao rastreoDao = null;

    /**
     * Constructor por defecto.<br>
     */
    private RastreoDao() {

    }

    /**
     * M&eacute;todo getInstancia.
     *
     * @return RastreoDao.
     */
    public static RastreoDao getInstancia() {
        if (rastreoDao == null) {
            rastreoDao = new RastreoDao();
        }
        return rastreoDao;
    }

    /**
     * Método de busqueda de rastreos.
     * <p/>
     * 1- Ejecutamos la sql : SELECT R.ID_RASTREO, R.FECHA, R.USUARIO, R.TIEMPO
     * FROM SEXISTA_RASTREOS AS R WHERE ID_RASTREO = ?
     * <p/>
     * 2- Parametros : ps.setString(1, rastreoDto.getIdRastreo());
     * <p/>
     * 3- Retorna : ArrayList(RastreoDto) listado = new ArrayList[RastreoDto](); :
     * ... : dto = new RastreoDto(); :
     * dto.setIdRastreo(rs.getString("ID_RASTREO")); :
     * dto.setUsuario(rs.getString("USUARIO")); :
     * dto.setTiempo(rs.getLong("TIEMPO")); : listado.add(dto);
     *
     * @param rastreoDto -
     *                   Datos de busqueda del rastreo
     * @param con        Conexion a la BD
     * @return ArrayList[RastreoDto] - Listado de rastreos encontrados
     * @throws BusinessException BusinessException
     */
    public List<RastreoDto> find(RastreoDto rastreoDto, Connection con) throws BusinessException {
        RastreoDto dto;
        List<RastreoDto> listado = new ArrayList<RastreoDto>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT R.ID_RASTREO, R.FECHA, R.USUARIO, R.TIEMPO ");
            sb.append(" FROM SEXISTA_RASTREOS AS R WHERE ID_RASTREO = ? ");

            //Mostramos en el log la sql a ejecutar
            Logger.putLog(sb.toString(), RastreoDao.class, Logger.LOG_LEVEL_INFO);

            ps = con.prepareStatement(sb.toString());

            // Añadimos el identificador por el que buscaremos
            ps.setLong(1, rastreoDto.getIdRastreo());

            // Ejecutamos la consulta
            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new RastreoDto();

                dto.setIdRastreo(rs.getLong("ID_RASTREO"));
                dto.setUsuario(rs.getString("USUARIO"));
                dto.setTiempo(rs.getLong("TIEMPO"));

                listado.add(dto);
            }

        } catch (SQLException e) {
            Logger.putLog("Exception al buscar Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
            throw new BusinessException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    Logger.putLog("Exception al cerrar búsqueda de  Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            } // end if
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    Logger.putLog("Exception al cerrar búsqueda de  Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            } // end if
        }// end finally

        return listado;
    }

    /**
     * Insertamos un nuevo registro de rastreo.
     * <p/>
     * 1- Ejecutamos la sql : INSERT INTO SEXISTA_RASTREOS (ID_RASTREO, FECHA,
     * USUARIO, TIEMPO) values (?, ?, ?, ?);
     * <p/>
     * 2- Parametros : ps.setString(1, rastreoDto.getIdRastreo()); :
     * ps.setDate(2, new Date(sdf.parse(rastreoDto.getFecha()).getTime())); :
     * ps.setString(3, rastreoDto.getUsuario()); : ps.setLong(4,
     * rastreoDto.getTiempo());
     *
     * @param rastreoDto -
     *                   Datos del registro a insertar
     * @param con        Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void insert(RastreoDto rastreoDto, Connection con) throws BusinessException {

        SimpleDateFormat sdf = new SimpleDateFormat(ConfigUtil.getConfiguracion(
                "lenox.properties").getProperty("config.formato.fecha.insercion"));

        PreparedStatement ps = null;

        try {

            String sb = "INSERT INTO SEXISTA_RASTREOS (ID_RASTREO, FECHA, USUARIO, TIEMPO) values (?, ?, ?, ?);";

            //Mostramos en el log la sql a ejecutar
            Logger.putLog(sb, RastreoDao.class, Logger.LOG_LEVEL_INFO);

            ps = (PreparedStatement) con.prepareStatement(sb);

            ps.setLong(1, rastreoDto.getIdRastreo());
            ps.setTimestamp(2, new Timestamp(sdf.parse(rastreoDto.getFecha()).getTime()));
            ps.setString(3, rastreoDto.getUsuario());
            ps.setLong(4, rastreoDto.getTiempoCrono());

            // Ejecutamos la sql
            ps.execute();

        } catch (ParseException e) {
            Logger.putLog("Exception al insertar Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
            throw new BusinessException(e);
        } catch (SQLException e) {
            Logger.putLog("Exception al insertar Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
            throw new BusinessException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                } // end if

            } catch (SQLException e) {
                Logger.putLog("Exception al buscar Rastreo", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
                throw new BusinessException(e);
            }
        }// end finally
    }

    /**
     * Eliminamos un rastreo.
     * <p/>
     * 1- Ejecutamos la sql : DELETE FROM SEXISTA_RASTREOS WHERE ID_RASTREO = ?
     * <p/>
     * 2- Parametros : ps.setString(1, rastreoDto.getIdRastreo());
     *
     * @param rastreoDto -
     *                   Dto con el identificador del rastreo a eliminar.
     * @param con        Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void delete(RastreoDto rastreoDto, Connection con) throws BusinessException {

        PreparedStatement ps = null;

        try {

            // Construimos las consulta
            String sql = " DELETE FROM SEXISTA_RASTREOS WHERE ID_RASTREO = ?";

            //Mostramos en el log la sql a ejecutar
            Logger.putLog(sql, RastreoDao.class, Logger.LOG_LEVEL_INFO);

            ps = (PreparedStatement) con.prepareStatement(sql);

            ps.setLong(1, rastreoDto.getIdRastreo());

            // Ejecutamos la sql
            ps.execute();

        } catch (SQLException e) {
            Logger.putLog("Exception en RastreoDao.", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
            throw new BusinessException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                } // end if
            } catch (SQLException e) {
                Logger.putLog("Exception en RastreoDao.delete", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
                throw new BusinessException(e);
            }
        }// end finally

    }

    /**
     * Actualizamos el registro.
     * <p/>
     * 1- Ejecutamos la sql : UPDATE SEXISTA_RASTREOS SET TIEMPO = ? WHERE
     * ID_RASTREO = ?
     * <p/>
     * 2- Parametros : ps.setLong(1, dto.getTiempo()); : ps.setString(2,
     * dto.getIdRastreo());
     *
     * @param dto -
     *            Datos del registro a actualizar
     * @param con Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void update(RastreoDto dto, Connection con) throws BusinessException {

        PreparedStatement ps = null;

        try {
            String sql = "UPDATE SEXISTA_RASTREOS SET TIEMPO = ? WHERE ID_RASTREO = ?";

            //Mostramos en el log la sql a ejecutar
            Logger.putLog(sql, RastreoDao.class, Logger.LOG_LEVEL_INFO);

            ps = (PreparedStatement) con.prepareStatement(sql);

            ps.setLong(1, dto.getTiempo());
            ps.setLong(2, dto.getIdRastreo());

            // Ejecutamos la sql
            ps.execute();
        } catch (SQLException e) {
            Logger.putLog("Exception en RastreoDao.update", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
            throw new BusinessException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                } // end if
            } catch (SQLException e) {
                Logger.putLog("Exception en RastreoDao.update", RastreoDao.class, Logger.LOG_LEVEL_ERROR, e);
                throw new BusinessException(e);
            }
        }// end finally
    }

}
