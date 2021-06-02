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
package es.inteco.common;

/**
 * The Class CssValidationError.
 */
public class CssValidationError {
    
    /** The uri. */
    private String uri;
    
    /** The line. */
    private int line;
    
    /** The message. */
    private String message;
    
    /** The skipped string. */
    private String skippedString;
    
    /** The context. */
    private String context;
    
    /** The code. */
    private String code;
    
    /** The summary. */
    private boolean summary;

    /**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
    public String getUri() {
        return uri;
    }

    /**
	 * Sets the uri.
	 *
	 * @param uri the new uri
	 */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
	 * Gets the line.
	 *
	 * @return the line
	 */
    public int getLine() {
        return line;
    }

    /**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
    public void setLine(int line) {
        this.line = line;
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
	 * Gets the skipped string.
	 *
	 * @return the skipped string
	 */
    public String getSkippedString() {
        return skippedString;
    }

    /**
	 * Sets the skipped string.
	 *
	 * @param skippedString the new skipped string
	 */
    public void setSkippedString(String skippedString) {
        this.skippedString = skippedString;
    }

    /**
	 * Gets the context.
	 *
	 * @return the context
	 */
    public String getContext() {
        return context;
    }

    /**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
    public void setContext(String context) {
        this.context = context;
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
	 * Checks if is summary.
	 *
	 * @return true, if is summary
	 */
    public boolean isSummary() {
        return summary;
    }

    /**
	 * Sets the summary.
	 *
	 * @param summary the new summary
	 */
    public void setSummary(boolean summary) {
        this.summary = summary;
    }
}
