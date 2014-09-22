package es.inteco.rastreador2.utils;

public class Rastreo {

    private String id_rastreo;
    private String codigo;
    private String codigoTitle;
    private String fecha;
    private String cartucho;
    private int estado;
    private String estadoTexto;
    private int profundidad = 0;
    private long id_cuenta;
    private long activo;
    private String pseudoAleatorio;

    public long getActivo() {
        return activo;
    }

    public void setActivo(long activo) {
        this.activo = activo;
    }

    public String getEstadoTexto() {
        return estadoTexto;
    }

    public void setEstadoTexto(String estadoTexto) {
        this.estadoTexto = estadoTexto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(String id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public long getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public String getPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(String pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public String getCodigoTitle() {
        return codigoTitle;
    }

    public void setCodigoTitle(String codigoTitle) {
        this.codigoTitle = codigoTitle;
    }
}
