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
package es.inteco.rastreador2.dao.export.database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Category.
 */
@Entity
@Table(name = "export_category")
public class Category {
    
    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /** The name. */
    @Column(name = "name", nullable = false)
    private String name;

    /** The num AA. */
    @Column(name = "numAA", nullable = false)
    private int numAA;

    /** The num A. */
    @Column(name = "numA", nullable = false)
    private int numA;

    /** The num NV. */
    @Column(name = "numNV", nullable = false)
    private int numNV;

    /** The score AA. */
    @Column(name = "scoreAA", nullable = false)
    private BigDecimal scoreAA;

    /** The score A. */
    @Column(name = "scoreA", nullable = false)
    private BigDecimal scoreA;

    /** The score NV. */
    @Column(name = "scoreNV", nullable = false)
    private BigDecimal scoreNV;

    /** The observatory. */
    @ManyToOne
    @JoinColumn(name = "idExecution", nullable = false, referencedColumnName = "idExecution")
    private Observatory observatory;

    /** The verification modality list. */
    @OneToMany(targetEntity = VerificationModality.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationModality> verificationModalityList;

    /** The verification score list. */
    @OneToMany(targetEntity = VerificationScore.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationScore> verificationScoreList;

    /** The aspect score list. */
    @OneToMany(targetEntity = AspectScore.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    /** The site list. */
    @OneToMany(targetEntity = Site.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Site> siteList;

    /** The id crawler category. */
    @Column(name = "idCrawlerCategory")
    private Long idCrawlerCategory;

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
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
	 * Gets the observatory.
	 *
	 * @return the observatory
	 */
    public Observatory getObservatory() {
        return observatory;
    }

    /**
	 * Sets the observatory.
	 *
	 * @param observatory the new observatory
	 */
    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }

    /**
	 * Gets the num AA.
	 *
	 * @return the num AA
	 */
    public int getNumAA() {
        return numAA;
    }

    /**
	 * Sets the num AA.
	 *
	 * @param numAA the new num AA
	 */
    public void setNumAA(int numAA) {
        this.numAA = numAA;
    }

    /**
	 * Gets the num A.
	 *
	 * @return the num A
	 */
    public int getNumA() {
        return numA;
    }

    /**
	 * Sets the num A.
	 *
	 * @param numA the new num A
	 */
    public void setNumA(int numA) {
        this.numA = numA;
    }

    /**
	 * Gets the num NV.
	 *
	 * @return the num NV
	 */
    public int getNumNV() {
        return numNV;
    }

    /**
	 * Sets the num NV.
	 *
	 * @param numNV the new num NV
	 */
    public void setNumNV(int numNV) {
        this.numNV = numNV;
    }

    /**
	 * Gets the score AA.
	 *
	 * @return the score AA
	 */
    public BigDecimal getScoreAA() {
        return scoreAA;
    }

    /**
	 * Sets the score AA.
	 *
	 * @param scoreAA the new score AA
	 */
    public void setScoreAA(BigDecimal scoreAA) {
        this.scoreAA = scoreAA;
    }

    /**
	 * Gets the score A.
	 *
	 * @return the score A
	 */
    public BigDecimal getScoreA() {
        return scoreA;
    }

    /**
	 * Sets the score A.
	 *
	 * @param scoreA the new score A
	 */
    public void setScoreA(BigDecimal scoreA) {
        this.scoreA = scoreA;
    }

    /**
	 * Gets the score NV.
	 *
	 * @return the score NV
	 */
    public BigDecimal getScoreNV() {
        return scoreNV;
    }

    /**
	 * Sets the score NV.
	 *
	 * @param scoreNV the new score NV
	 */
    public void setScoreNV(BigDecimal scoreNV) {
        this.scoreNV = scoreNV;
    }

    /**
	 * Gets the verification modality list.
	 *
	 * @return the verification modality list
	 */
    public List<VerificationModality> getVerificationModalityList() {
        if (verificationModalityList == null) {
            verificationModalityList = new ArrayList<>();
        }
        return verificationModalityList;
    }

    /**
	 * Sets the verification modality list.
	 *
	 * @param verificationModalityList the new verification modality list
	 */
    public void setVerificationModalityList(
            List<VerificationModality> verificationModalityList) {
        this.verificationModalityList = verificationModalityList;
    }

    /**
	 * Gets the verification score list.
	 *
	 * @return the verification score list
	 */
    public List<VerificationScore> getVerificationScoreList() {
        if (verificationScoreList == null) {
            verificationScoreList = new ArrayList<>();
        }
        return verificationScoreList;
    }

    /**
	 * Sets the verification score list.
	 *
	 * @param verificationScoreList the new verification score list
	 */
    public void setVerificationScoreList(
            List<VerificationScore> verificationScoreList) {
        this.verificationScoreList = verificationScoreList;
    }

    /**
	 * Gets the aspect score list.
	 *
	 * @return the aspect score list
	 */
    public List<AspectScore> getAspectScoreList() {
        if (aspectScoreList == null) {
            aspectScoreList = new ArrayList<>();
        }
        return aspectScoreList;
    }

    /**
	 * Sets the aspect score list.
	 *
	 * @param aspectScoreList the new aspect score list
	 */
    public void setAspectScoreList(List<AspectScore> aspectScoreList) {
        this.aspectScoreList = aspectScoreList;
    }

    /**
	 * Gets the site list.
	 *
	 * @return the site list
	 */
    public List<Site> getSiteList() {
        if (siteList == null) {
            siteList = new ArrayList<>();
        }
        return siteList;
    }

    /**
	 * Sets the site list.
	 *
	 * @param siteList the new site list
	 */
    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    /**
	 * Gets the id crawler category.
	 *
	 * @return the id crawler category
	 */
    public Long getIdCrawlerCategory() {
        return idCrawlerCategory;
    }

    /**
	 * Sets the id crawler category.
	 *
	 * @param idCrawlerCategory the new id crawler category
	 */
    public void setIdCrawlerCategory(Long idCrawlerCategory) {
        this.idCrawlerCategory = idCrawlerCategory;
    }
}
