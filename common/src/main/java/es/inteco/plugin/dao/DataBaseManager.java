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
package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DataBaseManager {

    private DataBaseManager() {
    }

    private static int getNumberOfConnectionRetries() {
        String retryString = System.getenv("DBCONN_RETRY_COUNT");
        int retries = 0;
        try {
            retries = (retryString != null) ? Integer.parseInt(retryString) : 0;
        } catch(NumberFormatException nfe) {
            Logger.putLog("Failed to parse database connection retry count", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, nfe);
        }
        return retries;
    }

    private static int getConnectionRetryDelay() {
        String delayString = System.getenv("DBCONN_RETRY_DELAY");
        int delay = 10;
        try {
            delay = (delayString != null) ? Integer.parseInt(delayString) : 0;
        } catch(NumberFormatException nfe) {
            Logger.putLog("Failed to parse database connection retry delay",DataBaseManager.class, Logger.LOG_LEVEL_ERROR, nfe);
        }
        return delay;
    }

    public static Connection getConnection() throws Exception {
        Logger.putLog("Conectando a la base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_DEBUG);

        final int numberOfRetries = getNumberOfConnectionRetries();
        final int retryDelay = getConnectionRetryDelay();

        for (int numberOfRetriesLeft = numberOfRetries; numberOfRetriesLeft >= 0; numberOfRetriesLeft--) {
            try {
                final Context initContext = new InitialContext();
                final Context envContext = (Context) initContext.lookup("java:/comp/env");

                final DataSource ds = (DataSource) envContext.lookup("jdbc/oaw");
                return ds.getConnection();
            } catch (Exception e) {

                if (numberOfRetriesLeft == 0) {
                    Logger.putLog("Error al conectar a la base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, e);

                    throw e;
                } else {
                    Logger.putLog("Error al conectar a la base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_ERROR);

                    try {
                        // Please verify/correct this Google Translate output ...
                        Logger.putLog(
                            "Durmiendo " + retryDelay + " segundos antes de volver a intentarlo ...",
                            DataBaseManager.class, Logger.LOG_LEVEL_ERROR
                            );
                        Thread.sleep(retryDelay * 1000L);
                    } catch (InterruptedException ie) {
                        Logger.putLog("Thread was interrupted during retry delay", DataBaseManager.class, Logger.LOG_LEVEL_WARNING);
                    }
                }
            }
        }

        return null;
    }

    public static void closeConnection(final Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la conexión a base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }
}
