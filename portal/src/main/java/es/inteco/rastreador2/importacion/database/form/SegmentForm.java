package es.inteco.rastreador2.importacion.database.form;

public class SegmentForm extends BaseForm {
	private Long orden;
	private String clave;
	private Long principal;

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Long getPrincipal() {
		return principal;
	}

	public void setPrincipal(Long principal) {
		this.principal = principal;
	}
}
