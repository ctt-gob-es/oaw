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


import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.rastreo.NormaForm;
import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ModificarObservatorioForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String id_observatorio;
    private String nombre;
    private String nombre_antiguo;
    private String periodicidad;
    private long lenguaje;
    private List<LenguajeForm> lenguajeVector;
    private List<PeriodicidadForm> periodicidadVector;
    private String profundidad;
    private String amplitud;
    private String fechaInicio;
    private String horaInicio;
    private String minutoInicio;
    private Date fecha;
    private List<NormaForm> normaV;
    private String normaAnalisis;
    private List<SemillaForm> semillasAnadidas;
    private List<SemillaForm> semillasNoAnadidas;
    private String buttonAction;
    private String[] categoria;
    private CartuchoForm cartucho;
    private boolean pseudoAleatorio;
    private boolean activo;
    private ObservatoryTypeForm tipo;

    private long idRastreableAntiguo;
    private long idNoRastreableAntiguo;

    public ModificarObservatorioForm() {
        cartucho = new CartuchoForm();
        tipo = new ObservatoryTypeForm();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public List<PeriodicidadForm> getPeriodicidadVector() {
        return periodicidadVector;
    }

    public void setPeriodicidadVector(List<PeriodicidadForm> periodicidadVector) {
        this.periodicidadVector = periodicidadVector;
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

    public String getNombre_antiguo() {
        return nombre_antiguo;
    }

    public void setNombre_antiguo(String nombre_antiguo) {
        this.nombre_antiguo = nombre_antiguo;
    }

    public List<NormaForm> getNormaV() {
        return normaV;
    }

    public void setNormaV(List<NormaForm> normaV) {
        this.normaV = normaV;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public String getId_observatorio() {
        return id_observatorio;
    }

    public void setId_observatorio(String id_observatorio) {
        this.id_observatorio = id_observatorio;
    }

    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public boolean isPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(boolean pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
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

    public CartuchoForm getCartucho() {
        return cartucho;
    }

    public void setCartucho(CartuchoForm cartucho) {
        this.cartucho = cartucho;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String[] getCategoria() {
        return categoria;
    }

    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }

    public List<SemillaForm> getSemillasAnadidas() {
        return semillasAnadidas;
    }

    public void setSemillasAnadidas(List<SemillaForm> semillasAnadidas) {
        this.semillasAnadidas = semillasAnadidas;
    }

    public List<SemillaForm> getSemillasNoAnadidas() {
        return semillasNoAnadidas;
    }

    public void setSemillasNoAnadidas(List<SemillaForm> semillasNoAnadidas) {
        this.semillasNoAnadidas = semillasNoAnadidas;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.lenguajeVector == null) {
            this.lenguajeVector = new ArrayList<>();
        }
        if (this.periodicidadVector == null) {
            this.periodicidadVector = new ArrayList<>();
        }
        if (this.normaV == null) {
            this.normaV = new ArrayList<>();
        }
        if (this.semillasAnadidas == null) {
            this.semillasAnadidas = new ArrayList<>();
        }
        if (this.semillasNoAnadidas == null) {
            this.semillasNoAnadidas = new ArrayList<>();
        }
        if (this.cartucho == null) {
            this.cartucho = new CartuchoForm();
        }
        if (this.tipo == null) {
            this.tipo = new ObservatoryTypeForm();
        }
    }

    public ObservatoryTypeForm getTipo() {
        return tipo;
    }

    public void setTipo(ObservatoryTypeForm tipo) {
        this.tipo = tipo;
    }

}