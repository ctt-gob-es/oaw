/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
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

/**
 * The Class BrokenLinksDAO.
 */
public final class BrokenLinksDAO {

    /**
	 * Instantiates a new broken links DAO.
	 */
    private BrokenLinksDAO() {
    }

    /**
	 * Gets the broken links.
	 *
	 * @param conn       the conn
	 * @param idCrawling the id crawling
	 * @param pagina     the pagina
	 * @return the broken links
	 * @throws SQLException the SQL exception
	 */
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

    /**
	 * Gets the num broken links.
	 *
	 * @param conn               the conn
	 * @param idRastreoRealizado the id rastreo realizado
	 * @return the num broken links
	 * @throws SQLException the SQL exception
	 */
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
