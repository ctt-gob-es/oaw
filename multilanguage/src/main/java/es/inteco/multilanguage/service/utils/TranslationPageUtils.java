package es.inteco.multilanguage.service.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TranslationPageUtils {

    public static Map<String, BigDecimal> getPercentagesMap(String content) {

        Map<String, BigDecimal> percentageMap = new HashMap<>();

        PropertiesManager pmgr = new PropertiesManager();
        //Cargamos los diccionarios de los distintos idiomas
        final String dictionariesPath = pmgr.getValue("multilanguage.properties", "dictionary.path");
        Map<String, List<String>> spanishDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "es.dictionary.txt"));
        Map<String, List<String>> catalanDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "ca.dictionary.txt"));
        Map<String, List<String>> frenchDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "fr.dictionary.txt"));
        Map<String, List<String>> galicianDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "gl.dictionary.txt"));
        Map<String, List<String>> englishDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "en.dictionary.txt"));
        Map<String, List<String>> valencianDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "va.dictionary.txt"));
        Map<String, List<String>> euskeraDictionary = loadDictionary(dictionariesPath + pmgr.getValue("multilanguage.properties", "eu.dictionary.txt"));
        Map<String, Integer> textWords = TranslationPageUtils.loadText(content);

        //Creamos una lista con las distintas palabras del texto encontradas en alguno de los diccionarios
        //Y las distintas listas de palabras en cada uno de los diccionarios
        final List<String> foundWords = new ArrayList<>();
        List<String> spFound = TranslationPageUtils.getWordsInDictionary(spanishDictionary, textWords, foundWords);
        List<String> caFound = TranslationPageUtils.getWordsInDictionary(catalanDictionary, textWords, foundWords);
        List<String> frFound = TranslationPageUtils.getWordsInDictionary(frenchDictionary, textWords, foundWords);
        List<String> gaFound = TranslationPageUtils.getWordsInDictionary(galicianDictionary, textWords, foundWords);
        List<String> enFound = TranslationPageUtils.getWordsInDictionary(englishDictionary, textWords, foundWords);
        List<String> vaFound = TranslationPageUtils.getWordsInDictionary(valencianDictionary, textWords, foundWords);
        List<String> euFound = TranslationPageUtils.getWordsInDictionary(euskeraDictionary, textWords, foundWords);

        if (foundWords.size() != 0) {
            percentageMap.put(pmgr.getValue("multilanguage.properties", "es.codice"), new BigDecimal(spFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "en.codice"), new BigDecimal(enFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "fr.codice"), new BigDecimal(frFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "ca.codice"), new BigDecimal(caFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "eu.codice"), new BigDecimal(euFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "gl.codice"), new BigDecimal(gaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            percentageMap.put(pmgr.getValue("multilanguage.properties", "va.codice"), new BigDecimal(vaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        } else {
            percentageMap.put(pmgr.getValue("multilanguage.properties", "es.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "en.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "fr.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "ca.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "eu.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "gl.codice"), BigDecimal.ZERO);
            percentageMap.put(pmgr.getValue("multilanguage.properties", "va.codice"), BigDecimal.ZERO);
        }
        return percentageMap;
    }

    /**
     * Carga el diccionario que del archivo que se le pasa como parámetro.
     *
     * @param file
     * @return
     */
    private static Map<String, List<String>> loadDictionary(String file) {
        Map<String, List<String>> dictionary = new TreeMap<>();

        BufferedReader br = null;
        String word;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((word = br.readLine()) != null) {
                if (!word.isEmpty()) {
                    word = word.trim().toUpperCase();
                    List<String> wordList = new ArrayList<>();
                    if (dictionary.get(word.substring(0, 1)) == null) {
                        wordList.add(word);
                        dictionary.put(word.substring(0, 1), wordList);
                    } else {
                        wordList = dictionary.get(word.substring(0, 1));
                        wordList.add(word);
                        dictionary.put(word.substring(0, 1), wordList);
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al cargar el diccionario: " + file, TranslationPageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.putLog("Error al cerrar el diccionario: " + file, TranslationPageUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
        return dictionary;
    }

    /**
     * Incluye en la lista de palabras las encontradas en el diccionario que se le pasa si éstas no
     * están ya en la lista. También devuelve las encontradas en un diccionario en concreto.
     *
     * @param dictionary
     * @param textWords
     * @param foundWords
     */

    private static List<String> getWordsInDictionary(Map<String, List<String>> dictionary, Map<String, Integer> textWords, List<String> foundWords) {
        List<String> dictionaryWords = new ArrayList<>();
        for (String word : textWords.keySet()) {
            if (exist(dictionary, word)) {
                dictionaryWords.add(word);
                if (!foundWords.contains(word)) {
                    foundWords.add(word);
                }
            }
        }
        return dictionaryWords;
    }

    private static boolean exist(Map<String, List<String>> dictionary, String word) {
        if (dictionary.get(word.substring(0, 1)) != null) {
            if (dictionary.get(word.substring(0, 1).toUpperCase()).contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Carga en un mapa las distintas palabras que aparecen en el texto y el número de ocurrencias
     *
     * @param text
     * @return
     */

    private static Map<String, Integer> loadText(String text) {
        TreeMap<String, Integer> textMap = new TreeMap<>();
        try {
            List<String> textList = Arrays.asList(text.split("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜàèìòùÀÈÌÒÙçÇ·]+"));
            for (String str : textList) {
                if (!str.isEmpty()) {
                    if (textMap.get(str.toUpperCase()) == null) {
                        textMap.put(str.toUpperCase(), 1);
                    } else {
                        textMap.put(str.toUpperCase(), textMap.get(str.toUpperCase()) + 1);
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar el texto", TranslationPageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return textMap;
    }

}
