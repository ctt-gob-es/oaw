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
 * The Class CargarRastreosRealizadosSearchForm.
 */
public class CargarRastreosRealizadosSearchForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The initial date. */
    private String initial_date;
    
    /** The final date. */
    private String final_date;
    
    /** The cartridge. */
    private String cartridge;
    
    /** The seed. */
    private String seed;

    /**
	 * Gets the initial date.
	 *
	 * @return the initial date
	 */
    public String getInitial_date() {
        return initial_date;
    }

    /**
	 * Sets the initial date.
	 *
	 * @param initial_date the new initial date
	 */
    public void setInitial_date(String initial_date) {
        this.initial_date = initial_date;
    }

    /**
	 * Gets the final date.
	 *
	 * @return the final date
	 */
    public String getFinal_date() {
        return final_date;
    }

    /**
	 * Sets the final date.
	 *
	 * @param final_date the new final date
	 */
    public void setFinal_date(String final_date) {
        this.final_date = final_date;
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

    /**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
    public String getSeed() {
        return seed;
    }

    /**
	 * Sets the seed.
	 *
	 * @param seed the new seed
	 */
    public void setSeed(String seed) {
        this.seed = seed;
    }
}