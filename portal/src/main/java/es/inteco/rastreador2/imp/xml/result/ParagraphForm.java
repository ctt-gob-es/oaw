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

/**
 * The Class ParagraphForm.
 */
public class ParagraphForm {

    /** The paragraph. */
    private String paragraph;
    
    /** The special chunks. */
    private Map<Integer, SpecialChunk> specialChunks;
    
    /** The type. */
    private String type;

    /**
	 * Instantiates a new paragraph form.
	 */
    public ParagraphForm() {
        type = Constants.OBJECT_TYPE_PARAGRAPH;
    }

    /**
	 * Gets the special chunks.
	 *
	 * @return the special chunks
	 */
    public Map<Integer, SpecialChunk> getSpecialChunks() {
        return specialChunks;
    }

    /**
	 * Sets the special chunks.
	 *
	 * @param specialChunks the special chunks
	 */
    public void setSpecialChunks(Map<Integer, SpecialChunk> specialChunks) {
        this.specialChunks = specialChunks;
    }

    /**
	 * Gets the paragraph.
	 *
	 * @return the paragraph
	 */
    public String getParagraph() {
        return paragraph;
    }

    /**
	 * Sets the paragraph.
	 *
	 * @param paragraph the new paragraph
	 */
    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    /**
	 * Gets the type.
	 *
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    public void setType(String type) {
        this.type = type;
    }
}
