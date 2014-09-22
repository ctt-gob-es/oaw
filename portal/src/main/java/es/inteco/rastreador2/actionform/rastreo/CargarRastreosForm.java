package es.inteco.rastreador2.actionform.rastreo;


import es.inteco.rastreador2.utils.Rastreo;
import org.apache.struts.action.ActionForm;

import java.util.List;


public class CargarRastreosForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<String> rastreos;
    private String user;
    private String pass;
    private List<Rastreo> vr;
    private int num_rastreos;
    private int maxrastreos;
    private String cartucho;

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public int getMaxrastreos() {
        return maxrastreos;
    }

    public void setMaxrastreos(int maxrastreos) {
        this.maxrastreos = maxrastreos;
    }

    public int getNum_rastreos() {
        return num_rastreos;
    }

    public void setNum_rastreos(int num_rastreos) {
        this.num_rastreos = num_rastreos;
    }

    public List<Rastreo> getVr() {
        return vr;
    }

    public void setVr(List<Rastreo> vr) {
        this.vr = vr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRastreos() {
        return rastreos;
    }

    public void setRastreos(List<String> rastreos) {
        this.rastreos = rastreos;
    }


}