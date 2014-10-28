package es.ctic.css;

import org.w3c.dom.Element;

/**
 * Interfaz que define a un recurso CSS este podrá ser tanto un bloque style, como estilos enlazados mediante link o
 * importados mediante la regla @import de CSS, como un estilo en línea aplicado a un elemento mediante el atributo style
 */
public interface CSSResource {

    /**
     * Método para obtener el elemento HTML utilizado para incluir este recurso
     *
     * @return elemento Element html que provoca la inclusión de este recurso o null si el recurso ha sido
     * importado desde una hoja de estilos mediante @import
     */
    public abstract Element getHTMLElement();

    /**
     * Método que devuelve el contenido CSS del recurso (si es en línea el contenido del atributo style, para otros
     * casos el contenido completo bien del bloque style o de la hoja de estilos enlazada
     */
    public abstract String getContent();

    /**
     * Método para saber si este recurso CSS es un recurso en línea o no
     *
     * @return true si representa el contenido del atributo style de un elemento false en cualquier otro caso
     */
    public abstract boolean isInline();

    /**
     * Método para saber si este recurso CSS ha sido importado mediante la regla @import
     *
     * @return true si el recurso ha sido incluido mediante la regla @import y false en cualquier otro caso
     */
    public abstract boolean isImported();
}
