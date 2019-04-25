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


public class VerUsuarioSistemaForm extends ActionForm {


    private static final long serialVersionUID = 1L;
    //Para Ver Usuarios
    private List<Role> tipos;
    private String nombre;
    private String usuario;
    private String apellidos;
    private String departamento;
    private String email;
    private int id_usuario;
    private List<CartuchoForm> cartucho;
    private List<String> nombreCuenta;


    public List<String> getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(List<String> nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public List<CartuchoForm> getCartucho() {
        return cartucho;
    }

    public void setCartucho(List<CartuchoForm> cartucho) {
        this.cartucho = cartucho;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<Role> getTipos() {
        return tipos;
    }

    public void setTipos(List<Role> tipos) {
        this.tipos = tipos;
    }
}
