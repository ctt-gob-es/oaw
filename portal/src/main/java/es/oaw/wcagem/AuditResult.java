
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "test",
    "assertedBy",
    "subject",
    "result",
    "mode",
    "hasPart"
})
public class AuditResult {

    @JsonProperty("type")
    private String type;
    @JsonProperty("test")
    private String test;
    @JsonProperty("assertedBy")
    private String assertedBy;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("result")
    private Result result;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("hasPart")
    private List<HasPart> hasPart = null;
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

    @JsonProperty("test")
    public String getTest() {
        return test;
    }

    @JsonProperty("test")
    public void setTest(String test) {
        this.test = test;
    }

    @JsonProperty("assertedBy")
    public String getAssertedBy() {
        return assertedBy;
    }

    @JsonProperty("assertedBy")
    public void setAssertedBy(String assertedBy) {
        this.assertedBy = assertedBy;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("result")
    public Result getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result result) {
        this.result = result;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("hasPart")
    public List<HasPart> getHasPart() {
        return hasPart;
    }

    @JsonProperty("hasPart")
    public void setHasPart(List<HasPart> hasPart) {
        this.hasPart = hasPart;
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
