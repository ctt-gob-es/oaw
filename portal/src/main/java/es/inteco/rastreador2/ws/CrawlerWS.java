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
package es.inteco.rastreador2.ws;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.rastreador2.ws.commons.Constants;
import es.inteco.rastreador2.ws.utils.CrawlerWSUtils;

import java.util.Calendar;

public class CrawlerWS {

    public Integer webCrawl(String url, int depth, int size, String guideline, String email, Calendar schedulingDate, String idUser) {
        BasicServiceForm basicServiceForm = CrawlerWSUtils.getBasicServiceForm(url, depth, size, guideline, email, schedulingDate, idUser);

        if (schedulingDate == null) {
            return CrawlerWSUtils.webCrawl(basicServiceForm);
        } else {
            try {
                if (schedulingDate.after(Calendar.getInstance())) {
                    BasicServiceUtils.scheduleBasicServiceJob(basicServiceForm);

                    return Constants.CRAWL_QUEUED;
                } else {
                    return Constants.CRAWL_SCHEDULING_PAST_DATE;
                }
            } catch (Exception e) {
                Logger.putLog("Error al programar el rastreo: ", CrawlerWS.class, Logger.LOG_LEVEL_ERROR, e);
                return Constants.CRAWL_SCHEDULING_ERROR;
            }


        }
    }

}