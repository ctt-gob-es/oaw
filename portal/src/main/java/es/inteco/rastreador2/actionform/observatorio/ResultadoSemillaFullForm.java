/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.actionform.observatorio;

import java.util.List;

import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

/**
 * The Class ResultadoSemillaFullForm.
 */
public class ResultadoSemillaFullForm extends ResultadoSemillaForm {
	/** The nombre. */
	private String nombre;
	/** The nombre antiguo. */
	private String nombre_antiguo;
	/** The lista urls. */
	private List<String> listaUrls;
	/** The lista urls string. */
	private String listaUrlsString;
	/** The acronimo. */
	private String acronimo;
	/** The activa. */
	private boolean activa;
	/** The activa str. */
	private String activaStr;
	/** The categoria. */
	private CategoriaForm categoria;
	/** The ambito. */
	private AmbitoForm ambito;
	/** The complejidad. */
	private ComplejidadForm complejidad;
	/** The in directory. */
	private boolean inDirectory;
	/** The in directory str. */
	private String inDirectoryStr;
	/** The dependencias. */
	private List<DependenciaForm> dependencias;
	/** The etiquetas. */
	private List<EtiquetaForm> etiquetas;
	/** The tags string. */
	private String tagsString;

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
	 * Gets the nombre antiguo.
	 *
	 * @return the nombre antiguo
	 */
	public String getNombre_antiguo() {
		return nombre_antiguo;
	}

	/**
	 * Sets the nombre antiguo.
	 *
	 * @param nombre_antiguo the new nombre antiguo
	 */
	public void setNombre_antiguo(String nombre_antiguo) {
		this.nombre_antiguo = nombre_antiguo;
	}

	/**
	 * Gets the lista urls.
	 *
	 * @return the lista urls
	 */
	public List<String> getListaUrls() {
		return listaUrls;
	}

	/**
	 * Sets the lista urls.
	 *
	 * @param listaUrls the new lista urls
	 */
	public void setListaUrls(List<String> listaUrls) {
		this.listaUrls = listaUrls;
	}

	/**
	 * Gets the lista urls string.
	 *
	 * @return the lista urls string
	 */
	public String getListaUrlsString() {
		return listaUrlsString;
	}

	/**
	 * Sets the lista urls string.
	 *
	 * @param listaUrlsString the new lista urls string
	 */
	public void setListaUrlsString(String listaUrlsString) {
		this.listaUrlsString = listaUrlsString;
	}

	/**
	 * Gets the acronimo.
	 *
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * Sets the acronimo.
	 *
	 * @param acronimo the new acronimo
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * Checks if is activa.
	 *
	 * @return true, if is activa
	 */
	public boolean isActiva() {
		return activa;
	}

	/**
	 * Sets the activa.
	 *
	 * @param activa the new activa
	 */
	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	/**
	 * Gets the activa str.
	 *
	 * @return the activa str
	 */
	public String getActivaStr() {
		return activaStr;
	}

	/**
	 * Sets the activa str.
	 *
	 * @param activaStr the new activa str
	 */
	public void setActivaStr(String activaStr) {
		this.activaStr = activaStr;
	}

	/**
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
	public CategoriaForm getCategoria() {
		return categoria;
	}

	/**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
	public void setCategoria(CategoriaForm categoria) {
		this.categoria = categoria;
	}

	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public AmbitoForm getAmbito() {
		return ambito;
	}

	/**
	 * Sets the ambito.
	 *
	 * @param ambito the new ambito
	 */
	public void setAmbito(AmbitoForm ambito) {
		this.ambito = ambito;
	}

	/**
	 * Gets the complejidad.
	 *
	 * @return the complejidad
	 */
	public ComplejidadForm getComplejidad() {
		return complejidad;
	}

	/**
	 * Sets the complejidad.
	 *
	 * @param complejidad the new complejidad
	 */
	public void setComplejidad(ComplejidadForm complejidad) {
		this.complejidad = complejidad;
	}

	/**
	 * Checks if is in directory.
	 *
	 * @return true, if is in directory
	 */
	public boolean isInDirectory() {
		return inDirectory;
	}

	/**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
	public void setInDirectory(boolean inDirectory) {
		this.inDirectory = inDirectory;
	}

	/**
	 * Gets the in directory str.
	 *
	 * @return the in directory str
	 */
	public String getInDirectoryStr() {
		return inDirectoryStr;
	}

	/**
	 * Sets the in directory str.
	 *
	 * @param inDirectoryStr the new in directory str
	 */
	public void setInDirectoryStr(String inDirectoryStr) {
		this.inDirectoryStr = inDirectoryStr;
	}

	/**
	 * Gets the dependencias.
	 *
	 * @return the dependencias
	 */
	public List<DependenciaForm> getDependencias() {
		return dependencias;
	}

	/**
	 * Sets the dependencias.
	 *
	 * @param dependencias the new dependencias
	 */
	public void setDependencias(List<DependenciaForm> dependencias) {
		this.dependencias = dependencias;
	}

	/**
	 * Gets the etiquetas.
	 *
	 * @return the etiquetas
	 */
	public List<EtiquetaForm> getEtiquetas() {
		return etiquetas;
	}

	/**
	 * Sets the etiquetas.
	 *
	 * @param etiquetas the new etiquetas
	 */
	public void setEtiquetas(List<EtiquetaForm> etiquetas) {
		this.etiquetas = etiquetas;
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
}
