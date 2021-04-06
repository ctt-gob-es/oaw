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
package es.inteco.rastreador2.actionform.rastreo;


import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * The Class SubirSemillaForm.
 */
public class SubirSemillaForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The semillas. */
    private List<String> semillas;
    
    /** The semilla. */
    private String semilla;

    /**
	 * Gets the semillas.
	 *
	 * @return the semillas
	 */
    public List<String> getSemillas() {
        return semillas;
    }

    /**
	 * Sets the semillas.
	 *
	 * @param semillas the new semillas
	 */
    public void setSemillas(List<String> semillas) {
        this.semillas = semillas;
    }

    /**
	 * Gets the semilla.
	 *
	 * @return the semilla
	 */
    public String getSemilla() {
        return semilla;
    }

    /**
	 * Sets the semilla.
	 *
	 * @param semilla the new semilla
	 */
    public void setSemilla(String semilla) {
        this.semilla = semilla;
    }

}
