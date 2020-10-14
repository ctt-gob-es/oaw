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
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ObservatorySubgroupForm.
 */
public class ObservatorySubgroupForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6269110944570286474L;
	/** The description. */
	private String description;
	/** The guideline id. */
	private String guidelineId;
	/** The aspect. */
	private String aspect;
	/** The fail checks. */
	private List<Integer> failChecks;
	/** The only warning checks. */
	private List<Integer> onlyWarningChecks;
	/** The ignore related checks. */
	private List<Integer> ignoreRelatedChecks;
	/** The success checks. */
	private List<Integer> successChecks;
	/** The problems. */
	private List<ProblemForm> problems;
	/** The value. */
	private int value;
	/** The not executed checks. */
	private List<Integer> notExecutedChecks;

	/**
	 * Instantiates a new observatory subgroup form.
	 */
	public ObservatorySubgroupForm() {
		this.failChecks = new ArrayList<>();
		this.onlyWarningChecks = new ArrayList<>();
		this.problems = new ArrayList<>();
		this.ignoreRelatedChecks = new ArrayList<>();
		this.successChecks = new ArrayList<>();
		this.notExecutedChecks = new ArrayList<>();
	}

	/**
	 * Gets the ignore related checks.
	 *
	 * @return the ignore related checks
	 */
	public List<Integer> getIgnoreRelatedChecks() {
		return ignoreRelatedChecks;
	}

	/**
	 * Sets the ignore related checks.
	 *
	 * @param ignoreRelatedChecks the new ignore related checks
	 */
	public void setIgnoreRelatedChecks(List<Integer> ignoreRelatedChecks) {
		this.ignoreRelatedChecks = ignoreRelatedChecks;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gets the guideline id.
	 *
	 * @return the guideline id
	 */
	public String getGuidelineId() {
		return guidelineId;
	}

	/**
	 * Sets the guideline id.
	 *
	 * @param guidelineId the new guideline id
	 */
	public void setGuidelineId(String guidelineId) {
		this.guidelineId = guidelineId;
	}

	/**
	 * Gets the fail checks.
	 *
	 * @return the fail checks
	 */
	public List<Integer> getFailChecks() {
		return failChecks;
	}

	/**
	 * Sets the fail checks.
	 *
	 * @param failChecks the new fail checks
	 */
	public void setFailChecks(List<Integer> failChecks) {
		this.failChecks = failChecks;
	}

	/**
	 * Gets the aspect.
	 *
	 * @return the aspect
	 */
	public String getAspect() {
		return aspect;
	}

	/**
	 * Sets the aspect.
	 *
	 * @param aspect the new aspect
	 */
	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	/**
	 * Gets the problems.
	 *
	 * @return the problems
	 */
	public List<ProblemForm> getProblems() {
		if (problems == null) {
			problems = new ArrayList<>();
		}
		return problems;
	}

	/**
	 * Sets the problems.
	 *
	 * @param problems the new problems
	 */
	public void setProblems(List<ProblemForm> problems) {
		this.problems = problems;
	}

	/**
	 * Gets the only warning checks.
	 *
	 * @return the only warning checks
	 */
	public List<Integer> getOnlyWarningChecks() {
		return onlyWarningChecks;
	}

	/**
	 * Sets the only warning checks.
	 *
	 * @param onlyWarningChecks the new only warning checks
	 */
	public void setOnlyWarningChecks(List<Integer> onlyWarningChecks) {
		this.onlyWarningChecks = onlyWarningChecks;
	}

	/**
	 * Gets the success checks.
	 *
	 * @return the success checks
	 */
	public List<Integer> getSuccessChecks() {
		return successChecks;
	}

	/**
	 * Sets the success checks.
	 *
	 * @param successChecks the new success checks
	 */
	public void setSuccessChecks(List<Integer> successChecks) {
		this.successChecks = successChecks;
	}

	/**
	 * Gets the not executed checks.
	 *
	 * @return the notExecutedChecks
	 */
	public List<Integer> getNotExecutedChecks() {
		return notExecutedChecks;
	}

	/**
	 * Sets the not executed checks.
	 *
	 * @param notExecutedChecks the notExecutedChecks to set
	 */
	public void setNotExecutedChecks(List<Integer> notExecutedChecks) {
		this.notExecutedChecks = notExecutedChecks;
	}
}
