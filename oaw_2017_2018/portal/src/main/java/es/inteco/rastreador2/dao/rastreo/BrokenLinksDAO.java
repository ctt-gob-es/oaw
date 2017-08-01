package es.inteco.rastreador2.dao.rastreo;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.BrokenLinks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class BrokenLinksDAO {

    private BrokenLinksDAO() {
    }

    public static List<BrokenLinks> getBrokenLinks(Connection conn, long idCrawling, int pagina) throws SQLException {
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        final int resultFrom = pagSize * pagina;

        final List<BrokenLinks> brokenLinksList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM enlaces_rotos WHERE id_rastreo_realizado = ? LIMIT ? OFFSET ?")) {
            pst.setLong(1, idCrawling);
            pst.setInt(2, pagSize);
            pst.setInt(3, resultFrom);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BrokenLinks brokenLinks = new BrokenLinks();
                    brokenLinks.setUrl(rs.getString("url"));
                    brokenLinks.setNumBrokenLinks(rs.getInt("num_enlaces"));
                    brokenLinksList.add(brokenLinks);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener la lista de enlaces rotos", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return brokenLinksList;
    }

    public static int getNumBrokenLinks(final Connection conn, final long idRastreoRealizado) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement("SELECT count(*) FROM enlaces_rotos WHERE id_rastreo_realizado = ?")) {
            pst.setLong(1, idRastreoRealizado);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener la lista de enlaces rotos", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }
}
