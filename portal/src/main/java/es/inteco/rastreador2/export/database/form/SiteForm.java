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
package es.inteco.rastreador2.export.database.form;

import java.util.List;

public class SiteForm {
    private String id;
    private String name;
    private String score;
    private String scoreLevel1;
    private String scoreLevel2;
    private String level;
    private String numAA;
    private String numA;
    private String numNV;
    private String idCrawlerSeed;
    private List<PageForm> pageList;

    public String getIdCrawlerSeed() {
        return idCrawlerSeed;
    }

    public void setIdCrawlerSeed(String idSeed) {
        this.idCrawlerSeed = idSeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreLevel1() {
        return scoreLevel1;
    }

    public void setScoreLevel1(String scoreLevel1) {
        this.scoreLevel1 = scoreLevel1;
    }

    public String getScoreLevel2() {
        return scoreLevel2;
    }

    public void setScoreLevel2(String scoreLevel2) {
        this.scoreLevel2 = scoreLevel2;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNumAA() {
        return numAA;
    }

    public void setNumAA(String numAA) {
        this.numAA = numAA;
    }

    public String getNumA() {
        return numA;
    }

    public void setNumA(String numA) {
        this.numA = numA;
    }

    public String getNumNV() {
        return numNV;
    }

    public void setNumNV(String numNV) {
        this.numNV = numNV;
    }

    public List<PageForm> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageForm> pageList) {
        this.pageList = pageList;
    }
}
