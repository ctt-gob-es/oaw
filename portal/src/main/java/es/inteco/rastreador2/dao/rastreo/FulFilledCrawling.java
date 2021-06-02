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
package es.inteco.rastreador2.dao.rastreo;

import es.inteco.rastreador2.actionform.semillas.SemillaForm;

import java.sql.Timestamp;

/**
 * The Class FulFilledCrawling.
 */
public class FulFilledCrawling {
    
    /** The id. */
    private long id;
    
    /** The id crawling. */
    private long idCrawling;
    
    /** The user. */
    private String user;
    
    /** The date. */
    private Timestamp date;
    
    /** The id cartridge. */
    private long idCartridge;
    
    /** The cartridge. */
    private String cartridge;
    
    /** The seed. */
    private SemillaForm seed;
    
    /** The id fulfilled observatory. */
    private long idFulfilledObservatory;
    
    /** The id observatory. */
    private long idObservatory;

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
    public long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * Gets the id crawling.
	 *
	 * @return the id crawling
	 */
    public long getIdCrawling() {
        return idCrawling;
    }

    /**
	 * Sets the id crawling.
	 *
	 * @param idCrawling the new id crawling
	 */
    public void setIdCrawling(long idCrawling) {
        this.idCrawling = idCrawling;
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
	 * Gets the date.
	 *
	 * @return the date
	 */
    public Timestamp getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
	 * Gets the id cartridge.
	 *
	 * @return the id cartridge
	 */
    public long getIdCartridge() {
        return idCartridge;
    }

    /**
	 * Sets the id cartridge.
	 *
	 * @param idCartridge the new id cartridge
	 */
    public void setIdCartridge(long idCartridge) {
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

    /**
	 * Gets the id fulfilled observatory.
	 *
	 * @return the id fulfilled observatory
	 */
    public long getIdFulfilledObservatory() {
        return idFulfilledObservatory;
    }

    /**
	 * Sets the id fulfilled observatory.
	 *
	 * @param idFulfilledObservatory the new id fulfilled observatory
	 */
    public void setIdFulfilledObservatory(long idFulfilledObservatory) {
        this.idFulfilledObservatory = idFulfilledObservatory;
    }

    /**
	 * Gets the id observatory.
	 *
	 * @return the id observatory
	 */
    public long getIdObservatory() {
        return idObservatory;
    }

    /**
	 * Sets the id observatory.
	 *
	 * @param idObservatory the new id observatory
	 */
    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }
}
