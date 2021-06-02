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
package es.inteco.rastreador2.utils.basic.service;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;

/**
 * The Class BasicServiceQueingThread.
 */
public class BasicServiceQueingThread extends Thread {

    /** The basic service form. */
    private final BasicServiceForm basicServiceForm;

    /**
	 * Instantiates a new basic service queing thread.
	 *
	 * @param basicServiceForm the basic service form
	 */
    public BasicServiceQueingThread(BasicServiceForm basicServiceForm) {
        this.basicServiceForm = basicServiceForm;
    }

    /**
	 * Run.
	 */
    @Override
    public void run() {
        if (BasicServiceConcurrenceSystem.passQueue()) {
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED);
            BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
            basicServiceThread.start();
        } else {
            // Enviar mensaje de error para advertir de que no ha podido superar la cola porque ha habido mucho tiempo de espera
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_RULED_OUT);
        }
    }
}
