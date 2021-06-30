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
package es.inteco.rastreador2.actionform.semillas;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;

/**
 * DependenciaForm. Clase para el manejo de dependencias.
 * 
 * @author alvaro.pelaez
 * 
 */
public class DependenciaForm extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6360416159148020455L;
	/** The id. */
	private Long id;
	/** The name. */
	private String name;
	/** The nombre antiguo. */
	private String nombreAntiguo;
	/** The send auto. */
	private Boolean sendAuto;
	/** The official. */
	private Boolean official;
	/** The emails. */
	private String emails;
	/** The ambito. */
	private List<AmbitoForm> ambitos;
	/** The tag. */
	private EtiquetaForm tag;
	/** The file seeds. */
	private FormFile dependencyFile;
	/** The acronym. */
	private String acronym;
	/** The send auto search. */
	private Integer sendAutoSearch;
	/** The official search. */
	private Integer officialSearch;

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
	 * Gets the nombre antiguo.
	 *
	 * @return the nombre antiguo
	 */
	public String getNombreAntiguo() {
		return nombreAntiguo;
	}

	/**
	 * Sets the nombre antiguo.
	 *
	 * @param nombreAntiguo the new nombre antiguo
	 */
	public void setNombreAntiguo(String nombreAntiguo) {
		this.nombreAntiguo = nombreAntiguo;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DependenciaForm [id=" + id + ", name=" + name + "]";
	}

	/**
	 * Checks if is send auto.
	 *
	 * @return the sendAuto
	 */
	public Boolean getSendAuto() {
		return sendAuto;
	}

	/**
	 * Sets the send auto.
	 *
	 * @param sendAuto the sendAuto to set
	 */
	public void setSendAuto(Boolean sendAuto) {
		this.sendAuto = sendAuto;
	}

	/**
	 * Gets the emails.
	 *
	 * @return the emails
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * Sets the emails.
	 *
	 * @param emails the emails to set
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}

	/**
	 * Gets the ambitos.
	 *
	 * @return the ambitos
	 */
	public List<AmbitoForm> getAmbitos() {
		return ambitos;
	}

	/**
	 * Sets the ambitos.
	 *
	 * @param ambitos the new ambitos
	 */
	public void setAmbitos(List<AmbitoForm> ambitos) {
		this.ambitos = ambitos;
	}

	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public EtiquetaForm getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 *
	 * @param tag the tag to set
	 */
	public void setTag(EtiquetaForm tag) {
		this.tag = tag;
	}

	/**
	 * Checks if is official.
	 *
	 * @return the official
	 */
	public Boolean getOfficial() {
		return official;
	}

	/**
	 * Sets the official.
	 *
	 * @param official the official to set
	 */
	public void setOfficial(Boolean official) {
		this.official = official;
	}

	/**
	 * Gets the dependency file.
	 *
	 * @return the dependency file
	 */
	public FormFile getDependencyFile() {
		return dependencyFile;
	}

	/**
	 * Sets the dependency file.
	 *
	 * @param dependencyFile the new dependency file
	 */
	public void setDependencyFile(FormFile dependencyFile) {
		this.dependencyFile = dependencyFile;
	}

	/**
	 * Gets the acronym.
	 *
	 * @return the acronym
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * Sets the acronym.
	 *
	 * @param acronym the new acronym
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Gets the send auto search.
	 *
	 * @return the send auto search
	 */
	public Integer getSendAutoSearch() {
		return sendAutoSearch;
	}

	/**
	 * Sets the send auto search.
	 *
	 * @param sendAutoSearch the new send auto search
	 */
	public void setSendAutoSearch(Integer sendAutoSearch) {
		this.sendAutoSearch = sendAutoSearch;
	}

	/**
	 * Gets the official search.
	 *
	 * @return the official search
	 */
	public Integer getOfficialSearch() {
		return officialSearch;
	}

	/**
	 * Sets the official search.
	 *
	 * @param officialSearch the new official search
	 */
	public void setOfficialSearch(Integer officialSearch) {
		this.officialSearch = officialSearch;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependenciaForm other = (DependenciaForm) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
