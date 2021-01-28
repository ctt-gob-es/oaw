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
package es.inteco.rastreador2.export.database.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.export.database.Page;
import es.inteco.rastreador2.dao.export.database.Site;
import es.inteco.rastreador2.dao.export.database.VerificationModality;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.PageForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.export.database.form.VerificationModalityForm;

/**
 * The Class ExportDataBaseUtils.
 */
public final class ExportDataBaseUtils {
	/**
	 * Instantiates a new export data base utils.
	 */
	private ExportDataBaseUtils() {
	}

	/**
	 * Gets the observatory form.
	 *
	 * @param observatory the observatory
	 * @return the observatory form
	 * @throws Exception the exception
	 */
	public static ObservatoryForm getObservatoryForm(Observatory observatory) throws Exception {
		ObservatoryForm observatoryForm = new ObservatoryForm();
		BeanUtils.copyProperties(observatoryForm, observatory);
		List<CategoryForm> categoryFormList = new ArrayList<>();
		if (observatory.getCategoryList() != null) {
			for (Category category : observatory.getCategoryList()) {
				categoryFormList.add(getCategoryForm(category));
			}
		}
		observatoryForm.setCategoryFormList(categoryFormList);
		return observatoryForm;
	}

	/**
	 * Gets the category form.
	 *
	 * @param category the category
	 * @return the category form
	 * @throws Exception the exception
	 */
	public static CategoryForm getCategoryForm(Category category) throws Exception {
		CategoryForm categoryForm = new CategoryForm();
		BeanUtils.copyProperties(categoryForm, category);
		List<SiteForm> siteFormList = new ArrayList<>();
		for (Site site : category.getSiteList()) {
			siteFormList.add(getSiteForm(site));
		}
		categoryForm.setSiteFormList(siteFormList);
		return categoryForm;
	}

	/**
	 * Gets the site form.
	 *
	 * @param site the site
	 * @return the site form
	 * @throws Exception the exception
	 */
	public static SiteForm getSiteForm(Site site) throws Exception {
		SiteForm siteForm = new SiteForm();
		BeanUtils.copyProperties(siteForm, site);
		List<PageForm> pageFormList = new ArrayList<>();
		for (Page page : site.getPageList()) {
			PageForm pageForm = new PageForm();
			BeanUtils.copyProperties(pageForm, page);
			pageFormList.add(pageForm);
		}
		List<VerificationModalityForm> verificationModalityFormList = new ArrayList<>();
		for (VerificationModality verification : site.getVerificationModalityList()) {
			verificationModalityFormList.add(getVerificationModalityForm(verification));
		}
		siteForm.setPageList(pageFormList);
		return siteForm;
	}

	/**
	 * Gets the verification modality form.
	 *
	 * @param verification the verification
	 * @return the verification modality form
	 * @throws Exception the exception
	 */
	public static VerificationModalityForm getVerificationModalityForm(VerificationModality verification) throws Exception {
		VerificationModalityForm verificationForm = new VerificationModalityForm();
		BeanUtils.copyProperties(verificationForm, verification);
		return verificationForm;
	}
}
