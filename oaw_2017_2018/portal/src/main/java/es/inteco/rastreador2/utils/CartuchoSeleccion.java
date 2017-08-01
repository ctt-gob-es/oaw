package es.inteco.rastreador2.utils;

import java.io.Serializable;

public class CartuchoSeleccion implements Serializable {

    private String cartucho;
    private String aplicacion;

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

}
