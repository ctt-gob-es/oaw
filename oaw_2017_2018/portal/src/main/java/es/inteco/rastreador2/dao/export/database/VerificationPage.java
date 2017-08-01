package es.inteco.rastreador2.dao.export.database;

import javax.persistence.*;

@Entity
@Table(name = "export_verification_page")
public class VerificationPage {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "verification", nullable = false)
    private String verification;

    @Column(name = "value")
    private Integer value;

    @Column(name = "modality", nullable = false)
    private String modality;

    @ManyToOne
    @JoinColumn(name = "idPage", referencedColumnName = "id")
    private Page page;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
