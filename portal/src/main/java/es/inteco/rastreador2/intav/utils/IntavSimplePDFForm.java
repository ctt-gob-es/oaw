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
package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.PriorityForm;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class IntavSimplePDFForm.
 */
public class IntavSimplePDFForm {

    /** The priority. */
    public PriorityForm priority;
    
    /** The guidelines list. */
    public List<ISPDFGuidelineForm> guidelinesList;

    /**
	 * Instantiates a new intav simple PDF form.
	 */
    public IntavSimplePDFForm() {
        priority = new PriorityForm();
        guidelinesList = new ArrayList<>();
    }

    /**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
    public PriorityForm getPriority() {
        return priority;
    }

    /**
	 * Sets the priority.
	 *
	 * @param priority the new priority
	 */
    public void setPriority(PriorityForm priority) {
        this.priority = priority;
    }

    /**
	 * Gets the guidelines list.
	 *
	 * @return the guidelines list
	 */
    public List<ISPDFGuidelineForm> getGuidelinesList() {
        return guidelinesList;
    }

    /**
	 * Sets the guidelines list.
	 *
	 * @param guidelinesList the new guidelines list
	 */
    public void setGuidelinesList(List<ISPDFGuidelineForm> guidelinesList) {
        this.guidelinesList = guidelinesList;
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
    @Override
    public int hashCode() {
        return priority != null ? priority.getPriorityName().hashCode() : 0;
    }

    /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntavSimplePDFForm) {
            IntavSimplePDFForm form = (IntavSimplePDFForm) obj;
            if (form.priority.getPriorityName().equals(priority.getPriorityName())) {
                return true;
            }
        }
        return false;
    }

}
