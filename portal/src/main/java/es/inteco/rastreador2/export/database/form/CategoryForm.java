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
 * The Class CategoryForm.
 */
public class CategoryForm {
    
    /** The id. */
    private String id;
    
    /** The name. */
    private String name;
    
    /** The num AA. */
    private String numAA;
    
    /** The num A. */
    private String numA;
    
    /** The num NV. */
    private String numNV;
    
    /** The score AA. */
    private String scoreAA;
    
    /** The score A. */
    private String scoreA;
    
    /** The score NV. */
    private String scoreNV;
    
    /** The site form list. */
    private List<SiteForm> siteFormList;
    
    /** The id crawler category. */
    private String idCrawlerCategory;

    /**
     * Gets the id crawler category.
     *
     * @return the id crawler category
     */
    public String getIdCrawlerCategory() {
        return idCrawlerCategory;
    }

    /**
     * Sets the id crawler category.
     *
     * @param idCrawlerCategory the new id crawler category
     */
    public void setIdCrawlerCategory(String idCrawlerCategory) {
        this.idCrawlerCategory = idCrawlerCategory;
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
     * Gets the score AA.
     *
     * @return the score AA
     */
    public String getScoreAA() {
        return scoreAA;
    }

    /**
     * Sets the score AA.
     *
     * @param scoreAA the new score AA
     */
    public void setScoreAA(String scoreAA) {
        this.scoreAA = scoreAA;
    }

    /**
     * Gets the score A.
     *
     * @return the score A
     */
    public String getScoreA() {
        return scoreA;
    }

    /**
     * Sets the score A.
     *
     * @param scoreA the new score A
     */
    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    /**
     * Gets the score NV.
     *
     * @return the score NV
     */
    public String getScoreNV() {
        return scoreNV;
    }

    /**
     * Sets the score NV.
     *
     * @param scoreNV the new score NV
     */
    public void setScoreNV(String scoreNV) {
        this.scoreNV = scoreNV;
    }

    /**
     * Gets the site form list.
     *
     * @return the site form list
     */
    public List<SiteForm> getSiteFormList() {
        return siteFormList;
    }

    /**
     * Sets the site form list.
     *
     * @param siteFormList the new site form list
     */
    public void setSiteFormList(List<SiteForm> siteFormList) {
        this.siteFormList = siteFormList;
    }
}
