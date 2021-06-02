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

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import java.util.Random;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * The Class BasicServiceConcurrenceSystem.
 */
public class BasicServiceConcurrenceSystem extends Thread {

    /** The concurrent users. */
    private static int concurrentUsers = 0;

    /**
	 * Gets the concurrent users.
	 *
	 * @return the concurrent users
	 */
    public static int getConcurrentUsers() {
        return concurrentUsers;
    }

    /**
	 * Sets the concurrent users.
	 *
	 * @param concurrentUsers the new concurrent users
	 */
    public static void setConcurrentUsers(int concurrentUsers) {
        BasicServiceConcurrenceSystem.concurrentUsers = concurrentUsers;
    }

    /**
	 * Increment concurrent users.
	 */
    public synchronized static void incrementConcurrentUsers() {
        concurrentUsers++;
    }

    /**
	 * Decrement concurrent users.
	 */
    public synchronized static void decrementConcurrentUsers() {
        concurrentUsers--;
    }

    /**
	 * Pass concurrence.
	 *
	 * @return true, if successful
	 */
    public static boolean passConcurrence() {
        PropertiesManager pmgr = new PropertiesManager();
        int concurrentUsers = BasicServiceConcurrenceSystem.getConcurrentUsers();
        return concurrentUsers < Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.max.num.concurrent.users"));
    }

    /**
	 * Pass queue.
	 *
	 * @return true, if successful
	 */
    public static boolean passQueue() {
        PropertiesManager pmgr = new PropertiesManager();
        int counter = 0;
        while (BasicServiceConcurrenceSystem.getConcurrentUsers() >= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.max.num.concurrent.users"))) {
            try {
                Logger.putLog("Hay demasiados usuarios concurrentes, se va a esperar para atender la solicitud del usuario", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_INFO);

                if (counter >= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.num.retries.check.concurrence"))) {
                    Logger.putLog("Se va a desatender la solicitud debido a que hay demasiados usuarios concurrentes", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_INFO);
                    return false;
                }

                Random r = new Random(System.currentTimeMillis());
                int max = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.time.retry.check.concurrence"));
                int min = max / 2;
                Thread.sleep(min + r.nextInt(min));
                counter++;
            } catch (Exception e) {
                Logger.putLog("Exception: ", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return true;
    }

}
