package es.inteco.intav.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObservatoryLevelForm implements Serializable {

    private String name;
    private BigDecimal score;
    private List<ObservatorySuitabilityForm> suitabilityGroups;

    public ObservatoryLevelForm() {
        this.suitabilityGroups = new ArrayList<>();
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<ObservatorySuitabilityForm> getSuitabilityGroups() {
        return suitabilityGroups;
    }

    public void setSuitabilityGroups(
            List<ObservatorySuitabilityForm> suitabilityGroups) {
        this.suitabilityGroups = suitabilityGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
