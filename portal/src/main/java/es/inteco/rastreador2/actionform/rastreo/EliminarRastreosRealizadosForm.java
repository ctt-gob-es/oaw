package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.validator.ValidatorForm;

public class EliminarRastreosRealizadosForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String[] select;

    public String[] getSelect() {
        return select;
    }

    public void setSelect(String[] select) {
        this.select = select;
    }

}