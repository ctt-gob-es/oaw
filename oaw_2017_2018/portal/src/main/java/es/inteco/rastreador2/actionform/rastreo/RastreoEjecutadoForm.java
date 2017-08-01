package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

public class RastreoEjecutadoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private long id_rastreo;
    private long id_ejecucion;
    private String nombre_rastreo;
    private String fecha;
    private long id_cartucho;

    public long getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(long id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public long getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(long id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public long getId_ejecucion() {
        return id_ejecucion;
    }

    public void setId_ejecucion(long id_ejecucion) {
        this.id_ejecucion = id_ejecucion;
    }

    public String getNombre_rastreo() {
        return nombre_rastreo;
    }

    public void setNombre_rastreo(String nombre_rastreo) {
        this.nombre_rastreo = nombre_rastreo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
