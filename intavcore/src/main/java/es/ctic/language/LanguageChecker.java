package es.ctic.language;

import org.apache.tika.language.LanguageIdentifier;

/**
 * Clase para comprobar el idioma de una página web. Se utilizan dos diferentes motores, uno basado en Tika y otro basado en el port de KDE
 * ya que cada uno de ellos da soporte a diferentes idiomas.
 */
public class LanguageChecker {

    private String expectedLanguage;

    /**
     *
     * @param expectedLanguage idioma sobre el que se comprobará
     */
    public LanguageChecker(final String expectedLanguage) {
        // Las herramientas no diferencian correctamente el valenciano del catalán.
        this.expectedLanguage = "va".equals(expectedLanguage) ? "ca" : expectedLanguage;
    }

    /**
     * Obtiene el código de idioma, si está entre los admitidos por este comprobador, de un texto
     * @param content cadena con el contenido en texto plano
     * @return una cadena con el código del idioma detectado para el contenido o la cadena "unknown" si no puede ser detectado
     */
    public String getLanguage(final String content) {
        if (LanguageIdentifier.getSupportedLanguages().contains(expectedLanguage)) {
            final LanguageIdentifier languageIdentifier = new LanguageIdentifier(content);
            return languageIdentifier.getLanguage();
        } else if (GuessLanguage.isSupportedLanguage(expectedLanguage)) {
            final GuessLanguage guessLanguage = new GuessLanguage();
            return guessLanguage.guessLanguage(content);
        }
        return "unknown";
    }

    /**
     * Método que comprueba si un determinado texto está escrito en el idioma esperado (indicado en el constructor) por este comprobador
     * @param content cadena con el contenido en formato texto plano a comprobar el idioma
     * @return true si el idioma detectado es el esperado (indicado en el constructor)
     */
    public boolean isExpectedLanguage(final String content) {
        return getLanguage(content).equals(expectedLanguage);
    }

}
