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

/**
 * The Class CargarRastreosSearchForm.
 */
public class CargarRastreosSearchForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The name. */
    private String name;
    
    /** The active. */
    private String active;
    
    /** The cartridge. */
    private String cartridge;

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
	 * Gets the active.
	 *
	 * @return the active
	 */
    public String getActive() {
        return active;
    }

    /**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
    public void setActive(String active) {
        this.active = active;
    }

    /**
	 * Gets the cartridge.
	 *
	 * @return the cartridge
	 */
    public String getCartridge() {
        return cartridge;
    }

    /**
	 * Sets the cartridge.
	 *
	 * @param cartridge the new cartridge
	 */
    public void setCartridge(String cartridge) {
        this.cartridge = cartridge;
    }


}