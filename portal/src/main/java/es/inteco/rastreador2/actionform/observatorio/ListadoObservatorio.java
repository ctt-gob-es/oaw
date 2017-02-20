package es.inteco.rastreador2.actionform.observatorio;

import java.util.List;

public class ListadoObservatorio {

    private String nombreObservatorio;
    private List<String> dominio;
    private Long id_observatorio;
    private long id_cartucho;
    private String cartucho;
    private String tipo;

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getNombreObservatorio() {
        return nombreObservatorio;
    }

    public void setNombreObservatorio(String nombreObservatorio) {
        this.nombreObservatorio = nombreObservatorio;
    }

    public List<String> getDominio() {
        return dominio;
    }

    public void setDominio(List<String> dominio) {
        this.dominio = dominio;
    }

    public Long getId_observatorio() {
        return id_observatorio;
    }

    public void setId_observatorio(Long id_observatorio) {
        this.id_observatorio = id_observatorio;
    }

    public long getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(long id_cartucho) {
        this.id_cartucho = id_cartucho;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
