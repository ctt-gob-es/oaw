package es.inteco.rastreador2.imp.xml.result;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public final class ImportSectionSetFromXml {

    static int sectionNumber = 1;

    private ImportSectionSetFromXml() {
    }

    public static SectionSetForm createSectionSetForm(HttpServletRequest request, String path, String defaultPath, boolean isHtml) throws Exception {
        SectionSetForm introduction = new SectionSetForm();

        final NodeList sectionSetList = ImportFromXmlUtils.getDocumentElements(path, "sectionSet", defaultPath);
        if (sectionSetList != null && sectionSetList.getLength() != 0) {
            final NodeList chapterList = ((Element) sectionSetList.item(0)).getElementsByTagName("chapter");
            if (chapterList != null && chapterList.getLength() != 0) {
                for (int i = 0; i < chapterList.getLength(); i++) {
                    SectionForm chapter = new SectionForm();
                    chapter.setSectionNumber(sectionNumber++);
                    Element element = (Element) chapterList.item(i);
                    NodeList elementList = element.getChildNodes();
                    for (int j = 0; j < elementList.getLength(); j++) {
                        if (elementList.item(j).getNodeType() == Element.ELEMENT_NODE) {
                            chapter = ImportFromXmlUtils.dealElement(request, (Element) elementList.item(j), chapter, isHtml);
                        }
                    }
                    List<SectionForm> sectionFormList = introduction.getSectionList();
                    sectionFormList.add(chapter);
                    introduction.setSectionList(sectionFormList);
                }
                sectionNumber = 1;
                return introduction;
            }
        }
        return null;
    }

}
