package es.inteco.crawler.sexista.modules.analisis.dao;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Dao de terminos.
 */
public class TerminosDao {

    /**
     * Realizamos una busqueda sobre la tabla de Terminos.
     * <p/>
     * 1- Ejecutamos la sql
     * : StringBuilder sb = new StringBuilder();
     * : sb.append(" SELECT ID_TERMINO, TERMINO, USO_GLOBAL, FEMENINO, PLURAL, FEMENINO_PLURAL ");
     * : sb.append(" FROM SEXISTA_TERMINOS ");
     * : sb.append(" WHERE TERMINO IN ( ");
     * : //Añadimos interrogantes por cada termino que deseemos consultar
     * : StringBuilder sbuilder = new StringBuilder();
     * : for (int i = 0; i < lstTerminos.size(); i++) {
     * :   sbuilder.append(" ? ").append(" , ");
     * : }
     * : //Eliminamos los tres ultimos caracteres para eliminar la coma sobrane
     * : sb.append(sbuilder.substring(0, sbuilder.length()-3));
     * : sb.append(" ) ");
     * <p/>
     * 2- Parametros
     * : for (int i = 0; i < lstTerminos.size(); i++) {
     * :   ps.setString(i+1, lstTerminos.get(i).trim());
     * : }
     * <p/>
     * 3- Retornamos
     * : ArrayList[TerminoDto] listado = new ArrayList[TerminoDto]();
     * : dto = new TerminoDto();
     * : dto.setIdTermino(rs.getInt("ID_TERMINO"));
     * : dto.setTermino(rs.getString("TERMINO"));
     * : dto.setUsaGlobalException(rs.getBoolean("USO_GLOBAL"));
     * : dto.setFemenino(rs.getString("FEMENINO"));
     * : dto.setPlural(rs.getString("PLURAL"));
     * : dto.setFemeninoPlural(rs.getString("FEMENINO_PLURAL"));
     * : listado.add(dto);
     *
     * @param lstTerminos - Listado de terminos a buscar
     * @return ArrayList[TerminoDto] - Listado de terminos encontrados
     * @throws BusinessException BusinessException
     */
    public ArrayList<TerminoDto> find(Connection conn, ArrayList<String> lstTerminos) throws BusinessException {

        TerminoDto dto;
        ArrayList<TerminoDto> listado = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_TERMINO, TERMINO, USO_GLOBAL, FEMENINO, PLURAL, FEMENINO_PLURAL, FU, ID_CATEGORIA, PS, PP ");
            sb.append(" FROM SEXISTA_TERMINOS ");
            sb.append(" WHERE UPPER(TERMINO) IN ( ");

            //Añadimos interrogantes por cada termino que deseemos consultar
            StringBuilder sbuilder = new StringBuilder();
            for (int i = 0; i < lstTerminos.size(); i++) {
                sbuilder.append(" ? ").append(" , ");
            }

            //Eliminamos los tres ultimos caracteres para eliminar la coma sobrane
            sb.append(sbuilder.substring(0, sbuilder.length() - 3));
            sb.append(" ) ");

            ps = (PreparedStatement) conn.prepareStatement(sb.toString());

            //Anadimos los terminos
            for (int i = 0; i < lstTerminos.size(); i++) {
                ps.setString(i + 1, lstTerminos.get(i).trim());
            }

            //Ejecutamos la consulta
            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new TerminoDto();

                dto.setIdTermino(rs.getInt("ID_TERMINO"));
                dto.setTermino(rs.getString("TERMINO"));
                dto.setUsaGlobalException(rs.getBoolean("USO_GLOBAL"));
                dto.setFemenino(rs.getString("FEMENINO"));
                dto.setPlural(rs.getString("PLURAL"));
                dto.setFemeninoPlural(rs.getString("FEMENINO_PLURAL"));
                dto.setFormaUnica(rs.getBoolean("FU"));
                dto.setIdCategoria(rs.getLong("ID_CATEGORIA"));
                dto.setPrioridadSingular(rs.getInt("PS"));
                dto.setPrioridadPlural(rs.getInt("PP"));
                listado.add(dto);
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return listado;
    }

    public ArrayList<TerminoDto> find(Connection conn, String termino) throws BusinessException {

        //Montamos el listado de busqueda
        ArrayList<String> lstTerminos = new ArrayList<>();
        lstTerminos.add(termino);

        //Buscamos el termino
        return this.find(conn, lstTerminos);
    }

    /**
     * Buscamos terminos compuestos en BBDD.
     * <p/>
     * 1- Ejecutamos la sql.
     * : StringBuilder sb = new StringBuilder();
     * : sb.append(" SELECT ID_TERMINO, TERMINO, USO_GLOBAL, FEMENINO, PLURAL, FEMENINO_PLURAL ");
     * : sb.append(" FROM SEXISTA_TERMINOS ");
     * : sb.append(" WHERE TERMINO LIKE '% %'");
     * <p/>
     * 2- Retornamos
     * : ArrayList[TerminoDto] listado = new ArrayList[TerminoDto]();
     * : dto = new TerminoDto();
     * : dto.setIdTermino(rs.getInt("ID_TERMINO"));
     * : dto.setTermino(rs.getString("TERMINO"));
     * : dto.setUsaGlobalException(rs.getBoolean("USO_GLOBAL"));
     * : dto.setFemenino(rs.getString("FEMENINO"));
     * : dto.setPlural(rs.getString("PLURAL"));
     * : dto.setFemeninoPlural(rs.getString("FEMENINO_PLURAL"));
     * : listado.add(dto);
     *
     * @return ArrayList[TerminoDto] - Listado de terminos compuestos encontrados
     * @throws BusinessException BusinessException
     */
    public ArrayList<TerminoDto> findCompuestos(Connection conn) throws BusinessException {

        TerminoDto dto;
        ArrayList<TerminoDto> listado = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_TERMINO, TERMINO, USO_GLOBAL, FEMENINO, PLURAL, FEMENINO_PLURAL, FU, ID_CATEGORIA ");
            sb.append(" FROM SEXISTA_TERMINOS ");
            sb.append(" WHERE UPPER(TERMINO) LIKE '% %'");

            ps = (PreparedStatement) conn.prepareStatement(sb.toString());

            //Ejecutamos la consulta
            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new TerminoDto();

                dto.setIdTermino(rs.getInt("ID_TERMINO"));
                dto.setTermino(rs.getString("TERMINO"));
                dto.setUsaGlobalException(rs.getBoolean("USO_GLOBAL"));
                dto.setFemenino(rs.getString("FEMENINO"));
                dto.setPlural(rs.getString("PLURAL"));
                dto.setFemeninoPlural(rs.getString("FEMENINO_PLURAL"));
                dto.setFormaUnica(rs.getBoolean("FU"));
                dto.setIdCategoria(rs.getLong("ID_CATEGORIA"));

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
