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
        return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
        // Imprime el elemento de cierre.
        JspWriter writer = pageContext.getOut();
        try {
            writer.print("");
        } catch (IOException e) {
            throw new JspException(e);
        }

        return (EVAL_PAGE);
    }

}
