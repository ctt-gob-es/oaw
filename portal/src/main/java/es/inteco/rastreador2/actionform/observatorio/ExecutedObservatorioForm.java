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

import java.util.Date;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class NuevoObservatorioForm.
 */
public class ExecutedObservatorioForm extends ValidatorForm {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5448154634354091798L;
	/** The id observatorio. */
	private Integer idObservatorio;
	/** The id obs ex. */
	private Integer idObsEx;
	/** The nombre. */
	private String nombre;
	/** The cartucho. */
	private String cartucho;
	/** The fecha ex. */
	private Date fechaEx;
	/** The fecha ex sting. */
	private String fechaExSting;
	/** The ambito. */
	private String ambito;
	/** The tipo. */
	private String tipo;

	/**
	 * Gets the id observatorio.
	 *
	 * @return the idObservatorio
	 */
	public Integer getIdObservatorio() {
		return idObservatorio;
	}

	/**
	 * Sets the id observatorio.
	 *
	 * @param idObservatorio the idObservatorio to set
	 */
	public void setIdObservatorio(Integer idObservatorio) {
		this.idObservatorio = idObservatorio;
	}

	/**
	 * Gets the id obs ex.
	 *
	 * @return the idObsEx
	 */
	public Integer getIdObsEx() {
		return idObsEx;
	}

	/**
	 * Sets the id obs ex.
	 *
	 * @param idObsEx the idObsEx to set
	 */
	public void setIdObsEx(Integer idObsEx) {
		this.idObsEx = idObsEx;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
	public String getCartucho() {
		return cartucho;
	}

	/**
	 * Sets the cartucho.
	 *
	 * @param cartucho the cartucho to set
	 */
	public void setCartucho(String cartucho) {
		this.cartucho = cartucho;
	}

	/**
	 * Gets the fecha ex.
	 *
	 * @return the fechaEx
	 */
	public Date getFechaEx() {
		return fechaEx;
	}

	/**
	 * Sets the fecha ex.
	 *
	 * @param fechaEx the fechaEx to set
	 */
	public void setFechaEx(Date fechaEx) {
		this.fechaEx = fechaEx;
	}

	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public String getAmbito() {
		return ambito;
	}

	/**
	 * Sets the ambito.
	 *
	 * @param ambito the ambito to set
	 */
	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	/**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Gets the fecha ex sting.
	 *
	 * @return the fechaExSting
	 */
	public String getFechaExSting() {
		return fechaExSting;
	}

	/**
	 * Sets the fecha ex sting.
	 *
	 * @param fechaExSting the fechaExSting to set
	 */
	public void setFechaExSting(String fechaExSting) {
		this.fechaExSting = fechaExSting;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}