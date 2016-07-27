package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriorityForm implements Serializable {
    private int numProblems;
    private int numWarnings;
    private int numInfos;
    private String priorityName;
    private List<GuidelineForm> guidelines;

    public PriorityForm() {
        guidelines = new ArrayList<>();
    }

    public int getNumProblems() {
        return numProblems;
    }

    public void setNumProblems(int numProblems) {
        this.numProblems = numProblems;
    }

    public int getNumWarnings() {
        return numWarnings;
    }

    public void setNumWarnings(int numWarnings) {
        this.numWarnings = numWarnings;
    }

    public int getNumInfos() {
        return numInfos;
    }

    public void setNumInfos(int numInfos) {
        this.numInfos = numInfos;
    }

    public List<GuidelineForm> getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(List<GuidelineForm> guidelines) {
        this.guidelines = guidelines;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
}
