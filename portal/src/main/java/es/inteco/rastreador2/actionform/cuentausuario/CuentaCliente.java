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

import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;

import java.sql.Timestamp;

/**
 * The Class CuentaCliente.
 */
public class CuentaCliente {
    
    /** The id cuenta. */
    private Long idCuenta;
    
    /** The active. */
    private boolean active;
    
    /** The nombre. */
    private String nombre;
    
    /** The periodicidad. */
    private PeriodicidadForm periodicidad;
    
    /** The fecha. */
    private Timestamp fecha;
    
    /** The datos rastreo. */
    private DatosCartuchoRastreoForm datosRastreo;

    /**
	 * Gets the id cuenta.
	 *
	 * @return the id cuenta
	 */
    public Long getIdCuenta() {
        return idCuenta;
    }

    /**
	 * Sets the id cuenta.
	 *
	 * @param idCuenta the new id cuenta
	 */
    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
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
    public PeriodicidadForm getPeriodicidad() {
        return periodicidad;
    }

    /**
	 * Sets the periodicidad.
	 *
	 * @param periodicidad the new periodicidad
	 */
    public void setPeriodicidad(PeriodicidadForm periodicidad) {
        this.periodicidad = periodicidad;
    }

    /**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    /**
	 * Gets the datos rastreo.
	 *
	 * @return the datos rastreo
	 */
    public DatosCartuchoRastreoForm getDatosRastreo() {
        return datosRastreo;
    }

    /**
	 * Sets the datos rastreo.
	 *
	 * @param datosRastreo the new datos rastreo
	 */
    public void setDatosRastreo(DatosCartuchoRastreoForm datosRastreo) {
        this.datosRastreo = datosRastreo;
    }

    /**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
    public void setActive(boolean active) {
        this.active = active;
    }
}
