package es.inteco.rastreador2.actionform.usuario;

import org.apache.struts.validator.ValidatorForm;

public class ModificarUsuarioPassForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String password2;
    private String passwold;
    private String idUsuario;
    private String roleType;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

}
