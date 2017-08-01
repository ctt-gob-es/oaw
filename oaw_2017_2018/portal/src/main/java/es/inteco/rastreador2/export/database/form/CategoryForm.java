package es.inteco.rastreador2.export.database.form;

import java.util.List;

public class CategoryForm {
    private String id;
    private String name;
    private String numAA;
    private String numA;
    private String numNV;
    private String scoreAA;
    private String scoreA;
    private String scoreNV;
    private List<SiteForm> siteFormList;
    private String idCrawlerCategory;

    public String getIdCrawlerCategory() {
        return idCrawlerCategory;
    }

    public void setIdCrawlerCategory(String idCrawlerCategory) {
        this.idCrawlerCategory = idCrawlerCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumAA() {
        return numAA;
    }

    public void setNumAA(String numAA) {
        this.numAA = numAA;
    }

    public String getNumA() {
        return numA;
    }

    public void setNumA(String numA) {
        this.numA = numA;
    }

    public String getNumNV() {
        return numNV;
    }

    public void setNumNV(String numNV) {
        this.numNV = numNV;
    }

    public String getScoreAA() {
        return scoreAA;
    }

    public void setScoreAA(String scoreAA) {
        this.scoreAA = scoreAA;
    }

    public String getScoreA() {
        return scoreA;
    }

    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    public String getScoreNV() {
        return scoreNV;
    }

    public void setScoreNV(String scoreNV) {
        this.scoreNV = scoreNV;
    }

    public List<SiteForm> getSiteFormList() {
        return siteFormList;
    }

    public void setSiteFormList(List<SiteForm> siteFormList) {
        this.siteFormList = siteFormList;
    }
}
