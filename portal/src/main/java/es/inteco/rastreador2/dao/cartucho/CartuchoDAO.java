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

    public static String getApplication(Connection c, Long idCartucho) throws Exception {
        PreparedStatement pes1 = null;
        ResultSet res1 = null;
        String application = "";
        try {
            pes1 = c.prepareStatement("SELECT aplicacion FROM cartucho WHERE id_cartucho = ?");
            pes1.setLong(1, idCartucho);
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

    /**
     * Método que devuelve el id de la normativa asociada al cartucho, en caso de los cartuchos de accesibilidad y -1 en otros casos
     * @param c Connection a la base de datos
     * @param idCartucho id del cartucho
     * @return el id de la normativa asociada si es un cartucho de accesibilidad y -1 en otros casos
     * @throws Exception
     */
    public static int getGuideline(final Connection c, long idCartucho) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int idGuideline = -1;
        try {
            preparedStatement = c.prepareStatement("SELECT id_guideline FROM cartucho WHERE id_cartucho = ?");
            preparedStatement.setLong(1, idCartucho);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idGuideline = resultSet.getInt(1);
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(preparedStatement, resultSet);
        }
        return idGuideline;
    }

    /**
     * Método que dado un id de cartucho indica si el cartucho pertenece a una normativa de accesibilidad o no
     * @param c Connection a la base de datos
     * @param idCartucho id del cartucho
     * @return true si el cartucho pertenece a un cartucho de accesibilidad
     * @throws Exception
     */
    public static boolean isCartuchoAccesibilidad(Connection c, long idCartucho) throws Exception {
        PreparedStatement pes = null;
        ResultSet res = null;
        try {
            pes = c.prepareStatement("SELECT nombre FROM cartucho WHERE id_cartucho = ?");
            pes.setLong(1, idCartucho);
            res = pes.executeQuery();
            if (res.next()) {
                return "es.inteco.accesibilidad.CartuchoAccesibilidad".equals(res.getString("nombre"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al isCartuchoAccesibilidad", CartuchoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(pes, res);
        }
        return false;
    }

    private static String[] getCartridgeNamesArray(List<String> cartridgeNames) {
        final String[] names = new String[cartridgeNames.size()];

        for (int i = 0; i < cartridgeNames.size(); i++) {
            names[i] = cartridgeNames.get(i);
        }

        return names;
    }

}
