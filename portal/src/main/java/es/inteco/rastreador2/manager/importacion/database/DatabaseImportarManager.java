package es.inteco.rastreador2.manager.importacion.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.upload.FormFile;

import com.google.gson.Gson;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.importacion.ImportarEntidadesResultadoForm;
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
public class DatabaseImportarManager extends BaseManager {
	public DatabaseImportarManager() {
	}

	public ImportarEntidadesResultadoForm importData(FormFile formFile) {
		ImportarEntidadesResultadoForm importResultForm = new ImportarEntidadesResultadoForm();
		try {
			deleteData();
			importResultForm = saveData(loadFileData(formFile));
		} catch (Exception e) {
			importResultForm.setValidImport(false);
			Logger.putLog("Error: ", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return importResultForm;
	}

	public ImportarEntidadesResultadoForm importDataWS(String content) {
		ImportarEntidadesResultadoForm importResultForm = new ImportarEntidadesResultadoForm();
		Gson g = new Gson();
		OAWForm result = g.fromJson(content, OAWForm.class);
		try {
			deleteData();
			importResultForm = saveData(result);
		} catch (Exception e) {
			importResultForm.setValidImport(false);
			Logger.putLog("Error: ", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return importResultForm;
	}

	private OAWForm loadFileData(FormFile formFile) throws FileNotFoundException, IOException {
		Logger.putLog("Carga de datos desde fichero", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
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

	private ImportarEntidadesResultadoForm saveData(OAWForm oawExportDTO) {
		ImportarEntidadesResultadoForm importResultForm = new ImportarEntidadesResultadoForm();
		try {
			int numSegments = saveSegments(oawExportDTO.getSegmentos());
			int numAdminLevels = saveAdministrativeLevels(oawExportDTO.getAmbitos());
			int numClassificationLabels = saveClassificationLabels(oawExportDTO.getClasificacionEtiquetas());
			int numLabels = saveLabels(oawExportDTO.getEtiquetas());
			int numComplexities = saveComplexities(oawExportDTO.getComplejidades());
			int numSeedTypes = saveSeedTypes(oawExportDTO.getTipoSemillas());
			int numScopes = saveScopes(oawExportDTO.getDependencias());
			int numSeeds = saveSeeds(oawExportDTO.getSemillas());
			importResultForm.setNumSegments(numSegments);
			importResultForm.setNumAdminLevels(numAdminLevels);
			importResultForm.setNumClassificationLabels(numClassificationLabels);
			importResultForm.setNumLabels(numLabels);
			importResultForm.setNumComplexities(numComplexities);
			importResultForm.setNumSeedTypes(numSeedTypes);
			importResultForm.setNumScopes(numScopes);
			importResultForm.setNumSeeds(numSeeds);
			importResultForm.setValidImport(true);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			importResultForm.setValidImport(false);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			importResultForm.setValidImport(false);
		}
		return importResultForm;
	}

	private int saveAdministrativeLevels(List<AdministrativeLevelForm> administrativeLevelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar niveles administrativos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		AdministrativeLevel administrationLevel = new AdministrativeLevel();
		for (AdministrativeLevelForm administrativeLevelForm : administrativeLevelFormList) {
			administrationLevel = new AdministrativeLevel();
			BeanUtils.copyProperties(administrationLevel, administrativeLevelForm);
			save(administrationLevel);
		}
		return administrativeLevelFormList != null ? administrativeLevelFormList.size() : 0;
	}

	private int saveClassificationLabels(List<ClassificationLabelForm> classificationLabelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar clasificación de etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (ClassificationLabelForm classificationLabelForm : classificationLabelFormList) {
			classificationLabel = new ClassificationLabel();
			BeanUtils.copyProperties(classificationLabel, classificationLabelForm);
			save(classificationLabel);
		}
		return classificationLabelFormList != null ? classificationLabelFormList.size() : 0;
	}

	private int saveSeedTypes(List<SeedTypeForm> seedTypeFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar tipos de semilla", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		SeedType seedType = new SeedType();
		for (SeedTypeForm seedTypeForm : seedTypeFormList) {
			seedType = new SeedType();
			BeanUtils.copyProperties(seedType, seedTypeForm);
			save(seedType);
		}
		return seedTypeFormList != null ? seedTypeFormList.size() : 0;
	}

	private int saveSegments(List<SegmentForm> segmentFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar segmentos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Segment segment = new Segment();
		for (SegmentForm segmentForm : segmentFormList) {
			segment = new Segment();
			BeanUtils.copyProperties(segment, segmentForm);
			save(segment);
		}
		return segmentFormList != null ? segmentFormList.size() : 0;
	}

	private int saveComplexities(List<ComplexityForm> complexityFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar complejidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Complexity complexity = new Complexity();
		for (ComplexityForm complexityForm : complexityFormList) {
			complexity = new Complexity();
			BeanUtils.copyProperties(complexity, complexityForm);
			save(complexity);
		}
		return complexityFormList != null ? complexityFormList.size() : 0;
	}

	private int saveLabels(List<LabelForm> labelFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Label label = new Label();
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (LabelForm labelForm : labelFormList) {
			classificationLabel = new ClassificationLabel();
			classificationLabel.setId(labelForm.getClasificacionEtiqueta().getId());
			label = new Label();
			label.setId(labelForm.getId());
			label.setNombre(labelForm.getNombre());
			label.setClasificacionEtiqueta(classificationLabel);
			save(label);
		}
		return labelFormList != null ? labelFormList.size() : 0;
	}

	private int saveScopes(List<ScopeForm> scopeFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar dependencias", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Scope scope = new Scope();
		AdministrativeLevel administrativeLevel = new AdministrativeLevel();
		for (ScopeForm scopeForm : scopeFormList) {
			// Admin Levels
			Set<AdministrativeLevel> administrativeLevels = new HashSet<>();
			if (scopeForm.getAmbitos() != null && scopeForm.getAmbitos().size() > 0) {
				Logger.putLog("Tiene ambitos: " + scopeForm.getNombre(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
				Set<AdministrativeLevelForm> administrativeLevelFormList = scopeForm.getAmbitos();
				for (AdministrativeLevelForm administrativeLevelForm : administrativeLevelFormList) {
					administrativeLevel = new AdministrativeLevel();
					administrativeLevel.setId(administrativeLevelForm.getId());
					administrativeLevel.setNombre(administrativeLevelForm.getNombre());
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
			save(scope);
		}
		return scopeFormList != null ? scopeFormList.size() : 0;
	}

	private int saveSeeds(List<SeedForm> seedFormList) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar semillas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Seed seed = new Seed();
		SeedType seedType = new SeedType();
		Complexity complexity = new Complexity();
		AdministrativeLevel administrativeLevel = new AdministrativeLevel();
		Scope dependency = new Scope();
		Segment segment = new Segment();
		ClassificationLabel classificationLabel = new ClassificationLabel();
		Label label = new Label();
		for (SeedForm seedForm : seedFormList) {
			// Seed type
			seedType = new SeedType();
			if (seedForm.getTipoSemilla() != null) {
				seedType.setId(seedForm.getTipoSemilla().getId());
			}
			// Admin level
			administrativeLevel = new AdministrativeLevel();
			if (seedForm.getAmbito() != null) {
				administrativeLevel.setId(seedForm.getAmbito().getId());
			}
			// Complexity
			complexity = new Complexity();
			if (seedForm.getComplejidad() != null) {
				complexity.setId(seedForm.getComplejidad().getId());
			}
			// Segment
			segment = new Segment();
			if (seedForm.getSegmento() != null) {
				segment.setId(seedForm.getSegmento().getId());
			}
			// Labels
			Set<Label> labels = new HashSet<>();
			if (seedForm.getEtiquetas() != null) {
				Set<LabelForm> labelFormList = seedForm.getEtiquetas();
				classificationLabel = new ClassificationLabel();
				label = new Label();
				for (LabelForm labelForm : labelFormList) {
					classificationLabel = new ClassificationLabel();
					classificationLabel.setId(labelForm.getClasificacionEtiqueta().getId());
					classificationLabel.setNombre(labelForm.getClasificacionEtiqueta().getNombre());
					label = new Label();
					label.setId(labelForm.getId());
					label.setNombre(labelForm.getNombre());
					label.setClasificacionEtiqueta(classificationLabel);
					labels.add(label);
				}
			}
			// Dependencies - scopes
			Set<ScopeForm> scopeFormList = seedForm.getDependencias();
			Set<Scope> dependencies = new HashSet<>();
			dependency = new Scope();
			for (ScopeForm scopeForm : scopeFormList) {
				dependency = new Scope();
				dependency.setId(scopeForm.getId());
				dependency.setNombre(scopeForm.getNombre());
				dependencies.add(dependency);
			}
			seed = new Seed();
			seed.setId(seedForm.getId());
			seed.setAcronimo(seedForm.getAcronimo());
			seed.setNombre(seedForm.getNombre());
			seed.setLista(seedForm.getLista());
			seed.setObservaciones(seedForm.getObservaciones());
			seed.setActiva(seedForm.isActiva());
			seed.setEliminada(seedForm.isEliminada());
			seed.setEnDirectorio(seedForm.isEnDirectorio());
			seed.setTipoSemilla(seedType);
			seed.setComplejidad(complexity);
			seed.setEtiquetas(labels);
			seed.setDependencias(dependencies);
			if (administrativeLevel.getId() != null) {
				seed.setAmbito(administrativeLevel);
			}
			if (segment.getId() != null) {
				seed.setSegmento(segment);
			}
			save(seed);
		}
		return seedFormList != null ? seedFormList.size() : 0;
	}
}