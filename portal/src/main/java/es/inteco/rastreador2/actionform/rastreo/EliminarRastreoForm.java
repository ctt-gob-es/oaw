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
 * The Class EliminarRastreoForm.
 */
public class EliminarRastreoForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The norma analisis. */
    private String codigo, fecha, cartucho, idrastreo, normaAnalisis;


    /**
	 * Gets the idrastreo.
	 *
	 * @return the idrastreo
	 */
    public String getIdrastreo() {
        return idrastreo;
    }

    /**
	 * Sets the idrastreo.
	 *
	 * @param idrastreo the new idrastreo
	 */
    public void setIdrastreo(String idrastreo) {
        this.idrastreo = idrastreo;
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