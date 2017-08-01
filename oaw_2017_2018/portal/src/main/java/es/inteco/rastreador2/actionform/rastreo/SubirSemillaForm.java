package es.inteco.rastreador2.actionform.rastreo;


import org.apache.struts.action.ActionForm;

import java.util.List;

public class SubirSemillaForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<String> semillas;
    private String semilla;

    public List<String> getSemillas() {
        return semillas;
    }

    public void setSemillas(List<String> semillas) {
        this.semillas = semillas;
    }

    public String getSemilla() {
        return semilla;
    }

    public void setSemilla(String semilla) {
        this.semilla = semilla;
    }

}
