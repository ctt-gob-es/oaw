package es.inteco.rastreador2.actionform.importation;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

public class ImportEntitiesForm extends ValidatorForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private FormFile file;

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	/**
	 * Validate the campo file is a json file and return an <code>ActionErrors</code> object that encapsulates any validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 * @return <code>ActionErrors</code> object that encapsulates any validation errors
	 */
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		errors = super.validate(actionMapping, request);
		if ((this.getFile() != null) && (this.getFile().getFileSize() > 0))
			if ("application/json" != getFile().getContentType()) {
				if (!this.getFile().getFileName().contains(".json"))
					errors.add("errorObligatorios", new ActionMessage("subir.fichero.import.campo.file.invalido"));
			}
		return errors;
	}
}
