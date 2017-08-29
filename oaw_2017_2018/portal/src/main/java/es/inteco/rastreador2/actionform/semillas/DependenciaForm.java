package es.inteco.rastreador2.actionform.semillas;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

/**
 * DependenciaForm. Clase para el manejo de dependencias.
 * 
 * @author alvaro.pelaez
 * 
 */
public class DependenciaForm extends ValidatorForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6360416159148020455L;

	/** The id. */
	private Long id;

	/** The name. */
	private String name;
	
	/** The nombre antiguo. */
	private String nombreAntiguo;

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
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	

	/**
	 * Gets the nombre antiguo.
	 *
	 * @return the nombre antiguo
	 */
	public String getNombreAntiguo() {
		return nombreAntiguo;
	}

	/**
	 * Sets the nombre antiguo.
	 *
	 * @param nombreAntiguo the new nombre antiguo
	 */
	public void setNombreAntiguo(String nombreAntiguo) {
		this.nombreAntiguo = nombreAntiguo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DependenciaForm [id=" + id + ", name=" + name + "]";
	}

}
