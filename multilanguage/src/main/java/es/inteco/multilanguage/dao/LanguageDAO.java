package es.inteco.multilanguage.dao;

import es.inteco.multilanguage.manager.BaseManager;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class LanguageDAO extends BaseManager {

    public static List<Language> getLanguages(boolean onlyToAnalyze) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Language.class);
        if (onlyToAnalyze) {
            criteria.add(Restrictions.like("toAnalyze", true));
        }
        List<Language> languages = criteria.list();

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return MultilanguageUtils.orderLanguages(languages);
    }

    public static Language getLanguage(long id) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Language.class);
        criteria.add(Restrictions.like("id", id));
        List<Language> languages = criteria.list();

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        if (languages != null && !languages.isEmpty()) {
            return languages.get(0);
        } else {
            return null;
        }
    }

    public static Language getLanguage(String name) {
        Session session = getSession();

        Criteria criteria = session.createCriteria(Language.class);
        criteria.add(Restrictions.like("name", name));
        List<Language> languages = criteria.list();

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        if (languages != null && !languages.isEmpty()) {
            return languages.get(0);
        } else {
            return null;
        }
    }

}
