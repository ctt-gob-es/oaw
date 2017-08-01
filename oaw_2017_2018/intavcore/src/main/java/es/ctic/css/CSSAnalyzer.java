package es.ctic.css;

import org.dom4j.Document;

import java.util.List;

/**
 *
 */
public interface CSSAnalyzer {
    List<CSSProblem> evaluate(Document document, List<CSSResource> cssResources);
}
