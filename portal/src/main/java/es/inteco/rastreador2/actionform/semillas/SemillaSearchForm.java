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
package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 * The Class SemillaSearchForm.
 */
public class SemillaSearchForm extends ValidatorForm {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private long id;

	/** The nombre. */
	private String nombre;

	/** The categoria. */
	private String categoria;

	/** The ambito. */
	private String ambito;

	/** The url. */
	private String url;

	/** The file seeds. */
	private FormFile fileSeeds;

	/**
	 * Gets the file seeds.
	 *
	 * @return the file seeds
	 */
	public FormFile getFileSeeds() {
		return fileSeeds;
	}

	/**
	 * Sets the file seeds.
	 *
	 * @param fileSeeds the new file seeds
	 */
	public void setFileSeeds(FormFile fileSeeds) {
		this.fileSeeds = fileSeeds;
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
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
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