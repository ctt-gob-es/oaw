package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.commons.dto.CategoryExcepcionDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO de recuperacion de excepciones locales.
 */
public class CategoryExcepcionDao {

    /**
     * Recuperamos las excepciones de categoría.
     *
     * @param idCategoria - identificador de la categoria a buscar
     * @return ArrayList(CategoryExcepcionDto) - Listado con las excepciones de
     *         categoria
     * @throws BusinessException BusinessException
     */
    public ArrayList<CategoryExcepcionDto> find(Connection conn, long idCategoria)
            throws BusinessException {

        CategoryExcepcionDto dto;
        ArrayList<CategoryExcepcionDto> listado = new ArrayList<CategoryExcepcionDto>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_EXCEPCION, EXCEPCION, POSICION, ID_CATEGORIA ");
            sb.append(" FROM SEXISTA_EXCEPCIONES3 ");
            sb.append(" WHERE ID_CATEGORIA = ? ");

            ps = (PreparedStatement) conn.prepareStatement(sb.toString());

            ps.setLong(1, idCategoria);

            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new CategoryExcepcionDto();

                dto.setIdExcepcion(rs.getInt("ID_EXCEPCION"));
                dto.setExcepcion(rs.getString("EXCEPCION"));
                dto.setPosition(rs.getInt("POSICION"));
                dto.setIdCategoria(rs.getInt("ID_CATEGORIA"));

                listado.add(dto);
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }// end finally

        return listado;
    }

}
