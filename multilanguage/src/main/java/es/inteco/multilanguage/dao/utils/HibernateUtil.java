package es.inteco.multilanguage.dao.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.common.Constants;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            PropertiesManager pmgr = new PropertiesManager();
            File hibernateCfgFile = new File(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "hibernate.cfg.file"));
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
