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
package es.inteco.rastreador2.intav.form;

import java.math.BigDecimal;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import es.inteco.rastreador2.export.database.form.VerificationScoreForm;

/**
 * The Class ScoreForm.
 */
public class ScoreForm {
	/**
	 * Instantiates a new score form.
	 */
	public ScoreForm() {
		totalScore = BigDecimal.ZERO;
		scoreLevel1 = BigDecimal.ZERO;
		scoreLevel2 = BigDecimal.ZERO;
		scoreLevelA = BigDecimal.ZERO;
		scoreLevelAA = BigDecimal.ZERO;
		suitabilityScore = BigDecimal.ZERO;
	}

	/** The level. */
	private String level;
	/** The total score. */
	private BigDecimal totalScore;
	/** The score level 1. */
	private BigDecimal scoreLevel1;
	/** The score level 2. */
	private BigDecimal scoreLevel2;
	/** The score level A. */
	private BigDecimal scoreLevelA;
	/** The score level AA. */
	private BigDecimal scoreLevelAA;
	/** The suitability score. */
	private BigDecimal suitabilityScore;
	/** The verifications 1. */
	private List<LabelValueBean> verifications1;
	/** The verifications 2. */
	private List<LabelValueBean> verifications2;
	/** The compliance. */
	private String compliance;
	/** The Verification score list. */
	private List<VerificationScoreForm> VerificationScoreList;

	/**
	 * Gets the verification score list.
	 *
	 * @return the verificationScoreList
	 */
	public List<VerificationScoreForm> getVerificationScoreList() {
		return VerificationScoreList;
	}

	/**
	 * Sets the verification score list.
	 *
	 * @param verificationScoreList the verificationScoreList to set
	 */
	public void setVerificationScoreList(List<VerificationScoreForm> verificationScoreList) {
		VerificationScoreList = verificationScoreList;
	}

	/**
	 * Gets the total score.
	 *
	 * @return the total score
	 */
	public BigDecimal getTotalScore() {
		return totalScore;
	}

	/**
	 * Sets the total score.
	 *
	 * @param totalScore the new total score
	 */
	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
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
	 * Gets the score level A.
	 *
	 * @return the score level A
	 */
	public BigDecimal getScoreLevelA() {
		return scoreLevelA;
	}

	/**
	 * Sets the score level A.
	 *
	 * @param scoreLevelA the new score level A
	 */
	public void setScoreLevelA(BigDecimal scoreLevelA) {
		this.scoreLevelA = scoreLevelA;
	}

	/**
	 * Gets the score level AA.
	 *
	 * @return the score level AA
	 */
	public BigDecimal getScoreLevelAA() {
		return scoreLevelAA;
	}

	/**
	 * Sets the score level AA.
	 *
	 * @param scoreLevelAA the new score level AA
	 */
	public void setScoreLevelAA(BigDecimal scoreLevelAA) {
		this.scoreLevelAA = scoreLevelAA;
	}

	/**
	 * Gets the verifications 1.
	 *
	 * @return the verifications 1
	 */
	public List<LabelValueBean> getVerifications1() {
		return verifications1;
	}

	/**
	 * Sets the verifications 1.
	 *
	 * @param verifications1 the new verifications 1
	 */
	public void setVerifications1(List<LabelValueBean> verifications1) {
		this.verifications1 = verifications1;
	}

	/**
	 * Gets the verifications 2.
	 *
	 * @return the verifications 2
	 */
	public List<LabelValueBean> getVerifications2() {
		return verifications2;
	}

	/**
	 * Sets the verifications 2.
	 *
	 * @param verifications2 the new verifications 2
	 */
	public void setVerifications2(List<LabelValueBean> verifications2) {
		this.verifications2 = verifications2;
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
	 * Gets the suitability score.
	 *
	 * @return the suitability score
	 */
	public BigDecimal getSuitabilityScore() {
		return suitabilityScore;
	}

	/**
	 * Sets the suitability score.
	 *
	 * @param suitabilityScore the new suitability score
	 */
	public void setSuitabilityScore(BigDecimal suitabilityScore) {
		this.suitabilityScore = suitabilityScore;
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
	 * @param compliance the new compliance
	 */
	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}
}
