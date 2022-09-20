package es.gob.oaw.webservice;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.struts.util.MessageResources;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.gob.oaw.webservice.dto.ProblemDTO;
import es.gob.oaw.webservice.dto.SpecificProblemDTO;
import es.gob.oaw.webservice.dto.ValidationRequestDTO;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.form.ProblemForm;
import es.inteco.intav.form.SpecificProblemForm;
import es.inteco.intav.utils.EvaluatorUtils;

public class OAWService {
	final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
	final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
	final PropertiesManager pmgr = new PropertiesManager();

	public ProblemDTO[] validationRequest(ValidationRequestDTO validationRequestDTO) throws Exception {
		byte[] decodedBytes = Base64.getDecoder().decode(validationRequestDTO.getSourceCode().trim());
		String sourceCode = new String(decodedBytes);
		CheckAccessibility checkAccessibility = new CheckAccessibility();
		checkAccessibility.setContent(sourceCode);
		checkAccessibility.setGuidelineFile("observatorio-inteco-1-0.xml");
		EvaluatorUtility.initialize();
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true, false);
		return getProblems(oef);
	}

	private ProblemDTO[] getProblems(ObservatoryEvaluationForm observatoryEvaluationForm) {
		List<ProblemDTO> problemsDTO = new ArrayList<ProblemDTO>();
		List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
		for (ObservatoryLevelForm group : groups) {
			List<ObservatorySuitabilityForm> suitabilityGroups = group.getSuitabilityGroups();
			for (ObservatorySuitabilityForm suitabilityGroup : suitabilityGroups) {
				List<ObservatorySubgroupForm> subGroups = suitabilityGroup.getSubgroups();
				for (ObservatorySubgroupForm subGroup : subGroups) {
					List<ProblemForm> problems = subGroup.getProblems();
					for (ProblemForm problem : problems) {
						String errorMessage = checkDescriptionsManager.getString(problem.getError());
						String rationaleMessage = checkDescriptionsManager.getString(problem.getRationale());
						ProblemDTO problemDTO = new ProblemDTO();
						problemDTO.setTitle(messageResources.getMessage(subGroup.getDescription()));
						problemDTO.setDescription(errorMessage);
						problemDTO.setHelp(rationaleMessage);
						problemDTO.setType(getType(problem));
						problemDTO.setSpecificProblems(getSpecificProblems(problem));
						problemsDTO.add(problemDTO);
					}
				}
			}
		}
		return problemsDTO.toArray(new ProblemDTO[problemsDTO.size()]);
	}

	private String getType(ProblemForm problem) {
		String message;
		if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
			message = messageResources.getMessage("pdf.accessibility.bs.warning");
		} else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
			message = messageResources.getMessage("pdf.accessibility.bs.problem");
		} else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
			message = messageResources.getMessage("pdf.accessibility.bs.info");
		} else {
			message = "-";
		}
		return message;
	}

	private List<SpecificProblemDTO> getSpecificProblems(ProblemForm problem) {
		List<SpecificProblemDTO> specificProblemsDTO = new ArrayList<SpecificProblemDTO>();
		List<SpecificProblemForm> specificProblems = problem.getSpecificProblems();
		for (SpecificProblemForm specificProblem : specificProblems) {
			SpecificProblemDTO specificProblemDTO = new SpecificProblemDTO();
			specificProblemDTO.setLine(specificProblem.getLine());
			specificProblemDTO.setColumn(specificProblem.getColumn());
			specificProblemDTO.setCode(specificProblem.getCode());
			specificProblemsDTO.add(specificProblemDTO);
		}
		return specificProblemsDTO;
	}
}
