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

/**
 * The Class VerificationPage.
 */
@Entity
@Table(name = "export_verification_page")
public class VerificationPage {

    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /** The verification. */
    @Column(name = "verification", nullable = false)
    private String verification;

    /** The value. */
    @Column(name = "value")
    private Float value;

    /** The modality. */
    @Column(name = "modality", nullable = false)
    private String modality;

    /** The page. */
    @ManyToOne
    @JoinColumn(name = "idPage", referencedColumnName = "id")
    private Page page;

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
	 * Gets the value.
	 *
	 * @return the value
	 */
    public Float getValue() {
        return value;
    }

    /**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
	 * Gets the modality.
	 *
	 * @return the modality
	 */
    public String getModality() {
        return modality;
    }

    /**
	 * Sets the modality.
	 *
	 * @param modality the new modality
	 */
    public void setModality(String modality) {
        this.modality = modality;
    }

    /**
	 * Gets the page.
	 *
	 * @return the page
	 */
    public Page getPage() {
        return page;
    }

    /**
	 * Sets the page.
	 *
	 * @param page the new page
	 */
    public void setPage(Page page) {
        this.page = page;
    }
}
