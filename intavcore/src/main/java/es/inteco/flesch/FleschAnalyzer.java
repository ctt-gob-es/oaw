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

public interface FleschAnalyzer {

    public static final int SPANISH = 1;
    public static final int ENGLISH = 2;

    public static final int FLESCH_LEVEL_UNKNOWN = 8;
    public static final int FLESCH_LEVEL_VERY_EASY = 7;
    public static final int FLESCH_LEVEL_EASY = 6;
    public static final int FLESCH_LEVEL_QUITE_EASY = 5;
    public static final int FLESCH_LEVEL_STANDARD = 4;
    public static final int FLESCH_LEVEL_QUITE_HARD = 3;
    public static final int FLESCH_LEVEL_HARD = 2;
    public static final int FLESCH_LEVEL_VERY_HARD = 1;

    /**
     * Contador de sílabas
     *
     * @param text
     * @return
     */
    public int countSyllables(String text);

    /**
     * Contador de frases
     *
     * @param text
     * @return
     */
    public int countPhrases(String text);

    /**
     * Contador de palabras
     *
     * @param text
     * @return
     */
    public int countWords(String text);

    /**
     * Devuelve el valor de la formula Flesch
     *
     * @param numSyllables
     * @param numWords
     * @param numPhrases
     * @return
     */
    public double calculateFleschValue(int numSyllables, int numWords, int numPhrases);

    /**
     * Devuelve el nivel de legibilidad de acuerdo con el valor Flesch
     *
     * @param fleschValue
     * @return
     */
    public int getReadabilityLevel(double fleschValue);

}
