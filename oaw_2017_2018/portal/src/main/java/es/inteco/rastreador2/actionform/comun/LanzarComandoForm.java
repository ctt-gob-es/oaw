package es.inteco.rastreador2.actionform.comun;


import org.apache.struts.action.ActionForm;


public class LanzarComandoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String user;
    private String pass;
    private String rastreo;
    private String comando;
    private String estado_antiguo;
    private String lanzarParar;
    private String lanzandoParando;


    public String getLanzarParar() {
        return lanzarParar;
    }

    public void setLanzarParar(String lanzarParar) {
        this.lanzarParar = lanzarParar;
    }

    public String getLanzandoParando() {
        return lanzandoParando;
    }

    public void setLanzandoParando(String lanzandoParando) {
        this.lanzandoParando = lanzandoParando;
    }

    public String getEstado_antiguo() {
        return estado_antiguo;
    }

    public void setEstado_antiguo(String estado_antiguo) {
        this.estado_antiguo = estado_antiguo;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getRastreo() {
        return rastreo;
    }

    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}