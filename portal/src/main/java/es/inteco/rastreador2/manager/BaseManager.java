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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.dao.BaseDAO;
import es.inteco.rastreador2.dao.utils.export.database.HibernateUtil;

/**
 * The Class BaseManager.
 */
public abstract class BaseManager {
	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	protected static Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		return sessionFactory.openSession();
	}

	/**
	 * Salvar en base de datos.
	 *
	 * @param object the object
	 * @return the object
	 * @throws HibernateException the hibernate exception
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
	 * Actualizar en base de datos.
	 *
	 * @param object the object
	 * @return the object
	 * @throws HibernateException the hibernate exception
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
	 * Borrar en base de datos.
	 *
	 * @param object the object
	 * @return the object
	 * @throws HibernateException the hibernate exception
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

	/**
	 * Borra contenido de las tablas para facilitar la importación de entidades desde el SSP
	 * 
	 * @param tables Nombre de las tablas a borrar
	 */
	public static void truncateTables(String[] tables) {
		Logger.putLog("Truncate tables", BaseManager.class, Logger.LOG_LEVEL_ERROR);
		Session session = getSession();
		session.beginTransaction();
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		for (String table : tables) {
			Logger.putLog("Truncate table: " + table, BaseManager.class, Logger.LOG_LEVEL_ERROR);
			session.createSQLQuery("truncate table " + table).executeUpdate();
		}
		session.getTransaction().commit();
		session = getSession();
		session.beginTransaction();
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
		session.getTransaction().commit();
		session.close();
		Logger.putLog("End Truncate tables", BaseManager.class, Logger.LOG_LEVEL_ERROR);
	}
}
