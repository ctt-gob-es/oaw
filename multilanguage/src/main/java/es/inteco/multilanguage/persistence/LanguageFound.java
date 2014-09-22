package es.inteco.multilanguage.persistence;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.form.LanguageFoundForm;
import org.apache.commons.beanutils.ConvertUtils;
import org.w3c.dom.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "languages_found")
public class LanguageFound implements Serializable {

    static {
        ConvertUtils.register(new GenericConverter(), LanguageFoundForm.class);
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "href", nullable = false, length = 750)
    private String href;

    @Transient
    private String hrefTitle;

    @Column(name = "declaration_lang")
    private String declarationLang;

    @ManyToOne
    @JoinColumn(name = "id_language")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "id_analysis")
    private Analysis analysis;

    @ManyToOne
    @JoinColumn(name = "id_sus_language")
    private Language languageSuspected;

    @Column(name = "cod_fuente")
    private String content;

    @Transient
    private List<Language> possibleLanguages;

    @Transient
    private Document remoteDocument;


    public String getDeclarationLang() {
        return declarationLang;
    }

    public void setDeclarationLang(String declarationLang) {
        this.declarationLang = declarationLang;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguageSuspected() {
        return languageSuspected;
    }

    public void setLanguageSuspected(Language languageSuspected) {
        this.languageSuspected = languageSuspected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Language> getPossibleLanguages() {
        if (this.possibleLanguages == null) {
            this.possibleLanguages = new ArrayList<Language>();
        }
        return possibleLanguages;
    }

    public void setPossibleLanguages(List<Language> possibleLanguages) {
        this.possibleLanguages = possibleLanguages;
    }

    public String getHrefTitle() {
        return hrefTitle;
    }

    public void setHrefTitle(String hrefTitle) {
        this.hrefTitle = hrefTitle;
    }

    public Document getRemoteDocument() {
        return remoteDocument;
    }

    public void setRemoteDocument(Document remoteDocument) {
        this.remoteDocument = remoteDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LanguageFound that = (LanguageFound) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (language != null ? !language.equals(that.language) : that.language != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return language != null ? language.hashCode() : 0;
    }

}
