package es.inteco.crawler.job;

public class CrawledLink {
    private String url;
    private String source;
    private String hash;
    private int numRetries;
    private int numRedirections;

    public CrawledLink(String url, String source, int numRetries, int numRedirections) {
        this.url = url;
        this.source = source;
        this.numRedirections = numRedirections;
        this.numRetries = numRetries;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }

    public int getNumRedirections() {
        return numRedirections;
    }

    public void setNumRedirections(int numRedirections) {
        this.numRedirections = numRedirections;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CrawledLink && ((CrawledLink) obj).getUrl().equals(this.url);
    }
}