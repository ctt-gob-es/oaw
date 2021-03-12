package es.inteco.rastreador2.actionform.semillas;

import java.io.Serializable;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 * The Class PlantillaForm.
 */
public class PlantillaForm extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5532039277670782238L;
	/** The id. */
	private Long id;
	/** The nombre. */
	private String nombre;
	/** The documento. */
	private byte[] documento;
	/** The file. */
	private FormFile file;
	/** The type. */
	private String type;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the documento.
	 *
	 * @return the documento
	 */
	public byte[] getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento.
	 *
	 * @param documento the documento to set
	 */
	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public FormFile getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
