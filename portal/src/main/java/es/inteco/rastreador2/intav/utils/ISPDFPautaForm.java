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

import es.inteco.intav.form.PautaForm;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ISPDFPautaForm.
 */
public class ISPDFPautaForm {

    /** The pauta. */
    public PautaForm pauta;
    
    /** The problem list. */
    public List<ISPDFProblemForm> problemList;

    /**
	 * Instantiates a new ISPDF pauta form.
	 */
    public ISPDFPautaForm() {
        pauta = new PautaForm();
        problemList = new ArrayList<>();
    }

    /**
	 * Gets the pauta.
	 *
	 * @return the pauta
	 */
    public PautaForm getPauta() {
        return pauta;
    }

    /**
	 * Sets the pauta.
	 *
	 * @param pauta the new pauta
	 */
    public void setPauta(PautaForm pauta) {
        this.pauta = pauta;
    }

    /**
	 * Gets the problem list.
	 *
	 * @return the problem list
	 */
    public List<ISPDFProblemForm> getProblemList() {
        return problemList;
    }

    /**
	 * Sets the problem list.
	 *
	 * @param problemList the new problem list
	 */
    public void setProblemList(List<ISPDFProblemForm> problemList) {
        this.problemList = problemList;
    }

    /**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISPDFPautaForm) {
            ISPDFPautaForm form = (ISPDFPautaForm) obj;
            if (form.getPauta().getName().equals(pauta.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
    @Override
    public int hashCode() {
        return pauta != null ? pauta.getName().hashCode() : 0;
    }
}
