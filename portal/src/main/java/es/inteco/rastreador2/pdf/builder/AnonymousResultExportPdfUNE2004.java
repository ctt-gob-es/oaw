package es.inteco.rastreador2.pdf.builder;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;

/**
 * Created by mikunis on 11/12/14.
 */
public class AnonymousResultExportPdfUNE2004 extends AnonymousResultExportPdf {
	public AnonymousResultExportPdfUNE2004() {
		super(new BasicServiceForm());
	}

	public AnonymousResultExportPdfUNE2004(final BasicServiceForm basicServiceForm) {
		super(basicServiceForm);
	}

	@Override
	public void createIntroductionChapter(MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("ob.resAnon.intav.report.chapter1.title"), pdfTocManager, titleFont);
		createChapter1(messageResources, chapter);
		Section section1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter11.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1);
		createSection11(messageResources, section1);
		document.add(chapter);
	}

	protected void createChapter1(MessageResources resources, Chapter chapter) {
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p1.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWords = new ArrayList<>();
		boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p2.bold"));
		Paragraph p = PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p2"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		Phrase ph = new Phrase(resources.getMessage("ob.resAnon.intav.report.1.p2.m1"), ConstantsFont.paragraphUnderlinedFont);
		p.add(ph);
		chapter.add(p);
		boldWords = new ArrayList<>();
		boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p3.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		if (isBasicService()) {
			PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.1.p4"), ConstantsFont.PARAGRAPH, chapter);
		}
		// PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.1.p5"), ConstantsFont.PARAGRAPH, chapter);
	}

	protected void createSection11(MessageResources messageResources, Section section) {
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.11.p4.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.11.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.11.p5"), ConstantsFont.PARAGRAPH, section);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.11.p1.bold"));
		// section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p1"), boldWords, ConstantsFont.paragraphBoldFont,
		// ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.11.p1"), ConstantsFont.PARAGRAPH, section);
	}

	@Override
	public void createObjetiveChapter(final MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont, java.util.List<ObservatoryEvaluationForm> evaList,
			long observatoryType) throws DocumentException {
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("ob.resAnon.intav.report.chapter2.title"), pdfTocManager, titleFont);
		createChapter2(messageResources, chapter, observatoryType);
		document.add(chapter);
	}

	protected void createChapter2(final MessageResources messageResources, Chapter chapter, long observatoryType) {
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
		chapter.add(
				PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.PARAGRAPH, chapter);
		} else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.PARAGRAPH, chapter);
		}
	}

	@Override
	public void createMethodologyChapter(final MessageResources resources, Document document, PdfTocManager pdfTocManager, Font titleFont,
			java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(resources.getMessage("ob.resAnon.intav.report.chapter3.title"), pdfTocManager, titleFont);
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.3.p1"),
				Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.3.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p2"), ConstantsFont.PARAGRAPH, chapter);
		if (!isBasicService) {
			Section section1 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter31.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
					pdfTocManager.addSection(), 1);
			if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
				createSection31(resources, section1, observatoryType, "AGE");
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
				createSection31(resources, section1, observatoryType, "CCAA");
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_EELL) {
				createSection31(resources, section1, observatoryType, "EELL");
			}
		}
		Section section2 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter32.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1);
		createSection32(resources, section2, primaryReportPageList, observatoryType, isBasicService);
		Section section3 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter33.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1, resources.getMessage("anchor.met.table"));
		createSection33(resources, section3, observatoryType);
		Section section31 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter331.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3,
				pdfTocManager.addSection(), 2);
		createSection331(resources, section31);
		Section section4 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter34.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1);
		createSection34(resources, section4);
		Section section41 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter341.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				pdfTocManager.addSection(), 2);
		createSection341(resources, section41);
		Section section42 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter342.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				pdfTocManager.addSection(), 2);
		createSection342(resources, section42);
		// Solo sale en el agregado
		if (primaryReportPageList == null) {
			Section section43 = PDFUtils.createSection(resources.getMessage("ob.resAnon.intav.report.chapter343.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
					pdfTocManager.addSection(), 2);
			createSection343(resources, section43);
		}
		document.add(chapter);
	}

	protected void createSection32(final MessageResources messageResources, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType,
			boolean isBasicService) {
		ArrayList<String> boldWords = new ArrayList<>();
		if (!isBasicService) {
			boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p1.bold"));
			section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont,
					ConstantsFont.PARAGRAPH, true));
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.PARAGRAPH, section);
		} else {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.PARAGRAPH, section);
		}
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.PARAGRAPH, section);
		com.itextpdf.text.List list = new com.itextpdf.text.List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p5.bold"));
		list.add(PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p6.bold"));
		list.add(PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.PARAGRAPH, section);
		PropertiesManager pmgr = new PropertiesManager();
		if (!isBasicService) {
			section.newPage();
		}
		PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.grafico.rastreo"), messageResources.getMessage("ob.resAnon.intav.report.32.img.alt"), 60);
		if (!isBasicService && observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p8"), ConstantsFont.PARAGRAPH, section);
			com.itextpdf.text.List listp8 = new com.itextpdf.text.List();
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l1"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l2"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l3"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l4"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l5"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l6"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l7"), listp8, ConstantsFont.PARAGRAPH);
			listp8.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
			section.add(listp8);
		}
		if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.PARAGRAPH, section);
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.PARAGRAPH, section);
		}
		section.newPage();
		if (primaryReportPageList != null) {
			PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.PARAGRAPH, section);
			section.add(addURLTable(messageResources, primaryReportPageList));
			section.newPage();
		}
	}

	protected Section createSection33(final MessageResources messageResources, Section section, long observatoryType) throws BadElementException, IOException {
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.PARAGRAPH, section);
		com.itextpdf.text.List list = new com.itextpdf.text.List();
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p2"));
		ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		com.itextpdf.text.List list2 = new com.itextpdf.text.List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p4"));
		ListItem itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p6"));
		itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		item.add(list2);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p8"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list2 = new com.itextpdf.text.List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p10"));
		itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p12"));
		itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		item.add(list2);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.PARAGRAPH, section);
		list = new com.itextpdf.text.List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p15"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p17"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p18"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p19"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p21"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p23"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p25"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p27.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.33.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p28"), ConstantsFont.PARAGRAPH, section);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold1"));
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold2"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p30.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.33.p30"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.PARAGRAPH, section);
		section.newPage();
		PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
		section.add(createMethodologyTable1(messageResources));
		PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
		section.add(createMethodologyTable2(messageResources));
		return section;
	}

	protected PdfPTable createMethodologyTable1(final MessageResources messageResources) {
		float[] widths = { 10f, 30f, 45f, 25f, 15f, 15f };
		PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		try {
			createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title1"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.111", "ob.resAnon.intav.report.33.table.111.name", "ob.resAnon.intav.report.33.table.111.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.111.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.111.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.111.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.112", "ob.resAnon.intav.report.33.table.112.name", "ob.resAnon.intav.report.33.table.112.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.112.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.112.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.112.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.113", "ob.resAnon.intav.report.33.table.113.name", "ob.resAnon.intav.report.33.table.113.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.113.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.113.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.113.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.114", "ob.resAnon.intav.report.33.table.114.name", "ob.resAnon.intav.report.33.table.114.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.114.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.114.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.114.modality"));
			createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title2"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.121", "ob.resAnon.intav.report.33.table.121.name", "ob.resAnon.intav.report.33.table.121.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.121.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.121.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.121.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.122", "ob.resAnon.intav.report.33.table.122.name", "ob.resAnon.intav.report.33.table.122.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.122.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.122.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.122.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.123", "ob.resAnon.intav.report.33.table.123.name", "ob.resAnon.intav.report.33.table.123.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.123.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.123.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.123.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.124", "ob.resAnon.intav.report.33.table.124.name", "ob.resAnon.intav.report.33.table.124.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.124.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.124.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.124.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.125", "ob.resAnon.intav.report.33.table.125.name", "ob.resAnon.intav.report.33.table.125.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.125.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.125.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.125.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.126", "ob.resAnon.intav.report.33.table.126.name", "ob.resAnon.intav.report.33.table.126.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.126.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.126.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.126.modality"));
		} catch (Exception e) {
			Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return table;
	}

	protected PdfPTable createMethodologyTable2(final MessageResources messageResources) {
		float[] widths = { 10f, 30f, 45f, 25f, 15f, 15f };
		PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		try {
			createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title1"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.211", "ob.resAnon.intav.report.33.table.211.name", "ob.resAnon.intav.report.33.table.211.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.211.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.211.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.211.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.212", "ob.resAnon.intav.report.33.table.212.name", "ob.resAnon.intav.report.33.table.212.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.212.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.212.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.212.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.213", "ob.resAnon.intav.report.33.table.213.name", "ob.resAnon.intav.report.33.table.213.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.213.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.213.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.213.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.214", "ob.resAnon.intav.report.33.table.214.name", "ob.resAnon.intav.report.33.table.214.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.214.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.214.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.214.modality"));
			createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title2"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.221", "ob.resAnon.intav.report.33.table.221.name", "ob.resAnon.intav.report.33.table.221.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.221.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.221.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.221.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.222", "ob.resAnon.intav.report.33.table.222.name", "ob.resAnon.intav.report.33.table.222.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.222.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.222.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.222.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.223", "ob.resAnon.intav.report.33.table.223.name", "ob.resAnon.intav.report.33.table.223.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.223.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.223.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.223.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.224", "ob.resAnon.intav.report.33.table.224.name", "ob.resAnon.intav.report.33.table.224.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.224.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.224.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.224.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.225", "ob.resAnon.intav.report.33.table.225.name", "ob.resAnon.intav.report.33.table.225.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.225.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.225.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.225.modality"));
			createMethodologyTableRow(messageResources, table, "ob.resAnon.intav.report.33.table.id.226", "ob.resAnon.intav.report.33.table.226.name", "ob.resAnon.intav.report.33.table.226.question",
					createTextList(messageResources, "ob.resAnon.intav.report.33.table.226.answer"), createTextList(messageResources, "ob.resAnon.intav.report.33.table.226.value"),
					createImageList(messageResources, "ob.resAnon.intav.report.33.table.226.modality"));
		} catch (Exception e) {
			Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return table;
	}

	private void createSection331(final MessageResources messageResource, Section section) throws BadElementException, IOException {
		PDFUtils.addParagraph(messageResource.getMessage("ob.resAnon.intav.report.331.p1"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addParagraph(messageResource.getMessage("ob.resAnon.intav.report.331.p2"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.createTitleTable(messageResource.getMessage("ob.resAnon.intav.report.331.table.title"), section, 450);
		section.add(create331Table(messageResource));
	}

	private PdfPTable create331Table(final MessageResources messageResources) {
		float[] widths = { 40f, 30f, 30f };
		PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		create331HaderTable(messageResources, table);
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.1.1", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.111"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.111"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.1.2", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.112"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.112"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.1.3", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.113"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.113"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.1.4", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.114"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.114"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.1", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.121"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.121"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.2", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.122"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.122"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.3", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.123"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.123"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.4", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.124"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.124"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.5", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.125"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.125"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.1.2.6", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.126"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.126"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.1.1", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.211"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.211"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.1.2", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.212"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.212"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.1.3", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.213"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.213"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.1.4", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.214"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.214"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.1", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.221"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.221"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.2", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.222"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.222"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.3", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.223"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.223"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.4", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.224"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.224"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.5", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.225"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.225"));
		create331TableRow(messageResources, table, "inteco.observatory.subgroup.2.2.6", createTextList(messageResources, "ob.resAnon.intav.report.331.table.req.226"),
				createTextList(messageResources, "ob.resAnon.intav.report.331.table.verP.226"));
		return table;
	}

	protected void create331HaderTable(final MessageResources messageResources, final PdfPTable table) {
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.331.table.header3"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
	}

	protected void create331TableRow(final MessageResources messageResources, PdfPTable table, String verification, com.itextpdf.text.List req, com.itextpdf.text.List verP) {
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 10, -1));
		table.addCell(PDFUtils.createListTableCell(req, Color.WHITE, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
	}

	protected void createSection342(final MessageResources messageResources, Section section) {
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p1"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p2"), ConstantsFont.PARAGRAPH, section);
		List list = new List();
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p3"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p4"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p5"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p6"), list, ConstantsFont.PARAGRAPH, false, true);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p7.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p8.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		list = new List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p9.bold"));
		ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p10.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p11.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p12.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		list = new List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p13.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p14.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p15.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p15"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p16.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p17"), ConstantsFont.PARAGRAPH, section);
		list = new List();
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p18"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p19"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p20"), list, ConstantsFont.PARAGRAPH, false, true);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p21"), ConstantsFont.PARAGRAPH, section);
		PropertiesManager pmgr = new PropertiesManager();
		PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "VNP.png", "VNP = SP/NP", 80);
		list = new List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p22.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p23.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p24.bold"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p25"), ConstantsFont.PARAGRAPH, section);
		list = new List();
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold1"));
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold2"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold1"));
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold2"));
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold3"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold1"));
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold2"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
	}

	@Override
	public void getMidsComparationByVerificationLevelGraphic(MessageResources messageResources, String level, String title, String filePath, String noDataMess,
			java.util.List<ObservatoryEvaluationForm> evaList, String value, boolean b) throws Exception {
	}

	@Override
	protected void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, java.util.List<ObservatoryEvaluationForm> evaList) {
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
	}

	@Override
	public String getTitle() {
		return "UNE 139803:2004";
	}

	@Override
	public void createIntroductionChapter(MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont, boolean isBasicService) throws Exception {
		this.createIntroductionChapter(messageResources, document, pdfTocManager, titleFont);
	}
}
