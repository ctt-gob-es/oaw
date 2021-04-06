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
package es.inteco.rastreador2.actionform.comun;

import org.apache.struts.action.ActionForm;

/**
 * The Class CambiarPassUsuarioCForm.
 */
public class CambiarPassUsuarioCForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The cancel button. */
    private String username, password, password2, passwold, cancelButton;

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
	 * Gets the cancel button.
	 *
	 * @return the cancel button
	 */
    public String getCancelButton() {
        return cancelButton;
    }

    /**
	 * Sets the cancel button.
	 *
	 * @param cancelButton the new cancel button
	 */
    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }


}