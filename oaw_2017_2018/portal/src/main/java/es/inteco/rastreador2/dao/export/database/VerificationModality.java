package es.inteco.rastreador2.dao.export.database;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "export_verification_modality")
public class VerificationModality {

    public VerificationModality() {

    }

    public VerificationModality(String verification) {
        this.verification = verification;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "verification", nullable = false)
    private String verification;

    @Column(name = "failPercentage", nullable = false)
    private BigDecimal failPercentage;

    @Column(name = "passPercentage", nullable = false)
    private BigDecimal passPercentage;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getFailPercentage() {
        return failPercentage;
    }

    public void setFailPercentage(BigDecimal failPercentage) {
        this.failPercentage = failPercentage;
    }

    public BigDecimal getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(BigDecimal passPercentage) {
        this.passPercentage = passPercentage;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (!(obj instanceof VerificationModality)) {
                return false;
            } else {
                return ((VerificationModality) obj).getVerification().equals(this.verification);
            }
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
