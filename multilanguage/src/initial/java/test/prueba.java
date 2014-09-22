package es.inteco.multilanguage.test;

import es.inteco.multilanguage.properties.PropertiesManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

public class prueba {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue("multilanguage.properties", "dictionary.path");
            br = new BufferedReader(new FileReader(path + "diccionario_valenciano.txt"));
            //bw = new BufferedWriter(new FileWriter(path + "diccionario_valenciano2.txt"));

            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.endsWith("i")) {
                    System.out.println(line);
                } else {
                    //bw.write(line);
                    //bw.newLine();
                }
                /*if (line.length() > 2 && (line.substring(line.length() - 2, line.length())).equals("-a")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -1)) + "a";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 4 && (line.substring(line.length() - 4, line.length())).equals("-ora")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -2)) + "ora";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 4 && (line.substring(line.length() - 4, line.length())).equals("-ona")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -2)) + "ona";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 4 && (line.substring(line.length() - 4, line.length())).equals("-esa")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -2)) + "esa";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 4 && (line.substring(line.length() - 4, line.length())).equals("-ana")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -2)) + "ana";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 4 && (line.substring(line.length() - 4, line.length())).equals("-ina")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() -2)) + "ina";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 3 && (line.substring(line.length() - 3, line.length())).equals("-oa")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() - 1)) + "oa";
					bw.write(line);
					bw.newLine();
				}else if(line.length() > 3 && (line.substring(line.length() - 3, line.length())).equals("-as")){
					bw.write(line.substring(0, line.indexOf("-")).trim());
					bw.newLine();
					line = (line.substring(0, line.indexOf("-")).trim());
					line = line.substring(0, (line.length() - 1)) + "as";
					bw.write(line);
					bw.newLine();
				}else{
					bw.write(line);
					bw.newLine();
				}*/
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
