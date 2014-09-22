package es.inteco.rastreador2.dao.cartucho;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.WebAnalyzerDao;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


public final class CartuchoDAO {

    private CartuchoDAO() {
    }

    public static String getApplication(Connection c, int id_cartridge) throws Exception {
        PreparedStatement pes1 = null;
        ResultSet res1 = null;
        String application = "";
        try {
            pes1 = c.prepareStatement("SELECT aplicacion FROM cartucho WHERE Id_Cartucho = ?");
            pes1.setInt(1, id_cartridge);
            res1 = pes1.executeQuery();
            while (res1.next()) {
                application = res1.getString(1);
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(pes1, res1);
        }
        return application;
    }

    public static String[] getNombreCartucho(long idTracking) throws Exception {
        List<String> cartridgeNames = null;
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            cartridgeNames = WebAnalyzerDao.getCartridgeNames(conn, idTracking);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(conn);
        }

        return getCartridgeNamesArray(cartridgeNames);
    }

    private static String[] getCartridgeNamesArray(List<String> cartridgeNames) {
        final String[] names = new String[cartridgeNames.size()];

        for (int i = 0; i < cartridgeNames.size(); i++) {
            names[i] = cartridgeNames.get(i);
        }

        return names;
    }

}
