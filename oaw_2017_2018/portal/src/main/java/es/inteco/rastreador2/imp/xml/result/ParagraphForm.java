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
package es.inteco.rastreador2.imp.xml.result;

import es.inteco.common.Constants;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;

import java.util.Map;

public class ParagraphForm {

    private String paragraph;
    private Map<Integer, SpecialChunk> specialChunks;
    private String type;

    public ParagraphForm() {
        type = Constants.OBJECT_TYPE_PARAGRAPH;
    }

    public Map<Integer, SpecialChunk> getSpecialChunks() {
        return specialChunks;
    }

    public void setSpecialChunks(Map<Integer, SpecialChunk> specialChunks) {
        this.specialChunks = specialChunks;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
