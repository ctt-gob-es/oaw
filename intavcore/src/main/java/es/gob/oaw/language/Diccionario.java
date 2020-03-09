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

import java.io.InputStream;
import java.util.*;

/**
 *
 */
public final class Diccionario {

    private static final Map<String, Set<String>> diccionario = new HashMap<>();

    static {
        Diccionario.init();
    }

    private static void init() {
        final InputStream is = Diccionario.class.getClassLoader().getResourceAsStream("languages/words_en.txt");
        final Scanner scanner = new Scanner(is);
        final Set<String> words = new HashSet<>();
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        scanner.close();
        diccionario.put("en", words);
    }

    private Diccionario() {
    }

    public static boolean containsWord(final String language, final String word) {
        final Set<String> words = diccionario.get(language);
        return words != null && words.contains(word);
    }

}
