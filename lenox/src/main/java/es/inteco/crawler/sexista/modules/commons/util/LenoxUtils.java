package es.inteco.crawler.sexista.modules.commons.util;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.service.TerminosService;
import es.inteco.crawler.sexista.modules.commons.Constants.CommonsConstants;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase ConexionBBDD.
 *
 * @author GPM
 */
public class LenoxUtils {

    private static Logger log = Logger.getRootLogger();

    /**
     * Formatea en negrita el termino localizado en el contexto para mostrarlo en la jsp de detalle.
     *
     * @param paramTermino Termino Localizado
     * @param contexto     Contexto a formatear
     * @return Contexto formateado
     */
    public static String formatearContexto(Connection conn, String paramTermino, String contexto) {

        //Eliminamos los espacios del termino por si en BD está con espacios
        String termino = paramTermino.trim();

        StringBuffer contextoFormateado = new StringBuffer(contexto);

        //Inicializamos el servicio para obtener los términos
        TerminosService terminosService = new TerminosService();
        terminosService.setDao(new TerminosDao());
        UtilProcessingText util = new UtilProcessingText();

        try {
            //Recuperamos los terminos compuestos
            ArrayList<TerminoDto> lstTerminosCompuestos;

            lstTerminosCompuestos = terminosService.findTerminosCompuestos(conn);

            //1- Separamos el texto por palabras y las almacenamos de forma consecutiva en un array.
            ArrayList<String> lstTerminos = util.splitText(contexto, lstTerminosCompuestos);
            List<String> articles = getArticles();

            contextoFormateado = new StringBuffer(CommonsConstants.CADENA_VACIA);

            for (int i = 0; i < lstTerminos.size(); i++) {
                String palabra = lstTerminos.get(i);

                if (isTerminoOnContext(termino, palabra) ||
                        ((i != (lstTerminos.size() - 1) && articles.contains(palabra.trim().toUpperCase())) && isTerminoOnContext(termino, lstTerminos.get(i + 1)))) {
                    StringBuilder palabraNegrita = new StringBuilder(CommonsConstants.TAG_INI_NEGRITA_INTECO);
                    palabraNegrita.append(palabra);
                    palabraNegrita.append(CommonsConstants.TAG_FIN_NEGRITA_INTECO);
                    palabra = palabraNegrita.toString();
                }
                contextoFormateado.append(palabra);
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }
        return contextoFormateado.toString();
    }

    private static List<String> getArticles() {
        PropertiesManager pmgr = new PropertiesManager();
        return Arrays.asList(pmgr.getValue("lenox.properties", "articles").split(";"));
    }

    /**
     * M&eacute;todo isTerminoOnContext.
     * Busca si coinciden el término y la palabra que le pasamos. Si no
     * coinciden se busca el singular o el plural del termino y volvemos
     * a comprobar si coinciden
     *
     * @param termino Término a localizar
     * @param palabra Palabra del contexto
     * @return True/False si coinciden o no
     */
    private static boolean isTerminoOnContext(String termino, String palabra) {
        boolean encontrado = false;
        //Configuramos el comparador
        Collator collator = Collator.getInstance();

        //Indicamos que obvie mayúsculas/minúsculas, acentos....
        collator.setStrength(Collator.PRIMARY);

        String palabraTrim = palabra.trim();
        if (collator.compare(termino, palabraTrim) != 0) {
            //Buscamos plural
            String terminoPlural = UtilCalculateTerms.calculatePlural(termino);

            if (collator.compare(terminoPlural, palabraTrim) != 0) {
                //Buscamos plural
                String terminoSingular = UtilCalculateTerms.calculateSingular(termino);

                if (collator.compare(terminoSingular, palabraTrim) == 0) {
                    encontrado = true;
                }
            } else {
                encontrado = true;
            }
        } else {
            encontrado = true;
        }

        return encontrado;
    }

}