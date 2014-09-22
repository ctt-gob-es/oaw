package es.inteco.crawler.sexista.modules.informes.dto;

import es.inteco.crawler.sexista.core.dto.impl.BaseDto;

import java.util.List;

/**
 * Dto de Detalle.
 *
 * @author psanchez
 */

public class DetalleDto extends BaseDto {

    private RastreoExtDto rastreo;
    private List<TerminoDetalleDto> detalleTerminos;
    private String gravedad;


    public List<TerminoDetalleDto> getDetalleTerminos() {
        return detalleTerminos;
    }

    public RastreoExtDto getRastreo() {
        return rastreo;
    }

    public void setDetalleTerminos(List<TerminoDetalleDto> detalleTerminos) {
        this.detalleTerminos = detalleTerminos;
    }

    public void setRastreo(RastreoExtDto rastreo) {
        this.rastreo = rastreo;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }


}
