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
 * The Class ModificarUsuarioSistemaForm.
 */
public class ModificarUsuarioSistemaForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre. */
    private String nombre;
    
    /** The password. */
    private String password;
    
    /** The nombre antiguo. */
    private String nombre_antiguo;
    
    /** The selected roles. */
    private String[] selectedRoles;
    
    /** The nombre 2. */
    private String nombre2;
    
    /** The apellidos. */
    private String apellidos;
    
    /** The departamento. */
    private String departamento;
    
    /** The email. */
    private String email;
    
    /** The cartucho. */
    private List<String> cartucho;
    
    /** The selected cartuchos. */
    private String[] selectedCartuchos;
    
    /** The selected cuenta cliente. */
    private String[] selectedCuentaCliente;
    
    /** The selected observatorio. */
    private String[] selectedObservatorio;
    
    /** The id usuario. */
    private String idUsuario;

    /** The cartuchos. */
    private List<String> cartuchos;
    
    /** The roles. */
    private List<Role> roles;
    
    /** The role type. */
    private String roleType;
    
    /** The cartuchos list. */
    private List<CartuchoForm> cartuchosList;
    
    /** The usuario. */
    private String usuario;
    
    /** The cuenta cliente. */
    private List<String> cuentaCliente;
    
    /** The cuenta cliente V. */
    private CargarCuentasUsuarioForm cuenta_clienteV;
    
    /** The observatorio. */
    private List<String> observatorio;
    
    /** The observatorio V. */
    private CargarObservatorioForm observatorioV;

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
	 * Gets the cuenta cliente.
	 *
	 * @return the cuenta cliente
	 */
    public List<String> getCuentaCliente() {
        return cuentaCliente;
    }

    /**
	 * Sets the cuenta cliente.
	 *
	 * @param cuentaCliente the new cuenta cliente
	 */
    public void setCuentaCliente(List<String> cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

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
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public List<String> getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(List<String> cartucho) {
        this.cartucho = cartucho;
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
	 * Gets the nombre antiguo.
	 *
	 * @return the nombre antiguo
	 */
    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    /**
	 * Sets the nombre antiguo.
	 *
	 * @param nombre_antiguo the new nombre antiguo
	 */
    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
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
	 * Gets the selected cuenta cliente.
	 *
	 * @return the selected cuenta cliente
	 */
    public String[] getSelectedCuentaCliente() {
        return selectedCuentaCliente;
    }

    /**
	 * Sets the selected cuenta cliente.
	 *
	 * @param selectedCuentaCliente the new selected cuenta cliente
	 */
    public void setSelectedCuentaCliente(String[] selectedCuentaCliente) {
        this.selectedCuentaCliente = selectedCuentaCliente;
    }

    /**
	 * Gets the observatorio.
	 *
	 * @return the observatorio
	 */
    public List<String> getObservatorio() {
        return observatorio;
    }

    /**
	 * Sets the observatorio.
	 *
	 * @param observatorio the new observatorio
	 */
    public void setObservatorio(List<String> observatorio) {
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

    /**
	 * Gets the selected observatorio.
	 *
	 * @return the selected observatorio
	 */
    public String[] getSelectedObservatorio() {
        return selectedObservatorio;
    }

    /**
	 * Sets the selected observatorio.
	 *
	 * @param selectedObservatorio the new selected observatorio
	 */
    public void setSelectedObservatorio(String[] selectedObservatorio) {
        this.selectedObservatorio = selectedObservatorio;
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