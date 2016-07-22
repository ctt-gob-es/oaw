package es.inteco.rastreador2.utils;

import java.math.BigDecimal;

public class GraphicData {

    private String adecuationLevel;
    private String percentageP;
    private BigDecimal rawPercentage = BigDecimal.ZERO;
    private String numberP;

    public String getAdecuationLevel() {
        return adecuationLevel;
    }

    public void setAdecuationLevel(String adecuationLevel) {
        this.adecuationLevel = adecuationLevel;
    }

    public String getPercentageP() {
        return percentageP;
    }

    public void setPercentageP(String percentageP) {
        this.percentageP = percentageP;
    }

    public String getNumberP() {
        return numberP;
    }

    public void setNumberP(String numberP) {
        this.numberP = numberP;
    }

    public BigDecimal getRawPercentage() {
        return rawPercentage;
    }

    public void setRawPercentage(BigDecimal rawPercentage) {
        this.rawPercentage = rawPercentage;
    }
}
