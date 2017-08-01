package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Set;

public final class GeneraRango {

    private GeneraRango() {
    }

    public static String getRango(int[] puertos, String ip1, String ip2, String listaUrls) {

        Generador hg = new Generador(ip1, ip2);
        Set<String> eo = hg.genera();

        String[] aver = eo.toArray(new String[eo.size()]);

        for (String anAver : aver) {
            for (int puerto : puertos) {
                listaUrls = urlsLis(anAver, listaUrls, puerto);
            }
        }
        return listaUrls;
    }

    public static String urlsLis(final String ip, String listaUrls, final int puerto) {;
        try {
            final InetAddress is = InetAddress.getByName(ip);
            final URL url = new URL("http://" + is.getCanonicalHostName() + ":" + puerto);
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(4000);
            urlConnection.connect();
            if (listaUrls.isEmpty()) {
                listaUrls = listaUrls + "http://" + is.getCanonicalHostName() + ":" + puerto;
            } else {
                listaUrls = listaUrls + ";http://" + is.getCanonicalHostName() + ":" + puerto;
            }
        } catch (UnknownHostException e) {
            Logger.putLog("EXCEPCION DE IP DESCONOCIDA (UnknownHostException) " + ip + ":" + puerto, GeneraRango.class, Logger.LOG_LEVEL_INFO);
        } catch (MalformedURLException e) {
            Logger.putLog("EXCEPCION DE URL MAL FORMADA " + ip + ":" + puerto, GeneraRango.class, Logger.LOG_LEVEL_INFO);
        } catch (IOException e) {
            Logger.putLog("EXCEPCION DE CONEXION " + ip + ":" + puerto, GeneraRango.class, Logger.LOG_LEVEL_INFO);
        }
        return listaUrls;
    }
}


