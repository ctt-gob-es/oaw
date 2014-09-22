package es.inteco.crawler.sexista.modules.commons.dto;

/**
 * Clase CommonExcepcionDto.
 *
 * @author jfiz
 */
public class CommonExcepcionDto {

    private int idExcepcion;
    private String excepcion;

    /**
     * Posición donde se aplica la excepción.
     * - 0: Al termino anterior
     * - 1: Al termino posterior
     */
    private int position;


    public int getIdExcepcion() {
        return idExcepcion;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public int getPosition() {
        return position;
    }

    public void setIdExcepcion(int idExcepcion) {
        this.idExcepcion = idExcepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
