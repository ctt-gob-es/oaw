package es.ctic.css;

import org.w3c.dom.Node;

import java.util.List;

/**
 *
 */
public interface CSSAnalyzer {
    List<CSSProblem> evaluate(Node node, List<CSSResource> cssResources);
}
