package es.inteco.rastreador2.dao.export.database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_observatory")
public class Observatory {
    @Id
    @Column(name = "idExecution", nullable = false)
    private Long idExecution;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "numAA", nullable = false)
    private int numAA;

    @Column(name = "numA", nullable = false)
    private int numA;

    @Column(name = "numNV", nullable = false)
    private int numNV;

    @OneToMany(targetEntity = VerificationModality.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationModality> verificationModalityList;

    @OneToMany(targetEntity = VerificationScore.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationScore> verificationScoreList;

    @OneToMany(targetEntity = AspectScore.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    @OneToMany(targetEntity = Category.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Category> categoryList;

    public Long getIdExecution() {
        return idExecution;
    }

    public void setIdExecution(Long idExecution) {
        this.idExecution = idExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

    public List<VerificationModality> getVerificationModalityList() {
        if (verificationModalityList == null) {
            verificationModalityList = new ArrayList<VerificationModality>();
        }
        return verificationModalityList;
    }

    public void setVerificationModalityList(
            List<VerificationModality> verificationModalityList) {
        this.verificationModalityList = verificationModalityList;
    }

    public List<VerificationScore> getVerificationScoreList() {
        if (verificationScoreList == null) {
            verificationScoreList = new ArrayList<VerificationScore>();
        }
        return verificationScoreList;
    }

    public void setVerificationScoreList(
            List<VerificationScore> verificationScoreList) {
        this.verificationScoreList = verificationScoreList;
    }

    public List<AspectScore> getAspectScoreList() {
        if (aspectScoreList == null) {
            aspectScoreList = new ArrayList<AspectScore>();
        }
        return aspectScoreList;
    }

    public void setAspectScoreList(List<AspectScore> aspectScoreList) {
        this.aspectScoreList = aspectScoreList;
    }

    public List<Category> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<Category>();
        }
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
