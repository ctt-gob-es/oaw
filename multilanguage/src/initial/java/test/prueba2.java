package es.inteco.multilanguage.test;

import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class prueba2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        initializeProxyProperties();
        long ini = 20459;
        long finish = 20534;
        String url = "http://www.edu.xunta.es/diccionarios/ListaTermos.jsp?IDXT=z01";
        String path = "/usr/share/eclipse/workspace/multilanguage/metadata/diccionarios/";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path + "palabras.txt"));
            while (ini <= finish) {
                HttpURLConnection connection;

                connection = (HttpURLConnection) new URL(url + ini).openConnection();
                ini = ini + 25;
                connection.connect();

                DOMParser parser = new DOMParser(new HTMLConfiguration());

                parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
                parser.setFeature("http://xml.org/sax/features/namespaces", false);

                parser.parse(new InputSource(connection.getInputStream()));
                Document document = parser.getDocument();

                NodeList list = document.getElementsByTagName("A");
                for (int i = 0; i < list.getLength(); i++) {
                    if (((Element) list.item(i)).hasAttribute("href") &&
                            ((Element) list.item(i)).getAttribute("href").startsWith("ListaDefinicion.jsp?IDXT=")) {
                        System.out.println(list.item(i).getTextContent().trim());
                        bw.write(list.item(i).getTextContent().trim());
                        bw.newLine();
                    }
                }

                Thread.sleep(1000);

            }
            bw.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void initializeProxyProperties() {
        System.setProperty("http.proxyHost", "172.23.50.61");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("https.proxyHost", "172.23.50.61");
        System.setProperty("https.proxyPort", "8080");
    }

}
