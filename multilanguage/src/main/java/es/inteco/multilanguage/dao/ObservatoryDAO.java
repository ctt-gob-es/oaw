package es.inteco.multilanguage.dao;

import es.inteco.multilanguage.persistence.Observatory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ObservatoryDAO {

    public static Observatory getMultilanguageObservatory(Session session, Long idExecution) {
        Criteria criteria = session.createCriteria(es.inteco.multilanguage.persistence.Observatory.class);
        criteria.add(Restrictions.eq("id", idExecution));

        List<Observatory> observatories = criteria.list();

        if (observatories != null && !observatories.isEmpty()) {
            return observatories.get(0);
        } else {
            return null;
        }
    }
}
