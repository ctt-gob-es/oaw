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
package es.inteco.view.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import es.inteco.rastreador2.actionform.semillas.AmbitoForm;

/**
 * The Class ComplexityViewListForm.
 */
public class AmbitViewListForm {
	/** The category. */
	private AmbitoForm ambit;
	/** The view list. */
	private List<LabelValueBean> viewList;

	/**
	 * Instantiates a new complexity view list form.
	 */
	public AmbitViewListForm() {
		ambit = new AmbitoForm();
		viewList = new ArrayList<>();
	}

	/**
	 * Instantiates a new complexity view list form.
	 *
	 * @param ambit    the ambit
	 * @param viewList the view list
	 */
	public AmbitViewListForm(AmbitoForm ambit, List<LabelValueBean> viewList) {
		this.ambit = ambit;
		this.viewList = viewList;
	}

	/**
	 * Gets the ambit.
	 *
	 * @return the ambit
	 */
	public AmbitoForm getAmbit() {
		return ambit;
	}

	/**
	 * Sets the ambit.
	 *
	 * @param ambit the ambit to set
	 */
	public void setAmbit(AmbitoForm ambit) {
		this.ambit = ambit;
	}

	/**
	 * Gets the view list.
	 *
	 * @return the view list
	 */
	public List<LabelValueBean> getViewList() {
		return viewList;
	}

	/**
	 * Sets the view list.
	 *
	 * @param viewList the new view list
	 */
	public void setViewList(List<LabelValueBean> viewList) {
		this.viewList = viewList;
	}
}
