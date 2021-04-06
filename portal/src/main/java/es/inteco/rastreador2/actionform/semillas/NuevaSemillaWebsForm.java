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
 * The Class NuevaSemillaWebsForm.
 */
public class NuevaSemillaWebsForm extends ValidatorForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre semilla. */
    private String nombreSemilla;
    
    /** The ta 1. */
    private String ta1;
    
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
	 * Gets the ta 1.
	 *
	 * @return the ta 1
	 */
    public String getTa1() {
        return ta1;
    }

    /**
	 * Sets the ta 1.
	 *
	 * @param ta1 the new ta 1
	 */
    public void setTa1(String ta1) {
        this.ta1 = ta1;
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