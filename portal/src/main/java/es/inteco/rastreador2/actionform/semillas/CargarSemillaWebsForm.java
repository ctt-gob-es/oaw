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


import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class CargarSemillaWebsForm.
 */
public class CargarSemillaWebsForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The urls. */
    private List<String> urls;
    
    /** The urls string. */
    private String urls_string;
    
    /** The archivo. */
    private String archivo;


    /**
	 * Gets the archivo.
	 *
	 * @return the archivo
	 */
    public String getArchivo() {
        return archivo;
    }

    /**
	 * Sets the archivo.
	 *
	 * @param archivo the new archivo
	 */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    /**
	 * Gets the urls string.
	 *
	 * @return the urls string
	 */
    public String getUrls_string() {
        return urls_string;
    }

    /**
	 * Sets the urls string.
	 *
	 * @param urls_string the new urls string
	 */
    public void setUrls_string(String urls_string) {
        this.urls_string = urls_string;
    }

    /**
	 * Gets the urls.
	 *
	 * @return the urls
	 */
    public List<String> getUrls() {
        return urls;
    }

    /**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}