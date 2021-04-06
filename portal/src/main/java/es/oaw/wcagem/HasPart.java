
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
 * The Class HasPart.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "assertedBy",
    "subject",
    "result",
    "multiPage",
    "mode",
    "testcase"
})
public class HasPart {

    /** The type. */
    @JsonProperty("type")
    private String type;
    
    /** The asserted by. */
    @JsonProperty("assertedBy")
    private String assertedBy;
    
    /** The subject. */
    @JsonProperty("subject")
    private List<String> subject = null;
    
    /** The result. */
    @JsonProperty("result")
    private Result_ result;
    
    /** The multi page. */
    @JsonProperty("multiPage")
    private Boolean multiPage;
    
    /** The mode. */
    @JsonProperty("mode")
    private String mode;
    
    /** The testcase. */
    @JsonProperty("testcase")
    private String testcase;
    
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
    public List<String> getSubject() {
        return subject;
    }

    /**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
    @JsonProperty("subject")
    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    /**
	 * Gets the result.
	 *
	 * @return the result
	 */
    @JsonProperty("result")
    public Result_ getResult() {
        return result;
    }

    /**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
    @JsonProperty("result")
    public void setResult(Result_ result) {
        this.result = result;
    }

    /**
	 * Gets the multi page.
	 *
	 * @return the multi page
	 */
    @JsonProperty("multiPage")
    public Boolean getMultiPage() {
        return multiPage;
    }

    /**
	 * Sets the multi page.
	 *
	 * @param multiPage the new multi page
	 */
    @JsonProperty("multiPage")
    public void setMultiPage(Boolean multiPage) {
        this.multiPage = multiPage;
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
	 * Gets the testcase.
	 *
	 * @return the testcase
	 */
    @JsonProperty("testcase")
    public String getTestcase() {
        return testcase;
    }

    /**
	 * Sets the testcase.
	 *
	 * @param testcase the new testcase
	 */
    @JsonProperty("testcase")
    public void setTestcase(String testcase) {
        this.testcase = testcase;
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
