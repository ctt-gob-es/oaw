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
package es.inteco.rastreador2.imp.xml.result;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.ConstantsFont;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ImportFromXmlUtils.
 */
public final class ImportFromXmlUtils {

    /**
	 * Instantiates a new import from xml utils.
	 */
    private ImportFromXmlUtils() {
    }

    /**
	 * Serialize node list.
	 *
	 * @param nodeList the node list
	 * @return the string
	 */
    public static String serializeNodeList(NodeList nodeList) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && !nodeList.item(i).getNodeName().equalsIgnoreCase("anchor")) {
                DOMImplementationLS domImplementationLS = (DOMImplementationLS) nodeList.item(i).getOwnerDocument().getImplementation();
                LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
                lsSerializer.getDomConfig().setParameter("xml-declaration", false);
                lsSerializer.getDomConfig().setParameter("namespaces", false);
                text.append(lsSerializer.writeToString(nodeList.item(i)));
            } else if (nodeList.item(i).getNodeType() == Node.TEXT_NODE || nodeList.item(i).getNodeName().equalsIgnoreCase("anchor")) {
                text.append(nodeList.item(i).getTextContent());
            }
        }
        return text.toString();
    }

    /**
	 * Gets the document elements.
	 *
	 * @param path        the path
	 * @param element     the element
	 * @param defaultPath the default path
	 * @return the document elements
	 * @throws SAXException the SAX exception
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
    public static NodeList getDocumentElements(String path, String element, String defaultPath) throws SAXException, IOException {
        File configFile = new File(path);
        if (!configFile.exists() && defaultPath != null) {
            configFile = new File(defaultPath);
        } else if (!configFile.exists()) {
            return null;
        }
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new FileInputStream(configFile)));
        Document document = parser.getDocument();

        return document.getElementsByTagName(element);
    }

    /**
	 * Gets the title.
	 *
	 * @param element the element
	 * @return the title
	 */
    public static String getTitle(Element element) {
        if (element.getElementsByTagName("title") != null && element.getElementsByTagName("title").item(0).getTextContent() != null) {
            return element.getElementsByTagName("title").item(0).getTextContent();
        }
        return "";
    }

    /**
	 * Gets the special chunks.
	 *
	 * @param request   the request
	 * @param element   the element
	 * @param paragraph the paragraph
	 * @param text      the text
	 * @return the special chunks
	 */
    public static String getSpecialChunks(HttpServletRequest request, Element element, ParagraphForm paragraph, String text) {
        NodeList specialChunks = element.getChildNodes();
        List<Element> elementList = new ArrayList<>();
        for (int i = 0; i < specialChunks.getLength(); i++) {
            if (specialChunks.item(i).getNodeName().equalsIgnoreCase("strong") || specialChunks.item(i).getNodeName().equalsIgnoreCase("anchor")
                    || specialChunks.item(i).getNodeName().equalsIgnoreCase("underline")) {
                elementList.add((Element) specialChunks.item(i));
            }
        }

        int countAnchor = 1;
        int countDiferentFormat = 0;
        int key = 0;
        for (Element specialChunk : elementList) {
            Map<Integer, SpecialChunk> specialChunkMap = paragraph.getSpecialChunks();
            if (specialChunkMap == null) {
                specialChunkMap = new HashMap<>();
            }
            if (specialChunk.getNodeName().equalsIgnoreCase("anchor")) {
                if (specialChunk.getAttribute("external") == null || specialChunk.getAttribute("external").isEmpty()) {
                    SpecialChunk anchor = new SpecialChunk(specialChunk.getTextContent(), CrawlerUtils.getResources(request).getMessage(specialChunk.getAttribute("destination")), false, ConstantsFont.PARAGRAPH_ANCHOR_FONT);
                    specialChunkMap.put(key, anchor);
                } else {
                    SpecialChunk externalLink = new SpecialChunk(specialChunk.getTextContent(), ConstantsFont.PARAGRAPH_ANCHOR_FONT);
                    externalLink.setExternalLink(true);
                    externalLink.setAnchor(specialChunk.getAttribute("external"));
                    specialChunkMap.put(key, externalLink);
                }
                text = text.replace(specialChunk.getTextContent(), "[anchor" + countAnchor + "]");
                countAnchor++;
            } else if (specialChunk.getNodeName().equalsIgnoreCase("strong")) {
                SpecialChunk bold = new SpecialChunk(specialChunk.getTextContent(), ConstantsFont.paragraphBoldFont);
                specialChunkMap.put(key, bold);
                text = text.replace(specialChunk.getTextContent(), "{" + countDiferentFormat + "}");
                countDiferentFormat++;
            } else if (specialChunk.getNodeName().equalsIgnoreCase("undeline")) {
                SpecialChunk underline = new SpecialChunk(specialChunk.getTextContent(), ConstantsFont.paragraphUnderlinedFont);
                specialChunkMap.put(key, underline);
                text = text.replace(specialChunk.getTextContent(), "{" + countDiferentFormat + "}");
                countDiferentFormat++;
            }
            paragraph.setSpecialChunks(specialChunkMap);
            key++;
        }
        return text;
    }

    /**
	 * Deal paragraph node.
	 *
	 * @param request the request
	 * @param element the element
	 * @param section the section
	 * @param isHtml  the is html
	 * @return the section form
	 */
    private static SectionForm dealParagraphNode(HttpServletRequest request, Element element, SectionForm section, boolean isHtml) {
        ParagraphForm paragraph = new ParagraphForm();
        String text = "";
        if (!isHtml) {
            text = HTMLEntities.unhtmlentities(element.getTextContent());
            text = ImportFromXmlUtils.getSpecialChunks(request, element, paragraph, text.trim());
        } else {
            text = ImportFromXmlUtils.serializeNodeList(element.getChildNodes());
        }
        paragraph.setParagraph(text.trim());
        section.addObject(paragraph);

        return section;
    }

    /**
	 * Deal element.
	 *
	 * @param request the request
	 * @param element the element
	 * @param section the section
	 * @param isHtml  the is html
	 * @return the section form
	 */
    public static SectionForm dealElement(HttpServletRequest request, Element element, SectionForm section, boolean isHtml) {
        if (element.getNodeName().equals("title")) {
            return dealTitleNode(element, section);
        } else if (element.getNodeName().equals("section")) {
            return dealSectionNode(request, element, section, isHtml);
        } else if (element.getNodeName().equals("paragraph")) {
            return dealParagraphNode(request, element, section, isHtml);
        } else {
            return section;
        }
    }

    /**
	 * Deal title node.
	 *
	 * @param element the element
	 * @param section the section
	 * @return the section form
	 */
    private static SectionForm dealTitleNode(Element element, SectionForm section) {
        section.setTitle(element.getTextContent());
        return section;
    }

    /**
	 * Deal section node.
	 *
	 * @param request the request
	 * @param element the element
	 * @param section the section
	 * @param isHtml  the is html
	 * @return the section form
	 */
    private static SectionForm dealSectionNode(HttpServletRequest request, Element element, SectionForm section, boolean isHtml) {
        SectionForm subSection = new SectionForm();
        subSection.setSectionNumber(ImportSectionSetFromXml.sectionNumber++);
        NodeList elementList = element.getChildNodes();
        for (int j = 0; j < elementList.getLength(); j++) {
            if (elementList.item(j).getNodeType() == Element.ELEMENT_NODE) {
                ImportFromXmlUtils.dealElement(request, (Element) elementList.item(j), subSection, isHtml);
            }
        }
        section.addObject(subSection);
        return section;
    }

}
