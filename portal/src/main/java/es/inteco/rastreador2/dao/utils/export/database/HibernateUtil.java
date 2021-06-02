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
package es.inteco.rastreador2.dao.utils.export.database;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.net.URL;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * The Class HibernateUtil.
 */
public final class HibernateUtil {

    /** The Constant sessionFactory. */
    private static final SessionFactory sessionFactory;

    /**
	 * Instantiates a new hibernate util.
	 */
    private HibernateUtil() {
    }

    static {
        try {
            final PropertiesManager pmgr = new PropertiesManager();
            final URL hibernateCfgFile = HibernateUtil.class.getClassLoader().getResource(pmgr.getValue(CRAWLER_PROPERTIES, "hibernate.cfg.file.export.database"));
            sessionFactory = new AnnotationConfiguration().configure(hibernateCfgFile).buildSessionFactory();
        } catch (Exception e) {
            Logger.putLog("Initial SessionFactory creation failed", HibernateUtil.class, Logger.LOG_LEVEL_ERROR, e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
