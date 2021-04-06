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

import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class VerCuentaUsuarioForm.
 */
public class VerCuentaUsuarioForm extends ActionForm {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre. */
    private String nombre;
    
    /** The dominio. */
    private List<String> dominio;
    
    /** The cartuchos. */
    private List<String> cartuchos;
    
    /** The periodicidad. */
    private String periodicidad;
    
    /** The profundidad. */
    private String profundidad;
    
    /** The amplitud. */
    private String amplitud;
    
    /** The responsable. */
    private List<String> responsable;
    
    /** The usuarios. */
    private List<String> usuarios;
    
    /** The norma analisis. */
    private Long normaAnalisis;
    
    /** The norma analisis st. */
    private String normaAnalisisSt;
    
    /** The lista rastreable. */
    private List<String> listaRastreable;
    
    /** The lista no rastreable. */
    private List<String> listaNoRastreable;
    
    /** The pseudo aleatorio. */
    private String pseudoAleatorio;
    
    /** The activo. */
    private boolean activo;
    
    /** The in directory. */
    private boolean inDirectory;

    /**
	 * Gets the lista rastreable.
	 *
	 * @return the lista rastreable
	 */
    public List<String> getListaRastreable() {
        return listaRastreable;
    }

    /**
	 * Sets the lista rastreable.
	 *
	 * @param listaRastreable the new lista rastreable
	 */
    public void setListaRastreable(List<String> listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    /**
	 * Gets the lista no rastreable.
	 *
	 * @return the lista no rastreable
	 */
    public List<String> getListaNoRastreable() {
        return listaNoRastreable;
    }

    /**
	 * Sets the lista no rastreable.
	 *
	 * @param listaNoRastreable the new lista no rastreable
	 */
    public void setListaNoRastreable(List<String> listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    /**
	 * Gets the responsable.
	 *
	 * @return the responsable
	 */
    public List<String> getResponsable() {
        return responsable;
    }

    /**
	 * Sets the responsable.
	 *
	 * @param responsable the new responsable
	 */
    public void setResponsable(List<String> responsable) {
        this.responsable = responsable;
    }

    /**
	 * Gets the usuarios.
	 *
	 * @return the usuarios
	 */
    public List<String> getUsuarios() {
        return usuarios;
    }

    /**
	 * Sets the usuarios.
	 *
	 * @param usuarios the new usuarios
	 */
    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
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
    public List<String> getDominio() {
        return dominio;
    }

    /**
	 * Sets the dominio.
	 *
	 * @param dominio the new dominio
	 */
    public void setDominio(List<String> dominio) {
        this.dominio = dominio;
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
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public List<String> getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    /**
	 * Gets the norma analisis.
	 *
	 * @return the norma analisis
	 */
    public Long getNormaAnalisis() {
        return normaAnalisis;
    }

    /**
	 * Sets the norma analisis.
	 *
	 * @param normaAnalisis the new norma analisis
	 */
    public void setNormaAnalisis(Long normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    /**
	 * Gets the norma analisis st.
	 *
	 * @return the norma analisis st
	 */
    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    /**
	 * Sets the norma analisis st.
	 *
	 * @param normaAnalisisSt the new norma analisis st
	 */
    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
    }

    /**
	 * Gets the pseudo aleatorio.
	 *
	 * @return the pseudo aleatorio
	 */
    public String getPseudoAleatorio() {
        return pseudoAleatorio;
    }

    /**
	 * Sets the pseudo aleatorio.
	 *
	 * @param pseudoAleatorio the new pseudo aleatorio
	 */
    public void setPseudoAleatorio(String pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
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
