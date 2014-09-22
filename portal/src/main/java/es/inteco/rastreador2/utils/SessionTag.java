package es.inteco.rastreador2.utils;


import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


public class SessionTag extends TagSupport {

    private static final long serialVersionUID = 7851110311230238272L;
    private String action = null;


    /**
     * ************ METODOS JAVABEAN ***********************
     */

    public String getAction() {
        return (this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    private boolean checkSessionAdmin() {
        PropertiesManager pmgr = new PropertiesManager();
        HttpSession sesion = null;
        sesion = pageContext.getSession();
        //comprobamos si estamos en session
        if (!Sesiones.comprobarSession(sesion)) {
            return false;
        }
        //cojemos el attributo "role" de sesion
        ArrayList<String> role = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
        if (role != null && role.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id"))) {
            //se trata de un usuario con role admin, tendra acceso a modicar y ver
            return true;
        }
        //no tiene rol admin
        return false;
    }

    private boolean checkpermissionAdminConfig() {
        PropertiesManager pmgr = new PropertiesManager();
        HttpSession sesion = null;
        sesion = pageContext.getSession();

        if (!Sesiones.comprobarSession(sesion)) {
            return false;
        }
        //cojemos el attributo "role" de sesion
        ArrayList<String> role = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
        if (role != null && (role.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")) ||
                role.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.configurator.id")))) {
            //se trata de un usuario con role admin y config, tendra acceso a lo que este dentro del tag
            return true;
        }
        //no tiene rol admin
        return false;
    }


    private boolean checkSessionVisor() {
        PropertiesManager pmgr = new PropertiesManager();
        HttpSession sesion = null;
        sesion = pageContext.getSession();

        if (!Sesiones.comprobarSession(sesion)) {
            return false;
        }
        //cojemos el attributo "role" de sesion
        ArrayList<String> role = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
        if (role != null && role.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.visualizer.id"))) {
            //se trata de un usuario con role admin, tendra acceso a modicar y ver
            return true;
        }
        //no tiene rol admin
        return false;
    }

    private boolean checkSessionConfig() {
        PropertiesManager pmgr = new PropertiesManager();
        HttpSession sesion = null;
        sesion = pageContext.getSession();

        if (!Sesiones.comprobarSession(sesion)) {
            return false;
        }
        //cojemos el attributo "role" de sesion
        ArrayList<String> role = (ArrayList<String>) sesion.getAttribute(Constants.ROLE);
        if (role != null && role.contains(pmgr.getValue(CRAWLER_PROPERTIES, "role.configurator.id"))) {
            //se trata de un usuario con role admin, tendra acceso a modicar y ver
            return true;
        }
        //no tiene rol admin
        return false;
    }


    private boolean checkSession() {
        HttpSession sesion = null;
        String sesionId = "";

        try {

            sesion = pageContext.getSession();

            // Primera comprobacion: tenemos
            if (sesion.getAttribute(Constants.ID_SESION) != null) {
                sesionId = (String) sesion.getAttribute(Constants.ID_SESION);
            } else {
                return false;
            }

            // Segunda comprobacion
            //Si el ID  es el mismo que teniamos guardado Y ademas se conserva el objeto de sesion
            return sesionId.equals(sesion.getId());
        } catch (Exception e) {
            return false;
        }
    }

    public int doStartTag() throws JspException {
        StringBuffer results = new StringBuffer("");
        String urlinicio = "../oaw/";

        try {
            // Se toma el stream de salida
            JspWriter writer = pageContext.getOut();

            // Si nos han pasado al atributo action con valor 'check' o vacio
            // comprobamos que la sesion esta activa y la pagina del usuario es valida
            if (this.getAction() == null || this.getAction().trim().compareTo("") == 0 || this.getAction().trim().toLowerCase().compareTo(Constants.CHECK) == 0) {
                if (!checkSession()) { // Sesion incorrecta!
                    writer.print(results.toString());
                    pageContext.forward(urlinicio);
                }
                return EVAL_BODY_INCLUDE;
            }

            //Para comprobar si el usuario actual tiene el rol de admin
            if (pageContext.getSession() != null && this.getAction() != null && this.getAction().trim().toLowerCase().compareTo(Constants.IF_ADMIN) == 0) {

                if (checkSessionAdmin()) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    return SKIP_BODY;
                }
            }
            //Para comprobar si el usuario actual tiene el rol de visor
            if (pageContext.getSession() != null && this.getAction() != null && this.getAction().trim().toLowerCase().compareTo(Constants.IF_VISOR) == 0) {
                if (checkSessionVisor()) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    return SKIP_BODY;
                }
            }
            //Para comprobar si el usuario actual tiene el rol de configurador
            if (pageContext.getSession() != null && this.getAction() != null && this.getAction().trim().toLowerCase().compareTo(Constants.IF_CONFIG) == 0) {
                if (checkSessionConfig()) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    return SKIP_BODY;
                }
            }
            //Para comprobar si el usuario actual tiene el rol de configurador o administrador
            if (pageContext.getSession() != null && this.getAction() != null && this.getAction().trim().toLowerCase().compareTo(Constants.IF_CONFIG_ADMIN) == 0) {
                if (checkpermissionAdminConfig()) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    pageContext.forward(urlinicio);
                    //return SKIP_BODY;
                }
            }
        } catch (Exception e) {
        }

        // Evalua el body de la
        return EVAL_BODY_INCLUDE;
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
