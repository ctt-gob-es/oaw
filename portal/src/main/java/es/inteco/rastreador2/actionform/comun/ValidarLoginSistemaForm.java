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


import es.inteco.rastreador2.utils.CartuchoSeleccion;
import org.apache.struts.validator.ValidatorForm;

import java.util.List;


/**
 * The Class ValidarLoginSistemaForm.
 */
public class ValidarLoginSistemaForm extends ValidatorForm {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The login user. */
    private String loginUser;
    
    /** The login pass. */
    private String loginPass;
    
    /** The tipo. */
    private String tipo;
    
    /** The cartuchos. */
    private List<String> cartuchos;
    
    /** The aplicaciones. */
    private List<String> aplicaciones;
    
    /** The vcs. */
    private List<CartuchoSeleccion> vcs;
    
    /** The num cartuchos. */
    private int numCartuchos;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The proyecto. */
    private String proyecto;

    /**
	 * Gets the proyecto.
	 *
	 * @return the proyecto
	 */
    public String getProyecto() {
        return proyecto;
    }

    /**
	 * Sets the proyecto.
	 *
	 * @param proyecto the new proyecto
	 */
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
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
	 * Gets the num cartuchos.
	 *
	 * @return the num cartuchos
	 */
    public int getNumCartuchos() {
        return numCartuchos;
    }

    /**
	 * Sets the num cartuchos.
	 *
	 * @param numCartuchos the new num cartuchos
	 */
    public void setNumCartuchos(int numCartuchos) {
        this.numCartuchos = numCartuchos;
    }

    /**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
    public String getTipo() {
        return tipo;
    }

    /**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
	 * Gets the login user.
	 *
	 * @return the login user
	 */
    public String getLoginUser() {
        return loginUser;
    }

    /**
	 * Sets the login user.
	 *
	 * @param loginUser the new login user
	 */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
	 * Gets the login pass.
	 *
	 * @return the login pass
	 */
    public String getLoginPass() {
        return loginPass;
    }

    /**
	 * Sets the login pass.
	 *
	 * @param loginPass the new login pass
	 */
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
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
	 * Gets the aplicaciones.
	 *
	 * @return the aplicaciones
	 */
    public List<String> getAplicaciones() {
        return aplicaciones;
    }

    /**
	 * Sets the aplicaciones.
	 *
	 * @param aplicaciones the new aplicaciones
	 */
    public void setAplicaciones(List<String> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    /**
	 * Gets the vcs.
	 *
	 * @return the vcs
	 */
    public List<CartuchoSeleccion> getVcs() {
        return vcs;
    }

    /**
	 * Sets the vcs.
	 *
	 * @param vcs the new vcs
	 */
    public void setVcs(List<CartuchoSeleccion> vcs) {
        this.vcs = vcs;
    }


}