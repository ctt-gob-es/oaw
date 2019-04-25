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

public class FulFilledCrawling {
    private long id;
    private long idCrawling;
    private String user;
    private Timestamp date;
    private long idCartridge;
    private String cartridge;
    private SemillaForm seed;
    private long idFulfilledObservatory;
    private long idObservatory;

    public String getCartridge() {
        return cartridge;
    }

    public void setCartridge(String cartridge) {
        this.cartridge = cartridge;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(long idCrawling) {
        this.idCrawling = idCrawling;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getIdCartridge() {
        return idCartridge;
    }

    public void setIdCartridge(long idCartridge) {
        this.idCartridge = idCartridge;
    }

    public SemillaForm getSeed() {
        return seed;
    }

    public void setSeed(SemillaForm seed) {
        this.seed = seed;
    }

    public long getIdFulfilledObservatory() {
        return idFulfilledObservatory;
    }

    public void setIdFulfilledObservatory(long idFulfilledObservatory) {
        this.idFulfilledObservatory = idFulfilledObservatory;
    }

    public long getIdObservatory() {
        return idObservatory;
    }

    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }
}
