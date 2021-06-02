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
 * The Interface FleschAnalyzer.
 */
public interface FleschAnalyzer {

    /** The Constant SPANISH. */
    public static final int SPANISH = 1;
    
    /** The Constant ENGLISH. */
    public static final int ENGLISH = 2;

    /** The Constant FLESCH_LEVEL_UNKNOWN. */
    public static final int FLESCH_LEVEL_UNKNOWN = 8;
    
    /** The Constant FLESCH_LEVEL_VERY_EASY. */
    public static final int FLESCH_LEVEL_VERY_EASY = 7;
    
    /** The Constant FLESCH_LEVEL_EASY. */
    public static final int FLESCH_LEVEL_EASY = 6;
    
    /** The Constant FLESCH_LEVEL_QUITE_EASY. */
    public static final int FLESCH_LEVEL_QUITE_EASY = 5;
    
    /** The Constant FLESCH_LEVEL_STANDARD. */
    public static final int FLESCH_LEVEL_STANDARD = 4;
    
    /** The Constant FLESCH_LEVEL_QUITE_HARD. */
    public static final int FLESCH_LEVEL_QUITE_HARD = 3;
    
    /** The Constant FLESCH_LEVEL_HARD. */
    public static final int FLESCH_LEVEL_HARD = 2;
    
    /** The Constant FLESCH_LEVEL_VERY_HARD. */
    public static final int FLESCH_LEVEL_VERY_HARD = 1;

    /**
	 * Contador de sílabas.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countSyllables(String text);

    /**
	 * Contador de frases.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countPhrases(String text);

    /**
	 * Contador de palabras.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countWords(String text);

    /**
	 * Devuelve el valor de la formula Flesch.
	 *
	 * @param numSyllables the num syllables
	 * @param numWords     the num words
	 * @param numPhrases   the num phrases
	 * @return the double
	 */
    public double calculateFleschValue(int numSyllables, int numWords, int numPhrases);

    /**
	 * Devuelve el nivel de legibilidad de acuerdo con el valor Flesch.
	 *
	 * @param fleschValue the flesch value
	 * @return the readability level
	 */
    public int getReadabilityLevel(double fleschValue);

}
