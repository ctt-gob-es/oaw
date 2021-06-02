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
package es.inteco.rastreador2.actionform.comun;


import org.apache.struts.action.ActionForm;


/**
 * The Class RedireccionConfigForm.
 */
public class RedireccionConfigForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The proyecto. */
    private String proyecto;
    
    /** The user. */
    private String user;
    
    /** The tipo. */
    private String tipo;
    
    /** The num cartuchos. */
    private int numCartuchos;
    
    /** The seleccionados. */
    private int seleccionados;
    
    /** The pass. */
    private String pass;
    
    /** The id cartucho. */
    private int id_cartucho;


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
	 * Gets the proyecto.
	 *
	 * @return the proyecto
	 */
    public String getProyecto() {
        return proyecto;
    }

    /**
	 * Sets the proyecto.
	 *
	 * @param proyecto the new proyecto
	 */
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    /**
	 * Gets the user.
	 *
	 * @return the user
	 */
    public String getUser() {
        return user;
    }

    /**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
    public void setUser(String user) {
        this.user = user;
    }

    /**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
    public String getTipo() {
        return tipo;
    }

    /**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
	 * Gets the num cartuchos.
	 *
	 * @return the num cartuchos
	 */
    public int getNumCartuchos() {
        return numCartuchos;
    }

    /**
	 * Sets the num cartuchos.
	 *
	 * @param numCartuchos the new num cartuchos
	 */
    public void setNumCartuchos(int numCartuchos) {
        this.numCartuchos = numCartuchos;
    }

    /**
	 * Gets the seleccionados.
	 *
	 * @return the seleccionados
	 */
    public int getSeleccionados() {
        return seleccionados;
    }

    /**
	 * Sets the seleccionados.
	 *
	 * @param seleccionados the new seleccionados
	 */
    public void setSeleccionados(int seleccionados) {
        this.seleccionados = seleccionados;
    }

    /**
	 * Gets the pass.
	 *
	 * @return the pass
	 */
    public String getPass() {
        return pass;
    }

    /**
	 * Sets the pass.
	 *
	 * @param pass the new pass
	 */
    public void setPass(String pass) {
        this.pass = pass;
    }

}