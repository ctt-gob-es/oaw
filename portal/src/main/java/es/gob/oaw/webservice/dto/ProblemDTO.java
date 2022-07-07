package es.gob.oaw.webservice.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.inteco.common.properties.PropertiesManager;

public class ProblemDTO {
	private String date;
	private String xpath;
	private String nameElement;
	private String stringLineNumber;
	private String stringColumnNumber;
	private boolean decisionPass;
	private int lineOffset;
	private int id;
	private boolean summary;

	public ProblemDTO() {
		final PropertiesManager properties = new PropertiesManager();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));
		date = dateFormat.format(new Date());
	}

	public String getDate() {
		return date;
	}

	public void setDate(String aDate) {
		date = aDate;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String anXpath) {
		xpath = anXpath;
	}

	public void setLineOffset(int offset) {
		lineOffset = offset;
	}

	public int getLineNumber() {
		if (stringLineNumber != null) {
			int lineNumber = Integer.parseInt(stringLineNumber);
			return lineNumber - lineOffset;
		} else {
			return 0;
		}
	}

	public String getLineNumberString() {
		if (lineOffset == 0) {
			return stringLineNumber;
		} else {
			int lineNumber = Integer.parseInt(stringLineNumber);
			return Integer.toString(lineNumber - lineOffset);
		}
	}

	public int getColumnNumber() {
		if (stringColumnNumber != null) {
			return Integer.parseInt(stringColumnNumber);
		} else {
			return 0;
		}
	}

	public String getColumnNumberString() {
		return stringColumnNumber;
	}

	public boolean getDecisionPass() {
		return decisionPass;
	}

	public void setDecisionPass(boolean state) {
		decisionPass = state;
	}

	public void setId(int number) {
		id = number;
	}

	public int getId() {
		return id;
	}

	public void setLineNumber(int linenumber) {
		stringLineNumber = Integer.toString(linenumber);
	}

	public void setColumnNumber(int columnnumber) {
		stringColumnNumber = Integer.toString(columnnumber);
	}

	public boolean isSummary() {
		return summary;
	}

	public void setSummary(boolean isSummary) {
		this.summary = isSummary;
	}

	public String getNameElement() {
		return nameElement;
	}

	public void setNameElement(String nameElement) {
		this.nameElement = nameElement;
	}

	public String getStringLineNumber() {
		return stringLineNumber;
	}

	public void setStringLineNumber(String stringLineNumber) {
		this.stringLineNumber = stringLineNumber;
	}

	public String getStringColumnNumber() {
		return stringColumnNumber;
	}

	public void setStringColumnNumber(String stringColumnNumber) {
		this.stringColumnNumber = stringColumnNumber;
	}

	public int getLineOffset() {
		return lineOffset;
	}
}
