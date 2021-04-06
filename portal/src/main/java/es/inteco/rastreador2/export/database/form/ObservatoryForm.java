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
package es.inteco.rastreador2.export.database.form;

import java.util.List;

/**
 * The Class ObservatoryForm.
 */
public class ObservatoryForm {
    
    /** The id execution. */
    private String idExecution;
    
    /** The name. */
    private String name;
    
    /** The date. */
    private String date;
    
    /** The num AA. */
    private String numAA;
    
    /** The num A. */
    private String numA;
    
    /** The num NV. */
    private String numNV;
    
    /** The category form list. */
    private List<CategoryForm> categoryFormList;

    /**
	 * Gets the id execution.
	 *
	 * @return the id execution
	 */
    public String getIdExecution() {
        return idExecution;
    }

    /**
	 * Sets the id execution.
	 *
	 * @param idExecution the new id execution
	 */
    public void setIdExecution(String idExecution) {
        this.idExecution = idExecution;
    }

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets the date.
	 *
	 * @return the date
	 */
    public String getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(String date) {
        this.date = date;
    }

    /**
	 * Gets the num AA.
	 *
	 * @return the num AA
	 */
    public String getNumAA() {
        return numAA;
    }

    /**
	 * Sets the num AA.
	 *
	 * @param numAA the new num AA
	 */
    public void setNumAA(String numAA) {
        this.numAA = numAA;
    }

    /**
	 * Gets the num A.
	 *
	 * @return the num A
	 */
    public String getNumA() {
        return numA;
    }

    /**
	 * Sets the num A.
	 *
	 * @param numA the new num A
	 */
    public void setNumA(String numA) {
        this.numA = numA;
    }

    /**
	 * Gets the num NV.
	 *
	 * @return the num NV
	 */
    public String getNumNV() {
        return numNV;
    }

    /**
	 * Sets the num NV.
	 *
	 * @param numNV the new num NV
	 */
    public void setNumNV(String numNV) {
        this.numNV = numNV;
    }

    /**
	 * Gets the category form list.
	 *
	 * @return the category form list
	 */
    public List<CategoryForm> getCategoryFormList() {
        return categoryFormList;
    }

    /**
	 * Sets the category form list.
	 *
	 * @param categoryFormList the new category form list
	 */
    public void setCategoryFormList(List<CategoryForm> categoryFormList) {
        this.categoryFormList = categoryFormList;
    }

}
