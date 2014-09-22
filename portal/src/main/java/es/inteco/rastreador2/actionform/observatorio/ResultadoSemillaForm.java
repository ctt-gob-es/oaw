package es.inteco.rastreador2.actionform.observatorio;

public class ResultadoSemillaForm {
    private String id;
    private String name;
    private String idCrawling;
    private String idFulfilledCrawling;
    private boolean active;
    private String score;
    private long idCategory;


    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
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

    public String getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(String idCrawling) {
        this.idCrawling = idCrawling;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIdFulfilledCrawling() {
        return idFulfilledCrawling;
    }

    public void setIdFulfilledCrawling(String idFulfilledCrawling) {
        this.idFulfilledCrawling = idFulfilledCrawling;
    }
}
