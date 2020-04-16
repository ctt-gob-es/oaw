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

/**
 * The Class SiteForm.
 */
public class SiteForm {
	/** The id. */
	private String id;
	/** The name. */
	private String name;
	/** The score. */
	private String score;
	/** The score level 1. */
	private String scoreLevel1;
	/** The score level 2. */
	private String scoreLevel2;
	/** The level. */
	private String level;
	/** The num AA. */
	private String numAA;
	/** The num A. */
	private String numA;
	/** The num NV. */
	private String numNV;
	/** The id crawler seed. */
	private String idCrawlerSeed;
	/** The compliance. */
	private String compliance;
	/** The page list. */
	private List<PageForm> pageList;

	/**
	 * Gets the id crawler seed.
	 *
	 * @return the id crawler seed
	 */
	public String getIdCrawlerSeed() {
		return idCrawlerSeed;
	}

	/**
	 * Sets the id crawler seed.
	 *
	 * @param idSeed the new id crawler seed
	 */
	public void setIdCrawlerSeed(String idSeed) {
		this.idCrawlerSeed = idSeed;
	}

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

	/**
	 * Gets the num AA.
	 *
	 * @return the num AA
	 */
	public String getNumAA() {
		return numAA;
	}

	/**
	 * Sets the num AA.
	 *
	 * @param numAA the new num AA
	 */
	public void setNumAA(String numAA) {
		this.numAA = numAA;
	}

	/**
	 * Gets the num A.
	 *
	 * @return the num A
	 */
	public String getNumA() {
		return numA;
	}

	/**
	 * Sets the num A.
	 *
	 * @param numA the new num A
	 */
	public void setNumA(String numA) {
		this.numA = numA;
	}

	/**
	 * Gets the num NV.
	 *
	 * @return the num NV
	 */
	public String getNumNV() {
		return numNV;
	}

	/**
	 * Sets the num NV.
	 *
	 * @param numNV the new num NV
	 */
	public void setNumNV(String numNV) {
		this.numNV = numNV;
	}

	/**
	 * Gets the page list.
	 *
	 * @return the page list
	 */
	public List<PageForm> getPageList() {
		return pageList;
	}

	/**
	 * Sets the page list.
	 *
	 * @param pageList the new page list
	 */
	public void setPageList(List<PageForm> pageList) {
		this.pageList = pageList;
	}

	/**
	 * Gets the compliance.
	 *
	 * @return the compliance
	 */
	public String getCompliance() {
		return compliance;
	}

	/**
	 * Sets the compliance.
	 *
	 * @param compliance the compliance to set
	 */
	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}
}
