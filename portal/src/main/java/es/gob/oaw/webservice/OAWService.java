package es.gob.oaw.webservice;

import es.gob.oaw.webservice.dto.CodeAnalysisDTO;
import es.gob.oaw.webservice.dto.ScheduledTaskDTO;

public class OAWService {
	public String codeAnalysis(CodeAnalysisDTO codeAnalysisDTO) {
		return "Analysis: " + codeAnalysisDTO.getCode();
	}

	public String scheduledTask(ScheduledTaskDTO scheduledTaskDTO) {
		return "Hello " + scheduledTaskDTO.getEmail();
	}
}
