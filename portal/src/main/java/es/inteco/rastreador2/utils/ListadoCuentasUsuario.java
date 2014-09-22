package es.inteco.rastreador2.utils;

import java.io.Serializable;
import java.util.List;

public class ListadoCuentasUsuario implements Serializable {
    private String nombreCuenta;
    private int id_cartucho;
    private List<String> dominio;
    private List<String> cartucho;
    private Long id_cuenta;

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public List<String> getCartucho() {
        return cartucho;
    }

    public void setCartucho(List<String> cartucho) {
        this.cartucho = cartucho;
    }

    public List<String> getDominio() {
        return dominio;
    }

    public void setDominio(List<String> dominio) {
        this.dominio = dominio;
    }

    public Long getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(Long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

}
