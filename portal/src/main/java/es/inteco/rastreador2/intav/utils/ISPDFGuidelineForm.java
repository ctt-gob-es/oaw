package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.GuidelineForm;

import java.util.ArrayList;
import java.util.List;

public class ISPDFGuidelineForm {

    public GuidelineForm guideline;
    public List<ISPDFPautaForm> pautaList;

    public ISPDFGuidelineForm() {
        guideline = new GuidelineForm();
        pautaList = new ArrayList<ISPDFPautaForm>();
    }

    public GuidelineForm getGuideline() {
        return guideline;
    }

    public void setGuideline(GuidelineForm guideline) {
        this.guideline = guideline;
    }

    public List<ISPDFPautaForm> getPautaList() {
        return pautaList;
    }

    public void setPautaList(List<ISPDFPautaForm> pautaList) {
        this.pautaList = pautaList;
    }

    @Override
    public int hashCode() {
        return guideline != null ? guideline.getDescription().hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISPDFGuidelineForm) {
            ISPDFGuidelineForm form = (ISPDFGuidelineForm) obj;
            if (form.getGuideline().getDescription().equals(guideline.getDescription())) {
                return true;
            }
        }
        return false;
    }

}
