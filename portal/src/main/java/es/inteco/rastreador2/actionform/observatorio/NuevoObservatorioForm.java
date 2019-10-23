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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;

/**
 * The Class NuevoObservatorioForm.
 */
public class NuevoObservatorioForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id observatorio. */
    private String id_observatorio;
    
    /** The nombre. */
    private String nombre;
    
    /** The periodicidad. */
    private String periodicidad;
    
    /** The lenguaje. */
    private long lenguaje;
    
    /** The periodicidad vector. */
    private List<PeriodicidadForm> periodicidadVector;
    
    /** The lenguaje vector. */
    private List<LenguajeForm> lenguajeVector;
    
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
    
    /** The add seeds. */
    private List<SemillaForm> addSeeds;
    
    /** The other seeds. */
    private List<SemillaForm> otherSeeds;
    
    /** The button action. */
    private String buttonAction;
    
    /** The categoria. */
    private String[] categoria;
    
    /** The complejidad. */
    private String[] complejidad;
    
    /** The ambito. */
    private String[] ambito;
    
    /** The cartucho. */
    private CartuchoForm cartucho;
    
    /** The pseudo aleatorio. */
    private String pseudoAleatorio;
    
    /** The activo. */
    private boolean activo;
    
    /** The tipo. */
    private ObservatoryTypeForm tipo;
    
    /** The ambito form. */
    private AmbitoForm ambitoForm;


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
	 * Instantiates a new nuevo observatorio form.
	 */
    public NuevoObservatorioForm() {
        cartucho = new CartuchoForm();
        tipo = new ObservatoryTypeForm();
        ambitoForm = new AmbitoForm();
    }

    /**
	 * Gets the button action.
	 *
	 * @return the button action
	 */
    public String getButtonAction() {
        return buttonAction;
    }

    /**
	 * Sets the button action.
	 *
	 * @param buttonAction the new button action
	 */
    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
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
	 * Gets the id observatorio.
	 *
	 * @return the id observatorio
	 */
    public String getId_observatorio() {
        return id_observatorio;
    }

    /**
	 * Sets the id observatorio.
	 *
	 * @param id_observatorio the new id observatorio
	 */
    public void setId_observatorio(String id_observatorio) {
        this.id_observatorio = id_observatorio;
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
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
    public String[] getCategoria() {
        return categoria;
    }

    /**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }
    
    /**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
    public String[] getAmbito() {
        return ambito;
    }

    /**
	 * Sets the ambito.
	 *
	 * @param ambito the new ambito
	 */
    public void setAmbito(String[] ambito) {
        this.ambito = ambito;
    }
    

    public String[] getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String[] complejidad) {
        this.complejidad = complejidad;
    }
    

    /**
	 * Gets the adds the seeds.
	 *
	 * @return the adds the seeds
	 */
    public List<SemillaForm> getAddSeeds() {
        return addSeeds;
    }

    /**
	 * Sets the adds the seeds.
	 *
	 * @param addSeeds the new adds the seeds
	 */
    public void setAddSeeds(List<SemillaForm> addSeeds) {
        this.addSeeds = addSeeds;
    }

    /**
	 * Gets the other seeds.
	 *
	 * @return the other seeds
	 */
    public List<SemillaForm> getOtherSeeds() {
        return otherSeeds;
    }

    /**
	 * Sets the other seeds.
	 *
	 * @param otherSeeds the new other seeds
	 */
    public void setOtherSeeds(List<SemillaForm> otherSeeds) {
        this.otherSeeds = otherSeeds;
    }

    /**
	 * Gets the ambito form.
	 *
	 * @return the ambito form
	 */
    public AmbitoForm getAmbitoForm() {
		return ambitoForm;
	}

	/**
	 * Sets the ambito form.
	 *
	 * @param ambitoForm the new ambito form
	 */
	public void setAmbitoForm(AmbitoForm ambitoForm) {
		this.ambitoForm = ambitoForm;
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
        if (this.periodicidadVector == null) {
            this.periodicidadVector = new ArrayList<>();
        }
        if (this.addSeeds == null) {
            this.addSeeds = new ArrayList<>();
        }
        if (this.otherSeeds == null) {
            this.otherSeeds = new ArrayList<>();
        }
        if (this.cartucho == null) {
            this.cartucho = new CartuchoForm();
        }
        if (this.tipo == null) {
            this.tipo = new ObservatoryTypeForm();
        }
    }
}