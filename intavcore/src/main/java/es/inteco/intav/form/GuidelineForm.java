package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuidelineForm implements Serializable {
    private String description;
    private String guidelineId;
    private List<PautaForm> pautas;

    public GuidelineForm() {
        pautas = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PautaForm> getPautas() {
        return pautas;
    }

    public void setPautas(List<PautaForm> pautas) {
        this.pautas = pautas;
    }

    public String getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(String guidelineId) {
        this.guidelineId = guidelineId;
    }
}
