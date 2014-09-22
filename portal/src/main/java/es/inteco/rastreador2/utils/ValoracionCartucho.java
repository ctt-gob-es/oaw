package es.inteco.rastreador2.utils;

public class ValoracionCartucho {
    private String c_rastreo;
    private int id_rastreo;
    private String url;
    private String url_trunc;
    private String fecha;
    private float nivel_sospecha;
    private String observaciones;


    public int getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(int id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getNivel_sospecha() {
        return nivel_sospecha;
    }

    public void setNivel_sospecha(float nivel_sospecha) {
        this.nivel_sospecha = nivel_sospecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getC_rastreo() {
        return c_rastreo;
    }

    public void setC_rastreo(String c_rastreo) {
        this.c_rastreo = c_rastreo;
    }

    public String getUrl_trunc() {
        return url_trunc;
    }

    public void setUrl_trunc(String url_trunc) {
        this.url_trunc = url_trunc;
    }

}
