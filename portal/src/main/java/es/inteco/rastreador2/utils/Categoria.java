package es.inteco.rastreador2.utils;

public class Categoria {
    private String nombre;
    private float umbral;
    private int terminos;
    private int id_categoria;


    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getUmbral() {
        return umbral;
    }

    public void setUmbral(float umbral) {
        this.umbral = umbral;
    }

    public int getTerminos() {
        return terminos;
    }

    public void setTerminos(int terminos) {
        this.terminos = terminos;
    }

}
