package es.gob.oaw.w3cvalidator;

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
 * The Class W3CValidatorResponse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "messages" })
public class W3CValidatorResponse {
	
	/** The messages. */
	@JsonProperty("messages")
	private List<Message> messages = null;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	@JsonProperty("messages")
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Sets the messages.
	 *
	 * @param messages the new messages
	 */
	@JsonProperty("messages")
	public void setMessages(List<Message> messages) {
		this.messages = messages;
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