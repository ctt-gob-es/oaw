package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObservatorySubgroupForm implements Serializable {
    private String description;
    private String guidelineId;
    private String aspect;
    private List<Integer> failChecks;
    private List<Integer> onlyWarningChecks;
    private List<Integer> ignoreRelatedChecks;
    private List<ProblemForm> problems;
    private int value;

    public ObservatorySubgroupForm() {
        this.failChecks = new ArrayList<Integer>();
        this.onlyWarningChecks = new ArrayList<Integer>();
        this.problems = new ArrayList<ProblemForm>();
        this.ignoreRelatedChecks = new ArrayList<Integer>();
    }

    public List<Integer> getIgnoreRelatedChecks() {
        return ignoreRelatedChecks;
    }

    public void setIgnoreRelatedChecks(List<Integer> ignoreRelatedChecks) {
        this.ignoreRelatedChecks = ignoreRelatedChecks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(String guidelineId) {
        this.guidelineId = guidelineId;
    }

    public List<Integer> getFailChecks() {
        return failChecks;
    }

    public void setFailChecks(List<Integer> failChecks) {
        this.failChecks = failChecks;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public List<ProblemForm> getProblems() {
        if (problems == null) {
            problems = new ArrayList<ProblemForm>();
        }
        return problems;
    }

    public void setProblems(List<ProblemForm> problems) {
        this.problems = problems;
    }

    public List<Integer> getOnlyWarningChecks() {
        return onlyWarningChecks;
    }

    public void setOnlyWarningChecks(List<Integer> onlyWarningChecks) {
        this.onlyWarningChecks = onlyWarningChecks;
    }
}
