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

import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CategoryViewListForm.
 */
public class CategoryViewListForm {

    /** The category. */
    private CategoriaForm category;
    
    /** The view list. */
    private List<LabelValueBean> viewList;

    /**
	 * Instantiates a new category view list form.
	 */
    public CategoryViewListForm() {
        category = new CategoriaForm();
        viewList = new ArrayList<>();
    }

    /**
	 * Instantiates a new category view list form.
	 *
	 * @param category the category
	 * @param viewList the view list
	 */
    public CategoryViewListForm(CategoriaForm category, List<LabelValueBean> viewList) {
        this.category = category;
        this.viewList = viewList;
    }

    /**
	 * Gets the category.
	 *
	 * @return the category
	 */
    public CategoriaForm getCategory() {
        return category;
    }

    /**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
    public void setCategory(CategoriaForm category) {
        this.category = category;
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
