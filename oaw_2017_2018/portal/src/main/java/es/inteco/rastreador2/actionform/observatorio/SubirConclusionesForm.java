package es.inteco.rastreador2.actionform.observatorio;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

public class SubirConclusionesForm extends ValidatorForm {
    private FormFile file;

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

}
