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

public class ObservatorySubgroupForm implements Serializable {
    private String description;
    private String guidelineId;
    private String aspect;
    private List<Integer> failChecks;
    private List<Integer> onlyWarningChecks;
    private List<Integer> ignoreRelatedChecks;
    private List<ProblemForm> problems;
    private int value;

    public ObservatorySubgroupForm() {
        this.failChecks = new ArrayList<>();
        this.onlyWarningChecks = new ArrayList<>();
        this.problems = new ArrayList<>();
        this.ignoreRelatedChecks = new ArrayList<>();
    }

    public List<Integer> getIgnoreRelatedChecks() {
        return ignoreRelatedChecks;
    }

    public void setIgnoreRelatedChecks(List<Integer> ignoreRelatedChecks) {
        this.ignoreRelatedChecks = ignoreRelatedChecks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(String guidelineId) {
        this.guidelineId = guidelineId;
    }

    public List<Integer> getFailChecks() {
        return failChecks;
    }

    public void setFailChecks(List<Integer> failChecks) {
        this.failChecks = failChecks;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public List<ProblemForm> getProblems() {
        if (problems == null) {
            problems = new ArrayList<>();
        }
        return problems;
    }

    public void setProblems(List<ProblemForm> problems) {
        this.problems = problems;
    }

    public List<Integer> getOnlyWarningChecks() {
        return onlyWarningChecks;
    }

    public void setOnlyWarningChecks(List<Integer> onlyWarningChecks) {
        this.onlyWarningChecks = onlyWarningChecks;
    }
}
