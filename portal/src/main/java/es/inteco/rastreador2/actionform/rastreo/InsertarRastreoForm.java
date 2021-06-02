/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.actionform.rastreo;


import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class InsertarRastreoForm.
 */
public class InsertarRastreoForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The codigo. */
    private String codigo;
    
    /** The fecha. */
    private String fecha;
    
    /** The usuario. */
    private String usuario;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The lenguaje. */
    private long lenguaje;
    
    /** The id semilla. */
    private long id_semilla;
    
    /** The id lista rastreable. */
    private long id_lista_rastreable;
    
    /** The id lista no rastreable. */
    private long id_lista_no_rastreable;
    
    /** The id rastreable antiguo. */
    private long idRastreableAntiguo;
    
    /** The id no rastreable antiguo. */
    private long idNoRastreableAntiguo;
    
    /** The lista rastreable. */
    private String listaRastreable;
    
    /** The lista no rastreable. */
    private String listaNoRastreable;
    
    /** The norma analisis. */
    private String normaAnalisis;
    
    /** The norma analisis enlaces. */
    private String normaAnalisisEnlaces;
    
    /** The id observatorio. */
    private Long id_observatorio;
    
    /** The pseudo aleatorio. */
    private boolean pseudoAleatorio;

    /** The profundidad. */
    private int profundidad;
    
    /** The top N. */
    private long topN;

    /** The hora. */
    //nuevo mine
    private String hora;
    
    /** The lenguaje vector. */
    private List<LenguajeForm> lenguajeVector;
    
    /** The cartuchos. */
    private List<CartuchoForm> cartuchos;
    
    /** The norma vector. */
    private List<NormaForm> normaVector;
    
    /** The nombre antiguo. */
    private String nombre_antiguo;
    
    /** The rastreo. */
    private String rastreo;
    
    /** The id rastreo. */
    private long id_rastreo;
    
    /** The id cartucho. */
    private int id_cartucho;

    /** The semilla. */
    private String semilla;
    
    /** The semilla boton. */
    private String semillaBoton;
    
    /** The cuenta cliente. */
    private Long cuenta_cliente;
    
    /** The observatorio. */
    private Long observatorio;

    /** The is active. */
    private boolean isActive;
    
    /** The exhaustive. */
    private boolean exhaustive;
    
    /** The in directory. */
    private boolean inDirectory;

    /**
	 * Checks if is in directory.
	 *
	 * @return true, if is in directory
	 */
    public boolean isInDirectory() {
        return inDirectory;
    }

    /**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

    /**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
    public boolean isActive() {
        return isActive;
    }

    /**
	 * Sets the active.
	 *
	 * @param isActive the new active
	 */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
	 * Gets the id rastreo.
	 *
	 * @return the id rastreo
	 */
    public long getId_rastreo() {
        return id_rastreo;
    }

    /**
	 * Sets the id rastreo.
	 *
	 * @param id_rastreo the new id rastreo
	 */
    public void setId_rastreo(long id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    /**
	 * Gets the rastreo.
	 *
	 * @return the rastreo
	 */
    public String getRastreo() {
        return rastreo;
    }

    /**
	 * Sets the rastreo.
	 *
	 * @param rastreo the new rastreo
	 */
    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    /**
	 * Gets the id semilla.
	 *
	 * @return the id semilla
	 */
    public long getId_semilla() {
        return id_semilla;
    }

    /**
	 * Sets the id semilla.
	 *
	 * @param id_semilla the new id semilla
	 */
    public void setId_semilla(long id_semilla) {
        this.id_semilla = id_semilla;
    }

    /**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
    public int getProfundidad() {
        return profundidad;
    }

    /**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    /**
	 * Gets the top N.
	 *
	 * @return the top N
	 */
    public long getTopN() {
        return topN;
    }

    /**
	 * Sets the top N.
	 *
	 * @param topN the new top N
	 */
    public void setTopN(long topN) {
        this.topN = topN;
    }

    /**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
    public String getCodigo() {
        return codigo;
    }

    /**
	 * Sets the codigo.
	 *
	 * @param codigo the new codigo
	 */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
    public String getFecha() {
        return fecha;
    }

    /**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
	 * Gets the usuario.
	 *
	 * @return the usuario
	 */
    public String getUsuario() {
        return usuario;
    }

    /**
	 * Sets the usuario.
	 *
	 * @param usuario the new usuario
	 */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public String getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    /**
	 * Gets the hora.
	 *
	 * @return the hora
	 */
    public String getHora() {
        return hora;
    }

    /**
	 * Sets the hora.
	 *
	 * @param hora the new hora
	 */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public List<CartuchoForm> getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(List<CartuchoForm> cartuchos) {
        this.cartuchos = cartuchos;
    }

    /**
	 * Gets the serial version UID.
	 *
	 * @return the serial version UID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
	 * Gets the nombre antiguo.
	 *
	 * @return the nombre antiguo
	 */
    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    /**
	 * Sets the nombre antiguo.
	 *
	 * @param nombre_antiguo the new nombre antiguo
	 */
    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
    }

    /**
	 * Gets the id cartucho.
	 *
	 * @return the id cartucho
	 */
    public int getId_cartucho() {
        return id_cartucho;
    }

    /**
	 * Sets the id cartucho.
	 *
	 * @param id_cartucho the new id cartucho
	 */
    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    /**
	 * Gets the semilla.
	 *
	 * @return the semilla
	 */
    public String getSemilla() {
        return semilla;
    }

    /**
	 * Sets the semilla.
	 *
	 * @param semilla the new semilla
	 */
    public void setSemilla(String semilla) {
        this.semilla = semilla;
    }

    /**
	 * Gets the lista rastreable.
	 *
	 * @return the lista rastreable
	 */
    public String getListaRastreable() {
        return listaRastreable;
    }

    /**
	 * Sets the lista rastreable.
	 *
	 * @param listaRastreable the new lista rastreable
	 */
    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    /**
	 * Gets the lista no rastreable.
	 *
	 * @return the lista no rastreable
	 */
    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    /**
	 * Sets the lista no rastreable.
	 *
	 * @param listaNoRastreable the new lista no rastreable
	 */
    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    /**
	 * Gets the cuenta cliente.
	 *
	 * @return the cuenta cliente
	 */
    public Long getCuenta_cliente() {
        return cuenta_cliente;
    }

    /**
	 * Sets the cuenta cliente.
	 *
	 * @param cuenta_cliente the new cuenta cliente
	 */
    public void setCuenta_cliente(Long cuenta_cliente) {
        this.cuenta_cliente = cuenta_cliente;
    }

    /**
	 * Gets the norma analisis.
	 *
	 * @return the norma analisis
	 */
    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    /**
	 * Sets the norma analisis.
	 *
	 * @param normaAnalisis the new norma analisis
	 */
    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    /**
	 * Gets the norma vector.
	 *
	 * @return the norma vector
	 */
    public List<NormaForm> getNormaVector() {
        return normaVector;
    }

    /**
	 * Sets the norma vector.
	 *
	 * @param normaVector the new norma vector
	 */
    public void setNormaVector(List<NormaForm> normaVector) {
        this.normaVector = normaVector;
    }

    /**
	 * Gets the semilla boton.
	 *
	 * @return the semilla boton
	 */
    public String getSemillaBoton() {
        return semillaBoton;
    }

    /**
	 * Sets the semilla boton.
	 *
	 * @param semillaBoton the new semilla boton
	 */
    public void setSemillaBoton(String semillaBoton) {
        this.semillaBoton = semillaBoton;
    }

    /**
	 * Gets the id lista rastreable.
	 *
	 * @return the id lista rastreable
	 */
    public long getId_lista_rastreable() {
        return id_lista_rastreable;
    }

    /**
	 * Sets the id lista rastreable.
	 *
	 * @param id_lista_rastreable the new id lista rastreable
	 */
    public void setId_lista_rastreable(long id_lista_rastreable) {
        this.id_lista_rastreable = id_lista_rastreable;
    }

    /**
	 * Gets the id lista no rastreable.
	 *
	 * @return the id lista no rastreable
	 */
    public long getId_lista_no_rastreable() {
        return id_lista_no_rastreable;
    }

    /**
	 * Sets the id lista no rastreable.
	 *
	 * @param id_lista_no_rastreable the new id lista no rastreable
	 */
    public void setId_lista_no_rastreable(long id_lista_no_rastreable) {
        this.id_lista_no_rastreable = id_lista_no_rastreable;
    }

    /**
	 * Gets the id no rastreable antiguo.
	 *
	 * @return the id no rastreable antiguo
	 */
    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    /**
	 * Sets the id no rastreable antiguo.
	 *
	 * @param idNoRastreableAntiguo the new id no rastreable antiguo
	 */
    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }

    /**
	 * Gets the id rastreable antiguo.
	 *
	 * @return the id rastreable antiguo
	 */
    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    /**
	 * Sets the id rastreable antiguo.
	 *
	 * @param idRastreableAntiguo the new id rastreable antiguo
	 */
    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    /**
	 * Gets the id observatorio.
	 *
	 * @return the id observatorio
	 */
    public Long getId_observatorio() {
        return id_observatorio;
    }

    /**
	 * Sets the id observatorio.
	 *
	 * @param id_observatorio the new id observatorio
	 */
    public void setId_observatorio(Long id_observatorio) {
        this.id_observatorio = id_observatorio;
    }

    /**
	 * Gets the observatorio.
	 *
	 * @return the observatorio
	 */
    public Long getObservatorio() {
        return observatorio;
    }

    /**
	 * Sets the observatorio.
	 *
	 * @param observatorio the new observatorio
	 */
    public void setObservatorio(Long observatorio) {
        this.observatorio = observatorio;
    }

    /**
	 * Gets the lenguaje.
	 *
	 * @return the lenguaje
	 */
    public long getLenguaje() {
        return lenguaje;
    }

    /**
	 * Sets the lenguaje.
	 *
	 * @param lenguaje the new lenguaje
	 */
    public void setLenguaje(long lenguaje) {
        this.lenguaje = lenguaje;
    }

    /**
	 * Gets the lenguaje vector.
	 *
	 * @return the lenguaje vector
	 */
    public List<LenguajeForm> getLenguajeVector() {
        return lenguajeVector;
    }

    /**
	 * Sets the lenguaje vector.
	 *
	 * @param lenguajeVector the new lenguaje vector
	 */
    public void setLenguajeVector(List<LenguajeForm> lenguajeVector) {
        this.lenguajeVector = lenguajeVector;
    }

    /**
	 * Reset.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.lenguajeVector == null) {
            this.lenguajeVector = new ArrayList<>();
        }
        if (this.cartuchos == null) {
            this.cartuchos = new ArrayList<>();
        }
        if (this.normaVector == null) {
            this.normaVector = new ArrayList<>();
        }
        if (this.normaAnalisisEnlaces == null) {
            this.normaAnalisisEnlaces = "1";
        }
    }

    /**
	 * Gets the norma analisis enlaces.
	 *
	 * @return the norma analisis enlaces
	 */
    public String getNormaAnalisisEnlaces() {
        return normaAnalisisEnlaces;
    }

    /**
	 * Sets the norma analisis enlaces.
	 *
	 * @param normaAnalisisEnlaces the new norma analisis enlaces
	 */
    public void setNormaAnalisisEnlaces(String normaAnalisisEnlaces) {
        this.normaAnalisisEnlaces = normaAnalisisEnlaces;
    }

    /**
	 * Checks if is pseudo aleatorio.
	 *
	 * @return true, if is pseudo aleatorio
	 */
    public boolean isPseudoAleatorio() {
        return pseudoAleatorio;
    }

    /**
	 * Sets the pseudo aleatorio.
	 *
	 * @param pseudoAleatorio the new pseudo aleatorio
	 */
    public void setPseudoAleatorio(boolean pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    /**
	 * Checks if is exhaustive.
	 *
	 * @return true, if is exhaustive
	 */
    public boolean isExhaustive() {
        return exhaustive;
    }

    /**
	 * Sets the exhaustive.
	 *
	 * @param exhaustive the new exhaustive
	 */
    public void setExhaustive(boolean exhaustive) {
        this.exhaustive = exhaustive;
    }
}