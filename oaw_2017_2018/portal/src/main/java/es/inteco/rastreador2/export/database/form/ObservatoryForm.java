package es.inteco.rastreador2.export.database.form;

import java.util.List;

public class ObservatoryForm {
    private String idExecution;
    private String name;
    private String date;
    private String numAA;
    private String numA;
    private String numNV;
    private List<CategoryForm> categoryFormList;

    public String getIdExecution() {
        return idExecution;
    }

    public void setIdExecution(String idExecution) {
        this.idExecution = idExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumAA() {
        return numAA;
    }

    public void setNumAA(String numAA) {
        this.numAA = numAA;
    }

    public String getNumA() {
        return numA;
    }

    public void setNumA(String numA) {
        this.numA = numA;
    }

    public String getNumNV() {
        return numNV;
    }

    public void setNumNV(String numNV) {
        this.numNV = numNV;
    }

    public List<CategoryForm> getCategoryFormList() {
        return categoryFormList;
    }

    public void setCategoryFormList(List<CategoryForm> categoryFormList) {
        this.categoryFormList = categoryFormList;
    }

}
