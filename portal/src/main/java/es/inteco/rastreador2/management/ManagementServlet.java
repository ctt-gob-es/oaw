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
package es.inteco.rastreador2.management;

import es.inteco.common.logging.Logger;

import javax.servlet.*;
import java.io.IOException;

public class ManagementServlet extends GenericServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ManagementThread managementThread = null;

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            Logger.putLog("Lanzando el hilo de monitorización de recursos", ManagementServlet.class, Logger.LOG_LEVEL_INFO);
            managementThread = new ManagementThread();
            managementThread.start();
        } catch (Exception e) {
            Logger.putLog("Error al lanzar el hilo de monitorización", ManagementServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void destroy() {
        if (managementThread != null) {
            // Paramos el thread
            managementThread.stopThread();
            managementThread = null;
        }
        super.destroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

}