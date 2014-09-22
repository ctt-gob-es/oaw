package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProblemForm implements Serializable {
    private String type;
    private String check;
    private String code;
    private String group;
    private String description;
    private String rationale;
    private String error;
    private String note;
    private List<SpecificProblemForm> specificProblems;

    public ProblemForm() {
        specificProblems = new ArrayList<SpecificProblemForm>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SpecificProblemForm> getSpecificProblems() {
        return specificProblems;
    }

    public void setSpecificProblems(List<SpecificProblemForm> specificProblems) {
        this.specificProblems = specificProblems;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
