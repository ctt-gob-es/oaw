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


public class EliminarUsuarioSistemaForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long id;
    private List<Role> roles;
    private List<CartuchoForm> cartuchos;
    private String nombre;
    private String usuario, user;
    private String apellidos;
    private String departamento;
    private String email;
    private int id_cartucho;
    private List<String> nombreCuenta;


    public List<String> getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(List<String> nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public List<CartuchoForm> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<CartuchoForm> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}