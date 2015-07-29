package es.inteco.intav.form;

import java.math.BigDecimal;
import java.util.List;

public class ObservatorySiteEvaluationForm {
    private Long id;
    private String name;
    private String level;
    private String acronym;
    private BigDecimal score;
    private Long idSeed;

    private List<ObservatoryEvaluationForm> pages;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<ObservatoryEvaluationForm> getPages() {
        return pages;
    }

    public void setPages(List<ObservatoryEvaluationForm> pages) {
        this.pages = pages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Long getIdSeed() {
        return idSeed;
    }

    public void setIdSeed(Long idSeed) {
        this.idSeed = idSeed;
    }

}