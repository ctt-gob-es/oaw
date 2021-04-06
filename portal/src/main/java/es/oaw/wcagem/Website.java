
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
 * The Class Website.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "id",
    "siteName",
    "siteScope"
})
public class Website {

    /** The type. */
    @JsonProperty("type")
    private List<String> type = null;
    
    /** The id. */
    @JsonProperty("id")
    private String id;
    
    /** The site name. */
    @JsonProperty("siteName")
    private String siteName;
    
    /** The site scope. */
    @JsonProperty("siteScope")
    private String siteScope;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
	 * Gets the type.
	 *
	 * @return the type
	 */
    @JsonProperty("type")
    public List<String> getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    @JsonProperty("type")
    public void setType(List<String> type) {
        this.type = type;
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
	 * Gets the site name.
	 *
	 * @return the site name
	 */
    @JsonProperty("siteName")
    public String getSiteName() {
        return siteName;
    }

    /**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
    @JsonProperty("siteName")
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
	 * Gets the site scope.
	 *
	 * @return the site scope
	 */
    @JsonProperty("siteScope")
    public String getSiteScope() {
        return siteScope;
    }

    /**
	 * Sets the site scope.
	 *
	 * @param siteScope the new site scope
	 */
    @JsonProperty("siteScope")
    public void setSiteScope(String siteScope) {
        this.siteScope = siteScope;
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
