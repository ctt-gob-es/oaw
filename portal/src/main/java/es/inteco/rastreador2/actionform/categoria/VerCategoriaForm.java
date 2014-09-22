package es.inteco.rastreador2.actionform.categoria;

import es.inteco.rastreador2.utils.TerminoCatVer;
import org.apache.struts.validator.ValidatorForm;

import java.util.List;

public class VerCategoriaForm extends ValidatorForm {
    private static final long serialVersionUID = 1L;

    private String categoria, nombre_antiguo;
    private int id_categoria;
    private String umbral;
    private List<TerminoCatVer> vectorTerminos;

    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getUmbral() {
        return umbral;
    }

    public void setUmbral(String umbral) {
        this.umbral = umbral;
    }

    public List<TerminoCatVer> getVectorTerminos() {
        return vectorTerminos;
    }

    public void setVectorTerminos(List<TerminoCatVer> vectorTerminos) {
        this.vectorTerminos = vectorTerminos;
    }

}
