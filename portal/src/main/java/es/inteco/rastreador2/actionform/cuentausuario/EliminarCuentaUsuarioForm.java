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


import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class EliminarCuentaUsuarioForm.
 */
public class EliminarCuentaUsuarioForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The nombre. */
    private String nombre;
    
    /** The dominio. */
    private String dominio;
    
    /** The id lista rastreable. */
    private long idListaRastreable;
    
    /** The id lista no rastreable. */
    private long idListaNoRastreable;
    
    /** The id semilla. */
    private long idSemilla;
    
    /** The cartuchos. */
    private List<String> cartuchos;
    
    /** The responsable. */
    private List<String> responsable;
    
    /** The usuarios. */
    private List<String> usuarios;
    
    /** The id. */
    private Long id;
    
    /** The norma analisis. */
    private String normaAnalisis;
    
    /** The norma analisis st. */
    private String normaAnalisisSt;

    /**
	 * Gets the norma analisis st.
	 *
	 * @return the norma analisis st
	 */
    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    /**
	 * Sets the norma analisis st.
	 *
	 * @param normaAnalisisSt the new norma analisis st
	 */
    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
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

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        this.id = id;
    }

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
	 * Gets the dominio.
	 *
	 * @return the dominio
	 */
    public String getDominio() {
        return dominio;
    }

    /**
	 * Sets the dominio.
	 *
	 * @param dominio the new dominio
	 */
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    /**
	 * Gets the id lista rastreable.
	 *
	 * @return the id lista rastreable
	 */
    public long getIdListaRastreable() {
        return idListaRastreable;
    }

    /**
	 * Sets the id lista rastreable.
	 *
	 * @param idListaRastreable the new id lista rastreable
	 */
    public void setIdListaRastreable(long idListaRastreable) {
        this.idListaRastreable = idListaRastreable;
    }

    /**
	 * Gets the id lista no rastreable.
	 *
	 * @return the id lista no rastreable
	 */
    public long getIdListaNoRastreable() {
        return idListaNoRastreable;
    }

    /**
	 * Sets the id lista no rastreable.
	 *
	 * @param idListaNoRastreable the new id lista no rastreable
	 */
    public void setIdListaNoRastreable(long idListaNoRastreable) {
        this.idListaNoRastreable = idListaNoRastreable;
    }

    /**
	 * Gets the id semilla.
	 *
	 * @return the id semilla
	 */
    public long getIdSemilla() {
        return idSemilla;
    }

    /**
	 * Sets the id semilla.
	 *
	 * @param idSemilla the new id semilla
	 */
    public void setIdSemilla(long idSemilla) {
        this.idSemilla = idSemilla;
    }

    /**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
    public List<String> getCartuchos() {
        return cartuchos;
    }

    /**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    /**
	 * Gets the responsable.
	 *
	 * @return the responsable
	 */
    public List<String> getResponsable() {
        return responsable;
    }

    /**
	 * Sets the responsable.
	 *
	 * @param responsable the new responsable
	 */
    public void setResponsable(List<String> responsable) {
        this.responsable = responsable;
    }

    /**
	 * Gets the usuarios.
	 *
	 * @return the usuarios
	 */
    public List<String> getUsuarios() {
        return usuarios;
    }

    /**
	 * Sets the usuarios.
	 *
	 * @param usuarios the new usuarios
	 */
    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

}