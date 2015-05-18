package es.inteco.crawler.sexista.modules.commons.util;

import es.inteco.crawler.sexista.modules.commons.Constants.TerminationConstants;

/**
 * Calculo de terminos. (Singular, plural, femenino...)
 */
public final class UtilCalculateTerms {

    /**
     * Array de diptongos y triptongos.
     */
    private static final String[] DIPTONGOS = {"IAI", "IAU", "IEI", "UAI", "UAU", "UEI", "AU", "EU", "OU", "UA", "UE",
            "UO", "AI", "EI", "OI", "IA", "IE", "IO", "UI", "IU"};

    /**
     * Array de diptongos y triptongos Acentuados.
     */
    private static final String[] DIPTONGOS_ACENTUADOS = {"IÁI", "IÁU", "IÉI", "UÁI", "UÁU", "UÉI", "ÁU", "ÉU", "ÓU",
            "UÁ", "UÉ", "UÓ", "ÁI", "ÉI", "ÓI", "IÁ", "IÉ", "IÓ"};

    /**
     * Array de letras que van acompañadas de vocal.
     */
    private static final String[] CONSONANTES_CON_VOCAL = {"GUE", "GUI", "QUE", "QUI"};
    /**
     * Array de sustitucion letras que van acompañadas de vocal.
     */
    private static final String[] SUSTIT_CONSONANTES_CON_VOCAL = {"GGE", "GGI", "QQE", "QQI"};

    /**
     * Array de vocales Acentuadas.
     */
    protected static final String[] VOCALES_ACENTUADAS = {"Á", "É", "Í", "Ó", "Ú"};
    /**
     * Array de vocales No Acentuadas.
     */
    protected static final String[] VOCALES_NO_ACENTUADAS = {"A", "E", "I", "O", "U"};

    /**
     * Array de excepciones de plurales.
     */
    protected static final String[] EXCP_PLURALES = {"CLASES", "FRASES", "MUELLES"};


    private UtilCalculateTerms() {
    }

    /**
     * Calculo de Femenino.
     *
     * @param term - Termino inicial a calcular
     * @return String - Femenino del termino
     */
    public static String calculateFemale(String term) {

        // Comprobamos que tengamos valor en term
        if (term == null || term.length() == 0) {
            return "";
        }

        String terminacion;
        String termino = term.trim().toUpperCase();

        // Excepciones formacion femenino
        if (termino.equals(TerminationConstants.EXCP_FEMENINO_EMPERADOR)) {
            return TerminationConstants.EXCP_FEMENINO_EMPERATRIZ;
        }
        if (termino.equals(TerminationConstants.EXCP_FEMENINO_ACTOR)) {
            return TerminationConstants.EXCP_FEMENINO_ACTRIZ;
        }

        // Terminaciones invariantes
        if (termino.length() > 2) {
            // Recuperamos la terminacion
            terminacion = termino.substring(termino.length() - 3);

            if (terminacion.equals(TerminationConstants.DOR)) {
                termino = termino.substring(0, termino.length() - 3).concat(
                        TerminationConstants.TRIZ);
                return termino;
            }
        }

        if (termino.length() > 2) {
            // Recuperamos la terminacion
            terminacion = termino.substring(termino.length() - 3);

            if (terminacion.equals(TerminationConstants.DAD)
                    || terminacion.equals(TerminationConstants.TAD)
                    || terminacion.equals(TerminationConstants.ION)
                    || terminacion.equals(TerminationConstants.SIS)) {
                return "";
            }
        }

        if (termino.length() > 1) {
            // Recuperamos la terminacion
            terminacion = termino.substring(termino.length() - 2);

            if (terminacion.equals(TerminationConstants.IE)
                    || terminacion.equals(TerminationConstants.EZ)) {
                return "";
            }

            if (terminacion.equals(TerminationConstants.AN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ANA);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.ON)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ONA);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.IN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.INA);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.ES1)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ESA);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.ES2)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.AS);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.OS)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.AS);
                return termino;
            }
        }

        // Terminaciones en vocal, se cambia la vocal por A
        terminacion = termino.substring(termino.length() - 1);

        if (TerminationConstants.VOCALES.contains(terminacion)) {
            termino = termino.substring(0, termino.length() - 1).concat(
                    TerminationConstants.A);
            return termino;
        }

        // Terminaciones en consonante, se añade una A al final
        return termino.concat(TerminationConstants.A);
    }

    /**
     * Calculo del plural.
     *
     * @param term - Termino inicial a calcular
     * @return String - Plural del termino
     */
    public static String calculatePlural(String term) {

        // Comprobamos que tengamos valor en term
        if (term == null || term.length() == 0) {
            return "";
        }

        String terminacion;
        String termino = term.trim().toUpperCase();

        if (termino.length() > 1) {
            terminacion = termino.substring(termino.length() - 2);

            if (terminacion.equals(TerminationConstants.IS)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ISES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.ES1)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ESES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.US)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.USES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.AN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ANES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.EN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ENES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.IN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.INES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.ON)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.ONES);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.UN)) {
                termino = termino.substring(0, termino.length() - 2).concat(
                        TerminationConstants.UNES);
                return termino;
            }

            // Se comenta por estar repetida
            // if (terminacion.equals(TerminationConstants.IS)) {
            //	return termino;
            // }
        }

        if (termino.length() > 0) {
            terminacion = termino.substring(termino.length() - 1);

            if (TerminationConstants.CONSONANTES_REGULARES.contains(terminacion)) {
                termino = termino.concat(TerminationConstants.ES2);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.Z)) {
                termino = termino.substring(0, termino.length() - 1).concat(
                        TerminationConstants.CES);
                return termino;
            }

            if (TerminationConstants.IU.contains(terminacion)) {
                termino = termino.concat(TerminationConstants.ES2);
                return termino;
            }
        }

        // Por defecto
        termino = termino.concat(TerminationConstants.S);
        return termino;
    }

    /**
     * Calculo del singular.
     *
     * @param term - Termino a transformar a singular
     * @return Singular el término
     */
    public static String calculateSingular(String term) {

        String firstWord = "";
        String firstWordAux;

        String termino;
        String terminoAuxiliar = "";
        int numSilabas;
        int silabaAcentuada;

        // Comprobamos que no llegue vacio
        if (null == term || term.equals("")) {
            return "";
        }

        term = term.trim();

        // Recuperamos el termino
        if (term.lastIndexOf(' ') > 0) {
            firstWord = term.substring(0, term.lastIndexOf(' ') + 1);
            term = term.substring(term.lastIndexOf(' ') + 1);
        }

        // Recuperamos el termino a calcular
        termino = term.trim().toUpperCase();

        // Calculamos el numero de sílabas y la acentuacion
        int[] res = calculaNumeroSilabasYAcentuacion(termino);
        numSilabas = res[0];
        silabaAcentuada = res[1];

        if (termino.length() > 5) {
            if (termino.substring(termino.length() - 5).equals("ARIOS")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 5)
                        .concat("ARIO");
                return firstWord.concat(terminoAuxiliar);
            }

            if (termino.substring(termino.length() - 5).equals("ISTAS")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 5)
                        .concat("ISTA");
                return firstWord.concat(terminoAuxiliar);
            }

            if (termino.substring(termino.length() - 5).equals("ENTES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 5)
                        .concat("ENTE");
                return firstWord.concat(terminoAuxiliar);
            }
        }

        if (termino.length() > 4) {

            if (termino.substring(termino.length() - 4).equals("ADES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("AD");
            }

            if (termino.substring(termino.length() - 4).equals("ERES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("ER");
            }

            if (termino.substring(termino.length() - 4).equals("ADOS")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("ADO");
            }

            if (termino.substring(termino.length() - 4).equals("EROS")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("ERO");
            }

            if (silabaAcentuada == 0) {
                if (termino.substring(termino.length() - 4).equals("ASES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÁS");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("AS");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("ESES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÉS");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("ES");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("ISES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÍS");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("IS");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("OSES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÓS");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("OS");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("USES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÚS");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("US");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("ANES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÁN");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("AN");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("ENES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÉN");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("EN");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("INES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÍN");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("IN");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("ONES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÓN");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("ON");
                    }
                }
                if (termino.substring(termino.length() - 4).equals("UNES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ÚN");
                    if (numSilabas == 2) {
                        terminoAuxiliar = termino.substring(0,
                                termino.length() - 4).concat("UN");
                    }
                }
            } else {

                if (termino.substring(termino.length() - 4).equals("ASES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("AS");
                }
                if (termino.substring(termino.length() - 4).equals("ESES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ES");
                }
                if (termino.substring(termino.length() - 4).equals("ISES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("IS");
                }
                if (termino.substring(termino.length() - 4).equals("OSES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("OS");
                }
                if (termino.substring(termino.length() - 4).equals("USES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("US");
                }
                if (termino.substring(termino.length() - 4).equals("ANES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("AN");
                }
                if (termino.substring(termino.length() - 4).equals("ENES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("EN");
                }
                if (termino.substring(termino.length() - 4).equals("INES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("IN");
                }
                if (termino.substring(termino.length() - 4).equals("ONES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("ON");
                }
                if (termino.substring(termino.length() - 4).equals("UNES")) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 4).concat("UN");
                }
            }
            if (termino.substring(termino.length() - 4).equals("ORES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("OR");
            }
            if (termino.substring(termino.length() - 4).equals("OTES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("OTE");
            }

            if (termino.substring(termino.length() - 4).equals("ACES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("AZ");
            }

            if (termino.substring(termino.length() - 4).equals("ICES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 4)
                        .concat("IZ");
            }

        }

        if (termino.length() > 3
                && (terminoAuxiliar == null || terminoAuxiliar.equals(""))) {
            if (termino.substring(termino.length() - 3).equals("CES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 3)
                        .concat("Z");
            }
            if (termino.substring(termino.length() - 3).equals("LES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 3)
                        .concat("L");
            }
            if (termino.substring(termino.length() - 3).equals("YES")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 3)
                        .concat("Y");
            }

            if (termino.substring(termino.length() - 2).equals("ES")
                    && TerminationConstants.VOCALES_ACENTUADAS.contains(termino.substring(termino.length() - 3,
                    termino.length() - 2))) {
                terminoAuxiliar = termino.substring(0, termino.length() - 2);
            }

            if (termino.substring(termino.length() - 2).equals("ES")
                    && TerminationConstants.VOCALES.contains(termino.substring(termino.length() - 3,
                    termino.length() - 2))) {
                terminoAuxiliar = termino.substring(0, termino.length() - 3);

                String c = termino.substring(termino.length() - 3, termino
                        .length() - 2);
                if (c.equals("A")) {
                    terminoAuxiliar += "Á";
                } else if (c.equals("E")) {
                    terminoAuxiliar += "É";
                } else if (c.equals("I")) {
                    terminoAuxiliar += "Í";
                } else if (c.equals("O")) {
                    terminoAuxiliar += "Ó";
                } else {
                    terminoAuxiliar += "Ú";
                }

                if (esMonosilabo(terminoAuxiliar)) {
                    terminoAuxiliar = termino
                            .substring(0, termino.length() - 2);
                }
            }
        }

        if (termino.length() > 2
                && (terminoAuxiliar == null || terminoAuxiliar.equals(""))) {
            if (termino.substring(termino.length() - 2).equals("OS")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 2)
                        .concat("O");
            }
        }

        //Comprobamos si es alguna de las excepciones de plurales
        //Para solo quitarle la s.
        boolean excPlurales = false;
        for (String cad : EXCP_PLURALES) {
            if (cad.equals(termino)) {
                excPlurales = true;
                break;
            }
        }

        if (termino.length() > 1
                && (terminoAuxiliar == null || terminoAuxiliar.equals("")) || excPlurales) {
            if (termino.substring(termino.length() - 1).equals("S")) {
                terminoAuxiliar = termino.substring(0, termino.length() - 1);
            }
        }

        // Las sobreesdrújulas que se convierten en esdréjulas conservan el
        // acento
        // Las esdrújulas que se convierten en llanas hay que comprobar el
        // acento
        // Las llanas que se convierten en agudas ya he controlado el acento con
        // las terminaciones n, s o vocal acentuada

        // Vamos a intentar controlar el 2º caso
        int nuevaSilabaAcentuada;
        res = calculaNumeroSilabasYAcentuacion(terminoAuxiliar);
        nuevaSilabaAcentuada = res[1];

        if (silabaAcentuada == 3 && nuevaSilabaAcentuada == 2) {
            // Comprobamos si al pasar de esdrújula llana sigue acentuada
            if (terminoAuxiliar.endsWith("N")
                    || terminoAuxiliar.endsWith("S")
                    || terminoAuxiliar.substring(terminoAuxiliar.length() - 1).contains(TerminationConstants.VOCALES_ACENTUADAS)) {
                // Hay que quitar el acento
                terminoAuxiliar = removeAccents(terminoAuxiliar);
            }
        }

        if (terminoAuxiliar != null && !terminoAuxiliar.equals("")) {
            if (firstWord != null && !firstWord.equals("")) {
                firstWordAux = calculateSingular(firstWord);
                if (firstWordAux != null && !firstWordAux.equals("")
                        && firstWordAux.compareTo(firstWord) != 0) {
                    return firstWordAux.concat(" ").concat(terminoAuxiliar);
                } else {
                    return firstWord.concat(terminoAuxiliar);
                }
            } else {
                return firstWord.concat(terminoAuxiliar);
            }
        } else {
            if (firstWord != null && !firstWord.equals("")) {
                firstWordAux = calculateSingular(firstWord);
                if (firstWordAux != null && !firstWordAux.equals("")
                        && firstWordAux.compareTo(firstWord) != 0) {
                    return firstWordAux.concat(" ").concat(termino);
                } else {
                    return firstWord.concat(termino);
                }
            } else {
                return firstWord.concat(termino);
            }
        }
    }

    /**
     * Consultamos si el termino es de género femenino.
     *
     * @param term - Termino consultado
     * @return boolean - Respuesta a la pregunta (true/false)
     */
    public static boolean isFemale(String term) {

        String terminacion;
        String termino;

        // Comprobamos que no llegue vacio
        if (term != null && term.length() > 0) {
            termino = term.trim().toUpperCase();

            // Determinantes que cumplen la regla del femenino, pero que no denotan
            // necesariamente un femenino
            if (termino.equals(TerminationConstants.CADA)) {
                return false;
            }
            if (termino.equals(TerminationConstants.CUYA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.TREINTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.CUARENTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.CINCUENTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.SESENTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.SETENTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.OCHENTA)) {
                return false;
            }
            if (termino.endsWith(TerminationConstants.NOVENTA)) {
                return false;
            }
            if (termino.startsWith(TerminationConstants.AGRICOLA)) {
                return false;
            }
            if (termino.startsWith(TerminationConstants.AGRIICOLA)) {
                return false;
            }


            // �triz
            if (termino.length() > 3) {
                terminacion = termino.substring(termino.length() - 4);

                if (terminacion.equals(TerminationConstants.TRIZ)) {
                    return true;
                }
            }

            // dad, -tad, -ión, -sis, -esa, -isa, -ina
            if (termino.length() > 2) {
                terminacion = termino.substring(termino.length() - 3);

                if (terminacion.equals(TerminationConstants.DAD)
                        || terminacion.equals(TerminationConstants.TAD)
                        || terminacion.equals(TerminationConstants.ION)
                        || terminacion.equals(TerminationConstants.SIS)
                        || terminacion.equals(TerminationConstants.ESA)
                        || terminacion.equals(TerminationConstants.ISA)
                        || terminacion.equals(TerminationConstants.INA)) {
                    // Es una excepcion
                    return !termino.equals(TerminationConstants.EXCP_ENFASIS);
                }
            }

            // -ie, -ez
            if (termino.length() > 1) {
                terminacion = termino.substring(termino.length() - 2);

                if ((terminacion.equals(TerminationConstants.IZ)
                        || terminacion.equals(TerminationConstants.IE) || terminacion
                        .equals(TerminationConstants.EZ)
                        && !termino.equals(TerminationConstants.EXCP_JUEZ))) {
                    // OJO, no puede ser JUEZ, ya que es una excepcion --> JUEZA
                    return true;
                }
            }

            // Estos términos terminan en -a, pero son genéricos
            if (termino.equals(TerminationConstants.EXCP_ARTISTA)
                    || termino.equals(TerminationConstants.EXCP_ASTRONAUTA)
                    || termino.equals(TerminationConstants.EXCP_ATLETA)
                    || termino.equals(TerminationConstants.EXCP_GUIA)
                    || termino.equals(TerminationConstants.EXCP_PERIODISTA)
                    || termino.equals(TerminationConstants.EXCP_TURISTA)
                    || termino.equals(TerminationConstants.EXCP_PIANISTA)
                    || termino.equals(TerminationConstants.EXCP_VIOLINISTA)
                    || termino.equals(TerminationConstants.EXCP_TENISTA)
                    || termino.equals(TerminationConstants.EXCP_ARTISTAS)
                    || termino.equals(TerminationConstants.EXCP_ASTRONAUTAS)
                    || termino.equals(TerminationConstants.EXCP_ATLETAS)
                    || termino.equals(TerminationConstants.EXCP_GUIAS)
                    || termino.equals(TerminationConstants.EXCP_PERIODISTAS)
                    || termino.equals(TerminationConstants.EXCP_TURISTAS)
                    || termino.equals(TerminationConstants.EXCP_PIANISTAS)
                    || termino.equals(TerminationConstants.EXCP_VIOLINISTAS)
                    || termino.equals(TerminationConstants.EXCP_TENISTAS)) {
                return false;
            }

            // -as
            if (termino.length() > 1) {
                terminacion = termino.substring(termino.length() - 2);
                if (terminacion.equals(TerminationConstants.AS)) {
                    return true;
                }
            }

            terminacion = termino.substring(termino.length() - 1);
            if (terminacion.equals(TerminationConstants.A)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Método que calcula si una palabra es monosílaba.
     *
     * @param palabra La palabra a comprobar
     * @return boolean
     */
    protected static boolean esMonosilabo(String palabra) {

        if (palabra != null && !"".equals(palabra)) {
            try {
                // Comprobamos que solo tiene una silaba
                return (calculaNumeroSilabas(palabra) == 1);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Calcula el número de sílabas de una palabra.
     *
     * @param palabra La palabra a comprobar
     * @return int
     */
    protected static int calculaNumeroSilabas(String palabra) {

        if (palabra != null && !"".equals(palabra)) {
            try {

                String palabraAux = palabra.toUpperCase();

                int numSilabas = 0;
                //Buscamos patrones GU+vocal y QU+vocal GUE,GUI,QUE,QUI
                //y los sustituimos por GGE,GGI,QQE,QQI para que no se cuenten como silabas.
                for (int i = 0; i < CONSONANTES_CON_VOCAL.length; i++) {
                    palabraAux = palabraAux.replace(CONSONANTES_CON_VOCAL[i], SUSTIT_CONSONANTES_CON_VOCAL[i]);
                }

                // Miramos si contiene diptongos
                for (String dipt : DIPTONGOS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(dipt, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(dipt, startIndex) + dipt.length();
                    }
                    palabraAux = palabraAux.replace(dipt, "$");

                }
                // Miramos si contiene diptongos acentuados
                for (String dipt : DIPTONGOS_ACENTUADOS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(dipt, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(dipt, startIndex) + dipt.length();
                    }
                    palabraAux = palabraAux.replace(dipt, "$");
                }
                // Miramos si contiene vocales acentuadas
                for (String vocal : VOCALES_ACENTUADAS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(vocal, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(vocal, startIndex) + 1;
                    }
                    palabraAux = palabraAux.replace(vocal, "$");
                }
                // Miramos si contiene vocales no acentuadas
                for (String vocal : VOCALES_NO_ACENTUADAS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(vocal, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(vocal, startIndex) + 1;
                    }
                    palabraAux = palabraAux.replace(vocal, "$");
                }

                return numSilabas;
            } catch (Exception e) {
                return 0;
            }

        } else {
            return 0;
        }
    }

    /**
     * Calcula el número de sílabas de una palabra y devuelve tambien el número
     * de sílaba acentuada: 1 la palabra es aguda, 2 la palabra es llana, 3 la
     * palabra es esdrújula, 4 o más la palabra es sobreesdrújula.
     *
     * @param palabra La palabra a comprobar
     * @return Número de sílabas de la palabra
     */
    protected static int[] calculaNumeroSilabasYAcentuacion(String palabra) {

        int[] result = new int[2];
        int silabaAcentuada = 0;
        int numSilabas = 0;
        if (palabra != null && !"".equals(palabra)) {
            try {

                String palabraAux = palabra.toUpperCase();

                //Buscamos patrones GU+vocal y QU+vocal GUE,GUI,QUE,QUI
                //y los sustituimos por GGE,GGI,QQE,QQI para que no se cuenten como silabas.
                for (int i = 0; i < CONSONANTES_CON_VOCAL.length; i++) {
                    palabraAux = palabraAux.replace(CONSONANTES_CON_VOCAL[i], SUSTIT_CONSONANTES_CON_VOCAL[i]);
                }


                // Miramos si contiene diptongos
                for (String dipt : DIPTONGOS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(dipt, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(dipt, startIndex) + dipt.length();
                    }
                    palabraAux = palabraAux.replace(dipt, "$");
                }
                // Miramos si contiene diptongos acentuados
                for (String dipt : DIPTONGOS_ACENTUADOS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(dipt, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(dipt, startIndex) + dipt.length();
                    }
                    palabraAux = palabraAux.replace(dipt, "#");
                }
                // Miramos si contiene vocales acentuadas
                for (String vocal : VOCALES_ACENTUADAS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(vocal, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(vocal, startIndex) + 1;
                    }
                    palabraAux = palabraAux.replace(vocal, "#");
                }
                // Miramos si contiene vocales no acentuadas
                for (String vocal : VOCALES_NO_ACENTUADAS) {
                    int startIndex = 0;
                    while ((startIndex < palabraAux.length())
                            && (palabraAux.indexOf(vocal, startIndex) >= 0)) {
                        numSilabas++;
                        startIndex = palabraAux.indexOf(vocal, startIndex) + 1;
                    }
                    palabraAux = palabraAux.replace(vocal, "$");
                }

                // Cálculo de la sílaba acentuada; hay que contar los $ por
                // detrás de la # (que debería ser única si existe)
                int startIndex2 = palabraAux.indexOf('#');
                if (startIndex2 >= 0) {
                    silabaAcentuada = 1;
                } else {
                    silabaAcentuada = 0;
                }
                while (startIndex2 >= 0 && startIndex2 < palabraAux.length()
                        && (palabraAux.indexOf('$', startIndex2) >= 0)) {
                    silabaAcentuada += 1;
                    startIndex2 = palabraAux.indexOf('$', startIndex2) + 1;
                }
                result[0] = numSilabas;
                result[1] = silabaAcentuada;
                return result;

            } catch (Exception e) {
                result[0] = 0;
                result[1] = silabaAcentuada;
                return result;
            }
        } else {
            result[0] = 0;
            result[1] = silabaAcentuada;
            return result;
        }
    }

    /**
     * Calcula si la palabra ya lleva tílde en alguna de sus sílabas.
     *
     * @param palabra La palabra a comprobar.
     * @return boolean
     */
    protected static boolean estaYaAcentuada(String palabra) {

        if (palabra != null && !"".equals(palabra)) {
            boolean found = false;
            String palabraAux = palabra.toUpperCase();

            // Buscamos vocales acentuadas en la palabra.
            for (String vocal : VOCALES_ACENTUADAS) {
                found = palabraAux.contains(vocal);
                if (found) {
                    break;
                }
            }
            return found;
        } else {
            return false;
        }
    }

    /**
     * Quitamos los acentos de una palabra y la retornamos en mayusculas.
     *
     * @param palabra - Palabra a modificar
     * @return String - Palabra sin acentos y en mayusculas
     */
    public static String removeAccents(String palabra) {

        palabra = palabra.toUpperCase();

        for (int i = 0; i < VOCALES_ACENTUADAS.length; i++) {
            palabra = palabra.replace(VOCALES_ACENTUADAS[i], VOCALES_NO_ACENTUADAS[i]);
        }

        return palabra;
    }
}
