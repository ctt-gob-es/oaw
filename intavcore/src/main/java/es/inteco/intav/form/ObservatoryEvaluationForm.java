package es.inteco.intav.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObservatoryEvaluationForm implements Serializable {
    private Long observatoryExecutionId;
    private Long crawlerExecutionId;
    private String entity;
    private String url;
    private BigDecimal score;
    private SeedForm seed;
    private List<ObservatoryLevelForm> groups;
    private List<AspectScoreForm> aspects;
    private List<Integer> checksFailed;
    private String source;

    public ObservatoryEvaluationForm() {
        this.groups = new ArrayList<>();
        this.checksFailed = new ArrayList<>();
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

    public List<ObservatoryLevelForm> getGroups() {
        return groups;
    }

    public void setGroups(List<ObservatoryLevelForm> groups) {
        this.groups = groups;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<AspectScoreForm> getAspects() {
        return aspects;
    }

    public void setAspects(List<AspectScoreForm> aspects) {
        this.aspects = aspects;
    }

    public Long getObservatoryExecutionId() {
        return observatoryExecutionId;
    }

    public void setObservatoryExecutionId(Long observatoryExecutionId) {
        this.observatoryExecutionId = observatoryExecutionId;
    }

    public Long getCrawlerExecutionId() {
        return crawlerExecutionId;
    }

    public void setCrawlerExecutionId(Long crawlerExecutionId) {
        this.crawlerExecutionId = crawlerExecutionId;
    }

    public List<Integer> getChecksFailed() {
        return checksFailed;
    }

    public void setChecksFailed(List<Integer> checksFailed) {
        this.checksFailed = checksFailed;
    }

    public SeedForm getSeed() {
        return seed;
    }

    public void setSeed(SeedForm seed) {
        this.seed = seed;
    }

}
