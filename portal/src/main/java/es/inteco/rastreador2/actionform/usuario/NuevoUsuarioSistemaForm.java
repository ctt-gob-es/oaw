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


import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.Role;
import org.apache.struts.validator.ValidatorForm;

import java.util.List;


/**
 * The Class NuevoUsuarioSistemaForm.
 */
public class NuevoUsuarioSistemaForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre. */
    private String nombre;
    
    /** The password. */
    private String password;
    
    /** The confirmar password. */
    private String confirmar_password;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The selected roles. */
    private String[] selectedRoles;
    
    /** The selected cartuchos. */
    private String[] selectedCartuchos;
    
    /** The nombre 2. */
    private String nombre2;
    
    /** The apellidos. */
    private String apellidos;
    
    /** The departamento. */
    private String departamento;
    
    /** The email. */
    private String email;
    
    /** The cuenta cliente. */
    private String[] cuenta_cliente;
    
    /** The cuenta cliente V. */
    private CargarCuentasUsuarioForm cuenta_clienteV;
    
    /** The observatorio. */
    private String[] observatorio;
    
    /** The observatorio V. */
    private CargarObservatorioForm observatorioV;

    /** The cartuchos. */
    //Para Nuevo Usuario
    private List<String> cartuchos;
    
    /** The roles. */
    private List<Role> roles;
    
    /** The cartuchos list. */
    private List<CartuchoForm> cartuchosList;

    /**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public List<String> getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(List<String> cartuchos) {
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
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public String getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
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
	 * Gets the nombre 2.
	 *
	 * @return the nombre 2
	 */
    public String getNombre2() {
        return nombre2;
    }

    /**
	 * Sets the nombre 2.
	 *
	 * @param nombre2 the new nombre 2
	 */
    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
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
	 * Gets the confirmar password.
	 *
	 * @return the confirmar password
	 */
    public String getConfirmar_password() {
        return confirmar_password;
    }

    /**
	 * Sets the confirmar password.
	 *
	 * @param confirmar_password the new confirmar password
	 */
    public void setConfirmar_password(String confirmar_password) {
        this.confirmar_password = confirmar_password;
    }

    /**
	 * Gets the selected roles.
	 *
	 * @return the selected roles
	 */
    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    /**
	 * Sets the selected roles.
	 *
	 * @param selectedRoles the new selected roles
	 */
    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    /**
	 * Gets the selected cartuchos.
	 *
	 * @return the selected cartuchos
	 */
    public String[] getSelectedCartuchos() {
        return selectedCartuchos;
    }

    /**
	 * Sets the selected cartuchos.
	 *
	 * @param selectedCartuchos the new selected cartuchos
	 */
    public void setSelectedCartuchos(String[] selectedCartuchos) {
        this.selectedCartuchos = selectedCartuchos;
    }

    /**
	 * Gets the cartuchos list.
	 *
	 * @return the cartuchos list
	 */
    public List<CartuchoForm> getCartuchosList() {
        return cartuchosList;
    }

    /**
	 * Sets the cartuchos list.
	 *
	 * @param cartuchosList the new cartuchos list
	 */
    public void setCartuchosList(List<CartuchoForm> cartuchosList) {
        this.cartuchosList = cartuchosList;
    }

    /**
	 * Gets the cuenta cliente.
	 *
	 * @return the cuenta cliente
	 */
    public String[] getCuenta_cliente() {
        return cuenta_cliente;
    }

    /**
	 * Sets the cuenta cliente.
	 *
	 * @param cuenta_cliente the new cuenta cliente
	 */
    public void setCuenta_cliente(String[] cuenta_cliente) {
        this.cuenta_cliente = cuenta_cliente;
    }

    /**
	 * Gets the cuenta cliente V.
	 *
	 * @return the cuenta cliente V
	 */
    public CargarCuentasUsuarioForm getCuenta_clienteV() {
        return cuenta_clienteV;
    }

    /**
	 * Sets the cuenta cliente V.
	 *
	 * @param cuenta_clienteV the new cuenta cliente V
	 */
    public void setCuenta_clienteV(CargarCuentasUsuarioForm cuenta_clienteV) {
        this.cuenta_clienteV = cuenta_clienteV;
    }

    /**
	 * Gets the observatorio.
	 *
	 * @return the observatorio
	 */
    public String[] getObservatorio() {
        return observatorio;
    }

    /**
	 * Sets the observatorio.
	 *
	 * @param observatorio the new observatorio
	 */
    public void setObservatorio(String[] observatorio) {
        this.observatorio = observatorio;
    }

    /**
	 * Gets the observatorio V.
	 *
	 * @return the observatorio V
	 */
    public CargarObservatorioForm getObservatorioV() {
        return observatorioV;
    }

    /**
	 * Sets the observatorio V.
	 *
	 * @param observatorioV the new observatorio V
	 */
    public void setObservatorioV(CargarObservatorioForm observatorioV) {
        this.observatorioV = observatorioV;
    }
}