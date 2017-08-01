package es.inteco.rastreador2.intav.form;

import org.apache.struts.util.LabelValueBean;

import java.math.BigDecimal;
import java.util.List;

public class ScoreForm {

    public ScoreForm() {
        totalScore = BigDecimal.ZERO;
        scoreLevel1 = BigDecimal.ZERO;
        scoreLevel2 = BigDecimal.ZERO;
        scoreLevelA = BigDecimal.ZERO;
        scoreLevelAA = BigDecimal.ZERO;
        suitabilityScore = BigDecimal.ZERO;
    }

    private String level;
    private BigDecimal totalScore;
    private BigDecimal scoreLevel1;
    private BigDecimal scoreLevel2;
    private BigDecimal scoreLevelA;
    private BigDecimal scoreLevelAA;
    private BigDecimal suitabilityScore;
    private List<LabelValueBean> verifications1;
    private List<LabelValueBean> verifications2;

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getScoreLevel1() {
        return scoreLevel1;
    }

    public void setScoreLevel1(BigDecimal scoreLevel1) {
        this.scoreLevel1 = scoreLevel1;
    }

    public BigDecimal getScoreLevel2() {
        return scoreLevel2;
    }

    public void setScoreLevel2(BigDecimal scoreLevel2) {
        this.scoreLevel2 = scoreLevel2;
    }

    public BigDecimal getScoreLevelA() {
        return scoreLevelA;
    }

    public void setScoreLevelA(BigDecimal scoreLevelA) {
        this.scoreLevelA = scoreLevelA;
    }

    public BigDecimal getScoreLevelAA() {
        return scoreLevelAA;
    }

    public void setScoreLevelAA(BigDecimal scoreLevelAA) {
        this.scoreLevelAA = scoreLevelAA;
    }

    public List<LabelValueBean> getVerifications1() {
        return verifications1;
    }

    public void setVerifications1(List<LabelValueBean> verifications1) {
        this.verifications1 = verifications1;
    }

    public List<LabelValueBean> getVerifications2() {
        return verifications2;
    }

    public void setVerifications2(List<LabelValueBean> verifications2) {
        this.verifications2 = verifications2;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getSuitabilityScore() {
        return suitabilityScore;
    }

    public void setSuitabilityScore(BigDecimal suitabilityScore) {
        this.suitabilityScore = suitabilityScore;
    }
}
