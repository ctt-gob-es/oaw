package es.inteco.rastreador2.actionform.observatorio;


import org.apache.struts.action.ActionForm;

import java.util.List;

public class CargarObservatorioForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<ListadoObservatorio> listadoObservatorio;
    private int numObservatorios;

    public List<ListadoObservatorio> getListadoObservatorio() {
        return listadoObservatorio;
    }

    public void setListadoObservatorio(List<ListadoObservatorio> listadoCuentasUsuario) {
        this.listadoObservatorio = listadoCuentasUsuario;
    }

    public int getNumObservatorios() {
        return numObservatorios;
    }

    public void setNumObservatorios(int numObservatorios) {
        this.numObservatorios = numObservatorios;
    }

}