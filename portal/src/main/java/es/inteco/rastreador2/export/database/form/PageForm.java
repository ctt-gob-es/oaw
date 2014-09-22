package es.inteco.rastreador2.export.database.form;

public class PageForm {
    private String id;
    private String url;
    private String score;
    private String scoreLevel1;
    private String scoreLevel2;
    private String level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreLevel1() {
        return scoreLevel1;
    }

    public void setScoreLevel1(String scoreLevel1) {
        this.scoreLevel1 = scoreLevel1;
    }

    public String getScoreLevel2() {
        return scoreLevel2;
    }

    public void setScoreLevel2(String scoreLevel2) {
        this.scoreLevel2 = scoreLevel2;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
