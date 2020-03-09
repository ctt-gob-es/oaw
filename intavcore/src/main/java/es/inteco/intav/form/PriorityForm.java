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
package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriorityForm implements Serializable {
    private int numProblems;
    private int numWarnings;
    private int numInfos;
    private String priorityName;
    private List<GuidelineForm> guidelines;

    public PriorityForm() {
        guidelines = new ArrayList<>();
    }

    public int getNumProblems() {
        return numProblems;
    }

    public void setNumProblems(int numProblems) {
        this.numProblems = numProblems;
    }

    public int getNumWarnings() {
        return numWarnings;
    }

    public void setNumWarnings(int numWarnings) {
        this.numWarnings = numWarnings;
    }

    public int getNumInfos() {
        return numInfos;
    }

    public void setNumInfos(int numInfos) {
        this.numInfos = numInfos;
    }

    public List<GuidelineForm> getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(List<GuidelineForm> guidelines) {
        this.guidelines = guidelines;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
}
