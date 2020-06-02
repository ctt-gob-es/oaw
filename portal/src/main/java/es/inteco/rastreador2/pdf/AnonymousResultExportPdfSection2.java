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
package es.inteco.rastreador2.pdf;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.Chapter;

import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

public final class AnonymousResultExportPdfSection2 {
	private AnonymousResultExportPdfSection2() {
	}

	protected static void createChapter2(HttpServletRequest request, Chapter chapter, long observatoryType) {
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		if (observatoryType == Constants.OBSERVATORY_TYPE_PRENSA) {
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p2.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
		} else {
			// PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p2"), ConstantsFont.PARAGRAPH, chapter);
			// PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3"), ConstantsFont.PARAGRAPH, chapter);
			if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
				// PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.AGE"), ConstantsFont.PARAGRAPH, chapter);
				PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.PARAGRAPH, chapter);
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
				PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.PARAGRAPH, chapter);
				PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.PARAGRAPH, chapter);
			}
		}
	}
}
