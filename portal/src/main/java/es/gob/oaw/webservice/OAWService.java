package es.gob.oaw.webservice;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.gob.oaw.webservice.dto.ProblemDTO;
import es.gob.oaw.webservice.dto.ScheduledTaskDTO;
import es.gob.oaw.webservice.dto.ValidationRequestDTO;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.utils.EvaluatorUtils;

public class OAWService {
	public ProblemDTO[] validationRequest(ValidationRequestDTO validationRequestDTO) throws Exception {
		CheckAccessibility checkAccessibility = new CheckAccessibility();
		checkAccessibility.setUrl(validationRequestDTO.getUrl().trim());
		checkAccessibility.setGuidelineFile("observatorio-inteco-1-0.xml");
		EvaluatorUtility.initialize();
		Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		return getProblems(evaluation);
	}

	public String scheduledTask(ScheduledTaskDTO scheduledTaskDTO) {
		return "Scheduled task for: " + scheduledTaskDTO.getEmail();
	}

	private ProblemDTO[] getProblems(Evaluation evaluation) {
		List<ProblemDTO> problems = new ArrayList<ProblemDTO>();
		int cont = 0;
		for (Problem problem : evaluation.getProblemsUser()) {
			ProblemDTO problemDTO = new ProblemDTO();
			problemDTO.setColumnNumber(problem.getColumnNumber());
			problemDTO.setDate(problem.getDate());
			problemDTO.setDecisionPass(problem.getDecisionPass());
			problemDTO.setId(problem.getId());
			problemDTO.setLineNumber(problem.getLineNumber());
			problemDTO.setLineOffset(problem.getLineOffset());
			problemDTO.setNameElement(problem.getNameElement());
			problemDTO.setStringColumnNumber(problem.getStringColumnNumber());
			problemDTO.setStringLineNumber(problem.getStringLineNumber());
			problemDTO.setSummary(false);
			problemDTO.setXpath(problem.getXpath());
			problems.add(problemDTO);
			if (cont > 10) {
				break;
			}
			cont++;
		}
		return problems.toArray(new ProblemDTO[problems.size()]);
	}
}
