package es.inteco.multilanguage.database.export;

import java.math.BigDecimal;

public class SiteTranslationInformationForm {
    String name;
    String type;
    BigDecimal noTranslationPercentage;
    BigDecimal correctTranslationPercentage;
    BigDecimal noCorrectTranslationPercentage;
    BigDecimal declarationRed;
    BigDecimal declarationGreen;
    BigDecimal textTranslationRed;
    BigDecimal textTranslationGreen;
    BigDecimal translationRed;
    BigDecimal translationGreen;
    int siteNumber;

    public SiteTranslationInformationForm() {
        this.correctTranslationPercentage = BigDecimal.ZERO;
        this.noCorrectTranslationPercentage = BigDecimal.ZERO;
        this.noTranslationPercentage = BigDecimal.ZERO;
        this.declarationGreen = BigDecimal.ZERO;
        this.declarationRed = BigDecimal.ZERO;
        this.textTranslationGreen = BigDecimal.ZERO;
        this.textTranslationRed = BigDecimal.ZERO;
        this.translationGreen = BigDecimal.ZERO;
        this.translationRed = BigDecimal.ZERO;
        siteNumber = 0;
    }

    public SiteTranslationInformationForm(String name, BigDecimal correctTranslationPercentage, BigDecimal noCorrectTranslationPercentage, BigDecimal noTranslationPercentage,
                                          BigDecimal declarationGreen, BigDecimal declarationRed, BigDecimal textTranslationGreen, BigDecimal textTranslationRed, BigDecimal translationGreen,
                                          BigDecimal translationRed, String type) {
        this.name = name;
        this.correctTranslationPercentage = correctTranslationPercentage;
        this.noCorrectTranslationPercentage = noCorrectTranslationPercentage;
        this.noTranslationPercentage = noTranslationPercentage;
        this.declarationGreen = declarationGreen;
        this.declarationRed = declarationRed;
        this.textTranslationGreen = textTranslationGreen;
        this.textTranslationRed = textTranslationRed;
        this.translationGreen = translationGreen;
        this.translationRed = translationRed;
        this.type = type;
        siteNumber = 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void incrementsiteNumber() {
        siteNumber++;
    }

    public void addInfo(BigDecimal correctTranslationPercentage, BigDecimal noCorrectTranslationPercentage, BigDecimal noTranslationPercentage,
                        BigDecimal declarationGreen, BigDecimal declarationRed, BigDecimal textTranslationGreen, BigDecimal textTranslationRed, BigDecimal translationGreen, BigDecimal translationRed) {
        this.correctTranslationPercentage = this.correctTranslationPercentage.add(correctTranslationPercentage);
        this.noCorrectTranslationPercentage = this.noCorrectTranslationPercentage.add(noCorrectTranslationPercentage);
        this.noTranslationPercentage = this.noTranslationPercentage.add(noTranslationPercentage);
        this.declarationGreen = this.declarationGreen.add(declarationGreen);
        this.declarationRed = this.declarationRed.add(declarationRed);
        this.textTranslationGreen = this.textTranslationGreen.add(textTranslationGreen);
        this.textTranslationRed = this.textTranslationRed.add(textTranslationRed);
        this.translationGreen = this.translationGreen.add(translationGreen);
        this.translationRed = this.translationRed.add(translationRed);
    }

    public int getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(int siteNumber) {
        this.siteNumber = siteNumber;
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

    public BigDecimal getDeclarationRed() {
        return declarationRed;
    }

    public void setDeclarationRed(BigDecimal declarationRed) {
        this.declarationRed = declarationRed;
    }

    public BigDecimal getDeclarationGreen() {
        return declarationGreen;
    }

    public void setDeclarationGreen(BigDecimal declarationGreen) {
        this.declarationGreen = declarationGreen;
    }

    public BigDecimal getTextTranslationRed() {
        return textTranslationRed;
    }

    public void setTextTranslationRed(BigDecimal textTranslationRed) {
        this.textTranslationRed = textTranslationRed;
    }

    public BigDecimal getTextTranslationGreen() {
        return textTranslationGreen;
    }

    public void setTextTranslationGreen(BigDecimal textTranslationGreen) {
        this.textTranslationGreen = textTranslationGreen;
    }

    public BigDecimal getTranslationRed() {
        return translationRed;
    }

    public void setTranslationRed(BigDecimal translationRed) {
        this.translationRed = translationRed;
    }

    public BigDecimal getTranslationGreen() {
        return translationGreen;
    }

    public void setTranslationGreen(BigDecimal translationGreen) {
        this.translationGreen = translationGreen;
    }
}
