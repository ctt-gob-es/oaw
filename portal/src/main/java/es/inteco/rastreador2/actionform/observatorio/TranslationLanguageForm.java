package es.inteco.rastreador2.actionform.observatorio;

import java.math.BigDecimal;

public class TranslationLanguageForm {
    String name;
    BigDecimal noTranslationPercentage;
    BigDecimal correctTranslationPercentage;
    BigDecimal noCorrectTranslationPercentage;

    public TranslationLanguageForm() {
        this.correctTranslationPercentage = BigDecimal.ZERO;
        this.noCorrectTranslationPercentage = BigDecimal.ZERO;
        this.noTranslationPercentage = BigDecimal.ZERO;
    }

    public BigDecimal getNoTranslationPercentage() {
        return noTranslationPercentage;
    }

    public void setNoTranslationPercentage(BigDecimal noTranslationPercentage) {
        this.noTranslationPercentage = noTranslationPercentage;
    }

    public BigDecimal getCorrectTranslationPercentage() {
        return correctTranslationPercentage;
    }

    public void setCorrectTranslationPercentage(BigDecimal correctTranslationPercentage) {
        this.correctTranslationPercentage = correctTranslationPercentage;
    }

    public BigDecimal getNoCorrectTranslationPercentage() {
        return noCorrectTranslationPercentage;
    }

    public void setNoCorrectTranslationPercentage(BigDecimal noCorrectTranslationPercentage) {
        this.noCorrectTranslationPercentage = noCorrectTranslationPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
