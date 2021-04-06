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


import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.Role;
import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class EliminarUsuarioSistemaForm.
 */
public class EliminarUsuarioSistemaForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private Long id;
    
    /** The roles. */
    private List<Role> roles;
    
    /** The cartuchos. */
    private List<CartuchoForm> cartuchos;
    
    /** The nombre. */
    private String nombre;
    
    /** The user. */
    private String usuario, user;
    
    /** The apellidos. */
    private String apellidos;
    
    /** The departamento. */
    private String departamento;
    
    /** The email. */
    private String email;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The nombre cuenta. */
    private List<String> nombreCuenta;


    /**
	 * Gets the nombre cuenta.
	 *
	 * @return the nombre cuenta
	 */
    public List<String> getNombreCuenta() {
        return nombreCuenta;
    }

    /**
	 * Sets the nombre cuenta.
	 *
	 * @param nombreCuenta the new nombre cuenta
	 */
    public void setNombreCuenta(List<String> nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    /**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public List<CartuchoForm> getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(List<CartuchoForm> cartuchos) {
        this.cartuchos = cartuchos;
    }

    /**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
    public List<Role> getRoles() {
        return roles;
    }

    /**
	 * Sets the roles.
	 *
	 * @param roles the new roles
	 */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
	 * Gets the usuario.
	 *
	 * @return the usuario
	 */
    public String getUsuario() {
        return usuario;
    }

    /**
	 * Sets the usuario.
	 *
	 * @param usuario the new usuario
	 */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
	 * Gets the apellidos.
	 *
	 * @return the apellidos
	 */
    public String getApellidos() {
        return apellidos;
    }

    /**
	 * Sets the apellidos.
	 *
	 * @param apellidos the new apellidos
	 */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
	 * Gets the departamento.
	 *
	 * @return the departamento
	 */
    public String getDepartamento() {
        return departamento;
    }

    /**
	 * Sets the departamento.
	 *
	 * @param departamento the new departamento
	 */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
	 * Gets the id cartucho.
	 *
	 * @return the id cartucho
	 */
    public int getId_cartucho() {
        return id_cartucho;
    }

    /**
	 * Sets the id cartucho.
	 *
	 * @param id_cartucho the new id cartucho
	 */
    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
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