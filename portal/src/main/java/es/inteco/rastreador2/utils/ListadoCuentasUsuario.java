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
import java.util.List;

/**
 * The Class ListadoCuentasUsuario.
 */
public class ListadoCuentasUsuario implements Serializable {
    
    /** The nombre cuenta. */
    private String nombreCuenta;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The dominio. */
    private List<String> dominio;
    
    /** The cartucho. */
    private List<String> cartucho;
    
    /** The id cuenta. */
    private Long id_cuenta;

    /**
	 * Gets the nombre cuenta.
	 *
	 * @return the nombre cuenta
	 */
    public String getNombreCuenta() {
        return nombreCuenta;
    }

    /**
	 * Sets the nombre cuenta.
	 *
	 * @param nombreCuenta the new nombre cuenta
	 */
    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
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
	 * Gets the cartucho.
	 *
	 * @return the cartucho
	 */
    public List<String> getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(List<String> cartucho) {
        this.cartucho = cartucho;
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
	 * Gets the id cuenta.
	 *
	 * @return the id cuenta
	 */
    public Long getId_cuenta() {
        return id_cuenta;
    }

    /**
	 * Sets the id cuenta.
	 *
	 * @param id_cuenta the new id cuenta
	 */
    public void setId_cuenta(Long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

}
