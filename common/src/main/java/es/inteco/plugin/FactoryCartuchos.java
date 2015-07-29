package es.inteco.plugin;

import es.inteco.common.logging.Logger;

/**
 * @author a.mesas
 *         Implementa la factoria de cartuchos. Dado el nombre de un cartucho nos retorna
 *         una instancia del mismo.
 */
public final class FactoryCartuchos {

    private FactoryCartuchos() {
    }

    /**
     * Devuelve una instancia del cartucho que se solicita
     *
     * @param nombreCartucho nombre completo de la clase del cartucho
     * @return un nuevo objeto de tipo Cartucho o null si se produce algún error
     */
    public static Cartucho getCartucho(final String nombreCartucho) {
        try {
            final Class clase = Class.forName(nombreCartucho);
            // Creo una instancia
            return (Cartucho) clase.newInstance();
        } catch (ClassCastException cce) {
            Logger.putLog("Fallo en el factory de cartuchos, la clase indicada no es un cartucho", FactoryCartuchos.class, Logger.LOG_LEVEL_ERROR);
        } catch (Exception e) {
            Logger.putLog("Fallo en el factory de cartuchos, cartucho no implementado", FactoryCartuchos.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

}
