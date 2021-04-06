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

/**
 * The Class AnalysisForm.
 */
public class AnalysisForm {
    
    /** The code. */
    private String code;
    
    /** The date. */
    private String date;
    
    /** The url. */
    private String url;
    
    /** The url title. */
    private String urlTitle;
    
    /** The time. */
    private String time;
    
    /** The file. */
    private String file;
    
    /** The entity. */
    private String entity;
    
    /** The entity title. */
    private String entityTitle;
    
    /** The tracker. */
    private String tracker;
    
    /** The guideline. */
    private String guideline;
    
    /** The problems. */
    private int problems;
    
    /** The warnings. */
    private int warnings;
    
    /** The observations. */
    private int observations;
    
    /** The status. */
    private int status;


    /**
	 * Gets the problems.
	 *
	 * @return the problems
	 */
    public int getProblems() {
        return problems;
    }

    /**
	 * Gets the url title.
	 *
	 * @return the url title
	 */
    public String getUrlTitle() {
        return urlTitle;
    }

    /**
	 * Sets the url title.
	 *
	 * @param urlTitle the new url title
	 */
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    /**
	 * Sets the problems.
	 *
	 * @param problems the new problems
	 */
    public void setProblems(int problems) {
        this.problems = problems;
    }

    /**
	 * Gets the warnings.
	 *
	 * @return the warnings
	 */
    public int getWarnings() {
        return warnings;
    }

    /**
	 * Sets the warnings.
	 *
	 * @param warnings the new warnings
	 */
    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    /**
	 * Gets the observations.
	 *
	 * @return the observations
	 */
    public int getObservations() {
        return observations;
    }

    /**
	 * Sets the observations.
	 *
	 * @param observations the new observations
	 */
    public void setObservations(int observations) {
        this.observations = observations;
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
	 * Gets the date.
	 *
	 * @return the date
	 */
    public String getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(String date) {
        this.date = date;
    }

    /**
	 * Gets the url.
	 *
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * Gets the time.
	 *
	 * @return the time
	 */
    public String getTime() {
        return time;
    }

    /**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
    public void setTime(String time) {
        this.time = time;
    }

    /**
	 * Gets the file.
	 *
	 * @return the file
	 */
    public String getFile() {
        return file;
    }

    /**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
    public void setFile(String file) {
        this.file = file;
    }

    /**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
    public String getEntity() {
        return entity;
    }

    /**
	 * Sets the entity.
	 *
	 * @param entity the new entity
	 */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
	 * Gets the tracker.
	 *
	 * @return the tracker
	 */
    public String getTracker() {
        return tracker;
    }

    /**
	 * Sets the tracker.
	 *
	 * @param tracker the new tracker
	 */
    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    /**
	 * Gets the guideline.
	 *
	 * @return the guideline
	 */
    public String getGuideline() {
        return guideline;
    }

    /**
	 * Sets the guideline.
	 *
	 * @param guideline the new guideline
	 */
    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    /**
	 * Gets the status.
	 *
	 * @return the status
	 */
    public int getStatus() {
        return status;
    }

    /**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
	 * Gets the entity title.
	 *
	 * @return the entity title
	 */
    public String getEntityTitle() {
        return entityTitle;
    }

    /**
	 * Sets the entity title.
	 *
	 * @param entityTitle the new entity title
	 */
    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }
}
