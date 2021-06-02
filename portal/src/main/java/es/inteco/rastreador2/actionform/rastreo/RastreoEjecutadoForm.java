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

import org.apache.struts.action.ActionForm;

/**
 * The Class RastreoEjecutadoForm.
 */
public class RastreoEjecutadoForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id rastreo. */
    private long id_rastreo;
    
    /** The id ejecucion. */
    private long id_ejecucion;
    
    /** The nombre rastreo. */
    private String nombre_rastreo;
    
    /** The fecha. */
    private String fecha;
    
    /** The id cartucho. */
    private long id_cartucho;

    /**
	 * Gets the id cartucho.
	 *
	 * @return the id cartucho
	 */
    public long getId_cartucho() {
        return id_cartucho;
    }

    /**
	 * Sets the id cartucho.
	 *
	 * @param id_cartucho the new id cartucho
	 */
    public void setId_cartucho(long id_cartucho) {
        this.id_cartucho = id_cartucho;
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
	 * Gets the id ejecucion.
	 *
	 * @return the id ejecucion
	 */
    public long getId_ejecucion() {
        return id_ejecucion;
    }

    /**
	 * Sets the id ejecucion.
	 *
	 * @param id_ejecucion the new id ejecucion
	 */
    public void setId_ejecucion(long id_ejecucion) {
        this.id_ejecucion = id_ejecucion;
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
}
