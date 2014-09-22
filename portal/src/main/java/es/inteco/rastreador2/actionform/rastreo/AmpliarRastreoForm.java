package es.inteco.rastreador2.actionform.rastreo;


import org.apache.struts.action.ActionForm;


public class AmpliarRastreoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String cod_rastreo;
    private String opcion_ampliar;
    private String nombre;
    private String bajadas;
    private String estado;
    private String pid;
    private String slot;
    private String user;
    private String pass;
    private String ultima_url;


    public String getUltima_url() {
        return ultima_url;
    }

    public void setUltima_url(String ultima_url) {
        this.ultima_url = ultima_url;
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

    public String getCod_rastreo() {
        return cod_rastreo;
    }

    public void setCod_rastreo(String cod_rastreo) {
        this.cod_rastreo = cod_rastreo;
    }

    public String getOpcion_ampliar() {
        return opcion_ampliar;
    }

    public void setOpcion_ampliar(String opcion_ampliar) {
        this.opcion_ampliar = opcion_ampliar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBajadas() {
        return bajadas;
    }

    public void setBajadas(String bajadas) {
        this.bajadas = bajadas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }


}