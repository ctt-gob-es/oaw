package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

import java.util.List;


public class VerRastreoForm extends ActionForm {


    private static final long serialVersionUID = 1L;
    private long id_rastreo;
    private int id_usuario;
    private String nombre_usuario;
    private long normaAnalisis;
    private String normaAnalisisSt;
    private int id_cartucho;
    private String nombre_cartucho;
    private List<String> url_semilla;
    private int profundidad;
    private String topN_ver;
    private String fecha;
    private String rastreo;
    private String fechaLanzado;
    private String listaRastreable;
    private List<String> url_listaRastreable;
    private String listaNoRastreable;
    private List<String> url_listaNoRastreable;
    private String cuentaCliente;
    private int automatico;
    private long activo;
    private String pseudoAleatorio;
    private boolean inDirectory;

    public String getCuentaCliente() {
        return cuentaCliente;
    }

    public void setCuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    public String getFechaLanzado() {
        return fechaLanzado;
    }

    public void setFechaLanzado(String fechaLanzado) {
        this.fechaLanzado = fechaLanzado;
    }

    public String getListaRastreable() {
        return listaRastreable;
    }

    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRastreo() {
        return rastreo;
    }

    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public String getNombre_cartucho() {
        return nombre_cartucho;
    }

    public void setNombre_cartucho(String nombre_cartucho) {
        this.nombre_cartucho = nombre_cartucho;
    }

    public List<String> getUrl_semilla() {
        return url_semilla;
    }

    public void setUrl_semilla(List<String> url_semilla) {
        this.url_semilla = url_semilla;
    }

    public long getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(long id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public long getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(long normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
    }

    public int getAutomatico() {
        return automatico;
    }

    public void setAutomatico(int automatico) {
        this.automatico = automatico;
    }

    public List<String> getUrl_listaRastreable() {
        return url_listaRastreable;
    }

    public void setUrl_listaRastreable(List<String> url_listaRastreable) {
        this.url_listaRastreable = url_listaRastreable;
    }

    public List<String> getUrl_listaNoRastreable() {
        return url_listaNoRastreable;
    }

    public void setUrl_listaNoRastreable(List<String> url_listaNoRastreable) {
        this.url_listaNoRastreable = url_listaNoRastreable;
    }

    public long getActivo() {
        return activo;
    }

    public void setActivo(long activo) {
        this.activo = activo;
    }

    public String getTopN_ver() {
        return topN_ver;
    }

    public void setTopN_ver(String topN_ver) {
        this.topN_ver = topN_ver;
    }

    public String getPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(String pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

}
