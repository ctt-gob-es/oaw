package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class TemplateRengeForm.
 */
public class UraSendHistoricRange extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8269010095675845298L;
	/** The id. */
	private Long id;
	/** The id observatory execution. */
	private Long idSendHistoric;
	/** The name. */
	private String name;
	/** The min value. */
	private Float minValue;
	/** The max value. */
	private Float maxValue;
	/** The min value operator. */
	private String minValueOperator;
	/** The max value operator. */
	private String maxValueOperator;
	/** The min position value. */
	private Float minPositionValue;
	/** The max position value. */
	private Float maxPositionValue;
	/** The min position value operator. */
	private String minPositionValueOperator;
	/** The max position value operator. */
	private String maxPositionValueOperator;
	/** The template. */
	private String template;

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
	 * Gets the id send historic.
	 *
	 * @return the id send historic
	 */
	public Long getIdSendHistoric() {
		return idSendHistoric;
	}

	/**
	 * Sets the id send historic.
	 *
	 * @param idSendHistoric the new id send historic
	 */
	public void setIdSendHistoric(Long idSendHistoric) {
		this.idSendHistoric = idSendHistoric;
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
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the min value.
	 *
	 * @return the min value
	 */
	public Float getMinValue() {
		return minValue;
	}

	/**
	 * Sets the min value.
	 *
	 * @param minValue the new min value
	 */
	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}

	/**
	 * Gets the max value.
	 *
	 * @return the max value
	 */
	public Float getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the max value.
	 *
	 * @param maxValue the new max value
	 */
	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * Gets the min value operator.
	 *
	 * @return the min value operator
	 */
	public String getMinValueOperator() {
		return minValueOperator;
	}

	/**
	 * Sets the min value operator.
	 *
	 * @param minValueOperator the new min value operator
	 */
	public void setMinValueOperator(String minValueOperator) {
		this.minValueOperator = minValueOperator;
	}

	/**
	 * Gets the max value operator.
	 *
	 * @return the max value operator
	 */
	public String getMaxValueOperator() {
		return maxValueOperator;
	}

	/**
	 * Sets the max value operator.
	 *
	 * @param maxValueOperator the new max value operator
	 */
	public void setMaxValueOperator(String maxValueOperator) {
		this.maxValueOperator = maxValueOperator;
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
	 * Gets the min position value.
	 *
	 * @return the min position value
	 */
	public Float getMinPositionValue() {
		return minPositionValue;
	}

	/**
	 * Sets the min position value.
	 *
	 * @param minPositionValue the new min position value
	 */
	public void setMinPositionValue(Float minPositionValue) {
		this.minPositionValue = minPositionValue;
	}

	/**
	 * Gets the max position value.
	 *
	 * @return the max position value
	 */
	public Float getMaxPositionValue() {
		return maxPositionValue;
	}

	/**
	 * Sets the max position value.
	 *
	 * @param maxPositionValue the new max position value
	 */
	public void setMaxPositionValue(Float maxPositionValue) {
		this.maxPositionValue = maxPositionValue;
	}

	/**
	 * Gets the min position value operator.
	 *
	 * @return the min position value operator
	 */
	public String getMinPositionValueOperator() {
		return minPositionValueOperator;
	}

	/**
	 * Sets the min position value operator.
	 *
	 * @param minPositionValueOperator the new min position value operator
	 */
	public void setMinPositionValueOperator(String minPositionValueOperator) {
		this.minPositionValueOperator = minPositionValueOperator;
	}

	/**
	 * Gets the max position value operator.
	 *
	 * @return the max position value operator
	 */
	public String getMaxPositionValueOperator() {
		return maxPositionValueOperator;
	}

	/**
	 * Sets the max position value operator.
	 *
	 * @param maxPositionValueOperator the new max position value operator
	 */
	public void setMaxPositionValueOperator(String maxPositionValueOperator) {
		this.maxPositionValueOperator = maxPositionValueOperator;
	}
}
