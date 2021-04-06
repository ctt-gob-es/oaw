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
 * The Class PriorityForm.
 */
public class PriorityForm implements Serializable {
    
    /** The num problems. */
    private int numProblems;
    
    /** The num warnings. */
    private int numWarnings;
    
    /** The num infos. */
    private int numInfos;
    
    /** The priority name. */
    private String priorityName;
    
    /** The guidelines. */
    private List<GuidelineForm> guidelines;

    /**
	 * Instantiates a new priority form.
	 */
    public PriorityForm() {
        guidelines = new ArrayList<>();
    }

    /**
	 * Gets the num problems.
	 *
	 * @return the num problems
	 */
    public int getNumProblems() {
        return numProblems;
    }

    /**
	 * Sets the num problems.
	 *
	 * @param numProblems the new num problems
	 */
    public void setNumProblems(int numProblems) {
        this.numProblems = numProblems;
    }

    /**
	 * Gets the num warnings.
	 *
	 * @return the num warnings
	 */
    public int getNumWarnings() {
        return numWarnings;
    }

    /**
	 * Sets the num warnings.
	 *
	 * @param numWarnings the new num warnings
	 */
    public void setNumWarnings(int numWarnings) {
        this.numWarnings = numWarnings;
    }

    /**
	 * Gets the num infos.
	 *
	 * @return the num infos
	 */
    public int getNumInfos() {
        return numInfos;
    }

    /**
	 * Sets the num infos.
	 *
	 * @param numInfos the new num infos
	 */
    public void setNumInfos(int numInfos) {
        this.numInfos = numInfos;
    }

    /**
	 * Gets the guidelines.
	 *
	 * @return the guidelines
	 */
    public List<GuidelineForm> getGuidelines() {
        return guidelines;
    }

    /**
	 * Sets the guidelines.
	 *
	 * @param guidelines the new guidelines
	 */
    public void setGuidelines(List<GuidelineForm> guidelines) {
        this.guidelines = guidelines;
    }

    /**
	 * Gets the priority name.
	 *
	 * @return the priority name
	 */
    public String getPriorityName() {
        return priorityName;
    }

    /**
	 * Sets the priority name.
	 *
	 * @param priorityName the new priority name
	 */
    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
}
