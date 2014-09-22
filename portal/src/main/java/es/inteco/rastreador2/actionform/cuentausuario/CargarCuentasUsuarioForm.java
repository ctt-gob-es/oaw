package es.inteco.rastreador2.actionform.cuentausuario;


import es.inteco.rastreador2.utils.ListadoCuentasUsuario;
import org.apache.struts.action.ActionForm;

import java.util.List;


public class CargarCuentasUsuarioForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String user;
    private String pass;
    private List<ListadoCuentasUsuario> listadoCuentasUsuario;
    private int numCuentasUsuario;

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

    public List<ListadoCuentasUsuario> getListadoCuentasUsuario() {
        return listadoCuentasUsuario;
    }

    public void setListadoCuentasUsuario(List<ListadoCuentasUsuario> listadoCuentasUsuario) {
        this.listadoCuentasUsuario = listadoCuentasUsuario;
    }

    public int getNumCuentasUsuario() {
        return numCuentasUsuario;
    }

    public void setNumCuentasUsuario(int numCuentasUsuario) {
        this.numCuentasUsuario = numCuentasUsuario;
    }
}