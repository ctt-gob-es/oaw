package es.gob.oaw.w3cvalidator;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "lastLine", "lastColumn", "firstColumn", "message", "extract", "hiliteStart", "hiliteLength", "subType" })
public class Message {
	@JsonProperty("type")
	private String type;
	@JsonProperty("lastLine")
	private Integer lastLine;
	@JsonProperty("lastColumn")
	private Integer lastColumn;
	@JsonProperty("firstColumn")
	private Integer firstColumn;
	@JsonProperty("message")
	private String message;
	@JsonProperty("extract")
	private String extract;
	@JsonProperty("hiliteStart")
	private Integer hiliteStart;
	@JsonProperty("hiliteLength")
	private Integer hiliteLength;
	@JsonProperty("subType")
	private String subType;
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

	@JsonProperty("lastLine")
	public Integer getLastLine() {
		return lastLine;
	}

	@JsonProperty("lastLine")
	public void setLastLine(Integer lastLine) {
		this.lastLine = lastLine;
	}

	@JsonProperty("lastColumn")
	public Integer getLastColumn() {
		return lastColumn;
	}

	@JsonProperty("lastColumn")
	public void setLastColumn(Integer lastColumn) {
		this.lastColumn = lastColumn;
	}

	@JsonProperty("firstColumn")
	public Integer getFirstColumn() {
		return firstColumn;
	}

	@JsonProperty("firstColumn")
	public void setFirstColumn(Integer firstColumn) {
		this.firstColumn = firstColumn;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("extract")
	public String getExtract() {
		return extract;
	}

	@JsonProperty("extract")
	public void setExtract(String extract) {
		this.extract = extract;
	}

	@JsonProperty("hiliteStart")
	public Integer getHiliteStart() {
		return hiliteStart;
	}

	@JsonProperty("hiliteStart")
	public void setHiliteStart(Integer hiliteStart) {
		this.hiliteStart = hiliteStart;
	}

	@JsonProperty("hiliteLength")
	public Integer getHiliteLength() {
		return hiliteLength;
	}

	@JsonProperty("hiliteLength")
	public void setHiliteLength(Integer hiliteLength) {
		this.hiliteLength = hiliteLength;
	}

	@JsonProperty("subType")
	public String getSubType() {
		return subType;
	}

	@JsonProperty("subType")
	public void setSubType(String subType) {
		this.subType = subType;
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