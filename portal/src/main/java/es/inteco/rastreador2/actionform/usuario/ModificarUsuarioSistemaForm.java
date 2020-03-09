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


public class ModificarUsuarioSistemaForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private String password;
    private String nombre_antiguo;
    private String[] selectedRoles;
    private String nombre2;
    private String apellidos;
    private String departamento;
    private String email;
    private List<String> cartucho;
    private String[] selectedCartuchos;
    private String[] selectedCuentaCliente;
    private String[] selectedObservatorio;
    private String idUsuario;

    private List<String> cartuchos;
    private List<Role> roles;
    private String roleType;
    private List<CartuchoForm> cartuchosList;
    private String usuario;
    private List<String> cuentaCliente;
    private CargarCuentasUsuarioForm cuenta_clienteV;
    private List<String> observatorio;
    private CargarObservatorioForm observatorioV;

    public CargarCuentasUsuarioForm getCuenta_clienteV() {
        return cuenta_clienteV;
    }

    public void setCuenta_clienteV(CargarCuentasUsuarioForm cuenta_clienteV) {
        this.cuenta_clienteV = cuenta_clienteV;
    }

    public List<String> getCuentaCliente() {
        return cuentaCliente;
    }

    public void setCuentaCliente(List<String> cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    public List<String> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public List<CartuchoForm> getCartuchosList() {
        return cartuchosList;
    }

    public void setCartuchosList(List<CartuchoForm> cartuchosList) {
        this.cartuchosList = cartuchosList;
    }

    public List<String> getCartucho() {
        return cartucho;
    }

    public void setCartucho(List<String> cartucho) {
        this.cartucho = cartucho;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String[] getSelectedCartuchos() {
        return selectedCartuchos;
    }

    public void setSelectedCartuchos(String[] selectedCartuchos) {
        this.selectedCartuchos = selectedCartuchos;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String[] getSelectedCuentaCliente() {
        return selectedCuentaCliente;
    }

    public void setSelectedCuentaCliente(String[] selectedCuentaCliente) {
        this.selectedCuentaCliente = selectedCuentaCliente;
    }

    public List<String> getObservatorio() {
        return observatorio;
    }

    public void setObservatorio(List<String> observatorio) {
        this.observatorio = observatorio;
    }

    public CargarObservatorioForm getObservatorioV() {
        return observatorioV;
    }

    public void setObservatorioV(CargarObservatorioForm observatorioV) {
        this.observatorioV = observatorioV;
    }

    public String[] getSelectedObservatorio() {
        return selectedObservatorio;
    }

    public void setSelectedObservatorio(String[] selectedObservatorio) {
        this.selectedObservatorio = selectedObservatorio;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}