package es.oaw.wcagem;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Result.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "outcome", "description", "date" })
public class Result {
	
	/** The outcome. */
	@JsonProperty("outcome")
	private String outcome;
	
	/** The description. */
	@JsonProperty("description")
	private String description;
	
	/** The date. */
	@JsonProperty("date")
	private String date;
	
	/** The type. */
	@JsonProperty("type")
	private String type;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the outcome.
	 *
	 * @return the outcome
	 */
	@JsonProperty("outcome")
	public String getOutcome() {
		return outcome;
	}

	/**
	 * Sets the outcome.
	 *
	 * @param outcome the new outcome
	 */
	@JsonProperty("outcome")
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the additional properties.
	 *
	 * @return the additional properties
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	/**
	 * Sets the additional property.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
}
