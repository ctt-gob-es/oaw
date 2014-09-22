package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EvaluationForm implements Serializable {
    private String entity;
    private String url;
    private String guideline;
    private String source;

    private List<PriorityForm> priorities;

    public EvaluationForm() {
        priorities = new ArrayList<PriorityForm>();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<PriorityForm> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<PriorityForm> priorities) {
        this.priorities = priorities;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }
}
