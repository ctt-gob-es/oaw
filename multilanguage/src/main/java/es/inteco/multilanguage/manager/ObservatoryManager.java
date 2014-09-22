package es.inteco.multilanguage.manager;

import es.inteco.multilanguage.dao.ObservatoryDAO;
import es.inteco.multilanguage.persistence.Observatory;
import org.hibernate.Session;

public class ObservatoryManager {
    public static Observatory getMultilanguageObservatory(Long idExecution) {
        Session session = BaseManager.getSession();

        Observatory observatory = ObservatoryDAO.getMultilanguageObservatory(session, idExecution);

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return observatory;
    }
}
