package es.inteco.rastreador2.manager.importation.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.upload.FormFile;

import com.google.gson.Gson;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.importation.ImportEntitiesResultForm;
import es.inteco.rastreador2.dao.importar.database.AdministrativeLevel;
import es.inteco.rastreador2.dao.importar.database.ClassificationLabel;
import es.inteco.rastreador2.dao.importar.database.Complexity;
import es.inteco.rastreador2.dao.importar.database.Label;
import es.inteco.rastreador2.dao.importar.database.Scope;
import es.inteco.rastreador2.dao.importar.database.Seed;
import es.inteco.rastreador2.dao.importar.database.SeedType;
import es.inteco.rastreador2.dao.importar.database.Segment;
import es.inteco.rastreador2.importacion.database.form.AdministrativeLevelForm;
import es.inteco.rastreador2.importacion.database.form.ClassificationLabelForm;
import es.inteco.rastreador2.importacion.database.form.ComplexityForm;
import es.inteco.rastreador2.importacion.database.form.LabelForm;
import es.inteco.rastreador2.importacion.database.form.OAWForm;
import es.inteco.rastreador2.importacion.database.form.ScopeForm;
import es.inteco.rastreador2.importacion.database.form.SeedForm;
import es.inteco.rastreador2.importacion.database.form.SeedTypeForm;
import es.inteco.rastreador2.importacion.database.form.SegmentForm;
import es.inteco.rastreador2.manager.BaseManager;

/**
 * Importar entidades desde el SSP al Rastreador
 * 
 */
public class DatabaseImportManager extends BaseManager {
	public DatabaseImportManager() {
	}

	public ImportEntitiesResultForm importData(FormFile formFile) {
		ImportEntitiesResultForm importResultForm = new ImportEntitiesResultForm();
		try {
			deleteData();
			importResultForm = saveData(loadFileData(formFile));
		} catch (Exception e) {
			importResultForm.setValidImport(false);
			Logger.putLog("Error: ", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return importResultForm;
	}

	public ImportEntitiesResultForm importDataWS(String content) {
		ImportEntitiesResultForm importResultForm = new ImportEntitiesResultForm();
		Gson g = new Gson();
		OAWForm result = g.fromJson(content, OAWForm.class);
		try {
			deleteData();
			importResultForm = saveData(result);
			Logger.putLog("Importación finalizada: ", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		} catch (Exception e) {
			importResultForm.setValidImport(false);
			Logger.putLog("Error: ", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return importResultForm;
	}

	private OAWForm loadFileData(FormFile formFile) throws FileNotFoundException, IOException {
		Logger.putLog("Carga de datos desde fichero", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		InputStream stream = formFile.getInputStream();
		String text = null;
		try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		Gson g = new Gson();
		OAWForm result = g.fromJson(text, OAWForm.class);
		return result;
	}

	private void deleteData() {
		String[] tables = { "dependencia_ambito", "categorias_lista", "etiqueta", "clasificacion_etiqueta", "lista", "dependencia", "complejidades_lista", "tipo_lista", "ambitos_lista",
				"semilla_dependencia", "semilla_etiqueta" };
		truncateTables(tables);
	}

	private ImportEntitiesResultForm saveData(OAWForm oawExportDTO) {
		ImportEntitiesResultForm importResultForm = new ImportEntitiesResultForm();
		try {
			int numImportSegments = saveSegments(oawExportDTO.getSegmentos());
			int numImportAdminLevels = saveAdministrativeLevels(oawExportDTO.getAmbitos());
			int numImportClassificationLabels = saveClassificationLabels(oawExportDTO.getClasificacionEtiquetas());
			int numImportLabels = saveLabels(oawExportDTO.getEtiquetas());
			int numImportComplexities = saveComplexities(oawExportDTO.getComplejidades());
			int numImportSeedTypes = saveSeedTypes(oawExportDTO.getTipoSemillas());
			int numImportScopes = saveScopes(oawExportDTO.getDependencias());
			int numImportSeeds = saveSeeds(oawExportDTO.getSemillas());
			importResultForm.setNumSegments(oawExportDTO.getSegmentos().size());
			importResultForm.setNumAdminLevels(oawExportDTO.getAmbitos().size());
			importResultForm.setNumClassificationLabels(oawExportDTO.getClasificacionEtiquetas().size());
			importResultForm.setNumLabels(oawExportDTO.getEtiquetas().size());
			importResultForm.setNumComplexities(oawExportDTO.getComplejidades().size());
			importResultForm.setNumSeedTypes(oawExportDTO.getTipoSemillas().size());
			importResultForm.setNumScopes(oawExportDTO.getDependencias().size());
			importResultForm.setNumSeeds(oawExportDTO.getSemillas().size());
			importResultForm.setNumImportSegments(numImportSegments);
			importResultForm.setNumImportAdminLevels(numImportAdminLevels);
			importResultForm.setNumImportClassificationLabels(numImportClassificationLabels);
			importResultForm.setNumImportLabels(numImportLabels);
			importResultForm.setNumImportComplexities(numImportComplexities);
			importResultForm.setNumImportSeedTypes(numImportSeedTypes);
			importResultForm.setNumImportScopes(numImportScopes);
			importResultForm.setNumImportSeeds(numImportSeeds);
			importResultForm.setValidImport(true);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
			importResultForm.setValidImport(false);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
			importResultForm.setValidImport(false);
		}
		return importResultForm;
	}

	private int saveAdministrativeLevels(List<AdministrativeLevelForm> administrativeLevelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar niveles administrativos", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		AdministrativeLevel administrationLevel;
		if (Objects.isNull(administrativeLevelFormList)) {
			return cont;
		}
		for (AdministrativeLevelForm administrativeLevelForm : administrativeLevelFormList) {
			administrationLevel = new AdministrativeLevel();
			BeanUtils.copyProperties(administrationLevel, administrativeLevelForm);
			save(administrationLevel);
			cont++;
		}
		return cont;
	}

	private int saveClassificationLabels(List<ClassificationLabelForm> classificationLabelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar clasificación de etiquetas", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		ClassificationLabel classificationLabel;
		if (Objects.isNull(classificationLabelFormList)) {
			return cont;
		}
		for (ClassificationLabelForm classificationLabelForm : classificationLabelFormList) {
			classificationLabel = new ClassificationLabel();
			BeanUtils.copyProperties(classificationLabel, classificationLabelForm);
			save(classificationLabel);
			cont++;
		}
		return cont;
	}

	private int saveSeedTypes(List<SeedTypeForm> seedTypeFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar tipos de semilla", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		SeedType seedType;
		if (Objects.isNull(seedTypeFormList)) {
			return cont;
		}
		for (SeedTypeForm seedTypeForm : seedTypeFormList) {
			seedType = new SeedType();
			BeanUtils.copyProperties(seedType, seedTypeForm);
			save(seedType);
			cont++;
		}
		return cont;
	}

	private int saveSegments(List<SegmentForm> segmentFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar segmentos", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		Segment segment;
		if (Objects.isNull(segmentFormList)) {
			return cont;
		}
		for (SegmentForm segmentForm : segmentFormList) {
			segment = new Segment();
			BeanUtils.copyProperties(segment, segmentForm);
			save(segment);
			cont++;
		}
		return cont;
	}

	private int saveComplexities(List<ComplexityForm> complexityFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar complejidades", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		Complexity complexity;
		if (Objects.isNull(complexityFormList)) {
			return cont;
		}
		for (ComplexityForm complexityForm : complexityFormList) {
			complexity = new Complexity();
			BeanUtils.copyProperties(complexity, complexityForm);
			save(complexity);
			cont++;
		}
		return cont;
	}

	private int saveLabels(List<LabelForm> labelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar etiquetas", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		Label label = new Label();
		ClassificationLabel classificationLabel;
		if (Objects.isNull(labelFormList)) {
			return cont;
		}
		for (LabelForm labelForm : labelFormList) {
			classificationLabel = new ClassificationLabel();
			classificationLabel.setId(labelForm.getClasificacionEtiqueta().getId());
			label = new Label();
			label.setId(labelForm.getId());
			label.setNombre(labelForm.getNombre());
			label.setClasificacionEtiqueta(classificationLabel);
			save(label);
			cont++;
		}
		return cont;
	}

	private int saveScopes(List<ScopeForm> scopeFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar dependencias", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		Scope scope;
		AdministrativeLevel administrativeLevel;
		if (Objects.isNull(scopeFormList)) {
			return cont;
		}
		for (ScopeForm scopeForm : scopeFormList) {
			// Admin Levels
			Set<AdministrativeLevel> administrativeLevels = new HashSet<>();
			if (Objects.nonNull(scopeForm.getAmbitos()) && scopeForm.getAmbitos().size() > 0) {
				Logger.putLog("Ambitos asociados: " + scopeForm.getNombre(), DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
				Set<AdministrativeLevelForm> administrativeLevelFormList = scopeForm.getAmbitos();
				for (AdministrativeLevelForm administrativeLevelForm : administrativeLevelFormList) {
					administrativeLevel = new AdministrativeLevel();
					administrativeLevel.setId(administrativeLevelForm.getId());
					administrativeLevels.add(administrativeLevel);
				}
			}
			scope = new Scope();
			scope.setId(scopeForm.getId());
			scope.setNombre(scopeForm.getNombre());
			scope.setEmails(scopeForm.getEmails());
			scope.setEnvioAutomatico(scopeForm.isEnvioAutomatico());
			scope.setOficial(scopeForm.isOficial());
			scope.setAmbitos(administrativeLevels);
			scope.setAcronimo(scopeForm.getAcronimo());
			Label label = new Label();
			if (Objects.nonNull(scopeForm.getEtiqueta())) {
				label.setId(scopeForm.getEtiqueta().getId());
				scope.setEtiqueta(label);
			}
			save(scope);
			cont++;
		}
		return cont;
	}

	private int saveSeeds(List<SeedForm> seedFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar semillas", DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
		int cont = 0;
		Seed seed;
		SeedType seedType;
		Complexity complexity;
		AdministrativeLevel administrativeLevel;
		Scope dependency;
		Segment segment;
		ClassificationLabel classificationLabel;
		Label label;
		if (seedFormList == null) {
			return cont;
		}
		for (SeedForm seedForm : seedFormList) {
			seed = new Seed();
			// Seed type
			seedType = new SeedType();
			if (Objects.nonNull(seedForm.getTipoSemilla())) {
				seedType.setId(seedForm.getTipoSemilla().getId());
				seed.setTipoSemilla(seedType);
			}
			// Admin level
			administrativeLevel = new AdministrativeLevel();
			if (Objects.nonNull(seedForm.getAmbito())) {
				administrativeLevel.setId(seedForm.getAmbito().getId());
				seed.setAmbito(administrativeLevel);
			}
			// Complexity
			complexity = new Complexity();
			if (Objects.nonNull(seedForm.getComplejidad())) {
				complexity.setId(seedForm.getComplejidad().getId());
				seed.setComplejidad(complexity);
			}
			// Segment
			segment = new Segment();
			if (Objects.nonNull(seedForm.getSegmento())) {
				segment.setId(seedForm.getSegmento().getId());
				seed.setSegmento(segment);
			}
			// Labels
			Set<Label> labels = new HashSet<>();
			if (Objects.nonNull(seedForm.getEtiquetas())) {
				Set<LabelForm> labelFormList = seedForm.getEtiquetas();
				classificationLabel = new ClassificationLabel();
				label = new Label();
				for (LabelForm labelForm : labelFormList) {
					classificationLabel = new ClassificationLabel();
					classificationLabel.setId(labelForm.getClasificacionEtiqueta().getId());
					label = new Label();
					label.setId(labelForm.getId());
					label.setClasificacionEtiqueta(classificationLabel);
					labels.add(label);
				}
			}
			// Dependencies - scopes
			Set<Scope> dependencies = new HashSet<>();
			if (Objects.nonNull(seedForm.getDependencias())) {
				Set<ScopeForm> scopeFormList = seedForm.getDependencias();
				for (ScopeForm scopeForm : scopeFormList) {
					dependency = new Scope();
					dependency.setId(scopeForm.getId());
					dependencies.add(dependency);
				}
			}
			seed.setId(seedForm.getId());
			seed.setAcronimo(seedForm.getAcronimo());
			seed.setNombre(seedForm.getNombre());
			seed.setLista(seedForm.getLista());
			seed.setObservaciones(seedForm.getObservacionesRastreador());
			seed.setActiva(seedForm.isActiva());
			seed.setEliminada(seedForm.isEliminada());
			seed.setEnDirectorio(seedForm.isEnDirectorio());
			seed.setEtiquetas(labels);
			seed.setDependencias(dependencies);
			Logger.putLog("Semilla: " + seed.getNombre(), DatabaseImportManager.class, Logger.LOG_LEVEL_ERROR);
			if (Objects.nonNull(seedType.getId())) {
				save(seed);
				cont++;
			}
		}
		return cont;
	}
}