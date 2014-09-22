package es.inteco.rastreador2.manager.export.database;

import es.inteco.rastreador2.dao.export.database.DatabaseExportDAO;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.manager.BaseManager;
import org.hibernate.Session;

public class DatabaseExportManager extends BaseManager {

    public static Observatory getObservatory(Long idExecution) {
        Session session = getSession();

        Observatory observatory = DatabaseExportDAO.getObservatory(session, idExecution);

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return observatory;
    }

}
