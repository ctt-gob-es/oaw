package es.inteco.multilanguage.persistence;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.form.LanguageForm;
import org.apache.commons.beanutils.ConvertUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "languages")
public class Language implements Serializable {

    static {
        ConvertUtils.register(new GenericConverter(), LanguageForm.class);
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "to_analyze", nullable = false)
    private Boolean toAnalyze;

    @Column(name = "title", nullable = false)
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getToAnalyze() {
        return toAnalyze;
    }

    public void setToAnalyze(Boolean toAnalyze) {
        this.toAnalyze = toAnalyze;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Language language = (Language) o;

        if (id != null ? !id.equals(language.id) : language.id != null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
