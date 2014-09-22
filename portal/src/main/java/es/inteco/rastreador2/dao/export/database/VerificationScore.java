package es.inteco.rastreador2.dao.export.database;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "export_verification_score")
public class VerificationScore {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "verification", nullable = false)
    private String verification;

    @Column(name = "score")
    private BigDecimal score;

    @ManyToOne
    @JoinColumn(name = "idExecution", referencedColumnName = "idExecution")
    private Observatory observatory;

    @ManyToOne
    @JoinColumn(name = "idCategory", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "idSite", referencedColumnName = "id")
    private Site site;

    public Observatory getObservatory() {
        return observatory;
    }

    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
