package es.inteco.rastreador2.lenox.dao;

/**
 * @author jbernal
 */
public final class UtilCalculateTerms {

    private UtilCalculateTerms() {
    }

    /**
     * Calculo de Femenino.
     *
     * @param term - Termino inicial a calcular
     * @return String - Femenino del termino
     */
    public static String calculateFemale(String term) {
        //Comprobamos que tengamos valor en term
        if (term == null || term.length() == 0) {
            return "";
        }

        String terminacion;
        String termino = term.trim().toUpperCase();

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

        if (termino.length() > 1) {
            // Recuperamos la terminacion
            terminacion = termino.substring(termino.length() - 2);

            if (terminacion.equals(TerminationConstants.IE)
                    || terminacion.equals(TerminationConstants.EZ)) {
                return termino;
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

        if (termino.length() > 2) {
            // Recuperamos la terminacion
            terminacion = termino.substring(termino.length() - 3);

            if (terminacion.equals(TerminationConstants.DAD)
                    || terminacion.equals(TerminationConstants.TAD)
                    || terminacion.equals(TerminationConstants.IÓN)
                    || terminacion.equals(TerminationConstants.SIS)) {
                return "";
            }
        }

        // Terminaciones en vocal, se cambia la vocal por A
        terminacion = termino.substring(termino.length() - 1);

        if (terminacion.indexOf(TerminationConstants.VOCALES) >= 0) {
            termino = termino.substring(0, termino.length() - 1).concat(
                    TerminationConstants.A);
            return termino;
        }

        // Terminaciones en consonante, se a�ade una A al final
        return termino.concat(TerminationConstants.A);
    }

    /**
     * Calculo del plural.
     *
     * @param term - Termino inicial a calcular
     * @return String - Plural del termino
     */
    public static String calculatePlural(String term) {

        //Comprobamos que tengamos valor en term
        if (term == null || term.length() == 0) {
            return "";
        }

        String terminacion;
        String termino = term.trim().toUpperCase();

        if (termino.length() > 1) {
            terminacion = termino.substring(termino.length() - 2);

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

            if (terminacion.equals(TerminationConstants.IS)) {
                return termino;
            }
        }

        if (termino.length() > 0) {
            terminacion = termino.substring(termino.length() - 1);

            if (TerminationConstants.CONSONANTES_REGULARES.indexOf(terminacion) >= 0) {
                termino = termino.concat(TerminationConstants.ES2);
                return termino;
            }

            if (terminacion.equals(TerminationConstants.Z)) {
                termino = termino.substring(0, termino.length() - 1).concat(
                        TerminationConstants.CES);
                return termino;
            }

            if (terminacion.indexOf(TerminationConstants.IU) >= 0) {
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
     * @param term
     */
    public static void calculateSingular(String term) {
        // TODO JBERNAL: Hacer solo si al final es necesario
    }

    /**
     * Consultamos si el termino es de g�nero femenino.
     *
     * @param term - Termino consultado
     * @return boolean - Respuesta a la pregunta (true/false)
     */
    public static boolean isFemale(String term) {

        String terminacion;
        String termino;

        //Comprobamos que no llegue vacio
        if (term != null && term.length() > 0) {
            termino = term.trim().toUpperCase();

            //�triz
            if (termino.length() > 3) {
                terminacion = termino.substring(termino.length() - 4);

                if (terminacion.equals(TerminationConstants.TRIZ)) {
                    return true;
                }
            }

            //dad, -tad, -i�n, -sis
            if (termino.length() > 2) {
                terminacion = termino.substring(termino.length() - 3);

                if (terminacion.equals(TerminationConstants.DAD)
                        || terminacion.equals(TerminationConstants.TAD)
                        || terminacion.equals(TerminationConstants.IÓN)
                        || terminacion.equals(TerminationConstants.SIS)) {
                    // Es una excepcion
                    return !termino.equals("ENFASIS");
                }
            }

            //-ie, -ez
            if (termino.length() > 1) {
                terminacion = termino.substring(termino.length() - 2);

                if ((terminacion.equals(TerminationConstants.IE)
                        || terminacion.equals(TerminationConstants.EZ)
                        && !terminacion.equals(TerminationConstants.EXCP_JUEZ))) {
                    //OJO, no puede ser JUEZ, ya que es una excepcion --> JUEZA
                    return true;
                }

            }

            //Estos t�rminos terminan en -a, pero son gen�ricos
            if (termino.equals(TerminationConstants.EXCP_ARTISTA)
                    || termino.equals(TerminationConstants.EXCP_ASTRONAUTA)
                    || termino.equals(TerminationConstants.EXCP_ATLETA)
                    || termino.equals(TerminationConstants.EXCP_GUIA)
                    || termino.equals(TerminationConstants.EXCP_PERIODISTA)
                    || termino.equals(TerminationConstants.EXCP_TURISTA)
                    || termino.equals(TerminationConstants.EXCP_PIANISTA)
                    || termino.equals(TerminationConstants.EXCP_VIOLINISTA)) {
                return false;
            }

            terminacion = termino.substring(termino.length() - 1);
            if (terminacion.equals(TerminationConstants.A)) {
                return true;
            }
        }

        return false;
    }


}
