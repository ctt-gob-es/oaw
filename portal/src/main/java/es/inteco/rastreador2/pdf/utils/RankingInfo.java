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
package es.inteco.rastreador2.pdf.utils;

import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;

import java.math.BigDecimal;

public class RankingInfo {

    private BigDecimal score;
    private int globalRank;
    private int globalSeedsNumber;
    private int categoryRank;
    private int categorySeedsNumber;
    private CategoriaForm categoria;
    private AmbitoForm ambito;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public int getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(int globalRank) {
        this.globalRank = globalRank;
    }

    public void incrementGlobalRank() {
        this.globalRank++;
    }

    public int getGlobalSeedsNumber() {
        return globalSeedsNumber;
    }

    public void setGlobalSeedsNumber(int globalSeedsNumber) {
        this.globalSeedsNumber = globalSeedsNumber;
    }

    public int getCategoryRank() {
        return categoryRank;
    }

    public void setCategoryRank(int categoryRank) {
        this.categoryRank = categoryRank;
    }

    public void incrementCategoryRank() {
        this.categoryRank++;
    }

    public int getCategorySeedsNumber() {
        return categorySeedsNumber;
    }

    public void setCategorySeedsNumber(int categorySeedsNumber) {
        this.categorySeedsNumber = categorySeedsNumber;
    }

    public CategoriaForm getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
    }

    public AmbitoForm getAmbito() {
        return ambito;
    }

    public void setAmbito(AmbitoForm ambito) {
        this.ambito = ambito;
    }
    
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
