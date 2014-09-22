package es.inteco.multilanguage.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class BaseDAO {

    /**
     * Salvar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object save(Session session, Object object) throws HibernateException {
        session.save(object);
        return object;
    }

    /**
     * Actualizar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object update(Session session, Object object) throws HibernateException {
        session.update(object);
        return object;
    }

    /**
     * Borrar en base de datos
     *
     * @param analysis
     * @return
     */
    public static Object delete(Session session, Object object) throws HibernateException {
        session.delete(object);
        return object;
    }
}
