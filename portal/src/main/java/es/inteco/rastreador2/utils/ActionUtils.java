package es.inteco.rastreador2.utils;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;

public final class ActionUtils {

    private ActionUtils() {
    }

    /**
     * Hay que redireccionar a la pantalla de login porque el usuario no está logado debidamente
     *
     * @param request
     * @return
     */
    public static boolean redirectToLogin(HttpServletRequest request) {

        if (!Sesiones.comprobarSession(request.getSession())) {
            ActionErrors error = new ActionErrors();
            error.add("errorPassObligatorios", new ActionMessage("error.NoSesion"));
            addErrors(request, error);
            return true;
        }

        return false;
    }

    /**
     * Copia del método addErrors de la clase Action de Struts
     *
     * @param request
     * @param errors
     */
    private static void addErrors(HttpServletRequest request, ActionErrors errors) {
        if (errors == null) {
            return;
        }

        ActionMessages requestErrors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (requestErrors == null) {
            requestErrors = new ActionMessages();
        }

        requestErrors.add(errors);

        if (requestErrors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        request.setAttribute(Globals.ERROR_KEY, requestErrors);
    }

}