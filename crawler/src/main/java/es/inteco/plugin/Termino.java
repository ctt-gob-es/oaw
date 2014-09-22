package es.inteco.plugin;

public class Termino {
    private long id;
    private String name;
    private float normalized_percentage;
    private long id_category;
    private String name_category;
    private float threshold;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId_category() {
        return id_category;
    }

    public void setId_category(long id_category) {
        this.id_category = id_category;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public float getNormalized_percentage() {
        return normalized_percentage;
    }

    public void setNormalized_percentage(float normalized_percentage) {
        this.normalized_percentage = normalized_percentage;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
