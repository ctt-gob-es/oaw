/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * The Class LeerXML.
 */
public class LeerXML {


    /** The archivo. */
    String archivo = "";
    
    /** The sb. */
    SAXBuilder sb = null;
    
    /** The doc. */
    Document doc = null;

    /** The enc. */
    boolean enc = false;

    /**
	 * Instantiates a new leer XML.
	 *
	 * @param archivo the archivo
	 */
    public LeerXML(String archivo) {
        this.archivo = archivo;
    }


    /**
	 * Recorrer elemento.
	 *
	 * @param raiz  the raiz
	 * @param tag   the tag
	 * @param valor the valor
	 * @return the element
	 */
    public Element recorrerElemento(Element raiz, String tag, String valor) {

        if (raiz.getName().equals(tag) && raiz.getText().equals(valor)) {
            enc = true;
            return raiz;
        }

        List elementos = raiz.getChildren();
        Iterator i = elementos.iterator();
        Element e = null;

        while (!enc && i.hasNext()) {
            e = recorrerElemento((Element) i.next(), tag, valor);
        }

        if (enc) {
            return e;
        } else {
            return null;
        }
    }

    /**
	 * Rellena combo.
	 *
	 * @param tagBuscar the tag buscar
	 * @return the string[]
	 */
    public String[] rellenaCombo(String tagBuscar) {
        String[] valores = null;
        int indice = 0;
        Element aux = null;

        this.inicializa();

        Element raiz = doc.getRootElement();

        List listaValores = raiz.getChildren();
        Iterator iValores = listaValores.iterator();

        valores = new String[listaValores.size()];

        while (iValores.hasNext()) {
            aux = (Element) iValores.next();
            valores[indice] = aux.getChildText(tagBuscar);
            indice++;
        }
        //this.getNodo();
        this.limpia();
        return (valores);

    }

    /**
	 * Obten sub valores.
	 *
	 * @param tag     the tag
	 * @param valor   the valor
	 * @param tagHija the tag hija
	 * @return the string[]
	 */
    public String[] obtenSubValores(String tag, String valor, String tagHija) {
        String[] subValores = null;
        int contaValores = 0;


        this.inicializa();
        Element raiz = doc.getRootElement();

        List hijosLocal = this.leeNodo(((Element) recorrerElemento(raiz, tag, valor)), tagHija);//local.getChildren(tagHija);
        Iterator itLocal = hijosLocal.iterator();

        subValores = new String[hijosLocal.size()];

        while (itLocal.hasNext()) {
            subValores[contaValores] = ((Element) (itLocal.next())).getText();
            contaValores++;

        }
        this.limpia();
        return (subValores);

    }

    /**
	 * Lee nodo.
	 *
	 * @param hijo   the hijo
	 * @param nombre the nombre
	 * @return the list
	 */
    public List leeNodo(Element hijo, String nombre) {
        Element padre = (Element) hijo.getParent();

        if (nombre.equals("")) {
            return (padre.getChildren());
        } else {
            return (padre.getChildren(nombre));
        }
    }

    /**
	 * Inicializa.
	 */
    private void inicializa() {
        sb = new SAXBuilder(false);
        try {
            doc = sb.build(new File(archivo));
        } catch (Exception e) {
            Logger.putLog("Excepción", LeerXML.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    /**
	 * Limpia.
	 */
    private void limpia() {
        sb = null;
        doc = null;
    }

    /**
	 * Obten status.
	 *
	 * @param reqCode the req code
	 * @return the string[]
	 */
    public String[] obtenStatus(String reqCode) {
        String[] status = new String[5];
        int contador = 0;

        this.inicializa();

        Element raiz = doc.getRootElement();
        List listaStatus = raiz.getChildren();

        for (Object listaStatu : listaStatus) {
            Element aux = (Element) listaStatu;
            if (aux.getChildText("reqCode").compareTo(reqCode) == 0 && contador == 0) {
                status[contador] = aux.getChildText("name");
                contador++;
                status[contador] = aux.getChild("status").getText();
                contador++;
                status[contador] = aux.getChild("downloaded").getText();
                contador++;
                status[contador] = aux.getChild("PID").getText();
                contador++;
                status[contador] = aux.getChild("slot").getText();
                contador++;
            }
        }

        return status;
    }

}	