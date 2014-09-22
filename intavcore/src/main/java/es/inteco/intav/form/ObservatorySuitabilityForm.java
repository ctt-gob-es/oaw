package es.inteco.intav.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObservatorySuitabilityForm implements Serializable {
    private String name;
    private BigDecimal score;
    private List<ObservatorySubgroupForm> subgroups;

    public ObservatorySuitabilityForm() {
        this.subgroups = new ArrayList<ObservatorySubgroupForm>();
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<ObservatorySubgroupForm> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(List<ObservatorySubgroupForm> subgroups) {
        this.subgroups = subgroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
