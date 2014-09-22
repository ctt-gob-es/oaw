package es.inteco.intav.form;

import java.io.Serializable;
import java.math.BigDecimal;

public class AspectScoreForm implements Serializable {
    private String name;
    private BigDecimal score;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
