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
package es.inteco.intav.form;

import java.io.Serializable;

/**
 * The Class SeedForm.
 */
public class SeedForm implements Serializable {
    
    /** The id. */
    private String id;
    
    /** The name. */
    private String name;
    
    /** The acronym. */
    private String acronym;
    
    /** The dependence. */
    private String dependence;
    
    /** The category. */
    private String category;

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
     * Gets the acronym.
     *
     * @return the acronym
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * Sets the acronym.
     *
     * @param acronym the new acronym
     */
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
     * Gets the dependence.
     *
     * @return the dependence
     */
    public String getDependence() {
        return dependence;
    }

    /**
     * Sets the dependence.
     *
     * @param dependence the new dependence
     */
    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }
}
