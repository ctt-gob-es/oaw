package es.inteco.multilanguage.test;

import es.inteco.multilanguage.properties.PropertiesManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EqualDictionariesWords {

    /**
     * @param args
     */
    public static void main(String[] args) {
        PropertiesManager pmgr = new PropertiesManager();
        String path = pmgr.getValue("multilanguage.properties", "dictionary.path");
        String file1 = path + "nombres.txt";
        String file2 = path + "diccionario_catalan.txt";

        try {
            Map<String, List<String>> Dictionary1 = loadDictionary(file1);
            int count = 0;
            for (String letra : Dictionary1.keySet()) {
                for (String palabra : Dictionary1.get(letra)) {
                    count++;
                }
            }
            System.out.println("Número palabras catalan: " + count);

            Map<String, List<String>> Dictionary2 = loadDictionary(file2);
            count = 0;
            for (String letra : Dictionary2.keySet()) {
                for (String palabra : Dictionary2.get(letra)) {
                    count++;
                }
            }
            System.out.println("Número palabras valenciano: " + count);

            int comun = 0;

            System.out.println("EXISTEN EN CATALAN Y NO EN VALENCIANO");
            for (String letra : Dictionary1.keySet()) {
                for (String palabra : Dictionary1.get(letra)) {
                    if (exist(Dictionary2, palabra)) {
                        System.out.println(palabra);
                        comun++;
                    }
                }

            }

            System.out.println("Comunes: " + comun);
        } catch (Exception e) {
            System.out.println("ERROR AL GENERAR LOS RESULTADOS");
        }
    }

    private static TreeMap<String, List<String>> loadDictionary(String file) {
        TreeMap<String, List<String>> dictionary = new TreeMap<String, List<String>>();

        BufferedReader br = null;
        String word = new String();
        try {
            br = new BufferedReader(new FileReader(file));
            while ((word = br.readLine()) != null) {

                //HABRÍA QUE COMPROBAR QUE LA PALABRA NO FUERA VACÍO, DE MOMENTO NO SE HACE PARA
                //QUE FALLE, QUIERE DECIR QUE EL DOC DEL DICCIONARIO TIENE ALGÚN FALLO!

                word = word.trim().toUpperCase();
                List<String> wordList = new ArrayList<String>();
                if (dictionary.get(word.substring(0, 1)) == null) {
                    wordList.add(word);
                    dictionary.put(word.substring(0, 1), wordList);
                } else {
                    wordList = dictionary.get(word.substring(0, 1));
                    wordList.add(word);
                    dictionary.put(word.substring(0, 1), wordList);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR AL RECUPERAR EL DICCIONARIO.");
            e.printStackTrace();
        }
        return dictionary;
    }

    private static boolean exist(Map<String, List<String>> dictionary, String word) {
        if (dictionary.get(word.substring(0, 1)) != null) {
            if (dictionary.get(word.substring(0, 1).toUpperCase()).contains(word)) {
                return true;
            }
        }
        return false;
    }
}
