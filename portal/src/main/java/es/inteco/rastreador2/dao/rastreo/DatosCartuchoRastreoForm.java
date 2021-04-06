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
package es.inteco.rastreador2.dao.rastreo;


import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class DatosCartuchoRastreoForm.
 */
public class DatosCartuchoRastreoForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre rastreo. */
    private String nombreRastreo;
    
    /** The id rastreo. */
    private int id_rastreo;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The nombre cart. */
    private String nombre_cart;
    
    /** The nombre rastreo. */
    private String nombre_rastreo;
    
    /** The num rastreos. */
    private int numRastreos;
    
    /** The profundidad. */
    private int profundidad;
    
    /** The top N. */
    private int topN;
    
    /** The id semilla. */
    private long idSemilla;
    
    /** The seed acronym. */
    private String seedAcronym;
    
    /** The lista no rastreable. */
    private String listaNoRastreable;
    
    /** The lista rastreable. */
    private String listaRastreable;
    
    /** The id cuenta cliente. */
    private long idCuentaCliente;
    
    /** The id observatory. */
    private long idObservatory;
    
    /** The pseudoaleatorio. */
    private boolean pseudoaleatorio;
    
    /** The exhaustive. */
    private boolean exhaustive;
    
    /** The in directory. */
    private boolean inDirectory;
    
    /** The language. */
    private LenguajeForm language;
    
    /** The cartuchos. */
    private String[] cartuchos;
    
    /** The urls. */
    private List<String> urls;
    
    /** The domains. */
    private List<String> domains;
    
    /** The exceptions. */
    private List<String> exceptions;
    
    /** The crawling list. */
    private List<String> crawlingList;
    
    /** The id guideline. */
    private long id_guideline;
    
    /** The fichero norma. */
    private String ficheroNorma;

    /**
	 * Gets the fichero norma.
	 *
	 * @return the fichero norma
	 */
    public String getFicheroNorma() {
        return ficheroNorma;
    }

    /**
	 * Sets the fichero norma.
	 *
	 * @param ficheroNorma the new fichero norma
	 */
    public void setFicheroNorma(String ficheroNorma) {
        this.ficheroNorma = ficheroNorma;
    }

    /**
	 * Gets the urls.
	 *
	 * @return the urls
	 */
    public List<String> getUrls() {
        return urls;
    }

    /**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    /**
	 * Gets the id guideline.
	 *
	 * @return the id guideline
	 */
    public long getId_guideline() {
        return id_guideline;
    }

    /**
	 * Sets the id guideline.
	 *
	 * @param id_guideline the new id guideline
	 */
    public void setId_guideline(long id_guideline) {
        this.id_guideline = id_guideline;
    }

    /**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public String[] getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(String[] cartuchos) {
        this.cartuchos = cartuchos;
    }

    /**
	 * Gets the id rastreo.
	 *
	 * @return the id rastreo
	 */
    public int getId_rastreo() {
        return id_rastreo;
    }

    /**
	 * Sets the id rastreo.
	 *
	 * @param id_rastreo the new id rastreo
	 */
    public void setId_rastreo(int id_rastreo) {
        this.id_rastreo = id_rastreo;
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
	 * Gets the nombre cart.
	 *
	 * @return the nombre cart
	 */
    public String getNombre_cart() {
        return nombre_cart;
    }

    /**
	 * Sets the nombre cart.
	 *
	 * @param nombre_cart the new nombre cart
	 */
    public void setNombre_cart(String nombre_cart) {
        this.nombre_cart = nombre_cart;
    }

    /**
	 * Gets the num rastreos.
	 *
	 * @return the num rastreos
	 */
    public int getNumRastreos() {
        return numRastreos;
    }

    /**
	 * Sets the num rastreos.
	 *
	 * @param numRastreos the new num rastreos
	 */
    public void setNumRastreos(int numRastreos) {
        this.numRastreos = numRastreos;
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
    public int getTopN() {
        return topN;
    }

    /**
	 * Sets the top N.
	 *
	 * @param topN the new top N
	 */
    public void setTopN(int topN) {
        this.topN = topN;
    }

    /**
	 * Gets the nombre rastreo.
	 *
	 * @return the nombre rastreo
	 */
    public String getNombre_rastreo() {
        return nombre_rastreo;
    }

    /**
	 * Sets the nombre rastreo.
	 *
	 * @param nombre_rastreo the new nombre rastreo
	 */
    public void setNombre_rastreo(String nombre_rastreo) {
        this.nombre_rastreo = nombre_rastreo;
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
	 * Gets the id cuenta cliente.
	 *
	 * @return the id cuenta cliente
	 */
    public long getIdCuentaCliente() {
        return idCuentaCliente;
    }

    /**
	 * Sets the id cuenta cliente.
	 *
	 * @param idCuentaCliente the new id cuenta cliente
	 */
    public void setIdCuentaCliente(long idCuentaCliente) {
        this.idCuentaCliente = idCuentaCliente;
    }

    /**
	 * Gets the id observatory.
	 *
	 * @return the id observatory
	 */
    public long getIdObservatory() {
        return idObservatory;
    }

    /**
	 * Sets the id observatory.
	 *
	 * @param idObservatory the new id observatory
	 */
    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }

    /**
	 * Checks if is pseudoaleatorio.
	 *
	 * @return true, if is pseudoaleatorio
	 */
    public boolean isPseudoaleatorio() {
        return pseudoaleatorio;
    }

    /**
	 * Sets the pseudoaleatorio.
	 *
	 * @param pseudoaleatorio the new pseudoaleatorio
	 */
    public void setPseudoaleatorio(boolean pseudoaleatorio) {
        this.pseudoaleatorio = pseudoaleatorio;
    }

    /**
	 * Gets the nombre rastreo.
	 *
	 * @return the nombre rastreo
	 */
    public String getNombreRastreo() {
        return nombreRastreo;
    }

    /**
	 * Sets the nombre rastreo.
	 *
	 * @param nombreRastreo the new nombre rastreo
	 */
    public void setNombreRastreo(String nombreRastreo) {
        this.nombreRastreo = nombreRastreo;
    }

    /**
	 * Gets the language.
	 *
	 * @return the language
	 */
    public LenguajeForm getLanguage() {
        return language;
    }

    /**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
    public void setLanguage(LenguajeForm language) {
        this.language = language;
    }

    /**
	 * Gets the id semilla.
	 *
	 * @return the id semilla
	 */
    public long getIdSemilla() {
        return idSemilla;
    }

    /**
	 * Sets the id semilla.
	 *
	 * @param idSemilla the new id semilla
	 */
    public void setIdSemilla(long idSemilla) {
        this.idSemilla = idSemilla;
    }

    /**
	 * Reset.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.language == null) {
            this.language = new LenguajeForm();
        }
        if (this.urls == null) {
            this.urls = new ArrayList<>();
        }
        if (this.domains == null) {
            this.domains = new ArrayList<>();
        }
        if (this.exceptions == null) {
            this.exceptions = new ArrayList<>();
        }
        if (this.crawlingList == null) {
            this.crawlingList = new ArrayList<>();
        }
    }

    /**
	 * Gets the domains.
	 *
	 * @return the domains
	 */
    public List<String> getDomains() {
        return domains;
    }

    /**
	 * Sets the domains.
	 *
	 * @param domains the new domains
	 */
    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    /**
	 * Gets the exceptions.
	 *
	 * @return the exceptions
	 */
    public List<String> getExceptions() {
        return exceptions;
    }

    /**
	 * Sets the exceptions.
	 *
	 * @param exceptions the new exceptions
	 */
    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    /**
	 * Gets the crawling list.
	 *
	 * @return the crawling list
	 */
    public List<String> getCrawlingList() {
        return crawlingList;
    }

    /**
	 * Sets the crawling list.
	 *
	 * @param crawlingList the new crawling list
	 */
    public void setCrawlingList(List<String> crawlingList) {
        this.crawlingList = crawlingList;
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

    /**
	 * Gets the seed acronym.
	 *
	 * @return the seed acronym
	 */
    public String getSeedAcronym() {
        return seedAcronym;
    }

    /**
	 * Sets the seed acronym.
	 *
	 * @param seedAcronym the new seed acronym
	 */
    public void setSeedAcronym(String seedAcronym) {
        this.seedAcronym = seedAcronym;
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