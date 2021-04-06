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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ObservatoryLevelForm.
 */
public class ObservatoryLevelForm implements Serializable {

    /** The name. */
    private String name;
    
    /** The score. */
    private BigDecimal score;
    
    /** The suitability groups. */
    private List<ObservatorySuitabilityForm> suitabilityGroups;

    /**
	 * Instantiates a new observatory level form.
	 */
    public ObservatoryLevelForm() {
        this.suitabilityGroups = new ArrayList<>();
    }

    /**
	 * Gets the score.
	 *
	 * @return the score
	 */
    public BigDecimal getScore() {
        return score;
    }

    /**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
	 * Gets the suitability groups.
	 *
	 * @return the suitability groups
	 */
    public List<ObservatorySuitabilityForm> getSuitabilityGroups() {
        return suitabilityGroups;
    }

    /**
	 * Sets the suitability groups.
	 *
	 * @param suitabilityGroups the new suitability groups
	 */
    public void setSuitabilityGroups(
            List<ObservatorySuitabilityForm> suitabilityGroups) {
        this.suitabilityGroups = suitabilityGroups;
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
}
