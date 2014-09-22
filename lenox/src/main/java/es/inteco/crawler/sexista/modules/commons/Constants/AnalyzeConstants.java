package es.inteco.crawler.sexista.modules.commons.Constants;

/**
 * Clase AnalyzeConstants.
 *
 * @author jbernal
 */
public class AnalyzeConstants {

    /**
     * Control de debug.
     */
    public static final boolean DEBUG = true;

    /**
     * Letras validas.
     */
    public static final String VALID_LETTERS = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789\u008A\u008E\u009A\u009C\u009E\u009FÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ";


    /**
     * Define el rango de terminos que alzancamos a la hora de recuperar en contexto de un termino localizado en el texto.
     */
    public static final int RANGO_CONTEXTO = 12;

    /**
     * Constante CLAVE_CRAWLER_ID_RASTREO.
     */
    public static final String CLAVE_CRAWLER_ID_RASTREO = "id_rastreo";

    /**
     * Constante CLAVE_CRAWLER_ID_RASTREO_REALIZADO.
     */
    public static final String CLAVE_CRAWLER_ID_RASTREO_REALIZADO = "idFulfilledCrawling";

    /**
     * Constante CLAVE_CRAWLER_FECHA_HORA.
     */
    public static final String CLAVE_CRAWLER_FECHA_HORA = "fecha_hora";

    /**
     * Constante CLAVE_CRAWLER_URL.
     */
    public static final String CLAVE_CRAWLER_URL = "url";

    /**
     * Constante CLAVE_CRAWLER_CONTENIDO.
     */
    public static final String CLAVE_CRAWLER_CONTENIDO = "contenido";

    /**
     * Constante CLAVE_CRAWLER_USUARIO.
     */
    public static final String CLAVE_CRAWLER_USUARIO = "user";

    /**
     * Caracteres de excepciones de terminación.
     */
    public static final String CHARACTER_GENERIC_FINAL = "-\\/";

    /**
     * Constante de terminación de excepcion "A".
     */
    public static final String TERM_A = "A";

    /**
     * Constante de terminación de excepcion "O".
     */
    public static final String TERM_O = "O";

    /**
     * Constante de terminación de excepcion "AS".
     */
    public static final String TERM_AS = "AS";

    /**
     * Constante de terminación de excepcion "OS".
     */
    public static final String TERM_OS = "OS";

    /**
     * Posicion anterior de excepciones, definido por BBDD.
     */
    public static final int WORD_EXCEPTION_BEFORE_POSITION = 0;

    /**
     * Posicion posterio de excepciones, definido por BBDD.
     */
    public static final int WORD_EXCEPTION_AFTER_POSITION = 1;

    public static final String DEFAULT_ENCODING = "UTF-8";

}
