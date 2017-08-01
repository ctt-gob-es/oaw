package es.inteco.rastreador2.utils;

import java.io.Serializable;
import java.util.List;

public class ListadoUsuario implements Serializable {
    private String usuario;
    private int id_cartucho;
    private List cartucho;
    private List tipo;
    private int tipoRol;
    private Long id_usuario;


    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public List getCartucho() {
        return cartucho;
    }

    public void setCartucho(List cartucho) {
        this.cartucho = cartucho;
    }

    public List getTipo() {
        return tipo;
    }

    public void setTipo(List tipo) {
        this.tipo = tipo;
    }

    public int getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(int tipoRol) {
        this.tipoRol = tipoRol;
    }
}
