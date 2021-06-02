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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * The Class TrunpTag.
 */
public class TrunpTag extends TagSupport {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7851110311230238272L;
    
    /** The cad. */
    private String cad;

    /**
	 * Gets the cad.
	 *
	 * @return the cad
	 */
    public String getCad() {
        return cad;
    }

    /**
	 * Sets the cad.
	 *
	 * @param cad the new cad
	 */
    public void setCad(String cad) {
        this.cad = cad;
    }

    /**
	 * Do start tag.
	 *
	 * @return the int
	 * @throws JspException the jsp exception
	 */
    public int doStartTag() throws JspException {
        try {
            // Se toma el stream de salida
            JspWriter writer = pageContext.getOut();
            // Si nos han pasado al atributo action con valor 'check' o vacio
            // comprobamos que la sesion esta activa y la pagina del usuario es valida
            String cadenavalor = (String) pageContext.getAttribute(cad);
            if (cadenavalor != null) {
                if (cadenavalor.lastIndexOf('.') != -1 && cadenavalor.length() != cadenavalor.lastIndexOf('.') - 1) {
                    cadenavalor = cadenavalor.substring(cadenavalor.lastIndexOf('.') + 1, cadenavalor.length());
                }
                writer.print(cadenavalor);
            }
        } catch (Exception e) {
            Logger.putLog("TrunpTag Exception", TrunpTag.class, Logger.LOG_LEVEL_ERROR, e);
        }
        // Evalua el body de la
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
            Logger.putLog("TrunpTag Exception", TrunpTag.class, Logger.LOG_LEVEL_ERROR, e);
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}
