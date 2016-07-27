package es.inteco.crawler.sexista.modules.analisis.filter.impl;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.base.BaseFilterExcepcion;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ContextFilterExcepcion extends BaseFilterExcepcion implements FilterExcepcion {

    private static final int SALTOS_CONTEXTO = 12;

    //Distancia optima optima estimada al termino
    private static final int DIST_OPTIMA = 2;

    private static final String[] CHAR_FINALES = new String[]{".", ":", ";", "?", "!", "]", ")"};

    private static final String[] FORMAS_DUALES_TEXTO_ANTERIOR = new String[]{"LA Y EL", "EL Y LA", "LA O EL",
            "EL O LA", "LA Y AL", "EL Y A LA", "LA O AL", "EL O A LA", "LOS Y LAS", "LOS O LAS", "LOS Y", "LAS Y",
            "LAS Y LOS", "LOS O", "LAS O", "LAS O LOS", "EL O DE LA", "LA O DEL", "LAS O DE LOS", "LOS O DE LAS",
            "EL Y DE LA", "LA Y DEL", "LAS Y DE LOS", "LOS Y DE LAS", "NUESTRAS Y NUESTROS", "NUESTROS Y NUESTRAS",
            "TODAS Y TODOS LOS", "TODOS Y TODAS LOS", "ESTOS Y ESTAS", "ESTAS Y ESTOS", "ESOS Y ESAS", "ESAS Y ESOS",
            "NUESTRAS O NUESTROS", "NUESTROS O NUESTRAS", "TODAS O TODOS LOS", "TODOS O TODAS LOS", "ESTOS O ESTAS",
            "ESTAS O ESTOS", "ESOS O ESAS", "ESAS O ESOS", "CHICAS", "NIÑAS", "HOMÓLOGAS"
    };

    private static final String[] FORMAS_DUALES_TEXTO_POSTERIOR = new String[]{
            "MUJERES", "CHICAS", "NIÑAS", "SEÑORAS", "HOMÓLOGAS"
    };

    private static final String[] EXPRESION_MUJER = new String[]{"MUJERES", "MUJERES DE"};
    private static final String[] EXPRESION_SENIORA = new String[]{"SEÑORAS", "SEÑORAS DE"};

    public void filterException(Connection conn, TerminoPositionDto terminoDto, List<String> contexto) {

        //Inicializamos el listado de excepciones de contexto
        List<String> excepcionesContexto = new ArrayList<>();

        if (!terminoDto.isFormaUnica()) {
            //'Se crea una lista que incluye el femenino y femenino plural, tanto calculados como los irregulares procedentes de BD
            excepcionesContexto = this.getLstContextException(terminoDto);
        }

        //Listados
        List<String> lstBeforeTerms;
        List<String> lstAfterTerms;
        List<Integer> lstAuxPosition = new ArrayList<>();

        //PARA CADA posicion en la que se encuentre el termino.
        for (Integer position : terminoDto.getPositions()) {

            //Recuperamos el contexto anterior termino a termino
            lstBeforeTerms = this.getBeforeTerminos(position, contexto);

            //Recuperamos el contexto posterior termino a termino
            lstAfterTerms = this.getAfterTerminos(position, contexto);

            //Si no es excepcion de contexto añadimos la posicion al listado final
            if (!this.isContextException(conn, terminoDto, excepcionesContexto, lstBeforeTerms, lstAfterTerms)) {
                lstAuxPosition.add(position);
            }
        }

        // 'Se verifica que en el vecindario no hay ninguno de los terminos considerados femeninos (lista anterior)
        terminoDto.setPositions(lstAuxPosition);
    }

    /**
     * Generamos el listado de excepciones contextuales.
     *
     * @param terminoDto - TerminoPositionDto
     * @return List<String> - Listado de excepciones contextuales
     */
    private List<String> getLstContextException(TerminoPositionDto terminoDto) {

        //Creamos el listado de las excepciones de contexto
        List<String> excepcionesContexto = new ArrayList<>();

        //Femenino Irregular
        String femenino = terminoDto.getFemenino();
        this.addContextException(femenino, excepcionesContexto);

        //Femenino plural irregular
        femenino = terminoDto.getFemeninoPlural();
        this.addContextException(femenino, excepcionesContexto);

        //Comprobamos si tiene un plural irregular
        femenino = terminoDto.getPlural();
        if (null != femenino && !femenino.isEmpty()) {

            //Calculamos el femenino
            femenino = UtilCalculateTerms.calculateFemale(femenino).trim().toUpperCase();

            //Añadimos el termino si no se encuentra ya en el listado
            if (!excepcionesContexto.contains(femenino)) {
                excepcionesContexto.add(femenino);
            }
        }

        //Recuperamos el termino a analizar.
        String termino = terminoDto.getTermino();

        //Calculamos el termino femenino
        femenino = UtilCalculateTerms.calculateFemale(termino);
        this.addContextException(femenino, excepcionesContexto);

        //Hacemos un calculo del plural sobre el femenino del termino
        femenino = UtilCalculateTerms.calculatePlural(UtilCalculateTerms.calculateFemale(termino));
        this.addContextException(femenino, excepcionesContexto);

        //Hacemos un calculo del femenino sobre el plural del termino
        femenino = UtilCalculateTerms.calculateFemale(UtilCalculateTerms.calculatePlural(termino));
        this.addContextException(femenino, excepcionesContexto);

        return excepcionesContexto;
    }

    /**
     * Aniadimos el termino encontrado al listado de excepcion contextuales.
     *
     * @param femenino
     * @param lstContextException
     */
    private void addContextException(String femenino, List<String> lstContextException) {
        if (null != femenino && !femenino.isEmpty()) {
            femenino = femenino.trim().toUpperCase();
            if (!lstContextException.contains(femenino)) {
                lstContextException.add(femenino);
            }
        }
    }

    /**
     * Recuperamos el contexto anterior termino a termino
     *
     * @param position
     * @param context
     * @return
     */
    private List<String> getBeforeTerminos(int position, List<String> context) {

        //Listado
        List<String> lstBeforeTerms = new ArrayList<>();

        //Recuperamos la primera posicion
        int beforePosition = this.getBeforePosition(position);

        //Recuperamos el subtexto anterior a analizar (sin el termino en cuestion)
        for (int i = beforePosition; i < position; i++) {
            lstBeforeTerms.add(context.get(i).trim().toUpperCase());
        }

        return lstBeforeTerms;
    }

    /**
     * Recuperamos el contexto posterior termino a termino
     *
     * @param position
     * @param context
     * @return
     */
    private List<String> getAfterTerminos(int position, List<String> context) {

        //Listado
        List<String> lstAfterTerms = new ArrayList<>();

        //Retornamos la ultima posicion posterior al termino
        int afterPosition = this.getAfterPosition(context, position);

        //Recuperamos el subtexto posterior a analizar (sin el termino en cuestion)
        for (int i = position + 1; i < afterPosition; i++) {
            lstAfterTerms.add(context.get(i).trim().toUpperCase());
        }

        return lstAfterTerms;
    }

    /**
     * Retornamos la primera posicion anterior al termino
     *
     * @param position
     * @return int
     */
    private int getBeforePosition(Integer position) {
        //Analizamos la posicion del termino por la parte anterior del texto
        int pos = position - SALTOS_CONTEXTO;
        //Retornamos la posicion anterior
        return (pos >= 0) ? pos : 0;
    }

    /**
     * Retornamos la ultima posicion posterior al termino
     *
     * @param lstTerminos
     * @param position
     * @return int
     */
    private int getAfterPosition(List<String> lstTerminos, Integer position) {
        //Analizamos la posicion del termino por la parte posterior del texto
        int pos = position + SALTOS_CONTEXTO;
        //Retornamos la posicion posterior
        return (pos < lstTerminos.size()) ? pos : lstTerminos.size();
    }

    /**
     * Comprobamos si es una excepcion de contexto
     *
     * @param excepcionesContexto
     * @param lstBeforeTerms
     * @param lstAfterTerms
     * @return
     */
    private boolean isContextException(Connection conn, TerminoDto terminoDto, List<String> excepcionesContexto,
                                       List<String> lstBeforeTerms, List<String> lstAfterTerms) {

        String palabra;
        String textoAnterior = "";
        boolean encontradoMismoTerminoAnterior = Boolean.FALSE;
        boolean encontradoDeterminanFemeninoPosterior = Boolean.FALSE;
        boolean encontradoDeterminanteMasculino = Boolean.FALSE;

        // Identificamos si el termino es femenino o forma unica
        boolean isTermFemaleOrFU = (terminoDto.isFormaUnica() || UtilCalculateTerms.isFemale(terminoDto.getTermino()));

        //Es un término con forma única, femenino y al principio de la frase. Atletas de todo el mundo ...
        if (isTermFemaleOrFU && lstBeforeTerms.size() == 0) {
            return Boolean.TRUE;
        }

        // --- Analizamos el contenido anterior ---

        // Recorremos las palabras anteriores al termino desde la posicion mas cercana hasta el principio de la frase
        for (int i = lstBeforeTerms.size(); i > 0; i--) {

            //Recuperamos el termino encontrado
            palabra = lstBeforeTerms.get(i - 1);

            //Validacion sobre la palabra
            if (this.validateFor(palabra)) {
                break;
            }

            //Eliminamos espacios y transformamos a mayusculas
            palabra = palabra.trim().toUpperCase();

            //Si la palabra es la mas cercana a termino por la zona anterior...
            if (i == lstBeforeTerms.size()) {

                if (UtilCalculateTerms.isFemale(palabra) && this.isFormaUnica(conn, palabra)) {
                    return Boolean.TRUE;
                }

                //Si el termino es forma unica o femenino y ademas la palabra es una excepcion o es numerica
                if (isTermFemaleOrFU && this.getExcepcionDelanterasFU().contains(palabra) || this.isNumber(palabra)) {
                    return Boolean.TRUE;
                }
            }

            //Comprobamos determinantes femeninos y de forma unica a una distancia optima estimada  del termino a analizar
            if (i > lstBeforeTerms.size() - DIST_OPTIMA && this.getDeterminantesFemeninos().contains(palabra) && isTermFemaleOrFU) {
                return Boolean.TRUE;
            }

            //Comprobamos determinantes masculinos y de forma unica a una distancia optima estimada del termino a analizar
            if (i > lstBeforeTerms.size() - DIST_OPTIMA && this.getDeterminantesMasculinos().contains(palabra) && isTermFemaleOrFU) {
                encontradoDeterminanteMasculino = Boolean.TRUE;
            }

            //Vamos concatenando los terminos para reconstruir la frase anterior
            textoAnterior = palabra + " " + textoAnterior.toUpperCase();

            //Comprobamos si la palabra es una excepcion de contexto
            if (excepcionesContexto.contains(palabra)) {
                return Boolean.TRUE;
            }

            //Mediante este IF y el siguiente se calculan frases como: Nuestras atletas y nuestros atletas
            if (encontradoMismoTerminoAnterior && this.getDeterminantesFemeninos().contains(palabra)) {
                return Boolean.TRUE;
            }

            String termino = terminoDto.getTermino().toUpperCase();
            String terminoPlural = UtilCalculateTerms.calculatePlural(terminoDto.getTermino());
            if (isTermFemaleOrFU && (palabra.equals(termino) || palabra.equals(terminoPlural))) {
                encontradoMismoTerminoAnterior = Boolean.TRUE;
            }
        }

        // --- Analizamos el contenido posterior ---
        StringBuilder textoPosterior = new StringBuilder();
        //Recorremos el listado posterior al termino partiendo desde la palabra mas cercana
        for (int i = 0; i < lstAfterTerms.size(); i++) {
            palabra = lstAfterTerms.get(i);

            //Validacion sobre la palabra
            if (this.validateFor(palabra)) {
                break;
            }

            //Eliminamos espacios y transformamos a mayusculas
            palabra = palabra.trim().toUpperCase();

            //Vamos concatenando los terminos para reconstruir la frase posterior
            textoPosterior.append(" ").append(palabra);

            //Comprobamos si la palabra es una excepcion de contexto
            if (excepcionesContexto.contains(palabra)) {
                return Boolean.TRUE;
            }

            // Mediante este condicional y el siguiente se calculan frases como: "Nuestros atletas y nuestras atletas"
            String termino = terminoDto.getTermino().toUpperCase();
            String terminoPlural = UtilCalculateTerms.calculatePlural(termino);
            if (encontradoDeterminanFemeninoPosterior && (palabra.equals(termino) || palabra.equals(terminoPlural))) {
                return Boolean.TRUE;
            }

            if (this.getDeterminantesFemeninos().contains(palabra)) {
                encontradoDeterminanFemeninoPosterior = Boolean.TRUE;
            }
        }

        //Comprobamos las excepciones de expresion (formas duales, etc...)
        if (this.isExpressionException(textoAnterior, textoPosterior.toString())) {
            return Boolean.TRUE;
        }

        return (!encontradoDeterminanteMasculino && isTermFemaleOrFU);
    }

    private boolean isFormaUnica(Connection conn, String palabra) {

        ArrayList<TerminoDto> list = null;
        try {
            list = this.getTerminoDao().find(conn, palabra);
        } catch (BusinessException e) {
            Logger.putLog("ContextFilterExcepcion Exception", ContextFilterExcepcion.class, Logger.LOG_LEVEL_ERROR, e);
            return Boolean.FALSE;
        }

        return (list.size() == 1);
    }

    /**
     * Validamos si la palabra es un número.
     *
     * @param palabra
     * @return
     */
    private boolean isNumber(String palabra) {
        return Pattern.matches("[0-9]*", palabra);
    }

    /**
     * Validamos el bucle de terminos posteriores al termino a analizar.
     *
     * @param palabra
     * @return
     */
    private boolean validateFor(String palabra) {

        if (null == palabra || palabra.isEmpty()) {
            return Boolean.TRUE;
        }

        //Si es uno de los siguientes codigos ascii finalizamos el for
        // - 7  - Campanilla (beep)
        // - 9  - Tabulador horizontal
        // - 12 - Salto de página
        // - 13 - Retorno de carro
        // - 14 - Shift fuera
        if (palabra.toCharArray().length == 0) {
            char c = palabra.toCharArray()[0];
            if ((int) c == 7 || (int) c == 9 || (int) c == 12 || (int) c == 13 || (int) c == 14) {
                return Boolean.TRUE;
            }
        }

        //Si es un caracter final, finalizamos el for
        for (String caracter : CHAR_FINALES) {
            if (palabra.equals(caracter)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Comprobamos si es una excepcion de expresion.
     *
     * @param textoAnterior beforeText
     * @return isExcepExpression
     */
    private boolean isExpressionException(String textoAnterior, String textoPosterior) {

        //Comprobamos las formas duales por la parte anterior al termino y expresiones que aparezcan
        if (null != textoAnterior && !textoAnterior.isEmpty()) {

            for (String formaDual : FORMAS_DUALES_TEXTO_ANTERIOR) {
                if (textoAnterior.contains(formaDual)) {
                    return Boolean.TRUE;
                }
                if (textoAnterior.contains(EXPRESION_MUJER[0])) {
                    if (textoAnterior.contains(EXPRESION_MUJER[1])) {
                        return Boolean.TRUE;
                    }
                }
                if (textoAnterior.contains(EXPRESION_SENIORA[0])) {
                    if (textoAnterior.contains(EXPRESION_SENIORA[1])) {
                        return Boolean.TRUE;
                    }
                }
            }
        }


        // Comprobamos si hay formas duales por la parte posterior al termino
        if (null != textoPosterior && !textoPosterior.isEmpty()) {
            for (String formaDual : FORMAS_DUALES_TEXTO_POSTERIOR) {
                if (textoPosterior.contains(formaDual)) {
                    return Boolean.TRUE;
                }
            }
        }

        //Por defecto retornamos false
        return Boolean.FALSE;
    }
}
