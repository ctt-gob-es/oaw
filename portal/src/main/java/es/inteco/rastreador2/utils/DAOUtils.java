package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.rastreo.NormaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class DAOUtils {

    private DAOUtils() {
    }

    public static void closeQueries(Statement st, ResultSet rs) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el resultset: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el statement: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
        }
    }

    public static List<String> getMailsByRol(long idRol) throws SQLException {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            return LoginDAO.getMailsByRole(c, idRol);
        } catch (Exception e) {
            Logger.putLog("Exception al recuperar la lista de direcciones de correo.", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw new SQLException(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    public static List<NormaForm> getNormas(Connection conn, boolean enlacesRotos) throws SQLException {
        PropertiesManager pmgr = new PropertiesManager();
        List<NormaForm> normas = new ArrayList<NormaForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM tguidelines;");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("cod_guideline") != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.observatorio.intav.id"))) {
                    if (!enlacesRotos) {
                        if (rs.getLong("cod_guideline") != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id")) &&
                                rs.getLong("cod_guideline") != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"))) {
                            NormaForm normaForm = new NormaForm();
                            normaForm.setId(rs.getLong("cod_guideline"));
                            normaForm.setName(rs.getString("des_guideline").substring(0, rs.getString("des_guideline").length() - 4).toUpperCase());
                            normas.add(normaForm);
                        }
                    } else {
                        if (rs.getLong("cod_guideline") == Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux")) &&
                                rs.getLong("cod_guideline") == Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux"))) {
                            NormaForm normaForm = new NormaForm();
                            normaForm.setId(rs.getLong("cod_guideline"));
                            normaForm.setName(rs.getString("des_guideline").substring(0, rs.getString("des_guideline").length() - 4).toUpperCase());
                            normas.add(normaForm);
                        }
                    }
                }
            }

            return normas;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<LenguajeForm> getLenguaje(Connection conn) throws SQLException {
        List<LenguajeForm> lenguajes = new ArrayList<LenguajeForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM languages;");
            rs = ps.executeQuery();
            while (rs.next()) {
                LenguajeForm lenguajeForm = new LenguajeForm();
                lenguajeForm.setId(rs.getLong("id_language"));
                lenguajeForm.setKeyName(rs.getString("key_name"));
                lenguajeForm.setCodice(rs.getString("codice"));
                lenguajes.add(lenguajeForm);
            }

            return lenguajes;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<PeriodicidadForm> getRecurrence(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT * FROM periodicidad;");
            rs = ps.executeQuery();
            List<PeriodicidadForm> recurrenceVector = new ArrayList<PeriodicidadForm>();
            while (rs.next()) {
                PeriodicidadForm periodicidadForm = new PeriodicidadForm();
                periodicidadForm.setId(rs.getLong("id_periodicidad"));
                periodicidadForm.setNombre(rs.getString("nombre"));
                periodicidadForm.setCronExpression(rs.getString("cronExpression"));
                periodicidadForm.setDias(rs.getInt("dias"));
                recurrenceVector.add(periodicidadForm);
            }
            return recurrenceVector;
        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Connection> getCartridgeConnections() {
        List<Connection> results = new ArrayList<Connection>();

        PropertiesManager pmgr = new PropertiesManager();

        List<String> datasources = new ArrayList<String>();
        datasources.add("datasource.name.intav");
        datasources.add("datasource.name.malware");
        datasources.add("datasource.name.lenox");
        datasources.add("datasource.name.multilanguage");

        for (String datasource : datasources) {
            try {
                Connection connection = DataBaseManager.getConnection();
                if (connection != null) {
                    results.add(connection);
                }
            } catch (Exception e) {
                Logger.putLog("Error al conectar a la base de datos: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
                // Avisamos a los administradores, que esto no es muy normal
                CrawlerUtils.warnAdministrators(e, DAOUtils.class);
            }
        }

        return results;
    }

}
