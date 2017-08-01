package es.inteco.intav.comun;

public class Incidencia {

    private int codigoIncidencia;
    private int codigoComprobacion;
    private long codigoAnalisis;
    private int codigoLineaFuente;
    private int codigoColumnaFuente;
    private String codigoFuente;

    public int getCodigoIncidencia() {
        return codigoIncidencia;
    }

    public void setCodigoIncidencia(int codigoIncidencia) {
        this.codigoIncidencia = codigoIncidencia;
    }

    public int getCodigoComprobacion() {
        return codigoComprobacion;
    }

    public void setCodigoComprobacion(int codigoComprobacion) {
        this.codigoComprobacion = codigoComprobacion;
    }

    public int getCodigoLineaFuente() {
        return codigoLineaFuente;
    }

    public void setCodigoLineaFuente(int codigoLineaFuente) {
        this.codigoLineaFuente = codigoLineaFuente;
    }

    public int getCodigoColumnaFuente() {
        return codigoColumnaFuente;
    }

    public void setCodigoColumnaFuente(int codigoColumnaFuente) {
        this.codigoColumnaFuente = codigoColumnaFuente;
    }

    public String getCodigoFuente() {
        return codigoFuente;
    }

    public void setCodigoFuente(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    public long getCodigoAnalisis() {
        return codigoAnalisis;
    }

    public void setCodigoAnalisis(long codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }
}
