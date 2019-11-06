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
package es.inteco.rastreador2.actionform.etiquetas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;

/**
 * The Class EtiquetaForm.
 */
public class EtiquetaForm extends ValidatorForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1752910926145527308L;

	/** The tag id. */
	private Long id;


	/** The tag name. */
	private String name;
	
	/** The clasificacion. */
	private ClasificacionForm clasificacion;

	/** The nombre antiguo. */
	private String nombreAntiguo;
	

	/**
	 * Instantiates a new etiqueta form.
	 */
	public EtiquetaForm() {
 
	}


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
	 * @param  the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * Gets the clasificacion.
	 *
	 * @return the clasificacion
	 */
	public ClasificacionForm getClasificacion() {
		return clasificacion;
	}

	/**
	 * Sets the clasificacion.
	 *
	 * @param complejidad the new clasificacion
	 */
	public void setClasificacion(ClasificacionForm clasificacion) {
		this.clasificacion = clasificacion;
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
	 * @param nombreAntiguo
	 *            the new nombre antiguo
	 */
	public void setNombreAntiguo(String nombreAntiguo) {
		this.nombreAntiguo = nombreAntiguo;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "EtiquetaForm [id=" + id + ", name" + name + ",nombreAntiguo" + nombreAntiguo + ",clasificacion=" + clasificacion + "]";
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
		EtiquetaForm other = (EtiquetaForm) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    

}