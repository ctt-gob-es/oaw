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

import java.sql.Timestamp;

import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;

/**
 * The Class ObservatorioForm.
 */
public class ObservatorioForm {
	/** The id. */
	private long id;
	/** The nombre. */
	private String nombre;
	/** The periodicidad. */
	private long periodicidad;
	/** The profundidad. */
	private int profundidad;
	/** The amplitud. */
	private long amplitud;
	/** The fecha inicio. */
	private Timestamp fecha_inicio;
	/** The id guideline. */
	private long id_guideline;
	/** The lenguaje. */
	private long lenguaje;
	/** The periodicidad form. */
	private PeriodicidadForm periodicidadForm;
	/** The categoria. */
	private String[] categoria;
	/** The ambito. */
	private String[] ambito;
	/** The complejidad. */
	private String[] complejidad;
	/** The cartucho. */
	private CartuchoForm cartucho;
	/** The pseudo aleatorio. */
	private boolean pseudoAleatorio;
	/** The estado. */
	private int estado;
	/** The tipo. */
	private long tipo;
	/** The tags. */
	private String[] tags;
	/** The tags string. */
	private String tagsString;

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 *
	 * @param tags the new tags
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
	 * Gets the tags string.
	 *
	 * @return the tags string
	 */
	public String getTagsString() {
		return tagsString;
	}

	/**
	 * Sets the tags string.
	 *
	 * @param tagsString the new tags string
	 */
	public void setTagsString(String tagsString) {
		this.tagsString = tagsString;
	}

	/**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
	public long getTipo() {
		return tipo;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}

	/**
	 * Gets the estado.
	 *
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * Sets the estado.
	 *
	 * @param estado the new estado
	 */
	public void setEstado(int estado) {
		this.estado = estado;
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
	 * Gets the periodicidad.
	 *
	 * @return the periodicidad
	 */
	public long getPeriodicidad() {
		return periodicidad;
	}

	/**
	 * Sets the periodicidad.
	 *
	 * @param periodicidad the new periodicidad
	 */
	public void setPeriodicidad(long periodicidad) {
		this.periodicidad = periodicidad;
	}

	/**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
	public int getProfundidad() {
		return profundidad;
	}

	/**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}

	/**
	 * Gets the fecha inicio.
	 *
	 * @return the fecha inicio
	 */
	public Timestamp getFecha_inicio() {
		return fecha_inicio;
	}

	/**
	 * Sets the fecha inicio.
	 *
	 * @param timestamp the new fecha inicio
	 */
	public void setFecha_inicio(Timestamp timestamp) {
		this.fecha_inicio = timestamp;
	}

	/**
	 * Gets the id guideline.
	 *
	 * @return the id guideline
	 */
	public long getId_guideline() {
		return id_guideline;
	}

	/**
	 * Sets the id guideline.
	 *
	 * @param id_guideline the new id guideline
	 */
	public void setId_guideline(long id_guideline) {
		this.id_guideline = id_guideline;
	}

	/**
	 * Gets the amplitud.
	 *
	 * @return the amplitud
	 */
	public long getAmplitud() {
		return amplitud;
	}

	/**
	 * Sets the amplitud.
	 *
	 * @param amplitud the new amplitud
	 */
	public void setAmplitud(long amplitud) {
		this.amplitud = amplitud;
	}

	/**
	 * Gets the lenguaje.
	 *
	 * @return the lenguaje
	 */
	public long getLenguaje() {
		return lenguaje;
	}

	/**
	 * Sets the lenguaje.
	 *
	 * @param lenguaje the new lenguaje
	 */
	public void setLenguaje(long lenguaje) {
		this.lenguaje = lenguaje;
	}

	/**
	 * Gets the periodicidad form.
	 *
	 * @return the periodicidad form
	 */
	public PeriodicidadForm getPeriodicidadForm() {
		return periodicidadForm;
	}

	/**
	 * Sets the periodicidad form.
	 *
	 * @param periodicidadForm the new periodicidad form
	 */
	public void setPeriodicidadForm(PeriodicidadForm periodicidadForm) {
		this.periodicidadForm = periodicidadForm;
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

	/**
	 * Checks if is pseudo aleatorio.
	 *
	 * @return true, if is pseudo aleatorio
	 */
	public boolean isPseudoAleatorio() {
		return pseudoAleatorio;
	}

	/**
	 * Sets the pseudo aleatorio.
	 *
	 * @param pseudoAleatorio the new pseudo aleatorio
	 */
	public void setPseudoAleatorio(boolean pseudoAleatorio) {
		this.pseudoAleatorio = pseudoAleatorio;
	}

	/**
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
	public String[] getCategoria() {
		return categoria;
	}

	/**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
	public void setCategoria(String[] categoria) {
		this.categoria = categoria;
	}

	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public String[] getAmbito() {
		return ambito;
	}

	/**
	 * Sets the ambito.
	 *
	 * @param ambito the new ambito
	 */
	public void setAmbito(String[] ambito) {
		this.ambito = ambito;
	}

	/**
	 * Gets the complejidad.
	 *
	 * @return the complejidad
	 */
	public String[] getComplejidad() {
		return complejidad;
	}

	/**
	 * Sets the complejidad.
	 *
	 * @param complejidad the new complejidad
	 */
	public void setComplejidad(String[] complejidad) {
		this.complejidad = complejidad;
	}
}
