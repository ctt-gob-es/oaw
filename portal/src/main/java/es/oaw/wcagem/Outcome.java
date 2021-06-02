
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
 * The Class Outcome.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@id",
    "@type"
})
public class Outcome {

    /** The id. */
    @JsonProperty("@id")
    private String id;
    
    /** The type. */
    @JsonProperty("@type")
    private String type;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Gets the type.
	 *
	 * @return the type
	 */
    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    @JsonProperty("@type")
    public void setType(String type) {
        this.type = type;
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
