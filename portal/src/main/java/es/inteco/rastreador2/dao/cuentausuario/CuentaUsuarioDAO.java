package es.inteco.rastreador2.dao.cuentausuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.*;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.UpdateListDataForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.ListadoCuentasUsuario;
import es.inteco.rastreador2.utils.Rastreo;
import org.apache.struts.util.LabelValueBean;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class CuentaUsuarioDAO {

    private CuentaUsuarioDAO() {
    }

    private static String getQuery(int type, long idAccount) {
        String statement = "";

        if (type == Constants.CLIENT_ACCOUNT_TYPE) {
            statement = "SELECT l.*, cc.activo, cc.id_cuenta as id, cc.nombre, cc.fecha_inicio, r.*, p.*, c.id_cartucho, c.nombre as nombre_cartucho, c.numrastreos, r.lista_rastreable as lista_rastreable_r, r.lista_no_rastreable as lista_no_rastreable_r, ll.acronimo " +
                    "FROM cuenta_cliente cc " +
                    "JOIN rastreo r ON (cc.id_cuenta = r.id_cuenta) " +
                    "JOIN languages l ON (r.id_language = l.id_language) " +
                    "JOIN cartucho_rastreo rc ON (r.id_rastreo = rc.id_rastreo) " +
                    "JOIN cartucho c ON (c.id_cartucho = rc.id_cartucho) " +
                    "JOIN lista ll ON (r.semillas = ll.id_lista) " +
                    "JOIN periodicidad p ON (p.id_periodicidad = cc.id_periodicidad) WHERE r.activo = 1";

            if (idAccount != Constants.ALL_DATA) {
                statement += " AND cc.id_cuenta = " + idAccount;
            }
        } else if (type == Constants.OBSERVATORY_TYPE) {
            statement = "SELECT l.*, o.activo, o.id_observatorio as id, o.nombre, o.fecha_inicio, r.*, p.*, c.id_cartucho, c.nombre as nombre_cartucho, c.numrastreos, r.lista_rastreable as lista_rastreable_r, r.lista_no_rastreable as lista_no_rastreable_r, ll.acronimo " +
                    "FROM observatorio o " +
                    "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " +
                    "JOIN languages l ON (r.id_language = l.id_language) " +
                    "JOIN cartucho_rastreo rc ON (r.id_rastreo = rc.id_rastreo) " +
                    "JOIN cartucho c ON (c.id_cartucho = rc.id_cartucho) " +
                    "JOIN lista ll ON (r.semillas = ll.id_lista) " +
                    "JOIN periodicidad p ON (p.id_periodicidad = o.id_periodicidad) WHERE r.activo = 1";

            if (idAccount != Constants.ALL_DATA) {
                statement += " AND o.id_observatorio = " + idAccount;
            }
        }

        return statement;
    }

    public static List<CuentaCliente> getClientAccounts(Connection c, long idAccount, int type) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List<CuentaCliente> clientAccounts = new ArrayList<>();

            String statement = getQuery(type, idAccount);

            ps = c.prepareStatement(statement);

            rs = ps.executeQuery();
            while (rs.next()) {
                CuentaCliente cuentaCliente = new CuentaCliente();
                cuentaCliente.setIdCuenta(rs.getLong("id"));
                cuentaCliente.setNombre(rs.getString("nombre"));
                cuentaCliente.setFecha(rs.getTimestamp("fecha_inicio"));
                cuentaCliente.setActive(rs.getBoolean("activo"));

                cuentaCliente.setDatosRastreo(new DatosCartuchoRastreoForm());
                cuentaCliente.getDatosRastreo().setId_rastreo(rs.getInt("id_rastreo"));
                cuentaCliente.getDatosRastreo().setId_cartucho(rs.getInt("id_cartucho"));
                cuentaCliente.getDatosRastreo().setNombre_cart(rs.getString("nombre_cartucho"));
                cuentaCliente.getDatosRastreo().setNumRastreos(rs.getInt("numrastreos"));
                cuentaCliente.getDatosRastreo().setProfundidad(rs.getInt("profundidad"));
                cuentaCliente.getDatosRastreo().setTopN(rs.getInt("topn"));
                cuentaCliente.getDatosRastreo().setNombre_rastreo(rs.getString("nombre_rastreo"));
                cuentaCliente.getDatosRastreo().setIdSemilla(rs.getLong("semillas"));
                cuentaCliente.getDatosRastreo().setSeedAcronym(rs.getString("acronimo"));
                cuentaCliente.getDatosRastreo().setListaRastreable(rs.getString("lista_rastreable_r"));
                cuentaCliente.getDatosRastreo().setListaNoRastreable(rs.getString("lista_no_rastreable_r"));
                cuentaCliente.getDatosRastreo().setIdObservatory(rs.getLong("id_observatorio"));
                cuentaCliente.getDatosRastreo().setIdCuentaCliente(rs.getLong("id_cuenta"));
                cuentaCliente.getDatosRastreo().setPseudoaleatorio(rs.getBoolean("pseudoaleatorio"));
                cuentaCliente.getDatosRastreo().setExhaustive(rs.getBoolean("exhaustive"));
                cuentaCliente.getDatosRastreo().setInDirectory(rs.getBoolean("in_directory"));
                cuentaCliente.setPeriodicidad(new PeriodicidadForm());
                cuentaCliente.getPeriodicidad().setCronExpression(rs.getString("cronExpression"));
                cuentaCliente.getPeriodicidad().setDias(rs.getInt("dias"));
                LenguajeForm lenguajeForm = new LenguajeForm();
                lenguajeForm.setId(rs.getLong("id_language"));
                lenguajeForm.setCodice(rs.getString("codice"));
                lenguajeForm.setKeyName(rs.getString("key_name"));
                cuentaCliente.getDatosRastreo().setLanguage(lenguajeForm);

                clientAccounts.add(cuentaCliente);
            }
            return clientAccounts;
        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int countAccounts(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM cuenta_cliente");
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static Long insertClientAccount(Connection c, NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();

        try {
            c.setAutoCommit(false);

            ps = c.prepareStatement("INSERT INTO cuenta_cliente (nombre, dominio, id_periodicidad, profundidad, amplitud, fecha_inicio, lista_rastreable, lista_no_rastreable, id_guideline, pseudoaleatorio, id_language, activo, in_directory) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, nuevaCuentaUsuarioForm.getNombre());
            ps.setLong(2, nuevaCuentaUsuarioForm.getIdSeed());
            ps.setLong(3, Long.parseLong(nuevaCuentaUsuarioForm.getPeriodicidad()));
            ps.setInt(4, Integer.parseInt(nuevaCuentaUsuarioForm.getProfundidad()));
            ps.setInt(5, Integer.parseInt(nuevaCuentaUsuarioForm.getAmplitud()));
            ps.setTimestamp(6, new Timestamp(nuevaCuentaUsuarioForm.getFecha().getTime()));
            if (nuevaCuentaUsuarioForm.getIdCrawlableList() != 0) {
                ps.setLong(7, nuevaCuentaUsuarioForm.getIdCrawlableList());
            } else {
                ps.setString(7, null);
            }

            if (nuevaCuentaUsuarioForm.getIdNoCrawlableList() != 0) {
                ps.setLong(8, nuevaCuentaUsuarioForm.getIdNoCrawlableList());
            } else {
                ps.setString(8, null);
            }

            if (nuevaCuentaUsuarioForm.getNormaAnalisisEnlaces().equals("1")) {
                if (nuevaCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
                    ps.setString(9, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
                } else if (nuevaCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
                    ps.setString(9, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
                } else {
                    ps.setString(9, nuevaCuentaUsuarioForm.getNormaAnalisis());
                }

            } else {
                ps.setString(9, nuevaCuentaUsuarioForm.getNormaAnalisis());
            }

            ps.setBoolean(10, nuevaCuentaUsuarioForm.isPseudoAleatorio());
            ps.setLong(11, nuevaCuentaUsuarioForm.getLenguaje());
            ps.setBoolean(12, nuevaCuentaUsuarioForm.isActivo());
            ps.setBoolean(13, nuevaCuentaUsuarioForm.isInDirectory());
            ps.executeUpdate();

            long idAccount = 0;
            ps = c.prepareStatement("SELECT id_cuenta FROM cuenta_cliente WHERE nombre = ?");
            ps.setString(1, nuevaCuentaUsuarioForm.getNombre());
            rs = ps.executeQuery();
            if (rs.next()) {
                idAccount = rs.getLong("id_cuenta");
            }

            for (int i = 0; i < nuevaCuentaUsuarioForm.getCartuchosSelected().length; i++) {
                ps = c.prepareStatement("INSERT INTO cuenta_cliente_cartucho VALUES (?,?)");
                ps.setLong(1, idAccount);
                ps.setLong(2, Integer.parseInt(nuevaCuentaUsuarioForm.getCartuchosSelected()[i]));
                ps.executeUpdate();
            }
            c.commit();
            return idAccount;
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static CargarCuentasUsuarioForm userList(Connection c, CargarCuentasUsuarioForm cargarCuentasUsuarioForm, int page) throws Exception {
        List<ListadoCuentasUsuario> userAccountList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            if (page == Constants.NO_PAGINACION) {
                ps = c.prepareStatement("SELECT DISTINCT(cc.nombre), l.lista, cc.id_cuenta, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos " +
                        "FROM cuenta_cliente cc " +
                        "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                        "JOIN lista l ON (cc.dominio = l.id_lista)" +
                        "JOIN cartucho c ON (ccc.id_cartucho = c.id_cartucho) GROUP BY cc.nombre ORDER BY cc.nombre");
            } else {
                ps = c.prepareStatement("SELECT DISTINCT(cc.nombre), l.lista, cc.id_cuenta, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos " +
                        "FROM cuenta_cliente cc " +
                        "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                        "JOIN lista l ON (cc.dominio = l.id_lista)" +
                        "JOIN cartucho c ON (ccc.id_cartucho = c.id_cartucho) GROUP BY cc.nombre ORDER BY cc.nombre LIMIT ? OFFSET ?");
                ps.setInt(1, pagSize);
                ps.setInt(2, resultFrom);
            }
            rs = ps.executeQuery();

            int numCuentas = 0;
            while (rs.next()) {
                numCuentas++;
                ListadoCuentasUsuario ls = new ListadoCuentasUsuario();
                ls.setNombreCuenta(rs.getString("nombre"));
                ls.setDominio(listFromString(rs.getString("lista"), ";"));
                ls.setId_cuenta(rs.getLong("id_cuenta"));
                if (rs.getString("cartuchos") != null) {
                    ls.setCartucho(listFromString(rs.getString("cartuchos"), ","));
                }
                userAccountList.add(ls);
            }
            cargarCuentasUsuarioForm.setListadoCuentasUsuario(userAccountList);
            cargarCuentasUsuarioForm.setNumCuentasUsuario(numCuentas);
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return cargarCuentasUsuarioForm;
    }

    private static List<String> listFromString(String listString, String separator) {
        List<String> roleList = new ArrayList<>();
        if (listString == null) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(listString, separator);
        while (tokenizer.hasMoreTokens()) {
            roleList.add(tokenizer.nextToken());
        }
        return roleList;
    }

    public static boolean existAccount(Connection c, String accountName) throws SQLException {
        //Comprobamos que ese nombre de usuario no existe en la BD
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM cuenta_cliente WHERE nombre = ?");
            ps.setString(1, accountName);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static NuevaCuentaUsuarioForm getUserAccount(Connection c, Long id_account) throws Exception {
        NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm = new NuevaCuentaUsuarioForm();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT *, p.nombre AS nombrePeriodicidad, cc.nombre as nombreCuenta FROM cuenta_cliente cc " +
                    "JOIN periodicidad p ON (cc.id_periodicidad = p.id_periodicidad) WHERE id_cuenta = ?");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();
            while (rs.next()) {
                nuevaCuentaUsuarioForm.setNombre(rs.getString("nombreCuenta"));
                nuevaCuentaUsuarioForm.setAmplitud(rs.getString("amplitud"));
                nuevaCuentaUsuarioForm.setProfundidad(rs.getString("profundidad"));
                nuevaCuentaUsuarioForm.setPeriodicidad(rs.getString("nombrePeriodicidad"));
                nuevaCuentaUsuarioForm.setId_cuenta(rs.getString("id_cuenta"));
                nuevaCuentaUsuarioForm.setIdSeed(rs.getLong("dominio"));
                nuevaCuentaUsuarioForm.setIdNoCrawlableList(rs.getLong("lista_no_rastreable"));
                nuevaCuentaUsuarioForm.setIdCrawlableList(rs.getLong("lista_rastreable"));
                nuevaCuentaUsuarioForm.setInDirectory(rs.getBoolean("in_directory"));
            }
            return nuevaCuentaUsuarioForm;
        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static String usersByAccountType(Connection c, Long id_account) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT DISTINCT(cc.id_cuenta), GROUP_CONCAT(DISTINCT(u.usuario)) as usuarios " +
                    "FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_usuario ccu ON (ccu.id_cuenta = cc.id_cuenta) " +
                    "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) " +
                    "WHERE cc.id_cuenta = ? GROUP BY u.usuario;");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("usuarios");
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return null;
    }

    public static VerCuentaUsuarioForm getDatosUsuarioVer(Connection c, VerCuentaUsuarioForm verCuentaUsuarioForm, Long id_account) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT DISTINCT(cc.nombre) AS nombreCuenta, cc.id_guideline, cc.activo, cc.in_directory, " +
                    "l.lista AS semilla, l1.lista AS listaRastreable, l2.lista AS listaNoRastreable, p.nombre " +
                    "AS periodicidad, profundidad, amplitud, pseudoaleatorio, GROUP_CONCAT(DISTINCT(c.aplicacion)) " +
                    "as cartuchos FROM cuenta_cliente cc " +
                    "JOIN periodicidad p ON (p.id_periodicidad = cc.id_periodicidad) " +
                    "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                    "JOIN cartucho c ON (c.id_cartucho = ccc.id_cartucho) " +
                    "JOIN lista l ON (cc.dominio = l.id_lista) " +
                    "LEFT JOIN lista l1 ON (cc.lista_rastreable = l1.id_lista) " +
                    "LEFT JOIN lista l2 ON (cc.lista_no_rastreable = l2.id_lista) " +
                    "WHERE cc.id_cuenta = ? GROUP BY cc.nombre;");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();
            if (rs.next()) {
                verCuentaUsuarioForm.setAmplitud(rs.getString("amplitud"));
                verCuentaUsuarioForm.setProfundidad(rs.getString("profundidad"));
                verCuentaUsuarioForm.setPseudoAleatorio(String.valueOf(rs.getBoolean("pseudoaleatorio")));
                verCuentaUsuarioForm.setNombre(rs.getString("nombreCuenta"));
                verCuentaUsuarioForm.setDominio(listFromString(rs.getString("semilla"), ";"));
                verCuentaUsuarioForm.setPeriodicidad(rs.getString("periodicidad"));
                verCuentaUsuarioForm.setNormaAnalisis(rs.getLong("id_guideline"));
                verCuentaUsuarioForm.setListaRastreable(listFromString(rs.getString("listaRastreable"), ";"));
                verCuentaUsuarioForm.setListaNoRastreable(listFromString(rs.getString("listaNoRastreable"), ";"));
                verCuentaUsuarioForm.setCartuchos(listFromString(rs.getString("cartuchos"), ","));
                verCuentaUsuarioForm.setActivo(rs.getBoolean("activo"));
                verCuentaUsuarioForm.setInDirectory(rs.getBoolean("in_directory"));

                String uCliente = usersByAccountType(c, id_account);
                String rCliente = usersByAccountType(c, id_account);

                if (uCliente != null) {
                    verCuentaUsuarioForm.setUsuarios(listFromString(uCliente, ","));
                }
                if (rCliente != null) {
                    verCuentaUsuarioForm.setResponsable(listFromString(rCliente, ","));
                }
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return verCuentaUsuarioForm;
    }

    public static EliminarCuentaUsuarioForm getDeleteUserAccounts(Connection c, Long id_account, EliminarCuentaUsuarioForm eliminarCuentaUsuarioForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT DISTINCT(cc.nombre) AS nombreCuenta, cc.*,l.lista, GROUP_CONCAT(DISTINCT(c.aplicacion)) as cartuchos " +
                    "FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                    "JOIN cartucho c ON (c.id_cartucho = ccc.id_cartucho) " +
                    "JOIN lista l ON (cc.dominio = l.id_lista) " +
                    "WHERE cc.id_cuenta = ? GROUP BY cc.nombre");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("cartuchos") != null) {
                    eliminarCuentaUsuarioForm.setCartuchos(listFromString(rs.getString("cartuchos"), ","));
                }
                eliminarCuentaUsuarioForm.setDominio(rs.getString("lista"));
                eliminarCuentaUsuarioForm.setNombre(rs.getString("nombreCuenta"));
                eliminarCuentaUsuarioForm.setId(rs.getLong("id_cuenta"));
                eliminarCuentaUsuarioForm.setNormaAnalisis(String.valueOf(rs.getLong("id_guideline")));
                eliminarCuentaUsuarioForm.setIdSemilla(rs.getLong("dominio"));
                eliminarCuentaUsuarioForm.setIdListaRastreable(rs.getLong("lista_rastreable"));
                eliminarCuentaUsuarioForm.setIdListaNoRastreable(rs.getLong("lista_no_rastreable"));

                String uCliente = usersByAccountType(c, id_account);
                String rCliente = usersByAccountType(c, id_account);

                if (uCliente != null) {
                    eliminarCuentaUsuarioForm.setUsuarios(listFromString(uCliente, ","));
                }
                if (rCliente != null) {
                    eliminarCuentaUsuarioForm.setResponsable(listFromString(rCliente, ","));
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return eliminarCuentaUsuarioForm;
    }

    public static void deleteUserAccount(Connection c, Long id_account, EliminarCuentaUsuarioForm eliminarCuentaUsuarioForm) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        final List<Connection> connections = DAOUtils.getCartridgeConnections();
        try {
            c.setAutoCommit(false);
            for (Connection conn : connections) {
                conn.setAutoCommit(false);
            }

            // Borramos primero los resultados de los cartuchos
            List<Long> executedCrawlingIdsList = RastreoDAO.getExecutedCrawlerClientAccountsIds(c, eliminarCuentaUsuarioForm.getId());
            if (executedCrawlingIdsList != null && !executedCrawlingIdsList.isEmpty()) {
                RastreoDAO.deleteAnalyse(connections, executedCrawlingIdsList);
            }

            ps = c.prepareStatement("DELETE FROM usuario WHERE id_usuario IN (" +
                    "SELECT id_usuario FROM cuenta_cliente_usuario WHERE id_cuenta = ?)");
            ps.setLong(1, id_account);
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            ps = c.prepareStatement("DELETE FROM rastreo WHERE id_cuenta = ? ");
            ps.setLong(1, id_account);
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            ps = c.prepareStatement("DELETE FROM cuenta_cliente WHERE id_cuenta = ?");
            ps.setLong(1, id_account);
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            ps = c.prepareStatement("DELETE FROM LISTA WHERE id_lista IN (?,?,?)");
            ps.setLong(1, eliminarCuentaUsuarioForm.getIdSemilla());
            ps.setLong(2, eliminarCuentaUsuarioForm.getIdListaRastreable());
            ps.setLong(3, eliminarCuentaUsuarioForm.getIdListaNoRastreable());
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            c.commit();
            for (Connection conn : connections) {
                conn.commit();
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
            for (Connection conn : connections) {
                DataBaseManager.closeConnection(conn);
            }
        }
    }

    public static List<FulFilledCrawling> getFulfilledCrawlings(Connection conn, Long idCrawling, int page) throws Exception {
        List<FulFilledCrawling> crawlings = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            ps = conn.prepareStatement("SELECT rr.* FROM rastreos_realizados rr " +
                    "WHERE id_rastreo = ? ORDER BY fecha DESC LIMIT ? OFFSET ?");
            ps.setLong(1, idCrawling);
            ps.setInt(2, pagSize);
            ps.setInt(3, resultFrom);
            rs = ps.executeQuery();
            while (rs.next()) {
                FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
                fulfilledCrawling.setId(rs.getLong("id"));
                fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
                fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
                fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
                crawlings.add(fulfilledCrawling);
            }

            return crawlings;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int getNumClientFulfilledCrawlings(Connection c, Long idClient) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM rastreos_realizados rr WHERE id_rastreo = ?");
            ps.setLong(1, idClient);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int getNumClientCrawlings(Connection c, Long idClient) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM rastreo r " +
                    "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                    "WHERE ccu.id_usuario = ?");
            ps.setLong(1, idClient);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (Exception e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Rastreo> getClientCrawlings(Connection c, Long idClient, int page) throws Exception {
        List<Rastreo> crawlings = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;

        try {
            if (page == Constants.NO_PAGINACION) {
                ps = c.prepareStatement("SELECT r.*, c.aplicacion FROM rastreo r " +
                        "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                        "JOIN cartucho_rastreo cr ON (cr.id_rastreo = r.id_rastreo)  " +
                        "JOIN cartucho c ON (cr.id_cartucho = c.id_cartucho) " +
                        "WHERE ccu.id_usuario = ?");
                ps.setLong(1, idClient);
            } else {
                ps = c.prepareStatement("SELECT r.*, c.aplicacion FROM rastreo r " +
                        "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                        "JOIN cartucho_rastreo cr ON (cr.id_rastreo = r.id_rastreo)  " +
                        "JOIN cartucho c ON (cr.id_cartucho = c.id_cartucho) " +
                        "WHERE ccu.id_usuario = ? LIMIT ? OFFSET ?");
                ps.setLong(1, idClient);
                ps.setInt(2, pagSize);
                ps.setInt(3, resultFrom);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                Rastreo rastreo = new Rastreo();
                rastreo.setId_rastreo(rs.getString("id_rastreo"));
                rastreo.setCartucho(rs.getString("aplicacion"));
                rastreo.setEstado(rs.getInt("estado"));
                rastreo.setEstadoTexto("rastreo.estado." + rastreo.getEstado());
                try {
                    Date date = rs.getDate("Fecha_lanzado");
                    if (date != null) {
                        rastreo.setFecha(date.toString());
                    }
                } catch (SQLException e) {
                    Logger.putLog("MySQL falla al recuperar valores que él mismo introduce por defecto :O", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR);
                }
                rastreo.setProfundidad(rs.getInt("profundidad"));
                String crawlingName = rs.getString("nombre_rastreo");
                if (crawlingName.contains("-")) {
                    crawlingName = crawlingName.substring(0, crawlingName.indexOf('-'));
                }
                rastreo.setCodigo(crawlingName);

                crawlings.add(rastreo);
            }

        } catch (Exception e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return crawlings;
    }

    private static ModificarCuentaUsuarioForm updateLists(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm) throws Exception {

        UpdateListDataForm updateListDataForm = new UpdateListDataForm();

        updateListDataForm.setListaRastreable(modificarCuentaUsuarioForm.getListaRastreable());
        updateListDataForm.setIdListaRastreable(modificarCuentaUsuarioForm.getIdListaRastreable());
        updateListDataForm.setListaNoRastreable(modificarCuentaUsuarioForm.getListaNoRastreable());
        updateListDataForm.setIdListaNoRastreable(modificarCuentaUsuarioForm.getIdListaNoRastreable());
        updateListDataForm.setNombre(modificarCuentaUsuarioForm.getNombre());

        SemillaDAO.updateLists(c, updateListDataForm);

        modificarCuentaUsuarioForm.setIdListaRastreable(updateListDataForm.getIdListaRastreable());
        modificarCuentaUsuarioForm.setIdListaNoRastreable(updateListDataForm.getIdListaNoRastreable());
        modificarCuentaUsuarioForm.setIdRastreableAntiguo(updateListDataForm.getIdRastreableAntiguo());
        modificarCuentaUsuarioForm.setIdNoRastreableAntiguo(updateListDataForm.getIdNoRastreableAntiguo());

        return modificarCuentaUsuarioForm;
    }

    public static void removeLists(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm) throws Exception {

        UpdateListDataForm updateListDataForm = new UpdateListDataForm();

        updateListDataForm.setListaRastreable(modificarCuentaUsuarioForm.getListaRastreable());
        updateListDataForm.setIdRastreableAntiguo(modificarCuentaUsuarioForm.getIdRastreableAntiguo());
        updateListDataForm.setListaNoRastreable(modificarCuentaUsuarioForm.getListaNoRastreable());
        updateListDataForm.setIdNoRastreableAntiguo(modificarCuentaUsuarioForm.getIdNoRastreableAntiguo());

        SemillaDAO.removeLists(c, updateListDataForm);
    }

    public static ModificarCuentaUsuarioForm updateUserAccount(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, boolean fromMenu) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            boolean hasIntav = false;
            for (int i = 0; i < modificarCuentaUsuarioForm.getCartuchosSelected().length; i++) {
                if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(modificarCuentaUsuarioForm.getCartuchosSelected()[i]))) {
                    hasIntav = true;
                }
            }

            modificarCuentaUsuarioForm = updateLists(c, modificarCuentaUsuarioForm);

            if (!fromMenu) {

                if (hasIntav) {
                    ps = c.prepareStatement("UPDATE cuenta_cliente SET Nombre = ?, Id_Periodicidad = ?, Profundidad = ?, Amplitud = ?, Fecha_Inicio = ?, lista_rastreable = ?, lista_no_rastreable = ?, id_guideline = ?, pseudoaleatorio = ?, id_language = ?, activo = ?, in_directory = ? WHERE id_cuenta = ?");
                    if (modificarCuentaUsuarioForm.getNormaAnalisisEnlaces().equals("1")) {
                        if (modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
                            ps.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
                        } else if (modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
                            ps.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
                        } else {
                            ps.setString(8, modificarCuentaUsuarioForm.getNormaAnalisis());
                        }

                    } else {
                        ps.setString(8, modificarCuentaUsuarioForm.getNormaAnalisis());
                    }
                } else {
                    ps = c.prepareStatement("UPDATE cuenta_cliente SET Nombre = ?, Id_Periodicidad = ?, Profundidad = ?, Amplitud = ?, Fecha_Inicio = ?, lista_rastreable = ?, lista_no_rastreable = ?, id_guideline = ?, pseudoaleatorio = ?, id_language = ?, activo = ?, in_directory = ? WHERE id_cuenta = ?");
                    ps.setString(8, null);
                }
                ps.setString(1, modificarCuentaUsuarioForm.getNombre());
                ps.setInt(2, Integer.parseInt(modificarCuentaUsuarioForm.getPeriodicidad()));
                ps.setInt(3, Integer.parseInt(modificarCuentaUsuarioForm.getProfundidad()));
                ps.setInt(4, Integer.parseInt(modificarCuentaUsuarioForm.getAmplitud()));
                modificarCuentaUsuarioForm.setFecha(CrawlerUtils.getFechaInicio(modificarCuentaUsuarioForm.getFechaInicio(),
                        modificarCuentaUsuarioForm.getHoraInicio(), modificarCuentaUsuarioForm.getMinutoInicio()));
                ps.setTimestamp(5, new Timestamp(modificarCuentaUsuarioForm.getFecha().getTime()));

                if (modificarCuentaUsuarioForm.getIdListaRastreable() != 0) {
                    ps.setLong(6, modificarCuentaUsuarioForm.getIdListaRastreable());
                } else {
                    ps.setString(6, null);
                }

                if (modificarCuentaUsuarioForm.getIdListaNoRastreable() != 0) {
                    ps.setLong(7, modificarCuentaUsuarioForm.getIdListaNoRastreable());
                } else {
                    ps.setString(7, null);
                }

                ps.setBoolean(9, modificarCuentaUsuarioForm.isPseudoAleatorio());
                ps.setLong(10, modificarCuentaUsuarioForm.getLenguaje());
                ps.setBoolean(11, modificarCuentaUsuarioForm.isActivo());
                ps.setBoolean(12, modificarCuentaUsuarioForm.isInDirectory());
                ps.setLong(13, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));

                ps.executeUpdate();
                DAOUtils.closeQueries(ps, null);
            }


            if (!fromMenu) {
                // Borramos los roles antiguos
                ps = c.prepareStatement("DELETE FROM cuenta_cliente_cartucho WHERE id_cuenta = ?");
                ps.setLong(1, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));
                ps.executeUpdate();

                // Insertamos los cartuchos nuevos
                if (modificarCuentaUsuarioForm.getCartuchosSelected() != null
                        && modificarCuentaUsuarioForm.getCartuchosSelected().length > 0 && !modificarCuentaUsuarioForm.getId_cuenta().equals("0")) {
                    ps = c.prepareStatement("INSERT INTO cuenta_cliente_cartucho VALUES (?,?)");
                    for (int i = 0; i < modificarCuentaUsuarioForm.getCartuchosSelected().length; i++) {
                        ps.setInt(2, Integer.parseInt(modificarCuentaUsuarioForm.getCartuchosSelected()[i]));
                        ps.setLong(1, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                ps = c.prepareStatement("UPDATE lista SET lista = ?, nombre = ?  WHERE id_lista = (" +
                        "SELECT dominio FROM cuenta_cliente WHERE id_cuenta= ?) ");
                ps.setString(1, modificarCuentaUsuarioForm.getDominio());
                ps.setString(2, modificarCuentaUsuarioForm.getNombre() + "-Semilla");
                ps.setLong(3, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar modificar el usuario", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Error al volver al estado anterior de la base de datos", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return modificarCuentaUsuarioForm;
    }

    public static void updateUAccount(ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String id_account, boolean fromMenu) throws Exception {

        Connection c = null;
        // Editamos los rastreos asociados
        try {
            c = DataBaseManager.getConnection();
            c.setAutoCommit(false);
            editCrawlings(c, modificarCuentaUsuarioForm, id_account, fromMenu);
            modificarCuentaUsuarioForm = CuentaUsuarioDAO.updateUserAccount(c, modificarCuentaUsuarioForm, fromMenu);
            CuentaUsuarioDAO.removeLists(c, modificarCuentaUsuarioForm);
            c.commit();
        } catch (Exception e) {
            Logger.putLog("Error: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (Exception excep) {
                Logger.putLog("Error al volver al estado anterior de la base de datos", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private static void editCrawlings(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String id_account, boolean isMenu) throws Exception {

        // Recorremos los rastreos asociados a la cuenta para desactivarlos si no han sido seleccionados de nuevo
        List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, Long.parseLong(id_account), Constants.CLIENT_ACCOUNT_TYPE);

        if (!isMenu) {
            List<String> cartuchosSelectedList = new ArrayList<>(Arrays.asList(modificarCuentaUsuarioForm.getCartuchosSelected()));
            for (CuentaCliente cuentaCliente : cuentasCliente) {
                // Si uno de los cartuchos anteriores no está entre los seleccionados, se desactiva
                if (!cartuchosSelectedList.contains(String.valueOf(cuentaCliente.getDatosRastreo().getId_cartucho()))) {
                    RastreoDAO.enableDisableCrawler(c, cuentaCliente.getDatosRastreo().getId_rastreo(), false);
                }
            }


            for (String cartuchoSelected : cartuchosSelectedList) {
                InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                // Recorremos los cartuchos seleccionados para editarlos si fueron previamente seleccionados
                // o añadirlos en otro caso
                insertarRastreoForm.setCuenta_cliente(Long.parseLong(id_account));
                CrawlerUtils.insertarDatosAutomaticos(insertarRastreoForm, modificarCuentaUsuarioForm, "");
                insertarRastreoForm.setCartucho(cartuchoSelected);

                boolean updated = false;
                for (CuentaCliente cuentaCliente : cuentasCliente) {
                    if (cartuchoSelected.equals(String.valueOf(cuentaCliente.getDatosRastreo().getId_cartucho()))) {
                        // Hay que editar el rastreo
                        if (!insertarRastreoForm.getCodigo().contains("-")) {
                            insertarRastreoForm.setCodigo(insertarRastreoForm.getCodigo() + "-" + CartuchoDAO.getApplication(c, Long.valueOf(cartuchoSelected)));
                        }
                        RastreoDAO.modificarRastreo(c, false, insertarRastreoForm, (long) cuentaCliente.getDatosRastreo().getId_rastreo());
                        updated = true;
                        RastreoDAO.enableDisableCrawler(c, cuentaCliente.getDatosRastreo().getId_rastreo(), true);
                    }
                }

                if (!updated) {
                    // Si no se ha editado, es que el rastreo es nuevo y hay que añadirlo
                    if (!insertarRastreoForm.getCodigo().contains("-")) {
                        insertarRastreoForm.setCodigo(insertarRastreoForm.getCodigo() + "-" + CartuchoDAO.getApplication(c, Long.valueOf(cartuchoSelected)));
                    }
                    insertarRastreoForm.setActive(true);
                    RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
                }
            }
        } else {
            InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
            insertarRastreoForm.setProfundidad(Integer.parseInt(modificarCuentaUsuarioForm.getProfundidad()));
            insertarRastreoForm.setTopN(Long.parseLong(modificarCuentaUsuarioForm.getAmplitud()));
            List<CartuchoForm> cartuchosCuenta = CuentaUsuarioDAO.getCartridgeFromClientAccount(c, Long.valueOf(id_account));
            for (CartuchoForm cartuchoSelected : cartuchosCuenta) {
                for (CuentaCliente cuentaCliente : cuentasCliente) {
                    if (String.valueOf(cartuchoSelected.getId()).equals(String.valueOf(cuentaCliente.getDatosRastreo().getId_cartucho()))) {
                        RastreoDAO.modificarRastreo(c, true, insertarRastreoForm, (long) cuentaCliente.getDatosRastreo().getId_rastreo());
                    }
                }
            }
        }
    }

    public static List<CartuchoForm> getCartridgeFromClientAccount(Connection c, Long id_account) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CartuchoForm> cartuchoList = new ArrayList<>();
        try {
            ps = c.prepareStatement("SELECT * FROM cartucho c JOIN cuenta_cliente_cartucho ccc " +
                    "ON (c.id_cartucho = ccc.id_cartucho) WHERE id_cuenta = ?");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartuchoForm cartuchoForm = new CartuchoForm();
                cartuchoForm.setId(rs.getLong("id_cartucho"));
                cartuchoForm.setName(rs.getString("aplicacion"));
                cartuchoList.add(cartuchoForm);
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return cartuchoList;
    }

    public static ModificarCuentaUsuarioForm getAccountDatesToUpdate(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT *, l.lista AS semilla, l1.lista AS l_lista_rastreable, l2.lista AS l_lista_no_rastreable " +
                    "FROM cuenta_cliente cc " +
                    "LEFT JOIN lista l ON (cc.dominio = l.id_lista) " +
                    "LEFT JOIN lista l1 ON (cc.lista_rastreable = l1.id_lista) " +
                    "LEFT JOIN lista l2 ON (cc.lista_no_rastreable = l2.id_lista) " +
                    "WHERE cc.id_cuenta = ? ");
            ps.setLong(1, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));
            rs = ps.executeQuery();
            if (rs.next()) {
                modificarCuentaUsuarioForm.setAmplitud(String.valueOf(rs.getInt("amplitud")));
                modificarCuentaUsuarioForm.setDominio(rs.getString("semilla"));
                modificarCuentaUsuarioForm.setListaRastreable(rs.getString("l_lista_rastreable"));
                modificarCuentaUsuarioForm.setListaNoRastreable(rs.getString("l_lista_no_rastreable"));
                modificarCuentaUsuarioForm.setFecha(rs.getTimestamp("fecha_inicio"));
                modificarCuentaUsuarioForm.setNombre(rs.getString("nombre"));
                modificarCuentaUsuarioForm.setPeriodicidad(rs.getString("id_periodicidad"));
                modificarCuentaUsuarioForm.setProfundidad(String.valueOf(rs.getInt("profundidad")));
                modificarCuentaUsuarioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
                modificarCuentaUsuarioForm.setNormaAnalisis(rs.getString("id_guideline"));
                modificarCuentaUsuarioForm.setIdSemilla(rs.getLong("dominio"));
                modificarCuentaUsuarioForm.setIdListaRastreable(rs.getLong("lista_rastreable"));
                modificarCuentaUsuarioForm.setIdListaNoRastreable(rs.getLong("lista_no_rastreable"));
                modificarCuentaUsuarioForm.setLenguaje(rs.getLong("id_language"));
                modificarCuentaUsuarioForm.setActivo(rs.getBoolean("activo"));
                modificarCuentaUsuarioForm.setInDirectory(rs.getBoolean("in_directory"));

                Calendar cl1 = Calendar.getInstance();
                PropertiesManager pmgr = new PropertiesManager();
                DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));

                cl1.setTime(modificarCuentaUsuarioForm.getFecha());
                modificarCuentaUsuarioForm.setFechaInicio(df.format(cl1.getTime()));
                String hour = String.valueOf(cl1.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(cl1.get(Calendar.MINUTE));
                if (Integer.parseInt(hour) < 10) {
                    hour = "0" + hour;
                }
                if (Integer.parseInt(minute) < 10) {
                    minute = "0" + minute;
                }
                modificarCuentaUsuarioForm.setHoraInicio(hour);
                modificarCuentaUsuarioForm.setMinutoInicio(minute);

                modificarCuentaUsuarioForm.setCartuchosList(LoginDAO.getAllUserCartridge(c));
                List<CartuchoForm> selectedCartuchos = getCartridgeFromClientAccount(c, Long.valueOf(modificarCuentaUsuarioForm.getId_cuenta()));
                modificarCuentaUsuarioForm.setCartuchosSelected(new String[selectedCartuchos.size()]);
                int i = 0;
                for (CartuchoForm cartucho : selectedCartuchos) {
                    modificarCuentaUsuarioForm.getCartuchosSelected()[i] = cartucho.getId().toString();
                    i++;
                }
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return modificarCuentaUsuarioForm;
    }

    public static Long getAccountFromUser(Connection c, String user) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT cc.id_cuenta FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " +
                    "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) WHERE usuario = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("id_cuenta");
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return null;
    }

    public static List<LabelValueBean> getAccountsFromUser(Connection c, String user) throws Exception {
        List<LabelValueBean> results = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT cc.id_cuenta, cc.nombre FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " +
                    "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) WHERE usuario = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();

            while (rs.next()) {
                LabelValueBean lvb = new LabelValueBean();
                lvb.setValue(rs.getString("id_cuenta"));
                lvb.setLabel(rs.getString("nombre"));
                results.add(lvb);
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return results;
    }

    public static long getIdSemillaFromCA(Connection c, long id_account) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT dominio FROM cuenta_cliente " +
                    "WHERE id_cuenta = ? ");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("dominio");
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static long getIdLRFromCA(Connection c, long id_account) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT lista_rastreable FROM cuenta_cliente " +
                    "WHERE id_cuenta = ? ");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("lista_rastreable");
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static long getIdLNRFromCA(Connection c, long id_account) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT lista_no_rastreable FROM cuenta_cliente " +
                    "WHERE id_cuenta = ? ");
            ps.setLong(1, id_account);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("lista_no_rastreable");
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

}
