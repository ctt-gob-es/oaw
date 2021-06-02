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

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * The Class ScheduleBasicServiceServlet.
 */
public class ScheduleBasicServiceServlet extends GenericServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Inits the.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Logger.putLog("Programando las peticiones al servicio básico", ScheduleBasicServiceServlet.class, Logger.LOG_LEVEL_INFO);

        final List<BasicServiceForm> basicServiceRequests = BasicServiceUtils.getBasicServiceRequestByStatus(Constants.BASIC_SERVICE_STATUS_SCHEDULED);
        try {
            if (basicServiceRequests != null) {
                for (BasicServiceForm basicServiceForm : basicServiceRequests) {
                    BasicServiceUtils.scheduleBasicServiceJob(basicServiceForm);
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente y observatorios", ScheduleBasicServiceServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    /**
	 * Service.
	 *
	 * @param arg0 the arg 0
	 * @param arg1 the arg 1
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
    }
}
