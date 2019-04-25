/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.language;

import org.apache.tika.language.LanguageIdentifier;

/**
 * Clase para comprobar el idioma de una página web. Se utilizan dos diferentes motores, uno basado en Tika y otro basado en el port de KDE
 * ya que cada uno de ellos da soporte a diferentes idiomas.
 */
public class LanguageChecker {

    private String expectedLanguage;

    /**
     *
     * @param expectedLanguage idioma sobre el que se comprobará
     */
    public LanguageChecker(final String expectedLanguage) {
        // Las herramientas no diferencian correctamente el valenciano del catalán.
        this.expectedLanguage = "va".equals(expectedLanguage) ? "ca" : expectedLanguage;
    }

    /**
     * Método que comprueba si un determinado texto está escrito en el idioma esperado (indicado en el constructor)
     * @param content cadena con el contenido en formato texto plano a comprobar el idioma
     * @return true si el idioma detectado es el esperado (indicado en el constructor)
     */
    public boolean isExpectedLanguage(final String content) {
        return getLanguage(content).equals(expectedLanguage);
    }

    /**
     * Obtiene el código de idioma, si está entre los admitidos por este comprobador, de un texto
     * @param content cadena con el contenido en texto plano
     * @return una cadena con el código del idioma detectado para el contenido o el idioma esperado si no se puede detectar ese idioma
     */
    protected String getLanguage(final String content) {
        if (GuessLanguage.isSupportedLanguage(expectedLanguage)) {
            final GuessLanguage guessLanguage = new GuessLanguage();
            return guessLanguage.guessLanguage(content);
        } else if (LanguageIdentifier.getSupportedLanguages().contains(expectedLanguage)) {
            final LanguageIdentifier languageIdentifier = new LanguageIdentifier(content);
            return languageIdentifier.getLanguage();
        }
        return expectedLanguage;
    }

}
