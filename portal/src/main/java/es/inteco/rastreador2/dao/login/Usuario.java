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
package es.inteco.rastreador2.dao.login;

import java.util.List;

/**
 * The Class Usuario.
 */
public class Usuario {
    
    /** The id. */
    private Long id;
    
    /** The login. */
    private String login;
    
    /** The tipos. */
    private List<String> tipos;
    
    /** The email. */
    private String email;
    
    /** The name. */
    private String name;
    
    /** The surname. */
    private String surname;
    
    /** The department. */
    private String department;
    
    /** The cartridge. */
    private List<String> cartridge;

    /**
	 * Gets the login.
	 *
	 * @return the login
	 */
    public String getLogin() {
        return login;
    }

    /**
	 * Sets the login.
	 *
	 * @param login the new login
	 */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
	 * Gets the email.
	 *
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
    public void setEmail(String email) {
        this.email = email;
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
	 * Gets the surname.
	 *
	 * @return the surname
	 */
    public String getSurname() {
        return surname;
    }

    /**
	 * Sets the surname.
	 *
	 * @param surname the new surname
	 */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
	 * Gets the department.
	 *
	 * @return the department
	 */
    public String getDepartment() {
        return department;
    }

    /**
	 * Sets the department.
	 *
	 * @param department the new department
	 */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
	 * Gets the cartridge.
	 *
	 * @return the cartridge
	 */
    public List<String> getCartridge() {
        return cartridge;
    }

    /**
	 * Sets the cartridge.
	 *
	 * @param cartridge the new cartridge
	 */
    public void setCartridge(List<String> cartridge) {
        this.cartridge = cartridge;
    }

    /**
	 * Gets the tipos.
	 *
	 * @return the tipos
	 */
    public List<String> getTipos() {
        return tipos;
    }

    /**
	 * Sets the tipos.
	 *
	 * @param tipos the new tipos
	 */
    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
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
}
