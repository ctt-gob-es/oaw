package es.inteco.crawler.sexista.modules.commons.util;

import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidad de procesamiento de texto.
 */
public class UtilProcessingText {

    /**
     * Partimos un texto separando las distintas palabras (y caracteres) que
     * aparecen.
     *
     * @param texto                 - Texto a partir
     * @param lstTerminosCompuestos - listado de terminos compuestos
     * @return ArrayList con el contenido del texto separado por palabras
     */
    public static ArrayList<String> splitText(String texto,
                                              ArrayList<TerminoDto> lstTerminosCompuestos) {
        // Variable local con el contenido total del texto
        String text = texto;

        // Sustituimos las bells y tabulaciones por nuevas lineas.
        byte bell = 7;
        text = text.replace((char) bell, '\n');
        byte tabulador = 9;
        text = text.replace((char) tabulador, '\n');

        // Eliminamos los espacios de los extremos
        text = text.trim();

        String[] words = splitSpaces(text, lstTerminosCompuestos);
        String word;

        Object[] secondWords;
        String secondWord;

        ArrayList<String> col = new ArrayList<String>();

        // Separamos los caracteres especiales
        for (int i = 0; i < words.length; i++) {

            word = words[i];

            // Validamos que no sea null
            if (null != word) {
                secondWords = splitSpecialCharacters(word);

                for (int j = 0; j < secondWords.length; j++) {
                    secondWord = (String) secondWords[j];
                    col.add(secondWord);

                }
                if (secondWords.length > 0) {
                    col.set(col.size() - 1, ((String) col.get(col.size() - 1))
                            .concat(" "));
                }
            }
        }

        // Repasamos el listado por si hubiese excepciones de terminación.
        filterTerminalException(col);

        // Retornamos el texto troceado
        return col;
    }

    /**
     * Repasamos el array generado para localizar posibles terminos con
     * excepcion de terminación final.
     *
     * @param col - Listado de terminos del texto a analizar
     */
    private static void filterTerminalException(ArrayList<String> col) {

        String specialChar;
        String firstTerm;
        String thirdTerm;
        String termination;

        for (int i = 0; i < col.size(); i++) {

            // Recuperamos el termino
            specialChar = col.get(i);

            // Comprobamos si es un caracter generico final y si existe algun
            // termino que lo sigue en la cadena
            if (specialChar.length() == 1
                    && isCharGenericFinal(specialChar.charAt(0))
                    && col.size() > (i + 1)) {

                // Recuperamos el siguiente termino
                termination = col.get(i + 1);

                // Si el termino es una terminacion final (Comparamos en
                // mayusculas y sin espacios)
                if (termination.toUpperCase().trim().equals(
                        AnalyzeConstants.TERM_A)
                        || termination.toUpperCase().trim().equals(
                        AnalyzeConstants.TERM_O)
                        || termination.toUpperCase().trim().equals(
                        AnalyzeConstants.TERM_AS)
                        || termination.toUpperCase().trim().equals(
                        AnalyzeConstants.TERM_OS)) {

                    // Concatenamos los valores en la posición anterior a la del
                    // specialChar
                    firstTerm = col.get(i - 1);
                    thirdTerm = col.get(i + 1);

                    col.set(i - 1, firstTerm + specialChar + thirdTerm);

                    // Y removemos las posiciones en las que estaban los valores
                    // concatenados
                    col.remove(i);
                    col.remove(i);
                }
            }
        }
    }

    /**
     * Agrupamos los distintos terminos (en mayusculas), indicando las
     * posiciones donde aparecen en el array que pasamos como parametro.
     *
     * @param listTerms - Listado de palabras que aparecen en el texto
     * @return ArrayList - De termPositionDto, con los distintos terminos y las
     *         posiciones en las que aparecen
     */
    public static ArrayList<TerminoPositionDto> groupingTerms(
            ArrayList<String> listTerms) {
        // Termino a examinar
        String term = "";

        // Termino auxiliar en el listado de agrupacion de terminos
        String groupTerm = "";

        // Posicion del termino si se encuentra en el listado de grupos
        int controlPosition;

        TerminoPositionDto terminoPositionDto;

        ArrayList<TerminoPositionDto> listGroupTerms = new ArrayList<TerminoPositionDto>();

        // 1- Recorremos el listado de terminos
        for (int index = 0; index < listTerms.size(); index++) {

            // Inicializamos el control de posicion
            controlPosition = -1;

            // Recuperamos el termino del listado general
            term = (String) listTerms.get(index);

            // Recorremos el listado de terminos agrupados
            for (int jndex = 0; jndex < listGroupTerms.size(); jndex++) {

                // Recuperamos el termino del listado agrupado
                groupTerm = (String) listGroupTerms.get(jndex).getTermino();

                if (groupTerm.trim().equalsIgnoreCase(term.trim())) {
                    controlPosition = jndex;
                    jndex = listGroupTerms.size();
                }
            }

            // Creamos o añadimos una nueva posición dependiendo de la situacion
            if (controlPosition < 0) {

                // Si es menor que cero, será un termino nuevo
                ArrayList<Integer> posiciones = new ArrayList<Integer>();
                posiciones.add(Integer.valueOf(index));

                terminoPositionDto = new TerminoPositionDto();
                terminoPositionDto.setTermino(term.trim().toUpperCase());
                terminoPositionDto.setPositions(posiciones);

                listGroupTerms.add(terminoPositionDto);

            } else {
                // Sino añadimos una posicion al objeto
                terminoPositionDto = listGroupTerms.get(controlPosition);
                terminoPositionDto.getPositions().add(Integer.valueOf(index));
            }
        }

        return listGroupTerms;
    }

    /**
     * Cortamos un texto en palabras, separando por espacios y teniendo en
     * cuenta los terminos compuestos.
     *
     * @param text                  - texto a procesar
     * @param lstTerminosCompuestos - Lista de términos compuestos
     * @return String[]
     */
    private static String[] splitSpaces(String text,
                                        ArrayList<TerminoDto> lstTerminosCompuestos) {

        String compuesto;
        String textoAnterior = "";
        String textoPosterior = "";
        ArrayList<Integer> listaPosiciones;
        HashMap<String, String> hmSustitucion = new HashMap<String, String>();

        int posicionComp = -1;
        int posicion = -1;

		/*
         * Definimos los caracteres: - [0]-> Salto de linea - [1]-> Espacio
		 */

        // Eliminamos los espacios por delante y detras del texto
        text = text.trim();

        // Reemplazamos los saltos de línea por espacios
        text = text.replace("\n", " ").replace("\r", " ");

        String[] auxArrayText = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < auxArrayText.length; i++) {
            sb.append(UtilCalculateTerms.calculateSingular(auxArrayText[i]));
            if (i != auxArrayText.length - 1) {
                sb.append(String.valueOf(" "));
            }
        }

        String auxText = sb.toString();

        // Recorremos el listado de terminos compuestos
        for (int index = 0; index < lstTerminosCompuestos.size(); index++) {

            // Recuperamos el termino compuesto
            compuesto = lstTerminosCompuestos.get(index).getTermino();

            // Comprobamos si aparece en el texto
            if (matchs(text, compuesto) || matchs(auxText, compuesto)) {
                // Establecemos la posicion inicial de busqueda
                posicion = -1;

                // Si existe, recuperamos las posiciones donde aparece
                listaPosiciones = obtenerPosiciones(text, auxText, compuesto,
                        posicion);

                // Sustituimos valores por variables del tipo &%[n], para
                // sustituilas posteriormente
                for (int j = listaPosiciones.size(); j > 0; j = j - 2) {
                    // Recuperamos la ultima posicion donde aparece en el texto
                    posicionComp = listaPosiciones.get(j - 2);

                    // Recuperamos el texto por delante y por detras del termino
                    if (posicionComp < text.length()) {
                        textoAnterior = text.substring(0, posicionComp);
                    }
                    if (listaPosiciones.get(j - 1) - 1 <= text.length()) {
                        textoPosterior = text.substring(listaPosiciones.get(j - 1) - 1);
                    }

                    // Concatenamos todas las partes
                    text = textoAnterior + "&#[" + index + "]" + textoPosterior;

                    // Añadimos el valor en una hashmap
                    hmSustitucion.put("&#[" + index + "]", compuesto);
                }
            }
        }

        // Partimos por los espacios
        String[] resultado = text.split(" ");

        // Sustituimos valores compuestos si los hubiese
        for (int i = 0; i < resultado.length; i++) {
            String temporal = resultado[i];

            // Buscamos las claces de sustitucion
            if (null != hmSustitucion && temporal.length() > 3
                    && temporal.substring(0, 3).equals("&#[")) {

                // Intercambiamos la clave por su valor almacenado en el HashMap
                resultado[i] = hmSustitucion.get(resultado[i]);
            }
        }

        // Partimos por los espacios
        return resultado;
    }

    private static boolean matchs(String text, String compuesto) {
        String regexp = "[^\\w]" + compuesto + "[^\\w]";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * Obtenemos las posiciones donde se encuentra el termino.
     *
     * @param text      texto
     * @param compuesta compuesta
     * @param fromIndex fromIndex
     * @return ArrayList con las posiciones
     */
    private static ArrayList<Integer> obtenerPosiciones(String text,
                                                        String auxText, String compuesta, int fromIndex) {

		/*
		 * Definimos los caracteres: - [0]-> Salto de linea - [1]-> Espacio
		 */
        char[] caracteres = "\n ".toCharArray();

        // Separamos el termino compuesto en palabras
        String[] terminosCompuesta = compuesta.split(String
                .valueOf(caracteres[1]));
        // Pasamos las palabras a singular
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terminosCompuesta.length; i++) {
            sb.append(UtilCalculateTerms
                    .calculateSingular(terminosCompuesta[i]));
            if (i != terminosCompuesta.length - 1) {
                sb.append(String.valueOf(caracteres[1]));
            }
        }

        // Separamos el texto original en palabras para la busqueda
        String[] terminosText = text.split(String.valueOf(caracteres[1]));

        // Buscamos la posición(en palabras) del termino compuesto en singular
        // en el texto en singular
        ArrayList<Integer> posicionCompuesta = new ArrayList<Integer>();
        int numCaracteres = 0;
        while (auxText.toUpperCase().indexOf(sb.toString().toUpperCase()) != -1) {
            String auxPrevText = auxText.substring(0, auxText.toUpperCase()
                    .indexOf(sb.toString().toUpperCase()));
            String auxNextText = auxText.substring(auxText.toUpperCase()
                    .indexOf(sb.toString().toUpperCase())
                    + sb.toString().length());
            // Detectaremos el termino compuesto cuando la cadena siguiente sea
            // vacía o empiece por espacio
            // (si fuera otro caracter, entenderíamos que es la terminación y
            // nuestro termino no es el mismo).
            if ("".equals(auxNextText)
                    || (!"".equals(auxNextText) && auxNextText
                    .startsWith(String.valueOf(caracteres[1])))) {
                int pos = 0;
                //Detectamos el termino en la cadena en singular
                // Y calculamos el numero de caracter donde empieza en la cadena original
                if (auxPrevText != null && !"".equals(auxPrevText)) {
                    pos = auxPrevText.split(String.valueOf(caracteres[1])).length;
                    while (pos > 0 && pos < terminosText.length) {
                        pos--;
                        numCaracteres += terminosText[pos].length() + 1;// Añadimos
                        // 1 por
                        // el
                        // espacio
                    }

                }
                posicionCompuesta.add(numCaracteres);
                int i = 0;
                //Calculamos también el caracter en el que termina
                if (!"".equals(auxPrevText)) {
                    pos = auxPrevText.split(String.valueOf(caracteres[1])).length;
                } else {
                    pos = 0;
                }
                while (pos >= 0 && pos < terminosText.length
                        && i < terminosCompuesta.length) {
                    numCaracteres += terminosText[pos].length() + 1;
                    pos++;
                    i++;
                }
                posicionCompuesta.add(numCaracteres);
            }
            auxText = auxText.substring(auxPrevText.length()
                    + sb.toString().length());

        }

        return posicionCompuesta;
    }

    /**
     * Separamos los caracteres especiales de la palabra que pasemos como
     * parametro.
     *
     * @param word - String con la palabra (word) a procesar
     * @return String[] - Con las palabra formada, cortada por los caracteres
     *         especiales
     */
    private static Object[] splitSpecialCharacters(String word) {

		/*
		 * Listado de apoyo para almacenar las disintas palabra que vallamos
		 * 'cortando'
		 */
        List<String> col = new ArrayList<String>();

		/* Cadena auxiliar, inicialmente a cadena vacia */
        String auxString = "";

		/* Variable para almacenar el caracter de la palabra */
        char letter;

		/* Booleano que indica si un caracter es una letra valida */
        boolean typeLetter = false;

		/*
		 * Booleano que indica si un catacter de la cadena auxiliar es una letra
		 * valida
		 */
        boolean auxTypeLetter = false;

        // Recorremos la palabra caracter a caracter
        for (int index = 0; index < word.length(); index++) {

            // Recuperamos el caracter que indique el contador
            letter = word.charAt(index);

            // Inicialmente la cadena auxiliar estará vacia.
            if (auxString.equals("")) {

                // Comprobamos si el caracter es una letra valida (o un espacio)
                // o caracter de terminacion
                typeLetter = isLetter(letter) || isSpace(letter);

                // Añadimos el primer caracter a la cadena auxiliar
                auxString = String.valueOf(letter);

            } else {
                // Comprobamos si el caracter es una letra valida (o un espacio)
                // o no
                auxTypeLetter = isLetter(letter) || isSpace(letter);

                // Comprobamos los tipos de letras, si son del mismo tipo
                // (validas o no validas), se mantienen unidas
                if (auxTypeLetter == typeLetter) {

                    // Concatenamos el nuevo caracter
                    auxString = auxString.concat(String.valueOf(letter));
                }

                // Comprobamos los tipos de letras, si NO son del mismo tipo
                // (validas o no validas)
                // y la cadena auxiliar es distinta de cadena vacia:
                // Vamos a realizar una separacion de la palabra
                if (auxTypeLetter != typeLetter && !auxString.equals("")) {

                    // Añadimos la secuencia de carateres del mismo tipo al
                    // array de apoyo
                    col.add(auxString);

                    // Inicializamos la cadena auxiliar con el nuevo caracter
                    // (que es del tipo nuevo)
                    auxString = String.valueOf(letter);

                    // Seteamos el tipo de letra, al mismo valor que el del
                    // auxiliar
                    typeLetter = auxTypeLetter;
                }
            }
        }

        // Añadimos la ultima secuencia de caracteres que nos pueda quedar
        if (!auxString.equals("")) {
            col.add(auxString);
        }

        // Retornamos el array de strings
        return col.toArray();
    }

    /**
     * Retornamos el contexto de una determinada posicion.
     *
     * @param posicion    - Poscion del termino en el listado
     * @param lstTerminos - Listado de terminos del texto
     * @return String - Contexto del termino
     */
    public static String obtenerContexto(Integer posicion,
                                         ArrayList<String> lstTerminos) {

        // Rango de obtencion del contexto
        int rango = AnalyzeConstants.RANGO_CONTEXTO;

        // Definimos el punto de inicio del contexto
        int startContext = 0;

        // Definimos el punto de fin del contexto
        int endContext = lstTerminos.size();

        // Comprobamos el rango por delante
        if (posicion - rango > 0) {
            startContext = posicion - rango;
        }

        // Comprobamos el rango por detras del termino
        if (lstTerminos.size() >= posicion + rango + 1) {
            endContext = posicion + rango + 1;
        }

        // Rellenamos el contexto
        StringBuilder sbFinal = formarContexto(startContext, endContext,
                lstTerminos);

        if (sbFinal.length() > 200) {

            boolean terminar = false;
            boolean izda = true;
            boolean dcha = false;
            boolean ultimaIzda = true;
            boolean ultimaDcha = false;
            int start = posicion;
            int end = posicion + 1;

            while (!terminar) {

                // Calculamos la longitud del contexto resultante según
                StringBuilder sbTemporal = formarContexto(start, end,
                        lstTerminos);
                if (end <= endContext && start >= startContext) {
                    if (sbTemporal.length() < 200) {
                        if ((izda) && (endContext >= posicion)) {
                            start--;
                            if (start < 0) {
                                start = 0;
                            }
                            // Si se ha acabado por la dcha seguimos por la izda
                            if (end == posicion) {
                                izda = true;
                                dcha = false;
                                // Sino seguimos por la dcha
                            } else {
                                dcha = true;
                                izda = false;

                            }
                            ultimaIzda = true;
                            ultimaDcha = false;

                        } else if ((dcha) && (startContext <= posicion)) {
                            end++;
                            if (end > lstTerminos.size()) {
                                end = lstTerminos.size();
                            }
                            // Si se ha acabado por la izda seguimos por la dcha
                            if (start == posicion) {
                                dcha = true;
                                izda = false;
                                // Sino seguimos por la izda
                            } else {
                                izda = true;
                                dcha = false;
                            }
                            ultimaDcha = true;
                            ultimaIzda = false;
                        }
                    } else {

                        // Si sobra por la dcha comprobamos que si
                        if (ultimaDcha) {
                            // Sino solo quitamos del final por la derecha
                            end--;
                            // Actualizamos la posicion final y seguimos
                            // iterando
                            endContext = end;

                            // Si sobra por la izda comprobamos que si
                        } else if (ultimaIzda) {
                            // Quitamos una posicion de la izda
                            start++;
                            // Actualizamos la posicion inicial y seguimos
                            // iterando
                            startContext = start;
                        }

                    }

                } else {
                    // Ya hemos recorrido todo el contexto
                    terminar = true;
                }
            }

            // Rellenamos el contexto
            sbFinal = formarContexto(startContext, endContext, lstTerminos);
        }

        return sbFinal.toString();
    }

    /**
     * Comprobamos si la letra es valida.
     *
     * @param letter Letra
     * @return True/False
     */
    private static boolean isLetter(char letter) {

        // Si la letra no esta incluida, retornará '-1'
        if (AnalyzeConstants.VALID_LETTERS.indexOf(letter) >= 0) {
            return true;
        }

        return false;
    }

    /**
     * Comprobamos si es un espacio.
     *
     * @param letter Letra
     * @return True/False
     */
    private static boolean isSpace(char letter) {

        // Si no es un caracter espacio, retornará '-1'
        if (" ".indexOf(letter) >= 0) {
            return true;
        }

        return false;
    }

    /**
     * Si el caracter es de final generico, no separamos el termino.
     *
     * @param letter Letra
     * @return True/False
     */
    private static boolean isCharGenericFinal(char letter) {

        // Si la letra no esta incluida, retornará '-1'
        if (AnalyzeConstants.CHARACTER_GENERIC_FINAL.indexOf(letter) >= 0) {
            return true;
        }

        return false;
    }

    /**
     * M&eacute;todo formarContexto. Devuelve un StringBuilder desde la posición
     * inicial hasta la final del contenido que le pasamos.
     *
     * @param posIni      posIni
     * @param posFin      posFin
     * @param lstPalabras Contenido procesado del contexto
     * @return SubStrign del contexto
     */
    private static StringBuilder formarContexto(int posIni, int posFin,
                                                ArrayList<String> lstPalabras) {
        StringBuilder sb = new StringBuilder();

        for (int i = posIni; i < posFin; i++) {
            sb.append(lstPalabras.get(i));
        }

        return sb;
    }
}
