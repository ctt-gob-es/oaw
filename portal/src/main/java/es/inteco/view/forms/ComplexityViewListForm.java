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

import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;

/**
 * The Class ComplexityViewListForm.
 */
public class ComplexityViewListForm {

    /** The category. */
    private ComplejidadForm complexity;
    
    /** The view list. */
    private List<LabelValueBean> viewList;

    /**
	 * Instantiates a new complexity view list form.
	 */
    public ComplexityViewListForm() {
    	complexity = new ComplejidadForm();
        viewList = new ArrayList<>();
    }

    /**
	 * Instantiates a new complexity view list form.
	 *
	 * @param complexity the complexity
	 * @param viewList   the view list
	 */
    public ComplexityViewListForm(ComplejidadForm complexity, List<LabelValueBean> viewList) {
        this.complexity = complexity;
        this.viewList = viewList;
    }

    /**
	 * Gets the category.
	 *
	 * @return the category
	 */
    public ComplejidadForm getCategory() {
        return complexity;
    }

    /**
	 * Sets the category.
	 *
	 * @param complexity the new category
	 */
    public void setCategory(ComplejidadForm complexity) {
        this.complexity = complexity;
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
