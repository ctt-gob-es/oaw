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
package es.inteco.flesch;

/**
 * The Class FleschAdapter.
 */
public final class FleschAdapter {

    /**
	 * Instantiates a new flesch adapter.
	 */
    private FleschAdapter() {
    }

    /**
	 * Gets the flesch analyzer.
	 *
	 * @param language the language
	 * @return the flesch analyzer
	 * @throws Exception the exception
	 */
    public static FleschAnalyzer getFleschAnalyzer(String language) throws Exception {
        if (language != null && language.toLowerCase().startsWith("es")) {
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.SPANISH);
        } else if (language != null && language.toLowerCase().startsWith("en")) {
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.ENGLISH);
        } else {
            // Por defecto devolvemos un analizador en castellano
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.SPANISH);
        }
    }
}
