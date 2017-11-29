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
package es.inteco.rastreador2.dao.export.database;

import es.inteco.rastreador2.dao.BaseDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DatabaseExportDAO extends BaseDAO {

    public static Observatory getObservatory(Session session, Long idExecution) {
        Criteria criteria = session.createCriteria(Observatory.class);
        criteria.add(Restrictions.eq("idExecution", idExecution));

        List<Observatory> observatories = criteria.list();

        if (observatories != null && !observatories.isEmpty()) {
            return observatories.get(0);
        } else {
            return null;
        }
    }

    public static List<Site> getSiteInformation(Session session, Long idCategory) {
        Criteria criteria = session.createCriteria(Site.class);
        criteria.add(Restrictions.eq("idCategory", idCategory));

        List<Site> sites = criteria.list();

        return sites;
    }

    public static List<Category> getCategoryInformation(Session session, Long idExecutionObs) {
        Criteria criteria = session.createCriteria(Category.class);
        criteria.add(Restrictions.eq("idExecution", idExecutionObs));

        List<Category> categories = criteria.list();

        return categories;
    }

}
