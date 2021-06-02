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
 * The Class LanzarComandoForm.
 */
public class LanzarComandoForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The user. */
    private String user;
    
    /** The pass. */
    private String pass;
    
    /** The rastreo. */
    private String rastreo;
    
    /** The comando. */
    private String comando;
    
    /** The estado antiguo. */
    private String estado_antiguo;
    
    /** The lanzar parar. */
    private String lanzarParar;
    
    /** The lanzando parando. */
    private String lanzandoParando;


    /**
	 * Gets the lanzar parar.
	 *
	 * @return the lanzar parar
	 */
    public String getLanzarParar() {
        return lanzarParar;
    }

    /**
	 * Sets the lanzar parar.
	 *
	 * @param lanzarParar the new lanzar parar
	 */
    public void setLanzarParar(String lanzarParar) {
        this.lanzarParar = lanzarParar;
    }

    /**
	 * Gets the lanzando parando.
	 *
	 * @return the lanzando parando
	 */
    public String getLanzandoParando() {
        return lanzandoParando;
    }

    /**
	 * Sets the lanzando parando.
	 *
	 * @param lanzandoParando the new lanzando parando
	 */
    public void setLanzandoParando(String lanzandoParando) {
        this.lanzandoParando = lanzandoParando;
    }

    /**
	 * Gets the estado antiguo.
	 *
	 * @return the estado antiguo
	 */
    public String getEstado_antiguo() {
        return estado_antiguo;
    }

    /**
	 * Sets the estado antiguo.
	 *
	 * @param estado_antiguo the new estado antiguo
	 */
    public void setEstado_antiguo(String estado_antiguo) {
        this.estado_antiguo = estado_antiguo;
    }

    /**
	 * Gets the comando.
	 *
	 * @return the comando
	 */
    public String getComando() {
        return comando;
    }

    /**
	 * Sets the comando.
	 *
	 * @param comando the new comando
	 */
    public void setComando(String comando) {
        this.comando = comando;
    }

    /**
	 * Gets the rastreo.
	 *
	 * @return the rastreo
	 */
    public String getRastreo() {
        return rastreo;
    }

    /**
	 * Sets the rastreo.
	 *
	 * @param rastreo the new rastreo
	 */
    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
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