package es.ctic.rastreador2.pdf.utils;

import com.lowagie.text.pdf.events.IndexEvents;

/**
 * Created by mikunis on 1/16/17.
 */
public class PdfTocManager {

    private final IndexEvents index;
    private int numChapter = 1;
    private int numSection = 1;

    public PdfTocManager(final IndexEvents index) {
        this.index = index;
    }

    public IndexEvents getIndex() {
        return index;
    }

    public void addChapterCount() {
        numChapter++;
    }

    public void addSectionCount() {
        numSection++;
    }

    public int getNumSection() {
        return numSection;
    }

    public int getNumChapter() {
        return numChapter;
    }

    public int addSection() {
        return numSection++;
    }
}
