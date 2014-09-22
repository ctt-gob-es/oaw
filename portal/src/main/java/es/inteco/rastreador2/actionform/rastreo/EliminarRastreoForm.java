package es.inteco.rastreador2.actionform.rastreo;


import org.apache.struts.action.ActionForm;


public class EliminarRastreoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String codigo, fecha, cartucho, idrastreo, normaAnalisis;


    public String getIdrastreo() {
        return idrastreo;
    }

    public void setIdrastreo(String idrastreo) {
        this.idrastreo = idrastreo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

}