package es.inteco.rastreador2.actionform.semillas;


import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;


public class NuevaSemillaGoogleForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String paginas;
    private String query;
    private String control;
    private String nombreSemilla;
    private CategoriaForm categoria;

    public String getNombreSemilla() {
        return nombreSemilla;
    }

    public void setNombreSemilla(String nombreSemilla) {
        this.nombreSemilla = nombreSemilla;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public CategoriaForm getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        if (categoria == null) {
            categoria = new CategoriaForm();
        }
    }
}