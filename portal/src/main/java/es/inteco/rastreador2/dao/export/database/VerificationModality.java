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

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The Class VerificationModality.
 */
@Entity
@Table(name = "export_verification_modality")
public class VerificationModality {

    /**
	 * Instantiates a new verification modality.
	 */
    public VerificationModality() {

    }

    /**
	 * Instantiates a new verification modality.
	 *
	 * @param verification the verification
	 */
    public VerificationModality(String verification) {
        this.verification = verification;
    }

    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /** The verification. */
    @Column(name = "verification", nullable = false)
    private String verification;

    /** The fail percentage. */
    @Column(name = "failPercentage", nullable = false)
    private BigDecimal failPercentage;

    /** The pass percentage. */
    @Column(name = "passPercentage", nullable = false)
    private BigDecimal passPercentage;

    /** The observatory. */
    @ManyToOne
    @JoinColumn(name = "idExecution", referencedColumnName = "idExecution")
    private Observatory observatory;

    /** The category. */
    @ManyToOne
    @JoinColumn(name = "idCategory", referencedColumnName = "id")
    private Category category;

    /** The site. */
    @ManyToOne
    @JoinColumn(name = "idSite", referencedColumnName = "id")
    private Site site;

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
	 * Gets the verification.
	 *
	 * @return the verification
	 */
    public String getVerification() {
        return verification;
    }

    /**
	 * Sets the verification.
	 *
	 * @param verification the new verification
	 */
    public void setVerification(String verification) {
        this.verification = verification;
    }

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
	 * Gets the category.
	 *
	 * @return the category
	 */
    public Category getCategory() {
        return category;
    }

    /**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
	 * Gets the fail percentage.
	 *
	 * @return the fail percentage
	 */
    public BigDecimal getFailPercentage() {
        return failPercentage;
    }

    /**
	 * Sets the fail percentage.
	 *
	 * @param failPercentage the new fail percentage
	 */
    public void setFailPercentage(BigDecimal failPercentage) {
        this.failPercentage = failPercentage;
    }

    /**
	 * Gets the pass percentage.
	 *
	 * @return the pass percentage
	 */
    public BigDecimal getPassPercentage() {
        return passPercentage;
    }

    /**
	 * Sets the pass percentage.
	 *
	 * @param passPercentage the new pass percentage
	 */
    public void setPassPercentage(BigDecimal passPercentage) {
        this.passPercentage = passPercentage;
    }

    /**
	 * Gets the site.
	 *
	 * @return the site
	 */
    public Site getSite() {
        return site;
    }

    /**
	 * Sets the site.
	 *
	 * @param site the new site
	 */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (!(obj instanceof VerificationModality)) {
                return false;
            } else {
                return ((VerificationModality) obj).getVerification().equals(this.verification);
            }
        }
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
    @Override
    public int hashCode() {
        return 0;
    }
}
