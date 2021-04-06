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
package es.inteco.rastreador2.actionform.observatorio;

import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * The Class VerObservatorioForm.
 */
public class VerObservatorioForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre. */
    private String nombre;
    
    /** The periodicidad. */
    private String periodicidad;
    
    /** The profundidad. */
    private String profundidad;
    
    /** The amplitud. */
    private String amplitud;
    
    /** The norma analisis. */
    private Long normaAnalisis;
    
    /** The norma analisis st. */
    private String normaAnalisisSt;
    
    /** The semillas list. */
    private List<SemillaForm> semillasList;
    
    /** The pseudo aleatorio. */
    private String pseudoAleatorio;
    
    /** The activo. */
    private boolean activo;
    
    /** The categorias. */
    private List<CategoriaForm> categorias;
    
    /** The cartucho. */
    private CartuchoForm cartucho;
    
    /** The tipo. */
    private ObservatoryTypeForm tipo;

    /**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
    public ObservatoryTypeForm getTipo() {
        return tipo;
    }

    /**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
    public void setTipo(ObservatoryTypeForm tipo) {
        this.tipo = tipo;
    }

    /**
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public CartuchoForm getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(CartuchoForm cartucho) {
        this.cartucho = cartucho;
    }

    /**
	 * Gets the semillas list.
	 *
	 * @return the semillas list
	 */
    public List<SemillaForm> getSemillasList() {
        return semillasList;
    }

    /**
	 * Sets the semillas list.
	 *
	 * @param semillasList the new semillas list
	 */
    public void setSemillasList(List<SemillaForm> semillasList) {
        this.semillasList = semillasList;
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
	 * Reset.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /**
	 * Gets the categorias.
	 *
	 * @return the categorias
	 */
    public List<CategoriaForm> getCategorias() {
        return categorias;
    }

    /**
	 * Sets the categorias.
	 *
	 * @param categorias the new categorias
	 */
    public void setCategorias(List<CategoriaForm> categorias) {
        this.categorias = categorias;
    }
}
