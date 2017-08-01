package es.inteco.rastreador2.actionform.cuentausuario;


import org.apache.struts.action.ActionForm;

import java.util.List;


public class EliminarCuentaUsuarioForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private String dominio;
    private long idListaRastreable;
    private long idListaNoRastreable;
    private long idSemilla;
    private List<String> cartuchos;
    private List<String> responsable;
    private List<String> usuarios;
    private Long id;
    private String normaAnalisis;
    private String normaAnalisisSt;

    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public long getIdListaRastreable() {
        return idListaRastreable;
    }

    public void setIdListaRastreable(long idListaRastreable) {
        this.idListaRastreable = idListaRastreable;
    }

    public long getIdListaNoRastreable() {
        return idListaNoRastreable;
    }

    public void setIdListaNoRastreable(long idListaNoRastreable) {
        this.idListaNoRastreable = idListaNoRastreable;
    }

    public long getIdSemilla() {
        return idSemilla;
    }

    public void setIdSemilla(long idSemilla) {
        this.idSemilla = idSemilla;
    }

    public List<String> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public List<String> getResponsable() {
        return responsable;
    }

    public void setResponsable(List<String> responsable) {
        this.responsable = responsable;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

}