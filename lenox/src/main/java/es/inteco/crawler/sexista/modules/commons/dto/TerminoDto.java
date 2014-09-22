package es.inteco.crawler.sexista.modules.commons.dto;

/**
 * Clase TerminoDto.
 *
 * @author jbernal
 */
public class TerminoDto {

    /**
     * Indetificador del termino.
     */
    private int idTermino;

    /**
     * Termino.
     */
    private String termino;

    /**
     * Boolenano que define si usamos excepciones globales para este termino.
     */
    private boolean usaGlobalException;

    /**
     * Es el femenino del término, si es que es irregular.
     */
    private String femenino;

    /**
     * Es el plural del término, si es que es irregular.
     */
    private String plural;

    /**
     * Es el femenino-plural del término, si es que es irregular.
     */
    private String femeninoPlural;

    /**
     * Define si el termino posee forma única
     */
    private boolean formaUnica;

    /**
     * Identificador de la categoria.
     */
    private long idCategoria;

    /**
     * Prioridad del término en singular
     */
    private int prioridadSingular;

    /**
     * Prioridad del término en plural
     */
    private int prioridadPlural;

    /**
     * Obtiene el valor del campo idTermino.<br>
     *
     * @return el campo idTermino
     */
    public int getIdTermino() {
        return idTermino;
    }

    /**
     * Obtiene el valor del campo termino.<br>
     *
     * @return el campo termino
     */
    public String getTermino() {
        return termino;
    }

    /**
     * Obtiene el valor del campo usaGlobalException.<br>
     *
     * @return el campo usaGlobalException
     */
    public boolean isUsaGlobalException() {
        return usaGlobalException;
    }

    /**
     * Obtiene el valor del campo femenino.<br>
     *
     * @return el campo femenino
     */
    public String getFemenino() {
        return femenino;
    }

    /**
     * Obtiene el valor del campo plural.<br>
     *
     * @return el campo plural
     */
    public String getPlural() {
        return plural;
    }

    /**
     * Obtiene el valor del campo femeninoPlural.<br>
     *
     * @return el campo femeninoPlural
     */
    public String getFemeninoPlural() {
        return femeninoPlural;
    }

    /**
     * Fija el valor para el campo idTermino.<br>
     *
     * @param idTermino el vlor de idTermino a fijar
     */
    public void setIdTermino(int idTermino) {
        this.idTermino = idTermino;
    }

    /**
     * Fija el valor para el campo termino.<br>
     *
     * @param termino el vlor de termino a fijar
     */
    public void setTermino(String termino) {
        this.termino = termino;
    }

    /**
     * Fija el valor para el campo usaGlobalException.<br>
     *
     * @param usaGlobalException el vlor de usaGlobalException a fijar
     */
    public void setUsaGlobalException(boolean usaGlobalException) {
        this.usaGlobalException = usaGlobalException;
    }

    /**
     * Fija el valor para el campo femenino.<br>
     *
     * @param femenino el vlor de femenino a fijar
     */
    public void setFemenino(String femenino) {
        this.femenino = femenino;
    }

    /**
     * Fija el valor para el campo plural.<br>
     *
     * @param plural el vlor de plural a fijar
     */
    public void setPlural(String plural) {
        this.plural = plural;
    }

    /**
     * Fija el valor para el campo femeninoPlural.<br>
     *
     * @param femeninoPlural el vlor de femeninoPlural a fijar
     */
    public void setFemeninoPlural(String femeninoPlural) {
        this.femeninoPlural = femeninoPlural;
    }

    public boolean isFormaUnica() {
        return formaUnica;
    }

    public void setFormaUnica(boolean formaUnica) {
        this.formaUnica = formaUnica;
    }

    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getPrioridadSingular() {
        return prioridadSingular;
    }

    public void setPrioridadSingular(int prioridadSingular) {
        this.prioridadSingular = prioridadSingular;
    }

    public int getPrioridadPlural() {
        return prioridadPlural;
    }

    public void setPrioridadPlural(int prioridadPlural) {
        this.prioridadPlural = prioridadPlural;
    }
}
