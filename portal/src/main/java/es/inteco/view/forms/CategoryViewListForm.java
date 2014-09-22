package es.inteco.view.forms;

import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewListForm {

    private CategoriaForm category;
    private List<LabelValueBean> viewList;

    public CategoryViewListForm() {
        category = new CategoriaForm();
        viewList = new ArrayList<LabelValueBean>();
    }

    public CategoryViewListForm(CategoriaForm category, List<LabelValueBean> viewList) {
        this.category = category;
        this.viewList = viewList;
    }

    public CategoriaForm getCategory() {
        return category;
    }

    public void setCategory(CategoriaForm category) {
        this.category = category;
    }

    public List<LabelValueBean> getViewList() {
        return viewList;
    }

    public void setViewList(List<LabelValueBean> viewList) {
        this.viewList = viewList;
    }

}
