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

/**
 * The Class Logger.
 */
@SuppressWarnings("rawtypes")
public final class Logger {
	/** The Constant LOG_LEVEL_ERROR. */
	public static final int LOG_LEVEL_ERROR = 1;
	/** The Constant LOG_LEVEL_DEBUG. */
	public static final int LOG_LEVEL_DEBUG = 2;
	/** The Constant LOG_LEVEL_INFO. */
	public static final int LOG_LEVEL_INFO = 3;
	/** The Constant LOG_LEVEL_WARNING. */
	public static final int LOG_LEVEL_WARNING = 4;

	/**
	 * Instantiates a new logger.
	 */
	private Logger() {
	}

	/**
	 * Put log.
	 *
	 * @param message  the message
	 * @param clazz    the clazz
	 * @param logLevel the log level
	 */
	public static void putLog(final String message, final Class clazz, final int logLevel) {
		writeLog(LogFactory.getLog(clazz), message, logLevel, null);
	}

	/**
	 * Put log.
	 *
	 * @param message  the message
	 * @param clazz    the clazz
	 * @param logLevel the log level
	 * @param ex       the ex
	 */
	public static void putLog(final String message, final Class clazz, final int logLevel, final Exception ex) {
		writeLog(LogFactory.getLog(clazz), message, logLevel, ex);
	}

	/**
	 * Write log.
	 *
	 * @param logger    the logger
	 * @param message   the message
	 * @param logLevel  the log level
	 * @param exception the exception
	 */
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
