package es.inteco.rastreador2.actionform.usuario;


import es.inteco.rastreador2.utils.ListadoUsuario;
import org.apache.struts.action.ActionForm;

import java.util.List;


public class CargarUsuariosSistemaForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String user;
    private String pass;
    private List<ListadoUsuario> listadoUsuarios;
    private int numUsuarios;


    public int getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(int numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public List<ListadoUsuario> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<ListadoUsuario> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
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


}