package es.inteco.crawler.sexista.modules.analisis.filter.base;

import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;

import java.util.ArrayList;
import java.util.List;

public class BaseFilterExcepcion {

    private static final String[] EXCEP_DELANTERAS_FU = {"A", "A FIN DE QUE", "ACASO", "ANTE",
            "ANTES QUE", "ASÍ", "ASÍ QUE", "AUNQUE", "BAJO", "BASTANTE",
            "BASTANTES", "CABE", "CADA", "CATORCE", "CINCO", "COMO", "CON",
            "CON TAL QUE", "CONTRA", "CUALQUIER", "CUANDO", "CUATRO", "DE",
            "DESDE", "DIECINUEVE", "DIECIOCHO", "DIECISÉIS", "DIECISIETE",
            "DIEZ", "DOCE", "DOS", "E", "EN", "ENTRE", "EQUIPO", "ERES", "ES",
            "ESTÁ", "ESTADO", "ESTÁN", "ESTAREMOS", "FUE", "FUERON", "FUI",
            "FUIMOS", "FUISTE", "FUISTEIS", "FUTURO", "FUTUROS", "GRANDE",
            "GRANDES", "HACIA", "HASTA", "JAMÁS", "MAS", "MÁS", "MEJOR",
            "MENOS", "MI", "MIS", "NI", "NO", "NUEVE", "NUNCA", "O", "OCHO",
            "ONCE", "PARA", "PARA QUE", "PEOR", "PERO", "PERSONAL", "POR",
            "POR LO TANTO", "PORQUE ", "POSIBLEMENTE", "PROBABLEMENTE", "PUES",
            "PUESTO QUE", "QUE", "QUÉ", "QUINCE", "QUIZÁ", "QUIZÁS", "SEGÚN",
            "SEGURAMENTE", "SEIS", "SERÁ", "SERÁN", "SERÁS", "SERÉ", "SERÉIS",
            "SEREMOS", "SI", "SÍ", "SI BIEN", "SIDO", "SIEMPRE", "SIEMPRE QUE",
            "SIETE", "SIN", "SINO", "SINO QUE", "SO", "SOBRE", "SOIS",
            "SOLAMENTE", "SÓLO", "SOMOS", "SON", "SOY", "SU", "SUS", "TAL",
            "TAL COMO", "TAL VEZ", "TALES", "TAMPOCO", "TAN", "TANTO",
            "TANTO QUE", "TRAS", "TRECE", "TRES", "TU", "TUS", "U", "UNO",
            "VEINTE", "Y"};

    private static final String[] DETERMINANTES_FEMENINOS = new String[]{
            "LA", "LAS", "UNA", "UNAS", "NUESTRAS", "VUESTRAS", "ESAS",
            "ESTAS", "AQUELLAS", "NUESTRA", "VUESTRA", "ESA", "ESTA",
            "AQUELLA", "TODA", "TODAS", "SUPUESTAS", "SUPUESTA", "NINGUNAS",
            "NINGUNA", "ALGUNAS", "ALGUNA", "CUANTAS", "CUANTA", "MUCHA",
            "MUCHAS", "POCA", "POCAS", "CUANTA", "DEMASIADA"};

    private static final String[] DETERMINANTES_MASCULINOS = new String[]{
            "EL", "AL", "LOS", "DEL", "UN", "UNO", "UNOS", "NUESTROS",
            "VUESTROS", "ESOS", "ESTOS", "AQUELLOS", "NUESTRO", "VUESTRO",
            "ESE", "ESTE", "AQUEL", "TODO", "TODOS", "SUPUESTOS", "SUPUESTO",
            "NINGUNOS", "NINGUNO", "ALGUNOS", "ALGUNO", "CUANTOS", "CUANTO",
            "MUCHO", "MUCHOS", "POCO", "POCOS", "CUANTO", "DEMASIADO"};

    private List<String> excepDelanterasFU = null;
    private List<String> determinantesFemeninos = null;
    private List<String> determinantesMasculinos = null;

    private TerminosDao terminoDao;

    /**
     * Cargamos las excepciones delanteras
     *
     * @return List<String> lista de excepciones excepDelanterasFU
     */
    protected List<String> getExcepcionDelanterasFU() {

        if (null == this.excepDelanterasFU) {

            this.excepDelanterasFU = new ArrayList<>();

            for (String exc : EXCEP_DELANTERAS_FU) {

                // Eliminamos las tildes
                exc = UtilCalculateTerms.removeAccents(exc);

                if (!this.excepDelanterasFU.contains(exc)) {
                    this.excepDelanterasFU.add(exc);
                }
            }
        }

        return this.excepDelanterasFU;
    }

    /**
     * Recuperamos los determinantes femenino.
     *
     * @return List<String>
     */
    protected List<String> getDeterminantesFemeninos() {

        if (null == this.determinantesFemeninos) {

            this.determinantesFemeninos = new ArrayList<>();
            String detPlural = null;
            for (String det : DETERMINANTES_FEMENINOS) {
                det = UtilCalculateTerms.removeAccents(det);
                if (!this.determinantesFemeninos.contains(det)) {
                    this.determinantesFemeninos.add(det);
                }
                detPlural = UtilCalculateTerms.calculatePlural(det);
                if (!this.determinantesFemeninos.contains(detPlural)) {
                    this.determinantesFemeninos.add(detPlural);
                }
            }
        }
        return this.determinantesFemeninos;
    }

    /**
     * Recuperamos los determinantes masculinos.
     *
     * @return List<String>
     */
    protected List<String> getDeterminantesMasculinos() {

        if (null == this.determinantesMasculinos) {

            this.determinantesMasculinos = new ArrayList<>();
            String detPlural = null;
            for (String det : DETERMINANTES_MASCULINOS) {
                det = UtilCalculateTerms.removeAccents(det);
                if (!this.determinantesMasculinos.contains(det)) {
                    this.determinantesMasculinos.add(det);
                }
                detPlural = UtilCalculateTerms.calculatePlural(det);
                if (!this.determinantesMasculinos.contains(detPlural)) {
                    this.determinantesMasculinos.add(detPlural);
                }
            }
        }
        return this.determinantesMasculinos;
    }

    /**
     * Comprobamos si es una excepcion anterior.
     * <p/>
     * 1- Definimos parametros : String beforeWord; : int startPositionExcep =
     * -1;
     * <p/>
     * 2- PARA CADA, Recorremos el listado buscando si existe la excepcion : for
     * (String excepcion : lstBeforeException)
     * <p/>
     * 2.1- Inicializamos : beforeWord = ""; : startPositionExcep =
     * arrayPosition - excepcion.split(" ").length;
     * <p/>
     * 2.2- SI, Recupereramos los terminos posteriores si existen :
     * if(startPositionExcep >= 0)
     * <p/>
     * 2.2.1- Montamos el texto que sigue al termino a analizar : for (int index
     * = startPositionExcep; index < arrayPosition.intValue(); index++) { :
     * beforeWord = beforeWord.concat(lstTerminos.get(index)); : }
     * <p/>
     * 2.2.2- Recuperamos la palabra posterior en mayusculas y sin espacios :
     * beforeWord = beforeWord.trim().toUpperCase();
     * <p/>
     * 2.2.3- Si son iguales, habra excepcion :
     * if(beforeWord.equals(excepcion)){ : return true; : }
     * <p/>
     * FIN SI (del punto 2.2)
     * <p/>
     * FIN PARA CADA (del punto 2)
     * <p/>
     * 3- Retornamos false : return false;
     *
     * @param arrayPosition      - Posicion de la palabra a analizar
     * @param lstTerminos        - Listado con todas las palabras del texto
     * @param lstBeforeException - listado con las excepciones a tratar
     * @return Boolean - Respuesta (true/false)
     */
    private boolean isBeforeException(Integer arrayPosition,
                                      List<String> lstTerminos, List<String> lstBeforeException) {

        // Palabra anterior a la que estamos analizando
        String beforeWord;

        int startPositionExcep = -1;

        // Recorremos el listado buscando si existe la excepcion
        for (String excepcion : lstBeforeException) {

            // Inicializamos las palabras anteriores
            beforeWord = "";

            startPositionExcep = arrayPosition - excepcion.split(" ").length;

            // Recupereramos los terminos posteriores si existen
            if (startPositionExcep >= 0) {

                // Montamos el texto que sigue al termino a analizar
                for (int index = startPositionExcep; index < arrayPosition; index++) {
                    beforeWord = beforeWord.concat(lstTerminos.get(index));
                }

                // Recuperamos la palabra posterior en mayusculas y sin espacios
                beforeWord = beforeWord.trim().toUpperCase();

                // Si son iguales, habra excepcion
                if (beforeWord.equals(excepcion)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Comprobamos si es una excepcion posterior.
     * <p/>
     * 1- Definimos parametros : String afterWord; : int endPositionExcep = -1;
     * <p/>
     * 2- PARA CADA, recorremos el listado buscando si existe la excepcion : for
     * (String excepcion : lstAfterException)
     * <p/>
     * 2.1- Inicializamos : afterWord = ""; : endPositionExcep = arrayPosition +
     * excepcion.split(" ").length;
     * <p/>
     * 2.2- SI, recupereramos los terminos posteriores si existen :
     * if(lstTerminos.size() > endPositionExcep)
     * <p/>
     * 2.2.1- Montamos el texto que sigue al termino a analizar : for (int index
     * = arrayPosition + 1; index <= endPositionExcep; index++) { : afterWord =
     * afterWord.concat(lstTerminos.get(index)); : }
     * <p/>
     * 2.2.2- Recuperamos la palabra posterior en mayusculas y sin espacios :
     * afterWord = afterWord.trim().toUpperCase();
     * <p/>
     * 2.2.3- Si son iguales, habra excepcion : if(afterWord.equals(excepcion)){
     * : return true; : }
     * <p/>
     * FIN SI (del punto 2.2)
     * <p/>
     * FIN PARA CADA (del punto 2)
     * <p/>
     * 3- Retornamos false : return false;
     *
     * @param arrayPosition     - Posicion de la palabra a analizar
     * @param lstTerminos       - Listado con todas las palabras del texto
     * @param lstAfterException - listado con las excepciones a tratar
     * @return Boolean - Respuesta (true/false)
     */
    private boolean isAfterException(Integer arrayPosition,
                                     List<String> lstTerminos, List<String> lstAfterException) {

        // Palabra posterior a la que estamos analizando
        String afterWord;

        int endPositionExcep = -1;

        // Recorremos el listado buscando si existe la excepcion
        for (String excepcion : lstAfterException) {

            // Inicializamos las palabras posteriores
            afterWord = "";

            endPositionExcep = arrayPosition + excepcion.split(" ").length;

            // Recupereramos los terminos posteriores si existen
            if (lstTerminos.size() > endPositionExcep) {

                // Montamos el texto que sigue al termino a analizar
                for (int index = arrayPosition + 1; index <= endPositionExcep; index++) {
                    afterWord = afterWord.concat(lstTerminos.get(index));
                }

                // Recuperamos la palabra posterior en mayusculas y sin espacios
                afterWord = afterWord.trim().toUpperCase();

                // Si son iguales, habra excepcion
                if (afterWord.equals(excepcion)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Comprueba el array de posiciones, busca excepciones anteriores y
     * posteriores y modifica el array de posiciones.
     *
     * @param terminoPositionDto TerminoPositionDto
     * @param lstTerminos        ArrayList
     * @param lstBeforeExcepcion ArrayList
     * @param lstAfterExcepcion  ArrayList
     */
    protected void checkPositionsArray(TerminoPositionDto terminoPositionDto,
                                       List<String> lstTerminos, List<String> lstBeforeExcepcion,
                                       List<String> lstAfterExcepcion) {

        boolean isBeforeException = false;
        boolean isAfterException = false;
        // Recuperamos el array de posiciones para el que trataremos cada
        // palabra
        List<Integer> positions = terminoPositionDto.getPositions();
        List<Integer> auxPositions = new ArrayList<>();

        // Revisamos posición a posición
        for (Integer arrayPosition : positions) {

            // Comprobamos excepciones
            isBeforeException = isBeforeException(arrayPosition, lstTerminos,
                    lstBeforeExcepcion);
            isAfterException = isAfterException(arrayPosition, lstTerminos,
                    lstAfterExcepcion);

            // Si no es excepcion(ni anterior ni posterior) lo añadimos al nuevo
            // array de posiciones
            if (!isBeforeException && !isAfterException) {
                auxPositions.add(arrayPosition);
            }
        }

        // Cambiamos el array de posiciones terminos sexistas
        terminoPositionDto.setPositions(auxPositions);
    }

    /**
     * @return the terminoDao
     */
    public TerminosDao getTerminoDao() {
        if (null == terminoDao) {
            this.terminoDao = new TerminosDao();
        }
        return terminoDao;
    }

    /**
     * @param terminoDao the terminoDao to set
     */
    public void setTerminoDao(TerminosDao terminoDao) {
        this.terminoDao = terminoDao;
    }
}
