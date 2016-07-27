package es.inteco.multilanguage.persistence;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.form.AnalysisForm;
import org.apache.commons.beanutils.ConvertUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "analysis")
public class Analysis implements Serializable {

    static {
        ConvertUtils.register(new GenericConverter(), AnalysisForm.class);
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "id_crawling", nullable = false)
    private Long idCrawling;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "url", nullable = false, length = 750)
    private String url;

    @Column(name = "cod_fuente")
    private String content;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(targetEntity = LanguageFound.class, mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LanguageFound> languagesFound;

    public Analysis() {
        languagesFound = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(Long idCrawling) {
        this.idCrawling = idCrawling;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<LanguageFound> getLanguagesFound() {
        return languagesFound;
    }

    public void setLanguagesFound(List<LanguageFound> languagesFound) {
        this.languagesFound = languagesFound;
    }
}
