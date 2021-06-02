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
package es.inteco.rastreador2.actionform.observatorio;

/**
 * The Class ResultadoSemillaForm.
 */
public class ResultadoSemillaForm {
	/** The id. */
	private String id;
	/** The name. */
	private String name;
	/** The id crawling. */
	private String idCrawling;
	/** The id fulfilled crawling. */
	private String idFulfilledCrawling;
	/** The active. */
	private boolean active;
	/** The score. */
	private String score;
	/** The nivel. */
	private String nivel;
	/** The id category. */
	private long idCategory;
	/** The id ambit. */
	private long idAmbit;
	/** The id complexity. */
	private long idComplexity;
	/** The compliance. */
	private String compliance;
	/** The num crawls. */
	private int numCrawls;
	/** The percent 7 num crawls. */
	private double percentNumCrawls;
	/** The observaciones. */
	private String observaciones;

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

	/**
	 * Gets the id category.
	 *
	 * @return the id category
	 */
	public long getIdCategory() {
		return idCategory;
	}

	/**
	 * Sets the id category.
	 *
	 * @param idCategory the new id category
	 */
	public void setIdCategory(long idCategory) {
		this.idCategory = idCategory;
	}

	/**
	 * Gets the id ambit.
	 *
	 * @return the id ambit
	 */
	public long getIdAmbit() {
		return idAmbit;
	}

	/**
	 * Sets the id ambit.
	 *
	 * @param idAmbit the new id ambit
	 */
	public void setIdAmbit(long idAmbit) {
		this.idAmbit = idAmbit;
	}

	/**
	 * Gets the id complexity.
	 *
	 * @return the id complexity
	 */
	public long getIdComplexity() {
		return idComplexity;
	}

	/**
	 * Sets the id complexity.
	 *
	 * @param idComplexity the new id complexity
	 */
	public void setIdComplexity(long idComplexity) {
		this.idComplexity = idComplexity;
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
	 * Gets the id crawling.
	 *
	 * @return the id crawling
	 */
	public String getIdCrawling() {
		return idCrawling;
	}

	/**
	 * Sets the id crawling.
	 *
	 * @param idCrawling the new id crawling
	 */
	public void setIdCrawling(String idCrawling) {
		this.idCrawling = idCrawling;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	 * Gets the id fulfilled crawling.
	 *
	 * @return the id fulfilled crawling
	 */
	public String getIdFulfilledCrawling() {
		return idFulfilledCrawling;
	}

	/**
	 * Sets the id fulfilled crawling.
	 *
	 * @param idFulfilledCrawling the new id fulfilled crawling
	 */
	public void setIdFulfilledCrawling(String idFulfilledCrawling) {
		this.idFulfilledCrawling = idFulfilledCrawling;
	}

	/**
	 * Gets the nivel.
	 *
	 * @return the nivel
	 */
	public String getNivel() {
		return nivel;
	}

	/**
	 * Sets the nivel.
	 *
	 * @param nivel the new nivel
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/**
	 * Gets the num crawls.
	 *
	 * @return the num crawls
	 */
	public int getNumCrawls() {
		return numCrawls;
	}

	/**
	 * Gets the observaciones.
	 *
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Sets the observaciones.
	 *
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Sets the num crawls.
	 *
	 * @param numCrawls the new num crawls
	 */
	public void setNumCrawls(int numCrawls) {
		this.numCrawls = numCrawls;
	}

	/**
	 * Gets the percent 7 num crawls.
	 *
	 * @return the percent7NumCrawls
	 */
	public double getPercentNumCrawls() {
		return percentNumCrawls;
	}

	/**
	 * Sets the percent 7 num crawls.
	 *
	 * @param percentNumCrawls the new percent num crawls
	 */
	public void setPercentNumCrawls(double percentNumCrawls) {
		this.percentNumCrawls = percentNumCrawls;
	}
}
