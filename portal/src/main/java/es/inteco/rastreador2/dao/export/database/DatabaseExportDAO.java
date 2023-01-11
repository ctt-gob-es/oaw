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
package es.inteco.rastreador2.dao.export.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.inteco.rastreador2.dao.BaseDAO;
import es.inteco.rastreador2.dao.importar.database.AdministrativeLevel;
import es.inteco.rastreador2.dao.importar.database.ClassificationLabel;
import es.inteco.rastreador2.dao.importar.database.Complexity;
import es.inteco.rastreador2.dao.importar.database.Label;
import es.inteco.rastreador2.dao.importar.database.Scope;
import es.inteco.rastreador2.dao.importar.database.Seed;
import es.inteco.rastreador2.dao.importar.database.SeedType;
import es.inteco.rastreador2.dao.importar.database.Segment;

/**
 * The Class DatabaseExportDAO.
 */
public class DatabaseExportDAO extends BaseDAO {
	/**
	 * Gets the observatory.
	 *
	 * @param session     the session
	 * @param idExecution the id execution
	 * @return the observatory
	 */
	public static Observatory getObservatory(Session session, Long idExecution) {
		Criteria criteria = session.createCriteria(Observatory.class);
		criteria.add(Restrictions.eq("idExecution", idExecution));
		List<Observatory> observatories = criteria.list();
		if (observatories != null && !observatories.isEmpty()) {
			return observatories.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets the site information.
	 *
	 * @param session    the session
	 * @param idCategory the id category
	 * @return the site information
	 */
	public static List<Site> getSiteInformation(Session session, Long idCategory) {
		Criteria criteria = session.createCriteria(Site.class);
		criteria.add(Restrictions.eq("idCategory", idCategory));
		List<Site> sites = criteria.list();
		return sites;
	}

	/**
	 * Gets the category information.
	 *
	 * @param session        the session
	 * @param idExecutionObs the id execution obs
	 * @return the category information
	 */
	public static List<Category> getCategoryInformation(Session session, Long idExecutionObs) {
		Criteria criteria = session.createCriteria(Category.class);
		criteria.add(Restrictions.eq("idExecution", idExecutionObs));
		List<Category> categories = criteria.list();
		return categories;
	}
	// Backup

	public static List<Complexity> getComplexityInformation(Session session) {
		Criteria criteria = session.createCriteria(Complexity.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Complexity> complexities = criteria.list();
		return complexities;
	}

	public static List<ClassificationLabel> getClassificationLabelInformation(Session session) {
		Criteria criteria = session.createCriteria(ClassificationLabel.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ClassificationLabel> classificationLabels = criteria.list();
		return classificationLabels;
	}

	public static List<Label> getLabelInformation(Session session) {
		Criteria criteria = session.createCriteria(Label.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Label> labels = criteria.list();
		return labels;
	}

	public static List<AdministrativeLevel> getAdministrativeLevelInformation(Session session) {
		Criteria criteria = session.createCriteria(AdministrativeLevel.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<AdministrativeLevel> administrativeLevels = criteria.list();
		return administrativeLevels;
	}

	public static List<SeedType> getSeedTypeInformation(Session session) {
		Criteria criteria = session.createCriteria(SeedType.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<SeedType> seedTypes = criteria.list();
		return seedTypes;
	}

	public static List<Segment> getSegmentInformation(Session session) {
		Criteria criteria = session.createCriteria(Segment.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Segment> segments = criteria.list();
		return segments;
	}

	public static List<Scope> getScopeInformation(Session session) {
		Criteria criteria = session.createCriteria(Scope.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Scope> scopes = criteria.list();
		return scopes;
	}

	public static List<Seed> getSeedInformation(Session session) {
		Criteria criteria = session.createCriteria(Seed.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Seed> scopes = criteria.list();
		return scopes;
	}
}
