package es.inteco.rastreador2.actionform.semillas;


import org.apache.struts.validator.ValidatorForm;

public class SemillaSearchForm extends ValidatorForm {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String nombre;
    private String categoria;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}