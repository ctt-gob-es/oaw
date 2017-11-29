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
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class BorrarXML {

    private String archivo = "";
    private String tag = "";
    private String valor = "";
    private boolean enc = false;

    public BorrarXML(String archivo, String tag, String valor) {
        this.archivo = archivo;
        this.tag = tag;
        this.valor = valor;
    }

    public boolean borra() {
        SAXBuilder sb = new SAXBuilder(false);
        Document borrador = null;
        try {
            borrador = sb.build(new File(archivo));
        } catch (Exception e) {
            System.out.println("EXCEPCIÓN: " + e.getMessage());
        }

        if (borrador == null) {
            return false;
        }
        Element raiz = borrador.getRootElement();

        Element borrable = (Element) encuentraElemento(raiz);
        if (borrable == null) {
            return false;
        }
        Element bo = (Element) borrable.getParent();
        bo.getParent().removeContent(bo);

        XMLOutputter out = new XMLOutputter();
        FileWriter fw = null;
        try {
            fw = new FileWriter(archivo);

            out.output(borrador, fw);
            fw.flush();
        } catch (Exception e) {
            System.out.println("EXCEPCION: " + e.getMessage());
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    Logger.putLog("Error cerrando FileWriter", BorrarXML.class, Logger.LOG_LEVEL_ERROR);
                }
            }
        }

        return true;
    }

    private Element encuentraElemento(Element raiz) {

        if (raiz.getName().equals(tag) && raiz.getText().equals(valor)) {
            enc = true;
            return raiz;
        }

        List elementos = raiz.getChildren();
        Iterator i = elementos.iterator();
        Element e = null;

        while (!enc && i.hasNext()) {
            e = encuentraElemento((Element) i.next());
        }

        if (enc) {
            return e;
        } else {
            return null;
        }
    }

}