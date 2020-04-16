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
package es.inteco.rastreador2.export.ddbb;

import es.inteco.plugin.dao.DataBaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportObservatoryDDBB {

    static String observatoryId = "8, 18";

    public static void main(String[] args) {

        Connection c = null;
        Connection conn = null;
        FileWriter bw = null;
        try {

            c = conectarSistemaDDBB();
            conn = conectarIntavDDBB();

            File file = new File("C:\\Programas\\observatoryDDBB.sql");
            if (file.exists()) {
                file.delete();
                file = new File("C:\\Programas\\observatoryDDBB.sql");
            }
            bw = new FileWriter(file, true);

            createTables(bw);
            bw.flush();

            List<String> fulfilledCIds = addObservatoryEntries(c, bw);
            bw.flush();
            addIntavEntries(conn, bw, fulfilledCIds);
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al exportar la BBDD");
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el bw");
            }
        }
    }

    private static Connection conectarSistemaDDBB() throws SQLException, ClassNotFoundException {
        //Obtenemos el Driver de la BBDD
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Obtenemos conexion a la BBDD
        Connection conn = DriverManager.getConnection("jdbc:mysql://172.23.52.30:3306/produccion_sistema", "root", "admin");

        return conn;
    }

    private static Connection conectarIntavDDBB() throws SQLException, ClassNotFoundException {
        //Obtenemos el Driver de la BBDD
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Obtenemos conexion a la BBDD
        Connection conn = DriverManager.getConnection("jdbc:mysql://172.23.52.30:3306/produccion_intav", "root", "admin");

        return conn;
    }

    private static Connection conectarMultilanguageDDBB() throws SQLException, ClassNotFoundException {
        //Obtenemos el Driver de la BBDD
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Obtenemos conexion a la BBDD
        Connection conn = DriverManager.getConnection("jdbc:mysql://172.23.52.30:3306/produccion_multilanguage", "root", "admin");

        return conn;
    }

    private static void createTables(FileWriter bw) throws IOException {

        bw.write("CREATE TABLE observatorio (id_observatorio bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "nombre varchar(100) NOT NULL,profundidad int NOT NULL DEFAULT 0," +
                "amplitud int NOT NULL DEFAULT 0,fecha_inicio timestamp NOT NULL, id_guideline bigint) " +
                "ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE observatorios_realizados (id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "id_observatorio bigint NOT NULL,fecha timestamp NOT NULL," +
                "FOREIGN KEY (id_observatorio) REFERENCES observatorio(id_observatorio) ON DELETE CASCADE) " +
                "ENGINE = InnoDB; ");
        bw.write("\n");

        bw.write("CREATE TABLE categorias_lista (id_categoria bigint NOT NULL AUTO_INCREMENT," +
                "nombre varchar(50) NOT NULL,PRIMARY KEY (id_categoria)) ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE lista (id_lista bigint(20) NOT NULL auto_increment," +
                "nombre varchar(255) NOT NULL,lista text NOT NULL," +
                "id_categoria bigint(20) default NULL, acronimo varchar(25) default NULL," +
                " PRIMARY KEY (id_lista), " +
                "KEY id_categoria (id_categoria)," +
                "FOREIGN KEY (id_categoria) REFERENCES categorias_lista (id_categoria) ON DELETE CASCADE) " +
                "ENGINE=InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE observatorio_categoria (id_observatorio bigint NOT NULL," +
                "id_categoria bigint NOT NULL,PRIMARY KEY (id_observatorio, id_categoria)," +
                "FOREIGN KEY (id_observatorio) REFERENCES observatorio(id_observatorio) ON DELETE CASCADE," +
                "FOREIGN KEY (id_categoria) REFERENCES categorias_lista (id_categoria) ON DELETE CASCADE) " +
                "ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE rastreo (id_rastreo bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "nombre_rastreo varchar(255) NOT NULL,fecha timestamp NOT NULL," +
                "profundidad int NOT NULL,topn int NOT NULL," +
                "semillas bigint NOT NULL,fecha_lanzado timestamp, " +
                "id_observatorio bigint,id_guideline bigint," +
                "FOREIGN KEY (id_observatorio) REFERENCES observatorio (id_observatorio) ON DELETE CASCADE," +
                "FOREIGN KEY (semillas) REFERENCES lista (id_lista) ON DELETE CASCADE) " +
                "ENGINE = InnoDB; ");
        bw.write("\n");

        bw.write("CREATE TABLE rastreos_realizados (id bigint(20) NOT NULL auto_increment, " +
                "id_rastreo bigint(20) NOT NULL, fecha timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP," +
                "id_obs_realizado bigint(20) default NULL, id_lista bigint(20), PRIMARY KEY  (id), KEY id_rastreo (id_rastreo)," +
                "KEY id_obs_realizado (id_obs_realizado), KEY id_lista (id_lista)," +
                "FOREIGN KEY (id_rastreo) REFERENCES rastreo (id_rastreo) ON DELETE CASCADE," +
                "FOREIGN KEY (id_obs_realizado) REFERENCES observatorios_realizados (id) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_lista) REFERENCES lista (id_lista) ON DELETE CASCADE " +
                ") ENGINE=InnoDB; ");
        bw.write("\n");

        bw.write("CREATE TABLE tguidelines (cod_guideline bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "des_guideline varchar(50) NOT NULL) ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE tanalisis (cod_analisis bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "fec_analisis timestamp NOT NULL,cod_url varchar(500),num_duracion bigint," +
                "nom_entidad varchar(100) NOT NULL,cod_rastreo bigint NOT NULL,cod_guideline bigint NOT NULL," +
                "checks_ejecutados text," +
                "FOREIGN KEY (cod_guideline) REFERENCES tguidelines (cod_guideline) ON DELETE CASCADE) " +
                "ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("CREATE TABLE tincidencia (cod_incidencia bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "cod_comprobacion bigint NOT NULL,cod_analisis bigint NOT NULL,cod_linea_fuente bigint NOT NULL," +
                "cod_columna_fuente bigint NOT NULL,des_fuente text," +
                "FOREIGN KEY (cod_analisis) REFERENCES tanalisis (cod_analisis) ON DELETE CASCADE) " +
                "ENGINE = InnoDB;");
        bw.write("\n");

        bw.write("\n");
    }

    private static List<String> addObservatoryEntries(Connection c, FileWriter bw) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> observatoriesId = new ArrayList<String>();
        List<String> crawlersId = new ArrayList<String>();
        List<String> fulfilledCrawlersId = new ArrayList<String>();

        //Cargamos los observatorios
        ps = c.prepareStatement("SELECT * FROM observatorio WHERE id_observatorio IN (" + observatoryId + ");");
        rs = ps.executeQuery();
        while (rs.next()) {
            observatoriesId.add(rs.getString("id_observatorio"));
            String query = "INSERT INTO observatorio VALUES(" + rs.getString("id_observatorio") + ",'" + replaceSpecialChars(rs.getString("nombre")) + "'," + rs.getString("profundidad") + ","
                    + rs.getString("amplitud") + ",'" + rs.getString("fecha_inicio") + "'," + rs.getString("id_guideline") + ");";
            bw.write(query + "\n");
        }
        bw.write("\n");

        //Cargamos los observatorios realizados
        ps = c.prepareStatement("SELECT * FROM observatorios_realizados WHERE id_observatorio IN (" + observatoryId + ");");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO observatorios_realizados VALUES(" + rs.getString("id") + "," + rs.getString("id_observatorio") + ",'"
                    + rs.getString("fecha") + "');";
            bw.write(query + "\n");
        }
        bw.write("\n");

        ps = c.prepareStatement("SELECT * FROM categorias_lista c JOIN observatorio_categoria oc ON (c.id_categoria = oc.id_categoria) WHERE id_observatorio IN (" + observatoryId + ");");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO categorias_lista VALUES(" + rs.getString("id_categoria") + ",'" + replaceSpecialChars(rs.getString("nombre")) + "');";
            bw.write(query + "\n");
        }
        bw.write("\n");

        ps = c.prepareStatement("SELECT * FROM lista l JOIN observatorio_categoria oc ON (l.id_categoria = oc.id_categoria) WHERE id_observatorio IN (" + observatoryId + ");");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO lista VALUES(" + rs.getString("id_lista") + ",'" + replaceSpecialChars(rs.getString("nombre")) + "','"
                    + rs.getString("lista") + "'," + rs.getString("id_categoria");
            if (rs.getString("acronimo") != null) {
                query += ",'" + rs.getString("acronimo") + "');";
            } else {
                query += "," + rs.getString("acronimo") + ");";
            }

            bw.write(query + "\n");
        }
        bw.write("\n");

        ps = c.prepareStatement("SELECT * FROM observatorio_categoria WHERE id_observatorio IN (" + observatoryId + ");");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO observatorio_categoria VALUES(" + rs.getString("id_observatorio") + "," + rs.getString("id_categoria") + ");";
            bw.write(query + "\n");
        }
        bw.write("\n");

        //Cargamos los rastreos de cada observatorio
        String observatoriesStr = " IN (" + observatoriesId.get(0);
        for (int i = 1; i < observatoriesId.size(); i++) {
            observatoriesStr += "," + observatoriesId.get(i);
        }
        observatoriesStr += ")";

        ps = c.prepareStatement("SELECT * FROM rastreo WHERE id_observatorio " + observatoriesStr + ";");
        rs = ps.executeQuery();
        while (rs.next()) {
            crawlersId.add(rs.getString("id_rastreo"));
            String query = "INSERT INTO rastreo VALUES(" + rs.getString("id_rastreo") + ",'" + replaceSpecialChars(rs.getString("nombre_rastreo")) + "','"
                    + rs.getString("fecha") + "'," + rs.getString("profundidad") + "," + rs.getString("topn") + "," + rs.getString("semillas");
            if (rs.getString("fecha_lanzado") != null) {
                query += ",'" + rs.getString("fecha_lanzado") + "',";
            } else {
                query += "," + rs.getString("fecha_lanzado") + ",";
            }
            query += rs.getString("id_observatorio") + "," + rs.getString("id_guideline") + ");";
            bw.write(query + "\n");
        }
        bw.write("\n");

        //Cargamos los rastreos realizados
        String rastreosStr = " IN (" + crawlersId.get(0);
        for (int i = 1; i < crawlersId.size(); i++) {
            rastreosStr += "," + crawlersId.get(i);
        }
        rastreosStr += ")";

        ps = c.prepareStatement("SELECT * FROM rastreos_realizados WHERE id_rastreo " + rastreosStr + ";");
        rs = ps.executeQuery();
        while (rs.next()) {
            fulfilledCrawlersId.add(rs.getString("id"));
            String query = "INSERT INTO rastreos_realizados VALUES(" + rs.getString("id") + "," + rs.getString("id_rastreo") + ",'"
                    + rs.getString("fecha") + "'," + rs.getString("id_obs_realizado") + "," + rs.getString("id_lista") + ");";
            bw.write(query + "\n");
        }
        bw.write("\n");


        return fulfilledCrawlersId;
    }

    private static void addIntavEntries(Connection c, FileWriter bw, List<String> fulfilledCIds) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> analysisId = new ArrayList<String>();

        ps = c.prepareStatement("SELECT * FROM tguidelines;");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO tguidelines VALUES(" + rs.getString("cod_guideline") + ",'" + rs.getString("des_guideline") + "');";
            bw.write(query + "\n");
        }
        bw.write("\n");

        String analisisStr = " IN (" + fulfilledCIds.get(0);
        for (int i = 1; i < fulfilledCIds.size(); i++) {
            analisisStr += "," + fulfilledCIds.get(i);
        }
        analisisStr += ")";

        ps = c.prepareStatement("SELECT * FROM tanalisis WHERE cod_rastreo " + analisisStr + ";");
        rs = ps.executeQuery();
        while (rs.next()) {
            analysisId.add(rs.getString("cod_analisis"));
            String query = "INSERT INTO tanalisis VALUES(" + rs.getString("cod_analisis") + ",'" + rs.getString("fec_analisis") + "','"
                    + rs.getString("cod_url") + "'," + rs.getString("num_duracion") + ",'" + replaceSpecialChars(rs.getString("nom_entidad")) + "',"
                    + rs.getString("cod_rastreo") + "," + rs.getString("cod_guideline");
            if (rs.getString("checks_ejecutados") != null) {
                query += ",'" + rs.getString("checks_ejecutados") + "'";
            } else {
                query += "," + rs.getString("checks_ejecutados");
            }
            query += ");";
            bw.write(query + "\n");
        }
        bw.write("\n");

        String incidenciasStr = " IN (" + analysisId.get(0);
        for (int i = 1; i < analysisId.size(); i++) {
            incidenciasStr += "," + analysisId.get(i);
        }
        incidenciasStr += ")";
        ps = c.prepareStatement("SELECT * FROM tincidencia WHERE cod_analisis " + incidenciasStr + ";");
        rs = ps.executeQuery();
        while (rs.next()) {
            String query = "INSERT INTO tincidencia VALUES(" + rs.getString("cod_incidencia") + "," + rs.getString("cod_comprobacion") + ","
                    + rs.getString("cod_analisis") + "," + rs.getString("cod_linea_fuente") + "," + rs.getString("cod_columna_fuente");
            if (rs.getString("des_fuente") != null) {
                query += ",'" + replaceSpecialChars(rs.getString("des_fuente")) + "');";
            } else {
                query += "," + rs.getString("des_fuente") + ");";
            }
            bw.write(query + "\n");
        }
        bw.write("\n");
    }

    private static String replaceSpecialChars(String cad) {
        return cad.replace("\\", "\\\\").replace("\'", "\\\'").replace("\"", "\\\"");
    }

}
