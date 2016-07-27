package es.inteco.multilanguage.bean;

import java.util.HashMap;

public class UrlConfiguration {
    private String url;
    private HashMap<Long, String> langUrls;

    public UrlConfiguration() {
        this.langUrls = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<Long, String> getLangUrls() {
        return langUrls;
    }

    public void setLangUrls(HashMap<Long, String> langUrls) {
        this.langUrls = langUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UrlConfiguration that = (UrlConfiguration) o;

        if (url != null ? !url.equals(that.url) : that.url != null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
