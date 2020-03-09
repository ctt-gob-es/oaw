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


public class CargarRastreosForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<String> rastreos;
    private String user;
    private String pass;
    private List<Rastreo> vr;
    private int num_rastreos;
    private int maxrastreos;
    private String cartucho;

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public int getMaxrastreos() {
        return maxrastreos;
    }

    public void setMaxrastreos(int maxrastreos) {
        this.maxrastreos = maxrastreos;
    }

    public int getNum_rastreos() {
        return num_rastreos;
    }

    public void setNum_rastreos(int num_rastreos) {
        this.num_rastreos = num_rastreos;
    }

    public List<Rastreo> getVr() {
        return vr;
    }

    public void setVr(List<Rastreo> vr) {
        this.vr = vr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRastreos() {
        return rastreos;
    }

    public void setRastreos(List<String> rastreos) {
        this.rastreos = rastreos;
    }


}