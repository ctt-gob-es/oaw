package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CategoriaForm extends ValidatorForm {
    private String id;
    private String name;
    private FormFile fileSeeds;
    private List<SemillaForm> seeds;
    private int orden;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormFile getFileSeeds() {
        return fileSeeds;
    }

    public void setFileSeeds(FormFile fileSeeds) {
        this.fileSeeds = fileSeeds;
    }

    public List<SemillaForm> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<SemillaForm> seeds) {
        this.seeds = seeds;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        if (seeds == null) {
            seeds = new ArrayList<SemillaForm>();
        }
    }
}
