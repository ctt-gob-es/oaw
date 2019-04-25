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
package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.PautaForm;

import java.util.ArrayList;
import java.util.List;

public class ISPDFPautaForm {

    public PautaForm pauta;
    public List<ISPDFProblemForm> problemList;

    public ISPDFPautaForm() {
        pauta = new PautaForm();
        problemList = new ArrayList<>();
    }

    public PautaForm getPauta() {
        return pauta;
    }

    public void setPauta(PautaForm pauta) {
        this.pauta = pauta;
    }

    public List<ISPDFProblemForm> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<ISPDFProblemForm> problemList) {
        this.problemList = problemList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISPDFPautaForm) {
            ISPDFPautaForm form = (ISPDFPautaForm) obj;
            if (form.getPauta().getName().equals(pauta.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return pauta != null ? pauta.getName().hashCode() : 0;
    }
}
