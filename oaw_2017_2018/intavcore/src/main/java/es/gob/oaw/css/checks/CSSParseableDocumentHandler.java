/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.css.checks;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.CSSParseError;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;

import es.gob.oaw.css.CSSProblem;
import es.gob.oaw.css.CSSResource;
import es.gob.oaw.css.OAWCSSVisitor;
import es.inteco.common.logging.Logger;
import org.dom4j.Document;

import java.util.Date;
import java.util.List;

/**
 * Clase para comprobar si un código CSS es parseable o no (se ajusta a la gramática CSS)
 */
public class CSSParseableDocumentHandler extends OAWCSSVisitor {

    @Override
    public List<CSSProblem> evaluate(final Document document, final CSSResource cssResource) {
        if (!cssResource.getContent().isEmpty()) {
            try {
                resource = cssResource;
                final CollectingCSSParseErrorHandler errorHandler = new CollectingCSSParseErrorHandler();
                final CascadingStyleSheet aCSS = CSSReader.readFromString(cssResource.getContent(), ECSSVersion.CSS30, errorHandler);
                if (aCSS != null) {
                    CSSVisitor.visitCSS(aCSS, this);
                    for (CSSParseError cssParseError : errorHandler.getAllParseErrors()) {
                        getProblems().add(createCSSParserError(cssParseError));
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Error al intentar parsear el CSS", CSSParseableDocumentHandler.class, Logger.LOG_LEVEL_INFO);
            }
        }
        return problems;
    }

    @Override
    public void end() {
        cleanupResources();
    }

    private CSSProblem createCSSParserError(final CSSParseError cssParseError) {
        final CSSProblem cssProblem = new CSSProblem();
        cssProblem.setDate(new Date());
        if (cssParseError.getFirstSkippedToken() != null) {
            cssProblem.setLineNumber(cssParseError.getFirstSkippedToken().getBeginLine());
            cssProblem.setColumnNumber(cssParseError.getFirstSkippedToken().getBeginColumn());
        }

        if (currentStyleRule != null) {
            cssProblem.setSelector(currentStyleRule.getSelectorsAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
        } else {
            cssProblem.setSelector("");
        }
        if (cssParseError.getLastValidToken() != null && cssParseError.getLastValidToken().getImage() != null && !cssParseError.getLastValidToken().getImage().trim().isEmpty()) {
            cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + "Encontrado error de parseo en: " + cssParseError.getLastValidToken().getImage());
        } else {
            cssProblem.setTextContent(resource.getStringSource() + System.lineSeparator() + cssParseError.getErrorMessage());
        }

        return cssProblem;
    }
}
