package es.inteco.multilanguage.persistence;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_site")
public class Site implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_site", nullable = false)
    private Long id_site;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(targetEntity = Results.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Results> results;

    @OneToMany(targetEntity = Page.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Page> pages;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false, referencedColumnName = "id")
    private Category category;

    public Site(Long idCrawling) {
        this.id_site = idCrawling;
        results = new ArrayList<Results>();
        pages = new ArrayList<Page>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_site() {
        return id_site;
    }

    public void setId_site(Long idSite) {
        id_site = idSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }


}
