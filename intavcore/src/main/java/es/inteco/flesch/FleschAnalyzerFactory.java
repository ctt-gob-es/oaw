package es.inteco.flesch;

public class FleschAnalyzerFactory {

    /**
     * Devuelve un analizador para el lenguaje pedido
     *
     * @param language
     * @return
     * @throws FleschException
     */
    public static FleschAnalyzer getFleschAnalyzer(int language) throws FleschException {
        if (language == FleschAnalyzer.SPANISH) {
            return new FleschSpanishAnalyzer();
        } else if (language == FleschAnalyzer.ENGLISH) {
            return new FleschEnglishAnalyzer();
        } else {
            throw new FleschException("Analyzer not found for that language");
        }
    }
}
