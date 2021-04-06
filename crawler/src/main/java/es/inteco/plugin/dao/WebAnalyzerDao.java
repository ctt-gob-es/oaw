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
package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.BrokenLinks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class WebAnalyzerDao.
 */
public final class WebAnalyzerDao {

    /**
	 * Instantiates a new web analyzer dao.
	 */
    private WebAnalyzerDao() {
    }

    /**
	 * Gets the cartridge names.
	 *
	 * @param connection the connection
	 * @param idTracking the id tracking
	 * @return the cartridge names
	 */
    public static List<String> getCartridgeNames(final Connection connection, final Long idTracking) {
        final List<String> cartridgeNames = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT nombre FROM cartucho c " +
                "JOIN cartucho_rastreo cr ON (c.id_cartucho = cr.id_cartucho) " +
                "WHERE cr.id_rastreo = ?")) {
            pstmt.setLong(1, idTracking);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cartridgeNames.add(rs.getString("nombre"));
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los nombres de cartucho", WebAnalyzerDao.class, Logger.LOG_LEVEL_ERROR, e);
            return cartridgeNames;
        }
        return cartridgeNames;
    }

    /**
	 * Save broken links list.
	 *
	 * @param conn            the conn
	 * @param idCrawling      the id crawling
	 * @param brokenLinksList the broken links list
	 * @throws Exception the exception
	 */
    public static void saveBrokenLinksList(Connection conn, Long idCrawling, List<BrokenLinks> brokenLinksList) throws Exception {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO enlaces_rotos VALUES (?, ?, ?)")) {
            for (BrokenLinks brokenLinks : brokenLinksList) {
                pstmt.setLong(1, idCrawling);
                pstmt.setString(2, brokenLinks.getUrl());
                pstmt.setInt(3, brokenLinks.getNumBrokenLinks());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
	 * Save broken links.
	 *
	 * @param conn        the conn
	 * @param idCrawling  the id crawling
	 * @param brokenLinks the broken links
	 * @throws Exception the exception
	 */
    public static void saveBrokenLinks(Connection conn, Long idCrawling, BrokenLinks brokenLinks) throws Exception {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO enlaces_rotos VALUES (?, ?, ?)")) {
            pstmt.setLong(1, idCrawling);
            pstmt.setString(2, brokenLinks.getUrl());
            pstmt.setInt(3, brokenLinks.getNumBrokenLinks());

            pstmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
