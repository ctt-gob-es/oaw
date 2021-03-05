package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

/**
 * The Class UraCustomTextForm.
 */
public class UraCustomTextForm extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5141470638405603314L;
	/** The id. */
	private Long id;
	/** The id observatory execution. */
	private Long idObservatoryExecution;
	/** The id ura. */
	private Long idUra;
	/** The id range. */
	private Long idRange;
	/** The template. */
	private String template;
	/** The ura. */
	private DependenciaForm ura;
	/** The range. */
	private RangeForm range;
	/** The ura name. */
	private String uraName;
	/** The ura id. */
	private Long uraId;
	/** The range value. */
	private Float rangeValue;

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
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the id observatory execution.
	 *
	 * @return the id observatory execution
	 */
	public Long getIdObservatoryExecution() {
		return idObservatoryExecution;
	}

	/**
	 * Sets the id observatory execution.
	 *
	 * @param idObservatoryExecution the new id observatory execution
	 */
	public void setIdObservatoryExecution(Long idObservatoryExecution) {
		this.idObservatoryExecution = idObservatoryExecution;
	}

	/**
	 * Gets the id ura.
	 *
	 * @return the id ura
	 */
	public Long getIdUra() {
		return idUra;
	}

	/**
	 * Sets the id ura.
	 *
	 * @param idUra the new id ura
	 */
	public void setIdUra(Long idUra) {
		this.idUra = idUra;
	}

	/**
	 * Gets the id range.
	 *
	 * @return the id range
	 */
	public Long getIdRange() {
		return idRange;
	}

	/**
	 * Sets the id range.
	 *
	 * @param idRange the new id range
	 */
	public void setIdRange(Long idRange) {
		this.idRange = idRange;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Gets the ura.
	 *
	 * @return the ura
	 */
	public DependenciaForm getUra() {
		return ura;
	}

	/**
	 * Sets the ura.
	 *
	 * @param ura the new ura
	 */
	public void setUra(DependenciaForm ura) {
		this.ura = ura;
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public RangeForm getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(RangeForm range) {
		this.range = range;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the ura name.
	 *
	 * @return the ura name
	 */
	public String getUraName() {
		return uraName;
	}

	/**
	 * Sets the ura name.
	 *
	 * @param uraName the new ura name
	 */
	public void setUraName(String uraName) {
		this.uraName = uraName;
	}

	/**
	 * Gets the ura id.
	 *
	 * @return the ura id
	 */
	public Long getUraId() {
		return uraId;
	}

	/**
	 * Sets the ura id.
	 *
	 * @param uraId the new ura id
	 */
	public void setUraId(Long uraId) {
		this.uraId = uraId;
	}

	/**
	 * Gets the range value.
	 *
	 * @return the range value
	 */
	public Float getRangeValue() {
		return rangeValue;
	}

	/**
	 * Sets the range value.
	 *
	 * @param rangeValue the new range value
	 */
	public void setRangeValue(Float rangeValue) {
		this.rangeValue = rangeValue;
	}
}
