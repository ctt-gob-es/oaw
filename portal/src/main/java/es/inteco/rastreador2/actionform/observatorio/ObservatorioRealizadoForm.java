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

import es.inteco.rastreador2.dao.login.CartuchoForm;

/**
 * The Class ObservatorioRealizadoForm.
 */
public class ObservatorioRealizadoForm {
	/** The id. */
	private long id;
	/** The observatorio. */
	private ObservatorioForm observatorio;
	/** The cartucho. */
	private CartuchoForm cartucho;
	/** The fecha. */
	private Date fecha;
	/** The fecha str. */
	private String fechaStr;
	/** The ambito. */
	private String ambito;

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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the observatorio.
	 *
	 * @return the observatorio
	 */
	public ObservatorioForm getObservatorio() {
		return observatorio;
	}

	/**
	 * Sets the observatorio.
	 *
	 * @param observatorio the new observatorio
	 */
	public void setObservatorio(ObservatorioForm observatorio) {
		this.observatorio = observatorio;
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Gets the fecha str.
	 *
	 * @return the fecha str
	 */
	public String getFechaStr() {
		return fechaStr;
	}

	/**
	 * Sets the fecha str.
	 *
	 * @param fechaStr the new fecha str
	 */
	public void setFechaStr(String fechaStr) {
		this.fechaStr = fechaStr;
	}

	/**
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
	public CartuchoForm getCartucho() {
		return cartucho;
	}

	/**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
	public void setCartucho(CartuchoForm cartucho) {
		this.cartucho = cartucho;
	}
}
