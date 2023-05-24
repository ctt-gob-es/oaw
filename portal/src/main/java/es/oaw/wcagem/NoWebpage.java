package es.oaw.wcagem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Webpage.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "id", "title", "tested" })
public class NoWebpage {
	
	/** The type. */
	@JsonProperty("type")
	private List<String> type = null;
	
	/** The id. */
	@JsonProperty("id")
	private String id;
	
	/** The title. */
	@JsonProperty("title")
	private String title;
	
	/** The tested. */
	@JsonProperty("tested")
	private Boolean tested;
	
	/** The description. */
	@JsonProperty("description")
	private String description;
	
	/** The source. */
	@JsonProperty("source")
	private String source;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@JsonProperty("type")
	public List<String> getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	@JsonProperty("type")
	public void setType(List<String> type) {
		this.type = type;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the tested.
	 *
	 * @return the tested
	 */
	@JsonProperty("tested")
	public Boolean getTested() {
		return tested;
	}

	/**
	 * Sets the tested.
	 *
	 * @param tested the new tested
	 */
	@JsonProperty("tested")
	public void setTested(Boolean tested) {
		this.tested = tested;
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
	 * Gets the source.
	 *
	 * @return the source
	 */
	@JsonProperty("source")
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
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
}
