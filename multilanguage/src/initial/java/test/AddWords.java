package es.inteco.multilanguage.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddWords {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReaderDic = new BufferedReader(new FileReader("/usr/share/diccionarios/diccionario_valenciano.txt"));
        List<String> resultsDic = new ArrayList<String>();
        String line = bufferedReaderDic.readLine();
        while (line != null) {
            if (!resultsDic.contains(line)) {
                resultsDic.add(line);
            }
            line = bufferedReaderDic.readLine();
        }


        BufferedReader bufferedReaderCons = new BufferedReader(new FileReader("/usr/share/diccionarios/texto.txt"));
        List<String> resultsCons = new ArrayList<String>();
        line = bufferedReaderCons.readLine();
        while (line != null) {
            String[] textList = line.split("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜàèìòùÀÈÌÒÙçÇ]+");
            for (int i = 0; i < textList.length; i++) {
                if (textList[i].length() > 1 && !resultsCons.contains(textList[i])) {
                    resultsCons.add(textList[i]);
                }
            }
            line = bufferedReaderCons.readLine();
        }

        for (String resultCons : resultsCons) {
            if (!resultsDic.contains(resultCons)) {
                resultsDic.add(resultCons);
            }
        }

        Collections.sort(resultsDic);

        FileWriter fw = new FileWriter("/usr/share/diccionarios/diccionario_valencianoNuevo.txt");
        for (String resultDic : resultsDic) {
            fw.write(resultDic + "\n");
        }
        fw.flush();
        fw.close();

    }
}
