package es.inteco.rastreador2.actionform.semillas;


import org.apache.struts.validator.ValidatorForm;

public class ComplejidadForm extends ValidatorForm {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}