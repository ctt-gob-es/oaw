package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class GeneradorGoogle {

    Set<String> hs = new HashSet<String>();

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

    public void extraeLinks(String partido) {

        String[] aver = partido.split("<");
        for (int i = 0; i < aver.length; i++) {
            if (aver[i].contains("class=l")) {
                hs.add(aver[i].substring(8).split("\"")[0]);
            }
        }

    }

}

