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
package es.inteco.rastreador2.actionform.cuentausuario;


import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class NuevaCuentaUsuarioForm.
 */
public class NuevaCuentaUsuarioForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id cuenta. */
    private String id_cuenta;
    
    /** The nombre. */
    private String nombre;
    
    /** The dominio. */
    private String dominio;
    
    /** The id seed. */
    private long idSeed;
    
    /** The id crawlable list. */
    private long idCrawlableList;
    
    /** The id no crawlable list. */
    private long idNoCrawlableList;
    
    /** The norma analisis. */
    private String normaAnalisis;
    
    /** The norma analisis enlaces. */
    private String normaAnalisisEnlaces;
    
    /** The lista rastreable. */
    private String listaRastreable;
    
    /** The lista no rastreable. */
    private String listaNoRastreable;
    
    /** The cartuchos selected. */
    private String[] cartuchosSelected;
    
    /** The cartuchos. */
    private List<CartuchoForm> cartuchos;
    
    /** The periodicidad. */
    private String periodicidad;
    
    /** The lenguaje vector. */
    private List<LenguajeForm> lenguajeVector;
    
    /** The lenguaje. */
    private long lenguaje;
    
    /** The periodicidad vector. */
    private List<PeriodicidadForm> periodicidadVector;
    
    /** The profundidad. */
    private String profundidad;
    
    /** The amplitud. */
    private String amplitud;
    
    /** The fecha inicio. */
    private String fechaInicio;
    
    /** The hora inicio. */
    private String horaInicio;
    
    /** The minuto inicio. */
    private String minutoInicio;
    
    /** The fecha. */
    private Date fecha;
    
    /** The pseudo aleatorio. */
    private boolean pseudoAleatorio;
    
    /** The activo. */
    private boolean activo;
    
    /** The in directory. */
    private boolean inDirectory;

    /**
	 * Gets the id cuenta.
	 *
	 * @return the id cuenta
	 */
    public String getId_cuenta() {
        return id_cuenta;
    }

    /**
	 * Sets the id cuenta.
	 *
	 * @param id_cuenta the new id cuenta
	 */
    public void setId_cuenta(String id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    /**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * Gets the dominio.
	 *
	 * @return the dominio
	 */
    public String getDominio() {
        return dominio;
    }

    /**
	 * Sets the dominio.
	 *
	 * @param dominio the new dominio
	 */
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    /**
	 * Gets the cartuchos selected.
	 *
	 * @return the cartuchos selected
	 */
    public String[] getCartuchosSelected() {
        return cartuchosSelected;
    }

    /**
	 * Sets the cartuchos selected.
	 *
	 * @param cartuchosSelected the new cartuchos selected
	 */
    public void setCartuchosSelected(String[] cartuchosSelected) {
        this.cartuchosSelected = cartuchosSelected;
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
	 * Gets the periodicidad.
	 *
	 * @return the periodicidad
	 */
    public String getPeriodicidad() {
        return periodicidad;
    }

    /**
	 * Sets the periodicidad.
	 *
	 * @param periodicidad the new periodicidad
	 */
    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    /**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
    public String getProfundidad() {
        return profundidad;
    }

    /**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    /**
	 * Gets the amplitud.
	 *
	 * @return the amplitud
	 */
    public String getAmplitud() {
        return amplitud;
    }

    /**
	 * Sets the amplitud.
	 *
	 * @param amplitud the new amplitud
	 */
    public void setAmplitud(String amplitud) {
        this.amplitud = amplitud;
    }

    /**
	 * Gets the periodicidad vector.
	 *
	 * @return the periodicidad vector
	 */
    public List<PeriodicidadForm> getPeriodicidadVector() {
        return periodicidadVector;
    }

    /**
	 * Sets the periodicidad vector.
	 *
	 * @param periodicidadVector the new periodicidad vector
	 */
    public void setPeriodicidadVector(List<PeriodicidadForm> periodicidadVector) {
        this.periodicidadVector = periodicidadVector;
    }

    /**
	 * Gets the fecha inicio.
	 *
	 * @return the fecha inicio
	 */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
	 * Sets the fecha inicio.
	 *
	 * @param fechaInicio the new fecha inicio
	 */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
	 * Gets the hora inicio.
	 *
	 * @return the hora inicio
	 */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
	 * Sets the hora inicio.
	 *
	 * @param horaInicio the new hora inicio
	 */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
	 * Gets the minuto inicio.
	 *
	 * @return the minuto inicio
	 */
    public String getMinutoInicio() {
        return minutoInicio;
    }

    /**
	 * Sets the minuto inicio.
	 *
	 * @param minutoInicio the new minuto inicio
	 */
    public void setMinutoInicio(String minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    /**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
    public Date getFecha() {
        return fecha;
    }

    /**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
	 * Gets the id seed.
	 *
	 * @return the id seed
	 */
    public long getIdSeed() {
        return idSeed;
    }

    /**
	 * Sets the id seed.
	 *
	 * @param idSeed the new id seed
	 */
    public void setIdSeed(long idSeed) {
        this.idSeed = idSeed;
    }

    /**
	 * Gets the id crawlable list.
	 *
	 * @return the id crawlable list
	 */
    public long getIdCrawlableList() {
        return idCrawlableList;
    }

    /**
	 * Sets the id crawlable list.
	 *
	 * @param idCrawlableList the new id crawlable list
	 */
    public void setIdCrawlableList(long idCrawlableList) {
        this.idCrawlableList = idCrawlableList;
    }

    /**
	 * Gets the id no crawlable list.
	 *
	 * @return the id no crawlable list
	 */
    public long getIdNoCrawlableList() {
        return idNoCrawlableList;
    }

    /**
	 * Sets the id no crawlable list.
	 *
	 * @param idNoCrawlableList the new id no crawlable list
	 */
    public void setIdNoCrawlableList(long idNoCrawlableList) {
        this.idNoCrawlableList = idNoCrawlableList;
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
	 * Checks if is activo.
	 *
	 * @return true, if is activo
	 */
    public boolean isActivo() {
        return activo;
    }

    /**
	 * Sets the activo.
	 *
	 * @param activo the new activo
	 */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
	 * Reset.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 */
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
}