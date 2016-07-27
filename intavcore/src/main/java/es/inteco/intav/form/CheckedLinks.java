package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckedLinks implements Serializable {

    private List<String> checkedLinks;
    private List<String> brokenLinks;
    private List<String> availablelinks;

    public CheckedLinks() {
        brokenLinks = new ArrayList<>();
        availablelinks = new ArrayList<>();
        checkedLinks = new ArrayList<>();
    }

    public List<String> getBrokenLinks() {
        return brokenLinks;
    }

    public void setBrokenLinks(List<String> brokenLinks) {
        this.brokenLinks = brokenLinks;
    }

    public List<String> getAvailablelinks() {
        return availablelinks;
    }

    public void setAvailablelinks(List<String> availablelinks) {
        this.availablelinks = availablelinks;
    }

    public List<String> getCheckedLinks() {
        return checkedLinks;
    }

    public void setCheckedLinks(List<String> checkedLinks) {
        this.checkedLinks = checkedLinks;
    }
}
