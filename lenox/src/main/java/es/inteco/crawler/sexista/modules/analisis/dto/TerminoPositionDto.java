package es.inteco.crawler.sexista.modules.analisis.dto;

import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto extendido de terminoDto con la posición donde aparecen en el contenido analizado.
 */
public class TerminoPositionDto extends TerminoDto {

    /**
     * Posiciones donde aparece en el texto.
     */
    List<Integer> positions = new ArrayList<Integer>();

    /**
     * Indica si encontramos el termino en singular o en plural
     */
    boolean foundInSingular = false;

    /**
     * Obtiene el valor del campo positions.<br>
     *
     * @return el campo positions
     */
    public List<Integer> getPositions() {
        return positions;
    }

    /**
     * Fija el valor para el campo positions.<br>
     *
     * @param positions el vlor de positions a fijar
     */
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public boolean isFoundInSingular() {
        return foundInSingular;
    }

    public void setFoundInSingular(boolean foundInSingular) {
        this.foundInSingular = foundInSingular;
    }
}
