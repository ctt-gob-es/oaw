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

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class RastreoClienteForm.
 */
public class RastreoClienteForm extends ValidatorForm {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id cuenta. */
    private String idCuenta;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The norma analisis. */
    private String normaAnalisis;
    
    /** The nombre. */
    private String nombre;

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
	 * Gets the id cuenta.
	 *
	 * @return the id cuenta
	 */
    public String getIdCuenta() {
        return idCuenta;
    }

    /**
	 * Sets the id cuenta.
	 *
	 * @param idCuenta the new id cuenta
	 */
    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
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
}
