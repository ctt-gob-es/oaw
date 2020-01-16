
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
    "@context",
    "type",
    "publisher",
    "lang",
    "evaluationScope",
    "auditResult",
    "title",
    "commissioner",
    "creator",
    "structuredSample",
    "randomSample",
    "reliedUponTechnology",
    "id",
    "name"
})
public class Graph {

    @JsonProperty("@context")
    private Context context;
    @JsonProperty("type")
    private String type;
    @JsonProperty("publisher")
    private String publisher;
    @JsonProperty("lang")
    private String lang;
    @JsonProperty("evaluationScope")
    private EvaluationScope evaluationScope;
    @JsonProperty("auditResult")
    private List<AuditResult> auditResult = null;
    @JsonProperty("title")
    private String title;
    @JsonProperty("commissioner")
    private String commissioner;
    @JsonProperty("creator")
    private String creator;
    @JsonProperty("structuredSample")
    private StructuredSample structuredSample;
    @JsonProperty("randomSample")
    private RandomSample randomSample;
    @JsonProperty("reliedUponTechnology")
    private List<ReliedUponTechnology> reliedUponTechnology = null;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@context")
    public Context getContext() {
        return context;
    }

    @JsonProperty("@context")
    public void setContext(Context context) {
        this.context = context;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("publisher")
    public String getPublisher() {
        return publisher;
    }

    @JsonProperty("publisher")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("lang")
    public String getLang() {
        return lang;
    }

    @JsonProperty("lang")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @JsonProperty("evaluationScope")
    public EvaluationScope getEvaluationScope() {
        return evaluationScope;
    }

    @JsonProperty("evaluationScope")
    public void setEvaluationScope(EvaluationScope evaluationScope) {
        this.evaluationScope = evaluationScope;
    }

    @JsonProperty("auditResult")
    public List<AuditResult> getAuditResult() {
        return auditResult;
    }

    @JsonProperty("auditResult")
    public void setAuditResult(List<AuditResult> auditResult) {
        this.auditResult = auditResult;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("commissioner")
    public String getCommissioner() {
        return commissioner;
    }

    @JsonProperty("commissioner")
    public void setCommissioner(String commissioner) {
        this.commissioner = commissioner;
    }

    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty("structuredSample")
    public StructuredSample getStructuredSample() {
        return structuredSample;
    }

    @JsonProperty("structuredSample")
    public void setStructuredSample(StructuredSample structuredSample) {
        this.structuredSample = structuredSample;
    }

    @JsonProperty("randomSample")
    public RandomSample getRandomSample() {
        return randomSample;
    }

    @JsonProperty("randomSample")
    public void setRandomSample(RandomSample randomSample) {
        this.randomSample = randomSample;
    }

    @JsonProperty("reliedUponTechnology")
    public List<ReliedUponTechnology> getReliedUponTechnology() {
        return reliedUponTechnology;
    }

    @JsonProperty("reliedUponTechnology")
    public void setReliedUponTechnology(List<ReliedUponTechnology> reliedUponTechnology) {
        this.reliedUponTechnology = reliedUponTechnology;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
