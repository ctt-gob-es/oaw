package es.inteco.crawler.sexista.modules.analisis.dto;

public class ResultsByUrlDto {

    public ResultsByUrlDto(String url, int numTerms) {
        this.url = url;
        this.numTerms = numTerms;
    }

    private String url;
    private int numTerms;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }
}
