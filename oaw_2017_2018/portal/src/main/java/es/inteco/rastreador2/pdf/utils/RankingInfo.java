package es.inteco.rastreador2.pdf.utils;

import es.inteco.rastreador2.actionform.semillas.CategoriaForm;

import java.math.BigDecimal;

public class RankingInfo {

    private BigDecimal score;
    private int globalRank;
    private int globalSeedsNumber;
    private int categoryRank;
    private int categorySeedsNumber;
    private CategoriaForm categoria;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public int getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(int globalRank) {
        this.globalRank = globalRank;
    }

    public void incrementGlobalRank() {
        this.globalRank++;
    }

    public int getGlobalSeedsNumber() {
        return globalSeedsNumber;
    }

    public void setGlobalSeedsNumber(int globalSeedsNumber) {
        this.globalSeedsNumber = globalSeedsNumber;
    }

    public int getCategoryRank() {
        return categoryRank;
    }

    public void setCategoryRank(int categoryRank) {
        this.categoryRank = categoryRank;
    }

    public void incrementCategoryRank() {
        this.categoryRank++;
    }

    public int getCategorySeedsNumber() {
        return categorySeedsNumber;
    }

    public void setCategorySeedsNumber(int categorySeedsNumber) {
        this.categorySeedsNumber = categorySeedsNumber;
    }

    public CategoriaForm getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
