package es.inteco.rastreador2.actionform.rastreo;


import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

public class SubiendoSemillaServidorLocalForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private FormFile url_semilla;

    public FormFile getUrl_semilla() {
        return url_semilla;
    }

    public void setUrl_semilla(FormFile url_semilla) {
        this.url_semilla = url_semilla;
    }

}