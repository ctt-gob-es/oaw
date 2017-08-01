package es.inteco.rastreador2.actionform.semillas;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

public class DependenciaForm extends ValidatorForm implements Serializable {

	private static final long serialVersionUID = 6360416159148020455L;
	
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
