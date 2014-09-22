package es.inteco.multilanguage.test;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.multilanguage.logging.Logger;
import es.inteco.multilanguage.properties.PropertiesManager;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.multilanguage.service.utils.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PercentageWords {

    /**
     * @param args
     */
    public static void main(String[] args) {

        setDefaultHostnameVerifier();
        setTrustingAllCerts();

        PropertiesManager pmgr = new PropertiesManager();
        String path = pmgr.getValue("multilanguage.properties", "dictionary.path");
        String spanishFile = path + "diccionario_espagnol.txt";
        String catalanFile = path + "diccionario_catalan.txt";
        String frenchFile = path + "diccionario_frances.txt";
        String galicianFile = path + "diccionario_gallego.txt";
        String englishFile = path + "diccionario_ingles.txt";
        String valencianFile = path + "diccionario_valenciano.txt";
        String euskeraFile = path + "diccionario_euskera.txt";

        List<String> fileText = new ArrayList<String>();
        fileText.add("https://www.ccn.cni.es/index.php?option=com_content&view=article&id=6&Itemid=9&lang=ca");

        //BufferedWriter bw = null;

        try {
            //bw = new BufferedWriter(new FileWriter(path + "results.txt"););
            initializeProxyProperties();
            for (String file : fileText) {
                String contenido = getUrlContent(file);
                //bw.write("ARCHIVO DE TEXTO: " + file);
                //bw.newLine();
                //bw.newLine();

                //Cargamos los diccionarios de los distintos idiomas
                Map<String, List<String>> spanishDictionary = loadDictionary(spanishFile);
                Map<String, List<String>> catalanDictionary = loadDictionary(catalanFile);
                Map<String, List<String>> frenchDictionary = loadDictionary(frenchFile);
                Map<String, List<String>> galicianDictionary = loadDictionary(galicianFile);
                Map<String, List<String>> englishDictionary = loadDictionary(englishFile);
                Map<String, List<String>> valencianDictionary = loadDictionary(valencianFile);
                Map<String, List<String>> euskeraDictionary = loadDictionary(euskeraFile);
                Map<String, Integer> textWords = loadText(HTMLEntities.unhtmlentities(contenido));


                //Mostramos el porcentaje no dependiente de otros diccionarios de cada uno de ellos
                /*bw.write("(PRIMER MÉTODO)PORCENTAJE DE PALABRAS ENCONTRADAS INDEPENDIENTEMENTE EN CADA UNO DE LOS DICCIONARIOS");;
				bw.newLine();
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(spanishDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de español.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(catalanDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de catalán.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(frenchDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de francés.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(galicianDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de gallego.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(englishDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de inglés.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(valencianDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de valenciano.");;
				bw.newLine();
				bw.write("El " + String.valueOf(percentageWords(euskeraDictionary, textWords)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de euskera.");;
				bw.newLine();
				bw.newLine();*/

                //Creamos una lista con las distintas palabras del texto encontradas en alguno de los diccionarios
                //Y las distintas listas de palabras en cada uno de los diccionarios
                List<String> foundWords = new ArrayList<String>();
                List<String> spFound = getWordsInDictionary(spanishDictionary, textWords, foundWords);
                List<String> caFound = getWordsInDictionary(catalanDictionary, textWords, foundWords);
                List<String> frFound = getWordsInDictionary(frenchDictionary, textWords, foundWords);
                List<String> gaFound = getWordsInDictionary(galicianDictionary, textWords, foundWords);
                List<String> enFound = getWordsInDictionary(englishDictionary, textWords, foundWords);
                List<String> vaFound = getWordsInDictionary(valencianDictionary, textWords, foundWords);
                List<String> euFound = getWordsInDictionary(euskeraDictionary, textWords, foundWords);
                for (String word : foundWords) {
                    System.out.println(word);
                }

                //bw.write("PALABRAS(DISTINTAS) TOTALES ENCONTRADAS EN ALGUNO DE LOS DICCIONARIOS: " + foundWords.size());
                System.out.println("PALABRAS(DISTINTAS) TOTALES ENCONTRADAS EN ALGUNO DE LOS DICCIONARIOS: " + foundWords.size());
                //bw.newLine();
                //bw.newLine();
                //bw.write("PORCENTAJE DE PALABRAS ENCONTRADAS EN CADA UNO DE LOS DICCIONARIOS DEPENDIENDO DEL NÚMERO TOTAL DE PALABRAS ENCONTRADAS EN ALGÚN DICCIONARIO");;
                System.out.println("PORCENTAJE DE PALABRAS ENCONTRADAS EN CADA UNO DE LOS DICCIONARIOS DEPENDIENDO DEL NÚMERO TOTAL DE PALABRAS ENCONTRADAS EN ALGÚN DICCIONARIO");
                ;
                //bw.newLine();
                //bw.newLine();
                System.out.println("URL: " + file);

                if (foundWords.size() != 0) {
                    //bw.write("El " + String.valueOf(new BigDecimal(spFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de español.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(spFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de español.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(caFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de catalán.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(caFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de catalán.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(frFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de francés.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(frFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de francés.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(gaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de gallego.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(gaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de gallego.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(enFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de inglés.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(enFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de inglés.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(vaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de valenciano.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(vaFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de valenciano.");
                    ;
                    //bw.newLine();
                    //bw.write("El " + String.valueOf(new BigDecimal(euFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", ""); + "% de las palabras se han encontrado en el diccionario de euskera.");;
                    System.out.println("El " + String.valueOf(new BigDecimal(euFound.size()).divide(new BigDecimal(foundWords.size()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2)).replace(".00", "") + "% de las palabras se han encontrado en el diccionario de euskera.");
                    ;
                    //bw.newLine();
                    //bw.newLine();
                } else {
                    //bw.write("NO SE HAN ENCONTRADO COINCIDENCIAS EN NINGÚN DICCIONARIO");;
                    System.out.println("NO SE HAN ENCONTRADO COINCIDENCIAS EN NINGÚN DICCIONARIO");
                    ;
                }
            }
            //bw.flush();
        } catch (Exception e) {
            System.out.println("ERROR AL GENERAR LOS RESULTADOS");
            ;
        } finally {
			/*try {
				//bw.close();
			} catch (IOException e) {
				System.out.println("ERROR AL CERRAR EL FICHEO DE RESULTADOS");;
			}*/
        }
    }

    private static void initializeProxyProperties() {
        System.setProperty("http.proxyHost", "172.23.50.61");
        ;
        System.setProperty("http.proxyPort", "8080");
        ;
        System.setProperty("https.proxyHost", "172.23.50.61");
        ;
        System.setProperty("https.proxyPort", "8080");
        ;
    }

    private static String getUrlContent(String url) throws MalformedURLException, IOException, SAXException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.addRequestProperty("Accept", "text/html");
        ;
        connection.addRequestProperty("Accept-Language", "es,en;q=0.8,es-es;q=0.5,en-us;q=0.3");
        ;
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.1.3) Gecko/20090915 Firefox/3.5.3 (.NET CLR 3.5.30729) ");
        ;
        connection.connect();
        InputStream content = (InputStream) connection.getInputStream();
        BufferedInputStream stream = new BufferedInputStream(content);
        String contenido = StringUtils.getContentAsString(stream);

        InputSource inputSource = new InputSource(new StringReader(contenido));
        DOMParser parser = new DOMParser(new HTMLConfiguration());
        parser.parse(inputSource);

        Document document = parser.getDocument();
        return getTextFromDOM(document);
    }
	
	/*private static String getAttributesTextFromDOM(String content) {
		StringBuffer result = new StringBuffer();
		try {
			InputSource inputSource = new InputSource(new StringReader(content));
			
			DOMParser parser = new DOMParser(new HTMLConfiguration());
			parser.parse(inputSource);
			
			Document document = parser.getDocument();
			
			PropertiesManager pmgr = new PropertiesManager();
			
			List<Node> nodes = new ArrayList<Node>();
			nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE);
			
			List<String> attributes = Arrays.asList(pmgr.getValue("multilanguage.properties","text.attributes");.split(";"););
			
			for(Node node : nodes) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					for(String attribute : attributes) {
						if(((Element) node).hasAttribute(attribute)) {
							result.append(((Element) node).getAttribute(attribute).trim() + ".\n");;
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al generar el texto del documento a partir de su árbol DOM", MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		
		return result.toString();
	}
	
	private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements) {
		if((node != null) && (nodeList.size() <= maxNumElements)) {
			if ((((node.getNodeType() == Node.ELEMENT_NODE) ||(node.getNodeType() == Node.DOCUMENT_NODE) ||
					(node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))){
				for (int x = 0; x < node.getChildNodes().getLength(); x++) {
					generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements);
				}
				if(node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
					nodeList.add(node);
				}
			}
		}
		return nodeList;
	}
	
	private static String removeTags (String content) {	
		content = Pattern.compile("<script[^>]*>(.*?)</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll("");;
		content = Pattern.compile("<style[^>]*>(.*?)</style>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll("");;
		content = Pattern.compile("</{0,1}[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");;
		content = Pattern.compile(" {2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");;
		content = Pattern.compile("[\\n\\r\\t ]{2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(".\n");;
		return content;
	}*/

    private static String getTextFromDOM(Document document) {
        StringBuffer result = new StringBuffer();
        try {
            PropertiesManager pmgr = new PropertiesManager();

            List<String> ignoredTags = Arrays.asList(pmgr.getValue("multilanguage.properties", "ignored.tags").split(";"));
            List<Node> nodes = new ArrayList<Node>();
            nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE, ignoredTags);

            List<String> attributes = Arrays.asList(pmgr.getValue("multilanguage.properties", "text.attributes").split(";"));

            for (Node node : nodes) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    for (String attribute : attributes) {
                        if (element.hasAttribute(attribute)) {
                            result.append(element.getAttribute(attribute).trim() + ".\n");
                        }
                    }
                    if (StringUtils.isNotEmpty(element.getTextContent())) {
                        NodeList children = element.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) {
                            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                                result.append(children.item(i).getTextContent() + " ");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al generar el texto del documento a partir de su árbol DOM", MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return HTMLEntities.unhtmlentities(result.toString());
    }

    private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements, List<String> ignoredTags) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                    (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements, ignoredTags);
                }
                if (node.getNodeType() == Node.TEXT_NODE || (node.getNodeType() == Node.ELEMENT_NODE && !ignoredTags.contains(node.getNodeName().toUpperCase()))) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    /**
     * Incluye en la lista de palabras las encontradas en el diccionario que se le pasa si éstas no
     * están ya en la lista. Tambi´n devuelve las encontradas en un diccionario en concreto.
     *
     * @param dictionary
     * @param textWords
     * @param foundWords
     */

    private static List<String> getWordsInDictionary(Map<String, List<String>> dictionary, Map<String, Integer> textWords, List<String> foundWords) {
        List<String> dictionaryWords = new ArrayList<String>();
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

    /**
     * Carga en un mapa las distintas palabras que aparecen en el texto y el número de ocurrencias
     *
     * @param textFile
     * @return
     */

    private static Map<String, Integer> loadText(String text) {
        TreeMap<String, Integer> textMap = new TreeMap<String, Integer>();
        try {
            List<String> textList = new ArrayList<String>();
            textList = Arrays.asList(text.split("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜàèìòùÀÈÌÒÙçÇ·]+"));

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
            System.out.println("ERROR AL RECUPERAR EL TEXTO.");
            e.printStackTrace();
        }
        return textMap;
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
	
	/*private static BigDecimal percentageWords(Map<String,List<String>> dictionary, Map<String, Integer> textWords){
		BigDecimal numLanguageWords = new BigDecimal(0);
		BigDecimal numTextWords = new BigDecimal(0);
		for(String word: textWords.keySet()){
			if (exist(dictionary, word)){
				numLanguageWords = numLanguageWords.add(new BigDecimal(textWords.get(word)));
			}
			numTextWords = numTextWords.add(new BigDecimal(textWords.get(word)));
		}
		if (numTextWords.compareTo(new BigDecimal(0)) == 0){
			return new BigDecimal(0);
		}else{
			return numLanguageWords.divide(numTextWords, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
		}
	}*/

    private static boolean exist(Map<String, List<String>> dictionary, String word) {
        if (dictionary.get(word.substring(0, 1)) != null) {
            if (dictionary.get(word.substring(0, 1).toUpperCase()).contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static void setTrustingAllCerts() {
        Logger.putLog("Configurando la aplicación para que no valide los certificados en SSL.", PercentageWords.class, Logger.LOG_LEVEL_INFO);
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            Logger.putLog("Excepción: ", PercentageWords.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static void setDefaultHostnameVerifier() {
        HostnameVerifier hv = new HostnameVerifier() {
            // No validamos el nombre de HOST
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}
