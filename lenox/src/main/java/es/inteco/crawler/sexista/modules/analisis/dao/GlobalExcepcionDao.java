package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.commons.dto.GlobalExcepcionDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Dao de Excepciones Globales.
 */
public class GlobalExcepcionDao {

    /**
     * Recuperamos TODAS las excepciones globales.
     * <p/>
     * 1- Ejecutamos la SQL
     * : SELECT ID_EXCEPCION, EXCEPCION, POSICION FROM SEXISTA_EXCEPCIONES2;
     *
     * @return ArrayList(GlobalExcepcionDto) - Listado con las excepciones globales
     * @throws BusinessException BusinessException
     */
    public ArrayList<GlobalExcepcionDto> find(Connection conn) throws BusinessException {

        GlobalExcepcionDto dto;
        ArrayList<GlobalExcepcionDto> listado = new ArrayList<GlobalExcepcionDto>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_EXCEPCION, EXCEPCION, POSICION ");
            sb.append(" FROM SEXISTA_EXCEPCIONES2 ");

            ps = (PreparedStatement) conn.prepareStatement(sb.toString());

            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new GlobalExcepcionDto();

                dto.setIdExcepcion(rs.getInt("ID_EXCEPCION"));
                dto.setExcepcion(rs.getString("EXCEPCION"));
                dto.setPosition(rs.getInt("POSICION"));

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
