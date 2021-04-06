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

/**
 * The Class SpecificProblemForm.
 */
public class SpecificProblemForm implements Serializable {
    
    /** The line. */
    private String line;
    
    /** The column. */
    private String column;
    
    /** The message. */
    private String message;
    
    /** The note. */
    private List<String> note;
    
    /** The code. */
    private List<String> code;

    /**
	 * Instantiates a new specific problem form.
	 */
    public SpecificProblemForm() {
        note = new ArrayList<>();
        code = new ArrayList<>();
    }

    /**
	 * Gets the line.
	 *
	 * @return the line
	 */
    public String getLine() {
        return line;
    }

    /**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
    public void setLine(String line) {
        this.line = line;
    }

    /**
	 * Gets the column.
	 *
	 * @return the column
	 */
    public String getColumn() {
        return column;
    }

    /**
	 * Sets the column.
	 *
	 * @param column the new column
	 */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
	 * Gets the code.
	 *
	 * @return the code
	 */
    public List<String> getCode() {
        return code;
    }

    /**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
    public void setCode(List<String> code) {
        this.code = code;
    }

    /**
	 * Gets the message.
	 *
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * Gets the note.
	 *
	 * @return the note
	 */
    public List<String> getNote() {
        return note;
    }

    /**
	 * Sets the note.
	 *
	 * @param note the new note
	 */
    public void setNote(List<String> note) {
        this.note = note;
    }
}
