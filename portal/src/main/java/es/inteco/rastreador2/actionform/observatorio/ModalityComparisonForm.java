package es.inteco.rastreador2.actionform.observatorio;

public class ModalityComparisonForm {
    String verification;
    String redPercentage;
    String greenPercentage;

    public ModalityComparisonForm() {

    }

    public ModalityComparisonForm(String verification) {
        this.verification = verification;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
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
            if (!(obj instanceof ModalityComparisonForm)) {
                return false;
            } else {
                return ((ModalityComparisonForm) obj).getVerification().equals(this.verification);
            }
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
