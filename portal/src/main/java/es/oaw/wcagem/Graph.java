
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
 * The Class Graph.
 */
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

    /** The context. */
    @JsonProperty("@context")
    private Context context;
    
    /** The type. */
    @JsonProperty("type")
    private String type;
    
    /** The publisher. */
    @JsonProperty("publisher")
    private String publisher;
    
    /** The lang. */
    @JsonProperty("lang")
    private String lang;
    
    /** The evaluation scope. */
    @JsonProperty("evaluationScope")
    private EvaluationScope evaluationScope;
    
    /** The audit result. */
    @JsonProperty("auditResult")
    private List<AuditResult> auditResult = null;
    
    /** The title. */
    @JsonProperty("title")
    private String title;
    
    /** The commissioner. */
    @JsonProperty("commissioner")
    private String commissioner;
    
    /** The creator. */
    @JsonProperty("creator")
    private String creator;
    
    /** The structured sample. */
    @JsonProperty("structuredSample")
    private StructuredSample structuredSample;
    
    /** The random sample. */
    @JsonProperty("randomSample")
    private RandomSample randomSample;
    
    /** The relied upon technology. */
    @JsonProperty("reliedUponTechnology")
    private List<ReliedUponTechnology> reliedUponTechnology = null;
    
    /** The id. */
    @JsonProperty("id")
    private String id;
    
    /** The name. */
    @JsonProperty("name")
    private String name;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
	 * Gets the context.
	 *
	 * @return the context
	 */
    @JsonProperty("@context")
    public Context getContext() {
        return context;
    }

    /**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
    @JsonProperty("@context")
    public void setContext(Context context) {
        this.context = context;
    }

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
	 * Gets the publisher.
	 *
	 * @return the publisher
	 */
    @JsonProperty("publisher")
    public String getPublisher() {
        return publisher;
    }

    /**
	 * Sets the publisher.
	 *
	 * @param publisher the new publisher
	 */
    @JsonProperty("publisher")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
    @JsonProperty("lang")
    public String getLang() {
        return lang;
    }

    /**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
    @JsonProperty("lang")
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
	 * Gets the evaluation scope.
	 *
	 * @return the evaluation scope
	 */
    @JsonProperty("evaluationScope")
    public EvaluationScope getEvaluationScope() {
        return evaluationScope;
    }

    /**
	 * Sets the evaluation scope.
	 *
	 * @param evaluationScope the new evaluation scope
	 */
    @JsonProperty("evaluationScope")
    public void setEvaluationScope(EvaluationScope evaluationScope) {
        this.evaluationScope = evaluationScope;
    }

    /**
	 * Gets the audit result.
	 *
	 * @return the audit result
	 */
    @JsonProperty("auditResult")
    public List<AuditResult> getAuditResult() {
        return auditResult;
    }

    /**
	 * Sets the audit result.
	 *
	 * @param auditResult the new audit result
	 */
    @JsonProperty("auditResult")
    public void setAuditResult(List<AuditResult> auditResult) {
        this.auditResult = auditResult;
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
	 * Gets the commissioner.
	 *
	 * @return the commissioner
	 */
    @JsonProperty("commissioner")
    public String getCommissioner() {
        return commissioner;
    }

    /**
	 * Sets the commissioner.
	 *
	 * @param commissioner the new commissioner
	 */
    @JsonProperty("commissioner")
    public void setCommissioner(String commissioner) {
        this.commissioner = commissioner;
    }

    /**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    /**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
	 * Gets the structured sample.
	 *
	 * @return the structured sample
	 */
    @JsonProperty("structuredSample")
    public StructuredSample getStructuredSample() {
        return structuredSample;
    }

    /**
	 * Sets the structured sample.
	 *
	 * @param structuredSample the new structured sample
	 */
    @JsonProperty("structuredSample")
    public void setStructuredSample(StructuredSample structuredSample) {
        this.structuredSample = structuredSample;
    }

    /**
	 * Gets the random sample.
	 *
	 * @return the random sample
	 */
    @JsonProperty("randomSample")
    public RandomSample getRandomSample() {
        return randomSample;
    }

    /**
	 * Sets the random sample.
	 *
	 * @param randomSample the new random sample
	 */
    @JsonProperty("randomSample")
    public void setRandomSample(RandomSample randomSample) {
        this.randomSample = randomSample;
    }

    /**
	 * Gets the relied upon technology.
	 *
	 * @return the relied upon technology
	 */
    @JsonProperty("reliedUponTechnology")
    public List<ReliedUponTechnology> getReliedUponTechnology() {
        return reliedUponTechnology;
    }

    /**
	 * Sets the relied upon technology.
	 *
	 * @param reliedUponTechnology the new relied upon technology
	 */
    @JsonProperty("reliedUponTechnology")
    public void setReliedUponTechnology(List<ReliedUponTechnology> reliedUponTechnology) {
        this.reliedUponTechnology = reliedUponTechnology;
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
	 * Gets the name.
	 *
	 * @return the name
	 */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
