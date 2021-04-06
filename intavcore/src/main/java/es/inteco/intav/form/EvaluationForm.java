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
 * The Class EvaluationForm.
 */
public class EvaluationForm implements Serializable {
    
    /** The entity. */
    private String entity;
    
    /** The url. */
    private String url;
    
    /** The guideline. */
    private String guideline;
    
    /** The source. */
    private String source;

    /** The priorities. */
    private List<PriorityForm> priorities;

    /**
	 * Instantiates a new evaluation form.
	 */
    public EvaluationForm() {
        priorities = new ArrayList<>();
    }

    /**
	 * Gets the source.
	 *
	 * @return the source
	 */
    public String getSource() {
        return source;
    }

    /**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
    public void setSource(String source) {
        this.source = source;
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
	 * Gets the priorities.
	 *
	 * @return the priorities
	 */
    public List<PriorityForm> getPriorities() {
        return priorities;
    }

    /**
	 * Sets the priorities.
	 *
	 * @param priorities the new priorities
	 */
    public void setPriorities(List<PriorityForm> priorities) {
        this.priorities = priorities;
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
}
