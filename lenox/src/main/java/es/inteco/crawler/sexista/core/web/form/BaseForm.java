package es.inteco.crawler.sexista.core.web.form;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Form vacío del que heredarán el resto de formuarios.
 *
 * @author GPM
 * @version 1.0
 */
public abstract class BaseForm extends ValidatorActionForm {

    /**
     * Logger.
     */
    private Logger log = Logger.getRootLogger();

    /**
     * Nombre del método a usar en el Action Dispatch (Ya sea de
     * Paginación/Búsqueda o propio).
     */
    private String method;

    /**
     * Indica cual es el método a ejecutar en el validate.
     */
    private String methodValidate = null;

    /**
     * Constructor por defecto.
     */
    public BaseForm() {
        super();
    }

    /**
     * Método validate. Si no se require el uso del DispatchForm, sobreescribir
     * este método en el Form concreto. Este método se ejecutará siempre que los
     * formularios tengan que ser validados. Este método decidirá a qué método
     * tiene que llamar para llevar a cabo la validación en los ActionForms que
     * heredan de él.
     *
     * @param mapping de tipo <b>ActionMapping</b>
     * @param request de tipo <b>HttpServletRequest</b>
     * @return <b>ActionErrors</b>
     */
    @SuppressWarnings("unchecked")
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = null;

        Class[] tiposParametros = {ActionMapping.class, HttpServletRequest.class};
        try {
            Method metodo = getClass().getMethod(this.methodValidate, tiposParametros);
            Object[] parametros = {mapping, request};

            errors = (ActionErrors) metodo.invoke(this, parametros);
        } catch (SecurityException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return errors;
    }

    // Getter / Setter

    /**
     * Getter.
     *
     * @return Logger
     */
    public Logger getLog() {
        return log;
    }

    /**
     * Setter.
     *
     * @param log the log
     */
    public void setLog(Logger log) {
        this.log = log;
    }

    /**
     * Getter.
     *
     * @return String
     */
    public String getMethodValidate() {
        return methodValidate;
    }

    /**
     * Setter.
     *
     * @param methodValidate the methodValidate
     */
    public void setMethodValidate(String methodValidate) {
        this.methodValidate = methodValidate;
    }

    /**
     * Getter.
     *
     * @return String
     */
    public String getMethod() {
        return method;
    }

    /**
     * Setter.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }
}
