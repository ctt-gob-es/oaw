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

import es.gob.oaw.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * The Class FreeSpaceJob.
 */
public class FreeSpaceJob implements StatefulJob {

    /** The Constant LOG. */
    public static final Log LOG = LogFactory.getLog(FreeSpaceJob.class);

    /**
	 * Execute.
	 *
	 * @param context the context
	 * @throws JobExecutionException the job execution exception
	 */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logger.putLog("Inicio del JOB que se encarga de comprobar el espacio libre en el disco duro", FreeSpaceJob.class, Logger.LOG_LEVEL_INFO);
        PropertiesManager pmgr = new PropertiesManager();
        BigDecimal limitPercent = new BigDecimal(pmgr.getValue("management.properties", "freespace.percentage.limit"));

        try {
            File[] files = File.listRoots();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getTotalSpace() > 0) {
                    BigDecimal freeSpace = new BigDecimal(files[i].getFreeSpace());
                    BigDecimal totalSpace = new BigDecimal(files[i].getTotalSpace());
                    BigDecimal spacePercent = freeSpace.divide(totalSpace, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    if (spacePercent.compareTo(limitPercent) < 0) {
                        Logger.putLog("El espacio libre en la unidad " + files[i].getPath() + " es del " + spacePercent + "%." +
                                " Se va a proceder a avisar a los administradores", FreeSpaceJob.class, Logger.LOG_LEVEL_INFO);

                        try {
                            sendMail(files[i].getPath(), spacePercent);
                        } catch (Exception e) {
                            Logger.putLog("Error al intentar enviar el correo electrónico", ManagementThread.class, Logger.LOG_LEVEL_ERROR, e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar borrar los directorios temporales", FreeSpaceJob.class, Logger.LOG_LEVEL_INFO, e);
        }
    }

    /**
	 * Send mail.
	 *
	 * @param filePath     the file path
	 * @param spacePercent the space percent
	 * @throws Exception the exception
	 */
    private void sendMail(String filePath, BigDecimal spacePercent) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> adminMails = DAOUtils.getMailsByRol(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")));
        String alertSubject = pmgr.getValue("management.properties", "freespace.alert.subject");
        String alertText = pmgr.getValue("management.properties", "common.alert.text");
        alertText += pmgr.getValue("management.properties", "freespace.alert.text")
                .replace("{0}", filePath).replace("{1}", spacePercent.toPlainString());
        String alertFromAddress = pmgr.getValue("management.properties", "alert.from.address");
        String alertFromName = pmgr.getValue("management.properties", "alert.from.name");

        MailService mailService = new MailService();
        mailService.sendMail(adminMails, alertSubject, alertText);
    }

}
