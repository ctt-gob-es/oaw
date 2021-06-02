package es.gob.oaw.w3cvalidator;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Message.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "lastLine", "lastColumn", "firstColumn", "message", "extract", "hiliteStart", "hiliteLength", "subType" })
public class Message {
	
	/** The type. */
	@JsonProperty("type")
	private String type;
	
	/** The last line. */
	@JsonProperty("lastLine")
	private Integer lastLine;
	
	/** The last column. */
	@JsonProperty("lastColumn")
	private Integer lastColumn;
	
	/** The first column. */
	@JsonProperty("firstColumn")
	private Integer firstColumn;
	
	/** The message. */
	@JsonProperty("message")
	private String message;
	
	/** The extract. */
	@JsonProperty("extract")
	private String extract;
	
	/** The hilite start. */
	@JsonProperty("hiliteStart")
	private Integer hiliteStart;
	
	/** The hilite length. */
	@JsonProperty("hiliteLength")
	private Integer hiliteLength;
	
	/** The sub type. */
	@JsonProperty("subType")
	private String subType;
	
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
	 * Gets the last line.
	 *
	 * @return the last line
	 */
	@JsonProperty("lastLine")
	public Integer getLastLine() {
		return lastLine;
	}

	/**
	 * Sets the last line.
	 *
	 * @param lastLine the new last line
	 */
	@JsonProperty("lastLine")
	public void setLastLine(Integer lastLine) {
		this.lastLine = lastLine;
	}

	/**
	 * Gets the last column.
	 *
	 * @return the last column
	 */
	@JsonProperty("lastColumn")
	public Integer getLastColumn() {
		return lastColumn;
	}

	/**
	 * Sets the last column.
	 *
	 * @param lastColumn the new last column
	 */
	@JsonProperty("lastColumn")
	public void setLastColumn(Integer lastColumn) {
		this.lastColumn = lastColumn;
	}

	/**
	 * Gets the first column.
	 *
	 * @return the first column
	 */
	@JsonProperty("firstColumn")
	public Integer getFirstColumn() {
		return firstColumn;
	}

	/**
	 * Sets the first column.
	 *
	 * @param firstColumn the new first column
	 */
	@JsonProperty("firstColumn")
	public void setFirstColumn(Integer firstColumn) {
		this.firstColumn = firstColumn;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the extract.
	 *
	 * @return the extract
	 */
	@JsonProperty("extract")
	public String getExtract() {
		return extract;
	}

	/**
	 * Sets the extract.
	 *
	 * @param extract the new extract
	 */
	@JsonProperty("extract")
	public void setExtract(String extract) {
		this.extract = extract;
	}

	/**
	 * Gets the hilite start.
	 *
	 * @return the hilite start
	 */
	@JsonProperty("hiliteStart")
	public Integer getHiliteStart() {
		return hiliteStart;
	}

	/**
	 * Sets the hilite start.
	 *
	 * @param hiliteStart the new hilite start
	 */
	@JsonProperty("hiliteStart")
	public void setHiliteStart(Integer hiliteStart) {
		this.hiliteStart = hiliteStart;
	}

	/**
	 * Gets the hilite length.
	 *
	 * @return the hilite length
	 */
	@JsonProperty("hiliteLength")
	public Integer getHiliteLength() {
		return hiliteLength;
	}

	/**
	 * Sets the hilite length.
	 *
	 * @param hiliteLength the new hilite length
	 */
	@JsonProperty("hiliteLength")
	public void setHiliteLength(Integer hiliteLength) {
		this.hiliteLength = hiliteLength;
	}

	/**
	 * Gets the sub type.
	 *
	 * @return the sub type
	 */
	@JsonProperty("subType")
	public String getSubType() {
		return subType;
	}

	/**
	 * Sets the sub type.
	 *
	 * @param subType the new sub type
	 */
	@JsonProperty("subType")
	public void setSubType(String subType) {
		this.subType = subType;
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