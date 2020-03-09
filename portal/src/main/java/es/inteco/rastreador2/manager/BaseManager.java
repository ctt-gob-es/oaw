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
package es.inteco.rastreador2.manager;

import es.inteco.rastreador2.dao.BaseDAO;
import es.inteco.rastreador2.dao.utils.export.database.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class BaseManager {

    protected static Session getSession() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        return sessionFactory.openSession();
    }

    /**
     * Salvar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object save(Object object) throws HibernateException {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            object = BaseDAO.save(session, object);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new HibernateException(e);
        } finally {
            session.close();
        }

        return object;
    }

    /**
     * Actualizar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object update(Object object) throws HibernateException {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            object = BaseDAO.update(session, object);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new HibernateException(e);
        } finally {
            session.close();
        }

        return object;
    }

    /**
     * Borrar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object delete(Object object) throws HibernateException {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            object = BaseDAO.delete(session, object);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new HibernateException(e);
        } finally {
            session.close();
        }

        return object;
    }
}
