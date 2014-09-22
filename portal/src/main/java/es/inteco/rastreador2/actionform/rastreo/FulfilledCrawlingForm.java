package es.inteco.rastreador2.actionform.rastreo;

import es.inteco.rastreador2.actionform.semillas.SemillaForm;

public class FulfilledCrawlingForm {
    private String id;
    private String idCrawling;
    private String user;
    private String date;
    private String idCartridge;
    private String cartridge;
    private SemillaForm seed;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCartridge() {
        return cartridge;
    }

    public void setCartridge(String cartridge) {
        this.cartridge = cartridge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(String idCrawling) {
        this.idCrawling = idCrawling;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIdCartridge() {
        return idCartridge;
    }

    public void setIdCartridge(String idCartridge) {
        this.idCartridge = idCartridge;
    }

    public SemillaForm getSeed() {
        return seed;
    }

    public void setSeed(SemillaForm seed) {
        this.seed = seed;
    }
}
