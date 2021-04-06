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
package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * The Class WritePropertiesTag.
 */
public class WritePropertiesTag extends TagSupport {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7851110311230238272L;
    
    /** The file. */
    private String file = null;
    
    /** The key. */
    private String key = null;


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
	 * Gets the key.
	 *
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * Do start tag.
	 *
	 * @return the int
	 * @throws JspException the jsp exception
	 */
    public int doStartTag() throws JspException {

        try {
            //Se toma el string de salida
            JspWriter writer = pageContext.getOut();

            PropertiesManager pmgr = new PropertiesManager();
            String message = pmgr.getValue(this.file, this.key);

            writer.print(message);

        } catch (IOException e) {
            Logger.putLog("Tag Exception", WritePropertiesTag.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return SKIP_BODY;
    }

    /**
	 * Do end tag.
	 *
	 * @return the int
	 * @throws JspException the jsp exception
	 */
    public int doEndTag() throws JspException {
        // Imprime el elemento de cierre.
        JspWriter writer = pageContext.getOut();
        try {
            writer.print("");
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}
