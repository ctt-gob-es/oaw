package es.inteco.rastreador2.manager.importacion.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

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
		boolean bExito = true;
		try {
			deleteData();
			saveData(loadFileData(formFile));
		} catch (Exception e) {
			bExito = false;
			Logger.putLog("Error: ", DatabaseImportarManager.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return bExito;
	}

	private OAWForm loadFileData(FormFile formFile) throws FileNotFoundException, IOException {
		Logger.putLog("Carga de datos desde fichero", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
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
		Logger.putLog("Inicio de borrado de entidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		// deleteAll(AdministrativeLevel.class);
		deleteAll(ClassificationLabel.class);
		deleteAll(Label.class);
		deleteAll(Complexity.class);
		deleteAll(SeedType.class);
		deleteAll(Segment.class);
		/*
		 * deleteAll(Scope.class); deleteAll(Seed.class);
		 */
		Logger.putLog("Fin de borrado de entidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
	}

	private void saveData(OAWForm oawExportDTO) {
		try {
			// saveAdministrativeLevels(oawExportDTO.getAmbitos());
			saveClassificationLabels(oawExportDTO.getClasificacionEtiquetas());
			saveLabels(oawExportDTO.getEtiquetas());
			saveComplexities(oawExportDTO.getComplejidades());
			saveSeedTypes(oawExportDTO.getTipoSemillas());
			saveSegments(oawExportDTO.getSegmentos());
			/**
			 * saveScopes(oawExportDTO.getDependencias()); saveSeeds(oawExportDTO.getSemillas());
			 * 
			 **/
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveAdministrativeLevels(List<AdministrativeLevelForm> nivelesAdministrativos) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar niveles administrativos", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		AdministrativeLevel administrationLevel = new AdministrativeLevel();
		for (AdministrativeLevelForm nivelAdministrativo : nivelesAdministrativos) {
			administrationLevel = new AdministrativeLevel();
			BeanUtils.copyProperties(administrationLevel, nivelAdministrativo);
			save(administrationLevel);
		}
	}

	private void saveClassificationLabels(List<ClassificationLabelForm> clasificacionEtiquetas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar clasificación de etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (ClassificationLabelForm clasificacionEtiqueta : clasificacionEtiquetas) {
			classificationLabel = new ClassificationLabel();
			BeanUtils.copyProperties(classificationLabel, clasificacionEtiqueta);
			save(classificationLabel);
		}
	}

	private void saveComplexities(List<ComplexityForm> complejidades) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar Complejidades", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		Complexity complexity = new Complexity();
		for (ComplexityForm complejidad : complejidades) {
			complexity = new Complexity();
			BeanUtils.copyProperties(complexity, complejidad);
			save(complexity);
		}
	}

	private void saveLabels(List<LabelForm> etiquetas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar Etiquetas", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		Label label = new Label();
		ClassificationLabel classificationLabel = new ClassificationLabel();
		for (LabelForm etiqueta : etiquetas) {
			// BeanUtils.copyProperties(label, etiqueta);
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
		Logger.putLog("Importar ámbitos", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		Scope scope = new Scope();
		for (ScopeForm ambito : ambitos) {
			scope = new Scope();
			BeanUtils.copyProperties(scope, ambito);
			save(scope);
		}
	}

	private void saveSeeds(List<SeedForm> semillas) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar semillas", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		Seed seed = new Seed();
		for (SeedForm semilla : semillas) {
			seed = new Seed();
			BeanUtils.copyProperties(seed, semilla);
			save(seed);
		}
	}

	private void saveSeedTypes(List<SeedTypeForm> tiposSemilla) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar tipos de semilla", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		SeedType seedType = new SeedType();
		for (SeedTypeForm tipoSemilla : tiposSemilla) {
			seedType = new SeedType();
			BeanUtils.copyProperties(seedType, tipoSemilla);
			save(seedType);
		}
	}

	private void saveSegments(List<SegmentForm> segmentos) throws IllegalAccessException, InvocationTargetException {
		Logger.putLog("Importar segmentos", DatabaseImportarManager.class, Logger.LOG_LEVEL_INFO);
		Segment segment = new Segment();
		for (SegmentForm segmento : segmentos) {
			segment = new Segment();
			BeanUtils.copyProperties(segment, segmento);
			save(segment);
		}
	}
}