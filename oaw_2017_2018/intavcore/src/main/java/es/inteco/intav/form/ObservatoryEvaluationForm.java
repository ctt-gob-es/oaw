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
package es.inteco.intav.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObservatoryEvaluationForm implements Serializable {
    private Long observatoryExecutionId;
    private Long crawlerExecutionId;
    private String entity;
    private String url;
    private BigDecimal score;
    private SeedForm seed;
    private List<ObservatoryLevelForm> groups;
    private List<AspectScoreForm> aspects;
    private List<Integer> checksFailed;
    private String source;
    private Long idAnalysis;

    public ObservatoryEvaluationForm() {
        this.groups = new ArrayList<>();
        this.checksFailed = new ArrayList<>();
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ObservatoryLevelForm> getGroups() {
        return groups;
    }

    public void setGroups(List<ObservatoryLevelForm> groups) {
        this.groups = groups;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<AspectScoreForm> getAspects() {
        return aspects;
    }

    public void setAspects(List<AspectScoreForm> aspects) {
        this.aspects = aspects;
    }

    public Long getObservatoryExecutionId() {
        return observatoryExecutionId;
    }

    public void setObservatoryExecutionId(Long observatoryExecutionId) {
        this.observatoryExecutionId = observatoryExecutionId;
    }

    public Long getCrawlerExecutionId() {
        return crawlerExecutionId;
    }

    public void setCrawlerExecutionId(Long crawlerExecutionId) {
        this.crawlerExecutionId = crawlerExecutionId;
    }

    public List<Integer> getChecksFailed() {
        return checksFailed;
    }

    public void setChecksFailed(List<Integer> checksFailed) {
        this.checksFailed = checksFailed;
    }

    public SeedForm getSeed() {
        return seed;
    }

    public void setSeed(SeedForm seed) {
        this.seed = seed;
    }


	public Long getIdAnalysis() {
		return idAnalysis;
	}


	public void setIdAnalysis(Long idAnalysis) {
		this.idAnalysis = idAnalysis;
	}

}
