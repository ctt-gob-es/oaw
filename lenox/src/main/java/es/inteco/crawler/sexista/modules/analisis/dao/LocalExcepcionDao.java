package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.commons.dto.LocalExcepcionDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de recuperacion de excepciones locales.
 */
public class LocalExcepcionDao {

    /**
     * Recuperamos las excepciones locales referentes a un parametro en particular.
     * <p/>
     * 1- Ejecutamos la SQL
     * : SELECT ID_EXCEPCION, EXCEPCION, POSICION, ID_TERMINO
     * : FROM SEXISTA_EXCEPCIONES1
     * : WHERE ID_TERMINO = ?
     * <p/>
     * 2- Parametros:
     * : ps.setInt(1, idTermino);
     * <p/>
     * 3- Retornamos:
     * : ArrayList(LocalExcepcionDto) listado = new ArrayList(LocalExcepcionDto)();
     * : ...
     * : dto = new LocalExcepcionDto();
     * : dto.setIdExcepcion(rs.getInt("ID_EXCEPCION"));
     * : dto.setExcepcion(rs.getString("EXCEPCION"));
     * : dto.setPosition(rs.getInt("POSICION"));
     * : dto.setIdTermino(rs.getInt("ID_TERMINO"));
     * : listado.add(dto);
     *
     * @param idTermino - identificador del termino a buscar
     * @return ArrayList(GlobalExcepcionDto) - Listado con las excepciones globales
     * @throws BusinessException BusinessException
     */
    public List<LocalExcepcionDto> find(Connection conn, int idTermino) throws BusinessException {

        LocalExcepcionDto dto;
        List<LocalExcepcionDto> listado = new ArrayList<LocalExcepcionDto>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_EXCEPCION, EXCEPCION, POSICION, ID_TERMINO ");
            sb.append(" FROM SEXISTA_EXCEPCIONES1 ");
            sb.append(" WHERE ID_TERMINO = ? ");

            ps = (PreparedStatement) conn.prepareStatement(sb.toString());

            ps.setInt(1, idTermino);

            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new LocalExcepcionDto();

                dto.setIdExcepcion(rs.getInt("ID_EXCEPCION"));
                dto.setExcepcion(rs.getString("EXCEPCION"));
                dto.setPosition(rs.getInt("POSICION"));
                dto.setIdTermino(rs.getInt("ID_TERMINO"));

                listado.add(dto);
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return listado;
    }

}
