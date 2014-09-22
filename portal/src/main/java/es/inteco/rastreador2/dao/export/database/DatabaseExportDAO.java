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
