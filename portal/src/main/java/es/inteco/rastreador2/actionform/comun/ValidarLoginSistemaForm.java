package es.inteco.rastreador2.actionform.comun;


import es.inteco.rastreador2.utils.CartuchoSeleccion;
import org.apache.struts.validator.ValidatorForm;

import java.util.List;


public class ValidarLoginSistemaForm extends ValidatorForm {
    private static final long serialVersionUID = 1L;
    private String loginUser;
    private String loginPass;
    private String tipo;
    private List<String> cartuchos;
    private List<String> aplicaciones;
    private List<CartuchoSeleccion> vcs;
    private int numCartuchos;
    private String cartucho;
    private String proyecto;

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public int getNumCartuchos() {
        return numCartuchos;
    }

    public void setNumCartuchos(int numCartuchos) {
        this.numCartuchos = numCartuchos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public List<String> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public List<String> getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(List<String> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    public List<CartuchoSeleccion> getVcs() {
        return vcs;
    }

    public void setVcs(List<CartuchoSeleccion> vcs) {
        this.vcs = vcs;
    }


}