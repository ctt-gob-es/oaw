package es.inteco.rastreador2.manager;

import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.DatabaseExportDAO;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.export.database.Site;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.export.database.utils.ExportDataBaseUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ObservatoryExportManager extends BaseManager {

    public static ObservatoryForm getObservatory(Long idExecutionObs) throws Exception {
        Session session = getSession();

        Observatory observatory = DatabaseExportDAO.getObservatory(session, idExecutionObs);

        ObservatoryForm observatoryForm = ExportDataBaseUtils.getObservatoryForm(observatory);

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return observatoryForm;
    }

    public static List<CategoryForm> getSiteCategory(Long idExecutionObs) throws Exception {
        Session session = getSession();

        List<Category> categories = DatabaseExportDAO.getCategoryInformation(session, idExecutionObs);
        List<CategoryForm> categoryFormList = new ArrayList<CategoryForm>();

        for (Category category : categories) {
            categoryFormList.add(ExportDataBaseUtils.getCategoryForm(category));
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return categoryFormList;
    }

    public static List<SiteForm> getSiteInformation(Long idCategory) throws Exception {
        Session session = getSession();

        List<Site> siteList = DatabaseExportDAO.getSiteInformation(session, idCategory);
        List<SiteForm> siteFormList = new ArrayList<SiteForm>();

        for (Site site : siteList) {
            siteFormList.add(ExportDataBaseUtils.getSiteForm(site));
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return siteFormList;
    }

}
