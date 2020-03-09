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

/**
 * The Class ObservatoryEvaluationForm.
 */
public class ObservatoryEvaluationForm implements Serializable {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3807133897377481618L;

	/** The observatory execution id. */
    private Long observatoryExecutionId;
    
    /** The crawler execution id. */
    private Long crawlerExecutionId;
    
    /** The entity. */
    private String entity;
    
    /** The url. */
    private String url;
    
    /** The score. */
    private BigDecimal score;
    
    /** The seed. */
    private SeedForm seed;
    
    /** The groups. */
    private List<ObservatoryLevelForm> groups;
    
    /** The aspects. */
    private List<AspectScoreForm> aspects;
    
    /** The checks failed. */
    private List<Integer> checksFailed;
    
    /** The source. */
    private String source;
    
    /** The id analysis. */
    private Long idAnalysis;

    /**
	 * Instantiates a new observatory evaluation form.
	 */
    public ObservatoryEvaluationForm() {
        this.groups = new ArrayList<>();
        this.checksFailed = new ArrayList<>();
    }


    /**
	 * Gets the source.
	 *
	 * @return the source
	 */
    public String getSource() {
        return source;
    }

    /**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
    public void setSource(String source) {
        this.source = source;
    }


    /**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
    public String getEntity() {
        return entity;
    }

    /**
	 * Sets the entity.
	 *
	 * @param entity the new entity
	 */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
	 * Gets the url.
	 *
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * Gets the groups.
	 *
	 * @return the groups
	 */
    public List<ObservatoryLevelForm> getGroups() {
        return groups;
    }

    /**
	 * Sets the groups.
	 *
	 * @param groups the new groups
	 */
    public void setGroups(List<ObservatoryLevelForm> groups) {
        this.groups = groups;
    }

    /**
	 * Gets the score.
	 *
	 * @return the score
	 */
    public BigDecimal getScore() {
        return score;
    }

    /**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
	 * Gets the aspects.
	 *
	 * @return the aspects
	 */
    public List<AspectScoreForm> getAspects() {
        return aspects;
    }

    /**
	 * Sets the aspects.
	 *
	 * @param aspects the new aspects
	 */
    public void setAspects(List<AspectScoreForm> aspects) {
        this.aspects = aspects;
    }

    /**
	 * Gets the observatory execution id.
	 *
	 * @return the observatory execution id
	 */
    public Long getObservatoryExecutionId() {
        return observatoryExecutionId;
    }

    /**
	 * Sets the observatory execution id.
	 *
	 * @param observatoryExecutionId the new observatory execution id
	 */
    public void setObservatoryExecutionId(Long observatoryExecutionId) {
        this.observatoryExecutionId = observatoryExecutionId;
    }

    /**
	 * Gets the crawler execution id.
	 *
	 * @return the crawler execution id
	 */
    public Long getCrawlerExecutionId() {
        return crawlerExecutionId;
    }

    /**
	 * Sets the crawler execution id.
	 *
	 * @param crawlerExecutionId the new crawler execution id
	 */
    public void setCrawlerExecutionId(Long crawlerExecutionId) {
        this.crawlerExecutionId = crawlerExecutionId;
    }

    /**
	 * Gets the checks failed.
	 *
	 * @return the checks failed
	 */
    public List<Integer> getChecksFailed() {
        return checksFailed;
    }

    /**
	 * Sets the checks failed.
	 *
	 * @param checksFailed the new checks failed
	 */
    public void setChecksFailed(List<Integer> checksFailed) {
        this.checksFailed = checksFailed;
    }

    /**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
    public SeedForm getSeed() {
        return seed;
    }

    /**
	 * Sets the seed.
	 *
	 * @param seed the new seed
	 */
    public void setSeed(SeedForm seed) {
        this.seed = seed;
    }


	/**
	 * Gets the id analysis.
	 *
	 * @return the id analysis
	 */
	public Long getIdAnalysis() {
		return idAnalysis;
	}


	/**
	 * Sets the id analysis.
	 *
	 * @param idAnalysis the new id analysis
	 */
	public void setIdAnalysis(Long idAnalysis) {
		this.idAnalysis = idAnalysis;
	}

}
