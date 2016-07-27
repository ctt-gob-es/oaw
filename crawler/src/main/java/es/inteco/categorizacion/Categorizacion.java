package es.inteco.categorizacion;

import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Clase que asigna las categorias a las URLs
 *
 * @author A.Gonzalez
 */
public class Categorizacion {

    Matcher mat;
    Pattern pat;


    /**
     * Class constructor.
     */
    public Categorizacion() {

    }

    /**
     * Metodo que añade puntuacion a cada categoría dependiendo de si aparece un término
     *
     * @author A.Gonzalez
     */
    public Map<String, Object> categoriza(Map<String, Object> terminos, String contenido) {

        Map<String, Object> scores = new Hashtable<>();
        int ocur = 0;
        for (int i = 0; i < terminos.size() / 4; i++) {

            this.compilaPat("[-,\\s,\",\',<]" + (String) terminos.get("Termino" + i) + "[-,\\s,\",\',>]");

            this.inicializaMat(contenido);

            ocur = this.compara();
            if (ocur > 0) {
                scores.put("Id_Termino" + i, (Long) terminos.get("Id_Termino" + i));
                scores.put("Id_Categoria" + i, (Long) terminos.get("Id_Categoria" + i));
                scores.put("Score" + i, (Float) terminos.get("Porcentaje_Normalizado" + i) * ocur);
            } else {
                scores.put("Id_Termino" + i, (Long) terminos.get("Id_Termino" + i));
                scores.put("Id_Categoria" + i, (Long) terminos.get("Id_Categoria" + i));
                scores.put("Score" + i, (float) 0.0);
            }

        }

        return scores;
    }


    /**
     * Metodo que inicializa el pattern
     *
     * @author A.Gonzalez
     */
    //INICIALIZA EL PATTERN
    private void compilaPat(String expresion) {

        pat = Pattern.compile(expresion);//,Pattern.CASE_INSENSITIVE);

    }


    /**
     * Metodo que inicializa el matcher
     *
     * @author A.Gonzalez
     */
    //INICIALIZA EL MATCHER
    private void inicializaMat(String texto) {
        mat = pat.matcher(texto);
    }


    /**
     * Metodo que busca el patron en el texto de la pagina
     *
     * @author A.Gonzalez
     */
    private int compara() {
        int contador = 0;

        while (true) {
            if (mat.find()) {

                contador++;
            } else {
                break;
            }
        }

        return (contador);
    }


}
