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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;

/**
 * The Class FreeSpaceJob.
 */
@SuppressWarnings("deprecation")
public class CleanSendResultsJob implements StatefulJob {
	/** The Constant LOG. */
	public static final Log LOG = LogFactory.getLog(CleanSendResultsJob.class);

	/**
	 * Execute.
	 *
	 * @param context the context
	 * @throws JobExecutionException the job execution exception
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Logger.putLog("Inicio del JOB que se encarga de comprobar ficheros de resultados obsoletos", CleanSendResultsJob.class, Logger.LOG_LEVEL_INFO);
		PropertiesManager pmgr = new PropertiesManager();
		try {
			Connection c = DataBaseManager.getConnection();
			long days = ObservatorioDAO.getFileExpirationFromConfig(c);
			DataBaseManager.closeConnection(c);
			final String exportDir = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.send.path");
			final File directory = new File(exportDir);
			if (directory.exists()) {
				File[] files = directory.listFiles();
				Arrays.sort(files, Comparator.comparingLong(File::lastModified));
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file != null && file.getName().endsWith(".zip")) {
						long diff = new Date().getTime() - file.lastModified();
						if (diff > days * 24 * 60 * 60 * 1000) {
							Logger.putLog("[CleanSendResultsJob] Deleting file: " + file.getName(), CleanSendResultsJob.class, Logger.LOG_LEVEL_INFO);
							file.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Se ha producido un error al intentar borrar los directorios temporales", CleanSendResultsJob.class, Logger.LOG_LEVEL_INFO, e);
		}
	}
}
