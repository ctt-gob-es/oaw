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
import java.util.ArrayList;
import java.util.List;

/**
 * The Class GuidelineForm.
 */
public class GuidelineForm implements Serializable {
    
    /** The description. */
    private String description;
    
    /** The guideline id. */
    private String guidelineId;
    
    /** The pautas. */
    private List<PautaForm> pautas;

    /**
	 * Instantiates a new guideline form.
	 */
    public GuidelineForm() {
        pautas = new ArrayList<>();
    }

    /**
	 * Gets the description.
	 *
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Gets the pautas.
	 *
	 * @return the pautas
	 */
    public List<PautaForm> getPautas() {
        return pautas;
    }

    /**
	 * Sets the pautas.
	 *
	 * @param pautas the new pautas
	 */
    public void setPautas(List<PautaForm> pautas) {
        this.pautas = pautas;
    }

    /**
	 * Gets the guideline id.
	 *
	 * @return the guideline id
	 */
    public String getGuidelineId() {
        return guidelineId;
    }

    /**
	 * Sets the guideline id.
	 *
	 * @param guidelineId the new guideline id
	 */
    public void setGuidelineId(String guidelineId) {
        this.guidelineId = guidelineId;
    }
}
