package es.inteco.multilanguage.form;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.persistence.LanguageFound;
import org.apache.commons.beanutils.ConvertUtils;

public class LanguageFoundForm {

    static {
        ConvertUtils.register(new GenericConverter(), LanguageFound.class);
    }

    private String id;
    private String href;
    private String hrefTitle;
    private boolean correct;
    private boolean correctTranslation;
    private boolean correctDeclaration;
    private LanguageForm language;
    private String declarationLang;
    private LanguageForm languageSuspected;
    private AnalysisForm analysis;
    private String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LanguageFoundForm() {
        this.language = new LanguageForm();
        this.languageSuspected = new LanguageForm();
        this.analysis = new AnalysisForm();
        this.correct = false;
        this.correctDeclaration = false;
        this.correctTranslation = false;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LanguageForm getLanguage() {
        return language;
    }

    public void setLanguage(LanguageForm language) {
        this.language = language;
    }

    public AnalysisForm getAnalysis() {
        return analysis;
    }

    public void setAnalysis(AnalysisForm analysis) {
        this.analysis = analysis;
    }

    public String getDeclarationLang() {
        return declarationLang;
    }

    public void setDeclarationLang(String declarationLang) {
        this.declarationLang = declarationLang;
    }

    public LanguageForm getLanguageSuspected() {
        return languageSuspected;
    }

    public void setLanguageSuspected(LanguageForm languageSuspected) {
        this.languageSuspected = languageSuspected;
    }

    public boolean isCorrectTranslation() {
        return correctTranslation;
    }

    public void setCorrectTranslation(boolean correctTranslation) {
        this.correctTranslation = correctTranslation;
    }

    public boolean isCorrectDeclaration() {
        return correctDeclaration;
    }

    public void setCorrectDeclaration(boolean correctDeclaration) {
        this.correctDeclaration = correctDeclaration;
    }

    public String getHrefTitle() {
        return hrefTitle;
    }

    public void setHrefTitle(String hrefTitle) {
        this.hrefTitle = hrefTitle;
    }
}
