package es.ctic.basicservice.historico;

/**
 * Clase para transferir la información de los resultados registrados del servicio de diagnóstico al cliente
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class BasicServiceResultado {

    private String id;
    private String date;

    public BasicServiceResultado() {
    }

    public BasicServiceResultado(final String id, final String date) {
        this.id = id;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Devuelve una cadena que representa al objeto en formato JSON
     *
     * @return una cadena que coincide con su representación en formato JSON
     */
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"date\":\"%s\"}", id, date);
    }
}
