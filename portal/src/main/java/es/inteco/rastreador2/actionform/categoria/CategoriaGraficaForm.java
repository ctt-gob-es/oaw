package es.inteco.rastreador2.actionform.categoria;


public class CategoriaGraficaForm {

    private String nombre;
    private String identificador;


    public CategoriaGraficaForm(String nombre, String identificador) {
        this.nombre = nombre;
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }


}
