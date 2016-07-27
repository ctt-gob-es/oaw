package es.inteco.rastreador2.dao.export.database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_page")
public class Page {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "score", nullable = false)
    private BigDecimal score;

    @Column(name = "scoreLevel1", nullable = false)
    private BigDecimal scoreLevel1;

    @Column(name = "scoreLevel2", nullable = false)
    private BigDecimal scoreLevel2;

    @Column(name = "level", nullable = false)
    private String level;

    @ManyToOne
    @JoinColumn(name = "idSite", nullable = false, referencedColumnName = "id")
    private Site site;

    @OneToMany(targetEntity = AspectScore.class, mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    @OneToMany(targetEntity = VerificationPage.class, mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationPage> verificationPageList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public List<AspectScore> getAspectScoreList() {
        if (aspectScoreList == null) {
            aspectScoreList = new ArrayList<>();
        }
        return aspectScoreList;
    }

    public void setAspectScoreList(List<AspectScore> aspectScoreList) {
        this.aspectScoreList = aspectScoreList;
    }

    public List<VerificationPage> getVerificationPageList() {
        if (verificationPageList == null) {
            verificationPageList = new ArrayList<>();
        }
        return verificationPageList;
    }

    public void setVerificationPageList(List<VerificationPage> verificationPageList) {
        this.verificationPageList = verificationPageList;
    }
}
