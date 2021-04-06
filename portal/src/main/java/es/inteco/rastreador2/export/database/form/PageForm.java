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

/**
 * The Class PageForm.
 */
public class PageForm {
    
    /** The id. */
    private String id;
    
    /** The url. */
    private String url;
    
    /** The score. */
    private String score;
    
    /** The score level 1. */
    private String scoreLevel1;
    
    /** The score level 2. */
    private String scoreLevel2;
    
    /** The level. */
    private String level;

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
	 * Gets the score.
	 *
	 * @return the score
	 */
    public String getScore() {
        return score;
    }

    /**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
    public void setScore(String score) {
        this.score = score;
    }

    /**
	 * Gets the score level 1.
	 *
	 * @return the score level 1
	 */
    public String getScoreLevel1() {
        return scoreLevel1;
    }

    /**
	 * Sets the score level 1.
	 *
	 * @param scoreLevel1 the new score level 1
	 */
    public void setScoreLevel1(String scoreLevel1) {
        this.scoreLevel1 = scoreLevel1;
    }

    /**
	 * Gets the score level 2.
	 *
	 * @return the score level 2
	 */
    public String getScoreLevel2() {
        return scoreLevel2;
    }

    /**
	 * Sets the score level 2.
	 *
	 * @param scoreLevel2 the new score level 2
	 */
    public void setScoreLevel2(String scoreLevel2) {
        this.scoreLevel2 = scoreLevel2;
    }

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
}
