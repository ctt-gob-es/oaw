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
 * The Class ProblemForm.
 */
public class ProblemForm implements Serializable {
    
    /** The type. */
    private String type;
    
    /** The check. */
    private String check;
    
    /** The code. */
    private String code;
    
    /** The group. */
    private String group;
    
    /** The description. */
    private String description;
    
    /** The rationale. */
    private String rationale;
    
    /** The error. */
    private String error;
    
    /** The note. */
    private String note;
    
    /** The specific problems. */
    private List<SpecificProblemForm> specificProblems;

    /**
	 * Instantiates a new problem form.
	 */
    public ProblemForm() {
        specificProblems = new ArrayList<>();
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

    /**
	 * Gets the check.
	 *
	 * @return the check
	 */
    public String getCheck() {
        return check;
    }

    /**
	 * Sets the check.
	 *
	 * @param check the new check
	 */
    public void setCheck(String check) {
        this.check = check;
    }

    /**
	 * Gets the code.
	 *
	 * @return the code
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * Gets the group.
	 *
	 * @return the group
	 */
    public String getGroup() {
        return group;
    }

    /**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
	 * Gets the description.
	 *
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Gets the rationale.
	 *
	 * @return the rationale
	 */
    public String getRationale() {
        return rationale;
    }

    /**
	 * Sets the rationale.
	 *
	 * @param rationale the new rationale
	 */
    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    /**
	 * Gets the error.
	 *
	 * @return the error
	 */
    public String getError() {
        return error;
    }

    /**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
    public void setError(String error) {
        this.error = error;
    }

    /**
	 * Gets the specific problems.
	 *
	 * @return the specific problems
	 */
    public List<SpecificProblemForm> getSpecificProblems() {
        return specificProblems;
    }

    /**
	 * Sets the specific problems.
	 *
	 * @param specificProblems the new specific problems
	 */
    public void setSpecificProblems(List<SpecificProblemForm> specificProblems) {
        this.specificProblems = specificProblems;
    }

    /**
	 * Gets the note.
	 *
	 * @return the note
	 */
    public String getNote() {
        return note;
    }

    /**
	 * Sets the note.
	 *
	 * @param note the new note
	 */
    public void setNote(String note) {
        this.note = note;
    }
}
