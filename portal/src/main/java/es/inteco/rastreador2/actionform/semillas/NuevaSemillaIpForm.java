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
package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;


/**
 * The Class NuevaSemillaIpForm.
 */
public class NuevaSemillaIpForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ip final 4. */
    private String ipInicial1, ipInicial2, ipInicial3, ipInicial4, ipFinal1, ipFinal2, ipFinal3, ipFinal4;
    
    /** The puerto 3. */
    private String puerto1, puerto2, puerto3;
    
    /** The nombre semilla. */
    private String nombreSemilla;
    
    /** The categoria. */
    private CategoriaForm categoria;

    /**
	 * Gets the nombre semilla.
	 *
	 * @return the nombre semilla
	 */
    public String getNombreSemilla() {
        return nombreSemilla;
    }

    /**
	 * Sets the nombre semilla.
	 *
	 * @param nombreSemilla the new nombre semilla
	 */
    public void setNombreSemilla(String nombreSemilla) {
        this.nombreSemilla = nombreSemilla;
    }

    /**
	 * Gets the ip inicial 1.
	 *
	 * @return the ip inicial 1
	 */
    public String getIpInicial1() {
        return ipInicial1;
    }

    /**
	 * Sets the ip inicial 1.
	 *
	 * @param ipInicial1 the new ip inicial 1
	 */
    public void setIpInicial1(String ipInicial1) {
        this.ipInicial1 = ipInicial1;
    }

    /**
	 * Gets the ip inicial 2.
	 *
	 * @return the ip inicial 2
	 */
    public String getIpInicial2() {
        return ipInicial2;
    }

    /**
	 * Sets the ip inicial 2.
	 *
	 * @param ipInicial2 the new ip inicial 2
	 */
    public void setIpInicial2(String ipInicial2) {
        this.ipInicial2 = ipInicial2;
    }

    /**
	 * Gets the ip inicial 3.
	 *
	 * @return the ip inicial 3
	 */
    public String getIpInicial3() {
        return ipInicial3;
    }

    /**
	 * Sets the ip inicial 3.
	 *
	 * @param ipInicial3 the new ip inicial 3
	 */
    public void setIpInicial3(String ipInicial3) {
        this.ipInicial3 = ipInicial3;
    }

    /**
	 * Gets the ip final 1.
	 *
	 * @return the ip final 1
	 */
    public String getIpFinal1() {
        return ipFinal1;
    }

    /**
	 * Sets the ip final 1.
	 *
	 * @param ipFinal1 the new ip final 1
	 */
    public void setIpFinal1(String ipFinal1) {
        this.ipFinal1 = ipFinal1;
    }

    /**
	 * Gets the ip final 2.
	 *
	 * @return the ip final 2
	 */
    public String getIpFinal2() {
        return ipFinal2;
    }

    /**
	 * Sets the ip final 2.
	 *
	 * @param ipFinal2 the new ip final 2
	 */
    public void setIpFinal2(String ipFinal2) {
        this.ipFinal2 = ipFinal2;
    }

    /**
	 * Gets the ip final 3.
	 *
	 * @return the ip final 3
	 */
    public String getIpFinal3() {
        return ipFinal3;
    }

    /**
	 * Sets the ip final 3.
	 *
	 * @param ipFinal3 the new ip final 3
	 */
    public void setIpFinal3(String ipFinal3) {
        this.ipFinal3 = ipFinal3;
    }

    /**
	 * Gets the puerto 1.
	 *
	 * @return the puerto 1
	 */
    public String getPuerto1() {
        return puerto1;
    }

    /**
	 * Sets the puerto 1.
	 *
	 * @param puerto1 the new puerto 1
	 */
    public void setPuerto1(String puerto1) {
        this.puerto1 = puerto1;
    }

    /**
	 * Gets the puerto 2.
	 *
	 * @return the puerto 2
	 */
    public String getPuerto2() {
        return puerto2;
    }

    /**
	 * Sets the puerto 2.
	 *
	 * @param puerto2 the new puerto 2
	 */
    public void setPuerto2(String puerto2) {
        this.puerto2 = puerto2;
    }

    /**
	 * Gets the puerto 3.
	 *
	 * @return the puerto 3
	 */
    public String getPuerto3() {
        return puerto3;
    }

    /**
	 * Sets the puerto 3.
	 *
	 * @param puerto3 the new puerto 3
	 */
    public void setPuerto3(String puerto3) {
        this.puerto3 = puerto3;
    }

    /**
	 * Gets the serial version UID.
	 *
	 * @return the serial version UID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
	 * Gets the ip inicial 4.
	 *
	 * @return the ip inicial 4
	 */
    public String getIpInicial4() {
        return ipInicial4;
    }

    /**
	 * Sets the ip inicial 4.
	 *
	 * @param ipInicial4 the new ip inicial 4
	 */
    public void setIpInicial4(String ipInicial4) {
        this.ipInicial4 = ipInicial4;
    }

    /**
	 * Gets the ip final 4.
	 *
	 * @return the ip final 4
	 */
    public String getIpFinal4() {
        return ipFinal4;
    }

    /**
	 * Sets the ip final 4.
	 *
	 * @param ipFinal4 the new ip final 4
	 */
    public void setIpFinal4(String ipFinal4) {
        this.ipFinal4 = ipFinal4;
    }

    /**
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
    public CategoriaForm getCategoria() {
        return categoria;
    }

    /**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
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

        if (categoria == null) {
            categoria = new CategoriaForm();
        }
    }
}