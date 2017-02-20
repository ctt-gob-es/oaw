package es.inteco.rastreador2.dao.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.observatorio.*;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static org.apache.commons.lang3.StringUtils.leftPad;


public final class ObservatorioDAO {

    private ObservatorioDAO() {
    }

    public static ObservatorioRealizadoForm getFulfilledObservatory(Connection c, long idObservatory, long idObservatoryExecution) throws SQLException {
        final ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();

        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        try (PreparedStatement ps = c.prepareStatement("SELECT oreal.*, c.aplicacion, o.nombre FROM observatorios_realizados oreal " +
                "LEFT JOIN cartucho c ON (c.id_cartucho = oreal.id_cartucho) " +
                "JOIN observatorio o ON (oreal.id_observatorio = o.id_observatorio) " +
                "WHERE oreal.id_observatorio = ? AND oreal.id = ?")) {
            ps.setLong(1, idObservatory);
            ps.setLong(2, idObservatoryExecution);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    observatorioRealizadoForm.setId(rs.getLong("id"));
                    observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
                    observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));

                    ObservatorioForm observatorio = new ObservatorioForm();
                    observatorio.setEstado(rs.getInt("estado"));
                    observatorio.setNombre(rs.getString("nombre"));
                    observatorioRealizadoForm.setObservatorio(observatorio);

                    CartuchoForm cartucho = new CartuchoForm();
                    cartucho.setId(rs.getLong("id_cartucho"));
                    cartucho.setName(rs.getString("aplicacion"));
                    observatorioRealizadoForm.setCartucho(cartucho);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error en getFulfilledObservatory", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return observatorioRealizadoForm;
    }

    public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date) throws SQLException {
        return getFulfilledObservatories(c, idObservatory, page, date, true);
    }

    public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date, boolean desc) throws SQLException {
        List<ObservatorioRealizadoForm> results = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String order = "DESC";
        if (!desc) {
            order = "ASC";
        }

        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;

        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        try {
            if (date == null) {
                if (page == Constants.NO_PAGINACION) {
                    ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " +
                            "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)" +
                            "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " +
                            "WHERE o.id_observatorio = ? ORDER BY o.fecha " + order);
                } else {
                    ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " +
                            "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)" +
                            "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " +
                            "WHERE o.id_observatorio = ? ORDER BY o.fecha " + order + " LIMIT ? OFFSET ?");

                    ps.setInt(2, pagSize);
                    ps.setInt(3, resultFrom);
                }
            } else {
                if (page == Constants.NO_PAGINACION) {
                    ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " +
                            "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)" +
                            "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " +
                            "WHERE o.id_observatorio = ? AND o.fecha <= ? ORDER BY o.fecha " + order);
                } else {
                    ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " +
                            "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)" +
                            "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " +
                            "WHERE o.id_observatorio = ? AND o.fecha <= ? ORDER BY o.fecha " + order + " LIMIT ? OFFSET ?");

                    ps.setInt(3, pagSize);
                    ps.setInt(4, resultFrom);
                }
                ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
            }
            ps.setLong(1, idObservatory);

            rs = ps.executeQuery();

            while (rs.next()) {
                ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
                observatorioRealizadoForm.setId(rs.getLong("id"));
                observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
                observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));

                ObservatorioForm observatorio = new ObservatorioForm();
                observatorio.setEstado(rs.getInt("estado"));
                observatorio.setNombre(rs.getString("ob.nombre"));
                observatorioRealizadoForm.setObservatorio(observatorio);

                CartuchoForm cartucho = new CartuchoForm();
                cartucho.setId(rs.getLong("id_cartucho"));
                cartucho.setName(rs.getString("aplicacion"));
                observatorioRealizadoForm.setCartucho(cartucho);

                results.add(observatorioRealizadoForm);
            }

        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static List<ObservatorioRealizadoForm> getAllFulfilledObservatories(Connection c) throws SQLException {
        final List<ObservatorioRealizadoForm> results = new ArrayList<>();

        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        try (Statement s = c.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT o.id, o.fecha, o.id_cartucho, c.aplicacion FROM observatorios_realizados o " +
                    "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho)")) {
                while (rs.next()) {
                    final ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
                    observatorioRealizadoForm.setId(rs.getLong("id"));
                    observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
                    observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));

                    final CartuchoForm cartucho = new CartuchoForm();
                    cartucho.setId(rs.getLong("id_cartucho"));
                    cartucho.setName(rs.getString("aplicacion"));
                    observatorioRealizadoForm.setCartucho(cartucho);

                    results.add(observatorioRealizadoForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error en getAllFulfilledObservatories", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return results;
    }

    public static int countFulfilledObservatories(Connection c, long idObservatory) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(id) FROM observatorios_realizados WHERE id_observatorio = ?")) {
            ps.setLong(1, idObservatory);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<ObservatorioForm> getObservatoryList(Connection c) throws SQLException {
        final List<ObservatorioForm> results = new ArrayList<>();

        try (Statement s = c.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT o.id_observatorio, o.nombre, o.fecha_inicio, o.id_cartucho, p.dias, p.cronExpression " +
                    "FROM observatorio o " +
                    "JOIN periodicidad p ON (o.id_periodicidad = p.id_periodicidad) " +
                    "WHERE o.activo = true;")) {
                while (rs.next()) {
                    ObservatorioForm observatorioForm = new ObservatorioForm();
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setNombre(rs.getString("nombre"));
                    observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));

                    PeriodicidadForm periodicidadForm = new PeriodicidadForm();
                    periodicidadForm.setCronExpression(rs.getString("cronExpression"));
                    periodicidadForm.setDias(rs.getInt("dias"));
                    observatorioForm.setPeriodicidadForm(periodicidadForm);

                    CartuchoForm cartuchoForm = new CartuchoForm();
                    cartuchoForm.setId(rs.getLong("id_cartucho"));
                    observatorioForm.setCartucho(cartuchoForm);

                    results.add(observatorioForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error en getObservatoryList", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return results;
    }

    public static List<ObservatorioForm> getObservatoriesFromSeed(final Connection c, final String idSeed) throws SQLException {
        final List<ObservatorioForm> observatoryFormList = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement("SELECT o.id_observatorio, o.nombre FROM observatorio o " +
                "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " +
                "WHERE r.semillas = ?")) {
            ps.setLong(1, Long.parseLong(idSeed));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final ObservatorioForm observatorioForm = new ObservatorioForm();
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setNombre(rs.getString("nombre"));
                    observatoryFormList.add(observatorioForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return observatoryFormList;
    }

    public static CategoriaForm getCategoryById(final Connection c, final Long id) throws SQLException {
        final CategoriaForm category = new CategoriaForm();

        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM categorias_lista WHERE id_categoria = ? ORDER BY id_categoria ASC")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category.setId(String.valueOf(rs.getLong("id_categoria")));
                    category.setName(rs.getString("nombre"));
                    category.setOrden(rs.getInt("orden"));
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return category;
    }

    public static List<ObservatorioForm> getObservatoriesFromCategory(Connection c, String idCategoria) throws SQLException {
        final List<ObservatorioForm> observatoryFormList = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT(o.id_observatorio), o.nombre, o.id_language, o.profundidad, o.amplitud, o.id_cartucho FROM observatorio o " +
                "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " +
                "JOIN observatorio_categoria oc ON (o.id_observatorio = oc.id_observatorio) " +
                "WHERE oc.id_categoria = ? OR r.id_rastreo IN (SELECT " +
                "id_rastreo FROM rastreo WHERE semillas IN (SELECT " +
                "id_lista FROM lista WHERE id_categoria = ?))")) {
            ps.setLong(1, Long.parseLong(idCategoria));
            ps.setLong(2, Long.parseLong(idCategoria));
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ObservatorioForm observatorioForm = new ObservatorioForm();
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setNombre(rs.getString("nombre"));
                    observatorioForm.setLenguaje(rs.getLong("id_language"));
                    observatorioForm.setProfundidad(rs.getInt("profundidad"));
                    observatorioForm.setAmplitud(rs.getInt("amplitud"));

                    CartuchoForm cartuchoForm = new CartuchoForm();
                    cartuchoForm.setId(rs.getLong("id_cartucho"));
                    observatorioForm.setCartucho(cartuchoForm);

                    List<CategoriaForm> selectedCategories = getObservatoryCategories(c, observatorioForm.getId());
                    observatorioForm.setCategoria(new String[selectedCategories.size()]);
                    int i = 0;
                    for (CategoriaForm categoria : selectedCategories) {
                        observatorioForm.getCategoria()[i] = categoria.getId();
                        i++;
                    }

                    observatoryFormList.add(observatorioForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return observatoryFormList;
    }

    public static CargarObservatorioForm userList(Connection c, CargarObservatorioForm cargarObservatorioForm) throws SQLException {
        final List<ListadoObservatorio> observatoryUserList = new ArrayList<>();

        try (Statement s = c.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT nombre, id_observatorio FROM observatorio o")) {
                int numObs = 0;
                while (rs.next()) {
                    numObs++;
                    ListadoObservatorio ls = new ListadoObservatorio();
                    ls.setNombreObservatorio(rs.getString("nombre"));
                    ls.setId_observatorio(rs.getLong("id_observatorio"));
                    observatoryUserList.add(ls);
                }
                cargarObservatorioForm.setListadoObservatorio(observatoryUserList);
                cargarObservatorioForm.setNumObservatorios(numObs);
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return cargarObservatorioForm;
    }

    public static List<SemillaForm> getObservatorySeeds(final Connection c) throws SQLException {
        final List<SemillaForm> seedList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT id_lista, nombre FROM lista WHERE id_tipo_lista = ?")) {
            ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final SemillaForm semillaForm = new SemillaForm();
                    semillaForm.setId(rs.getLong("id_lista"));
                    semillaForm.setNombre(rs.getString("nombre"));
                    seedList.add(semillaForm);
                }
                return seedList;
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static CargarObservatorioForm observatoryList(final Connection c, int page) throws SQLException {
        final CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
        final List<ListadoObservatorio> observatoryList = new ArrayList<>();
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        final int resultFrom = pagSize * page;

        try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT(o.nombre), o.id_observatorio, c.id_cartucho, c.aplicacion, ot.name" +
                " FROM observatorio o JOIN cartucho c ON (o.id_cartucho = c.id_cartucho) JOIN observatorio_tipo ot ON (o.id_tipo=ot.id_tipo)" +
                " LIMIT ? OFFSET ?")) {
            ps.setInt(1, pagSize);
            ps.setInt(2, resultFrom);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final ListadoObservatorio ls = new ListadoObservatorio();
                    ls.setNombreObservatorio(rs.getString("nombre"));
                    ls.setId_observatorio(rs.getLong("id_observatorio"));
                    ls.setId_cartucho(rs.getLong("id_cartucho"));
                    ls.setCartucho(rs.getString("aplicacion"));
                    ls.setTipo(rs.getString("name"));
                    observatoryList.add(ls);
                }
                cargarObservatorioForm.setListadoObservatorio(observatoryList);
                cargarObservatorioForm.setNumObservatorios(observatoryList.size());
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return cargarObservatorioForm;
    }

    public static int countObservatories(Connection c) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(id_observatorio) FROM observatorio");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static int countSeeds(Connection c, long idObservatorio) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM observatorio_lista WHERE id_observatorio = ?")) {
            ps.setLong(1, idObservatorio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void deteleObservatory(final Connection c, final long idObservatory) throws SQLException {
        final boolean isAutoCommit = c.getAutoCommit();
        try {
            c.setAutoCommit(false);

            //RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
            final List<Long> idsRastreos = getCrawlerIds(c, idObservatory);

            //BORRAMOS RASTREOS Y RESULTADOS
            for (Long idRastreo : idsRastreos) {
                RastreoDAO.borrarRastreo(c, idRastreo);
            }

            //ELIMINAMOS LAS RELACIONES OBSERVATORIO_SEMILLA
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?")) {
                ps.setLong(1, idObservatory);
                ps.executeUpdate();
                DAOUtils.closeQueries(ps, null);
            }

            //ELIMINAMOS EL OBSERVATORIO
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio WHERE id_observatorio = ?")) {
                ps.setLong(1, idObservatory);
                ps.executeUpdate();
            }

            c.commit();
            c.setAutoCommit(isAutoCommit);
        } catch (SQLException e) {
            c.rollback();
            c.setAutoCommit(isAutoCommit);
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void deteleFulfilledObservatory(Connection connR, long idExecution) throws SQLException {
        PreparedStatement ps = null;

        try {
            connR.setAutoCommit(false);

            //RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
            List<Long> crawlingExecutionsIds = getFulfilledCrawlingIds(connR, idExecution);

            RastreoDAO.deleteAnalyse(connR, crawlingExecutionsIds);

            //ELIMINAMOS EL OBSERVATORIO
            ps = connR.prepareStatement("DELETE FROM observatorios_realizados WHERE id = ?");
            ps.setLong(1, idExecution);
            ps.executeUpdate();

            connR.commit();
        } catch (SQLException e) {
            try {
                connR.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    private static List<Long> getCrawlerIds(Connection connR, long idObservatorio) throws SQLException {
        final List<Long> crawlerIds = new ArrayList<>();
        //RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
        try (PreparedStatement ps = connR.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE id_observatorio = ?")) {
            ps.setLong(1, idObservatorio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    crawlerIds.add(rs.getLong("id_rastreo"));
                }
                return crawlerIds;
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static List<Long> getFulfilledCrawlingIds(Connection connR, long idExecution) throws SQLException {
        final List<Long> crawlerIds = new ArrayList<>();
        try (PreparedStatement ps = connR.prepareStatement("SELECT id FROM rastreos_realizados rr WHERE id_obs_realizado = ?")) {
            ps.setLong(1, idExecution);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    crawlerIds.add(rs.getLong("id"));
                }
                return crawlerIds;
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<FulFilledCrawling> getFulfilledCrawlingByObservatoryExecution(Connection connR, long idExecution) throws SQLException {
        final List<FulFilledCrawling> crawlings = new ArrayList<>();
        try (PreparedStatement ps = connR.prepareStatement("SELECT * FROM rastreos_realizados rr JOIN lista l ON (l.id_lista = rr.id_lista) " +
                "JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) WHERE id_obs_realizado = ?")) {
            ps.setLong(1, idExecution);
            //RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
                    fulfilledCrawling.setId(rs.getLong("id"));
                    fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
                    fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
                    fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
                    crawlings.add(fulfilledCrawling);
                    SemillaForm seed = new SemillaForm();
                    seed.setId(rs.getLong("l.id_lista"));
                    seed.setNombre(rs.getString("l.nombre"));
                    CategoriaForm category = new CategoriaForm();
                    category.setId(String.valueOf(rs.getLong("cl.id_categoria")));
                    category.setName(rs.getString("cl.nombre"));
                    category.setOrden(rs.getInt("cl.orden"));
                    seed.setCategoria(category);
                    fulfilledCrawling.setSeed(seed);
                }
                return crawlings;
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static long insertObservatory(Connection c, NuevoObservatorioForm nuevoObservatorioForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c.setAutoCommit(false);

            ps = c.prepareStatement("INSERT INTO observatorio (nombre, id_periodicidad, profundidad, amplitud, fecha_inicio, id_guideline, pseudoaleatorio, id_language, id_cartucho, activo, id_tipo) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, nuevoObservatorioForm.getNombre());
            ps.setLong(2, Long.parseLong(nuevoObservatorioForm.getPeriodicidad()));
            ps.setInt(3, Integer.parseInt(nuevoObservatorioForm.getProfundidad()));
            ps.setInt(4, Integer.parseInt(nuevoObservatorioForm.getAmplitud()));
            ps.setTimestamp(5, new Timestamp(nuevoObservatorioForm.getFecha().getTime()));
            ps.setLong(6, CartuchoDAO.getGuideline(c, nuevoObservatorioForm.getCartucho().getId()));
            ps.setBoolean(7, Boolean.valueOf(nuevoObservatorioForm.getPseudoAleatorio()));
            ps.setLong(8, nuevoObservatorioForm.getLenguaje());
            ps.setLong(9, nuevoObservatorioForm.getCartucho().getId());
            ps.setBoolean(10, nuevoObservatorioForm.isActivo());
            ps.setLong(11, nuevoObservatorioForm.getTipo().getId());
            ps.executeUpdate();

            long idObservatory = 0;
            ps = c.prepareStatement("SELECT id_observatorio FROM observatorio WHERE Nombre = ?");
            ps.setString(1, nuevoObservatorioForm.getNombre());
            rs = ps.executeQuery();
            if (rs.next()) {
                idObservatory = rs.getLong("id_observatorio");
            }

            ps = c.prepareStatement("INSERT INTO observatorio_categoria VALUES (?,?)");
            if (nuevoObservatorioForm.getCategoria() != null) {
                for (String categoria : nuevoObservatorioForm.getCategoria()) {
                    ps.setLong(1, idObservatory);
                    ps.setLong(2, Long.parseLong(categoria));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            if ((idObservatory != 0) && (nuevoObservatorioForm.getAddSeeds() != null) && (!nuevoObservatorioForm.getAddSeeds().isEmpty())) {
                for (SemillaForm semillaForm : nuevoObservatorioForm.getAddSeeds()) {
                    ps = c.prepareStatement("INSERT INTO observatorio_lista VALUES(?,?)");
                    ps.setLong(1, idObservatory);
                    ps.setLong(2, semillaForm.getId());
                    ps.executeUpdate();
                }
            }

            List<SemillaForm> totalSeedsAdded = nuevoObservatorioForm.getAddSeeds() == null ? new ArrayList<SemillaForm>() : nuevoObservatorioForm.getAddSeeds();
            if (nuevoObservatorioForm.getCategoria() != null) {
                for (String categoria : nuevoObservatorioForm.getCategoria()) {
                    totalSeedsAdded.addAll(SemillaDAO.getSeedsByCategory(c, Long.parseLong(categoria), Constants.NO_PAGINACION, new SemillaForm()));
                }
            }
            insertNewCrawlers(c, idObservatory, totalSeedsAdded);

            c.commit();
            return idObservatory;
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static VerObservatorioForm getObservatoryView(Connection c, long idObservatorio, int page) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        PropertiesManager pmgr = new PropertiesManager();

        VerObservatorioForm verObservatorioForm = new VerObservatorioForm();

        try {
            ps = c.prepareStatement("SELECT DISTINCT(o.nombre) AS nombreObservatorio , o.id_guideline , " +
                    "ca.* ,ot.*, p.nombre " +
                    "AS periodicidad, cl.nombre AS nombreCategoria, profundidad, amplitud, pseudoaleatorio, activo FROM observatorio o " +
                    "JOIN periodicidad p ON (p.id_periodicidad = o.id_periodicidad) " +
                    "LEFT JOIN observatorio_categoria oc ON (o.id_observatorio = oc.id_observatorio) " +
                    "LEFT JOIN categorias_lista cl ON (cl.id_categoria = oc.id_categoria) " +
                    "LEFT JOIN cartucho ca ON (o.id_cartucho = ca.id_cartucho) " +
                    "LEFT JOIN observatorio_tipo ot ON (o.id_tipo = ot.id_tipo) " +
                    "WHERE o.id_observatorio = ? GROUP BY o.nombre;");
            ps.setLong(1, idObservatorio);
            rs = ps.executeQuery();
            if (rs.next()) {
                verObservatorioForm.setAmplitud(rs.getString("amplitud"));
                verObservatorioForm.setProfundidad(rs.getString("profundidad"));
                verObservatorioForm.setPseudoAleatorio(String.valueOf(rs.getBoolean("pseudoaleatorio")));
                verObservatorioForm.setNombre(rs.getString("nombreObservatorio"));
                verObservatorioForm.setPeriodicidad(rs.getString("periodicidad"));
                verObservatorioForm.setNormaAnalisis(rs.getLong("id_guideline"));
                verObservatorioForm.setActivo(rs.getBoolean("activo"));

                CartuchoForm cartucho = new CartuchoForm();
                cartucho.setId(rs.getLong("ca.id_cartucho"));
                cartucho.setName(rs.getString("ca.aplicacion"));
                verObservatorioForm.setCartucho(cartucho);

                ObservatoryTypeForm tipo = new ObservatoryTypeForm();
                tipo.setId(rs.getLong("ot.id_tipo"));
                tipo.setName(rs.getString("ot.name"));
                verObservatorioForm.setTipo(tipo);

                verObservatorioForm.setCategorias(getObservatoryCategories(c, idObservatorio));
            }
            DAOUtils.closeQueries(ps, rs);

            int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
            int resultFrom = pagSize * page;
            List<SemillaForm> semillasList = new ArrayList<>();
            ps = c.prepareStatement("SELECT * FROM lista l " +
                    "JOIN observatorio_lista ol ON (l.id_lista = ol.id_lista) " +
                    "JOIN observatorio o ON (o.id_observatorio = ol.id_observatorio) " +
                    "WHERE o.id_observatorio = ? LIMIT ? OFFSET ? ");
            ps.setLong(1, idObservatorio);
            ps.setInt(2, pagSize);
            ps.setInt(3, resultFrom);
            rs = ps.executeQuery();
            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setNombre(rs.getString("nombre"));
                semillaForm.setListaUrlsString(rs.getString("lista"));
                semillasList.add(semillaForm);
            }
            verObservatorioForm.setSemillasList(semillasList);
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return verObservatorioForm;
    }

    public static ObservatorioForm getObservatoryForm(final Connection c, final long idObservatorio) throws SQLException {
        final ObservatorioForm observatorioForm = new ObservatorioForm();

        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio WHERE id_observatorio = ?")) {
            ps.setLong(1, idObservatorio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setAmplitud(rs.getLong("amplitud"));
                    observatorioForm.setProfundidad(rs.getInt("profundidad"));
                    observatorioForm.setNombre(rs.getString("nombre"));
                    observatorioForm.setPeriodicidad(rs.getLong("id_periodicidad"));
                    observatorioForm.setId_guideline(rs.getLong("id_guideline"));
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                    observatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
                    observatorioForm.setLenguaje(rs.getLong("id_language"));
                    observatorioForm.setTipo(rs.getLong("id_tipo"));

                    final CartuchoForm cartuchoForm = new CartuchoForm();
                    cartuchoForm.setId(rs.getLong("id_cartucho"));
                    observatorioForm.setCartucho(cartuchoForm);

                    final List<CategoriaForm> selectedCategories = getObservatoryCategories(c, idObservatorio);
                    observatorioForm.setCategoria(new String[selectedCategories.size()]);
                    int i = 0;
                    for (CategoriaForm categoria : selectedCategories) {
                        observatorioForm.getCategoria()[i] = categoria.getId();
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return observatorioForm;
    }

    public static ObservatorioForm getObservatoryFormFromExecution(Connection c, long idObservatorioExecution) throws SQLException {
        final ObservatorioForm observatorioForm = new ObservatorioForm();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio o JOIN observatorios_realizados ore " +
                "ON (o.id_observatorio = ore.id_observatorio) WHERE ore.id = ?;")) {
            ps.setLong(1, idObservatorioExecution);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setAmplitud(rs.getLong("amplitud"));
                    observatorioForm.setProfundidad(rs.getInt("profundidad"));
                    observatorioForm.setNombre(rs.getString("nombre"));
                    observatorioForm.setPeriodicidad(rs.getLong("id_periodicidad"));
                    observatorioForm.setId_guideline(rs.getLong("id_guideline"));
                    observatorioForm.setId(rs.getLong("id_observatorio"));
                    observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                    observatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
                    observatorioForm.setLenguaje(rs.getLong("id_language"));
                    observatorioForm.setTipo(rs.getLong("id_tipo"));

                    CartuchoForm cartuchoForm = new CartuchoForm();
                    cartuchoForm.setId(rs.getLong("id_cartucho"));
                    observatorioForm.setCartucho(cartuchoForm);
                }
                return observatorioForm;
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static ModificarObservatorioForm getObservatoryDataToUpdate(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio o WHERE o.id_observatorio = ?")) {
            ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    populateModificarObservatorioFormFromResultSet(c, modificarObservatorioForm, rs);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return modificarObservatorioForm;
    }

    private static void populateModificarObservatorioFormFromResultSet(Connection c, ModificarObservatorioForm modificarObservatorioForm, ResultSet rs) throws SQLException {
        modificarObservatorioForm.setAmplitud(String.valueOf(rs.getInt("amplitud")));
        modificarObservatorioForm.setFecha(rs.getTimestamp("fecha_inicio"));
        modificarObservatorioForm.setNombre(rs.getString("nombre"));
        modificarObservatorioForm.setPeriodicidad(rs.getString("id_periodicidad"));
        modificarObservatorioForm.setProfundidad(String.valueOf(rs.getInt("profundidad")));
        modificarObservatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
        modificarObservatorioForm.setNormaAnalisis(rs.getString("id_guideline"));
        modificarObservatorioForm.setActivo(rs.getBoolean("activo"));

        final CartuchoForm cartuchoForm = new CartuchoForm();
        cartuchoForm.setId(rs.getLong("id_cartucho"));
        modificarObservatorioForm.setCartucho(cartuchoForm);

        final ObservatoryTypeForm tipo = new ObservatoryTypeForm();
        tipo.setId(rs.getLong("id_tipo"));
        modificarObservatorioForm.setTipo(tipo);

        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));

        final Calendar cl1 = Calendar.getInstance();
        cl1.setTime(modificarObservatorioForm.getFecha());
        modificarObservatorioForm.setFechaInicio(df.format(cl1.getTime()));
        final String hour = leftPad(String.valueOf(cl1.get(Calendar.HOUR_OF_DAY)), 2, '0');
        final String minute = leftPad(String.valueOf(cl1.get(Calendar.MINUTE)), 2, '0');
        modificarObservatorioForm.setHoraInicio(hour);
        modificarObservatorioForm.setMinutoInicio(minute);

        modificarObservatorioForm.setSemillasAnadidas(getSeedsFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));
        modificarObservatorioForm.setSemillasNoAnadidas(getSeedsNotObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));

        final List<CategoriaForm> selectedCategories = getObservatoryCategories(c, Long.valueOf(modificarObservatorioForm.getId_observatorio()));
        modificarObservatorioForm.setCategoria(new String[selectedCategories.size()]);
        final List<String> categoriesId = new ArrayList<>(selectedCategories.size());
        for (CategoriaForm categoria : selectedCategories) {
            categoriesId.add(categoria.getId());
        }
        modificarObservatorioForm.setCategoria(categoriesId.toArray(new String[categoriesId.size()]));
    }

    public static List<ResultadoSemillaForm> getResultSeedsFromObservatory(final Connection c, final SemillaForm searchForm, final Long idObservatorio, final Long idCategoria, final int page) throws SQLException {
        final PropertiesManager pmgr = new PropertiesManager();
        final List<ResultadoSemillaForm> semillasFormList = new ArrayList<>();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"));
        final int resultFrom = pagSize * page;
        int paramCount = 1;

        String query = "SELECT l.id_lista, l.nombre, r.activo, r.id_rastreo, l.id_categoria, rr.id FROM lista l " +
                "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) " +
                "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) " +
                "WHERE id_obs_realizado = ? ";

        if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
            query += " AND l.lista like ?";
        }

        if (StringUtils.isNotEmpty(searchForm.getNombre())) {
            query += " AND l.nombre like ?";
        }

        if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
            query += " AND l.id_categoria = ?";
        }

        if (page != Constants.NO_PAGINACION) {
            query += " LIMIT ? OFFSET ?";
        }

        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setLong(paramCount++, idObservatorio);

            if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
                ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
            }

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
            }

            if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
                ps.setLong(paramCount++, idCategoria);
            }

            if (page != Constants.NO_PAGINACION) {
                ps.setInt(paramCount++, pagSize);
                ps.setInt(paramCount, resultFrom);
            }
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    final ResultadoSemillaForm resultadoSemillaForm = new ResultadoSemillaForm();
                    resultadoSemillaForm.setId(rs.getString("l.id_lista"));
                    resultadoSemillaForm.setName(rs.getString("l.nombre"));
                    resultadoSemillaForm.setActive(rs.getBoolean("r.activo"));
                    resultadoSemillaForm.setIdCrawling(rs.getString("r.id_rastreo"));
                    resultadoSemillaForm.setIdCategory(rs.getLong("l.id_categoria"));
                    resultadoSemillaForm.setIdFulfilledCrawling(rs.getString("rr.id"));
                    semillasFormList.add(resultadoSemillaForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return semillasFormList;
    }

    public static int countResultSeedsFromObservatory(Connection c, SemillaForm searchForm, long idExecutionObservatory, long idCategory) throws SQLException {
        int paramCount = 1;

        String query = "SELECT COUNT(*) FROM lista l " +
                "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) " +
                "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) " +
                "WHERE id_obs_realizado = ? ";

        if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
            query += " AND l.lista like ?";
        }

        if (StringUtils.isNotEmpty(searchForm.getNombre())) {
            query += " AND l.nombre like ?";
        }

        if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
            query += " AND l.id_categoria = ?";
        }

        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setLong(paramCount++, idExecutionObservatory);

            if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
                ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
            }

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
            }

            if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
                ps.setLong(paramCount, idCategory);
            }
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return 0;
    }

    private static List<SemillaForm> getSeedsFromObservatory(Connection c, long idObservatorio) throws SQLException {
        final List<SemillaForm> semillasFormList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM lista l " +
                "LEFT JOIN observatorio_lista ol ON (ol.id_lista = l.id_lista) " +
                "WHERE ol.id_observatorio = ? AND l.id_categoria IS NULL")) {
            ps.setLong(1, idObservatorio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final SemillaForm semillaForm = new SemillaForm();
                    semillaForm.setId(rs.getLong("id_lista"));
                    semillaForm.setNombre(rs.getString("nombre"));
                    semillasFormList.add(semillaForm);
                }
            }
            return semillasFormList;
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static List<SemillaForm> getSeedsNotObservatory(Connection c, long idObservatorio) throws SQLException {
        final List<SemillaForm> semillasFormList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM lista l WHERE l.id_tipo_lista = ? AND l.id_categoria IS NULL " +
                "AND (l.id_lista NOT IN (" +
                "SELECT ol.id_lista FROM observatorio_lista ol " +
                "WHERE ol.id_observatorio = ?)) ")) {
            ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
            ps.setLong(2, idObservatorio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SemillaForm semillaForm = new SemillaForm();
                    semillaForm.setId(rs.getLong("id_lista"));
                    semillaForm.setNombre(rs.getString("nombre"));
                    semillasFormList.add(semillaForm);
                }
            }
            return semillasFormList;
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static void updateObservatoryCrawlings(final Connection c, final ModificarObservatorioForm newObservatory, final ObservatorioForm oldObservatory) throws SQLException {
        final List<String> newCategories = Arrays.asList(newObservatory.getCategoria());
        final List<String> oldCategories = Arrays.asList(oldObservatory.getCategoria());

        // Desactivamos las categorías antiguas que no están entre las nuevas
        for (String oldCategory : oldCategories) {
            if (!newCategories.contains(oldCategory)) {
                final List<Long> crawlerIds = ObservatorioDAO.getCrawlerFromCategory(c, Long.parseLong(oldCategory));
                for (Long idCrawler : crawlerIds) {
                    RastreoDAO.enableDisableCrawler(c, idCrawler, false);
                }
            }
        }

        // Añadimos las categorías nuevas que no están entre las viejas
        for (String newCategory : newCategories) {
            final List<SemillaForm> seedsFromCategory = new ArrayList<>();
            if (!oldCategories.contains(newCategory)) {
                seedsFromCategory.addAll(SemillaDAO.getSeedsByCategory(c, Long.parseLong(newCategory), Constants.NO_PAGINACION, new SemillaForm()));
            }
            insertNewCrawlers(c, oldObservatory.getId(), seedsFromCategory);
        }
    }

    public static ModificarObservatorioForm updateObservatory(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {
        PreparedStatement ps = null;
        try {
            c.setAutoCommit(false);

            // Actualizamos los rastreos
            ObservatorioForm oldObservatory = getObservatoryForm(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            updateObservatoryCrawlings(c, modificarObservatorioForm, oldObservatory);

            ps = c.prepareStatement("UPDATE observatorio SET Nombre = ?, Id_Periodicidad = ?, Profundidad = ?, Amplitud = ?, Fecha_Inicio = ?, id_guideline = ?, pseudoaleatorio = ?, id_language = ?, id_cartucho = ?, activo = ?, id_tipo = ? WHERE id_observatorio = ?");

            ps.setString(1, modificarObservatorioForm.getNombre());
            ps.setInt(2, Integer.parseInt(modificarObservatorioForm.getPeriodicidad()));
            ps.setInt(3, Integer.parseInt(modificarObservatorioForm.getProfundidad()));
            ps.setInt(4, Integer.parseInt(modificarObservatorioForm.getAmplitud()));
            modificarObservatorioForm.setFecha(CrawlerUtils.getFechaInicio(modificarObservatorioForm.getFechaInicio(),
                    modificarObservatorioForm.getHoraInicio(), modificarObservatorioForm.getMinutoInicio()));
            ps.setTimestamp(5, new Timestamp(modificarObservatorioForm.getFecha().getTime()));

            ps.setLong(6, CartuchoDAO.getGuideline(c, modificarObservatorioForm.getCartucho().getId()));

            ps.setBoolean(7, modificarObservatorioForm.isPseudoAleatorio());
            ps.setLong(8, modificarObservatorioForm.getLenguaje());
            ps.setLong(9, modificarObservatorioForm.getCartucho().getId());

            ps.setBoolean(10, modificarObservatorioForm.isActivo());
            ps.setLong(11, modificarObservatorioForm.getTipo().getId());

            ps.setLong(12, Long.parseLong(modificarObservatorioForm.getId_observatorio()));

            ps.executeUpdate();
            DAOUtils.closeQueries(ps, null);

            // Rehacemos las asociaciones con las categorías
            ps = c.prepareStatement("DELETE FROM observatorio_categoria WHERE id_observatorio = ?");
            ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, null);

            ps = c.prepareStatement("INSERT INTO observatorio_categoria VALUES (?,?)");
            if (modificarObservatorioForm.getCategoria() != null) {
                for (String categoria : modificarObservatorioForm.getCategoria()) {
                    ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
                    ps.setLong(2, Long.parseLong(categoria));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            editCrawlings(c, modificarObservatorioForm);

            disableCrawlers(c, modificarObservatorioForm.getSemillasNoAnadidas(), modificarObservatorioForm.getId_observatorio());

            deleteSeedAssociation(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            addObservatorySeeds(c, modificarObservatorioForm.getSemillasAnadidas(), Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            //Insertamos los rastreos independientes de la categoría asociada
            insertNewCrawlers(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()), modificarObservatorioForm.getSemillasAnadidas());

            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
        return modificarObservatorioForm;
    }

    public static void putDataToInsert(final InsertarRastreoForm insertarRastreoForm, final ObservatorioForm observatorioForm) {
        insertarRastreoForm.setId_observatorio(observatorioForm.getId());
        insertarRastreoForm.setCartucho(observatorioForm.getCartucho().getId().toString());
        insertarRastreoForm.setNormaAnalisis(String.valueOf(observatorioForm.getId_guideline()));
        insertarRastreoForm.setProfundidad(observatorioForm.getProfundidad());
        insertarRastreoForm.setTopN(observatorioForm.getAmplitud());
        insertarRastreoForm.setPseudoAleatorio(observatorioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(observatorioForm.getLenguaje());
    }

    public static void insertNewCrawlers(final Connection c, final long idObservatory, final List<SemillaForm> seeds) throws SQLException {
        final InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
        final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
        putDataToInsert(insertarRastreoForm, observatorioForm);

        if (seeds != null) {
            for (SemillaForm semillaForm : seeds) {
                insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + SemillaDAO.getSeedById(c, semillaForm.getId()).getNombre());
                insertarRastreoForm.setId_semilla(semillaForm.getId());
                insertarRastreoForm.setId_observatorio(idObservatory);
                insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
                final Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, idObservatory, semillaForm.getId());
                if (idCrawler == -1) {
                    insertarRastreoForm.setActive(semillaForm.isActiva());
                    RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
                } else {
                    RastreoDAO.enableDisableCrawler(c, idCrawler, semillaForm.isActiva());
                }
            }
        }
    }

    private static void disableCrawlers(Connection c, List<SemillaForm> seeds, String idObservatory) throws SQLException {
        for (SemillaForm semillaForm : seeds) {
            //Desactivamos los rastreos de las semilla sque han sido desasignadas al observatorio
            Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, Long.parseLong(idObservatory), semillaForm.getId());
            if (idCrawler != -1) {
                RastreoDAO.enableDisableCrawler(c, idCrawler, false);
            }
        }
    }

    private static void deleteSeedAssociation(Connection c, long idObservatorio) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?")) {
            ps.setLong(1, idObservatorio);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static void addObservatorySeeds(final Connection c, final List<SemillaForm> seeds, final long idObservatory) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO observatorio_lista VALUES(?,?)")) {
            for (SemillaForm semillaForm : seeds) {
                ps.setLong(1, idObservatory);
                ps.setLong(2, semillaForm.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static Map<Long, Date> getObservatoryExecutionIds(Connection c, long observatoryId, long executionId, long cartridgeId) throws SQLException {
        final Map<Long, Date> observatoryExecutionIdsList = new HashMap<>();
        final PropertiesManager pmgr = new PropertiesManager();
        final String limit = pmgr.getValue(CRAWLER_PROPERTIES, "observatory.evolution.limit").trim();
        try (PreparedStatement ps = c.prepareStatement("SELECT id,fecha FROM observatorios_realizados" +
                " WHERE id_observatorio = ? AND id_cartucho = ? AND fecha <= (SELECT fecha FROM observatorios_realizados WHERE id = ?)" +
                " ORDER BY fecha DESC LIMIT ?;")) {
            ps.setLong(1, observatoryId);
            ps.setLong(2, cartridgeId);
            ps.setLong(3, executionId);
            ps.setInt(4, Integer.parseInt(limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    observatoryExecutionIdsList.put(rs.getLong("id"), rs.getTimestamp("fecha"));
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return observatoryExecutionIdsList;
    }

    private static long existObservatoryCrawl(final Connection c, final long idObservatory, final long idSeed) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                "JOIN lista l ON (r.semillas = l.id_lista) " +
                "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " +
                "WHERE l.id_lista = ? AND o.id_observatorio = ?")) {
            ps.setLong(1, idSeed);
            ps.setLong(2, idObservatory);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong("id_rastreo") : -1;
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    /**
     * Comprobamos que si existe un observatorio con un determinado nombre
     *
     * @param c               conexión Connection a la BD
     * @param observatoryName cadena String con el nombre del observatorio
     * @return true si existe un observatorio con ese nombre o false en caso contrario
     * @throws SQLException
     */
    public static boolean existObservatory(Connection c, String observatoryName) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM observatorio WHERE nombre = ?")) {
            ps.setString(1, observatoryName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static List<Long> getCrawlerFromCategory(final Connection c, final long idObservatory) throws SQLException {
        final List<Long> idCrawlerList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                "JOIN lista l ON (r.semillas = l.id_lista) " +
                "WHERE l.id_categoria = ?")) {
            ps.setLong(1, idObservatory);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    idCrawlerList.add(rs.getLong("id_rastreo"));
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return idCrawlerList;
    }

    private static List<Long> getCrawlerFromObservatory(Connection c, long idObservatory) throws SQLException {
        final List<Long> idCrawlerList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " +
                "WHERE o.id_observatorio = ?")) {
            ps.setLong(1, idObservatory);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    idCrawlerList.add(rs.getLong("id_rastreo"));
                }
            }
            return idCrawlerList;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static void editCrawlings(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws SQLException {
        InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
        insertarRastreoForm.setProfundidad(Integer.parseInt(modificarObservatorioForm.getProfundidad()));
        insertarRastreoForm.setTopN(Long.parseLong(modificarObservatorioForm.getAmplitud()));
        insertarRastreoForm.setCartucho(modificarObservatorioForm.getCartucho().getId().toString());
        insertarRastreoForm.setNormaAnalisis(modificarObservatorioForm.getNormaAnalisis());
        insertarRastreoForm.setPseudoAleatorio(modificarObservatorioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(modificarObservatorioForm.getLenguaje());
        List<Long> idCrawlerList = getCrawlerFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
        for (Long idCrawler : idCrawlerList) {
            SemillaForm semillaForm = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idCrawler));
            insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
            insertarRastreoForm.setCodigo(modificarObservatorioForm.getNombre() + "-" + semillaForm.getNombre());
            insertarRastreoForm.setId_semilla(semillaForm.getId());
            RastreoDAO.modificarRastreo(c, false, insertarRastreoForm, idCrawler);
        }
    }

    public static long getCartridgeFromExecutedObservatoryId(final Connection c, final Long observatoryId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT id_cartucho FROM rastreos_realizados WHERE id = ?")) {
            ps.setLong(1, observatoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_cartucho");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<Long> getSubsequentObservatoryExecutionIds(Connection c, Long observatoryId, Long executionId, Long cartridgeId) throws SQLException {
        final List<Long> subsequentExecutionIds = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT id FROM observatorios_realizados WHERE id_observatorio = ? AND " +
                "id_cartucho = ? AND fecha > (SELECT fecha FROM observatorios_realizados WHERE id = ? )")) {
            ps.setLong(1, observatoryId);
            ps.setLong(2, cartridgeId);
            ps.setLong(3, executionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subsequentExecutionIds.add(rs.getLong("id"));
                }
            }
            return subsequentExecutionIds;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void updateObservatoryStatus(Connection c, Long executedId, int estado) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE observatorios_realizados SET estado = ? WHERE id = ?")) {
            ps.setInt(1, estado);
            ps.setLong(2, executedId);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Error al modificar el estado de la ejecución con id: " + executedId + " del observatorio.", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<CategoriaForm> getObservatoryCategories(Connection c, Long idObservatory) throws SQLException {
        final List<CategoriaForm> observatoryCategories = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio_categoria oc " +
                "JOIN categorias_lista cl ON (cl.id_categoria = oc.id_categoria) " +
                "WHERE id_observatorio = ? ORDER BY cl.nombre")) {
            ps.setLong(1, idObservatory);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final CategoriaForm categoriaForm = new CategoriaForm();
                    categoriaForm.setId(rs.getString("oc.id_categoria"));
                    categoriaForm.setName(rs.getString("cl.nombre"));
                    categoriaForm.setOrden(rs.getInt("cl.orden"));
                    observatoryCategories.add(categoriaForm);
                }
            }
            return observatoryCategories;
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

    }

    public static List<CategoriaForm> getExecutionObservatoryCategories(final Connection c, final Long idExecutionObservatory) throws SQLException {
        final List<CategoriaForm> observatoryCategories = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT cl.nombre, cl.id_categoria, cl.orden FROM rastreos_realizados rr " +
                "JOIN lista l ON (l.id_lista = rr.id_lista) " +
                "JOIN observatorio_categoria oc ON (l.id_categoria = oc.id_categoria) " +
                "JOIN categorias_lista cl ON (oc.id_categoria = cl.id_categoria) " +
                "WHERE rr.id_obs_realizado = ? GROUP BY cl.id_categoria ORDER BY cl.orden, cl.nombre;")) {
            ps.setLong(1, idExecutionObservatory);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final CategoriaForm categoriaForm = new CategoriaForm();
                    categoriaForm.setId(rs.getString("cl.id_categoria"));
                    categoriaForm.setName(rs.getString("cl.nombre"));
                    categoriaForm.setOrden(rs.getInt("cl.orden"));
                    observatoryCategories.add(categoriaForm);
                }
            }
            return observatoryCategories;
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void saveMethodology(Connection c, Long idExecution, String methodology) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO observatorio_metodologia (id_obs_realizado, metodologia) VALUES (?,?)")) {
            ps.setLong(1, idExecution);
            ps.setString(2, methodology);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static String getMethodology(Connection c, Long idExecution) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio_metodologia WHERE id_obs_realizado = ?")) {
            ps.setLong(1, idExecution);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("metodologia");
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<ObservatoryTypeForm> getAllObservatoryTypes(Connection c) throws SQLException {
        final List<ObservatoryTypeForm> typeList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio_tipo")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ObservatoryTypeForm type = new ObservatoryTypeForm();
                    type.setId(rs.getLong("id_tipo"));
                    type.setName(rs.getString("name"));
                    typeList.add(type);
                }
            }
            return typeList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar la lista de roles", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    /**
     * Dada una ejecución de un observatorio obtiene el identificador de la ejecución inmediatamente previa del mismo observatorio
     *
     * @param c                      Conexión a la base de datos
     * @param idObservatoryExecution Identificador de la iteración (ejecución) de un observatorio
     * @return El identificador de la iteración anterior de ese observatorio o -1 si no existe
     */
    public static Long getPreviousObservatoryExecution(final Connection c, final Long idObservatoryExecution) {
        try (PreparedStatement ps = c.prepareStatement("SELECT id FROM observatorios_realizados " +
                "WHERE id_observatorio = ? AND id<? AND estado=0 ORDER BY fecha DESC")) {
            final long idObservatory = getObservatoryFormFromExecution(c, idObservatoryExecution).getId();
            ps.setLong(1, idObservatory);
            ps.setLong(2, idObservatoryExecution);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return -1L;
    }
}
