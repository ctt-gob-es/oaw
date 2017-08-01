package es.inteco.intav.form;

import java.io.Serializable;

public class CategoryForm implements Serializable {
    private String name;
    private String acronym;
    private String dependence;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }
}
