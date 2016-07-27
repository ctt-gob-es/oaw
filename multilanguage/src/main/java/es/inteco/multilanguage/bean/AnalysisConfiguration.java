package es.inteco.multilanguage.bean;

import java.util.ArrayList;
import java.util.List;

public class AnalysisConfiguration {
    private Integer type;
    private List<UrlConfiguration> urlConfigurations;

    public AnalysisConfiguration() {
        this.urlConfigurations = new ArrayList<>();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<UrlConfiguration> getUrlConfigurations() {
        return urlConfigurations;
    }

    public void setUrlConfigurations(List<UrlConfiguration> urlConfigurations) {
        this.urlConfigurations = urlConfigurations;
    }

    public UrlConfiguration getUrlConfiguration(String url) {
        for (UrlConfiguration urlConfiguration : this.urlConfigurations) {
            if (urlConfiguration.getUrl().equals(url)) {
                return urlConfiguration;
            }
        }
        return null;
    }

}
