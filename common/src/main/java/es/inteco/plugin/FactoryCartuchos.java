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
package es.inteco.plugin;

import es.inteco.common.logging.Logger;

/**
 * @author a.mesas
 *         Implementa la factoria de cartuchos. Dado el nombre de un cartucho nos retorna
 *         una instancia del mismo.
 */
public final class FactoryCartuchos {

    private FactoryCartuchos() {
    }

    /**
     * Devuelve una instancia del cartucho que se solicita
     *
     * @param nombreCartucho nombre completo de la clase del cartucho
     * @return un nuevo objeto de tipo Cartucho o null si se produce algún error
     */
    public static Cartucho getCartucho(final String nombreCartucho) {
        try {
            final Class clase = Class.forName(nombreCartucho);
            // Creo una instancia
            return (Cartucho) clase.newInstance();
        } catch (ClassCastException cce) {
            Logger.putLog("Fallo en el factory de cartuchos, la clase indicada no es un cartucho", FactoryCartuchos.class, Logger.LOG_LEVEL_ERROR);
        } catch (Exception e) {
            Logger.putLog("Fallo en el factory de cartuchos, cartucho no implementado", FactoryCartuchos.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

}
