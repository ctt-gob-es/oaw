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
package es.inteco.rastreador2.utils;

import java.math.BigDecimal;

/**
 * The Class GraphicData.
 */
public class GraphicData {

    /** The adecuation level. */
    private String adecuationLevel;
    
    /** The percentage P. */
    private String percentageP;
    
    /** The raw percentage. */
    private BigDecimal rawPercentage = BigDecimal.ZERO;
    
    /** The number P. */
    private String numberP;

    /**
	 * Gets the adecuation level.
	 *
	 * @return the adecuation level
	 */
    public String getAdecuationLevel() {
        return adecuationLevel;
    }

    /**
	 * Sets the adecuation level.
	 *
	 * @param adecuationLevel the new adecuation level
	 */
    public void setAdecuationLevel(String adecuationLevel) {
        this.adecuationLevel = adecuationLevel;
    }

    /**
	 * Gets the percentage P.
	 *
	 * @return the percentage P
	 */
    public String getPercentageP() {
        return percentageP;
    }

    /**
	 * Sets the percentage P.
	 *
	 * @param percentageP the new percentage P
	 */
    public void setPercentageP(String percentageP) {
        this.percentageP = percentageP;
    }

    /**
	 * Gets the number P.
	 *
	 * @return the number P
	 */
    public String getNumberP() {
        return numberP;
    }

    /**
	 * Sets the number P.
	 *
	 * @param numberP the new number P
	 */
    public void setNumberP(String numberP) {
        this.numberP = numberP;
    }

    /**
	 * Gets the raw percentage.
	 *
	 * @return the raw percentage
	 */
    public BigDecimal getRawPercentage() {
        return rawPercentage;
    }

    /**
	 * Sets the raw percentage.
	 *
	 * @param rawPercentage the new raw percentage
	 */
    public void setRawPercentage(BigDecimal rawPercentage) {
        this.rawPercentage = rawPercentage;
    }
}
