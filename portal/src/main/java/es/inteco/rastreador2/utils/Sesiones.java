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
package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;

import javax.servlet.http.HttpSession;


/**
 * The Class Sesiones.
 */
public final class Sesiones {

    /**
	 * Instantiates a new sesiones.
	 */
    private Sesiones() {
    }

    /**
	 * Comprobar session.
	 *
	 * @param session the session
	 * @return true, if successful
	 */
    public static boolean comprobarSession(HttpSession session) {
        return session != null && session.getAttribute(Constants.USER) != null;
    }


    /*
     * Return 0, es que no hay problemas, todo esta bien
     * Return 1, es que hay un user con nuestro Username
     * Return 2, es que hay un configurador para nuestro cartucho
     */
   /* public static int comprobarSesionesActivas(HttpSession session) {
        Connection c = null;

        PropertiesManager pmgr = new PropertiesManager();

        try {
            c = DataBaseManager.getConnection();
            //comprobamos si hay un usuario activo con nuestro username y que no seamos nosotros logicamente
            PreparedStatement pstmtone = c.prepareStatement("SELECT * FROM Sesiones_Activas where Usuario=? and Id_Sesion!=?");
            pstmtone.setString(1, (String) session.getAttribute("user"));
            pstmtone.setString(2, session.getId());
            ResultSet rs = pstmtone.executeQuery();
            if (rs.next()) {
                //nos vamos
                return 1;
            }

            PreparedStatement pstmt2 = c.prepareStatement("SELECT * FROM usuario u " +
                    "JOIN usuario_rol ur ON (u.usuario = ur.usuario) " +
                    "JOIN roles r ON (ur.id_rol = r.id_rol) WHERE u.usuario = ?");
            pstmt2.setString(1, (String) session.getAttribute("user"));
            ResultSet rs2 = pstmt2.executeQuery();
            int id_cartucho = -2;
            int id_rol = 0;
            if (rs2.next()) {
                if (!rs2.getString("id_rol").equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.configurator.id"))) {
                    //para el visualizador no se restringe
                    return 0;
                }
                id_cartucho = rs2.getInt("Id_Cartucho");
                id_rol = rs2.getInt("id_rol");
            }

            pstmt2 = c.prepareStatement("SELECT * FROM sesiones_activas s " +
                    "JOIN usuario u ON (u.usuario = s.usuario) " +
                    "JOIN usuario_rol ur ON (ur.usuario = u.usuario) " +
                    "JOIN roles r ON (r.id_rol = ur.id_rol) " +
                    "WHERE u.usuario != ? AND r.id_rol = ? AND u.id_cartucho = ?");
            pstmt2.setString(1, (String) session.getAttribute("user"));
            pstmt2.setInt(2, id_rol);
            pstmt2.setInt(3, id_cartucho);
            rs2 = pstmt2.executeQuery();
            if (rs2.next()) {
                return 2;
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion", Sesiones.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return 0;
    }*/

}


	


