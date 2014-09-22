package es.inteco.rastreador2.actionform.categoria;


import org.apache.struts.validator.ValidatorForm;


public class NuevoTerminoCatForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String termino;
    private int id_termino;
    private String porcentaje;
    private int id_categoria;
    private String accion;


    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_termino() {
        return id_termino;
    }

    public void setId_termino(int id_termino) {
        this.id_termino = id_termino;
    }

}