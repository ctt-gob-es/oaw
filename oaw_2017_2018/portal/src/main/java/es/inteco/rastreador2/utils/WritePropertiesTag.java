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

public class WritePropertiesTag extends TagSupport {

    private static final long serialVersionUID = 7851110311230238272L;
    private String file = null;
    private String key = null;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
