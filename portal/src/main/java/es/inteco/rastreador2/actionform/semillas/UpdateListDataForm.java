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

/**
 * The Class UpdateListDataForm.
 */
public class UpdateListDataForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The lista rastreable. */
    private String listaRastreable;
    
    /** The id lista rastreable. */
    private long idListaRastreable;
    
    /** The id rastreable antiguo. */
    private long idRastreableAntiguo;

    /** The lista no rastreable. */
    private String listaNoRastreable;
    
    /** The id lista no rastreable. */
    private long idListaNoRastreable;
    
    /** The id no rastreable antiguo. */
    private long idNoRastreableAntiguo;

    /** The nombre. */
    private String nombre;


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
	 * Gets the lista rastreable.
	 *
	 * @return the lista rastreable
	 */
    public String getListaRastreable() {
        return listaRastreable;
    }

    /**
	 * Sets the lista rastreable.
	 *
	 * @param listaRastreable the new lista rastreable
	 */
    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
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
	 * Gets the id rastreable antiguo.
	 *
	 * @return the id rastreable antiguo
	 */
    public long getIdRastreableAntiguo() {
        return idRastreableAntiguo;
    }

    /**
	 * Sets the id rastreable antiguo.
	 *
	 * @param idRastreableAntiguo the new id rastreable antiguo
	 */
    public void setIdRastreableAntiguo(long idRastreableAntiguo) {
        this.idRastreableAntiguo = idRastreableAntiguo;
    }

    /**
	 * Gets the lista no rastreable.
	 *
	 * @return the lista no rastreable
	 */
    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    /**
	 * Sets the lista no rastreable.
	 *
	 * @param listaNoRastreable the new lista no rastreable
	 */
    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
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
	 * Gets the id no rastreable antiguo.
	 *
	 * @return the id no rastreable antiguo
	 */
    public long getIdNoRastreableAntiguo() {
        return idNoRastreableAntiguo;
    }

    /**
	 * Sets the id no rastreable antiguo.
	 *
	 * @param idNoRastreableAntiguo the new id no rastreable antiguo
	 */
    public void setIdNoRastreableAntiguo(long idNoRastreableAntiguo) {
        this.idNoRastreableAntiguo = idNoRastreableAntiguo;
    }


}