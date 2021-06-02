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

/**
 * The Class PeriodicidadForm.
 */
public class PeriodicidadForm extends ActionForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private long id;
    
    /** The nombre. */
    private String nombre;
    
    /** The cron expression. */
    private String cronExpression;
    
    /** The dias. */
    private int dias;

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(long id) {
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
	 * Gets the cron expression.
	 *
	 * @return the cron expression
	 */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
	 * Sets the cron expression.
	 *
	 * @param cronExpression the new cron expression
	 */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
	 * Gets the dias.
	 *
	 * @return the dias
	 */
    public int getDias() {
        return dias;
    }

    /**
	 * Sets the dias.
	 *
	 * @param dias the new dias
	 */
    public void setDias(int dias) {
        this.dias = dias;
    }
}