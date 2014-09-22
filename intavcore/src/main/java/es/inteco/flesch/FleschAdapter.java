package es.inteco.flesch;

public class FleschAdapter {
    public static FleschAnalyzer getFleschAnalyzer(String language) throws Exception {
        if (language != null && language.toLowerCase().startsWith("es")) {
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.SPANISH);
        } else if (language != null && language.toLowerCase().startsWith("en")) {
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.ENGLISH);
        } else {
            // Por defecto devolvemos un analizador en castellano
            return FleschAnalyzerFactory.getFleschAnalyzer(FleschAnalyzer.SPANISH);
        }
    }
}
