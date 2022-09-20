package es.gob.oaw.webservice.dto;

import java.util.List;

public class ProblemDTO {
	private String title;
	private String description;
	private String help;
	private String type;
	private SpecificProblemDTO[] specificProblems;

	public ProblemDTO() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SpecificProblemDTO[] getSpecificProblems() {
		return specificProblems;
	}

	public void setSpecificProblems(List<SpecificProblemDTO> specificProblems) {
		this.specificProblems = specificProblems.toArray(new SpecificProblemDTO[specificProblems.size()]);
	}
}
