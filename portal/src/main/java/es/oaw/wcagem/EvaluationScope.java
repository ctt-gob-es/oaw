
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
 * The Class EvaluationScope.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "conformanceTarget",
    "additionalEvalRequirement",
    "website",
    "accessibilitySupportBaseline"
})
public class EvaluationScope {

    /** The type. */
    @JsonProperty("type")
    private String type;
    
    /** The conformance target. */
    @JsonProperty("conformanceTarget")
    private String conformanceTarget;
    
    /** The additional eval requirement. */
    @JsonProperty("additionalEvalRequirement")
    private String additionalEvalRequirement;
    
    /** The website. */
    @JsonProperty("website")
    private Website website;
    
    /** The accessibility support baseline. */
    @JsonProperty("accessibilitySupportBaseline")
    private String accessibilitySupportBaseline;
    
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
	 * Gets the conformance target.
	 *
	 * @return the conformance target
	 */
    @JsonProperty("conformanceTarget")
    public String getConformanceTarget() {
        return conformanceTarget;
    }

    /**
	 * Sets the conformance target.
	 *
	 * @param conformanceTarget the new conformance target
	 */
    @JsonProperty("conformanceTarget")
    public void setConformanceTarget(String conformanceTarget) {
        this.conformanceTarget = conformanceTarget;
    }

    /**
	 * Gets the additional eval requirement.
	 *
	 * @return the additional eval requirement
	 */
    @JsonProperty("additionalEvalRequirement")
    public String getAdditionalEvalRequirement() {
        return additionalEvalRequirement;
    }

    /**
	 * Sets the additional eval requirement.
	 *
	 * @param additionalEvalRequirement the new additional eval requirement
	 */
    @JsonProperty("additionalEvalRequirement")
    public void setAdditionalEvalRequirement(String additionalEvalRequirement) {
        this.additionalEvalRequirement = additionalEvalRequirement;
    }

    /**
	 * Gets the website.
	 *
	 * @return the website
	 */
    @JsonProperty("website")
    public Website getWebsite() {
        return website;
    }

    /**
	 * Sets the website.
	 *
	 * @param website the new website
	 */
    @JsonProperty("website")
    public void setWebsite(Website website) {
        this.website = website;
    }

    /**
	 * Gets the accessibility support baseline.
	 *
	 * @return the accessibility support baseline
	 */
    @JsonProperty("accessibilitySupportBaseline")
    public String getAccessibilitySupportBaseline() {
        return accessibilitySupportBaseline;
    }

    /**
	 * Sets the accessibility support baseline.
	 *
	 * @param accessibilitySupportBaseline the new accessibility support baseline
	 */
    @JsonProperty("accessibilitySupportBaseline")
    public void setAccessibilitySupportBaseline(String accessibilitySupportBaseline) {
        this.accessibilitySupportBaseline = accessibilitySupportBaseline;
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
