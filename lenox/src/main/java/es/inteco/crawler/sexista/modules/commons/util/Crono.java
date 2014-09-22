package es.inteco.crawler.sexista.modules.commons.util;

import java.util.Date;

/**
 * Utilidad de contador de tiempo.
 */
public class Crono {

    /**
     * Campo t0 y t1.
     */
    private Date t0, t1;

    /**
     * Inicializamos el periodo de tiempo a medir.
     */
    public void inicializa() {
        t0 = new Date();
    }

    /**
     * Retornamos el valor de tiempo transcurrido.
     *
     * @return long - Milisegundo de tiempo
     */
    public long getTime() {
        t1 = new Date();
        return (t1.getTime() - t0.getTime());
    }

    /**
     * Retorna una cadena con el tiempo.
     *
     * @param milisegundos - Milisegundos que queremos transforma a horas
     * @return Cadena con el tiempo
     */
    public String millis2Hora(long milisegundos) {

        long hora, minuto, segundo;
        long restohora, restominuto, restosegundo;

        hora = milisegundos / 3600000;
        restohora = milisegundos % 3600000;

        minuto = restohora / 60000;
        restominuto = restohora % 60000;

        segundo = restominuto / 1000;
        restosegundo = restominuto % 1000;

        return hora + ":" + minuto + ":" + segundo + "." + restosegundo;
    }

}
