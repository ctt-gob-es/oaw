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
 * The Class ValidationLanguageForm.
 */
public class ValidationLanguageForm {
    
    /** The language. */
    String language;
    
    /** The red percentage. */
    String redPercentage;
    
    /** The green percentage. */
    String greenPercentage;

    /**
	 * Instantiates a new validation language form.
	 */
    public ValidationLanguageForm() {

    }

    /**
	 * Instantiates a new validation language form.
	 *
	 * @param language the language
	 */
    public ValidationLanguageForm(String language) {
        this.language = language;
    }

    /**
	 * Gets the language.
	 *
	 * @return the language
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
    public void setLanguage(String language) {
        this.language = language;
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
            if (!(obj instanceof ValidationLanguageForm)) {
                return false;
            } else {
                return ((ValidationLanguageForm) obj).getLanguage().equals(this.language);
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
