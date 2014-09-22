package es.inteco.rastreador2.lenox.form;

import org.apache.struts.action.ActionForm;

public class InformesSearchForm extends ActionForm {
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
