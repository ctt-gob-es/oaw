package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class RangeForm.
 */
public class RangeForm extends ValidatorForm implements Serializable, Comparable<RangeForm> {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4984235581971279444L;
	/** The id. */
	private Long id;
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
	/** The weight. */
	private Integer weight;
	/** The color. */
	private String color;

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
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangeForm other = (RangeForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int compareTo(RangeForm o) {
		return this.getWeight().compareTo(o.getWeight());
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RangeForm [id=").append(id).append(", name=").append(name).append(", minValue=").append(minValue).append(", maxValue=").append(maxValue).append(", minValueOperator=")
				.append(minValueOperator).append(", maxValueOperator=").append(maxValueOperator).append(", weight=").append(weight).append(", color=").append(color).append("]");
		return builder.toString();
	}
}
