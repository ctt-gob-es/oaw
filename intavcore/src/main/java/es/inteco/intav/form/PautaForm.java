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
 * The Class PautaForm.
 */
public class PautaForm implements Serializable {
    
    /** The name. */
    private String name;
    
    /** The pauta id. */
    private String pautaId;
    
    /** The problems. */
    private List<ProblemForm> problems;

    /**
	 * Instantiates a new pauta form.
	 */
    public PautaForm() {
        problems = new ArrayList<>();
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
	 * Gets the problems.
	 *
	 * @return the problems
	 */
    public List<ProblemForm> getProblems() {
        return problems;
    }

    /**
	 * Sets the problems.
	 *
	 * @param problems the new problems
	 */
    public void setProblems(List<ProblemForm> problems) {
        this.problems = problems;
    }

    /**
	 * Gets the pauta id.
	 *
	 * @return the pauta id
	 */
    public String getPautaId() {
        return pautaId;
    }

    /**
	 * Sets the pauta id.
	 *
	 * @param pautaId the new pauta id
	 */
    public void setPautaId(String pautaId) {
        this.pautaId = pautaId;
    }
}
