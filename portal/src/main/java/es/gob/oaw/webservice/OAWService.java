package es.gob.oaw.webservice;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.gob.oaw.webservice.dto.ImportDataRequestDTO;
import es.gob.oaw.webservice.dto.ImportDataResponseDTO;
import es.gob.oaw.webservice.dto.ProblemDTO;
import es.gob.oaw.webservice.dto.SpecificProblemDTO;
import es.gob.oaw.webservice.dto.TrackerBackupRequestDTO;
import es.gob.oaw.webservice.dto.TrackerBackupResponseDTO;
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
import es.inteco.rastreador2.actionform.importation.ImportEntitiesResultForm;
import es.inteco.rastreador2.manager.exportation.database.DatabaseExportManager;
import es.inteco.rastreador2.manager.importation.database.DatabaseImportManager;

public class OAWService {
	final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
	final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
	final PropertiesManager pmgr = new PropertiesManager();
	static final String defaultGuideline = "observatorio-une-en2019-nobroken.xml";
	static final String defaultLanguage = "es";
	static final String headersError = "<p>Los elementos de encabezado (<code>H1</code>...<code>H6</code>) sirven para identificar los títulos de las diferentes "
			+ "secciones en las que se estructura un documento. El nivel empleado en cada encabezado es lo que definirá la estructura "
			+ "jerárquica de las secciones del documento. Por tanto, esta estructura de encabezados y los niveles empleados ha de ser "
			+ "correcta reflejando la estructura lógica del contenido de la página, identificando como encabezados todos los títulos de sección, "
			+ "sin emplear elementos de encabezado únicamente para crear efectos de presentación y sin saltarse niveles intermedios al descender en "
			+ "la jerarquia de encabezados.</p><p>Cada encabezado ha de disponer de la correspondiente sección de contenido a la que titulan. Un encabezado "
			+ "que no tenga su correspondiente sección de contenido no tiene razón de ser y podrá generar consfusión a los usuarios. Por tanto, debe existir "
			+ "algún contenido textual entre un encabezado y el siguiente encabezado del mismo nivel o de un nivel superior. Por ejemplo, debe existir contenido "
			+ "entre secuencias de encabezados del tipo (<code>H2, H2</code>) o (<code>H2, H1</code>).</p><p>Una estructura correcta de encabezados es de gran "
			+ "importancia ya que las aplicaciones de usuario y los productos de apoyo, como los lectores de pantalla, pueden proporcionar mecanismos especiales "
			+ "de navegación que permitan a los usuarios acceder de forma rápida a las distintas secciones que componen una página web (p. ej. mediante una índice "
			+ "o mapa del documento con accesos directos a las diferentes secciones del mismo).</p>";
	static final String headersErrorAlternative = "Encabezados consecutivos del mismo nivel (o superior) sin contenido entre ellos.";

	// Servicio de validación de página web
	public ProblemDTO[] validationRequest(ValidationRequestDTO validationRequestDTO) throws Exception {
		CheckAccessibility checkAccessibility = new CheckAccessibility();
		checkAccessibility.setWebService(true);
		// Metodología por defecto (Plugins de CMS y Navegador)
		checkAccessibility.setGuidelineFile(defaultGuideline);
		// Si el usuario fija una metodología, comprobación de enlaces... buscamos en las guidelines (Validación de URL)
		if (!StringUtils.isBlank(validationRequestDTO.getMethodology())) {
			checkAccessibility.setGuidelineFile(getGuidelineFile(validationRequestDTO));
		}
		EvaluatorUtility.initialize();
		Evaluation evaluation = null;
		// Validación de código fuente (Plugin de CMS, Plugin de Navegador)
		if (!StringUtils.isBlank(validationRequestDTO.getHtmlContent())) {
			byte[] decodedBytes = Base64.getDecoder().decode(validationRequestDTO.getHtmlContent().trim());
			String sourceCode = new String(decodedBytes);
			checkAccessibility.setContent(sourceCode);
			evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, defaultLanguage);
			// Validación por url (Validador de URL). Esta opción hace uso del motor-js para renderizar la página web a analizar
		} else if (!StringUtils.isBlank(validationRequestDTO.getUrl())) {
			checkAccessibility.setUrl(validationRequestDTO.getUrl());
			evaluation = EvaluatorUtils.evaluate(checkAccessibility, defaultLanguage);
		}
		if (Objects.nonNull(evaluation)) {
			ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true, false);
			return getProblems(oef);
		} else {
			return getEvaluationProblems();
		}
	}

	// Importación de datos desde el SSP
	public ImportDataResponseDTO importDataRequest(ImportDataRequestDTO importDataRequestDTO) {
		DatabaseImportManager importEntitiesManager = new DatabaseImportManager();
		byte[] decodedBytes = Base64.getDecoder().decode(importDataRequestDTO.getContent().trim());
		String data = new String(decodedBytes);
		ImportDataResponseDTO importResultDTO = new ImportDataResponseDTO();
		try {
			ImportEntitiesResultForm importEntitiesResultForm = importEntitiesManager.importDataWS(data);
			String result = formatResults(importEntitiesResultForm);
			importResultDTO.setObservations(result);
			importResultDTO.setValidImport(importEntitiesResultForm.isValidImport());
		} catch (Exception e) {
			importResultDTO.setObservations(e.getMessage());
			importResultDTO.setValidImport(false);
		}
		return importResultDTO;
	}

	// Backup de datos del Rastreador
	public TrackerBackupResponseDTO trackerBackupRequest(TrackerBackupRequestDTO trackerBackupRequestDTO) {
		DatabaseExportManager exportEntitiesManager = new DatabaseExportManager();
		TrackerBackupResponseDTO trackerBackupResponseDTO = new TrackerBackupResponseDTO();
		try {
			String content = exportEntitiesManager.backup();
			trackerBackupResponseDTO.setContent(Base64.getEncoder().encodeToString(content.getBytes()));
			trackerBackupResponseDTO.setObservations("Backup finalizado con éxito");
			trackerBackupResponseDTO.setValidExport(true);
		} catch (Exception e) {
			trackerBackupResponseDTO.setObservations(e.getMessage());
			trackerBackupResponseDTO.setValidExport(false);
		}
		return trackerBackupResponseDTO;
	}

	private ProblemDTO[] getProblems(ObservatoryEvaluationForm observatoryEvaluationForm) {
		List<ProblemDTO> problemsDTO = new ArrayList<>();
		List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
		for (ObservatoryLevelForm group : groups) {
			List<ObservatorySuitabilityForm> suitabilityGroups = group.getSuitabilityGroups();
			for (ObservatorySuitabilityForm suitabilityGroup : suitabilityGroups) {
				List<ObservatorySubgroupForm> subGroups = suitabilityGroup.getSubgroups();
				for (ObservatorySubgroupForm subGroup : subGroups) {
					List<ProblemForm> problems = subGroup.getProblems();
					for (ProblemForm problem : problems) {
						if (!ignoreProblem(problem)) {
							String errorMessage = checkDescriptionsManager.getString(problem.getError());
							String rationaleMessage = checkDescriptionsManager.getString(problem.getRationale());
							ProblemDTO problemDTO = new ProblemDTO();
							problemDTO.setTitle(messageResources.getMessage(subGroup.getDescription()));
							if (headersError.equals(errorMessage)) {
								problemDTO.setDescription(headersErrorAlternative);
								problemDTO.setHelp(errorMessage);
							} else {
								problemDTO.setDescription(errorMessage);
								problemDTO.setHelp(rationaleMessage);
							}
							problemDTO.setType(getType(problem));
							problemDTO.setSpecificProblems(getSpecificProblems(problem));
							problemsDTO.add(problemDTO);
						}
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
		List<SpecificProblemDTO> specificProblemsDTO = new ArrayList<>();
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

	private boolean ignoreProblem(ProblemForm problem) {
		boolean ignore = false;
		// Las páginas analizadas en el muestreo presentan todas el mismo título.
		if (problem.getCheck().contains("462")) {
			ignore = true;
		}
		return ignore;
	}

	private String formatResults(ImportEntitiesResultForm importEntitiesResultForm) {
		String result = "\n\n".concat("Resultados de la importación \n\n".concat(formatResult("Semillas", importEntitiesResultForm.getNumImportSeeds(), importEntitiesResultForm.getNumSeeds())))
				.concat(formatResult("Tipos semillas", importEntitiesResultForm.getNumImportSeedTypes(), importEntitiesResultForm.getNumSeedTypes()))
				.concat(formatResult("Dependencias", importEntitiesResultForm.getNumImportScopes(), importEntitiesResultForm.getNumScopes()))
				.concat(formatResult("Segmentos", importEntitiesResultForm.getNumImportSegments(), importEntitiesResultForm.getNumSegments()))
				.concat(formatResult("Etiquetas", importEntitiesResultForm.getNumImportLabels(), importEntitiesResultForm.getNumLabels()))
				.concat(formatResult("Clasificación Etiquetas", importEntitiesResultForm.getNumImportClassificationLabels(), importEntitiesResultForm.getNumClassificationLabels()))
				.concat(formatResult("Niveles administrativos", importEntitiesResultForm.getNumImportAdminLevels(), importEntitiesResultForm.getNumAdminLevels()))
				.concat(formatResult("Complejidades", importEntitiesResultForm.getNumImportComplexities(), importEntitiesResultForm.getNumComplexities()));
		return result;
	}

	private String formatResult(String entity, int importElements, int totalElements) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(entity);
		stringBuilder.append(": ");
		stringBuilder.append(importElements);
		stringBuilder.append("/");
		stringBuilder.append(totalElements);
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	private String getGuidelineFile(ValidationRequestDTO validationRequestDTO) {
		String guideline = validationRequestDTO.getMethodology();
		if (!validationRequestDTO.isBrokenLinks()) {
			guideline = guideline.concat("-nobroken");
		}
		return guideline.concat(".xml");
	}

	private ProblemDTO[] getEvaluationProblems() {
		List<ProblemDTO> problemsDTO = new ArrayList<>();
		ProblemDTO problemDTO = new ProblemDTO();
		problemDTO.setTitle("Problema de conectividad");
		problemDTO.setDescription("No se ha podido establecer conexión con la web deseada");
		problemDTO.setHelp("No se ha podido establecer conexión con la web deseada");
		problemDTO.setType("-");
		problemsDTO.add(problemDTO);
		return problemsDTO.toArray(new ProblemDTO[problemsDTO.size()]);
	}
}
