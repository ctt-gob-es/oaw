package es.inteco.rastreador2.dao.export.database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_category")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "numAA", nullable = false)
    private int numAA;

    @Column(name = "numA", nullable = false)
    private int numA;

    @Column(name = "numNV", nullable = false)
    private int numNV;

    @Column(name = "scoreAA", nullable = false)
    private BigDecimal scoreAA;

    @Column(name = "scoreA", nullable = false)
    private BigDecimal scoreA;

    @Column(name = "scoreNV", nullable = false)
    private BigDecimal scoreNV;

    @ManyToOne
    @JoinColumn(name = "idExecution", nullable = false, referencedColumnName = "idExecution")
    private Observatory observatory;

    @OneToMany(targetEntity = VerificationModality.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationModality> verificationModalityList;

    @OneToMany(targetEntity = VerificationScore.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationScore> verificationScoreList;

    @OneToMany(targetEntity = AspectScore.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    @OneToMany(targetEntity = Site.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Site> siteList;

    @Column(name = "idCrawlerCategory")
    private Long idCrawlerCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Observatory getObservatory() {
        return observatory;
    }

    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }

    public int getNumAA() {
        return numAA;
    }

    public void setNumAA(int numAA) {
        this.numAA = numAA;
    }

    public int getNumA() {
        return numA;
    }

    public void setNumA(int numA) {
        this.numA = numA;
    }

    public int getNumNV() {
        return numNV;
    }

    public void setNumNV(int numNV) {
        this.numNV = numNV;
    }

    public BigDecimal getScoreAA() {
        return scoreAA;
    }

    public void setScoreAA(BigDecimal scoreAA) {
        this.scoreAA = scoreAA;
    }

    public BigDecimal getScoreA() {
        return scoreA;
    }

    public void setScoreA(BigDecimal scoreA) {
        this.scoreA = scoreA;
    }

    public BigDecimal getScoreNV() {
        return scoreNV;
    }

    public void setScoreNV(BigDecimal scoreNV) {
        this.scoreNV = scoreNV;
    }

    public List<VerificationModality> getVerificationModalityList() {
        if (verificationModalityList == null) {
            verificationModalityList = new ArrayList<>();
        }
        return verificationModalityList;
    }

    public void setVerificationModalityList(
            List<VerificationModality> verificationModalityList) {
        this.verificationModalityList = verificationModalityList;
    }

    public List<VerificationScore> getVerificationScoreList() {
        if (verificationScoreList == null) {
            verificationScoreList = new ArrayList<>();
        }
        return verificationScoreList;
    }

    public void setVerificationScoreList(
            List<VerificationScore> verificationScoreList) {
        this.verificationScoreList = verificationScoreList;
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

    public List<Site> getSiteList() {
        if (siteList == null) {
            siteList = new ArrayList<>();
        }
        return siteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    public Long getIdCrawlerCategory() {
        return idCrawlerCategory;
    }

    public void setIdCrawlerCategory(Long idCrawlerCategory) {
        this.idCrawlerCategory = idCrawlerCategory;
    }
}
