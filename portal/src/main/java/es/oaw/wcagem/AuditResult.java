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
 * The Class AuditResult.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "test", "assertedBy", "subject", "result", "mode", "hasPart" })
public class AuditResult {
	/** The type. */
	@JsonProperty("type")
	private String type;
	/** The test. */
	@JsonProperty("test")
	private String test;
	/** The asserted by. */
	@JsonProperty("assertedBy")
	private String assertedBy;
	/** The subject. */
	@JsonProperty("subject")
	private String subject;
	/** The result. */
	@JsonProperty("result")
	private Result result;
	/** The mode. */
	@JsonProperty("mode")
	private String mode;
	/** The mode. */
	@JsonProperty("webAudit")
	private boolean webAudit;
	/** The has part. */
	@JsonProperty("hasPart")
	private List<HasPart> hasPart = null;
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	/**
	 * Gets the test.
	 *
	 * @return the test
	 */
	@JsonProperty("test")
	public String getTest() {
		return test;
	}

	/**
	 * Sets the test.
	 *
	 * @param test the new test
	 */
	@JsonProperty("test")
	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * Gets the asserted by.
	 *
	 * @return the asserted by
	 */
	@JsonProperty("assertedBy")
	public String getAssertedBy() {
		return assertedBy;
	}

	/**
	 * Sets the asserted by.
	 *
	 * @param assertedBy the new asserted by
	 */
	@JsonProperty("assertedBy")
	public void setAssertedBy(String assertedBy) {
		this.assertedBy = assertedBy;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	@JsonProperty("subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	@JsonProperty("result")
	public Result getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	@JsonProperty("result")
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	@JsonProperty("mode")
	public String getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	@JsonProperty("mode")
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Gets the checks for part.
	 *
	 * @return the checks for part
	 */
	@JsonProperty("hasPart")
	public List<HasPart> getHasPart() {
		return hasPart;
	}

	/**
	 * Sets the checks for part.
	 *
	 * @param hasPart the new checks for part
	 */
	@JsonProperty("hasPart")
	public void setHasPart(List<HasPart> hasPart) {
		this.hasPart = hasPart;
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

	@JsonProperty("webAudit")
	public boolean isWebAudit() {
		return webAudit;
	}

	@JsonProperty("webAudit")
	public void setWebAudit(boolean webAudit) {
		this.webAudit = webAudit;
	}
}
