package es.inteco.rastreador2.utils;

public class LineaCartuchos {

    private String nombre, aplicacion, instalado, id_cartucho;
    private int numRastreos, numHilos;

    public int getNumRastreos() {
        return numRastreos;
    }

    public void setNumRastreos(int numRastreos) {
        this.numRastreos = numRastreos;
    }

    public int getNumHilos() {
        return numHilos;
    }

    public void setNumHilos(int numHilos) {
        this.numHilos = numHilos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getInstalado() {
        return instalado;
    }

    public void setInstalado(String instalado) {
        this.instalado = instalado;
    }

    public String getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(String id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

}
