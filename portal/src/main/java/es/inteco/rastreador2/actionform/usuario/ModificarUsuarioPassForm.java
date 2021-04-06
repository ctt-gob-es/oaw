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
package es.inteco.rastreador2.actionform.usuario;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class ModificarUsuarioPassForm.
 */
public class ModificarUsuarioPassForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The username. */
    private String username;
    
    /** The password. */
    private String password;
    
    /** The password 2. */
    private String password2;
    
    /** The passwold. */
    private String passwold;
    
    /** The id usuario. */
    private String idUsuario;
    
    /** The role type. */
    private String roleType;

    /**
	 * Gets the id usuario.
	 *
	 * @return the id usuario
	 */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
	 * Sets the id usuario.
	 *
	 * @param idUsuario the new id usuario
	 */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
	 * Gets the username.
	 *
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * Gets the password.
	 *
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Gets the password 2.
	 *
	 * @return the password 2
	 */
    public String getPassword2() {
        return password2;
    }

    /**
	 * Sets the password 2.
	 *
	 * @param password2 the new password 2
	 */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
	 * Gets the serial version UID.
	 *
	 * @return the serial version UID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
	 * Gets the passwold.
	 *
	 * @return the passwold
	 */
    public String getPasswold() {
        return passwold;
    }

    /**
	 * Sets the passwold.
	 *
	 * @param passwold the new passwold
	 */
    public void setPasswold(String passwold) {
        this.passwold = passwold;
    }

    /**
	 * Gets the role type.
	 *
	 * @return the role type
	 */
    public String getRoleType() {
        return roleType;
    }

    /**
	 * Sets the role type.
	 *
	 * @param roleType the new role type
	 */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

}
