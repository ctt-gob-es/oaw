package es.ctic.css;

import org.dom4j.Document;
import org.w3c.dom.Node;

import java.util.List;

/**
 *
 */
public interface CSSAnalyzer {
    List<CSSProblem> evaluate(Document document, List<CSSResource> cssResources);
}
