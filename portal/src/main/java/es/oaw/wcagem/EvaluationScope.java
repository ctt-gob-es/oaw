
package es.oaw.wcagem;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "conformanceTarget",
    "additionalEvalRequirement",
    "website",
    "accessibilitySupportBaseline"
})
public class EvaluationScope {

    @JsonProperty("type")
    private String type;
    @JsonProperty("conformanceTarget")
    private String conformanceTarget;
    @JsonProperty("additionalEvalRequirement")
    private String additionalEvalRequirement;
    @JsonProperty("website")
    private Website website;
    @JsonProperty("accessibilitySupportBaseline")
    private String accessibilitySupportBaseline;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("conformanceTarget")
    public String getConformanceTarget() {
        return conformanceTarget;
    }

    @JsonProperty("conformanceTarget")
    public void setConformanceTarget(String conformanceTarget) {
        this.conformanceTarget = conformanceTarget;
    }

    @JsonProperty("additionalEvalRequirement")
    public String getAdditionalEvalRequirement() {
        return additionalEvalRequirement;
    }

    @JsonProperty("additionalEvalRequirement")
    public void setAdditionalEvalRequirement(String additionalEvalRequirement) {
        this.additionalEvalRequirement = additionalEvalRequirement;
    }

    @JsonProperty("website")
    public Website getWebsite() {
        return website;
    }

    @JsonProperty("website")
    public void setWebsite(Website website) {
        this.website = website;
    }

    @JsonProperty("accessibilitySupportBaseline")
    public String getAccessibilitySupportBaseline() {
        return accessibilitySupportBaseline;
    }

    @JsonProperty("accessibilitySupportBaseline")
    public void setAccessibilitySupportBaseline(String accessibilitySupportBaseline) {
        this.accessibilitySupportBaseline = accessibilitySupportBaseline;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
