package es.inteco.multilanguage.persistence;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_category")
public class Category implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_category", nullable = false)
    private Long id_category;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(targetEntity = Results.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Results> results;

    @OneToMany(targetEntity = Site.class, mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Site> sites;

    @ManyToOne
    @JoinColumn(name = "id_observatory", nullable = false, referencedColumnName = "id")
    private Observatory observatory;

    public Category(Long idCategory) {
        this.id_category = idCategory;
        results = new ArrayList<Results>();
        sites = new ArrayList<Site>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_category() {
        return id_category;
    }

    public void setId_category(Long idCategory) {
        id_category = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public Observatory getObservatory() {
        return observatory;
    }

    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }
}
