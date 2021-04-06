/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.css;

import org.w3c.dom.Element;

/**
 * Interfaz que define a un recurso CSS este podrá ser tanto un bloque style, como estilos enlazados mediante link o importados mediante la regla @import de CSS, como un estilo en línea aplicado a un
 * elemento mediante el atributo style.
 */
public interface CSSResource {

    /**
	 * Método para obtener el elemento HTML utilizado para incluir este recurso.
	 *
	 * @return elemento Element html que provoca la inclusión de este recurso o null si el recurso ha sido importado desde una hoja de estilos mediante @import
	 */
    public abstract Element getHTMLElement();

    /**
	 * Método que devuelve el contenido CSS del recurso (si es en línea el contenido del atributo style, para otros casos el contenido completo bien del bloque style o de la hoja de estilos enlazada.
	 *
	 * @return the content
	 */
    public abstract String getContent();

    /**
	 * Método para saber si este recurso CSS es un recurso en línea o no.
	 *
	 * @return true si representa el contenido del atributo style de un elemento false en cualquier otro caso
	 */
    public abstract boolean isInline();

    /**
	 * Método para obtener una cadena que identifique de donde proviene este recurso CSS.
	 *
	 * @return "<style>" si proviene de un bloque style, el atributo href si proviene de un elemento link y el nombre de la etiqueta si es un estilo incrustado
	 */
    public abstract String getStringSource();

    /**
	 * Método para saber si este recurso CSS ha sido importado mediante la regla @import.
	 *
	 * @return true si el recurso ha sido incluido mediante la regla @import y false en cualquier otro caso
	 */
    public abstract boolean isImported();
}
