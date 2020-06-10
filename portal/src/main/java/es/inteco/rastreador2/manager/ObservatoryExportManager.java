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
package es.inteco.rastreador2.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.DatabaseExportDAO;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.export.database.Site;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.export.database.utils.ExportDataBaseUtils;

/**
 * The Class ObservatoryExportManager.
 */
public class ObservatoryExportManager extends BaseManager {
	/**
	 * Gets the observatory.
	 *
	 * @param idExecutionObs the id execution obs
	 * @return the observatory
	 * @throws Exception the exception
	 */
	public static ObservatoryForm getObservatory(Long idExecutionObs) throws Exception {
		Session session = getSession();
		Observatory observatory = DatabaseExportDAO.getObservatory(session, idExecutionObs);
		ObservatoryForm observatoryForm = null;
		if (observatory != null) {
			observatoryForm = ExportDataBaseUtils.getObservatoryForm(observatory);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return observatoryForm;
	}

	/**
	 * Gets the site category.
	 *
	 * @param idExecutionObs the id execution obs
	 * @return the site category
	 * @throws Exception the exception
	 */
	public static List<CategoryForm> getSiteCategory(Long idExecutionObs) throws Exception {
		Session session = getSession();
		List<Category> categories = DatabaseExportDAO.getCategoryInformation(session, idExecutionObs);
		List<CategoryForm> categoryFormList = new ArrayList<>();
		for (Category category : categories) {
			categoryFormList.add(ExportDataBaseUtils.getCategoryForm(category));
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return categoryFormList;
	}

	/**
	 * Gets the site information.
	 *
	 * @param idCategory the id category
	 * @return the site information
	 * @throws Exception the exception
	 */
	public static List<SiteForm> getSiteInformation(Long idCategory) throws Exception {
		Session session = getSession();
		List<Site> siteList = DatabaseExportDAO.getSiteInformation(session, idCategory);
		List<SiteForm> siteFormList = new ArrayList<>();
		for (Site site : siteList) {
			siteFormList.add(ExportDataBaseUtils.getSiteForm(site));
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return siteFormList;
	}
}
