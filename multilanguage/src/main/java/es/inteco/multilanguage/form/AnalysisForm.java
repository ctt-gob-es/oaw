package es.inteco.multilanguage.form;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.persistence.Analysis;
import org.apache.commons.beanutils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;


public class AnalysisForm {

    static {
        ConvertUtils.register(new GenericConverter(), Analysis.class);
    }

    private String id;
    private String idCrawling;
    private String date;
    private String url;
    private String urlTitle;
    private String content;
    private String status;
    private List<LanguageFoundForm> languagesFound;

    public AnalysisForm() {
        languagesFound = new ArrayList<LanguageFoundForm>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(String idCrawling) {
        this.idCrawling = idCrawling;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LanguageFoundForm> getLanguagesFound() {
        return languagesFound;
    }

    public void setLanguagesFound(List<LanguageFoundForm> languagesFound) {
        this.languagesFound = languagesFound;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }
}
