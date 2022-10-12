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

public class DatabaseImportarManager extends BaseManager {
	public DatabaseImportarManager() {
	}

	public boolean importData(FormFile formFile) {
		boolean rightImport = true;
		try {
			deleteData();
			saveData(loadFileData(formFile));
		} catch (Exception e) {
			rightImport = false;
			Logger.putLog("Error: ", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return rightImport;
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
		try {
			Logger.putLog("Inicio de borrado de entidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			Logger.putLog("Borrado Dependencias", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(Scope.class);
			Logger.putLog("Borrado Clasificación etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(ClassificationLabel.class);
			Logger.putLog("Borrado Etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(Label.class);
			Logger.putLog("Borrado Semillas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(Seed.class);
			Logger.putLog("Borrado Segmentos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(Segment.class);
			Logger.putLog("Borrado Ámbitos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(AdministrativeLevel.class);
			Logger.putLog("Borrado Complejidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(Complexity.class);
			Logger.putLog("Borrado Tipo Semillas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
			deleteAll(SeedType.class);
			Logger.putLog("Fin de borrado de entidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		}
	}

	private void saveData(OAWForm oawExportDTO) {
		try {
			saveSegments(oawExportDTO.getSegmentos());
			saveAdministrativeLevels(oawExportDTO.getAmbitos());
			saveClassificationLabels(oawExportDTO.getClasificacionEtiquetas());
			saveLabels(oawExportDTO.getEtiquetas());
			saveComplexities(oawExportDTO.getComplejidades());
			saveSeedTypes(oawExportDTO.getTipoSemillas());
			saveScopes(oawExportDTO.getDependencias());
			saveSeeds(oawExportDTO.getSemillas());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Logger.putLog(e.getMessage(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		}
	}

	private void saveAdministrativeLevels(List<AdministrativeLevelForm> nivelesAdministrativos) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar niveles administrativos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		AdministrativeLevel administrationLevel = new AdministrativeLevel();
		for (AdministrativeLevelForm nivelAdministrativo : nivelesAdministrativos) {
			administrationLevel = new AdministrativeLevel();
			BeanUtils.copyProperties(administrationLevel, nivelAdministrativo);
			save(administrationLevel);
		}
	}

	private void saveClassificationLabels(List<ClassificationLabelForm> clasificacionEtiquetas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar clasificación de etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (ClassificationLabelForm clasificacionEtiqueta : clasificacionEtiquetas) {
			classificationLabel = new ClassificationLabel();
			BeanUtils.copyProperties(classificationLabel, clasificacionEtiqueta);
			save(classificationLabel);
		}
	}

	private void saveComplexities(List<ComplexityForm> complejidades) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar Complejidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Complexity complexity = new Complexity();
		for (ComplexityForm complejidad : complejidades) {
			complexity = new Complexity();
			BeanUtils.copyProperties(complexity, complejidad);
			save(complexity);
		}
	}

	private void saveLabels(List<LabelForm> etiquetas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar Etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Label label = new Label();
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (LabelForm etiqueta : etiquetas) {
			classificationLabel = new ClassificationLabel();
			classificationLabel.setId(etiqueta.getClasificacionEtiqueta().getId());
			classificationLabel.setNombre(etiqueta.getClasificacionEtiqueta().getNombre());
			label = new Label();
			label.setId(etiqueta.getId());
			label.setNombre(etiqueta.getNombre());
			label.setClasificacionEtiqueta(classificationLabel);
			save(label);
		}
	}

	private void saveScopes(List<ScopeForm> ambitos) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar dependencias", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Scope scope = new Scope();
		for (ScopeForm ambito : ambitos) {
			scope = new Scope();
			scope.setId(ambito.getId());
			scope.setNombre(ambito.getNombre());
			scope.setEmails(ambito.getEmails());
			scope.setEnvioAutomatico(ambito.isEnvioAutomatico());
			scope.setOficial(ambito.isOficial());
			save(scope);
		}
	}

	private void saveSeeds(List<SeedForm> semillas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar semillas", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Seed seed = new Seed();
		SeedType seedType = new SeedType();
		Complexity complexity = new Complexity();
		AdministrativeLevel administrativeLevel = new AdministrativeLevel();
		Segment segment = new Segment();
		for (SeedForm semilla : semillas) {
			// Seed type
			seedType = new SeedType();
			seedType.setId(semilla.getTipoSemilla().getId());
			seedType.setNombre(semilla.getTipoSemilla().getNombre());
			// Admin level
			administrativeLevel = new AdministrativeLevel();
			if (semilla.getAmbito() != null) {
				administrativeLevel.setId(semilla.getAmbito().getId());
				administrativeLevel.setNombre(semilla.getAmbito().getNombre());
				administrativeLevel.setDescripcion(semilla.getAmbito().getDescripcion());
			}
			// Complexity
			complexity = new Complexity();
			if (semilla.getComplejidad() != null) {
				complexity.setId(semilla.getComplejidad().getId());
				complexity.setNombre(semilla.getComplejidad().getNombre());
				complexity.setProfundidad(semilla.getComplejidad().getProfundidad());
			}
			// Segment
			segment = new Segment();
			if (semilla.getSegmento() != null) {
				segment.setId(semilla.getSegmento().getId());
				segment.setNombre(semilla.getSegmento().getNombre());
				segment.setClave(semilla.getSegmento().getClave());
				segment.setOrden(semilla.getSegmento().getOrden());
				segment.setPrincipal(semilla.getSegmento().getPrincipal());
			}
			// Labels
			Set<Label> etiquetas = new HashSet<>();
			if (semilla.getEtiquetas() != null) {
				Set<LabelForm> labels = semilla.getEtiquetas();
				ClassificationLabel classificationLabel = new ClassificationLabel();
				Label etiqueta = new Label();
				for (LabelForm label : labels) {
					classificationLabel = new ClassificationLabel();
					classificationLabel.setId(label.getClasificacionEtiqueta().getId());
					classificationLabel.setNombre(label.getClasificacionEtiqueta().getNombre());
					etiqueta = new Label();
					etiqueta.setId(label.getId());
					etiqueta.setNombre(label.getNombre());
					etiqueta.setClasificacionEtiqueta(classificationLabel);
					etiquetas.add(etiqueta);
				}
			}
			// Dependencies - scopes
			Set<ScopeForm> dependencias = semilla.getDependencias();
			Set<Scope> dependencies = new HashSet<>();
			Scope dependency = new Scope();
			for (ScopeForm dependencia : dependencias) {
				dependency = new Scope();
				dependency.setId(dependencia.getId());
				dependency.setEmails(dependencia.getEmails());
				dependency.setEnvioAutomatico(dependencia.isEnvioAutomatico());
				dependency.setOficial(dependencia.isOficial());
				dependency.setNombre(dependencia.getNombre());
				dependencies.add(dependency);
			}
			seed = new Seed();
			seed.setId(semilla.getId());
			seed.setAcronimo(semilla.getAcronimo());
			seed.setNombre(semilla.getNombre());
			seed.setLista(semilla.getLista());
			seed.setObservaciones(semilla.getObservaciones());
			seed.setActiva(semilla.isActiva());
			seed.setEliminada(semilla.isEliminada());
			seed.setEnDirectorio(semilla.isEnDirectorio());
			seed.setTipoSemilla(seedType);
			seed.setComplejidad(complexity);
			seed.setEtiquetas(etiquetas);
			seed.setDependencias(dependencies);
			if (administrativeLevel.getId() != null) {
				// Logger.putLog("--> Admin level " + administrativeLevel.getId() + " - " + administrativeLevel.getNombre(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
				seed.setAmbito(administrativeLevel);
			}
			if (segment.getId() != null) {
				// Logger.putLog("--> Segmento " + segment.getId() + " - " + segment.getNombre(), DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
				seed.setSegmento(segment);
			}
			save(seed);
		}
	}

	private void saveSeedTypes(List<SeedTypeForm> tiposSemilla) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar tipos de semilla", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		SeedType seedType = new SeedType();
		for (SeedTypeForm tipoSemilla : tiposSemilla) {
			seedType = new SeedType();
			BeanUtils.copyProperties(seedType, tipoSemilla);
			save(seedType);
		}
	}

	private void saveSegments(List<SegmentForm> segmentos) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar segmentos", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR);
		Segment segment = new Segment();
		for (SegmentForm segmento : segmentos) {
			segment = new Segment();
			BeanUtils.copyProperties(segment, segmento);
			save(segment);
		}
	}
}