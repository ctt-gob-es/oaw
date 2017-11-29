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
package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.action.ActionForm;

public class UpdateListDataForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String listaRastreable;
    private long idListaRastreable;
    private long idRastreableAntiguo;

    private String listaNoRastreable;
    private long idListaNoRastreable;
    private long idNoRastreableAntiguo;

    private String nombre;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getListaRastreable() {
        return listaRastreable;
    }

    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    public long getIdListaRastreable() {
        return idListaRastreable;
    }

    public void setIdListaRastreable(long idListaRastreable) {
        this.idListaRastreable = idListaRastreable;
    }

    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    public long getIdListaNoRastreable() {
        return idListaNoRastreable;
    }

    public void setIdListaNoRastreable(long idListaNoRastreable) {
        this.idListaNoRastreable = idListaNoRastreable;
    }

    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }


}