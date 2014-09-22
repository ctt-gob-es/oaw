package es.inteco.rastreador2.actionform.semillas;


import org.apache.struts.action.ActionForm;

import java.util.List;


public class CargarSemillaWebsForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<String> urls;
    private String urls_string;
    private String archivo;


    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getUrls_string() {
        return urls_string;
    }

    public void setUrls_string(String urls_string) {
        this.urls_string = urls_string;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}