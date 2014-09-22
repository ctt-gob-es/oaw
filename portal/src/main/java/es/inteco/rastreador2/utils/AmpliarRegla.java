package es.inteco.rastreador2.utils;

public class AmpliarRegla {
    private String codigo;
    private String nombre;
    private String valor;
    private float sosp_individual;
    private int ocurrencias;
    private float sosp_total;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getSosp_individual() {
        return sosp_individual;
    }

    public void setSosp_individual(float sosp_individual) {
        this.sosp_individual = sosp_individual;
    }

    public float getSosp_total() {
        return sosp_total;
    }

    public void setSosp_total(float sosp_total) {
        this.sosp_total = sosp_total;
    }

    public int getOcurrencias() {
        return ocurrencias;
    }

    public void setOcurrencias(int ocurrencias) {
        this.ocurrencias = ocurrencias;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
