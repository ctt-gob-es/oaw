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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.utils.FileUtils;

/**
 * The Class RastreoUtils.
 */
public final class RastreoUtils {
	/**
	 * Instantiates a new rastreo utils.
	 */
	private RastreoUtils() {
	}

	/**
	 * Borrar archivos asociados.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @throws SQLException the SQL exception
	 */
	public static void borrarArchivosAsociados(final Connection c, final String idRastreo) {
		final PropertiesManager pmgr = new PropertiesManager();
		try {
			final FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(c, Long.parseLong(idRastreo));
			final int cartucho = Integer.parseInt(fulfilledCrawlingForm.getIdCartridge());
			final int id = Integer.parseInt(fulfilledCrawlingForm.getId());
			String pdfPath = "";
			if (CartuchoDAO.isCartuchoAccesibilidad(c, cartucho)) {
				String chartPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + idRastreo + File.separator + id;
				pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.intav");
				FileUtils.deleteDir(new File(chartPath));
				File rastreoDir = new File(pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + idRastreo);
				if (existenGraficasAsociadas(rastreoDir)) {
					if (!rastreoDir.delete()) {
						Logger.putLog("No se ha podido borrar el directorio temporal del rastreo " + idRastreo, RastreoUtils.class, Logger.LOG_LEVEL_ERROR);
					}
				}
			} else if (cartucho == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
				pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.lenox");
			}
			FileUtils.deleteDir(new File(pdfPath + File.separator + idRastreo + File.separator + id));
			File rastreoDir = new File(pdfPath + File.separator + idRastreo);
			if (!rastreoDir.delete()) {
				Logger.putLog("No se ha podido borrar el directorio temporal del rastreo " + idRastreo, RastreoUtils.class, Logger.LOG_LEVEL_ERROR);
			}
		} catch (Exception e) {
			Logger.putLog("Error: ", RastreoUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Existen graficas asociadas.
	 *
	 * @param rastreoDir the rastreo dir
	 * @return true, if successful
	 */
	private static boolean existenGraficasAsociadas(final File rastreoDir) {
		if (rastreoDir.isDirectory()) {
			final String[] ficheros = rastreoDir.list();
			return ficheros != null && ficheros.length == 0;
		} else {
			return false;
		}
	}
}
