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
import es.inteco.rastreador2.utils.ListadoCuentasUsuario;
import es.inteco.rastreador2.utils.Rastreo;
import org.apache.struts.util.LabelValueBean;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static org.apache.commons.lang3.StringUtils.leftPad;

public final class CuentaUsuarioDAO {

    private CuentaUsuarioDAO() {
    }

    private static String getClientAccountsQuery(int type, long idAccount) {
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

    public static List<CuentaCliente> getClientAccounts(Connection c, long idAccount, int type) throws SQLException {
        final List<CuentaCliente> clientAccounts = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(getClientAccountsQuery(type, idAccount))) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final CuentaCliente cuentaCliente = new CuentaCliente();
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

                    final LenguajeForm lenguajeForm = new LenguajeForm();
                    lenguajeForm.setId(rs.getLong("id_language"));
                    lenguajeForm.setCodice(rs.getString("codice"));
                    lenguajeForm.setKeyName(rs.getString("key_name"));
                    cuentaCliente.getDatosRastreo().setLanguage(lenguajeForm);

                    clientAccounts.add(cuentaCliente);
                }
            }
            return clientAccounts;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static int countAccounts(final Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM cuenta_cliente");
             final ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Logger.putLog("Error eb countAccounts ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static Long insertClientAccount(final Connection connection, final NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm) throws SQLException {
        final PropertiesManager pmgr = new PropertiesManager();
        try (PreparedStatement insertCuentaCliente = connection.prepareStatement("INSERT INTO cuenta_cliente (nombre, dominio, id_periodicidad, profundidad, amplitud, fecha_inicio, lista_rastreable, lista_no_rastreable, id_guideline, pseudoaleatorio, id_language, activo, in_directory) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertCuentaCliente.setString(1, nuevaCuentaUsuarioForm.getNombre());
            insertCuentaCliente.setLong(2, nuevaCuentaUsuarioForm.getIdSeed());
            insertCuentaCliente.setLong(3, Long.parseLong(nuevaCuentaUsuarioForm.getPeriodicidad()));
            insertCuentaCliente.setInt(4, Integer.parseInt(nuevaCuentaUsuarioForm.getProfundidad()));
            insertCuentaCliente.setInt(5, Integer.parseInt(nuevaCuentaUsuarioForm.getAmplitud()));
            insertCuentaCliente.setTimestamp(6, new Timestamp(nuevaCuentaUsuarioForm.getFecha().getTime()));
            if (nuevaCuentaUsuarioForm.getIdCrawlableList() != 0) {
                insertCuentaCliente.setLong(7, nuevaCuentaUsuarioForm.getIdCrawlableList());
            } else {
                insertCuentaCliente.setString(7, null);
            }

            if (nuevaCuentaUsuarioForm.getIdNoCrawlableList() != 0) {
                insertCuentaCliente.setLong(8, nuevaCuentaUsuarioForm.getIdNoCrawlableList());
            } else {
                insertCuentaCliente.setString(8, null);
            }

            if (nuevaCuentaUsuarioForm.getNormaAnalisisEnlaces().equals("1")) {
                if (nuevaCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
                    insertCuentaCliente.setString(9, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
                } else if (nuevaCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
                    insertCuentaCliente.setString(9, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
                } else {
                    insertCuentaCliente.setString(9, nuevaCuentaUsuarioForm.getNormaAnalisis());
                }

            } else {
                insertCuentaCliente.setString(9, nuevaCuentaUsuarioForm.getNormaAnalisis());
            }

            insertCuentaCliente.setBoolean(10, nuevaCuentaUsuarioForm.isPseudoAleatorio());
            insertCuentaCliente.setLong(11, nuevaCuentaUsuarioForm.getLenguaje());
            insertCuentaCliente.setBoolean(12, nuevaCuentaUsuarioForm.isActivo());
            insertCuentaCliente.setBoolean(13, nuevaCuentaUsuarioForm.isInDirectory());
            insertCuentaCliente.executeUpdate();
            long idAccount = 0;
            try (ResultSet rs = insertCuentaCliente.getGeneratedKeys()) {
                if (rs.next()) {
                    idAccount = rs.getLong(1);
                }
            }

            insertCuentaClienteCartucho(connection, idAccount, nuevaCuentaUsuarioForm.getCartuchosSelected());

            connection.commit();
            connection.setAutoCommit(true);
            return idAccount;
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private static void insertCuentaClienteCartucho(final Connection c, final long idAccount, final String[] selectedCartuchos) throws SQLException {
        try (PreparedStatement insertCuentaClienteCartucho = c.prepareStatement("INSERT INTO cuenta_cliente_cartucho VALUES (?,?)")) {
            for (String selectedCartucho : selectedCartuchos) {
                insertCuentaClienteCartucho.setLong(1, idAccount);
                insertCuentaClienteCartucho.setLong(2, Integer.parseInt(selectedCartucho));
                insertCuentaClienteCartucho.addBatch();
            }

            insertCuentaClienteCartucho.executeBatch();
        }
    }

    public static CargarCuentasUsuarioForm userList(Connection c, CargarCuentasUsuarioForm cargarCuentasUsuarioForm, int page) throws SQLException {
        final List<ListadoCuentasUsuario> userAccountList = new ArrayList<>();
        final String query;
        if (page == Constants.NO_PAGINACION) {
            query = "SELECT DISTINCT(cc.nombre), l.lista, cc.id_cuenta, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos " +
                    "FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                    "JOIN lista l ON (cc.dominio = l.id_lista)" +
                    "JOIN cartucho c ON (ccc.id_cartucho = c.id_cartucho) GROUP BY cc.nombre ORDER BY cc.nombre";
        } else {
            query = "SELECT DISTINCT(cc.nombre), l.lista, cc.id_cuenta, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos " +
                    "FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                    "JOIN lista l ON (cc.dominio = l.id_lista)" +
                    "JOIN cartucho c ON (ccc.id_cartucho = c.id_cartucho) GROUP BY cc.nombre ORDER BY cc.nombre LIMIT ? OFFSET ?";
        }
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        final int resultFrom = pagSize * page;
        try (PreparedStatement ps = c.prepareStatement(query)) {
            if (page != Constants.NO_PAGINACION) {
                ps.setInt(1, pagSize);
                ps.setInt(2, resultFrom);
            }
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return cargarCuentasUsuarioForm;
    }

    private static List<String> listFromString(final String listString, final String separator) {
        final List<String> roleList = new ArrayList<>();
        if (listString == null) {
            return null;
        }
        final StringTokenizer tokenizer = new StringTokenizer(listString, separator);
        while (tokenizer.hasMoreTokens()) {
            roleList.add(tokenizer.nextToken());
        }
        return roleList;
    }

    /**
     * Comprueba si existe una cuenta con un determinado nombre de usuario
     *
     * @param connection  conexión Connection a la base de datos
     * @param accountName nombre de la cuenta a comprobar
     * @return true si existe una cuenta con ese nombre o false en caso contrario
     * @throws SQLException
     */
    public static boolean existAccount(final Connection connection, final String accountName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM cuenta_cliente WHERE nombre = ?")) {
            ps.setString(1, accountName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Logger.putLog("Error en existAccount", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static NuevaCuentaUsuarioForm getUserAccount(final Connection connection, final Long idAccount) throws SQLException {
        final NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm = new NuevaCuentaUsuarioForm();
        try (PreparedStatement ps = connection.prepareStatement("SELECT *, p.nombre AS nombrePeriodicidad, cc.nombre as nombreCuenta FROM cuenta_cliente cc " +
                "JOIN periodicidad p ON (cc.id_periodicidad = p.id_periodicidad) WHERE id_cuenta = ?")) {
            ps.setLong(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
            return nuevaCuentaUsuarioForm;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static String usersByAccountType(final Connection connection, final Long idAccount) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(cc.id_cuenta), GROUP_CONCAT(DISTINCT(u.usuario)) as usuarios " +
                "FROM cuenta_cliente cc " +
                "JOIN cuenta_cliente_usuario ccu ON (ccu.id_cuenta = cc.id_cuenta) " +
                "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) " +
                "WHERE cc.id_cuenta = ? GROUP BY u.usuario;")) {
            ps.setLong(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("usuarios");
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return null;
    }

    public static VerCuentaUsuarioForm getDatosUsuarioVer(Connection connection, VerCuentaUsuarioForm verCuentaUsuarioForm, Long idAccount) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(cc.nombre) AS nombreCuenta, cc.id_guideline, cc.activo, cc.in_directory, " +
                "l.lista AS semilla, l1.lista AS listaRastreable, l2.lista AS listaNoRastreable, p.nombre " +
                "AS periodicidad, profundidad, amplitud, pseudoaleatorio, GROUP_CONCAT(DISTINCT(c.aplicacion)) " +
                "as cartuchos FROM cuenta_cliente cc " +
                "JOIN periodicidad p ON (p.id_periodicidad = cc.id_periodicidad) " +
                "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                "JOIN cartucho c ON (c.id_cartucho = ccc.id_cartucho) " +
                "JOIN lista l ON (cc.dominio = l.id_lista) " +
                "LEFT JOIN lista l1 ON (cc.lista_rastreable = l1.id_lista) " +
                "LEFT JOIN lista l2 ON (cc.lista_no_rastreable = l2.id_lista) " +
                "WHERE cc.id_cuenta = ? GROUP BY cc.nombre;")) {
            ps.setLong(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    populateVerCuentaUsuarioFormFromResultSet(connection, verCuentaUsuarioForm, idAccount, rs);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return verCuentaUsuarioForm;
    }

    private static void populateVerCuentaUsuarioFormFromResultSet(Connection connection, VerCuentaUsuarioForm verCuentaUsuarioForm, Long idAccount, ResultSet rs) throws SQLException {
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

        final String uCliente = usersByAccountType(connection, idAccount);
        final String rCliente = usersByAccountType(connection, idAccount);

        if (uCliente != null) {
            verCuentaUsuarioForm.setUsuarios(listFromString(uCliente, ","));
        }
        if (rCliente != null) {
            verCuentaUsuarioForm.setResponsable(listFromString(rCliente, ","));
        }
    }

    public static EliminarCuentaUsuarioForm getDeleteUserAccounts(Connection connection, Long idAccount, EliminarCuentaUsuarioForm eliminarCuentaUsuarioForm) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(cc.nombre) AS nombreCuenta, cc.*,l.lista, GROUP_CONCAT(DISTINCT(c.aplicacion)) as cartuchos " +
                "FROM cuenta_cliente cc " +
                "JOIN cuenta_cliente_cartucho ccc ON (ccc.id_cuenta = cc.id_cuenta) " +
                "JOIN cartucho c ON (c.id_cartucho = ccc.id_cartucho) " +
                "JOIN lista l ON (cc.dominio = l.id_lista) " +
                "WHERE cc.id_cuenta = ? GROUP BY cc.nombre")) {
            ps.setLong(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
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

                    String uCliente = usersByAccountType(connection, idAccount);
                    String rCliente = usersByAccountType(connection, idAccount);

                    if (uCliente != null) {
                        eliminarCuentaUsuarioForm.setUsuarios(listFromString(uCliente, ","));
                    }
                    if (rCliente != null) {
                        eliminarCuentaUsuarioForm.setResponsable(listFromString(rCliente, ","));
                    }
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return eliminarCuentaUsuarioForm;
    }

    public static void deleteUserAccount(Connection c, Long idAccount, EliminarCuentaUsuarioForm eliminarCuentaUsuarioForm) throws SQLException {
        try {
            c.setAutoCommit(false);

            // Borramos primero los resultados de los cartuchos
            final List<Long> executedCrawlingIdsList = RastreoDAO.getExecutedCrawlerClientAccountsIds(c, eliminarCuentaUsuarioForm.getId());
            if (executedCrawlingIdsList != null && !executedCrawlingIdsList.isEmpty()) {
                RastreoDAO.deleteAnalyse(c, executedCrawlingIdsList);
            }

            try (PreparedStatement deleteUsuarioStatement = c.prepareStatement("DELETE FROM usuario WHERE id_usuario IN (" +
                    "SELECT id_usuario FROM cuenta_cliente_usuario WHERE id_cuenta = ?)")) {
                deleteUsuarioStatement.setLong(1, idAccount);
                deleteUsuarioStatement.executeUpdate();
            }

            try (PreparedStatement deleteRastreoStatement = c.prepareStatement("DELETE FROM rastreo WHERE id_cuenta = ? ")) {
                deleteRastreoStatement.setLong(1, idAccount);
                deleteRastreoStatement.executeUpdate();
            }

            try (PreparedStatement deleteCuentaClienteStatement = c.prepareStatement("DELETE FROM cuenta_cliente WHERE id_cuenta = ?")) {
                deleteCuentaClienteStatement.setLong(1, idAccount);
                deleteCuentaClienteStatement.executeUpdate();
            }

            try (PreparedStatement deleteListaStatement = c.prepareStatement("DELETE FROM lista WHERE id_lista IN (?,?,?)")) {
                deleteListaStatement.setLong(1, eliminarCuentaUsuarioForm.getIdSemilla());
                deleteListaStatement.setLong(2, eliminarCuentaUsuarioForm.getIdListaRastreable());
                deleteListaStatement.setLong(3, eliminarCuentaUsuarioForm.getIdListaNoRastreable());
                deleteListaStatement.executeUpdate();
            }

            c.commit();
            c.setAutoCommit(true);
        } catch (SQLException e) {
            c.rollback();
            c.setAutoCommit(true);
            throw e;
        }
    }

    public static List<FulFilledCrawling> getFulfilledCrawlings(Connection connection, Long idCrawling, int page) throws SQLException {
        final List<FulFilledCrawling> crawlings = new ArrayList<>();
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        final int resultFrom = pagSize * page;
        try (PreparedStatement ps = connection.prepareStatement("SELECT rr.* FROM rastreos_realizados rr " +
                "WHERE id_rastreo = ? ORDER BY fecha DESC LIMIT ? OFFSET ?")) {
            ps.setLong(1, idCrawling);
            ps.setInt(2, pagSize);
            ps.setInt(3, resultFrom);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
                    fulfilledCrawling.setId(rs.getLong("id"));
                    fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
                    fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
                    fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
                    crawlings.add(fulfilledCrawling);
                }
            }
            return crawlings;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static int getNumClientFulfilledCrawlings(Connection c, Long idClient) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM rastreos_realizados rr WHERE id_rastreo = ?")) {
            ps.setLong(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static int getNumClientCrawlings(Connection connection, Long idClient) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM rastreo r " +
                "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                "WHERE ccu.id_usuario = ?")) {
            ps.setLong(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<Rastreo> getClientCrawlings(Connection c, Long idClient, int page) throws SQLException {
        final String query;
        if (page == Constants.NO_PAGINACION) {
            query = "SELECT r.*, c.aplicacion FROM rastreo r " +
                    "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                    "JOIN cartucho_rastreo cr ON (cr.id_rastreo = r.id_rastreo)  " +
                    "JOIN cartucho c ON (cr.id_cartucho = c.id_cartucho) " +
                    "WHERE ccu.id_usuario = ?";
        } else {
            query = "SELECT r.*, c.aplicacion FROM rastreo r " +
                    "JOIN cuenta_cliente_usuario ccu ON (r.id_cuenta = ccu.id_cuenta) " +
                    "JOIN cartucho_rastreo cr ON (cr.id_rastreo = r.id_rastreo)  " +
                    "JOIN cartucho c ON (cr.id_cartucho = c.id_cartucho) " +
                    "WHERE ccu.id_usuario = ? LIMIT ? OFFSET ?";
        }
        final List<Rastreo> crawlings = new ArrayList<>();
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        final int resultFrom = pagSize * page;

        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setLong(1, idClient);
            if (page != Constants.NO_PAGINACION) {
                ps.setInt(2, pagSize);
                ps.setInt(3, resultFrom);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final Rastreo rastreo = new Rastreo();
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
                        Logger.putLog("MySQL falla al recuperar valores que él mismo introduce por defecto :O", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    }
                    rastreo.setProfundidad(rs.getInt("profundidad"));
                    String crawlingName = rs.getString("nombre_rastreo");
                    if (crawlingName.contains("-")) {
                        crawlingName = crawlingName.substring(0, crawlingName.indexOf('-'));
                    }
                    rastreo.setCodigo(crawlingName);

                    crawlings.add(rastreo);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener los datos de la lista de rastreos de clientes", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
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
        final PropertiesManager pmgr = new PropertiesManager();
        boolean hasIntav = false;
        try {
            for (int i = 0; i < modificarCuentaUsuarioForm.getCartuchosSelected().length; i++) {
                if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(modificarCuentaUsuarioForm.getCartuchosSelected()[i]))) {
                    hasIntav = true;
                }
            }

            modificarCuentaUsuarioForm = updateLists(c, modificarCuentaUsuarioForm);

            final long idAccount = Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta());
            if (!fromMenu) {
                try (PreparedStatement updateCuentaClienteStatement = c.prepareStatement("UPDATE cuenta_cliente SET Nombre = ?, Id_Periodicidad = ?, Profundidad = ?, Amplitud = ?, Fecha_Inicio = ?, lista_rastreable = ?, lista_no_rastreable = ?, id_guideline = ?, pseudoaleatorio = ?, id_language = ?, activo = ?, in_directory = ? WHERE id_cuenta = ?")) {
                    if (hasIntav) {
                        if ("1".equals(modificarCuentaUsuarioForm.getNormaAnalisisEnlaces())) {
                            if (modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
                                updateCuentaClienteStatement.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
                            } else if (modificarCuentaUsuarioForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
                                updateCuentaClienteStatement.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
                            } else {
                                updateCuentaClienteStatement.setString(8, modificarCuentaUsuarioForm.getNormaAnalisis());
                            }

                        } else {
                            updateCuentaClienteStatement.setString(8, modificarCuentaUsuarioForm.getNormaAnalisis());
                        }
                    } else {
                        updateCuentaClienteStatement.setString(8, null);
                    }
                    updateCuentaClienteStatement.setString(1, modificarCuentaUsuarioForm.getNombre());
                    updateCuentaClienteStatement.setInt(2, Integer.parseInt(modificarCuentaUsuarioForm.getPeriodicidad()));
                    updateCuentaClienteStatement.setInt(3, Integer.parseInt(modificarCuentaUsuarioForm.getProfundidad()));
                    updateCuentaClienteStatement.setInt(4, Integer.parseInt(modificarCuentaUsuarioForm.getAmplitud()));
                    modificarCuentaUsuarioForm.setFecha(CrawlerUtils.getFechaInicio(modificarCuentaUsuarioForm.getFechaInicio(),
                            modificarCuentaUsuarioForm.getHoraInicio(), modificarCuentaUsuarioForm.getMinutoInicio()));
                    updateCuentaClienteStatement.setTimestamp(5, new Timestamp(modificarCuentaUsuarioForm.getFecha().getTime()));

                    if (modificarCuentaUsuarioForm.getIdListaRastreable() != 0) {
                        updateCuentaClienteStatement.setLong(6, modificarCuentaUsuarioForm.getIdListaRastreable());
                    } else {
                        updateCuentaClienteStatement.setString(6, null);
                    }

                    if (modificarCuentaUsuarioForm.getIdListaNoRastreable() != 0) {
                        updateCuentaClienteStatement.setLong(7, modificarCuentaUsuarioForm.getIdListaNoRastreable());
                    } else {
                        updateCuentaClienteStatement.setString(7, null);
                    }

                    updateCuentaClienteStatement.setBoolean(9, modificarCuentaUsuarioForm.isPseudoAleatorio());
                    updateCuentaClienteStatement.setLong(10, modificarCuentaUsuarioForm.getLenguaje());
                    updateCuentaClienteStatement.setBoolean(11, modificarCuentaUsuarioForm.isActivo());
                    updateCuentaClienteStatement.setBoolean(12, modificarCuentaUsuarioForm.isInDirectory());
                    updateCuentaClienteStatement.setLong(13, idAccount);

                    updateCuentaClienteStatement.executeUpdate();
                } catch (SQLException e) {
                    c.rollback();
                    c.setAutoCommit(true);
                    throw e;
                }
            }

            if (!fromMenu) {
                // Borramos los roles antiguos
                try (PreparedStatement deleteCuentaClienteStatement = c.prepareStatement("DELETE FROM cuenta_cliente_cartucho WHERE id_cuenta = ?")) {
                    deleteCuentaClienteStatement.setLong(1, idAccount);
                    deleteCuentaClienteStatement.executeUpdate();
                } catch (Exception e) {
                    c.rollback();
                    c.setAutoCommit(true);
                    throw e;
                }

                // Insertamos los cartuchos nuevos
                if (modificarCuentaUsuarioForm.getCartuchosSelected() != null
                        && modificarCuentaUsuarioForm.getCartuchosSelected().length > 0 && idAccount != 0) {
                    insertCuentaClienteCartucho(c, idAccount, modificarCuentaUsuarioForm.getCartuchosSelected());
                }

                try (PreparedStatement updateListaStatement = c.prepareStatement("UPDATE lista SET lista = ?, nombre = ?  WHERE id_lista = (" +
                        "SELECT dominio FROM cuenta_cliente WHERE id_cuenta= ?) ")) {
                    updateListaStatement.setString(1, modificarCuentaUsuarioForm.getDominio());
                    updateListaStatement.setString(2, modificarCuentaUsuarioForm.getNombre() + "-Semilla");
                    updateListaStatement.setLong(3, idAccount);
                    updateListaStatement.executeUpdate();
                } catch (SQLException e) {
                    c.rollback();
                    c.setAutoCommit(true);
                    throw e;
                }
            }
            c.commit();
            c.setAutoCommit(true);
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar modificar el usuario", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            c.rollback();
            c.setAutoCommit(true);
            throw e;
        }
        return modificarCuentaUsuarioForm;
    }

    public static void updateUAccount(ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String idAccount, boolean fromMenu) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            c.setAutoCommit(false);
            try {
                editCrawlings(c, modificarCuentaUsuarioForm, idAccount, fromMenu);
                modificarCuentaUsuarioForm = CuentaUsuarioDAO.updateUserAccount(c, modificarCuentaUsuarioForm, fromMenu);
                CuentaUsuarioDAO.removeLists(c, modificarCuentaUsuarioForm);
                c.commit();
                c.setAutoCommit(true);
            } catch (Exception e) {
                Logger.putLog("Error: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                c.rollback();
                c.setAutoCommit(true);
                throw e;
            }
            c.setAutoCommit(true);
        }
    }

    private static void editCrawlings(Connection c, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String idAccount, boolean isMenu) throws Exception {
        // Recorremos los rastreos asociados a la cuenta para desactivarlos si no han sido seleccionados de nuevo
        List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, Long.parseLong(idAccount), Constants.CLIENT_ACCOUNT_TYPE);

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
                insertarRastreoForm.setCuenta_cliente(Long.parseLong(idAccount));
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
            List<CartuchoForm> cartuchosCuenta = CuentaUsuarioDAO.getCartridgeFromClientAccount(c, Long.valueOf(idAccount));
            for (CartuchoForm cartuchoSelected : cartuchosCuenta) {
                for (CuentaCliente cuentaCliente : cuentasCliente) {
                    if (String.valueOf(cartuchoSelected.getId()).equals(String.valueOf(cuentaCliente.getDatosRastreo().getId_cartucho()))) {
                        RastreoDAO.modificarRastreo(c, true, insertarRastreoForm, (long) cuentaCliente.getDatosRastreo().getId_rastreo());
                    }
                }
            }
        }
    }

    public static List<CartuchoForm> getCartridgeFromClientAccount(final Connection connection, final Long idAccount) throws SQLException {
        final List<CartuchoForm> cartuchoList = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM cartucho c JOIN cuenta_cliente_cartucho ccc " +
                "ON (c.id_cartucho = ccc.id_cartucho) WHERE id_cuenta = ?")) {
            ps.setLong(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartuchoForm cartuchoForm = new CartuchoForm();
                    cartuchoForm.setId(rs.getLong("id_cartucho"));
                    cartuchoForm.setName(rs.getString("aplicacion"));
                    cartuchoList.add(cartuchoForm);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return cartuchoList;
    }

    public static ModificarCuentaUsuarioForm getAccountDatesToUpdate(Connection connection, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT *, l.lista AS semilla, l1.lista AS l_lista_rastreable, l2.lista AS l_lista_no_rastreable " +
                "FROM cuenta_cliente cc " +
                "LEFT JOIN lista l ON (cc.dominio = l.id_lista) " +
                "LEFT JOIN lista l1 ON (cc.lista_rastreable = l1.id_lista) " +
                "LEFT JOIN lista l2 ON (cc.lista_no_rastreable = l2.id_lista) " +
                "WHERE cc.id_cuenta = ? ")) {
            ps.setLong(1, Long.parseLong(modificarCuentaUsuarioForm.getId_cuenta()));
            try (ResultSet rs = ps.executeQuery()) {
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

                    final PropertiesManager pmgr = new PropertiesManager();
                    final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));

                    final Calendar cl1 = Calendar.getInstance();
                    cl1.setTime(modificarCuentaUsuarioForm.getFecha());
                    modificarCuentaUsuarioForm.setFechaInicio(df.format(cl1.getTime()));
                    final String hour = leftPad(String.valueOf(cl1.get(Calendar.HOUR_OF_DAY)), 2, '0');
                    final String minute = leftPad(String.valueOf(cl1.get(Calendar.MINUTE)), 2, '0');
                    modificarCuentaUsuarioForm.setHoraInicio(hour);
                    modificarCuentaUsuarioForm.setMinutoInicio(minute);

                    modificarCuentaUsuarioForm.setCartuchosList(LoginDAO.getAllUserCartridge(connection));
                    List<CartuchoForm> selectedCartuchos = getCartridgeFromClientAccount(connection, Long.valueOf(modificarCuentaUsuarioForm.getId_cuenta()));
                    modificarCuentaUsuarioForm.setCartuchosSelected(new String[selectedCartuchos.size()]);
                    int i = 0;
                    for (CartuchoForm cartucho : selectedCartuchos) {
                        modificarCuentaUsuarioForm.getCartuchosSelected()[i] = cartucho.getId().toString();
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return modificarCuentaUsuarioForm;
    }

    public static Long getAccountFromUser(final Connection connection, final String user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT cc.id_cuenta FROM cuenta_cliente cc " +
                "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " +
                "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) WHERE usuario = ?")) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_cuenta");
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return null;
    }

    public static List<LabelValueBean> getAccountsFromUser(final Connection connection, final String user) throws SQLException {
        final List<LabelValueBean> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT cc.id_cuenta, cc.nombre FROM cuenta_cliente cc " +
                "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " +
                "JOIN usuario u ON (ccu.id_usuario = u.id_usuario) WHERE usuario = ?")) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final LabelValueBean lvb = new LabelValueBean();
                    lvb.setValue(rs.getString("id_cuenta"));
                    lvb.setLabel(rs.getString("nombre"));
                    results.add(lvb);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return results;
    }

    public static long getIdSemillaFromCA(final Connection connection, final long idAccount) throws SQLException {
        return getFieldFromCuentaClienteByIdCuenta(connection, idAccount, "SELECT dominio FROM cuenta_cliente WHERE id_cuenta = ?", "dominio");
    }

    public static long getIdLRFromCA(final Connection connection, final long idAccount) throws SQLException {
        return getFieldFromCuentaClienteByIdCuenta(connection, idAccount, "SELECT lista_rastreable FROM cuenta_cliente WHERE id_cuenta = ?", "lista_rastreable");
    }

    public static long getIdLNRFromCA(final Connection connection, final long idAccount) throws SQLException {
        return getFieldFromCuentaClienteByIdCuenta(connection, idAccount, "SELECT lista_no_rastreable FROM cuenta_cliente WHERE id_cuenta = ?", "lista_no_rastreable");
    }

    private static long getFieldFromCuentaClienteByIdCuenta(final Connection connection, final long idCuenta, final String query, final String field) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, idCuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(field);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return 0;
    }

}
