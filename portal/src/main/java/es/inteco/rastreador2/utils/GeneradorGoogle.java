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

import es.inteco.common.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class GeneradorGoogle.
 */
public class GeneradorGoogle {

    /** The hs. */
    Set<String> hs = new HashSet<>();

    /**
	 * Busca.
	 *
	 * @param query  the query
	 * @param pagina the pagina
	 * @return the sets the
	 */
    public Set<String> busca(String query, int pagina) {

        Socket s = null;
        PrintStream p = null;
        InputStreamReader in = null;

        try {
            s = new Socket("google.es", 80);
            p = new PrintStream(s.getOutputStream());
            p.print("GET /search?q=" + query + "&hl=es&start=" + (pagina * 10) + "&sa=N HTTP/1.0\r\n");
            p.print("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1) Gecko/20061010 Firefox/2.0\r\n");
            p.print("Connection: close\r\n\r\n");

            in = new InputStreamReader(s.getInputStream());

            BufferedReader buffer = new BufferedReader(in);
            String line;

            while ((line = buffer.readLine()) != null) {
                this.extraeLinks(line);
            }
            in.close();
        } catch (IOException e) {
            Logger.putLog("Error al cerrar conexión con Google", this.getClass(), Logger.LOG_LEVEL_INFO);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.putLog("Error al cerrar conexión con Google", this.getClass(), Logger.LOG_LEVEL_INFO);
                }
            }
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    Logger.putLog("Error al cerrar conexión con Google", this.getClass(), Logger.LOG_LEVEL_INFO);
                }
            }
            if (p != null) {
                p.close();
            }
        }
        return hs;
    }

    /**
	 * Extrae links.
	 *
	 * @param partido the partido
	 */
    public void extraeLinks(String partido) {

        String[] aver = partido.split("<");
        for (int i = 0; i < aver.length; i++) {
            if (aver[i].contains("class=l")) {
                hs.add(aver[i].substring(8).split("\"")[0]);
            }
        }

    }

}

