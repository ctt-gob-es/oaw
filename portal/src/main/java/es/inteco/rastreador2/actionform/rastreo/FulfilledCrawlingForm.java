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

import es.inteco.rastreador2.actionform.semillas.SemillaForm;

/**
 * The Class FulfilledCrawlingForm.
 */
public class FulfilledCrawlingForm {
	/** The id. */
	private String id;
	/** The id crawling. */
	private String idCrawling;
	/** The user. */
	private String user;
	/** The date. */
	private String date;
	/** The id cartridge. */
	private String idCartridge;
	/** The cartridge. */
	private String cartridge;
	/** The seed. */
	private SemillaForm seed;
	/** The selected. */
	private boolean selected;

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the id crawling.
	 *
	 * @return the id crawling
	 */
	public String getIdCrawling() {
		return idCrawling;
	}

	/**
	 * Sets the id crawling.
	 *
	 * @param idCrawling the new id crawling
	 */
	public void setIdCrawling(String idCrawling) {
		this.idCrawling = idCrawling;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the id cartridge.
	 *
	 * @return the id cartridge
	 */
	public String getIdCartridge() {
		return idCartridge;
	}

	/**
	 * Sets the id cartridge.
	 *
	 * @param idCartridge the new id cartridge
	 */
	public void setIdCartridge(String idCartridge) {
		this.idCartridge = idCartridge;
	}

	/**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
	public SemillaForm getSeed() {
		return seed;
	}

	/**
	 * Sets the seed.
	 *
	 * @param seed the new seed
	 */
	public void setSeed(SemillaForm seed) {
		this.seed = seed;
	}
}
