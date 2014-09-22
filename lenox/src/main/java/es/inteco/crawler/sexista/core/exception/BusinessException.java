package es.inteco.crawler.sexista.core.exception;

/**
 * Clase BusinessException.
 * Excepción de negocio genérica.
 *
 * @author GPM
 */
public class BusinessException extends Exception {

    /**
     * Campo serialVersionUID.
     */
    private static final long serialVersionUID = 1687472286070121597L;

    /**
     * Constructor.
     */
    public BusinessException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message Mensaje de error
     * @param cause   Causa del Error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Mensaje de error
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause Causa de error
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

}
