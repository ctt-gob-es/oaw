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

import java.util.Arrays;
import java.util.List;

/**
 * The Class FleschSpanishAnalyzer.
 */
public class FleschSpanishAnalyzer implements FleschAnalyzer {

    /**
	 * Count phrases.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countPhrases(String text) {
        String[] phrases = text.split("\\.");

        return phrases.length;
    }

    /**
	 * Count syllables.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countSyllables(String text) {
        final String[] words = text.split("[^a-zA-ZáéíóúÁÉÍÓÚñÑ]");
        final List<String> consonants = Arrays.asList("b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n",
                "ñ", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z");
        final List<String> vowels = Arrays.asList("a", "e", "i", "o", "u", "á", "é", "í", "ó", "ú", "ü");
        final List<String> strongVowels = Arrays.asList("a", "e", "o", "á", "é", "ó");
        final List<String> weakVowels = Arrays.asList("i", "u");
        // List<String> exceptions1 = Arrays.asList("b","d", "c", "f", "g", "t", "p");
        // List<String> exceptions2 = Arrays.asList("l", "r");


        int count = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i] != null && words[i].trim().length() > 0) {
                String word = words[i];
                for (int j = 0; j < word.length(); j++) {
                    if (consonants.contains(String.valueOf(word.charAt(j)))
                            && j + 1 < word.length() && strongVowels.contains(String.valueOf(word.charAt(j + 1)))
                            && j + 2 < word.length() && weakVowels.contains(String.valueOf(word.charAt(j + 2)))) {
                        // Diptongos: consonante + vocal fuerte + vocal débil (cau-dal)
                        count++;
                        j += 2;
                    } else if (consonants.contains(String.valueOf(word.charAt(j)))
                            && j + 1 < word.length() && weakVowels.contains(String.valueOf(word.charAt(j + 1)))
                            && j + 2 < word.length() && strongVowels.contains(String.valueOf(word.charAt(j + 2)))) {
                        // Diptongos: consonante + vocal débil + vocal fuerte (ca-mión)
                        count++;
                        j += 2;
                    } else if (strongVowels.contains(String.valueOf(word.charAt(j)))
                            && j + 1 < word.length() && strongVowels.contains(String.valueOf(word.charAt(j + 1)))) {
                        // Hiato: vocal fuerte + vocal fuerte (a-é-re-o)
                        count++;
                    } else if (strongVowels.contains(String.valueOf(word.charAt(j)))
                            && j + 1 == word.length()) {
                        // La última vocal fuerte sola
                        count++;
                    } else if (consonants.contains(String.valueOf(word.charAt(j))) && j + 1 < word.length() && vowels.contains(String.valueOf(word.charAt(j + 1)))) {
                        // Caso simple: consonante + vocal (ma-no)
                        count++;
                        j++;
                    } else if (vowels.contains(String.valueOf(word.charAt(j))) && j + 1 < word.length() && consonants.contains(String.valueOf(word.charAt(j + 1)))) {
                        // Caso simple: vocal + consonante (u-ti-li-zar)
                        count++;
                    } else if (word.length() == 1) {
                        // Caso simple: palabras de una letra (y, o, e...)
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
	 * Count words.
	 *
	 * @param text the text
	 * @return the int
	 */
    public int countWords(String text) {
        return FleschUtils.countWords(text);
    }

    /**
	 * Calculate flesch value.
	 *
	 * @param numSyllables the num syllables
	 * @param numWords     the num words
	 * @param numPhrases   the num phrases
	 * @return the double
	 */
    public double calculateFleschValue(int numSyllables, int numWords, int numPhrases) {
        // return 206.84 - (60 * NS / NW) - (1.02 * NW / NP)
        return 206.84 - (60 * (double) numSyllables / (double) numWords) - (1.02 * (double) numWords / (double) numPhrases);
    }

    /**
	 * Gets the readability level.
	 *
	 * @param fleschValue the flesch value
	 * @return the readability level
	 */
    public int getReadabilityLevel(double fleschValue) {
        if (fleschValue > 90) {
            return FleschAnalyzer.FLESCH_LEVEL_VERY_EASY;
        } else if (fleschValue <= 90 && fleschValue > 80) {
            return FleschAnalyzer.FLESCH_LEVEL_EASY;
        } else if (fleschValue <= 80 && fleschValue > 70) {
            return FleschAnalyzer.FLESCH_LEVEL_QUITE_EASY;
        } else if (fleschValue <= 70 && fleschValue > 60) {
            return FleschAnalyzer.FLESCH_LEVEL_STANDARD;
        } else if (fleschValue <= 60 && fleschValue > 50) {
            return FleschAnalyzer.FLESCH_LEVEL_QUITE_HARD;
        } else if (fleschValue <= 50 && fleschValue > 30) {
            return FleschAnalyzer.FLESCH_LEVEL_HARD;
        } else if (fleschValue <= 30 && fleschValue >= 0) {
            return FleschAnalyzer.FLESCH_LEVEL_VERY_HARD;
        } else {
            return FleschAnalyzer.FLESCH_LEVEL_UNKNOWN;
        }
    }

}
