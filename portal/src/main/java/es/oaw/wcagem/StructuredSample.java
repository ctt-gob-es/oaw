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
 * The Class StructuredSample.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "webpage" })
public class StructuredSample {
	/** The webpage. */
	@JsonProperty("webpage")
	private List<Webpage> webpage = null;
	/** The webpage. */
	@JsonProperty("nowebpage")
	private List<NoWebpage> nowebpage = null;
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the webpage.
	 *
	 * @return the webpage
	 */
	@JsonProperty("webpage")
	public List<Webpage> getWebpage() {
		return webpage;
	}

	/**
	 * Sets the webpage.
	 *
	 * @param webpage the new webpage
	 */
	@JsonProperty("webpage")
	public void setWebpage(List<Webpage> webpage) {
		this.webpage = webpage;
	}

	/**
	 * Gets the webpage.
	 *
	 * @return the webpage
	 */
	@JsonProperty("nowebpage")
	public List<NoWebpage> getNoWebpage() {
		return nowebpage;
	}

	/**
	 * Sets the webpage.
	 *
	 * @param webpage the new webpage
	 */
	@JsonProperty("nowebpage")
	public void setNoWebpage(List<NoWebpage> nowebpage) {
		this.nowebpage = nowebpage;
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
