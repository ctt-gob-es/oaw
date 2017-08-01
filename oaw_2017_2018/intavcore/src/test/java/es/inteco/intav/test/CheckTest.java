package es.inteco.intav.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


public class CheckTest {

    public static void main(String[] args) throws Exception {

        File f = new File("/usr/share/eclipse/workspace/IntavCore/resources/checks/checks.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document checks = builder.parse(f);
        Element nodeRoot = checks.getDocumentElement();

        NodeList checkList = nodeRoot.getElementsByTagName("CHECK");

		/*for (int i=404; i< 412; i++){
            for (int j = 0; j < checkList.getLength(); j++){
				if (Integer.valueOf(((Element)checkList.item(j)).getAttribute("id")) == i){
					System.out.println("ID: " + ((Element)checkList.item(j)).getAttribute("id"));
					System.out.println("TIPO: " + ((Element)checkList.item(j)).getAttribute("confidence"));
					String messageKey = null;
					if (((Element)checkList.item(j)).getElementsByTagName("ERROR") != null &&
							((Element)checkList.item(j)).getElementsByTagName("ERROR").getLength() > 0){
						messageKey = ((Element)checkList.item(j)).getElementsByTagName("ERROR").item(0).getTextContent();
					}
					String rationaleKey = null;
					if (((Element)checkList.item(j)).getElementsByTagName("RATIONALE") != null &&
							((Element)checkList.item(j)).getElementsByTagName("RATIONALE").getLength() > 0){
						rationaleKey = ((Element)checkList.item(j)).getElementsByTagName("RATIONALE").item(0).getTextContent();
					}
					if (messageKey != null && !messageKey.isEmpty()){
						System.out.println("MESSAGE: " + getText(messageKey));
					}else{
						System.out.println("No message");
					}
					if (rationaleKey != null && !rationaleKey.isEmpty()){
						System.out.println("RATIONALE: " + getText(rationaleKey));
					}else{
						System.out.println("No rationale");
					}
				
					if (((Element)checkList.item(j)).getElementsByTagName("TRIGGER") != null &&
							((Element)checkList.item(j)).getElementsByTagName("TRIGGER").getLength() > 0){
						System.out.println("ELEMENT: " + ((Element)((Element)checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element"));
					}
					
					getReglas(checks, (Element)checkList.item(j));
					
					getMetodologies(i);
					
					getPrerequisite((Element)checkList.item(j));
					
					System.out.println("------------------------------------------------------------------------------");
				}
			}
		}*/

        // checks por elemento
        printChecksByElement(checkList);

    }

    private static void printChecksByElement(NodeList checkList) {
        HashMap<String, String> checkMap = new HashMap<String, String>();

        for (int j = 0; j < checkList.getLength(); j++) {
            if (((Element) checkList.item(j)).getElementsByTagName("TRIGGER") != null &&
                    ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").getLength() > 0 &&
                    ((Element) ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element") != null &&
                    !((Element) ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element").isEmpty()) {
                String check = ((Element) checkList.item(j)).getAttribute("id");
                if (checkMap.get(((Element) ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element")) != null) {
                    check = checkMap.get(((Element) ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element")) + "," + ((Element) checkList.item(j)).getAttribute("id");
                }
                checkMap.put(((Element) ((Element) checkList.item(j)).getElementsByTagName("TRIGGER").item(0)).getAttribute("element"), check);
            }
        }

        for (String element : checkMap.keySet()) {
            System.out.println(element.toUpperCase() + " --> " + checkMap.get(element));
        }
    }

    private static void getMetodologies(int checkId) throws Exception {
        File fWCAG1 = new File("/usr/share/eclipse/workspace/IntavCore/resources/guidelines/wcag-1-0.xml");
        File fUNE = new File("/usr/share/eclipse/workspace/IntavCore/resources/guidelines/une-139803.xml");
        File fObs = new File("/usr/share/eclipse/workspace/IntavCore/resources/guidelines/observatorio-inteco-1-0.xml");

        String metodolgies = "";

        if (existCheck(fObs, checkId)) {
            metodolgies += "Observatorio";
        }
        if (existCheck(fUNE, checkId)) {
            if (!metodolgies.isEmpty()) {
                metodolgies += ", ";
            }
            metodolgies += "UNE";
        }
        if (existCheck(fWCAG1, checkId)) {
            if (!metodolgies.isEmpty()) {
                metodolgies += ", ";
            }
            metodolgies += "WCAG 1.0";
        }
        System.out.println("METODOLOGIAS: " + metodolgies);
    }

    private static boolean existCheck(File f, int checkId) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document checks = builder.parse(f);
        Element nodeRoot = checks.getDocumentElement();

        NodeList checkList = nodeRoot.getElementsByTagName("CHECK");

        for (int i = 0; i < checkList.getLength(); i++) {
            if (Integer.valueOf(((Element) checkList.item(i)).getAttribute("id")) == checkId) {
                return true;
            }
        }

        return false;
    }

    private static void getPrerequisite(Element element) {
        NodeList prerequisiteList = (element).getElementsByTagName("PREREQUISITE");
        if (prerequisiteList != null && prerequisiteList.getLength() > 0) {
            for (int k = 0; k < prerequisiteList.getLength(); k++) {
                System.out.println("PREREQUISITO: " + ((Element) prerequisiteList.item(k)).getAttribute("id"));
            }
        } else {
            System.out.println("PREREQUISITO: ----");
        }
    }

    private static void getReglas(Document checks, Element element) {
        NodeList functionElement = (element).getElementsByTagName("MACHINE");
        if (functionElement != null && functionElement.getLength() > 0) {
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) checks.getImplementation();
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            lsSerializer.getDomConfig().setParameter("xml-declaration", false);
            System.out.println("REGLAS: ");
            for (int k = 0; k < functionElement.getLength(); k++) {
                System.out.println(lsSerializer.writeToString(functionElement.item(k)));
            }
        } else {
            System.out.println("REGLAS: ----");
        }
    }

    private static String getText(String key) throws Exception {
        File f = new File("/usr/share/eclipse/workspace/intav/resources/ApplicationResources_es.properties");
        BufferedReader br = new BufferedReader(new FileReader(f));
        if (f.exists()) {
            String message = br.readLine();
            while (message != null) {
                if (message.startsWith(key)) {
                    return message;
                }
                message = br.readLine();
            }
        }
        return key;
    }

}
