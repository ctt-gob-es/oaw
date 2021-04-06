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
package es.inteco.rastreador2.utils;

import java.io.Serializable;

/**
 * The Class Rastreo.
 */
public class Rastreo implements Serializable {

    /** The id rastreo. */
    private String idRastreo;
    
    /** The codigo. */
    private String codigo;
    
    /** The codigo title. */
    private String codigoTitle;
    
    /** The fecha. */
    private String fecha;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The estado. */
    private int estado;
    
    /** The estado texto. */
    private String estadoTexto;
    
    /** The profundidad. */
    private int profundidad = 0;
    
    /** The id cuenta. */
    private long idCuenta;
    
    /** The activo. */
    private long activo;
    
    /** The pseudo aleatorio. */
    private String pseudoAleatorio;

    /**
	 * Gets the activo.
	 *
	 * @return the activo
	 */
    public long getActivo() {
        return activo;
    }

    /**
	 * Sets the activo.
	 *
	 * @param activo the new activo
	 */
    public void setActivo(long activo) {
        this.activo = activo;
    }

    /**
	 * Gets the estado texto.
	 *
	 * @return the estado texto
	 */
    public String getEstadoTexto() {
        return estadoTexto;
    }

    /**
	 * Sets the estado texto.
	 *
	 * @param estadoTexto the new estado texto
	 */
    public void setEstadoTexto(String estadoTexto) {
        this.estadoTexto = estadoTexto;
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
	 * Gets the estado.
	 *
	 * @return the estado
	 */
    public int getEstado() {
        return estado;
    }

    /**
	 * Sets the estado.
	 *
	 * @param estado the new estado
	 */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
	 * Gets the id rastreo.
	 *
	 * @return the id rastreo
	 */
    public String getIdRastreo() {
        return idRastreo;
    }

    /**
	 * Sets the id rastreo.
	 *
	 * @param idRastreo the new id rastreo
	 */
    public void setIdRastreo(String idRastreo) {
        this.idRastreo = idRastreo;
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
	 * Gets the id cuenta.
	 *
	 * @return the id cuenta
	 */
    public long getIdCuenta() {
        return idCuenta;
    }

    /**
	 * Sets the id cuenta.
	 *
	 * @param idCuenta the new id cuenta
	 */
    public void setIdCuenta(long idCuenta) {
        this.idCuenta = idCuenta;
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
	 * Gets the codigo title.
	 *
	 * @return the codigo title
	 */
    public String getCodigoTitle() {
        return codigoTitle;
    }

    /**
	 * Sets the codigo title.
	 *
	 * @param codigoTitle the new codigo title
	 */
    public void setCodigoTitle(String codigoTitle) {
        this.codigoTitle = codigoTitle;
    }
}
