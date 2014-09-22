package es.inteco.intav.persistence;

import java.util.Date;

public class Analysis {

    private int code;
    private Date date;
    private String url;
    private long time;
    private String file;
    private String entity;
    private long tracker;
    private String guideline;
    private int problems;
    private int warnings;
    private int observations;
    private String checksExecutedStr;
    private int status;
    private String source;

    public int getProblems() {
        return problems;
    }

    public void setProblems(int problems) {
        this.problems = problems;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public int getObservations() {
        return observations;
    }

    public void setObservations(int observations) {
        this.observations = observations;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public long getTracker() {
        return tracker;
    }

    public void setTracker(long tracker) {
        this.tracker = tracker;
    }

    public void setTracker(int tracker) {
        this.tracker = tracker;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    public String getChecksExecutedStr() {
        return checksExecutedStr;
    }

    public void setChecksExecutedStr(String checksExecutedStr) {
        this.checksExecutedStr = checksExecutedStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
