package es.inteco.rastreador2.imp.xml.result;

import java.util.ArrayList;
import java.util.List;

public class SectionSetForm {
    private List<SectionForm> sectionList;

    public SectionSetForm() {
        sectionList = new ArrayList<SectionForm>();
    }

    public List<SectionForm> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionForm> sectionList) {
        this.sectionList = sectionList;
    }

}
