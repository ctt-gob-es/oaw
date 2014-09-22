package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

public class LenguajeForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String keyName;
    private String codice;

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

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }


}
