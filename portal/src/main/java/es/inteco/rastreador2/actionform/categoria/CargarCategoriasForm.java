package es.inteco.rastreador2.actionform.categoria;


import es.inteco.rastreador2.utils.Categoria;
import org.apache.struts.action.ActionForm;

import java.util.List;


public class CargarCategoriasForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<Categoria> cats;

    public List<Categoria> getCats() {
        return cats;
    }

    public void setCats(List<Categoria> cats) {
        this.cats = cats;
    }

}