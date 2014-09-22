package es.inteco.crawler.sexista.modules.analisis.filter.impl;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.base.BaseFilterExcepcion;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * HeuristicFilterExcepcion.
 *
 * @author jfiz
 */
public class HeuristicFilterExcepcion extends BaseFilterExcepcion implements
        FilterExcepcion {

    private static final int SALTOS_CONTEXTO = 12;

    boolean singularSolitario = true;
    boolean nombrePropioHabilitada = true;
    boolean vocativoYProfesionHabilitada = true;
    boolean gentilicioComienzoFraseHabilitada = true;
    boolean gentilicioYVocativoOPreposicion = true;
    boolean profesionesHabilitada = true;
    boolean posibleApellidoHabilitada = true;

    String[] categoriasNombrePropio = {"28", "29", "31", "34"};
    //String[] categoriasValidas = {"31", "30", "28", "33", "29", "35", "34"};

    private static final String CATEGORIA_GENTILICIO = "28";
    private static final String CATEGORIA_PROFESION = "29";
    private static final String ALFABETO_MAYUSCULAS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

    // Buscar genérico
    String[] separadoresClausualasExplicativas = {",", "(", "A", "DE", "DEL"};

    String[] separadoresGentilicio = {"Y", "O", "E", ","};

    String[] determinantesMasculinos = {"EL", "AL", "LOS", "DEL", "UN", "UNO",
            "UNOS", "NUESTROS", "VUESTROS", "ESOS", "ESTOS", "AQUELLOS",
            "NUESTRO", "VUESTRO", "ESE", "ESTE", "AQUEL", "TODO", "TODOS",
            "SUPUESTOS", "SUPUESTO", "NINGUNOS", "NINGUNO", "ALGUNOS",
            "ALGUNO", "CUANTOS", "CUANTO", "MUCHO", "MUCHOS", "POCO", "POCOS",
            "CUANTO", "DEMASIADO"};

    String[] separadoresVocativo = {",", "(", "ES", "SERA", "FUE", "SIDO"};

    public void filterException(Connection conn, TerminoPositionDto terminoDto,
                                List<String> contexto) {

        // Listados
        List<String> lstBeforeTerms;
        List<String> lstAfterTerms;
        List<Integer> lstAuxPosition = new ArrayList<Integer>();

        // PARA CADA posicion en la que se encuentre el termino.
        for (Integer position : terminoDto.getPositions()) {

            // Recuperamos el contexto anterior termino a termino
            lstBeforeTerms = this.getBeforeTerminos(position, contexto);

            // Recuperamos el contexto posterior termino a termino
            lstAfterTerms = this.getAfterTerminos(position, contexto);

            // Si no es excepcion de heuristica añadimos la posicion al listado
            // final
            if (!this.isHeuristicException(conn, terminoDto, contexto, position, lstBeforeTerms, lstAfterTerms)) {
                lstAuxPosition.add(position);
            }
        }
        // Se verifica que en el vecindario no hay ninguno de los terminos
        // considerados femeninos (lista anterior)
        terminoDto.setPositions(lstAuxPosition);
    }

    /**
     * Método que comprueba si una cadena está en un array de cadenas.
     *
     * @param listado array
     * @param valor   cadena
     * @return verdadero si está
     */
    private boolean contains(String[] listado, String valor) {
        for (String cad : listado) {
            if (cad.equals(valor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba las distintas heuristicas para el termino que se le indique.
     *
     * @param terminoDto     Termino a comprobar
     * @param lstTerminos    Listado de terminos
     * @param position       Posición del término actual
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Verdadero si es una excepción heurística
     */
    private boolean isHeuristicException(Connection conn, TerminoPositionDto terminoDto,
                                         List<String> lstTerminos, Integer position,
                                         List<String> lstBeforeTerms, List<String> lstAfterTerms) {
        try {
            if (singularSolitario && hTerminoSingularSolitario(terminoDto, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (nombrePropioHabilitada && hNombrePropio(terminoDto, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (vocativoYProfesionHabilitada && hVocativoYProfesion(terminoDto, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (gentilicioComienzoFraseHabilitada && hGentilicioAlComienzoDeFrase(terminoDto, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (gentilicioYVocativoOPreposicion && hGentilicioSeguidoDeVocativoOPreposicion(conn, terminoDto, lstTerminos, position, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (profesionesHabilitada && hProfesiones(conn, terminoDto, lstTerminos, position, lstBeforeTerms, lstAfterTerms)) {
                return true;
            } else if (posibleApellidoHabilitada && hPosibleApellido(terminoDto, lstTerminos, position)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Heurística de Termino en singular solitario.
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     */
    private boolean hTerminoSingularSolitario(TerminoPositionDto terminoDto,
                                              List<String> lstBeforeTerms, List<String> lstAfterTerms) {

        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {
            return ((lstBeforeTerms.size() == 0) && (lstAfterTerms.size() == 0));
        }
        return false;
    }

    /**
     * Heurística de Nombre propio.
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     */
    private boolean hNombrePropio(TerminoPositionDto terminoDto,
                                  List<String> lstBeforeTerms, List<String> lstAfterTerms) {

        // HEURISTICA NOMBRE PROPIO
        // TérminoSingular + PalabraMayúsculas.
        // P.e. el capitán Alatriste,  el suizo Roger Federer. Este
        // término puede ser Sustantivo, Adjetivo, Profesión o Gentilicio
        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {
            if (lstBeforeTerms.size() > 0) {
                if (lstAfterTerms.size() > 1) {
                    return (contains(categoriasNombrePropio, ""
                            + terminoDto.getIdCategoria()) && isCapitalized(lstAfterTerms.get(0))) || (contains(
                            separadoresClausualasExplicativas, lstAfterTerms
                            .get(0)) && isCapitalized(lstAfterTerms.get(1)));
                } else {
                    if (lstAfterTerms.size() > 0) {
                        return (contains(categoriasNombrePropio, ""
                                + terminoDto.getIdCategoria()) && isCapitalized(lstAfterTerms.get(0)));
                    }
                }
            }
        }
        return false;
    }

    /**
     * Heurística de vocativo y profesión
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     */
    private boolean hVocativoYProfesion(TerminoPositionDto terminoDto,
                                        List<String> lstBeforeTerms, List<String> lstAfterTerms) {

        // HEURISTICA VOCATIVO + PROFESION
        // PalabraMayúsculas + (, o ( o es o será o fue o sido) +
        // [el] + ProfesiónSingular. P.e. Zapatero, presidente del gobierno,
        // Cristiano será el jugador mejor pagado
        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {
            if (lstBeforeTerms.size() > 1) {
                if (CATEGORIA_PROFESION.equals("" + terminoDto
                        .getIdCategoria())) {
                    if (contains(determinantesMasculinos, lstBeforeTerms.get(lstBeforeTerms.size() - 1)
                            .trim().toUpperCase())) {
                        if (lstBeforeTerms.size() > 2) {
                            return (contains(separadoresVocativo,
                                    UtilCalculateTerms
                                            .removeAccents(lstBeforeTerms
                                                    .get(lstBeforeTerms.size() - 2).trim()
                                                    .toUpperCase())) && isCapitalized(lstBeforeTerms.get(lstBeforeTerms.size() - 3)));
                        }
                    } else {
                        return (contains(separadoresVocativo,
                                UtilCalculateTerms.removeAccents(lstBeforeTerms
                                        .get(lstBeforeTerms.size() - 1).trim().toUpperCase()))
                                && isCapitalized(lstBeforeTerms.get(lstBeforeTerms.size() - 2)));
                    }
                }
            }
        }
        return false;
    }

    /**
     * Heurística de gentilicio al comienzo de frase.
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     */
    private boolean hGentilicioAlComienzoDeFrase(TerminoPositionDto terminoDto,
                                                 List<String> lstBeforeTerms, List<String> lstAfterTerms) {

        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {
            return ((lstBeforeTerms.size() == 0 || ".".equals(lstBeforeTerms.get(lstBeforeTerms.size() - 1).trim())) && (CATEGORIA_GENTILICIO
                    .equals("" + terminoDto.getIdCategoria())));
        }
        return false;
    }

    /**
     * Heurística de gentilicio seguido de vocativo o preposicion
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     * @throws BusinessException bex
     */
    private boolean hGentilicioSeguidoDeVocativoOPreposicion(Connection conn,
                                                             TerminoPositionDto terminoDto, List<String> lstTerminos,
                                                             Integer position, List<String> lstBeforeTerms,
                                                             List<String> lstAfterTerms) throws BusinessException {

        // GENTILICIO Y CONJUNCION
        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {
            if (CATEGORIA_GENTILICIO.equals("" + terminoDto.getIdCategoria())) {
                if (lstBeforeTerms.size() > 0) {

                    if (contains(separadoresGentilicio, UtilCalculateTerms
                            .removeAccents(lstBeforeTerms.get(lstBeforeTerms.size() - 1).trim()
                                    .toUpperCase()))) {
                        return true;
                    }

                    if (position > 0) {
                        List<TerminoDto> find = getTerminoDao().find(conn,
                                lstTerminos.get(position - 1));
                        if (find.size() == 1) {
                            TerminoDto tdtoAux = find.get(0);
                            if (tdtoAux != null
                                    && CATEGORIA_PROFESION.equals("" + tdtoAux
                                    .getIdCategoria())) {
                                return true;
                            }
                        }
                    }
                }
                if (lstAfterTerms.size() > 0) {

                    if (contains(separadoresGentilicio, UtilCalculateTerms
                            .removeAccents(lstAfterTerms.get(0).trim()
                                    .toUpperCase()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Heurística de profesiones
     *
     * @param terminoDto     Termino a comprobar.
     * @param lstBeforeTerms Contexto anterior
     * @param lstAfterTerms  Contexto posterior
     * @return Si es una excepción
     * @throws BusinessException bex
     */
    private boolean hProfesiones(Connection conn, TerminoPositionDto terminoDto,
                                 List<String> lstTerminos, Integer position,
                                 List<String> lstBeforeTerms, List<String> lstAfterTerms)
            throws BusinessException {

        // HEURISTICA PROFESIONES
        // P.e. El tenista mallorquín, (El alcalde de Madrid ¡ésta última
        // quitada!)
        if (terminoDto.isUsaGlobalException() && terminoDto.isFoundInSingular()) {

            if (CATEGORIA_PROFESION.equals("" + terminoDto.getIdCategoria())
                    && lstAfterTerms.size() > 0) {

                if (position < lstTerminos.size() - 1) {
                    List<TerminoDto> find = getTerminoDao().find(conn,
                            lstTerminos.get(position + 1));
                    if (find.size() == 1) {
                        TerminoDto tdtoAux = find.get(0);
                        return (tdtoAux != null && CATEGORIA_GENTILICIO
                                .equals("" + tdtoAux.getIdCategoria()));
                    }
                }
            }
        }
        return false;
    }

    /**
     * Recuperamos el contexto anterior termino a termino
     *
     * @param position
     * @param context
     * @return
     */
    private List<String> getBeforeTerminos(int position, List<String> context) {

        // Listado
        List<String> lstBeforeTerms = new ArrayList<String>();

        // Recuperamos la primera posicion
        int beforePosition = this.getBeforePosition(position);

        // Recuperamos el subtexto anterior a analizar (sin el termino en
        // cuestion)
        for (int i = beforePosition; i < position; i++) {
            lstBeforeTerms.add(context.get(i).trim());
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

        // Listado
        List<String> lstAfterTerms = new ArrayList<String>();

        // Retornamos la ultima posicion posterior al termino
        int afterPosition = this.getAfterPosition(context, position);

        // Recuperamos el subtexto posterior a analizar (sin el termino en
        // cuestion)
        for (int i = position + 1; i < afterPosition; i++) {
            lstAfterTerms.add(context.get(i).trim());
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
        // Analizamos la posicion del termino por la parte anterior del texto
        int pos = (position.intValue() - SALTOS_CONTEXTO);
        // Retornamos la posicion anterior
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
        // Analizamos la posicion del termino por la parte posterior del texto
        int pos = (position.intValue() + SALTOS_CONTEXTO);
        // Retornamos la posicion posterior
        return (pos < lstTerminos.size()) ? pos : lstTerminos.size();
    }

    /**
     * Si el término actual pertenece a alguna categoría de nombres propios, y tanto él mismo como el término anterior
     * comienzan por mayúscula, se considerará un posible apellido (P.E. Jose Luis Rodríguez Zapatero)
     *
     * @param terminoDto
     * @param lstTerminos
     * @param position
     * @return
     */
    private boolean hPosibleApellido(TerminoPositionDto terminoDto, List<String> lstTerminos, Integer position) {
        if (contains(categoriasNombrePropio, String.valueOf(terminoDto.getIdCategoria()))
                && position > 0
                && isCapitalized(lstTerminos.get(position))
                && isCapitalized(lstTerminos.get(position - 1))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCapitalized(String term) {
        return (ALFABETO_MAYUSCULAS.contains(term.trim().substring(0, 1))
                && (!ALFABETO_MAYUSCULAS.contains(term.trim().substring(1, 2))));
    }

}
