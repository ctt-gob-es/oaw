package es.inteco.rastreador2.actionform.comun;


import org.apache.struts.action.ActionForm;


public class RedireccionConfigForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String proyecto;
    private String user;
    private String tipo;
    private int numCartuchos;
    private int seleccionados;
    private String pass;
    private int id_cartucho;


    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumCartuchos() {
        return numCartuchos;
    }

    public void setNumCartuchos(int numCartuchos) {
        this.numCartuchos = numCartuchos;
    }

    public int getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(int seleccionados) {
        this.seleccionados = seleccionados;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}