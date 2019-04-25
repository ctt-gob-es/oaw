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

public class GraphicData {

    private String adecuationLevel;
    private String percentageP;
    private BigDecimal rawPercentage = BigDecimal.ZERO;
    private String numberP;

    public String getAdecuationLevel() {
        return adecuationLevel;
    }

    public void setAdecuationLevel(String adecuationLevel) {
        this.adecuationLevel = adecuationLevel;
    }

    public String getPercentageP() {
        return percentageP;
    }

    public void setPercentageP(String percentageP) {
        this.percentageP = percentageP;
    }

    public String getNumberP() {
        return numberP;
    }

    public void setNumberP(String numberP) {
        this.numberP = numberP;
    }

    public BigDecimal getRawPercentage() {
        return rawPercentage;
    }

    public void setRawPercentage(BigDecimal rawPercentage) {
        this.rawPercentage = rawPercentage;
    }
}
