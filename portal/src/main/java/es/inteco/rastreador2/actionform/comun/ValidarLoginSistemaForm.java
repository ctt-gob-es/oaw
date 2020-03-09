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


public class ValidarLoginSistemaForm extends ValidatorForm {
    private static final long serialVersionUID = 1L;
    private String loginUser;
    private String loginPass;
    private String tipo;
    private List<String> cartuchos;
    private List<String> aplicaciones;
    private List<CartuchoSeleccion> vcs;
    private int numCartuchos;
    private String cartucho;
    private String proyecto;

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public int getNumCartuchos() {
        return numCartuchos;
    }

    public void setNumCartuchos(int numCartuchos) {
        this.numCartuchos = numCartuchos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public List<String> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public List<String> getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(List<String> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    public List<CartuchoSeleccion> getVcs() {
        return vcs;
    }

    public void setVcs(List<CartuchoSeleccion> vcs) {
        this.vcs = vcs;
    }


}