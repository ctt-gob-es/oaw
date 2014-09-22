package es.inteco.rastreador2.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComprobadorCaracteres {
    private String entrada;

    public ComprobadorCaracteres(String en) {
        this.entrada = en;
    }


    public boolean comprueba() {
        Pattern p = Pattern.compile("[^a-z,A-Z,0-9,Á,É,Í,Ó,Ú,Ü,á,é,í,ó,ú,ü,ç,ñ,_,\\-,\\s\\.]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(entrada);
        if (m.find()) {
            //Devuelve FALSE si la expresión es incorrecta
            return false;
        }
        //Devuelve TRUE si la expresión es correcta
        return true;
    }


    public String espacios() {
        while (entrada.startsWith(" ")) {
            entrada = entrada.substring(1, entrada.length());
        }
        while (entrada.endsWith(" ")) {
            entrada = entrada.substring(0, entrada.length() - 1);
        }
        return entrada;
    }

}
