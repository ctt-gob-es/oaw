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

import java.util.List;

/**
 * The Class ListadoObservatorio.
 */
public class ListadoObservatorio {
	/** The nombre observatorio. */
	private String nombreObservatorio;
	/** The dominio. */
	private List<String> dominio;
	/** The id observatorio. */
	private Long id_observatorio;
	/** The id cartucho. */
	private long id_cartucho;
	/** The cartucho. */
	private String cartucho;
	/** The tipo. */
	private String tipo;
	/** The ambito. */
	private String ambito;

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
	 * @param cartucho the new cartucho
	 */
	public void setCartucho(String cartucho) {
		this.cartucho = cartucho;
	}

	/**
	 * Gets the nombre observatorio.
	 *
	 * @return the nombre observatorio
	 */
	public String getNombreObservatorio() {
		return nombreObservatorio;
	}

	/**
	 * Sets the nombre observatorio.
	 *
	 * @param nombreObservatorio the new nombre observatorio
	 */
	public void setNombreObservatorio(String nombreObservatorio) {
		this.nombreObservatorio = nombreObservatorio;
	}

	/**
	 * Gets the dominio.
	 *
	 * @return the dominio
	 */
	public List<String> getDominio() {
		return dominio;
	}

	/**
	 * Sets the dominio.
	 *
	 * @param dominio the new dominio
	 */
	public void setDominio(List<String> dominio) {
		this.dominio = dominio;
	}

	/**
	 * Gets the id observatorio.
	 *
	 * @return the id observatorio
	 */
	public Long getId_observatorio() {
		return id_observatorio;
	}

	/**
	 * Sets the id observatorio.
	 *
	 * @param id_observatorio the new id observatorio
	 */
	public void setId_observatorio(Long id_observatorio) {
		this.id_observatorio = id_observatorio;
	}

	/**
	 * Gets the id cartucho.
	 *
	 * @return the id cartucho
	 */
	public long getId_cartucho() {
		return id_cartucho;
	}

	/**
	 * Sets the id cartucho.
	 *
	 * @param id_cartucho the new id cartucho
	 */
	public void setId_cartucho(long id_cartucho) {
		this.id_cartucho = id_cartucho;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	 * @param ambito the new ambito
	 */
	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
}
