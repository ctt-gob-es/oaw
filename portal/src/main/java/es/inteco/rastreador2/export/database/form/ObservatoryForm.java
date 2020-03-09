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
package es.inteco.rastreador2.export.database.form;

import java.util.List;

public class ObservatoryForm {
    private String idExecution;
    private String name;
    private String date;
    private String numAA;
    private String numA;
    private String numNV;
    private List<CategoryForm> categoryFormList;

    public String getIdExecution() {
        return idExecution;
    }

    public void setIdExecution(String idExecution) {
        this.idExecution = idExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumAA() {
        return numAA;
    }

    public void setNumAA(String numAA) {
        this.numAA = numAA;
    }

    public String getNumA() {
        return numA;
    }

    public void setNumA(String numA) {
        this.numA = numA;
    }

    public String getNumNV() {
        return numNV;
    }

    public void setNumNV(String numNV) {
        this.numNV = numNV;
    }

    public List<CategoryForm> getCategoryFormList() {
        return categoryFormList;
    }

    public void setCategoryFormList(List<CategoryForm> categoryFormList) {
        this.categoryFormList = categoryFormList;
    }

}
