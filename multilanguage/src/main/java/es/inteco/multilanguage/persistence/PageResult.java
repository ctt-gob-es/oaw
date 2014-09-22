package es.inteco.multilanguage.persistence;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "export_page_result")
public class PageResult implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "language")
    private String language;

    @Column(name = "id_txt_language")
    private String id_txt_language;

    @Column(name = "translation", columnDefinition = "TINYINT(1)")
    private Boolean translation;

    @ManyToOne
    @JoinColumn(name = "id_page", nullable = false, referencedColumnName = "id")
    private Page page;

    public PageResult(String code, Language language, Language languageSusp) {
        this.code = code;
        this.language = language.getName();
        this.id_txt_language = languageSusp.getName();
        if (code != null && language.getCode() != null && code.matches(language.getCode()) && language.getId().equals(languageSusp.getId())) {
            this.translation = true;
        } else {
            this.translation = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getId_txt_language() {
        return id_txt_language;
    }

    public void setId_txt_language(String idTxtLanguage) {
        id_txt_language = idTxtLanguage;
    }

    public boolean isTranslation() {
        return translation;
    }

    public void setTranslation(boolean translation) {
        this.translation = translation;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


}
