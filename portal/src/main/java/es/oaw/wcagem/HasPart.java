
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
    "assertedBy",
    "subject",
    "result",
    "multiPage",
    "mode",
    "testcase"
})
public class HasPart {

    @JsonProperty("type")
    private String type;
    @JsonProperty("assertedBy")
    private String assertedBy;
    @JsonProperty("subject")
    private List<String> subject = null;
    @JsonProperty("result")
    private Result_ result;
    @JsonProperty("multiPage")
    private Boolean multiPage;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("testcase")
    private String testcase;
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

    @JsonProperty("assertedBy")
    public String getAssertedBy() {
        return assertedBy;
    }

    @JsonProperty("assertedBy")
    public void setAssertedBy(String assertedBy) {
        this.assertedBy = assertedBy;
    }

    @JsonProperty("subject")
    public List<String> getSubject() {
        return subject;
    }

    @JsonProperty("subject")
    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    @JsonProperty("result")
    public Result_ getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result_ result) {
        this.result = result;
    }

    @JsonProperty("multiPage")
    public Boolean getMultiPage() {
        return multiPage;
    }

    @JsonProperty("multiPage")
    public void setMultiPage(Boolean multiPage) {
        this.multiPage = multiPage;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("testcase")
    public String getTestcase() {
        return testcase;
    }

    @JsonProperty("testcase")
    public void setTestcase(String testcase) {
        this.testcase = testcase;
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
