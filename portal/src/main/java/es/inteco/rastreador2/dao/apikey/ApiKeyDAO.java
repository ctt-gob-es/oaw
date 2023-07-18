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
package es.inteco.rastreador2.dao.apikey;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.inteco.rastreador2.dao.BaseDAO;
import es.inteco.rastreador2.export.database.form.ApiKeyForm;

/**
 * The Class ApiKeyDAO.
 */
public class ApiKeyDAO extends BaseDAO {
	/**
	 * Gets the ApiKey.
	 *
	 * @param session the session
	 * @param id      the id of the apiKey
	 * @return the ApiKey
	 */
	public static ApiKey getApiKey(Session session, Long id) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("idApiKey", id));
		ApiKey apiKey = (ApiKey) criteria.uniqueResult();
		return apiKey;
	}

	public static ApiKeyForm getApiKeyForm(Session session, Long id) {
		ApiKeyForm form = new ApiKeyForm();
		ApiKey apiKey = getApiKey(session, id);
		form.setDescription(apiKey.getDescription());
		form.setId(apiKey.getId());
		form.setName(apiKey.getName());
		form.setType(apiKey.getType());
		return form;
	}

	public static List<ApiKey> getApiKeys(Session session) {
		Criteria criteria = session.createCriteria(ApiKey.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<ApiKey>) criteria.list();
	}

	public static List<ApiKeyForm> getApiKeyForms(Session session) {
		List<ApiKey> apiKeys = getApiKeys(session);
		List<ApiKeyForm> apiKeyForms = new ArrayList<>();
		for (ApiKey apiKey : apiKeys) {
			ApiKeyForm form = new ApiKeyForm();
			form.setId(apiKey.getId());
			form.setDescription(apiKey.getDescription());
			form.setName(apiKey.getName());
			form.setType(apiKey.getType());
			apiKeyForms.add(form);
		}
		return apiKeyForms;
	}

	public static int getApiKeySize(Session session) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.setProjection(Projections.rowCount());
		return (int) criteria.uniqueResult();
	}

	public static void deleteApiKey(Session session, Long id) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("idApiKey", id));
		List<ApiKey> resultList = criteria.list();
		if (!resultList.isEmpty()) {
			ApiKey elementToDelete = resultList.get(0);
			session.delete(elementToDelete);
		}
	}

	public static void updateApiKey(Session session, ApiKeyForm apiKeyForm) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("idApiKey", apiKeyForm.getId()));
		List<ApiKey> resultList = criteria.list();
		if (!resultList.isEmpty()) {
			ApiKey elementToUpdate = resultList.get(0);
			elementToUpdate.setName(apiKeyForm.getName());
			elementToUpdate.setDescription(apiKeyForm.getDescription());
			elementToUpdate.setType(apiKeyForm.getType());
			session.update(elementToUpdate);
		}
	}

	public static void saveApiKey(Session session, ApiKeyForm apiKeyForm) {
		ApiKey newApiKey = new ApiKey();
		newApiKey.setName(apiKeyForm.getName());
		newApiKey.setDescription(apiKeyForm.getDescription());
		newApiKey.setType(apiKeyForm.getType());
		session.save(newApiKey);
	}

	public static boolean existsApiKey(Session session, String name) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("name", name));
		List<ApiKey> resultList = criteria.list();
		return !resultList.isEmpty();
	}
}
