package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

public class CargarRastreosRealizadosSearchForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String initial_date;
    private String final_date;
    private String cartridge;
    private String seed;

    public String getInitial_date() {
        return initial_date;
    }

    public void setInitial_date(String initial_date) {
        this.initial_date = initial_date;
    }

    public String getFinal_date() {
        return final_date;
    }

    public void setFinal_date(String final_date) {
        this.final_date = final_date;
    }

    public String getCartridge() {
        return cartridge;
    }

    public void setCartridge(String cartridge) {
        this.cartridge = cartridge;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}