package es.inteco.rastreador2.lenox.dao;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.dto.ResultsByUrlDto;
import es.inteco.crawler.sexista.modules.analisis.service.TerminosService;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;
import es.inteco.crawler.sexista.modules.commons.util.UtilProcessingText;
import es.inteco.rastreador2.lenox.constants.CommonsConstants;
import es.inteco.rastreador2.lenox.constants.InformesConstants;
import es.inteco.rastreador2.lenox.dto.*;
import es.inteco.rastreador2.lenox.form.ObservatoryLenoxForm;
import es.inteco.rastreador2.lenox.mapper.BaseMapper;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase DAO de Informes. Gestiona todas las llamadas a BD relacionadas
 * con Informes
 *
 * @author psanchez, jgalindo
 */
public class InformesDao extends BaseDao {

    /**
     * Devuelve una lista paginada de rastreos según los criterios indicados en el dto que le
     * pasamos a la función.
     *
     * @param dto Dto de Búsqueda
     * @return Lista de rastreos para paginación
     * @throws BusinessException BusinessException
     */
    public RastreosSearchDto searchRastreosPaginado(RastreosSearchDto dto) throws BusinessException {
        StringBuilder sbInicial = new StringBuilder();
        StringBuilder sbFinal = new StringBuilder();
        StringBuilder sbFiltros = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();

        sbInicial.append(" SELECT agrupacion.ID_RASTREO, agrupacion.USUARIO AS LOGIN, agrupacion.FECHA, ");
        sbInicial.append(" IF(agrupacion.ID_TERMINO=0,0,COUNT(agrupacion.ID_TERMINO)) AS NUM_TERMINOS_GRAVEDAD, ");
        sbInicial.append(" IF(agrupacion.ID_TERMINO=0,0,(SELECT COUNT(r2.ID_RASTREO) ");
        sbInicial.append(" FROM SEXISTA_RASTREOS r2 LEFT JOIN SEXISTA_RESULTADOS rs2 ON r2.ID_RASTREO=rs2.ID_RASTREO  ");
        sbInicial.append(" AND rs2.ID_TERMINO <>0 ");
        sbInicial.append(" WHERE rs2.ID_RASTREO = agrupacion.ID_RASTREO )) AS NUM_TERMINOS  ");

        sbInicial.append(" FROM ");

        sbInicial.append(" (SELECT temp.ID_RASTREO,temp.ID_TERMINO, temp.EN_SINGULAR, temp.USUARIO, temp.FECHA, temp.URL_TERMINO, temp.GRAVEDAD ");
        sbInicial.append(" FROM (  ");
        sbInicial.append(" SELECT r.ID_RASTREO,rs.ID_TERMINO, rs.EN_SINGULAR, r.USUARIO, r.FECHA, rs.URL_TERMINO, ");
        sbInicial.append(" IF (rs.EN_SINGULAR=0,(SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ");
        sbInicial.append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) AS GRAVEDAD ");
        sbInicial.append(" FROM SEXISTA_RASTREOS r LEFT JOIN SEXISTA_RESULTADOS rs ON r.ID_RASTREO=rs.ID_RASTREO AND rs.ID_TERMINO <>0) AS temp ");

        sbInicial.append("  WHERE 1=1 ");

        sbFinal.append(" ) AS agrupacion ");
        sbFinal.append(" GROUP BY ID_RASTREO ");
        sbFinal.append(" ORDER BY FECHA DESC ");

        List<Object> params = new ArrayList<>();

        if (null != dto.getNombreRastreo() && !"".equals(dto.getNombreRastreo())) {
            sbFiltros.append(" AND temp.ID_RASTREO LIKE ? ");
            String rastreo = dto.getNombreRastreo().replaceAll("\\?", "%");
            params.add(rastreo);

        }

        if (null != dto.getFechaDesde() && !"".equals(dto.getFechaDesde())) {
            sbFiltros.append(" AND STR_TO_DATE(DATE_FORMAT(temp.FECHA , ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(" ), ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(" ) >= STR_TO_DATE( ?, ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(") ");

            params.add(dto.getFechaDesde());
        }

        if (null != dto.getFechaHasta() && !"".equals(dto.getFechaHasta())) {
            sbFiltros.append(" AND STR_TO_DATE(DATE_FORMAT(temp.FECHA , ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(" ), ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(" ) <= STR_TO_DATE( ?, ");
            sbFiltros.append(CommonsConstants.FORMATO_FECHA_BD);
            sbFiltros.append(") ");

            params.add(dto.getFechaHasta());
        }

        if (null != dto.getUrl() && !"".equals(dto.getUrl())) {
            sbFiltros.append(" AND TRIM(temp.URL_TERMINO) LIKE TRIM(?) ");
            String url = dto.getUrl().replaceAll("\\?", "%");
            params.add(url);
        }

        if (null != dto.getUsuario() && !"".equals(dto.getUsuario())) {
            sbFiltros.append(" AND temp.USUARIO LIKE ? ");
            String usuario = dto.getUsuario().replaceAll("\\?", "%");
            params.add(usuario);
        }

        if (null != dto.getGravedad() && !"".equals(dto.getGravedad())) {
            if (InformesConstants.GRAVEDAD_ALTA.equals(dto.getGravedad())) {
                sbFiltros.append(" AND GRAVEDAD = ");
                sbFiltros.append(InformesConstants.GRAVEDAD_ALTA_VALOR);
            }

            if (InformesConstants.GRAVEDAD_MEDIA_ALTA.equals(dto.getGravedad())) {
                sbFiltros.append(" AND GRAVEDAD >= ");
                sbFiltros.append(InformesConstants.GRAVEDAD_MEDIA_ALTA_VALOR);
            }

        }

        //Construimos la sql final
        sbTotal.append(sbInicial);
        sbTotal.append(sbFiltros);
        sbTotal.append(sbFinal);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = sbTotal.toString().toUpperCase();
        sql = sql.replaceAll(CommonsConstants.FORMATO_FECHA_BD.toUpperCase(),
                CommonsConstants.FORMATO_FECHA_BD);

        try {

            dto = (RastreosSearchDto) this.executePreparedQueryPag(sql, params, dto, new BaseMapper() {

                public RastreoExtDto extract(ResultSet rs) throws SQLException {
                    RastreoExtDto dto = new RastreoExtDto();

                    String formato = ConfigUtil.getConfiguracion().getProperty("config.formato.fecha");

                    SimpleDateFormat sdf = new SimpleDateFormat(formato);

                    dto.setIdRastreo(rs.getLong("ID_RASTREO"));
                    dto.setLoginUsuario(rs.getString("LOGIN"));
                    Date date = rs.getDate("FECHA");
                    dto.setFecha(sdf.format(date));
                    dto.setNumTerminosLocalizados(rs.getInt("NUM_TERMINOS"));
                    dto.setNumTerminosGravedad(rs.getInt("NUM_TERMINOS_GRAVEDAD"));

                    return dto;
                }

            });

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(con, ps, rs);
        } // end finally

        return dto;
    }

    /**
     * Busca los datos generales de un determinado rastreo.
     *
     * @param dto <b>DetalleDto</b>
     * @return <b>List</b>
     * @throws BusinessException en caso de error.
     */
    public RastreoExtDto searchRastreoDetalle(Connection conn, DetalleDto dto) throws BusinessException {

        StringBuilder sbInicial = new StringBuilder();
        StringBuilder sbFinal = new StringBuilder();

        sbInicial.append(" SELECT agrupacion.ID_RASTREO, ");
        sbInicial.append("     agrupacion.USUARIO AS LOGIN, ");
        sbInicial.append("     agrupacion.FECHA, ");
        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,COUNT(agrupacion.ID_TERMINO)) AS NUM_TERMINOS_GRAVEDAD, ");

        // Cambio de num_terminos gravedad a prioridad alta, medi, baja
        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.URL_TERMINO like ? ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 3, t.PS = 3))) as PRIORIDAD_ALTA, ");

        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.URL_TERMINO like ? ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 2, t.PS = 2))) as PRIORIDAD_MEDIA, ");

        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.URL_TERMINO like ? ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 1, t.PS = 1))) as PRIORIDAD_BAJA, ");


        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,(SELECT COUNT(r2.ID_RASTREO) ");
        sbInicial.append("                                    FROM SEXISTA_RASTREOS r2 LEFT JOIN SEXISTA_RESULTADOS rs2 ON r2.ID_RASTREO=rs2.ID_RASTREO AND rs2.ID_TERMINO <>0");
        sbInicial.append("                                    WHERE rs2.ID_RASTREO = agrupacion.ID_RASTREO AND rs2.URL_TERMINO like ?)) AS TOTAL_OCURRENCIAS, ");
        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,(SELECT COUNT(distinct rs2.ID_TERMINO) ");
        sbInicial.append("                                    FROM SEXISTA_RASTREOS r2 LEFT JOIN SEXISTA_RESULTADOS rs2 ON r2.ID_RASTREO=rs2.ID_RASTREO AND rs2.ID_TERMINO <>0");
        sbInicial.append("                                    WHERE rs2.ID_RASTREO = agrupacion.ID_RASTREO AND rs2.URL_TERMINO like ?)) AS TOTAL_TERMINOS ");
        sbInicial.append(" FROM ");
        sbInicial.append("   (SELECT temp.ID_RASTREO,temp.ID_TERMINO, temp.EN_SINGULAR, temp.USUARIO, temp.FECHA, temp.URL_TERMINO, temp.GRAVEDAD ");
        sbInicial.append("    FROM ( ");
        sbInicial.append("          SELECT r.ID_RASTREO,rs.ID_TERMINO, rs.EN_SINGULAR, r.USUARIO, r.FECHA, rs.URL_TERMINO, ");
        sbInicial.append("                  IF (rs.EN_SINGULAR=0,(SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ");
        sbInicial.append("                                        (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ");
        sbInicial.append(" AS GRAVEDAD ");
        sbInicial.append("           FROM SEXISTA_RASTREOS r LEFT JOIN SEXISTA_RESULTADOS rs ON r.ID_RASTREO=rs.ID_RASTREO AND rs.ID_TERMINO <>0) AS temp ");
        sbInicial.append("           WHERE temp.ID_RASTREO = ? AND temp.URL_TERMINO like ?");

        sbFinal.append(" ) AS agrupacion ");
        sbFinal.append(" GROUP BY ID_RASTREO ");

        PreparedStatement ps = null;
        ResultSet rs = null;

        List<RastreoExtDto> listado = new ArrayList<>();

        String formato = ConfigUtil.getConfiguracion().getProperty("config.formato.fecha");

        SimpleDateFormat sdf = new SimpleDateFormat(formato);

        StringBuilder sb = new StringBuilder();
        sb.append(sbInicial.toString()).append(sbFinal.toString());

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setString(1, dto.getUrl());
            ps.setString(2, dto.getUrl());
            ps.setString(3, dto.getUrl());
            ps.setString(4, dto.getUrl());
            ps.setString(5, dto.getUrl());
            ps.setLong(6, dto.getRastreo().getIdRastreo());
            ps.setString(7, dto.getUrl());

            rs = ps.executeQuery();

            while (rs.next()) {
                RastreoExtDto rdto = new RastreoExtDto();
                rdto.setIdRastreo(rs.getLong("ID_RASTREO"));
                rdto.setLoginUsuario(rs.getString("LOGIN"));
                Date date = rs.getDate("FECHA");
                rdto.setFecha(sdf.format(date));
                rdto.setNumTerminosLocalizados(rs.getInt("TOTAL_TERMINOS"));
                rdto.setNumTerminosOcurrentes(rs.getInt("TOTAL_OCURRENCIAS"));
                rdto.setNumTerminosGravedad(rs.getInt("NUM_TERMINOS_GRAVEDAD"));
                rdto.setNumTerminosPrioridadAlta(rs.getInt("PRIORIDAD_ALTA"));
                rdto.setNumTerminosPrioridadMedia(rs.getInt("PRIORIDAD_MEDIA"));
                rdto.setNumTerminosPrioridadBaja(rs.getInt("PRIORIDAD_BAJA"));

                listado.add(rdto);
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        if (null != listado && !listado.isEmpty()) {
            return (RastreoExtDto) listado.get(0);
        } else {
            throw new BusinessException("No se ha encontrado el rastreo");
        }
    }

    /**
     * Busca las urls asociadas para un determinado rastreo.
     *
     * @param dto <b>DetalleDto</b>
     * @return <b>List</b>
     * @throws BusinessException en caso de error.
     */
    public List<String> searchUrlsDetalle(DetalleDto dto) throws BusinessException {
        StringBuilder sb = new StringBuilder();


        if (InformesConstants.GRAVEDAD_TODOS.equalsIgnoreCase(dto.getGravedad())) {
            sb.append("SELECT DISTINCT rs.URL_TERMINO ");
            sb.append(" FROM SEXISTA_RESULTADOS rs ");
            sb.append(" WHERE rs.ID_RASTREO = ? ");
        } else {
            sb.append(" SELECT DISTINCT URL_TERMINO FROM ").
                    append(" (SELECT rs.URL_TERMINO,IF (rs.EN_SINGULAR=0, ").
                    append(" (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ").
                    append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ").
                    append(" AS GRAVEDAD ").
                    append(" FROM SEXISTA_RESULTADOS rs, SEXISTA_TERMINOS t ").
                    append(" WHERE t.ID_TERMINO = rs.ID_TERMINO ").
                    append(" AND rs.ID_RASTREO = ?) AS TEMP ");


            if (InformesConstants.GRAVEDAD_MEDIA_ALTA.equalsIgnoreCase(dto.getGravedad())) {
                sb.append(" WHERE GRAVEDAD >= ").append(InformesConstants.GRAVEDAD_MEDIA_ALTA_VALOR);
            } else {
                sb.append(" WHERE GRAVEDAD = ").append(InformesConstants.GRAVEDAD_ALTA_VALOR);
            }
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> listado = new ArrayList<>();

        try {
            con = ConexionBBDD.conectar();

            ps = con.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getRastreo().getIdRastreo());

            rs = ps.executeQuery();

            while (rs.next()) {
                listado.add(rs.getString("URL_TERMINO"));
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(con, ps, rs);
        }

        return listado;
    }

    /**
     * Busca los resultados que tiene término asociado para un determinado rastreo.
     *
     * @param dto <b>DetalleDto</b>
     * @return <b>List</b>
     * @throws BusinessException en caso de error.
     */
    public List<Integer> searchIdTerminosDetalle(Connection conn, DetalleDto dto, int page) throws BusinessException {
        StringBuilder sb = new StringBuilder();

        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "lenox.report.pagination"));
        int resultFrom = pagSize * page;

        if (page != Constants.NO_PAGINACION) {
            if (StringUtils.isEmpty(dto.getGravedad())) {
                sb.append("SELECT DISTINCT(rs.ID_TERMINO) AS ID_TERMINO");
                sb.append(" FROM SEXISTA_RESULTADOS rs ");
                sb.append(" WHERE rs.ID_RASTREO =  ? ");
                sb.append(" AND rs.ID_TERMINO <> 0  ");
                sb.append(" AND rs.URL_TERMINO LIKE ? LIMIT ? OFFSET ?");
            } else {
                sb.append(" SELECT DISTINCT(TERMINO) AS ID_TERMINO FROM ( ").
                        append(" SELECT rs.ID_TERMINO AS TERMINO, IF (rs.EN_SINGULAR=0, ").
                        append(" (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ").
                        append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ").
                        append(" AS GRAVEDAD ").
                        append(" FROM SEXISTA_RESULTADOS rs, SEXISTA_TERMINOS t ").
                        append(" WHERE t.ID_TERMINO = rs.ID_TERMINO ").
                        append(" AND rs.ID_TERMINO <> 0 ").
                        append(" AND rs.ID_RASTREO = ? AND rs.URL_TERMINO LIKE ?) AS TEMP").
                        append(" WHERE GRAVEDAD = ? LIMIT ? OFFSET ?");
            }
        } else {
            if (StringUtils.isEmpty(dto.getGravedad())) {
                sb.append("SELECT DISTINCT(rs.ID_TERMINO) AS ID_TERMINO");
                sb.append(" FROM SEXISTA_RESULTADOS rs ");
                sb.append(" WHERE rs.ID_RASTREO =  ? ");
                sb.append(" AND rs.ID_TERMINO <> 0  ");
                sb.append(" AND rs.URL_TERMINO LIKE ?");
            } else {
                sb.append(" SELECT DISTINCT(TERMINO) AS ID_TERMINO FROM ( ").
                        append(" SELECT rs.ID_TERMINO AS TERMINO, IF (rs.EN_SINGULAR=0, ").
                        append(" (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ").
                        append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ").
                        append(" AS GRAVEDAD ").
                        append(" FROM SEXISTA_RESULTADOS rs, SEXISTA_TERMINOS t ").
                        append(" WHERE t.ID_TERMINO = rs.ID_TERMINO ").
                        append(" AND rs.ID_TERMINO <> 0 ").
                        append(" AND rs.ID_RASTREO = ? AND rs.URL_TERMINO LIKE ?) AS TEMP").
                        append(" WHERE GRAVEDAD = ?");
            }
        }


        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> listado = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getRastreo().getIdRastreo());
            ps.setString(2, dto.getUrl());

            if (StringUtils.isNotEmpty(dto.getGravedad())) {
                ps.setString(3, dto.getGravedad());
                if (page != Constants.NO_PAGINACION) {
                    ps.setInt(4, pagSize);
                    ps.setInt(5, resultFrom);
                }
            } else if (page != Constants.NO_PAGINACION) {
                ps.setInt(3, pagSize);
                ps.setInt(4, resultFrom);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                listado.add(rs.getInt("ID_TERMINO"));
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return listado;
    }

    public static int countDetail(Connection conn, DetalleDto dto) throws BusinessException, SQLException {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(dto.getGravedad())) {
            sb.append("SELECT COUNT(DISTINCT(rs.ID_TERMINO)) ");
            sb.append(" FROM SEXISTA_RESULTADOS rs ");
            sb.append(" WHERE rs.ID_RASTREO =  ? ");
            sb.append(" AND rs.ID_TERMINO <> 0  ");
            sb.append(" AND rs.URL_TERMINO LIKE ?");
        } else {
            sb.append(" SELECT COUNT(DISTINCT(rs.ID_TERMINO)) FROM ( ").
                    append(" SELECT rs.ID_TERMINO AS TERMINO, IF (rs.EN_SINGULAR=0, ").
                    append(" (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ").
                    append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ").
                    append(" AS GRAVEDAD ").
                    append(" FROM SEXISTA_RESULTADOS rs, SEXISTA_TERMINOS t ").
                    append(" WHERE t.ID_TERMINO = rs.ID_TERMINO ").
                    append(" AND rs.ID_TERMINO <> 0 ").
                    append(" AND rs.ID_RASTREO = ? AND rs.URL_TERMINO LIKE ?) AS TEMP").
                    append(" WHERE GRAVEDAD = ? LIMIT");
        }


        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getRastreo().getIdRastreo());
            ps.setString(2, dto.getUrl());

            if (StringUtils.isNotEmpty(dto.getGravedad())) {
                ps.setString(3, dto.getGravedad());
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }
    }

    /**
     * Busca los datos generales del término encontrado.
     *
     * @param dto <b>TerminoDetalleDto</b>
     * @return <b>TerminoDetalleDto</b>
     * @throws BusinessException en caso de error.
     */
    public TerminoDetalleDto searchTerminoDetalle(Connection conn, TerminoDetalleDto dto, String url, String gravedad) throws BusinessException {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT T.TERMINO, T.PS, T.PP, R.EN_SINGULAR ");
        sb.append("FROM SEXISTA_TERMINOS T ");
        sb.append("JOIN SEXISTA_RESULTADOS R ON (T.ID_TERMINO = R.ID_TERMINO) ");
        sb.append("WHERE T.ID_TERMINO = ? AND R.ID_RASTREO = ? AND R.URL_TERMINO = ?");

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sb.toString());

            ps.setInt(1, dto.getIdTermino());
            ps.setLong(2, dto.getIdRastreo());
            ps.setString(3, url);

            rs = ps.executeQuery();

            while (rs.next()) {
                dto.setNombre(rs.getString("TERMINO"));
                String ocurrenceGravity = rs.getString("EN_SINGULAR").equals("0") ? rs.getString("PP") : rs.getString("PS");
                if (StringUtils.isEmpty(gravedad) || gravedad.equals(ocurrenceGravity)) {
                    dto.setNumOcurrencias(dto.getNumOcurrencias() + 1);
                }
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return dto;
    }

    /**
     * Busca los contextos de los términos encontrados.
     *
     * @param dto <b>TerminoDetalleDto</b>
     * @return <b>List</b>
     * @throws BusinessException en caso de error.
     */
    public List<ResultadoDto> searchContextosTerminoDetalle(Connection conn, TerminoDetalleDto dto, String url, String gravedad)
            throws BusinessException {
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT ID_RESULTADO, ID_TERMINO, ID_RASTREO, ");
        sb.append("        CONTEXTO, URL_TERMINO, GRAVEDAD ");
        sb.append(" FROM ");
        sb.append(" (SELECT rs.ID_RESULTADO,rs.ID_TERMINO,rs.ID_RASTREO, ");
        sb.append("        rs.CONTEXTO,rs.URL_TERMINO, IF (rs.EN_SINGULAR=0, ");
        sb.append("                                       (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ");
        sb.append("                                       (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ");
        sb.append("                                    AS GRAVEDAD ");
        sb.append(" FROM SEXISTA_RESULTADOS rs ");
        sb.append(" WHERE rs.ID_RASTREO = ? ");
        sb.append("       AND rs.ID_TERMINO = ? AND rs.URL_TERMINO LIKE ?) AS TEMP ");

        if (StringUtils.isNotEmpty(gravedad)) {
            sb.append(" WHERE GRAVEDAD = ?");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ResultadoDto> listado = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getIdRastreo());
            ps.setInt(2, dto.getIdTermino());
            ps.setString(3, url);

            if (StringUtils.isNotEmpty(gravedad)) {
                ps.setString(4, gravedad);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                ResultadoDto rdto = new ResultadoDto();
                String contextoFormateado = formatearContexto(conn, dto.getNombre(), rs.getString("CONTEXTO"));
                rdto.setContexto(contextoFormateado);

                rdto.setUrlTermino(rs.getString("URL_TERMINO"));
                rdto.setIdResultado(rs.getInt("ID_RESULTADO"));
                rdto.setIdTermino(rs.getInt("ID_TERMINO"));
                rdto.setIdRastreo(rs.getLong("ID_RASTREO"));
                rdto.setGravedad(rs.getString("GRAVEDAD"));

                listado.add(rdto);
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return listado;
    }

    /**
     * Busca las posibles alternativas a un término.
     *
     * @param dto <b>TerminoDetalleDto</b>
     * @return <b>List</b>
     * @throws BusinessException en caso de error.
     */
    public List<SugerenciaDto> searchAlternativasTerminoDetalle(Connection conn, TerminoDetalleDto dto)
            throws BusinessException {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ID_SUGERENCIA, ALTERNATIVA, DESCRIPCION, ID_TERMINO ");
        sb.append(" FROM SEXISTA_SUGERENCIAS ");
        sb.append(" WHERE ID_TERMINO = ? ");

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SugerenciaDto> listado = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setInt(1, dto.getIdTermino());

            rs = ps.executeQuery();

            while (rs.next()) {
                SugerenciaDto sdto = new SugerenciaDto();
                sdto.setAlternativa(rs.getString("ALTERNATIVA"));
                sdto.setDescripcion(rs.getString("DESCRIPCION"));
                sdto.setIdSugerencia(rs.getInt("ID_SUGERENCIA"));
                sdto.setIdTermino(rs.getInt("ID_TERMINO"));
                listado.add(sdto);
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return listado;
    }

    /**
     * Formatea en negrita el termino localizado en el contexto para mostrarlo en la jsp de detalle.
     *
     * @param paramTermino Termino Localizado
     * @param contexto     Contexto a formatear
     * @return Contexto formateado
     */
    private String formatearContexto(Connection conn, String paramTermino, String contexto) {

        //Eliminamos los espacios del termino por si en BD está con espacios
        String termino = paramTermino.trim();

        StringBuffer contextoFormateado = new StringBuffer(contexto);

        //Inicializamos el servicio para obtener los términos
        TerminosService terminosService = new TerminosService();
        terminosService.setDao(new TerminosDao());

        try {
            //Recuperamos los terminos compuestos
            ArrayList<TerminoDto> lstTerminosCompuestos;

            lstTerminosCompuestos = terminosService.findTerminosCompuestos(conn);

            //1- Separamos el texto por palabras y las almacenamos de forma consecutiva en un array.
            ArrayList<String> lstTerminos = UtilProcessingText.splitText(contexto, lstTerminosCompuestos);

            contextoFormateado = new StringBuffer(CommonsConstants.CADENA_VACIA);

            for (int i = 0; i < lstTerminos.size(); i++) {
                String palabra = lstTerminos.get(i);

                if (isTerminoOnContext(termino, palabra)) {
                    StringBuilder palabraNegrita = new StringBuilder(CommonsConstants.TAG_INI_NEGRITA);
                    palabraNegrita.append(palabra);
                    palabraNegrita.append(CommonsConstants.TAG_FIN_NEGRITA);
                    palabra = palabraNegrita.toString();
                }

                contextoFormateado.append(palabra);
            }
        } catch (BusinessException e) {
            getLog().error(e.getMessage());
        }
        return contextoFormateado.toString();
    }

    /**
     * M&eacute;todo isTerminoOnContext.
     * Busca si coinciden el término y la palabra que le pasamos. Si no
     * coinciden se busca el singular o el plural del termino y volvemos
     * a comprobar si coinciden
     *
     * @param termino Término a localizar
     * @param palabra Palabra del contexto
     * @return True/False si coinciden o no
     */
    private boolean isTerminoOnContext(String termino, String palabra) {
        boolean encontrado = false;
        //Configuramos el comparador
        Collator collator = Collator.getInstance();

        //Indicamos que obvie mayúsculas/minúsculas, acentos....
        collator.setStrength(Collator.PRIMARY);

        String palabraTrim = palabra.trim();
        if (collator.compare(termino, palabraTrim) != 0) {
            //Buscamos plural
            String terminoPlural = UtilCalculateTerms.calculatePlural(termino);

            if (collator.compare(terminoPlural, palabraTrim) != 0) {
                //Buscamos plural
                String terminoSingular = UtilCalculateTerms.calculateSingular(termino);

                if (collator.compare(terminoSingular, palabraTrim) == 0) {
                    encontrado = true;
                }
            } else {
                encontrado = true;
            }
        } else {
            encontrado = true;
        }

        return encontrado;
    }

    /**
     * Cuando no encuentra términos sexistas para una url, introduce un registro en la tabla de resultados
     * con id_termino = 0. Debido a esto si se ejecuta una sola consulta podemos hacer, o que aparezca reflejado
     * que las páginas sin ningún término sexista tengan un término sexista (falso positivo), o que simplemente
     * no aparezcan en el listado, con lo que despistará a los usuarios ya que parece que no se han analizado.
     * Para solucionar esto, se harán dos peticiones, una para contar los resultados de forma correcta, y otro
     * para ver las páginas analizadas, después se contrastarán ambos para mostrar el listado al usuario.
     *
     * @param conn
     * @param idRastreo
     * @param pagina
     * @return
     * @throws BusinessException
     * @throws SQLException
     */
    public List<ResultsByUrlDto> getResultsByUrl(Connection conn, Long idRastreo, int pagina) throws BusinessException, SQLException {
        PropertiesManager pmgr = new PropertiesManager();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * pagina;
        try {
            ps = conn.prepareStatement("SELECT URL_TERMINO, COUNT(*) AS NUMTERMINOS " +
                    "FROM SEXISTA_RESULTADOS WHERE ID_RASTREO = ? AND ID_TERMINO <> 0 " +
                    "GROUP BY URL_TERMINO");
            ps.setLong(1, idRastreo);
            rs = ps.executeQuery();

            Map<String, Integer> resultsMap = new HashMap<>();
            while (rs.next()) {
                resultsMap.put(rs.getString("url_termino"), rs.getInt("numTerminos"));
            }
            DAOUtils.close(null, ps, rs);

            if (pagina != Constants.NO_PAGINACION) {
                ps = conn.prepareStatement("SELECT DISTINCT(URL_TERMINO) FROM SEXISTA_RESULTADOS " +
                        "WHERE ID_RASTREO = ? LIMIT ? OFFSET ?");
                ps.setInt(2, pagSize);
                ps.setInt(3, resultFrom);
            } else {
                ps = conn.prepareStatement("SELECT DISTINCT(URL_TERMINO) FROM SEXISTA_RESULTADOS " +
                        "WHERE ID_RASTREO = ?");
            }

            ps.setLong(1, idRastreo);

            rs = ps.executeQuery();

            List<String> urlList = new ArrayList<>();
            while (rs.next()) {
                urlList.add(rs.getString("URL_TERMINO"));
            }

            return createResults(resultsMap, urlList);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }
    }

    private static List<ResultsByUrlDto> createResults(Map<String, Integer> resultsMap, List<String> urlList) {
        List<ResultsByUrlDto> results = new ArrayList<>();

        for (String url : urlList) {
            if (resultsMap.containsKey(url)) {
                results.add(new ResultsByUrlDto(url, resultsMap.get(url)));
            } else {
                results.add(new ResultsByUrlDto(url, 0));
            }
        }

        return results;
    }

    public static int countResultsByUrl(Connection conn, Long idRastreo) throws BusinessException, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(DISTINCT(URL_TERMINO)) FROM SEXISTA_RESULTADOS WHERE ID_RASTREO = ? ;");
            ps.setLong(1, idRastreo);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return 0;
    }

    public static List<ObservatoryLenoxForm> getObservatoryData(Connection conn, List<Long> listExecutionsIds) throws BusinessException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ObservatoryLenoxForm> results = new ArrayList<>();
        try {
            final StringBuilder query = new StringBuilder("SELECT ID_RASTREO, COUNT(DISTINCT(ID_RESULTADO)) AS TERMINOSLOCALIZADOS FROM SEXISTA_RESULTADOS WHERE ID_RASTREO IN ");
            query.append(buildParamValues(listExecutionsIds.size()));
            query.append(" GROUP BY ID_RASTREO ");

            ps = conn.prepareStatement(query.toString());

            int countParameters = 1;
            for (Long id : listExecutionsIds) {
                ps.setLong(countParameters++, id);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                ObservatoryLenoxForm observatoryLenoxForm = new ObservatoryLenoxForm();
                observatoryLenoxForm.setIdExecution(rs.getLong("ID_RASTREO"));
                observatoryLenoxForm.setTerminosLocalizados(rs.getInt("TERMINOSLOCALIZADOS"));
                results.add(observatoryLenoxForm);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return results;
    }

    private static StringBuilder buildParamValues(int size) {
        final StringBuilder params = new StringBuilder("(");
        for (int i = size-1; i>0; i--) {
            params.append("?, ");
        }
        params.append("?)");
        return params;
    }

    public static int countExecutionTermsByPriority(Connection conn, long idExecution, int priority) throws BusinessException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) AS TERMSBYPRIORITY FROM (SELECT IF (SR.EN_SINGULAR = 0, " +
                    "(SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = SR.ID_TERMINO), " +
                    "(SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= SR.ID_TERMINO)) AS GRAVEDAD " +
                    "FROM SEXISTA_RESULTADOS SR JOIN SEXISTA_TERMINOS ST ON (SR.ID_TERMINO = ST.ID_TERMINO) " +
                    "WHERE ID_RASTREO = ?) AS TEMP WHERE TEMP.GRAVEDAD = ? ");
            ps.setLong(1, idExecution);
            ps.setInt(2, priority);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("TERMSBYPRIORITY");
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, ps, rs);
        }

        return 0;
    }

}
