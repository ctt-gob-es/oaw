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
 * The Class ListadoUsuario.
 */
public class ListadoUsuario implements Serializable {
    
    /** The usuario. */
    private String usuario;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The cartucho. */
    private List cartucho;
    
    /** The tipo. */
    private List tipo;
    
    /** The tipo rol. */
    private int tipoRol;
    
    /** The id usuario. */
    private Long id_usuario;


    /**
	 * Gets the id usuario.
	 *
	 * @return the id usuario
	 */
    public Long getId_usuario() {
        return id_usuario;
    }

    /**
	 * Sets the id usuario.
	 *
	 * @param id_usuario the new id usuario
	 */
    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
	 * Gets the usuario.
	 *
	 * @return the usuario
	 */
    public String getUsuario() {
        return usuario;
    }

    /**
	 * Sets the usuario.
	 *
	 * @param usuario the new usuario
	 */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
    public List getCartucho() {
        return cartucho;
    }

    /**
	 * Sets the cartucho.
	 *
	 * @param cartucho the new cartucho
	 */
    public void setCartucho(List cartucho) {
        this.cartucho = cartucho;
    }

    /**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
    public List getTipo() {
        return tipo;
    }

    /**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
    public void setTipo(List tipo) {
        this.tipo = tipo;
    }

    /**
	 * Gets the tipo rol.
	 *
	 * @return the tipo rol
	 */
    public int getTipoRol() {
        return tipoRol;
    }

    /**
	 * Sets the tipo rol.
	 *
	 * @param tipoRol the new tipo rol
	 */
    public void setTipoRol(int tipoRol) {
        this.tipoRol = tipoRol;
    }
}
