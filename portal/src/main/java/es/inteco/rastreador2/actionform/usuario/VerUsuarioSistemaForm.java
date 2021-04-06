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
 * The Class VerUsuarioSistemaForm.
 */
public class VerUsuarioSistemaForm extends ActionForm {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The tipos. */
    //Para Ver Usuarios
    private List<Role> tipos;
    
    /** The nombre. */
    private String nombre;
    
    /** The usuario. */
    private String usuario;
    
    /** The apellidos. */
    private String apellidos;
    
    /** The departamento. */
    private String departamento;
    
    /** The email. */
    private String email;
    
    /** The id usuario. */
    private int id_usuario;
    
    /** The cartucho. */
    private List<CartuchoForm> cartucho;
    
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
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public List<CartuchoForm> getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(List<CartuchoForm> cartucho) {
        this.cartucho = cartucho;
    }

    /**
	 * Gets the id usuario.
	 *
	 * @return the id usuario
	 */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
	 * Sets the id usuario.
	 *
	 * @param id_usuario the new id usuario
	 */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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
	 * Gets the serial version UID.
	 *
	 * @return the serial version UID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
	 * Gets the tipos.
	 *
	 * @return the tipos
	 */
    public List<Role> getTipos() {
        return tipos;
    }

    /**
	 * Sets the tipos.
	 *
	 * @param tipos the new tipos
	 */
    public void setTipos(List<Role> tipos) {
        this.tipos = tipos;
    }
}
