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

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import es.inteco.rastreador2.dao.BaseDAO;
import es.inteco.rastreador2.dao.importar.database.AdministrativeLevel;
import es.inteco.rastreador2.dao.importar.database.ClassificationLabel;
import es.inteco.rastreador2.dao.importar.database.Complexity;
import es.inteco.rastreador2.dao.importar.database.Label;
import es.inteco.rastreador2.dao.importar.database.Scope;
import es.inteco.rastreador2.dao.importar.database.Seed;
import es.inteco.rastreador2.dao.importar.database.SeedType;
import es.inteco.rastreador2.dao.importar.database.Segment;
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
	 * Borrar todas las entradas asociadas a una entidad
	 * 
	 * @param clazz
	 */
	public static void deleteAll(final Class<?> clazz) {
		Session session = getSession();
		session.beginTransaction();
		final List<?> instances = session.createCriteria(clazz).list();
		for (Object obj : instances) {
			if (obj instanceof ClassificationLabel) {
				if (((ClassificationLabel) obj).getEtiquetas() != null) {
					Set<Label> etiquetas = ((ClassificationLabel) obj).getEtiquetas();
					for (Label etiqueta : etiquetas) {
						session.delete(etiqueta);
					}
					((ClassificationLabel) obj).setEtiquetas(null);
				}
			} else if (obj instanceof Segment) {
				((Segment) obj).setSemillas(null);
			} else if (obj instanceof AdministrativeLevel) {
				((AdministrativeLevel) obj).setSemillas(null);
			} else if (obj instanceof Complexity) {
				((Complexity) obj).setSemilla(null);
			} else if (obj instanceof SeedType) {
				((SeedType) obj).setSemilla(null);
			} else if (obj instanceof Seed) {
				((Seed) obj).setDependencias(null);
			} else if (obj instanceof Scope) {
				if (((Scope) obj).getSemillas() != null) {
					Set<Seed> semillas = ((Scope) obj).getSemillas();
					for (Seed semilla : semillas) {
						session.delete(semilla);
					}
					((Scope) obj).setSemillas(null);
				}
			} else if (((Label) obj).getSemillas() != null) {
				if (((Label) obj).getSemillas() != null) {
					Set<Seed> semillas = ((Label) obj).getSemillas();
					for (Seed semilla : semillas) {
						session.delete(semilla);
					}
					((Label) obj).setSemillas(null);
				}
			}
			session.delete(obj);
		}
		session.getTransaction().commit();
		session.close();
	}
}
