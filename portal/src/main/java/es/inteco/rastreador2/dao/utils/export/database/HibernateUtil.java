package es.inteco.rastreador2.dao.utils.export.database;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.File;
import java.net.URL;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class HibernateUtil {

    private static final SessionFactory sessionFactory;

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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
