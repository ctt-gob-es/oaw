package es.inteco.rastreador2.imp.xml.result;

import es.inteco.common.Constants;

import java.util.ArrayList;
import java.util.List;

public class SectionForm {

    private String title;
    private int sectionNumber;
    private int finalSectionToPaint;
    private List<Object> objectList;
    private String type;

    public SectionForm() {
        this.objectList = new ArrayList<Object>();
        this.title = "";
        this.type = Constants.OBJECT_TYPE_SECTION;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public void addObject(Object object) {
        objectList.add(object);
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFinalSectionToPaint() {
        return finalSectionToPaint;
    }

    public void setFinalSectionToPaint(int finalSectionToPaint) {
        this.finalSectionToPaint = finalSectionToPaint;
    }
}
