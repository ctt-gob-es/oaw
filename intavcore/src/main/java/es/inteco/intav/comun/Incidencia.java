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
package es.inteco.intav.comun;

/**
 * The Class Incidencia.
 */
public class Incidencia {

    /** The codigo incidencia. */
    private int codigoIncidencia;
    
    /** The codigo comprobacion. */
    private int codigoComprobacion;
    
    /** The codigo analisis. */
    private long codigoAnalisis;
    
    /** The codigo linea fuente. */
    private int codigoLineaFuente;
    
    /** The codigo columna fuente. */
    private int codigoColumnaFuente;
    
    /** The codigo fuente. */
    private String codigoFuente;

    /**
	 * Gets the codigo incidencia.
	 *
	 * @return the codigo incidencia
	 */
    public int getCodigoIncidencia() {
        return codigoIncidencia;
    }

    /**
	 * Sets the codigo incidencia.
	 *
	 * @param codigoIncidencia the new codigo incidencia
	 */
    public void setCodigoIncidencia(int codigoIncidencia) {
        this.codigoIncidencia = codigoIncidencia;
    }

    /**
	 * Gets the codigo comprobacion.
	 *
	 * @return the codigo comprobacion
	 */
    public int getCodigoComprobacion() {
        return codigoComprobacion;
    }

    /**
	 * Sets the codigo comprobacion.
	 *
	 * @param codigoComprobacion the new codigo comprobacion
	 */
    public void setCodigoComprobacion(int codigoComprobacion) {
        this.codigoComprobacion = codigoComprobacion;
    }

    /**
	 * Gets the codigo linea fuente.
	 *
	 * @return the codigo linea fuente
	 */
    public int getCodigoLineaFuente() {
        return codigoLineaFuente;
    }

    /**
	 * Sets the codigo linea fuente.
	 *
	 * @param codigoLineaFuente the new codigo linea fuente
	 */
    public void setCodigoLineaFuente(int codigoLineaFuente) {
        this.codigoLineaFuente = codigoLineaFuente;
    }

    /**
	 * Gets the codigo columna fuente.
	 *
	 * @return the codigo columna fuente
	 */
    public int getCodigoColumnaFuente() {
        return codigoColumnaFuente;
    }

    /**
	 * Sets the codigo columna fuente.
	 *
	 * @param codigoColumnaFuente the new codigo columna fuente
	 */
    public void setCodigoColumnaFuente(int codigoColumnaFuente) {
        this.codigoColumnaFuente = codigoColumnaFuente;
    }

    /**
	 * Gets the codigo fuente.
	 *
	 * @return the codigo fuente
	 */
    public String getCodigoFuente() {
        return codigoFuente;
    }

    /**
	 * Sets the codigo fuente.
	 *
	 * @param codigoFuente the new codigo fuente
	 */
    public void setCodigoFuente(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    /**
	 * Gets the codigo analisis.
	 *
	 * @return the codigo analisis
	 */
    public long getCodigoAnalisis() {
        return codigoAnalisis;
    }

    /**
	 * Sets the codigo analisis.
	 *
	 * @param codigoAnalisis the new codigo analisis
	 */
    public void setCodigoAnalisis(long codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }
}
