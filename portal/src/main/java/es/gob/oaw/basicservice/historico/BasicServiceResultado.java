package es.gob.oaw.basicservice.historico;

/**
 * Clase para transferir la información de los resultados registrados del servicio de diagnóstico al cliente.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class BasicServiceResultado {

    /** The id. */
    private String id;
    
    /** The date. */
    private String date;

    /**
	 * Instantiates a new basic service resultado.
	 */
    public BasicServiceResultado() {
    }

    /**
	 * Instantiates a new basic service resultado.
	 *
	 * @param id   the id
	 * @param date the date
	 */
    public BasicServiceResultado(final String id, final String date) {
        this.id = id;
        this.date = date;
    }

    /**
	 * Gets the date.
	 *
	 * @return the date
	 */
    public String getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(String date) {
        this.date = date;
    }

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(String id) {
        this.id = id;
    }


    /**
	 * Devuelve una cadena que representa al objeto en formato JSON.
	 *
	 * @return una cadena que coincide con su representación en formato JSON
	 */
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"date\":\"%s\"}", id, date);
    }
}
