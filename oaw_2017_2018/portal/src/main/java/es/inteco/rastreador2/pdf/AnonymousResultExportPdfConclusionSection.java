package es.inteco.rastreador2.pdf;

import com.lowagie.text.Chapter;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.rastreador2.imp.xml.result.SectionSetForm;

import javax.servlet.http.HttpServletRequest;

public final class AnonymousResultExportPdfConclusionSection {

    private AnonymousResultExportPdfConclusionSection() {
    }

    protected static int createSectionGlobalResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
        return countSections;
    }

    protected static int createSectionSegmentResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
        return countSections;
    }

    protected static int createSectionEvolutionResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
        return countSections;
    }

    private static void createConclusion(SectionSetForm conclusion, Section section) {
    }
}
