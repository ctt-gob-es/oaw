package es.inteco.rastreador2.actionform.observatorio;

import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;

import java.sql.Timestamp;

public class ObservatorioForm {

    private long id;
    private String nombre;
    private long periodicidad;
    private int profundidad;
    private long amplitud;
    private Timestamp fecha_inicio;
    private long id_guideline;
    private long lenguaje;
    private PeriodicidadForm periodicidadForm;
    private String[] categoria;
    private CartuchoForm cartucho;
    private boolean pseudoAleatorio;
    private int estado;
    private long tipo;

    public long getTipo() {
        return tipo;
    }

    public void setTipo(long tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(long periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public Timestamp getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Timestamp timestamp) {
        this.fecha_inicio = timestamp;
    }

    public long getId_guideline() {
        return id_guideline;
    }

    public void setId_guideline(long id_guideline) {
        this.id_guideline = id_guideline;
    }

    public long getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(long amplitud) {
        this.amplitud = amplitud;
    }

    public long getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(long lenguaje) {
        this.lenguaje = lenguaje;
    }

    public PeriodicidadForm getPeriodicidadForm() {
        return periodicidadForm;
    }

    public void setPeriodicidadForm(PeriodicidadForm periodicidadForm) {
        this.periodicidadForm = periodicidadForm;
    }

    public CartuchoForm getCartucho() {
        return cartucho;
    }

    public void setCartucho(CartuchoForm cartucho) {
        this.cartucho = cartucho;
    }

    public boolean isPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(boolean pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public String[] getCategoria() {
        return categoria;
    }

    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }
}
