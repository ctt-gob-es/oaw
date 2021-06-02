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
package es.inteco.rastreador2.actionform.cuentausuario;


import es.inteco.rastreador2.utils.ListadoCuentasUsuario;
import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class CargarCuentasUsuarioForm.
 */
public class CargarCuentasUsuarioForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The user. */
    private String user;
    
    /** The pass. */
    private String pass;
    
    /** The listado cuentas usuario. */
    private List<ListadoCuentasUsuario> listadoCuentasUsuario;
    
    /** The num cuentas usuario. */
    private int numCuentasUsuario;

    /**
	 * Gets the user.
	 *
	 * @return the user
	 */
    public String getUser() {
        return user;
    }

    /**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
    public void setUser(String user) {
        this.user = user;
    }

    /**
	 * Gets the pass.
	 *
	 * @return the pass
	 */
    public String getPass() {
        return pass;
    }

    /**
	 * Sets the pass.
	 *
	 * @param pass the new pass
	 */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
	 * Gets the listado cuentas usuario.
	 *
	 * @return the listado cuentas usuario
	 */
    public List<ListadoCuentasUsuario> getListadoCuentasUsuario() {
        return listadoCuentasUsuario;
    }

    /**
	 * Sets the listado cuentas usuario.
	 *
	 * @param listadoCuentasUsuario the new listado cuentas usuario
	 */
    public void setListadoCuentasUsuario(List<ListadoCuentasUsuario> listadoCuentasUsuario) {
        this.listadoCuentasUsuario = listadoCuentasUsuario;
    }

    /**
	 * Gets the num cuentas usuario.
	 *
	 * @return the num cuentas usuario
	 */
    public int getNumCuentasUsuario() {
        return numCuentasUsuario;
    }

    /**
	 * Sets the num cuentas usuario.
	 *
	 * @param numCuentasUsuario the new num cuentas usuario
	 */
    public void setNumCuentasUsuario(int numCuentasUsuario) {
        this.numCuentasUsuario = numCuentasUsuario;
    }
}