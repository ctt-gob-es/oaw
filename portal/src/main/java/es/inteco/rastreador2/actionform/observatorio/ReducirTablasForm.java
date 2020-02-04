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

import java.io.Serializable;
import org.apache.struts.validator.ValidatorForm;
/**
 * The Class ReducirTablasForm.
 */


public class ReducirTablasForm extends ValidatorForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1752910926145527308L;

	
	/** The table size. */
	private Long size;


	/** The table name. */
	private String name;


	/**
	 * Instantiates a new ReducirTablas form.
	 */
	public ReducirTablasForm() {
 
	}


	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param id the new size
	 */
	public void setSize(Long size) {
		this.size = size;
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
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ReducirTablasForm [name=" + name + ",size=" + size + "]";
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
		ReducirTablasForm other = (ReducirTablasForm) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    

}