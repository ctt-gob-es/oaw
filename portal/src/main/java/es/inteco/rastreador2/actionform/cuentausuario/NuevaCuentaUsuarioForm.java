package es.inteco.rastreador2.actionform.cuentausuario;


import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevaCuentaUsuarioForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String id_cuenta;
    private String nombre;
    private String dominio;
    private long idSeed;
    private long idCrawlableList;
    private long idNoCrawlableList;
    private String normaAnalisis;
    private String normaAnalisisEnlaces;
    private String listaRastreable;
    private String listaNoRastreable;
    private String[] cartuchosSelected;
    private List<CartuchoForm> cartuchos;
    private String periodicidad;
    private List<LenguajeForm> lenguajeVector;
    private long lenguaje;
    private List<PeriodicidadForm> periodicidadVector;
    private String profundidad;
    private String amplitud;
    private String fechaInicio;
    private String horaInicio;
    private String minutoInicio;
    private Date fecha;
    private boolean pseudoAleatorio;
    private boolean activo;
    private boolean inDirectory;

    public String getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(String id_cuenta) {
        this.id_cuenta = id_cuenta;
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

    public String[] getCartuchosSelected() {
        return cartuchosSelected;
    }

    public void setCartuchosSelected(String[] cartuchosSelected) {
        this.cartuchosSelected = cartuchosSelected;
    }

    public List<CartuchoForm> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<CartuchoForm> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(String amplitud) {
        this.amplitud = amplitud;
    }

    public List<PeriodicidadForm> getPeriodicidadVector() {
        return periodicidadVector;
    }

    public void setPeriodicidadVector(List<PeriodicidadForm> periodicidadVector) {
        this.periodicidadVector = periodicidadVector;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(String minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public long getIdSeed() {
        return idSeed;
    }

    public void setIdSeed(long idSeed) {
        this.idSeed = idSeed;
    }

    public long getIdCrawlableList() {
        return idCrawlableList;
    }

    public void setIdCrawlableList(long idCrawlableList) {
        this.idCrawlableList = idCrawlableList;
    }

    public long getIdNoCrawlableList() {
        return idNoCrawlableList;
    }

    public void setIdNoCrawlableList(long idNoCrawlableList) {
        this.idNoCrawlableList = idNoCrawlableList;
    }

    public boolean isPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(boolean pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public List<LenguajeForm> getLenguajeVector() {
        return lenguajeVector;
    }

    public void setLenguajeVector(List<LenguajeForm> lenguajeVector) {
        this.lenguajeVector = lenguajeVector;
    }

    public long getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(long lenguaje) {
        this.lenguaje = lenguaje;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.cartuchos == null) {
            this.cartuchos = new ArrayList<>();
        }
        if (this.periodicidadVector == null) {
            this.periodicidadVector = new ArrayList<>();
        }
        if (this.lenguajeVector == null) {
            this.lenguajeVector = new ArrayList<>();
        }
        if (this.normaAnalisisEnlaces == null) {
            this.normaAnalisisEnlaces = "1";
        }
    }

    public String getNormaAnalisisEnlaces() {
        return normaAnalisisEnlaces;
    }

    public void setNormaAnalisisEnlaces(String normaAnalisisEnlaces) {
        this.normaAnalisisEnlaces = normaAnalisisEnlaces;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }
}