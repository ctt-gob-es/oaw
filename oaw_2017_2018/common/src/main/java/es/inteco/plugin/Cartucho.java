package es.inteco.plugin;

import java.util.Map;

/**
 * @author a.mesas
 *         Esta clase define un cartucho generico, el cual únicamente va a tener
 *         un método que dado una url y el contenido de una página ejecutará
 *         El analisis correspondiente a dicho cartucho.
 */
public abstract class Cartucho {

    public abstract void analyzer(Map<String, Object> datos);

    public abstract void setConfig(long idRastreo);

}