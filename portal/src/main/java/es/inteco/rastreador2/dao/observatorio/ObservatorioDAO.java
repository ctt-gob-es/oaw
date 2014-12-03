package es.inteco.rastreador2.dao.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.observatorio.*;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
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


public final class ObservatorioDAO {

    private ObservatorioDAO() {
    }

    public static ObservatorioRealizadoForm getFulfilledObservatory(Connection c, long idObservatory, long idExecution) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        try {
            ps = c.prepareStatement("SELECT oreal.*, c.aplicacion, o.nombre FROM observatorios_realizados oreal " +
                    "LEFT JOIN cartucho c ON (c.id_cartucho = oreal.id_cartucho) " +
                    "JOIN observatorio o ON (oreal.id_observatorio = o.id_observatorio) " +
                    "WHERE oreal.id_observatorio = ? AND oreal.id = ?");
            ps.setLong(1, idObservatory);
            ps.setLong(2, idExecution);

            rs = ps.executeQuery();

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

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatorioRealizadoForm;
    }

    public static Long getIdExecutionObservatoryFromExCrawler(Connection c, Long idExecutedCrawler) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM rastreos_realizados r WHERE r.id = ?");

            ps.setLong(1, idExecutedCrawler);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (rs.getLong("id_obs_realizado"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return null;
    }

    public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date) throws Exception {
        return getFulfilledObservatories(c, idObservatory, page, date, true);
    }

    public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date, boolean desc) throws Exception {
        List<ObservatorioRealizadoForm> results = new ArrayList<ObservatorioRealizadoForm>();
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

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static List<ObservatorioRealizadoForm> getAllFulfilledObservatories(Connection c) throws Exception {
        List<ObservatorioRealizadoForm> results = new ArrayList<ObservatorioRealizadoForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        try {
            ps = c.prepareStatement("SELECT o.*, c.aplicacion FROM observatorios_realizados o " +
                    "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho)");

            rs = ps.executeQuery();

            while (rs.next()) {
                ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
                observatorioRealizadoForm.setId(rs.getLong("id"));
                observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
                observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));

                CartuchoForm cartucho = new CartuchoForm();
                cartucho.setId(rs.getLong("id_cartucho"));
                cartucho.setName(rs.getString("aplicacion"));
                observatorioRealizadoForm.setCartucho(cartucho);

                results.add(observatorioRealizadoForm);
            }

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static int countFulfilledObservatories(Connection c, long idObservatory) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM observatorios_realizados WHERE id_observatorio = ?");
            ps.setLong(1, idObservatory);
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Long> realizeCrawlerIds(Connection c, long idObservatoryExecution) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Long> ids = new ArrayList<Long>();

        try {
            ps = c.prepareStatement("SELECT id FROM rastreos_realizados WHERE id_obs_realizado = ?");
            ps.setLong(1, idObservatoryExecution);
            rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getLong("id"));
            }
            return ids;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<ObservatorioForm> getObservatoryList(Connection c) throws Exception {
        List<ObservatorioForm> results = new ArrayList<ObservatorioForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT o.*, p.dias, p.cronExpression " +
                    "FROM observatorio o " +
                    "JOIN periodicidad p ON (o.id_periodicidad = p.id_periodicidad) " +
                    "WHERE o.activo = true;");

            rs = ps.executeQuery();

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

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static List<ObservatorioForm> getObservatoriesFromSeed(Connection c, String idSeed) throws Exception {

        List<ObservatorioForm> observatoryFormList = new ArrayList<ObservatorioForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT o.* FROM observatorio o " +
                    "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " +
                    "WHERE r.semillas = ?");
            ps.setLong(1, Long.parseLong(idSeed));
            rs = ps.executeQuery();

            while (rs.next()) {
                ObservatorioForm observatorioForm = new ObservatorioForm();
                observatorioForm.setId(rs.getLong("id_observatorio"));
                observatorioForm.setNombre(rs.getString("nombre"));
                observatoryFormList.add(observatorioForm);
            }

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatoryFormList;
    }

    public static CategoriaForm getCategoryById(Connection c, Long id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CategoriaForm category = new CategoriaForm();

        try {

            ps = c.prepareStatement("SELECT * FROM categorias_lista WHERE id_categoria = ?");
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                category.setId(String.valueOf(rs.getLong("id_categoria")));
                category.setName(rs.getString("nombre"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return category;
    }

    public static List<ObservatorioForm> getObservatoriesFromCategory(Connection c, String idCategoria) throws Exception {

        List<ObservatorioForm> observatoryFormList = new ArrayList<ObservatorioForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT DISTINCT(o.id_observatorio), o.* FROM observatorio o " +
                    "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " +
                    "JOIN observatorio_categoria oc ON (o.id_observatorio = oc.id_observatorio) " +
                    "WHERE oc.id_categoria = ? OR r.id_rastreo IN (SELECT " +
                    "id_rastreo FROM rastreo WHERE semillas IN (SELECT " +
                    "id_lista FROM lista WHERE id_categoria = ?))");
            ps.setLong(1, Long.parseLong(idCategoria));
            ps.setLong(2, Long.parseLong(idCategoria));
            rs = ps.executeQuery();

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

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatoryFormList;
    }

    public static CargarObservatorioForm userList(Connection c, CargarObservatorioForm cargarObservatorioForm) throws Exception {
        List<ListadoObservatorio> observatoryUserList = new ArrayList<ListadoObservatorio>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT nombre, id_observatorio FROM observatorio o");

            rs = ps.executeQuery();

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

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return cargarObservatorioForm;
    }

    public static List<SemillaForm> getObservatorySeeds(Connection c) throws Exception {

        List<SemillaForm> seedList = new ArrayList<SemillaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT * FROM lista WHERE id_tipo_lista = ?");
            ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                seedList.add(semillaForm);
            }

            return seedList;

        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static CargarObservatorioForm observatoryList(Connection c, int page) throws Exception {
        CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
        List<ListadoObservatorio> observatoryList = new ArrayList<ListadoObservatorio>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();

        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            ps = c.prepareStatement("SELECT DISTINCT(o.nombre), o.id_observatorio, c.id_cartucho, c.aplicacion" +
                    " FROM observatorio o JOIN cartucho c ON (o.id_cartucho = c.id_cartucho) " +
                    " LIMIT ? OFFSET ?");
            ps.setInt(1, pagSize);
            ps.setInt(2, resultFrom);

            rs = ps.executeQuery();

            int numObs = 0;
            while (rs.next()) {
                numObs++;
                ListadoObservatorio ls = new ListadoObservatorio();
                ls.setNombreObservatorio(rs.getString("nombre"));
                ls.setId_observatorio(rs.getLong("id_observatorio"));
                ls.setId_cartucho(rs.getLong("id_cartucho"));
                ls.setCartucho(rs.getString("aplicacion"));
                observatoryList.add(ls);
            }
            cargarObservatorioForm.setListadoObservatorio(observatoryList);
            cargarObservatorioForm.setNumObservatorios(numObs);
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return cargarObservatorioForm;
    }

    public static void deleteListObservatoryAssociation(Connection c, long id_list, long id_observatory) throws Exception {
        PreparedStatement ps = null;

        try {
            ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ? AND id_lista = ?");
            ps.setLong(1, id_observatory);
            ps.setLong(2, id_list);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static int countObservatories(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM observatorio");
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int countSeeds(Connection c, long idObservatorio) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM observatorio_lista " +
                    "WHERE id_observatorio = ?");
            ps.setLong(1, idObservatorio);
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int countSeedsToAdd(Connection c, long idObservatorio) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM lista l WHERE l.id_tipo_lista = ? " +
                    "AND (l.id_lista NOT IN (" +
                    "SELECT ol.id_lista FROM observatorio_lista ol WHERE ol.id_observatorio = ?))");
            ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
            ps.setLong(2, idObservatorio);
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static void deteleObservatory(Connection connR, long id_observatory) throws Exception {
        final List<Connection> connections = DAOUtils.getCartridgeConnections();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connR.setAutoCommit(false);
            for (Connection conn : connections) {
                conn.setAutoCommit(false);
            }

            //RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
            List<Long> idsRastreos = getCrawlerIds(connR, id_observatory);

            //BORRAMOS RASTREOS Y RESULTADOS
            for (Long id_rastreo : idsRastreos) {
                RastreoDAO.borrarRastreo(connR, connections, id_rastreo);
            }

            //ELIMINAMOS LAS RELACIONES OBSERVATORIO_SEMILLA
            ps = connR.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?");
            ps.setLong(1, id_observatory);
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            //ELIMINAMOS EL OBSERVATORIO
            ps = connR.prepareStatement("DELETE FROM observatorio WHERE id_observatorio = ?");
            ps.setLong(1, id_observatory);
            ps.executeUpdate();

            connR.commit();

            for (Connection conn : connections) {
                conn.commit();
            }
        } catch (Exception e) {
            try {
                connR.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            for (Connection conn : connections) {
                try {
                    conn.rollback();
                } catch (Exception excep) {
                    Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    throw e;
                }
            }
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);

            for (Connection conn : connections) {
                DataBaseManager.closeConnection(conn);
            }
        }
    }

    public static void deteleFulfilledObservatory(Connection connR, long idExecution) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        final List<Connection> connections = DAOUtils.getCartridgeConnections();

        try {
            connR.setAutoCommit(false);
            for (Connection conn : connections) {
                conn.setAutoCommit(false);
            }

            //RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
            List<Long> crawlingExecutionsIds = getFulfilledCrawlingIds(connR, idExecution);

            RastreoDAO.deleteAnalyse(connections, crawlingExecutionsIds);

            //ELIMINAMOS EL OBSERVATORIO
            ps = connR.prepareStatement("DELETE FROM observatorios_realizados WHERE id = ?");
            ps.setLong(1, idExecution);
            ps.executeUpdate();

            connR.commit();
            for (Connection conn : connections) {
                conn.commit();
            }
        } catch (Exception e) {
            try {
                connR.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            for (Connection conn : connections) {
                try {
                    conn.rollback();
                } catch (Exception excep) {
                    Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    throw e;
                }
            }
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
            for (Connection conn : connections) {
                DataBaseManager.closeConnection(conn);
            }
        }
    }

    public static void deteleAnalyse(Connection connI, Connection connR, long id_observatorio, List<Long> idsRastreosRealizados) throws Exception {
        PreparedStatement ps = null;

        try {

            //ELIMINAMOS LOS ANÁLISIS E INCIENCIAS DE  LOS RASTREOS DEL OBSERVATORIO
            StringBuilder executionIdStrList = new StringBuilder("DELETE FROM tanalisis WHERE cod_rastreo IN (");
            for (int i = 1; i <= idsRastreosRealizados.size(); i++) {
                if (idsRastreosRealizados.size() > i) {
                    executionIdStrList.append("?,");
                } else if (idsRastreosRealizados.size() == i) {
                    executionIdStrList.append("?)");
                }
            }
            ps = connI.prepareStatement(executionIdStrList.toString());
            for (int i = 0; i < idsRastreosRealizados.size(); i++) {
                ps.setLong(i + 1, idsRastreosRealizados.get(i));
            }
            ps.executeUpdate();

        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    private static List<Long> getCrawlerIds(Connection connR, long id_observatorio) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Long> crawlerIds = new ArrayList<Long>();
        try {
            //RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
            ps = connR.prepareStatement("SELECT id_rastreo FROM rastreo r " +
                    "WHERE id_observatorio = ?");
            ps.setLong(1, id_observatorio);
            rs = ps.executeQuery();
            while (rs.next()) {
                crawlerIds.add(rs.getLong("id_rastreo"));
            }
            return crawlerIds;
        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    private static List<Long> getFulfilledCrawlingIds(Connection connR, long idExecution) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Long> crawlerIds = new ArrayList<Long>();
        try {
            //RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
            ps = connR.prepareStatement("SELECT id FROM rastreos_realizados rr WHERE id_obs_realizado = ?");
            ps.setLong(1, idExecution);
            rs = ps.executeQuery();
            while (rs.next()) {
                crawlerIds.add(rs.getLong("id"));
            }
            return crawlerIds;
        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<FulFilledCrawling> getFulfilledCrawlingByObservatoryExecution(Connection connR, long idExecution) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FulFilledCrawling> crawlings = new ArrayList<FulFilledCrawling>();
        try {
            //RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
            ps = connR.prepareStatement("SELECT * FROM rastreos_realizados rr JOIN lista l ON (l.id_lista = rr.id_lista) " +
                    "JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) WHERE id_obs_realizado = ?");
            ps.setLong(1, idExecution);
            rs = ps.executeQuery();
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
                seed.setCategoria(category);
                fulfilledCrawling.setSeed(seed);
            }
            return crawlings;
        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
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
        } catch (Exception e) {
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

    public static VerObservatorioForm getObservatoryView(Connection c, long idObservatorio, int page) throws Exception {
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
            List<SemillaForm> semillasList = new ArrayList<SemillaForm>();
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

    public static ObservatorioForm getObservatoryForm(Connection c, long idObservatorio) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        ObservatorioForm observatorioForm = new ObservatorioForm();

        try {
            ps = c.prepareStatement("SELECT * FROM observatorio WHERE id_observatorio = ?");
            ps.setLong(1, idObservatorio);
            rs = ps.executeQuery();
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

                List<CategoriaForm> selectedCategories = getObservatoryCategories(c, idObservatorio);
                observatorioForm.setCategoria(new String[selectedCategories.size()]);
                int i = 0;
                for (CategoriaForm categoria : selectedCategories) {
                    observatorioForm.getCategoria()[i] = categoria.getId();
                    i++;
                }
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatorioForm;
    }

    public static ObservatorioForm getObservatoryFormFromExecution(Connection c, long idObservatorioExecution) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        ObservatorioForm observatorioForm = new ObservatorioForm();

        try {
            ps = c.prepareStatement("SELECT * FROM observatorio o JOIN observatorios_realizados ore " +
                    "ON (o.id_observatorio = ore.id_observatorio) WHERE id = ?;");
            ps.setLong(1, idObservatorioExecution);
            rs = ps.executeQuery();
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

        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatorioForm;
    }

    public static ModificarObservatorioForm getObservatoryDataToUpdate(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();

        try {
            ps = c.prepareStatement("SELECT * FROM observatorio o WHERE o.id_observatorio = ?");
            ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            rs = ps.executeQuery();
            if (rs.next()) {
                modificarObservatorioForm.setAmplitud(String.valueOf(rs.getInt("amplitud")));
                modificarObservatorioForm.setFecha(rs.getTimestamp("fecha_inicio"));
                modificarObservatorioForm.setNombre(rs.getString("nombre"));
                modificarObservatorioForm.setPeriodicidad(rs.getString("id_periodicidad"));
                modificarObservatorioForm.setProfundidad(String.valueOf(rs.getInt("profundidad")));
                modificarObservatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
                modificarObservatorioForm.setNormaAnalisis(rs.getString("id_guideline"));
                modificarObservatorioForm.setActivo(rs.getBoolean("activo"));

                CartuchoForm cartuchoForm = new CartuchoForm();
                cartuchoForm.setId(rs.getLong("id_cartucho"));
                modificarObservatorioForm.setCartucho(cartuchoForm);

                ObservatoryTypeForm tipo = new ObservatoryTypeForm();
                tipo.setId(rs.getLong("id_tipo"));
                modificarObservatorioForm.setTipo(tipo);

                Calendar cl1 = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));

                cl1.setTime(modificarObservatorioForm.getFecha());
                modificarObservatorioForm.setFechaInicio(df.format(cl1.getTime()));
                String hour = String.valueOf(cl1.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(cl1.get(Calendar.MINUTE));
                if (Integer.parseInt(hour) < 10) {
                    hour = "0" + hour;
                }
                if (Integer.parseInt(minute) < 10) {
                    minute = "0" + minute;
                }
                modificarObservatorioForm.setHoraInicio(hour);
                modificarObservatorioForm.setMinutoInicio(minute);
                modificarObservatorioForm.setSemillasAnadidas(getSeedsFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));
                modificarObservatorioForm.setSemillasNoAnadidas(getSeedsNotObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));

                List<CategoriaForm> selectedCategories = getObservatoryCategories(c, Long.valueOf(modificarObservatorioForm.getId_observatorio()));
                modificarObservatorioForm.setCategoria(new String[selectedCategories.size()]);
                int i = 0;
                for (CategoriaForm categoria : selectedCategories) {
                    modificarObservatorioForm.getCategoria()[i] = categoria.getId();
                    i++;
                }
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return modificarObservatorioForm;
    }

    public static List<ResultadoSemillaForm> getResultSeedsFromObservatory(Connection c, SemillaForm searchForm, Long idObservatorio, Long idCategoria, int page) throws Exception {

        List<ResultadoSemillaForm> semillasFormList = new ArrayList<ResultadoSemillaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();

        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"));
        int resultFrom = pagSize * page;
        int paramCount = 1;
        try {
            String query = "SELECT l.*, r.*, rr.* FROM lista l " +
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

            ps = c.prepareStatement(query);
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
            rs = ps.executeQuery();

            while (rs.next()) {
                ResultadoSemillaForm resultadoSemillaForm = new ResultadoSemillaForm();
                resultadoSemillaForm.setId(rs.getString("id_lista"));
                resultadoSemillaForm.setName(rs.getString("nombre"));
                resultadoSemillaForm.setActive(rs.getBoolean("activo"));
                resultadoSemillaForm.setIdCrawling(rs.getString("id_rastreo"));
                resultadoSemillaForm.setIdCategory(rs.getLong("l.id_categoria"));
                resultadoSemillaForm.setIdFulfilledCrawling(rs.getString("rr.id"));
                semillasFormList.add(resultadoSemillaForm);
            }

        } catch (Exception e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return semillasFormList;
    }

    public static int countResultSeedsFromObservatory(Connection c, SemillaForm searchForm, long idExecutionObservatory, long idCategory) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        int paramCount = 1;
        try {
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

            ps = c.prepareStatement(query);
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
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static List<SemillaForm> getSeedsFromObservatory(Connection c, long id_observatorio) throws Exception {

        List<SemillaForm> semillasFormList = new ArrayList<SemillaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String query = "SELECT * FROM lista l " +
                    "LEFT JOIN observatorio_lista ol ON (ol.id_lista = l.id_lista) " +
                    "WHERE ol.id_observatorio = ? AND l.id_categoria IS NULL";

            ps = c.prepareStatement(query);
            ps.setLong(1, id_observatorio);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                semillasFormList.add(semillaForm);
            }

        } catch (Exception e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return semillasFormList;
    }

    private static List<SemillaForm> getSeedsNotObservatory(Connection c, long id_observatorio) throws Exception {

        List<SemillaForm> semillasFormList = new ArrayList<SemillaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT * FROM lista l WHERE l.id_tipo_lista = ? AND l.id_categoria IS NULL " +
                    "AND (l.id_lista NOT IN (" +
                    "SELECT ol.id_lista FROM observatorio_lista ol " +
                    "WHERE ol.id_observatorio = ?)) ");

            ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
            ps.setLong(2, id_observatorio);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                semillasFormList.add(semillaForm);
            }

        } catch (Exception e) {
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return semillasFormList;
    }

    private static void updateObservatoryCrawlings(Connection c, ModificarObservatorioForm newObservatory, ObservatorioForm oldObservatory) throws Exception {
        List<String> newCategories = Arrays.asList(newObservatory.getCategoria());
        List<String> oldCategories = Arrays.asList(oldObservatory.getCategoria());

        // Desactivamos las categorías antiguas que no están entre las nuevas
        for (String oldCategory : oldCategories) {
            if (!newCategories.contains(oldCategory)) {
                List<Long> crawlerIds = ObservatorioDAO.getCrawlerFromCategory(c, Long.parseLong(oldCategory));
                for (Long idCrawler : crawlerIds) {
                    RastreoDAO.enableDisableCrawler(c, idCrawler, false);
                }
            }
        }

        // Añadimos las categorías nuevas que no están entre las viejas
        for (String newCategory : newCategories) {
            List<SemillaForm> seedsFromCategory = new ArrayList<SemillaForm>();
            if (!oldCategories.contains(newCategory)) {
                seedsFromCategory.addAll(SemillaDAO.getSeedsByCategory(c, Long.parseLong(newCategory), Constants.NO_PAGINACION, new SemillaForm()));
            }
            insertNewCrawlers(c, oldObservatory.getId(), seedsFromCategory);
        }
    }

    public static ModificarObservatorioForm updateObservatory(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
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

            ps.setLong(6, Long.parseLong(modificarObservatorioForm.getNormaAnalisis()));

            ps.setBoolean(7, modificarObservatorioForm.isPseudoAleatorio());
            ps.setLong(8, modificarObservatorioForm.getLenguaje());
            ps.setLong(9, modificarObservatorioForm.getCartucho().getId());

            ps.setBoolean(10, modificarObservatorioForm.isActivo());
            ps.setLong(11, modificarObservatorioForm.getTipo().getId());

            ps.setLong(12, Long.parseLong(modificarObservatorioForm.getId_observatorio()));

            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            // Rehacemos las asociaciones con las categorías
            ps = c.prepareStatement("DELETE FROM observatorio_categoria WHERE id_observatorio = ?");
            ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

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
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return modificarObservatorioForm;
    }

    public static void putDataToInsert(InsertarRastreoForm insertarRastreoForm, ObservatorioForm observatorioForm) {
        PropertiesManager pmgr = new PropertiesManager();
        insertarRastreoForm.setId_observatorio(observatorioForm.getId());
        insertarRastreoForm.setCartucho(observatorioForm.getCartucho().getId().toString());
        insertarRastreoForm.setNormaAnalisis(String.valueOf(observatorioForm.getId_guideline()));
        insertarRastreoForm.setProfundidad(observatorioForm.getProfundidad());
        insertarRastreoForm.setTopN(observatorioForm.getAmplitud());
        insertarRastreoForm.setPseudoAleatorio(observatorioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(observatorioForm.getLenguaje());
    }

    public static void insertNewCrawlers(Connection c, long id_observatory, List<SemillaForm> seeds) throws Exception {
        InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
        ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, id_observatory);
        putDataToInsert(insertarRastreoForm, observatorioForm);

        if (seeds != null) {
            for (SemillaForm semillaForm : seeds) {
                insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + SemillaDAO.getSeedById(c, semillaForm.getId()).getNombre());
                insertarRastreoForm.setId_semilla(semillaForm.getId());
                insertarRastreoForm.setId_observatorio(id_observatory);
                insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
                Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, id_observatory, semillaForm.getId());
                if (idCrawler == -1) {
                    insertarRastreoForm.setActive(semillaForm.isActiva());
                    RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
                } else {
                    RastreoDAO.enableDisableCrawler(c, idCrawler, semillaForm.isActiva());
                }
            }
        }
    }

    private static void disableCrawlers(Connection c, List<SemillaForm> seeds, String id_observatory) throws Exception {
        for (SemillaForm semillaForm : seeds) {
            //Desactivamos los rastreos de las semilla sque han sido desasignadas al observatorio
            Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, Long.parseLong(id_observatory), semillaForm.getId());
            if (idCrawler != -1) {
                RastreoDAO.enableDisableCrawler(c, idCrawler, false);
            }
        }
    }

    private static void deleteSeedAssociation(Connection c, long id_observatorio) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?");
            ps.setLong(1, id_observatorio);
            ps.executeUpdate();
        } catch (Exception e) {
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
    }

    private static void addObservatorySeeds(Connection c, List<SemillaForm> seeds, long idObservatory) throws Exception {
        PreparedStatement ps = null;
        try {
            for (SemillaForm semillaForm : seeds) {
                ps = c.prepareStatement("INSERT INTO observatorio_lista VALUES(?,?)");
                ps.setLong(1, idObservatory);
                ps.setLong(2, semillaForm.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
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
    }

    public static Map<Long, Date> getObservatoryExecutionIds(Connection c, long observatory_id, long execution_id, long cartridge_id) throws SQLException {

        Map<Long, Date> observatoryExecutionIdsList = new HashMap<Long, Date>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();

        try {
            String limit = pmgr.getValue(CRAWLER_PROPERTIES, "observatory.evolution.limit").trim();
            ps = c.prepareStatement("SELECT id,fecha FROM observatorios_realizados" +
                    " WHERE id_observatorio = ? AND id_cartucho = ? AND fecha <= (SELECT fecha FROM observatorios_realizados WHERE id = ?)" +
                    " ORDER BY fecha DESC LIMIT ?;");
            ps.setLong(1, observatory_id);
            ps.setLong(2, cartridge_id);
            ps.setLong(3, execution_id);
            ps.setInt(4, Integer.parseInt(limit));
            rs = ps.executeQuery();
            while (rs.next()) {
                observatoryExecutionIdsList.put(rs.getLong("id"), rs.getTimestamp("fecha"));
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return observatoryExecutionIdsList;
    }

    public static long existObservatoryCrawl(Connection c, long idObservatory, long idSeed) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                    "JOIN lista l ON (r.semillas = l.id_lista) " +
                    "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " +
                    "WHERE l.id_lista = ? AND o.id_observatorio = ?");
            ps.setLong(1, idSeed);
            ps.setLong(2, idObservatory);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("id_rastreo");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
        return -1;
    }

    public static boolean existObservatory(Connection c, String observatoryName) throws SQLException {
        //Comprobamos que ese nombre de observatorio no existe en la BD
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM observatorio WHERE nombre = ?");
            ps.setString(1, observatoryName);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    private static List<Long> getCrawlerFromCategory(Connection c, long idObservatory) throws SQLException {
        List<Long> idCrawlerList = new ArrayList<Long>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                    "JOIN lista l ON (r.semillas = l.id_lista) " +
                    "WHERE l.id_categoria = ?");
            ps.setLong(1, idObservatory);
            rs = ps.executeQuery();

            while (rs.next()) {
                idCrawlerList.add(rs.getLong("id_rastreo"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return idCrawlerList;
    }

    private static List<Long> getCrawlerFromObservatory(Connection c, long idObservatory) throws SQLException {
        List<Long> idCrawlerList = new ArrayList<Long>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " +
                    "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " +
                    "WHERE o.id_observatorio = ?");
            ps.setLong(1, idObservatory);
            rs = ps.executeQuery();

            while (rs.next()) {
                idCrawlerList.add(rs.getLong("id_rastreo"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return idCrawlerList;
    }

    private static void editCrawlings(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {
        InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
        insertarRastreoForm.setProfundidad(Integer.parseInt(modificarObservatorioForm.getProfundidad()));
        insertarRastreoForm.setTopN(Long.parseLong(modificarObservatorioForm.getAmplitud()));
        insertarRastreoForm.setCartucho(modificarObservatorioForm.getCartucho().getId().toString());
        insertarRastreoForm.setNormaAnalisis(modificarObservatorioForm.getNormaAnalisis());
        insertarRastreoForm.setPseudoAleatorio(modificarObservatorioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(modificarObservatorioForm.getLenguaje());
        List<Long> idCrawlerList = getCrawlerFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
        for (Long idCrawler : idCrawlerList) {
            SemillaForm semillaForm = SemillaDAO.getSeedById(c, RastreoDAO.getSeedByCrawler(c, idCrawler));
            insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
            insertarRastreoForm.setCodigo(modificarObservatorioForm.getNombre() + "-" + semillaForm.getNombre());
            insertarRastreoForm.setId_semilla(semillaForm.getId());
            RastreoDAO.modificarRastreo(c, false, insertarRastreoForm, idCrawler);
        }
    }

    public static long getCartridgeFromExecutedObservatoryId(Connection c, Long observatory_id) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT id_cartucho FROM rastreos_realizados WHERE id = ?");
            ps.setLong(1, observatory_id);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("id_cartucho");
            }

        } catch (Exception e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static List<Long> getSubsequentObservatoryExecutionIds(Connection c, Long observatory_id,
                                                                  Long executionId, Long cartridgeId) throws Exception {

        List<Long> subsequentExecutionIds = new ArrayList<Long>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = c.prepareStatement("SELECT id FROM observatorios_realizados WHERE id_observatorio = ? AND " +
                    "id_cartucho = ? AND fecha > (SELECT fecha FROM observatorios_realizados WHERE id = ? )");
            ps.setLong(1, observatory_id);
            ps.setLong(2, cartridgeId);
            ps.setLong(3, executionId);

            rs = ps.executeQuery();
            while (rs.next()) {
                subsequentExecutionIds.add(rs.getLong("id"));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return subsequentExecutionIds;

    }

    public static void updateObservatoryStatus(Connection c, Long executedId, int estado) throws SQLException {
        PreparedStatement ps = null;
        try {

            ps = c.prepareStatement("UPDATE observatorios_realizados SET estado = ? WHERE id = ?");
            ps.setInt(1, estado);
            ps.setLong(2, executedId);
            ps.executeUpdate();

        } catch (Exception e) {
            Logger.putLog("Error al modificar el estado de la ejecución con id: " + executedId + " del observatorio.", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static boolean getObservatoryStatus(Connection c, Long executedId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = c.prepareStatement("SELECT lanzado FROM rastreos_realizados WHERE id = ?");
            ps.setLong(1, executedId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("estado");
            }

        } catch (Exception e) {
            Logger.putLog("Error al recuperar el estado de la ejecución con id: " + executedId + " del observatorio.", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return false;
    }

    public static List<CategoriaForm> getObservatoryCategories(Connection c, Long idObservatory) throws SQLException {
        List<CategoriaForm> results = new ArrayList<CategoriaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = c.prepareStatement("SELECT * FROM observatorio_categoria oc " +
                    "JOIN categorias_lista cl ON (cl.id_categoria = oc.id_categoria) " +
                    "WHERE id_observatorio = ?");
            ps.setLong(1, idObservatory);
            rs = ps.executeQuery();

            while (rs.next()) {
                // return rs.getBoolean("estado");
                CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("oc.id_categoria"));
                categoriaForm.setName(rs.getString("cl.nombre"));
                results.add(categoriaForm);
            }

        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static List<CategoriaForm> getExecutionObservatoryCategories(Connection c, Long idExecutionObservatory) throws SQLException {
        final List<CategoriaForm> results = new ArrayList<CategoriaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = c.prepareStatement("SELECT cl.nombre, cl.id_categoria FROM rastreos_realizados rr " +
                    "JOIN lista l ON (l.id_lista = rr.id_lista) " +
                    "JOIN observatorio_categoria oc ON (l.id_categoria = oc.id_categoria) " +
                    "JOIN categorias_lista cl ON (oc.id_categoria = cl.id_categoria) " +
                    "WHERE rr.id_obs_realizado = ? GROUP BY cl.id_categoria;");
            ps.setLong(1, idExecutionObservatory);
            rs = ps.executeQuery();

            while (rs.next()) {
                // return rs.getBoolean("estado");
                final CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("cl.id_categoria"));
                categoriaForm.setName(rs.getString("cl.nombre"));
                results.add(categoriaForm);
            }

        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    public static void saveMethodology(Connection c, Long idExecution, String methodology) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("INSERT INTO observatorio_metodologia (id_obs_realizado, metodologia) VALUES (?,?)");
            ps.setLong(1, idExecution);
            ps.setString(2, methodology);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static String getMethodology(Connection c, Long idExecution) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM observatorio_metodologia WHERE id_obs_realizado = ?");
            ps.setLong(1, idExecution);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("metodologia");
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return null;
    }

    public static List<Long> getSeedExecutionsFromObservatory(Connection c, String idSeed, Long idObservatory) throws SQLException {
        List<Long> executedCrawlerList = new ArrayList<Long>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM rastreos_realizados r " +
                    "JOIN observatorios_realizados o ON (r.id_obs_realizado = o.id) " +
                    "WHERE id_lista = ? AND id_observatorio = ?");
            ps.setString(1, idSeed);
            ps.setLong(2, idObservatory);
            rs = ps.executeQuery();
            while (rs.next()) {
                executedCrawlerList.add(rs.getLong("id"));
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return executedCrawlerList;
    }

    public static List<ObservatoryTypeForm> getAllObservatoryTypes(Connection c) throws Exception {
        List<ObservatoryTypeForm> typeList = new ArrayList<ObservatoryTypeForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM observatorio_tipo");
            rs = ps.executeQuery();

            while (rs.next()) {
                ObservatoryTypeForm type = new ObservatoryTypeForm();
                type.setId(rs.getLong("id_tipo"));
                type.setName(rs.getString("name"));
                typeList.add(type);
            }
            return typeList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar la lista de roles", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

}
