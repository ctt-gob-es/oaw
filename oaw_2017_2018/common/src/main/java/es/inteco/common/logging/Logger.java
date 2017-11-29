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
package es.inteco.common.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Logger {
    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARNING = 4;

    private Logger() {
    }

    public static void putLog(final String message, final Class clazz, final int logLevel) {
        writeLog(LogFactory.getLog(clazz), message, logLevel, null);
    }

    public static void putLog(final String message, final Class clazz, final int logLevel, final Exception ex) {
        writeLog(LogFactory.getLog(clazz), message, logLevel, ex);
    }

    private static void writeLog(final Log logger, final String message, final int logLevel, final Exception exception) {
        if (logLevel == LOG_LEVEL_ERROR) {
            logger.error(message, exception);
        } else if (logLevel == LOG_LEVEL_DEBUG) {
            logger.debug(message, exception);
        } else if (logLevel == LOG_LEVEL_INFO) {
            logger.info(message, exception);
        } else if (logLevel == LOG_LEVEL_WARNING) {
            logger.warn(message, exception);
        }
    }

}
