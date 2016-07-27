package es.inteco.crawler.sexista.modules.commons.util;

import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.modules.commons.Constants.CommonsConstants;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase ConexionBBDD.
 *
 * @author GPM
 */
public class ConexionBBDDSistema {

    /**
     * Método conectar.
     * Conecta a la BD.
     *
     * @return Conexión a la BD
     * @throws SQLException           SQLException
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws FileNotFoundException  FileNotFoundException
     * @throws IOException            IOException
     */
    public static Connection conectar() throws SQLException, ClassNotFoundException,
            FileNotFoundException, IOException {

        Properties properties = ConfigUtil.getConfiguracion("lenox.properties");

        String usuario = properties.getProperty(
                CommonsConstants.USUARIO_BD_SISTEMA);
        String password = properties.getProperty(
                CommonsConstants.PASSWORD_BD_SISTEMA);
        String url = properties.getProperty(
                CommonsConstants.URL_BD_SISTEMA);

        Connection con = null;

        try {
            //Obtener el driver BBDD
            Class.forName(properties.getProperty(CommonsConstants.DRIVER_BD_SISTEMA));
            //Obtener la conexión a la BBDD
            con = DriverManager.getConnection(url, usuario, password);
        } catch (ClassNotFoundException | SQLException w) {
            Logger.getLogger(ConexionBBDD.class).error(w.getMessage());
            throw w;
        }

        return con;
    }

    /**
     * M&eacute;todo desconectar.
     *
     * @param con Conexión
     * @throws SQLException SQLException
     */
    public static void desconectar(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

}