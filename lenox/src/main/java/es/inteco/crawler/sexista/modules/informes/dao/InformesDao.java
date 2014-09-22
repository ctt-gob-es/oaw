package es.inteco.crawler.sexista.modules.informes.dao;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.core.dao.BaseDao;
import es.inteco.crawler.sexista.core.dao.mapper.BaseMapper;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.service.TerminosService;
import es.inteco.crawler.sexista.modules.commons.Constants.CommonsConstants;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.dto.SugerenciaDto;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;
import es.inteco.crawler.sexista.modules.commons.util.UtilProcessingText;
import es.inteco.crawler.sexista.modules.informes.constants.InformesConstants;
import es.inteco.crawler.sexista.modules.informes.dto.DetalleDto;
import es.inteco.crawler.sexista.modules.informes.dto.RastreoExtDto;
import es.inteco.crawler.sexista.modules.informes.dto.RastreosSearchDto;
import es.inteco.crawler.sexista.modules.informes.dto.TerminoDetalleDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

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

        List<Object> params = new ArrayList<Object>();

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
            try {
                if (rs != null) {
                    rs.close();
                } // end if

                if (ps != null) {
                    ps.close();
                } // end if

                if (con != null) {
                    con.close();
                } // end if
            } catch (SQLException e) {
                getLog().error(e.getMessage());
                throw new BusinessException(e);
            }
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
    public RastreoExtDto searchRastreoDetalle(DetalleDto dto) throws BusinessException {

        StringBuilder sbInicial = new StringBuilder();
        StringBuilder sbFinal = new StringBuilder();
        StringBuilder sbFiltros = new StringBuilder();

        sbInicial.append(" SELECT agrupacion.ID_RASTREO, ");
        sbInicial.append("     agrupacion.USUARIO AS LOGIN, ");
        sbInicial.append("     agrupacion.FECHA, ");
        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,COUNT(agrupacion.ID_TERMINO)) AS NUM_TERMINOS_GRAVEDAD, ");

        // Cambio de num_terminos gravedad a prioridad alta, medi, baja
        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 3, t.PS = 3))) as PRIORIDAD_ALTA, ");

        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 2, t.PS = 2))) as PRIORIDAD_MEDIA, ");

        sbInicial.append("     IF(AGRUPACION.ID_TERMINO=0,0,(SELECT COUNT(RS3.ID_TERMINO) ");
        sbInicial.append("     FROM SEXISTA_RESULTADOS RS3,SEXISTA_TERMINOS t ");
        sbInicial.append("     WHERE  RS3.ID_TERMINO = t.ID_TERMINO ");
        sbInicial.append("     AND RS3.ID_RASTREO = AGRUPACION.ID_RASTREO ");
        sbInicial.append("     AND IF(RS3.EN_SINGULAR=0, t.PP = 1, t.PS = 1))) as PRIORIDAD_BAJA, ");


        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,(SELECT COUNT(r2.ID_RASTREO) ");
        sbInicial.append("                                    FROM SEXISTA_RASTREOS r2 LEFT JOIN SEXISTA_RESULTADOS rs2 ON r2.ID_RASTREO=rs2.ID_RASTREO AND rs2.ID_TERMINO <>0");
        sbInicial.append("                                    WHERE rs2.ID_RASTREO = agrupacion.ID_RASTREO )) AS TOTAL_OCURRENCIAS, ");
        sbInicial.append("     IF(agrupacion.ID_TERMINO=0,0,(SELECT COUNT(distinct rs2.ID_TERMINO) ");
        sbInicial.append("                                    FROM SEXISTA_RASTREOS r2 LEFT JOIN SEXISTA_RESULTADOS rs2 ON r2.ID_RASTREO=rs2.ID_RASTREO AND rs2.ID_TERMINO <>0");
        sbInicial.append("                                    WHERE rs2.ID_RASTREO = agrupacion.ID_RASTREO )) AS TOTAL_TERMINOS ");
        sbInicial.append(" FROM ");
        sbInicial.append("   (SELECT temp.ID_RASTREO,temp.ID_TERMINO, temp.EN_SINGULAR, temp.USUARIO, temp.FECHA, temp.URL_TERMINO, temp.GRAVEDAD ");
        sbInicial.append("    FROM ( ");
        sbInicial.append("          SELECT r.ID_RASTREO,rs.ID_TERMINO, rs.EN_SINGULAR, r.USUARIO, r.FECHA, rs.URL_TERMINO, ");
        sbInicial.append("                  IF (rs.EN_SINGULAR=0,(SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ");
        sbInicial.append("                                        (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ");
        sbInicial.append(" AS GRAVEDAD ");
        sbInicial.append("           FROM SEXISTA_RASTREOS r LEFT JOIN SEXISTA_RESULTADOS rs ON r.ID_RASTREO=rs.ID_RASTREO AND rs.ID_TERMINO <>0) AS temp ");
        sbInicial.append("           WHERE temp.ID_RASTREO = ? ");

        sbFinal.append(" ) AS agrupacion ");
        sbFinal.append(" GROUP BY ID_RASTREO ");


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

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<RastreoExtDto> listado = new ArrayList<RastreoExtDto>();

        String formato = ConfigUtil.getConfiguracion().getProperty("config.formato.fecha");

        SimpleDateFormat sdf = new SimpleDateFormat(formato);

        StringBuilder sb = new StringBuilder();
        sb.append(sbInicial.toString())
                .append(sbFiltros.toString())
                .append(sbFinal.toString());


        try {
            con = ConexionBBDD.conectar();

            ps = con.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getRastreo().getIdRastreo());

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
            DAOUtils.close(con, ps, rs);
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
            sb.append(" WHERE rs.id_rastreo = ? ");
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
        List<String> listado = new ArrayList<String>();

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
    public List<Integer> searchIdTerminosDetalle(DetalleDto dto) throws BusinessException {
        StringBuilder sb = new StringBuilder();

        if (InformesConstants.GRAVEDAD_TODOS.equalsIgnoreCase(dto.getGravedad())) {
            sb.append("SELECT DISTINCT(rs.ID_TERMINO) AS ID_TERMINO");
            sb.append(" FROM SEXISTA_RESULTADOS rs ");
            sb.append(" WHERE rs.id_rastreo =  ? ");
            sb.append(" AND rs.ID_TERMINO <> 0  ");
        } else {
            sb.append(" SELECT DISTINCT(TERMINO) AS ID_TERMINO FROM ( ").
                    append(" SELECT rs.ID_TERMINO AS TERMINO, IF (rs.EN_SINGULAR=0, ").
                    append(" (SELECT PP FROM SEXISTA_TERMINOS WHERE ID_TERMINO = rs.ID_TERMINO), ").
                    append(" (SELECT PS FROM SEXISTA_TERMINOS WHERE ID_TERMINO= rs.ID_TERMINO)) ").
                    append(" AS GRAVEDAD ").
                    append(" FROM SEXISTA_RESULTADOS rs, SEXISTA_TERMINOS t ").
                    append(" WHERE t.ID_TERMINO = rs.ID_TERMINO ").
                    append(" AND rs.ID_TERMINO <> 0 ").
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
        List<Integer> listado = new ArrayList<Integer>();

        try {
            con = ConexionBBDD.conectar();

            ps = con.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getRastreo().getIdRastreo());

            rs = ps.executeQuery();

            while (rs.next()) {
                listado.add(Integer.valueOf(rs.getInt("ID_TERMINO")));
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
     * Busca los datos generales del término encontrado.
     *
     * @param dto <b>TerminoDetalleDto</b>
     * @return <b>TerminoDetalleDto</b>
     * @throws BusinessException en caso de error.
     */
    public TerminoDetalleDto searchTerminoDetalle(TerminoDetalleDto dto) throws BusinessException {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT T.TERMINO, RS.ID_TERMINO, COUNT(RS.ID_TERMINO) AS NUM_OCURRENCIAS ");
        sb.append(" FROM SEXISTA_RESULTADOS RS, SEXISTA_TERMINOS t ");
        sb.append(" WHERE RS.ID_TERMINO = T.ID_TERMINO ");
        sb.append(" AND RS.ID_TERMINO = ? ");
        sb.append(" AND RS.ID_RASTREO = ? ");
        sb.append(" GROUP BY RS.ID_TERMINO ");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBBDD.conectar();

            ps = con.prepareStatement(sb.toString().toUpperCase());

            ps.setInt(1, dto.getIdTermino());
            ps.setLong(2, dto.getIdRastreo());

            rs = ps.executeQuery();

            if (rs.next()) {
                dto.setNombre(rs.getString("TERMINO"));
                dto.setNumOcurrencias(rs.getInt("NUM_OCURRENCIAS"));
            }

        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(con, ps, rs);
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
    public List<ResultadoDto> searchContextosTerminoDetalle(Connection conn, TerminoDetalleDto dto)
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
        sb.append("       AND rs.ID_TERMINO = ? ) AS TEMP ");

        if (InformesConstants.GRAVEDAD_MEDIA_ALTA.equalsIgnoreCase(dto.getGravedad())) {
            sb.append(" WHERE GRAVEDAD >= ").append(InformesConstants.GRAVEDAD_MEDIA_ALTA_VALOR);
        } else if (InformesConstants.GRAVEDAD_ALTA.equalsIgnoreCase(dto.getGravedad())) {
            sb.append(" WHERE GRAVEDAD = ").append(InformesConstants.GRAVEDAD_ALTA_VALOR);
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ResultadoDto> listado = new ArrayList<ResultadoDto>();

        try {
            ps = conn.prepareStatement(sb.toString().toUpperCase());

            ps.setLong(1, dto.getIdRastreo());
            ps.setInt(2, dto.getIdTermino());

            rs = ps.executeQuery();

            while (rs.next()) {
                ResultadoDto rdto = new ResultadoDto();
                String contextoFormateado = formatearContexto(conn, dto.getNombre(), rs.getString("CONTEXTO"));
                rdto.setContexto(contextoFormateado);

                rdto.setUrlTermino(rs.getString("URL_TERMINO"));
                rdto.setIdResultado(rs.getInt("ID_RESULTADO"));
                rdto.setIdTermino(rs.getInt("ID_TERMINO"));
                rdto.setIdRastreo(rs.getLong("ID_RASTREO"));

                String gravedad = rs.getString("GRAVEDAD");

                String language = "language." + Locale.getDefault().getLanguage();

                PropertiesManager pmgr = new PropertiesManager();
                String firstLocaleParam = pmgr.getValue("language.properties", language).substring(0,
                        pmgr.getValue("language.properties", language).indexOf(':'));
                String secondLocaleParam = pmgr.getValue("language.properties", language).
                        substring(pmgr.getValue("language.properties", language).indexOf(':') + 1);
                Locale newLocale = new Locale(firstLocaleParam, secondLocaleParam);

                ResourceBundle resb = ResourceBundle.getBundle("i18n/sub/ApplicationResources", newLocale);

                if (InformesConstants.GRAVEDAD_BAJA_VALOR.equals(gravedad)) {
                    rdto.setGravedad(resb.getString(InformesConstants.GRAVEDAD_BAJA_LABEL));
                } else if (InformesConstants.GRAVEDAD_MEDIA_ALTA_VALOR.equals(gravedad)) {
                    rdto.setGravedad(resb.getString(InformesConstants.GRAVEDAD_MEDIA_LABEL));
                } else {
                    rdto.setGravedad(resb.getString(InformesConstants.GRAVEDAD_ALTA_LABEL));
                }


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
        List<SugerenciaDto> listado = new ArrayList<SugerenciaDto>();

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
        UtilProcessingText util = new UtilProcessingText();

        try {
            //Recuperamos los terminos compuestos
            ArrayList<TerminoDto> lstTerminosCompuestos;

            lstTerminosCompuestos = terminosService.findTerminosCompuestos(conn);

            //1- Separamos el texto por palabras y las almacenamos de forma consecutiva en un array.
            ArrayList<String> lstTerminos = util.splitText(contexto, lstTerminosCompuestos);

            contextoFormateado = new StringBuffer(CommonsConstants.CADENA_VACIA);

            for (int i = 0; i < lstTerminos.size(); i++) {
                String palabra = lstTerminos.get(i);

                if (isTerminoOnContext(termino, palabra)) {
                    StringBuffer palabraNegrita = new StringBuffer(CommonsConstants.TAG_INI_NEGRITA);
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
}
