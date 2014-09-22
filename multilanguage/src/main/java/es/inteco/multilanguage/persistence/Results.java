package es.inteco.multilanguage.persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "export_percentage")
public class Results implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_language")
    private Language language;

    @Column(name = "no_translation_pages")
    private BigDecimal no_translation_pages;

    @Column(name = "ok_translation_pages")
    private BigDecimal ok_translation_pages;

    @Column(name = "ko_translation_pages")
    private BigDecimal ko_translation_pages;

    @Column(name = "with_code")
    private BigDecimal with_code;

    @Column(name = "without_code")
    private BigDecimal without_code;

    @Column(name = "with_translation_txt")
    private BigDecimal with_translation_txt;

    @Column(name = "without_translation_txt")
    private BigDecimal without_translation_txt;

    @Column(name = "translation")
    private BigDecimal translation;

    @Column(name = "no_translation")
    private BigDecimal no_translation;

    @ManyToOne
    @JoinColumn(name = "id_observatory", nullable = true, referencedColumnName = "id")
    private Observatory observatory;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = true, referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = true, referencedColumnName = "id")
    private Site site;

    @Column(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Results(Language language, BigDecimal no_translation_pages, BigDecimal ok_translation_pages, BigDecimal ko_translation_pages,
                   BigDecimal with_code, BigDecimal without_code, BigDecimal with_translation, BigDecimal without_translation, BigDecimal translation,
                   BigDecimal no_translation, String type) {
        this.language = language;
        this.no_translation_pages = no_translation_pages;
        this.ok_translation_pages = ok_translation_pages;
        this.ko_translation_pages = ko_translation_pages;
        this.with_code = with_code;
        this.without_code = without_code;
        this.with_translation_txt = with_translation;
        this.without_translation_txt = without_translation;
        this.translation = translation;
        this.no_translation = no_translation;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public BigDecimal getNo_translation_pages() {
        return no_translation_pages;
    }

    public void setNo_translation_pages(BigDecimal noTranslationPages) {
        no_translation_pages = noTranslationPages;
    }

    public BigDecimal getOk_translation_pages() {
        return ok_translation_pages;
    }

    public void setOk_translation_pages(BigDecimal okTranslationPages) {
        ok_translation_pages = okTranslationPages;
    }

    public BigDecimal getKo_translation_pages() {
        return ko_translation_pages;
    }

    public void setKo_translation_pages(BigDecimal koTranslationPages) {
        ko_translation_pages = koTranslationPages;
    }

    public BigDecimal getWith_code() {
        return with_code;
    }

    public void setWith_code(BigDecimal withCode) {
        with_code = withCode;
    }

    public BigDecimal getWithout_code() {
        return without_code;
    }

    public void setWithout_code(BigDecimal withoutCode) {
        without_code = withoutCode;
    }

    public BigDecimal getWith_translation_txt() {
        return with_translation_txt;
    }

    public void setWith_translation_txt(BigDecimal withTranslationTxt) {
        with_translation_txt = withTranslationTxt;
    }

    public BigDecimal getWithout_translation_txt() {
        return without_translation_txt;
    }

    public void setWithout_translation_txt(BigDecimal withoutTranslationTxt) {
        without_translation_txt = withoutTranslationTxt;
    }

    public BigDecimal getTranslation() {
        return translation;
    }

    public void setTranslation(BigDecimal translation) {
        this.translation = translation;
    }

    public BigDecimal getNo_translation() {
        return no_translation;
    }

    public void setNo_translation(BigDecimal noTranslation) {
        no_translation = noTranslation;
    }

    public Observatory getObservatory() {
        return observatory;
    }

    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }


}
