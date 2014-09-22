package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.PriorityForm;

import java.util.ArrayList;
import java.util.List;

public class IntavSimplePDFForm {

    public PriorityForm priority;
    public List<ISPDFGuidelineForm> guidelinesList;

    public IntavSimplePDFForm() {
        priority = new PriorityForm();
        guidelinesList = new ArrayList<ISPDFGuidelineForm>();
    }

    public PriorityForm getPriority() {
        return priority;
    }

    public void setPriority(PriorityForm priority) {
        this.priority = priority;
    }

    public List<ISPDFGuidelineForm> getGuidelinesList() {
        return guidelinesList;
    }

    public void setGuidelinesList(List<ISPDFGuidelineForm> guidelinesList) {
        this.guidelinesList = guidelinesList;
    }

    @Override
    public int hashCode() {
        return priority != null ? priority.getPriorityName().hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntavSimplePDFForm) {
            IntavSimplePDFForm form = (IntavSimplePDFForm) obj;
            if (form.priority.getPriorityName().equals(priority.getPriorityName())) {
                return true;
            }
        }
        return false;
    }

}
