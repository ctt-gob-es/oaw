package es.inteco.rastreador2.actionform.cuentausuario;


import org.apache.struts.action.ActionForm;

public class PeriodicidadForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private long id;
    private String nombre;
    private String cronExpression;
    private int dias;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
}