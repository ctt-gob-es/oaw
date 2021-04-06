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
 * The Class Page.
 */
@Entity
@Table(name = "export_page")
public class Page {
	
	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;
	
	/** The url. */
	@Column(name = "url", nullable = false, length = 500)
	private String url;
	
	/** The score. */
	@Column(name = "score", nullable = false)
	private BigDecimal score;
	
	/** The score level 1. */
	@Column(name = "scoreLevel1", nullable = false)
	private BigDecimal scoreLevel1;
	
	/** The score level 2. */
	@Column(name = "scoreLevel2", nullable = false)
	private BigDecimal scoreLevel2;
	
	/** The level. */
	@Column(name = "level", nullable = false)
	private String level;
	
	/** The site. */
	@ManyToOne
	@JoinColumn(name = "idSite", nullable = false, referencedColumnName = "id")
	private Site site;
	
	/** The aspect score list. */
	@OneToMany(targetEntity = AspectScore.class, mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<AspectScore> aspectScoreList;
	
	/** The verification page list. */
	@OneToMany(targetEntity = VerificationPage.class, mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<VerificationPage> verificationPageList;

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
	 * Gets the site.
	 *
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * Sets the site.
	 *
	 * @param site the new site
	 */
	public void setSite(Site site) {
		this.site = site;
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
	 * Gets the verification page list.
	 *
	 * @return the verification page list
	 */
	public List<VerificationPage> getVerificationPageList() {
		if (verificationPageList == null) {
			verificationPageList = new ArrayList<>();
		}
		return verificationPageList;
	}

	/**
	 * Sets the verification page list.
	 *
	 * @param verificationPageList the new verification page list
	 */
	public void setVerificationPageList(List<VerificationPage> verificationPageList) {
		this.verificationPageList = verificationPageList;
	}
}
