package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class TrunpTag extends TagSupport {

    private static final long serialVersionUID = 7851110311230238272L;
    private String cad;

    public String getCad() {
        return cad;
    }

    public void setCad(String cad) {
        this.cad = cad;
    }

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
        } catch (IOException e) {
            Logger.putLog("TrunpTag Exception", TrunpTag.class, Logger.LOG_LEVEL_ERROR, e);
        } catch (Exception e) {
            Logger.putLog("TrunpTag Exception", TrunpTag.class, Logger.LOG_LEVEL_ERROR, e);
        }
        // Evalua el body de la
        return SKIP_BODY;
    }

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
