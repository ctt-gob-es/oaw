package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.action.ActionForm;

public class UpdateListDataForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String listaRastreable;
    private long idListaRastreable;
    private long idRastreableAntiguo;

    private String listaNoRastreable;
    private long idListaNoRastreable;
    private long idNoRastreableAntiguo;

    private String nombre;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getListaRastreable() {
        return listaRastreable;
    }

    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    public long getIdListaRastreable() {
        return idListaRastreable;
    }

    public void setIdListaRastreable(long idListaRastreable) {
        this.idListaRastreable = idListaRastreable;
    }

    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    public long getIdListaNoRastreable() {
        return idListaNoRastreable;
    }

    public void setIdListaNoRastreable(long idListaNoRastreable) {
        this.idListaNoRastreable = idListaNoRastreable;
    }

    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }


}