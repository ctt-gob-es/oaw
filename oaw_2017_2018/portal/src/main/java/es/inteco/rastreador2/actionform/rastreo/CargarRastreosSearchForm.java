package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

public class CargarRastreosSearchForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String name;
    private String active;
    private String cartridge;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCartridge() {
        return cartridge;
    }

    public void setCartridge(String cartridge) {
        this.cartridge = cartridge;
    }


}