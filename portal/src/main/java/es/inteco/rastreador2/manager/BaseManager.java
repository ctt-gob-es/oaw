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
            if (session.isOpen()) {
                session.close();
            }
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
            if (session.isOpen()) {
                session.close();
            }
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
            if (session.isOpen()) {
                session.close();
            }
        }

        return object;
    }
}
