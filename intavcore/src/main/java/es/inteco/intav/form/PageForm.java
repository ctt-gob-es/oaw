package es.inteco.intav.form;

public class PageForm {
    private String title;
    private String path;
    private String styleClass;
    private boolean active;

    public PageForm() {
        super();
    }

    public PageForm(String title, String path, String styleClass, boolean active) {
        this.title = title;
        this.path = path;
        this.styleClass = styleClass;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
