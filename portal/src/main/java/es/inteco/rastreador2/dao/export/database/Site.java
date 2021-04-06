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
package es.inteco.rastreador2.dao.export.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The Class Site.
 */
@Entity
@Table(name = "export_site")
public class Site {
	
	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;
	
	/** The name. */
	@Column(name = "name", nullable = false)
	private String name;
	
	/** The score. */
	@Column(name = "score")
	private BigDecimal score;
	
	/** The score level 1. */
	@Column(name = "scoreLevel1")
	private BigDecimal scoreLevel1;
	
	/** The score level 2. */
	@Column(name = "scoreLevel2")
	private BigDecimal scoreLevel2;
	
	/** The level. */
	@Column(name = "level", nullable = false)
	private String level;
	
	/** The num AA. */
	@Column(name = "numAA", nullable = false)
	private int numAA;
	
	/** The num A. */
	@Column(name = "numA", nullable = false)
	private int numA;
	
	/** The num NV. */
	@Column(name = "numNV", nullable = false)
	private int numNV;
	
	/** The compliance. */
	@Column(name = "compliance", nullable = true)
	private String compliance;
	
	/** The category. */
	@ManyToOne
	@JoinColumn(name = "idCategory", nullable = false, referencedColumnName = "id")
	private Category category;
	
	/** The page list. */
	@OneToMany(targetEntity = Page.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Page> pageList;
	
	/** The aspect score list. */
	@OneToMany(targetEntity = AspectScore.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<AspectScore> aspectScoreList;
	
	/** The verification score list. */
	@OneToMany(targetEntity = VerificationScore.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<VerificationScore> verificationScoreList;
	
	/** The verification modality list. */
	@OneToMany(targetEntity = VerificationModality.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<VerificationModality> verificationModalityList;
	
	/** The id crawler seed. */
	@Column(name = "idCrawlerSeed")
	private Long idCrawlerSeed;

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
	 * Gets the category.
	 *
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(Category category) {
		this.category = category;
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
	 * Gets the page list.
	 *
	 * @return the page list
	 */
	public List<Page> getPageList() {
		if (pageList == null) {
			pageList = new ArrayList<>();
		}
		return pageList;
	}

	/**
	 * Sets the page list.
	 *
	 * @param pageList the new page list
	 */
	public void setPageList(List<Page> pageList) {
		this.pageList = pageList;
	}

	/**
	 * Gets the score level 1.
	 *
	 * @return the score level 1
	 */
	public BigDecimal getScoreLevel1() {
		return scoreLevel1;
	}

	/**
	 * Sets the score level 1.
	 *
	 * @param scoreLevel1 the new score level 1
	 */
	public void setScoreLevel1(BigDecimal scoreLevel1) {
		this.scoreLevel1 = scoreLevel1;
	}

	/**
	 * Gets the score level 2.
	 *
	 * @return the score level 2
	 */
	public BigDecimal getScoreLevel2() {
		return scoreLevel2;
	}

	/**
	 * Sets the score level 2.
	 *
	 * @param scoreLevel2 the new score level 2
	 */
	public void setScoreLevel2(BigDecimal scoreLevel2) {
		this.scoreLevel2 = scoreLevel2;
	}

	/**
	 * Gets the num AA.
	 *
	 * @return the num AA
	 */
	public int getNumAA() {
		return numAA;
	}

	/**
	 * Sets the num AA.
	 *
	 * @param numAA the new num AA
	 */
	public void setNumAA(int numAA) {
		this.numAA = numAA;
	}

	/**
	 * Gets the num A.
	 *
	 * @return the num A
	 */
	public int getNumA() {
		return numA;
	}

	/**
	 * Sets the num A.
	 *
	 * @param numA the new num A
	 */
	public void setNumA(int numA) {
		this.numA = numA;
	}

	/**
	 * Gets the num NV.
	 *
	 * @return the num NV
	 */
	public int getNumNV() {
		return numNV;
	}

	/**
	 * Sets the num NV.
	 *
	 * @param numNV the new num NV
	 */
	public void setNumNV(int numNV) {
		this.numNV = numNV;
	}

	/**
	 * Gets the aspect score list.
	 *
	 * @return the aspect score list
	 */
	public List<AspectScore> getAspectScoreList() {
		if (aspectScoreList == null) {
			aspectScoreList = new ArrayList<>();
		}
		return aspectScoreList;
	}

	/**
	 * Sets the aspect score list.
	 *
	 * @param aspectScoreList the new aspect score list
	 */
	public void setAspectScoreList(List<AspectScore> aspectScoreList) {
		this.aspectScoreList = aspectScoreList;
	}

	/**
	 * Gets the verification score list.
	 *
	 * @return the verification score list
	 */
	public List<VerificationScore> getVerificationScoreList() {
		if (verificationScoreList == null) {
			verificationScoreList = new ArrayList<>();
		}
		return verificationScoreList;
	}

	/**
	 * Sets the verification score list.
	 *
	 * @param verificationScoreList the new verification score list
	 */
	public void setVerificationScoreList(List<VerificationScore> verificationScoreList) {
		this.verificationScoreList = verificationScoreList;
	}

	/**
	 * Gets the verification modality list.
	 *
	 * @return the verification modality list
	 */
	public List<VerificationModality> getVerificationModalityList() {
		if (verificationModalityList == null) {
			verificationModalityList = new ArrayList<>();
		}
		return verificationModalityList;
	}

	/**
	 * Sets the verification modality list.
	 *
	 * @param verificationModalityList the new verification modality list
	 */
	public void setVerificationModalityList(List<VerificationModality> verificationModalityList) {
		this.verificationModalityList = verificationModalityList;
	}

	/**
	 * Gets the id crawler seed.
	 *
	 * @return the id crawler seed
	 */
	public Long getIdCrawlerSeed() {
		return idCrawlerSeed;
	}

	/**
	 * Sets the id crawler seed.
	 *
	 * @param idCrawlerSeed the new id crawler seed
	 */
	public void setIdCrawlerSeed(Long idCrawlerSeed) {
		this.idCrawlerSeed = idCrawlerSeed;
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
