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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.inteco.rastreador2.dao.BaseDAO;

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
		criteria.add(Restrictions.eq("id", id));
		ApiKey apiKey = (ApiKey) criteria.uniqueResult();
		return apiKey;
	}

	public static ApiKey getApiKey(Session session, String apiKey) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("apiKey", apiKey));
		ApiKey aKey = (ApiKey) criteria.uniqueResult();
		return aKey;
	}

	public static List<ApiKey> getApiKeys(Session session) {
		Criteria criteria = session.createCriteria(ApiKey.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<ApiKey>) criteria.list();
	}

	public static int getNumApiKeys(Session session) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.setProjection(Projections.rowCount());
		return (int) criteria.uniqueResult();
	}

	public static boolean existsApiKey(Session session, String apiKey) {
		Criteria criteria = session.createCriteria(ApiKey.class);
		criteria.add(Restrictions.eq("apiKey", apiKey));
		List<ApiKey> resultList = criteria.list();
		return !resultList.isEmpty();
	}
}
