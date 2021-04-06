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
package es.inteco.rastreador2.actionform.rastreo;


import es.inteco.rastreador2.utils.Rastreo;
import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class CargarRastreosForm.
 */
public class CargarRastreosForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The rastreos. */
    private List<String> rastreos;
    
    /** The user. */
    private String user;
    
    /** The pass. */
    private String pass;
    
    /** The vr. */
    private List<Rastreo> vr;
    
    /** The num rastreos. */
    private int num_rastreos;
    
    /** The maxrastreos. */
    private int maxrastreos;
    
    /** The cartucho. */
    private String cartucho;

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
	 * Gets the maxrastreos.
	 *
	 * @return the maxrastreos
	 */
    public int getMaxrastreos() {
        return maxrastreos;
    }

    /**
	 * Sets the maxrastreos.
	 *
	 * @param maxrastreos the new maxrastreos
	 */
    public void setMaxrastreos(int maxrastreos) {
        this.maxrastreos = maxrastreos;
    }

    /**
	 * Gets the num rastreos.
	 *
	 * @return the num rastreos
	 */
    public int getNum_rastreos() {
        return num_rastreos;
    }

    /**
	 * Sets the num rastreos.
	 *
	 * @param num_rastreos the new num rastreos
	 */
    public void setNum_rastreos(int num_rastreos) {
        this.num_rastreos = num_rastreos;
    }

    /**
	 * Gets the vr.
	 *
	 * @return the vr
	 */
    public List<Rastreo> getVr() {
        return vr;
    }

    /**
	 * Sets the vr.
	 *
	 * @param vr the new vr
	 */
    public void setVr(List<Rastreo> vr) {
        this.vr = vr;
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
	 * Gets the rastreos.
	 *
	 * @return the rastreos
	 */
    public List<String> getRastreos() {
        return rastreos;
    }

    /**
	 * Sets the rastreos.
	 *
	 * @param rastreos the new rastreos
	 */
    public void setRastreos(List<String> rastreos) {
        this.rastreos = rastreos;
    }


}