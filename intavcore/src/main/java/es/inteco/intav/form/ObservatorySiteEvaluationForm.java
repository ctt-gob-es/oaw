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

import java.math.BigDecimal;
import java.util.List;

/**
 * The Class ObservatorySiteEvaluationForm.
 */
public class ObservatorySiteEvaluationForm {
    
    /** The id. */
    private Long id;
    
    /** The name. */
    private String name;
    
    /** The level. */
    private String level;
    
    /** The acronym. */
    private String acronym;
    
    /** The score. */
    private BigDecimal score;
    
    /** The id seed. */
    private Long idSeed;

    /** The pages. */
    private List<ObservatoryEvaluationForm> pages;

    /**
	 * Gets the level.
	 *
	 * @return the level
	 */
    public String getLevel() {
        return level;
    }

    /**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
    public void setLevel(String level) {
        this.level = level;
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
	 * Gets the pages.
	 *
	 * @return the pages
	 */
    public List<ObservatoryEvaluationForm> getPages() {
        return pages;
    }

    /**
	 * Sets the pages.
	 *
	 * @param pages the new pages
	 */
    public void setPages(List<ObservatoryEvaluationForm> pages) {
        this.pages = pages;
    }

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets the acronym.
	 *
	 * @return the acronym
	 */
    public String getAcronym() {
        return acronym;
    }

    /**
	 * Sets the acronym.
	 *
	 * @param acronym the new acronym
	 */
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
	 * Gets the id seed.
	 *
	 * @return the id seed
	 */
    public Long getIdSeed() {
        return idSeed;
    }

    /**
	 * Sets the id seed.
	 *
	 * @param idSeed the new id seed
	 */
    public void setIdSeed(Long idSeed) {
        this.idSeed = idSeed;
    }

}