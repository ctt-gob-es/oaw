package es.inteco.flesch;

public interface FleschAnalyzer {

    public static final int SPANISH = 1;
    public static final int ENGLISH = 2;

    public static final int FLESCH_LEVEL_UNKNOWN = 8;
    public static final int FLESCH_LEVEL_VERY_EASY = 7;
    public static final int FLESCH_LEVEL_EASY = 6;
    public static final int FLESCH_LEVEL_QUITE_EASY = 5;
    public static final int FLESCH_LEVEL_STANDARD = 4;
    public static final int FLESCH_LEVEL_QUITE_HARD = 3;
    public static final int FLESCH_LEVEL_HARD = 2;
    public static final int FLESCH_LEVEL_VERY_HARD = 1;

    /**
     * Contador de sílabas
     *
     * @param text
     * @return
     */
    public int countSyllables(String text);

    /**
     * Contador de frases
     *
     * @param text
     * @return
     */
    public int countPhrases(String text);

    /**
     * Contador de palabras
     *
     * @param text
     * @return
     */
    public int countWords(String text);

    /**
     * Devuelve el valor de la formula Flesch
     *
     * @param numSyllables
     * @param numWords
     * @param numPhrases
     * @return
     */
    public double calculateFleschValue(int numSyllables, int numWords, int numPhrases);

    /**
     * Devuelve el nivel de legibilidad de acuerdo con el valor Flesch
     *
     * @param fleschValue
     * @return
     */
    public int getReadabilityLevel(double fleschValue);

}
