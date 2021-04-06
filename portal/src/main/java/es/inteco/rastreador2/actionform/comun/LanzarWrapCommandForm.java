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
 * The Class LanzarWrapCommandForm.
 */
public class LanzarWrapCommandForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The opcion. */
    private String opcion;
    
    /** The user. */
    private String user;
    
    /** The pass. */
    private String pass;
    
    /** The cartucho. */
    private String cartucho;
    
    /** The rastreo. */
    private String rastreo;
    
    /** The fecha. */
    private String fecha;
    
    /** The c rastreo. */
    private String c_rastreo;
    
    /** The comando. */
    private String comando;
    
    /** The mensaje. */
    private String mensaje;
    
    /** The texto adicional. */
    private String textoAdicional;
    
    /** The texto adicional 2. */
    private String textoAdicional2;
    
    /** The id language. */
    private long id_language;

    /**
	 * Gets the id language.
	 *
	 * @return the id language
	 */
    public long getId_language() {
        return id_language;
    }

    /**
	 * Sets the id language.
	 *
	 * @param id_language the new id language
	 */
    public void setId_language(long id_language) {
        this.id_language = id_language;
    }

    /**
	 * Gets the texto adicional 2.
	 *
	 * @return the texto adicional 2
	 */
    public String getTextoAdicional2() {
        return textoAdicional2;
    }

    /**
	 * Sets the texto adicional 2.
	 *
	 * @param textoAdicional2 the new texto adicional 2
	 */
    public void setTextoAdicional2(String textoAdicional2) {
        this.textoAdicional2 = textoAdicional2;
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
	 * Gets the texto adicional.
	 *
	 * @return the texto adicional
	 */
    public String getTextoAdicional() {
        return textoAdicional;
    }

    /**
	 * Sets the texto adicional.
	 *
	 * @param textoAdicional the new texto adicional
	 */
    public void setTextoAdicional(String textoAdicional) {
        this.textoAdicional = textoAdicional;
    }

    /**
	 * Gets the mensaje.
	 *
	 * @return the mensaje
	 */
    public String getMensaje() {
        return mensaje;
    }

    /**
	 * Sets the mensaje.
	 *
	 * @param mensaje the new mensaje
	 */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
	 * Gets the c rastreo.
	 *
	 * @return the c rastreo
	 */
    public String getC_rastreo() {
        return c_rastreo;
    }

    /**
	 * Sets the c rastreo.
	 *
	 * @param c_rastreo the new c rastreo
	 */
    public void setC_rastreo(String c_rastreo) {
        this.c_rastreo = c_rastreo;
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
	 * Gets the opcion.
	 *
	 * @return the opcion
	 */
    public String getOpcion() {
        return opcion;
    }

    /**
	 * Sets the opcion.
	 *
	 * @param opcion the new opcion
	 */
    public void setOpcion(String opcion) {
        this.opcion = opcion;
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