package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.BrokenLinks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class WebAnalyzerDao {

    private WebAnalyzerDao() {
    }

    public static List<String> getCartridgeNames(Connection conn, Long idTracking) {
        List<String> cartridgeNames = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT nombre FROM cartucho c " +
                    "JOIN cartucho_rastreo cr ON (c.id_cartucho = cr.id_cartucho) " +
                    "WHERE cr.id_rastreo = ?");
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cartridgeNames.add(rs.getString("nombre"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los nombres de cartucho", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el resultset", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el preparedStament", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
        return cartridgeNames;
    }

    public static void saveBrokenLinksList(Connection conn, Long idCrawling, List<BrokenLinks> brokenLinksList) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO enlaces_rotos VALUES (?, ?, ?)");
            for (BrokenLinks brokenLinks : brokenLinksList) {
                pstmt.setLong(1, idCrawling);
                pstmt.setString(2, brokenLinks.getUrl());
                pstmt.setInt(3, brokenLinks.getNumBrokenLinks());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el preparedStament", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    public static void saveBrokenLinks(Connection conn, Long idCrawling, BrokenLinks brokenLinks) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO enlaces_rotos VALUES (?, ?, ?)");
            pstmt.setLong(1, idCrawling);
            pstmt.setString(2, brokenLinks.getUrl());
            pstmt.setInt(3, brokenLinks.getNumBrokenLinks());

            pstmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el preparedStament", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }
}
