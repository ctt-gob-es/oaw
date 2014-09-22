package es.inteco.rastreador2.actionform.comun;

import org.apache.struts.action.ActionForm;

public class CambiarPassUsuarioCForm extends ActionForm {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String username, password, password2, passwold, cancelButton;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPasswold() {
        return passwold;
    }

    public void setPasswold(String passwold) {
        this.passwold = passwold;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }


}