package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

public class ObservatoryTypeForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
