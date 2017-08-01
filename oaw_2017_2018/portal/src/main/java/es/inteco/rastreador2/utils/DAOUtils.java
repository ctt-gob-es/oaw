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

    public static void closeQueries(final Statement st, final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el resultset: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el statement: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    public static List<String> getMailsByRol(long idRol) throws SQLException {
        try (Connection c = DataBaseManager.getConnection()) {
            return LoginDAO.getMailsByRole(c, idRol);
        } catch (Exception e) {
            Logger.putLog("Exception al recuperar la lista de direcciones de correo.", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw new SQLException(e);
        }
    }

    public static List<NormaForm> getNormas(Connection conn, boolean enlacesRotos) throws SQLException {
        final PropertiesManager pmgr = new PropertiesManager();
        final List<NormaForm> normas = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM tguidelines;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final long codGuideline = rs.getLong("cod_guideline");
                if (codGuideline != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.observatorio.intav.id"))) {
                    if (!enlacesRotos) {
                        if (codGuideline != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id")) &&
                                codGuideline != Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"))) {
                            NormaForm normaForm = new NormaForm();
                            normaForm.setId(codGuideline);
                            normaForm.setName(rs.getString("des_guideline").substring(0, rs.getString("des_guideline").length() - 4).toUpperCase());
                            normas.add(normaForm);
                        }
                    } else {
                        if (codGuideline == Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux")) &&
                                codGuideline == Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux"))) {
                            NormaForm normaForm = new NormaForm();
                            normaForm.setId(codGuideline);
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
        }
    }

    public static List<LenguajeForm> getLenguaje(Connection conn) throws SQLException {
        final List<LenguajeForm> lenguajes = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM languages;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final LenguajeForm lenguajeForm = new LenguajeForm();
                lenguajeForm.setId(rs.getLong("id_language"));
                lenguajeForm.setKeyName(rs.getString("key_name"));
                lenguajeForm.setCodice(rs.getString("codice"));
                lenguajes.add(lenguajeForm);
            }

            return lenguajes;
        } catch (SQLException e) {
            Logger.putLog("Exception: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<PeriodicidadForm> getRecurrence(Connection c) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM periodicidad;");
             ResultSet rs = ps.executeQuery()) {
            final List<PeriodicidadForm> recurrenceVector = new ArrayList<>();
            while (rs.next()) {
                final PeriodicidadForm periodicidadForm = new PeriodicidadForm();
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
        }
    }

}
