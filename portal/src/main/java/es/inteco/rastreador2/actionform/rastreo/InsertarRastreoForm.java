package es.inteco.rastreador2.actionform.rastreo;


import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class InsertarRastreoForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String fecha;
    private String usuario;
    private String cartucho;
    private long lenguaje;
    private long id_semilla;
    private long id_lista_rastreable;
    private long id_lista_no_rastreable;
    private long idRastreableAntiguo;
    private long idNoRastreableAntiguo;
    private String listaRastreable;
    private String listaNoRastreable;
    private String normaAnalisis;
    private String normaAnalisisEnlaces;
    private Long id_observatorio;
    private boolean pseudoAleatorio;

    private int profundidad;
    private long topN;

    //nuevo mine
    private String hora;
    private List<LenguajeForm> lenguajeVector;
    private List<CartuchoForm> cartuchos;
    private List<NormaForm> normaVector;
    private String nombre_antiguo;
    private String rastreo;
    private long id_rastreo;
    private int id_cartucho;

    private String semilla;
    private String semillaBoton;
    private Long cuenta_cliente;
    private Long observatorio;

    private boolean isActive;
    private boolean exhaustive;
    private boolean inDirectory;

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public long getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(long id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public String getRastreo() {
        return rastreo;
    }

    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    public long getId_semilla() {
        return id_semilla;
    }

    public void setId_semilla(long id_semilla) {
        this.id_semilla = id_semilla;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public long getTopN() {
        return topN;
    }

    public void setTopN(long topN) {
        this.topN = topN;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<CartuchoForm> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<CartuchoForm> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
    }

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public String getSemilla() {
        return semilla;
    }

    public void setSemilla(String semilla) {
        this.semilla = semilla;
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

    public Long getCuenta_cliente() {
        return cuenta_cliente;
    }

    public void setCuenta_cliente(Long cuenta_cliente) {
        this.cuenta_cliente = cuenta_cliente;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public List<NormaForm> getNormaVector() {
        return normaVector;
    }

    public void setNormaVector(List<NormaForm> normaVector) {
        this.normaVector = normaVector;
    }

    public String getSemillaBoton() {
        return semillaBoton;
    }

    public void setSemillaBoton(String semillaBoton) {
        this.semillaBoton = semillaBoton;
    }

    public long getId_lista_rastreable() {
        return id_lista_rastreable;
    }

    public void setId_lista_rastreable(long id_lista_rastreable) {
        this.id_lista_rastreable = id_lista_rastreable;
    }

    public long getId_lista_no_rastreable() {
        return id_lista_no_rastreable;
    }

    public void setId_lista_no_rastreable(long id_lista_no_rastreable) {
        this.id_lista_no_rastreable = id_lista_no_rastreable;
    }

    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }

    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    public Long getId_observatorio() {
        return id_observatorio;
    }

    public void setId_observatorio(Long id_observatorio) {
        this.id_observatorio = id_observatorio;
    }

    public Long getObservatorio() {
        return observatorio;
    }

    public void setObservatorio(Long observatorio) {
        this.observatorio = observatorio;
    }

    public long getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(long lenguaje) {
        this.lenguaje = lenguaje;
    }

    public List<LenguajeForm> getLenguajeVector() {
        return lenguajeVector;
    }

    public void setLenguajeVector(List<LenguajeForm> lenguajeVector) {
        this.lenguajeVector = lenguajeVector;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.lenguajeVector == null) {
            this.lenguajeVector = new ArrayList<LenguajeForm>();
        }
        if (this.cartuchos == null) {
            this.cartuchos = new ArrayList<CartuchoForm>();
        }
        if (this.normaVector == null) {
            this.normaVector = new ArrayList<NormaForm>();
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

    public boolean isPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(boolean pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public boolean isExhaustive() {
        return exhaustive;
    }

    public void setExhaustive(boolean exhaustive) {
        this.exhaustive = exhaustive;
    }
}