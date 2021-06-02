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

/**
 * The Class ObservatoryEvaluationLevelForm.
 */
public class ObservatoryEvaluationLevelForm {
    
    /** The level. */
    private String level;
    
    /** The observatory evaluation form. */
    private ObservatoryEvaluationForm observatoryEvaluationForm;

    /**
	 * Gets the level.
	 *
	 * @return the level
	 */
    public String getLevel() {
        return level;
    }

    /**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
	 * Gets the observatory evaluation form.
	 *
	 * @return the observatory evaluation form
	 */
    public ObservatoryEvaluationForm getObservatoryEvaluationForm() {
        return observatoryEvaluationForm;
    }

    /**
	 * Sets the observatory evaluation form.
	 *
	 * @param observatoryEvaluationForm the new observatory evaluation form
	 */
    public void setObservatoryEvaluationForm(
            ObservatoryEvaluationForm observatoryEvaluationForm) {
        this.observatoryEvaluationForm = observatoryEvaluationForm;
    }


}
