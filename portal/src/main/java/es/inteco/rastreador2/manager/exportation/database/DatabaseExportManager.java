/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.manager.exportation.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.inteco.rastreador2.dao.export.database.DatabaseExportDAO;
import es.inteco.rastreador2.dao.export.database.Observatory;
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
 * The Class DatabaseExportManager.
 */
public class DatabaseExportManager extends BaseManager {
	/**
	 * Gets the observatory.
	 *
	 * @param idExecution the id execution
	 * @return the observatory
	 */
	public static Observatory getObservatory(Long idExecution) {
		Session session = getSession();
		Observatory observatory = DatabaseExportDAO.getObservatory(session, idExecution);
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return observatory;
	}

	/**
	 * Entities backup
	 * 
	 * @return Entities backup
	 * @throws Exception
	 */
	public String backup() throws Exception {
		OAWForm oawForm = new OAWForm();
		try {
			oawForm.setComplejidades(getComplejidades());
			oawForm.setClasificacionEtiquetas(getClasificacionEtiquetas());
			oawForm.setEtiquetas(getEtiquetas());
			oawForm.setSegmentos(getSegmentos());
			oawForm.setTipoSemillas(getTiposSemilla());
			oawForm.setAmbitos(getAmbitos());
			oawForm.setDependencias(getDependencias());
			oawForm.setSemillas(getSemillas());
		} catch (Exception e) {
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(oawForm);
	}

	private static List<ComplexityForm> getComplejidades() throws Exception {
		Session session = getSession();
		List<Complexity> complexityList = DatabaseExportDAO.getComplexityInformation(session);
		List<ComplexityForm> complexityFormList = new ArrayList<>();
		ComplexityForm complexityForm;
		for (Complexity complexity : complexityList) {
			complexityForm = new ComplexityForm();
			BeanUtils.copyProperties(complexityForm, complexity);
			complexityFormList.add(complexityForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return complexityFormList;
	}

	private static List<ClassificationLabelForm> getClasificacionEtiquetas() throws Exception {
		Session session = getSession();
		List<ClassificationLabel> classificationLabelList = DatabaseExportDAO.getClassificationLabelInformation(session);
		List<ClassificationLabelForm> classificationLabelFormList = new ArrayList<>();
		ClassificationLabelForm classificationLabelForm;
		for (ClassificationLabel classificationLabel : classificationLabelList) {
			classificationLabelForm = new ClassificationLabelForm();
			BeanUtils.copyProperties(classificationLabelForm, classificationLabel);
			classificationLabelFormList.add(classificationLabelForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return classificationLabelFormList;
	}

	private static List<SegmentForm> getSegmentos() throws Exception {
		Session session = getSession();
		List<Segment> segmentList = DatabaseExportDAO.getSegmentInformation(session);
		List<SegmentForm> segmentFormList = new ArrayList<>();
		SegmentForm segmentForm;
		for (Segment segment : segmentList) {
			segmentForm = new SegmentForm();
			BeanUtils.copyProperties(segmentForm, segment);
			segmentFormList.add(segmentForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return segmentFormList;
	}

	private static List<SeedTypeForm> getTiposSemilla() throws Exception {
		Session session = getSession();
		List<SeedType> seedTypeList = DatabaseExportDAO.getSeedTypeInformation(session);
		List<SeedTypeForm> seedTypeFormList = new ArrayList<>();
		SeedTypeForm seedTypeForm;
		for (SeedType seedType : seedTypeList) {
			seedTypeForm = new SeedTypeForm();
			BeanUtils.copyProperties(seedTypeForm, seedType);
			seedTypeFormList.add(seedTypeForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return seedTypeFormList;
	}

	private static List<AdministrativeLevelForm> getAmbitos() throws Exception {
		Session session = getSession();
		List<AdministrativeLevel> administrativeLevelList = DatabaseExportDAO.getAdministrativeLevelInformation(session);
		List<AdministrativeLevelForm> administrativeLevelFormList = new ArrayList<>();
		AdministrativeLevelForm administrativeLevelForm;
		for (AdministrativeLevel administrativeLevel : administrativeLevelList) {
			administrativeLevelForm = new AdministrativeLevelForm();
			BeanUtils.copyProperties(administrativeLevelForm, administrativeLevel);
			administrativeLevelFormList.add(administrativeLevelForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return administrativeLevelFormList;
	}

	private static List<LabelForm> getEtiquetas() throws Exception {
		Session session = getSession();
		List<Label> labelList = DatabaseExportDAO.getLabelInformation(session);
		List<LabelForm> labelFormList = new ArrayList<>();
		LabelForm labelForm;
		ClassificationLabelForm classificationLabelForm;
		for (Label label : labelList) {
			classificationLabelForm = new ClassificationLabelForm();
			classificationLabelForm.setId(label.getClasificacionEtiqueta().getId());
			labelForm = new LabelForm();
			labelForm.setId(label.getId());
			labelForm.setNombre(label.getNombre());
			labelForm.setClasificacionEtiqueta(classificationLabelForm);
			labelFormList.add(labelForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return labelFormList;
	}

	private static List<ScopeForm> getDependencias() throws Exception {
		Session session = getSession();
		List<Scope> scopeList = DatabaseExportDAO.getScopeInformation(session);
		List<ScopeForm> scopeFormList = new ArrayList<>();
		ScopeForm scopeForm;
		for (Scope scope : scopeList) {
			scopeForm = new ScopeForm();
			Set<AdministrativeLevelForm> administrativeLevelFormSet = null;
			if (Objects.nonNull(scope.getAmbitos()) && scope.getAmbitos().size() > 0) {
				administrativeLevelFormSet = new HashSet<>();
				for (AdministrativeLevel administrativeLevel : scope.getAmbitos()) {
					AdministrativeLevelForm administrativeLevelForm = new AdministrativeLevelForm();
					administrativeLevelForm.setId(administrativeLevel.getId());
					administrativeLevelFormSet.add(administrativeLevelForm);
				}
			}
			if (Objects.nonNull(scope.getEtiqueta())) {
				LabelForm labelForm = new LabelForm();
				ClassificationLabelForm classificationLabelForm = new ClassificationLabelForm();
				if (Objects.nonNull(scope.getEtiqueta().getClasificacionEtiqueta())) {
					classificationLabelForm.setId(scope.getEtiqueta().getClasificacionEtiqueta().getId());
					labelForm.setClasificacionEtiqueta(classificationLabelForm);
				}
				labelForm.setId(scope.getEtiqueta().getId());
				scopeForm.setEtiqueta(labelForm);
			}
			scopeForm.setId(scope.getId());
			scopeForm.setNombre(scope.getNombre());
			scopeForm.setEnvioAutomatico(scope.isEnvioAutomatico());
			scopeForm.setOficial(scope.isOficial());
			scopeForm.setEmails(scope.getEmails());
			scopeForm.setAcronimo(scope.getAcronimo());
			scopeForm.setAmbitos(administrativeLevelFormSet);
			scopeFormList.add(scopeForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return scopeFormList;
	}

	private static List<SeedForm> getSemillas() throws Exception {
		Session session = getSession();
		List<Seed> seedList = DatabaseExportDAO.getSeedInformation(session);
		List<SeedForm> seedFormList = new ArrayList<>();
		SeedForm seedForm;
		for (Seed seed : seedList) {
			// Seed type
			SeedTypeForm seedTypeForm = new SeedTypeForm();
			if (Objects.nonNull(seed.getTipoSemilla())) {
				seedTypeForm.setId(seed.getTipoSemilla().getId());
			}
			// Admin level
			AdministrativeLevelForm administrativeLevelForm = new AdministrativeLevelForm();
			if (Objects.nonNull(seed.getAmbito())) {
				administrativeLevelForm.setId(seed.getAmbito().getId());
			}
			// Complexity
			ComplexityForm complexityForm = new ComplexityForm();
			if (Objects.nonNull(seed.getComplejidad())) {
				complexityForm.setId(seed.getComplejidad().getId());
			}
			// Segment
			SegmentForm segmentForm = new SegmentForm();
			if (Objects.nonNull(seed.getSegmento())) {
				segmentForm.setId(seed.getSegmento().getId());
			}
			// Labels
			Set<LabelForm> labelsForm = new HashSet<>();
			if (Objects.nonNull(seed.getEtiquetas())) {
				Set<Label> labelList = seed.getEtiquetas();
				LabelForm labelForm;
				ClassificationLabelForm classificationLabelForm;
				for (Label label : labelList) {
					classificationLabelForm = new ClassificationLabelForm();
					classificationLabelForm.setId(label.getClasificacionEtiqueta().getId());
					labelForm = new LabelForm();
					labelForm.setId(label.getId());
					labelForm.setClasificacionEtiqueta(classificationLabelForm);
					labelsForm.add(labelForm);
				}
			}
			// Dependencies - scopes
			Set<Scope> scopeList = seed.getDependencias();
			Set<ScopeForm> dependencyFormSet = new HashSet<>();
			ScopeForm dependencyForm;
			for (Scope scope : scopeList) {
				dependencyForm = new ScopeForm();
				dependencyForm.setId(scope.getId());
				dependencyFormSet.add(dependencyForm);
			}
			seedForm = new SeedForm();
			seedForm.setId(seed.getId());
			seedForm.setTipoSemilla(seedTypeForm);
			seedForm.setAcronimo(seed.getAcronimo());
			seedForm.setNombre(seed.getNombre());
			seedForm.setLista(seed.getLista());
			seedForm.setObservacionesRastreador(seed.getObservaciones());
			seedForm.setActiva(seed.isActiva());
			seedForm.setEliminada(seed.isEliminada());
			seedForm.setEnDirectorio(seed.isEnDirectorio());
			seedForm.setComplejidad(complexityForm);
			if (labelsForm.size() > 0) {
				seedForm.setEtiquetas(labelsForm);
			}
			if (dependencyFormSet.size() > 0) {
				seedForm.setDependencias(dependencyFormSet);
			}
			if (Objects.nonNull(administrativeLevelForm.getId())) {
				seedForm.setAmbito(administrativeLevelForm);
			}
			if (Objects.nonNull(segmentForm.getId())) {
				seedForm.setSegmento(segmentForm);
			}
			seedFormList.add(seedForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return seedFormList;
	}
}
