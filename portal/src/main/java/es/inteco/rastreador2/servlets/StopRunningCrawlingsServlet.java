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
package es.inteco.rastreador2.servlets;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;

/**
 * The Class StopRunningCrawlingsServlet.
 */
public class StopRunningCrawlingsServlet extends GenericServlet {

    /**
	 * Inits the.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
    /*
     * Puede que al detener el servidor hayan quedado rastreos activos. Los procesos se detendán al parar el servidor
     * pero en base de datos segirán constando como activos. Para evitarlo, cada vez que se reinicie el servidor
     * se actualizará el estado de los procesos activos a 'finalizado'.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try (Connection connection = DataBaseManager.getConnection()) {
            RastreoDAO.stopRunningCrawlings(connection);
            RastreoDAO.stopRunningObservatories(connection);
            Logger.putLog("Se ha cambiado el estado de los rastreos activos a 'finalizado'", StopRunningCrawlingsServlet.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("No se ha podido cambiar el estado de los rastreos activos a 'finalizado'", StopRunningCrawlingsServlet.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    /**
	 * Service.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

    }

}
