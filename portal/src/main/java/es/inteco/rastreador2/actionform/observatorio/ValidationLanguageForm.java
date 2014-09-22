package es.inteco.rastreador2.actionform.observatorio;

public class ValidationLanguageForm {
    String language;
    String redPercentage;
    String greenPercentage;

    public ValidationLanguageForm() {

    }

    public ValidationLanguageForm(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRedPercentage() {
        return redPercentage;
    }

    public void setRedPercentage(String redPercentage) {
        this.redPercentage = redPercentage;
    }

    public String getGreenPercentage() {
        return greenPercentage;
    }

    public void setGreenPercentage(String greenPercentage) {
        this.greenPercentage = greenPercentage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (!(obj instanceof ValidationLanguageForm)) {
                return false;
            } else {
                return ((ValidationLanguageForm) obj).getLanguage().equals(this.language);
            }
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
