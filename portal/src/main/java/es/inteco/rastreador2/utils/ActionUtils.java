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

import es.inteco.common.properties.PropertiesManager;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;

import static es.inteco.rastreador2.utils.CrawlerUtils.getResources;

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
     * Asigna a la petición request el mensaje de éxito y la acción de volver para Actions que han sido ejecutadas sin
     * ningún error.
     * @param request la petición
     * @param mensajeKey el identificador del mensaje de éxito a mostrar, se leerá del fichero Application.properties
     * @param returnPathKey el identificador de la acción volver, se leerá del fichero returnPaths.properties
     */
    public static void setSuccesActionAttributes(final HttpServletRequest request, final String mensajeKey, final String returnPathKey) {
        final PropertiesManager pmgr = new PropertiesManager();
        request.setAttribute("mensajeExito", getResources(request).getMessage(mensajeKey));
        request.setAttribute("accionVolver", pmgr.getValue("returnPaths.properties", returnPathKey));
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