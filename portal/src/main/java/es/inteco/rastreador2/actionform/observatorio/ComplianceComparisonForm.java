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
package es.inteco.rastreador2.actionform.observatorio;

/**
 * The Class ComplianceComparisonForm.
 */
public class ComplianceComparisonForm {
    
    /** The verification. */
    private String verification;
    
    /** The red percentage. */
    private String redPercentage;
    
    /** The green percentage. */
    private String greenPercentage;
    
    /** The gray percentage. */
    private String grayPercentage;

    /**
	 * Instantiates a new compliance comparison form.
	 */
    public ComplianceComparisonForm() {
    }

    /**
	 * Instantiates a new compliance comparison form.
	 *
	 * @param verification the verification
	 */
    public ComplianceComparisonForm(String verification) {
        this.verification = verification;
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
	 * Gets the red percentage.
	 *
	 * @return the red percentage
	 */
    public String getRedPercentage() {
        return redPercentage;
    }

    /**
	 * Sets the red percentage.
	 *
	 * @param redPercentage the new red percentage
	 */
    public void setRedPercentage(String redPercentage) {
        this.redPercentage = redPercentage;
    }

    /**
	 * Gets the green percentage.
	 *
	 * @return the green percentage
	 */
    public String getGreenPercentage() {
        return greenPercentage;
    }

    /**
	 * Sets the green percentage.
	 *
	 * @param greenPercentage the new green percentage
	 */
    public void setGreenPercentage(String greenPercentage) {
        this.greenPercentage = greenPercentage;
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
            return obj instanceof ComplianceComparisonForm && ((ComplianceComparisonForm) obj).getVerification().equals(this.verification);
        }
    }

    /**
	 * Gets the gray percentage.
	 *
	 * @return the gray percentage
	 */
    public String getGrayPercentage() {
		return grayPercentage;
	}

	/**
	 * Sets the gray percentage.
	 *
	 * @param grayPercentage the new gray percentage
	 */
	public void setGrayPercentage(String grayPercentage) {
		this.grayPercentage = grayPercentage;
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
