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
package es.inteco.rastreador2.dao.export.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class ApiKey.
 */

@Entity
@Table(name = "export_apiKey")
public class ApiKey {
	
	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long idApiKey;

	@Column (name = "nombre", nullable = false)
	private String name;

	@Column (name = "descripcion")
	private String description;

	@Column (name = "tipo")
	private String type;

	 /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public Long getId() {
        return idApiKey;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        this.idApiKey = id;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
    public void setDescription(String description) {
        this.description = description;
    }
	
	 /**
	 * Gets the type.
	 *
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    public void setType(String type) {
        this.type = type;
    }
}
