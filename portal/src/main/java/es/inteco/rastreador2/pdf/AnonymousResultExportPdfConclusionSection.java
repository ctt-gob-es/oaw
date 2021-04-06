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

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.events.IndexEvents;

import es.inteco.rastreador2.imp.xml.result.SectionSetForm;

/**
 * The Class AnonymousResultExportPdfConclusionSection.
 */
public final class AnonymousResultExportPdfConclusionSection {
	
	/**
	 * Instantiates a new anonymous result export pdf conclusion section.
	 */
	private AnonymousResultExportPdfConclusionSection() {
	}

	/**
	 * Creates the section global results.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param countSections the count sections
	 * @param conclusion    the conclusion
	 * @param chapter       the chapter
	 * @return the int
	 */
	protected static int createSectionGlobalResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
		return countSections;
	}

	/**
	 * Creates the section segment results.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param countSections the count sections
	 * @param conclusion    the conclusion
	 * @param chapter       the chapter
	 * @return the int
	 */
	protected static int createSectionSegmentResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
		return countSections;
	}

	/**
	 * Creates the section evolution results.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param countSections the count sections
	 * @param conclusion    the conclusion
	 * @param chapter       the chapter
	 * @return the int
	 */
	protected static int createSectionEvolutionResults(HttpServletRequest request, IndexEvents index, int countSections, SectionSetForm conclusion, Chapter chapter) {
		return countSections;
	}

	/**
	 * Creates the conclusion.
	 *
	 * @param conclusion the conclusion
	 * @param section    the section
	 */
	private static void createConclusion(SectionSetForm conclusion, Section section) {
	}
}
