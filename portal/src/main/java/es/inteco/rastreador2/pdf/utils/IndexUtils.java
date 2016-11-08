package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.IndexEvents;
import com.lowagie.text.pdf.events.IndexEvents.Entry;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import org.apache.struts.util.MessageResources;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class IndexUtils {

    private IndexUtils() {
    }

    public static void createIndex(final PdfWriter writer, final Document document, final MessageResources resources, final IndexEvents index, final Font titleFont) throws DocumentException {
        int beforeIndex = writer.getPageNumber();
        document.newPage();
        final Paragraph indexTitle = new Paragraph(resources.getMessage("pdf.accessibility.index.title"), titleFont);
        indexTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(indexTitle);

        final PdfPTable indexTable = createIndexTable(index);
        document.add(indexTable);

        try {
            //Situa el index al principio del PDF
            int totalPages = writer.getPageNumber();
            if (totalPages == beforeIndex) {
                beforeIndex = beforeIndex - 1;
            }
            int[] reorder = new int[totalPages];
            reorder[0] = 1;
            for (int i = 1; i < totalPages; i++) {
                reorder[i] = i + beforeIndex;
                if (reorder[i] > totalPages) {
                    reorder[i] -= totalPages - 1;
                }
            }
            document.newPage();
            writer.reorderPages(reorder);
        } catch (DocumentException de) {
            //Para el problema de si la ultima pagina del indice esta
            //completa y se introduce una pagina que en realidad no existe
            int totalPages = writer.getPageNumber() - 1;
            int[] reorder = new int[totalPages];
            reorder[0] = 1;
            for (int i = 1; i < totalPages; i++) {
                reorder[i] = i + beforeIndex;
                if (reorder[i] > totalPages) {
                    reorder[i] -= totalPages - 1;
                }
            }
            document.newPage();
            writer.reorderPages(reorder);
        }
    }

    private static PdfPTable createIndexTable(IndexEvents index) {
        ExportPageEventsObservatoryMP.setPrintFooter(false);

        float[] tamTabla = {30f, 3f};
        PdfPTable indexTable = new PdfPTable(tamTabla);
        indexTable.setWidthPercentage(100);

        EntryComparator entryComparator = new EntryComparator();
        index.setComparator(entryComparator);
        java.util.List<Entry> list = index.getSortedEntries();


        try {
            if (list.get(0).getIn1().contains("@&") || list.get(0).getIn1().contains("@1&") || list.get(0).getIn1().contains("@2&")) {
                Collections.sort(list, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        final int o1Num = getIndexNumberFromEntry(o1);
                        final int o2Num = getIndexNumberFromEntry(o2);

                        return o1Num - o2Num;
                    }

                    private int getIndexNumberFromEntry(final Entry entry) {
                        if (entry.getKey().contains("@&")) {
                            return Integer.valueOf(entry.getKey().substring(0, entry.getKey().indexOf("@&")));
                        } else if (entry.getKey().contains("@1&")) {
                            return Integer.valueOf(entry.getKey().substring(0, entry.getKey().indexOf("@1&")));
                        } else {
                            return Integer.valueOf(entry.getKey().substring(0, entry.getKey().indexOf("@2&")));
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.putLog("Excepción al crear el índice", IndexUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }


        int chapter = 1;
        int sectionL1 = 1;
        int sectionL2 = 1;
        for (int i = 0, n = list.size(); i < n; i++) {
            IndexEvents.Entry entry = list.get(i);
            Paragraph in = new Paragraph("", ConstantsFont.INDEX_ITEMS);
            String text = entry.getIn1();
            int identation = 0;
            if (entry.getIn1().contains("@&")) {
                text = text.substring(text.indexOf("@&") + 2, text.length());
                text = chapter + "." + text;
                chapter++;
                sectionL1 = 1;
            } else if (entry.getIn1().contains("@1&")) {
                text = text.substring(text.indexOf("@1&") + 3, text.length());
                identation = 1;
                text = (chapter - 1) + "." + sectionL1 + "." + text;
                sectionL1++;
                sectionL2 = 1;
            } else if (entry.getIn1().contains("@2&")) {
                text = text.substring(text.indexOf("@2&") + 3, text.length());
                identation = 2;
                text = (chapter - 1) + "." + (sectionL1 - 1) + "." + sectionL2 + "." + text;
                sectionL2++;
            }
            Chunk chunk = new Chunk(text);
            chunk.setLocalGoto(Constants.ANCLA_PDF + (i + 1));
            in.add(chunk);
            in.setIndentationLeft(identation * ConstantsFont.IDENTATION_LEFT_SPACE);
            PdfPCell cellText = new PdfPCell();
            cellText.addElement(in);
            cellText.setBorderWidth(0);
            indexTable.addCell(cellText);
            List pages = entry.getPagenumbers();
            List tags = entry.getTags();
            for (int p = 0; p < 1; p++) {
                Paragraph inPag = new Paragraph("", ConstantsFont.INDEX_ITEMS);
                Chunk pagenr = new Chunk(String.valueOf(Integer.parseInt(pages.get(p).toString()) - 1));
                pagenr.setLocalGoto((String) tags.get(p));
                inPag.setAlignment(Chunk.ALIGN_RIGHT);
                inPag.add(pagenr);
                PdfPCell cellPag = new PdfPCell();
                cellPag.addElement(inPag);
                cellPag.setBorderWidth(0);
                indexTable.addCell(cellPag);
            }
        }

        return indexTable;
    }


    private static class EntryComparator implements Comparator<IndexEvents.Entry> {
        public int compare(IndexEvents.Entry o1, IndexEvents.Entry o2) {
            if (Integer.parseInt(o1.getPagenumbers().get(0).toString()) > Integer.parseInt(o2.getPagenumbers().get(0).toString())) {
                return 1;
            }
            if (Integer.parseInt(o1.getPagenumbers().get(0).toString()) < Integer.parseInt(o2.getPagenumbers().get(0).toString())) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
