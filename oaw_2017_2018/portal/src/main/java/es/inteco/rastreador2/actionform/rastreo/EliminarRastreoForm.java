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


import org.apache.struts.action.ActionForm;


public class EliminarRastreoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String codigo, fecha, cartucho, idrastreo, normaAnalisis;


    public String getIdrastreo() {
        return idrastreo;
    }

    public void setIdrastreo(String idrastreo) {
        this.idrastreo = idrastreo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

}