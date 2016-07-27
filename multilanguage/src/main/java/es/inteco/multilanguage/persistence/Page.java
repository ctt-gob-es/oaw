package es.inteco.multilanguage.persistence;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_page")
public class Page implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_page", nullable = false)
    private Long id_page;

    @Column(name = "name", nullable = false, length = 750)
    private String name;

    @OneToMany(targetEntity = PageResult.class, mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PageResult> results;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = false, referencedColumnName = "id")
    private Site site;

    public Page(Long id_page, String name) {
        this.id_page = id_page;
        this.name = name;
        results = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_page() {
        return id_page;
    }

    public void setId_page(Long idPage) {
        id_page = idPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<PageResult> getResults() {
        return results;
    }

    public void setResults(List<PageResult> results) {
        this.results = results;
    }


}
