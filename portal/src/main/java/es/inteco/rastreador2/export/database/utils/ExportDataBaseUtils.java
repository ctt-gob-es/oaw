package es.inteco.rastreador2.export.database.utils;

import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.export.database.Page;
import es.inteco.rastreador2.dao.export.database.Site;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.PageForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public final class ExportDataBaseUtils {

    private ExportDataBaseUtils() {
    }

    public static ObservatoryForm getObservatoryForm(Observatory observatory) throws Exception {
        ObservatoryForm observatoryForm = new ObservatoryForm();
        BeanUtils.copyProperties(observatoryForm, observatory);
        List<CategoryForm> categoryFormList = new ArrayList<CategoryForm>();
        if (observatory.getCategoryList() != null) {
            for (Category category : observatory.getCategoryList()) {
                categoryFormList.add(getCategoryForm(category));
            }
        }
        observatoryForm.setCategoryFormList(categoryFormList);
        return observatoryForm;
    }

    public static CategoryForm getCategoryForm(Category category) throws Exception {
        CategoryForm categoryForm = new CategoryForm();

        BeanUtils.copyProperties(categoryForm, category);
        List<SiteForm> siteFormList = new ArrayList<SiteForm>();
        for (Site site : category.getSiteList()) {
            siteFormList.add(getSiteForm(site));
        }
        categoryForm.setSiteFormList(siteFormList);
        return categoryForm;
    }

    public static SiteForm getSiteForm(Site site) throws Exception {
        SiteForm siteForm = new SiteForm();

        BeanUtils.copyProperties(siteForm, site);
        List<PageForm> pageFormList = new ArrayList<PageForm>();
        for (Page page : site.getPageList()) {
            PageForm pageForm = new PageForm();
            BeanUtils.copyProperties(pageForm, page);
            pageFormList.add(pageForm);
        }
        siteForm.setPageList(pageFormList);
        return siteForm;
    }

}
