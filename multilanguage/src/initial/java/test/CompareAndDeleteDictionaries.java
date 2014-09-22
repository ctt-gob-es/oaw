package es.inteco.multilanguage.test;

import es.inteco.multilanguage.properties.PropertiesManager;

import java.io.*;
import java.util.*;

public class CompareAndDeleteDictionaries {

    public static void main(String[] args) {
        PropertiesManager pmgr = new PropertiesManager();
        String path = pmgr.getValue("multilanguage.properties", "dictionary.path");
        String file1 = path + "diccionario_catalan.txt";
        String file2 = path + "wordsToDelete.txt";
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(path + "diccionario.txt"));
            Map<String, List<String>> Dictionary1 = loadDictionary(file1);
            Map<String, List<String>> toDelete = loadDictionary(file2);
            for (String letra : toDelete.keySet()) {
                if (Dictionary1.get(letra) != null) {
                    List<String> dictionaryWords = new ArrayList<String>();
                    for (String dicPal : Dictionary1.get(letra)) {
                        if (!toDelete.get(letra).contains(dicPal)) {
                            dictionaryWords.add(dicPal);
                        }
                    }
                    Collections.sort(dictionaryWords);
                    Dictionary1.put(letra, dictionaryWords);
                }
            }

            for (String l : Dictionary1.keySet()) {
                for (String palabra : Dictionary1.get(l)) {
                    bw.write(palabra);
                    bw.newLine();
                }
            }

            bw.flush();
        } catch (Exception e) {
            System.out.println("ERROR AL GENERAR LOS RESULTADOS");
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                System.out.println("ERROR AL CERRAR EL FICHEO DE RESULTADOS");
            }
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
}
