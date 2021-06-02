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
package es.inteco.rastreador2.actionform.observatorio;


import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * The Class CargarObservatorioForm.
 */
public class CargarObservatorioForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The listado observatorio. */
    private List<ListadoObservatorio> listadoObservatorio;
    
    /** The num observatorios. */
    private int numObservatorios;

    /**
	 * Gets the listado observatorio.
	 *
	 * @return the listado observatorio
	 */
    public List<ListadoObservatorio> getListadoObservatorio() {
        return listadoObservatorio;
    }

    /**
	 * Sets the listado observatorio.
	 *
	 * @param listadoCuentasUsuario the new listado observatorio
	 */
    public void setListadoObservatorio(List<ListadoObservatorio> listadoCuentasUsuario) {
        this.listadoObservatorio = listadoCuentasUsuario;
    }

    /**
	 * Gets the num observatorios.
	 *
	 * @return the num observatorios
	 */
    public int getNumObservatorios() {
        return numObservatorios;
    }

    /**
	 * Sets the num observatorios.
	 *
	 * @param numObservatorios the new num observatorios
	 */
    public void setNumObservatorios(int numObservatorios) {
        this.numObservatorios = numObservatorios;
    }

}