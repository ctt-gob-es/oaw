package es.inteco.common;

public class CheckAccessibility {
    private String url;
    private String entity;
    private String level;
    private String guideline;
    private String guidelineFile;
    private long idRastreo;
    private long idObservatory;
    private String content;
    private boolean isWebService;
    private String templateContent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    public String getGuidelineFile() {
        return guidelineFile;
    }

    public void setGuidelineFile(String guidelineFile) {
        this.guidelineFile = guidelineFile;
    }

    public long getIdRastreo() {
        return idRastreo;
    }

    public void setIdRastreo(long idRastreo) {
        this.idRastreo = idRastreo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getIdObservatory() {
        return idObservatory;
    }

    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }

    public boolean isWebService() {
        return isWebService;
    }

    public void setWebService(boolean isWebService) {
        this.isWebService = isWebService;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
}

