package es.inteco.rastreador2.actionform.categoria;


import org.apache.struts.validator.ValidatorForm;


public class NuevaCategoriaForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    private String umbral;
    private String nombre;
    private String termino1;
    private String peso1;
    private String termino2;
    private String peso2;
    private String termino3;
    private String peso3;
    private String termino4;
    private String peso4;

    public String getUmbral() {
        return umbral;
    }

    public void setUmbral(String umbral) {
        this.umbral = umbral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTermino1() {
        return termino1;
    }

    public void setTermino1(String termino1) {
        this.termino1 = termino1;
    }

    public String getPeso1() {
        return peso1;
    }

    public void setPeso1(String peso1) {
        this.peso1 = peso1;
    }

    public String getTermino2() {
        return termino2;
    }

    public void setTermino2(String termino2) {
        this.termino2 = termino2;
    }

    public String getPeso2() {
        return peso2;
    }

    public void setPeso2(String peso2) {
        this.peso2 = peso2;
    }

    public String getTermino3() {
        return termino3;
    }

    public void setTermino3(String termino3) {
        this.termino3 = termino3;
    }

    public String getPeso3() {
        return peso3;
    }

    public void setPeso3(String peso3) {
        this.peso3 = peso3;
    }

    public String getTermino4() {
        return termino4;
    }

    public void setTermino4(String termino4) {
        this.termino4 = termino4;
    }

    public String getPeso4() {
        return peso4;
    }

    public void setPeso4(String peso4) {
        this.peso4 = peso4;
    }


}