package es.gob.oaw.webservice;

import java.util.ArrayList;
import java.util.List;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.gob.oaw.webservice.dto.CodeAnalysisDTO;
import es.gob.oaw.webservice.dto.ProblemDTO;
import es.gob.oaw.webservice.dto.ScheduledTaskDTO;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.utils.EvaluatorUtils;

public class OAWService {
	public ProblemDTO[] codeAnalysis(CodeAnalysisDTO codeAnalysisDTO) throws Exception {
		CheckAccessibility checkAccessibility = new CheckAccessibility();
		// checkAccessibility.setContent(codeAnalysisDTO.getCode());
		checkAccessibility.setUrl(codeAnalysisDTO.getCode().trim());
		checkAccessibility.setGuidelineFile("observatorio-inteco-1-0.xml");
		EvaluatorUtility.initialize();
		Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		// JAXBContext context = JAXBContext.newInstance(Problems.class);
		// Marshaller marshaller = context.createMarshaller();
		// marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// StringWriter sw = new StringWriter();
		// Problems problems = new Problems();
		// problems.setProblems(evaluation.getProblemsUser());
		// marshaller.marshal(problems, sw);
		List<ProblemDTO> problems = new ArrayList<ProblemDTO>();
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
		}
		return problems.toArray(new ProblemDTO[problems.size()]);
	}

	public String scheduledTask(ScheduledTaskDTO scheduledTaskDTO) {
		return "Hello " + scheduledTaskDTO.getEmail();
	}
}
