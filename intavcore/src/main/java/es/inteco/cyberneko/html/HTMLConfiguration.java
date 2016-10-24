package es.inteco.cyberneko.html;

public class HTMLConfiguration extends org.cyberneko.html.HTMLConfiguration {
    public HTMLConfiguration() {
        this(false);
    }

    public HTMLConfiguration(boolean balanceTags) {
        super();

        // No queremos incluir el balanceado de etiquetas
        setFeature(BALANCE_TAGS, balanceTags);

        setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags", true);

        // No se alteran las mayúsculas ni minúsculas de los nombres de elementos y atributos
        setProperty(NAMES_ELEMS, "match");
        setProperty(NAMES_ATTRS, "no-change");
    }
}