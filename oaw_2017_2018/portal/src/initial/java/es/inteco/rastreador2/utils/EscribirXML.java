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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class EscribirXML {


    private String archivo = "";
    private String[] datos = null;
    private SAXBuilder sb = null;
    private Document doc = null;
    private XMLOutputter out = null;
    private FileWriter fw = null;


    public EscribirXML(String archivo, String[] datos) {
        this.archivo = archivo;
        this.datos = datos;

    }

    public void escribeProfile() {
        this.inicializa();

        Element root = doc.getRootElement();
        Element profile = new Element("profile");

        root.addContent(profile);

        Element name = new Element("name");
        name.setText(datos[0]);
        profile.addContent(name);
        Element plugin = null;

        for (int contaplugin = 1; contaplugin < datos.length; contaplugin++) {
            plugin = new Element("plugin");
            plugin.setText(datos[contaplugin]);
            profile.addContent(plugin);
            plugin = null;
        }

        this.escribe();

        this.limpia();

    }


    public void escribeCommand() {


        this.inicializa();

        Element root = doc.getRootElement();
        Element wrapCommand = new Element("wrapCommand");

        root.addContent(wrapCommand);

        Element name = new Element("name");
        name.setText(datos[3]);
        wrapCommand.addContent(name);

        Element command = new Element("command");
        command.setText(datos[1]);
        wrapCommand.addContent(command);

        Element user = new Element("user");
        user.setText(datos[2]);
        wrapCommand.addContent(user);

        Element reqCode = new Element("reqCode");
        reqCode.setText(datos[3]);
        wrapCommand.addContent(reqCode);

        Element id_rastreo = new Element("id_rastreo");
        id_rastreo.setText(datos[4]);
        wrapCommand.addContent(id_rastreo);

        Element fecha = new Element("fecha");
        fecha.setText(datos[5]);
        wrapCommand.addContent(fecha);

        this.escribe();

        this.limpia();

    }


    public void escribeCrawlDefs(List<String> carts, List<String> tips) {
        this.inicializa();

        Element cartucho = null;

        Element root = doc.getRootElement();
        Element crawlDef = new Element("crawlDef");

        root.addContent(crawlDef);

        Element id_rastreo = new Element("id_rastreo");
        id_rastreo.setText(datos[0]);
        crawlDef.addContent(id_rastreo);

        Element name = new Element("name");
        name.setText(datos[1]);
        crawlDef.addContent(name);

        Element fechaDef = new Element("fechaDef");
        fechaDef.setText(datos[2]);
        crawlDef.addContent(fechaDef);

        Element id_cartucho = new Element("id_cartucho");
        id_cartucho.setText(datos[3]);
        crawlDef.addContent(id_cartucho);

        Element hilos = new Element("hilos");
        hilos.setText(datos[4]);
        crawlDef.addContent(hilos);

        Element profundidad = new Element("profundidad");
        profundidad.setText(datos[5]);
        crawlDef.addContent(profundidad);

        Element topN = new Element("topN");
        topN.setText(datos[6]);
        crawlDef.addContent(topN);

        Element modo = new Element("modo");
        modo.setText(datos[7]);
        crawlDef.addContent(modo);

        Element categorizacion = new Element("categorizacion");
        categorizacion.setText(datos[8]);
        crawlDef.addContent(categorizacion);

        Element semilla = new Element("semilla");
        semilla.setText(datos[9]);
        crawlDef.addContent(semilla);

        Element alertas = new Element("alertas");
        alertas.setText(datos[10]);
        crawlDef.addContent(alertas);

        Element cartuchos = new Element("cartuchos");
        Iterator<String> it = carts.iterator();
        while (it.hasNext()) {
            cartucho = new Element("cartucho");
            cartucho.setText(it.next());
            cartuchos.addContent(cartucho);
        }
        crawlDef.addContent(cartuchos);

        Element whitelist = new Element("whitelist");
        whitelist.setText(datos[11]);
        crawlDef.addContent(whitelist);

        Element listaRastreable = new Element("listaRastreable");
        listaRastreable.setText(datos[12]);
        crawlDef.addContent(listaRastreable);

        Element umbralAlarma = new Element("umbralAlarma");
        umbralAlarma.setText(datos[13]);
        crawlDef.addContent(umbralAlarma);

        Element as = new Element("as");
        as.setText(datos[14]);
        crawlDef.addContent(as);

        Element tipos = new Element("tipos");
        it = tips.iterator();
        while (it.hasNext()) {
            Element tipo = new Element("tipo");
            tipo.setText(it.next());
            tipos.addContent(tipo);
        }
        crawlDef.addContent(tipos);

        this.escribe();

        this.limpia();
    }


    public void escribeUsers() {

        Element profile = null;

        this.inicializa();


        Element root = doc.getRootElement();
        Element user = new Element("user");

        root.addContent(user);

        Element name = new Element("name");
        name.setText(datos[0]);
        user.addContent(name);

        Element password = new Element("password");
        password.setText(datos[1]);
        user.addContent(password);

        for (int contaprof = 2; contaprof < datos.length; contaprof++) {
            profile = new Element("profile");
            profile.setText(datos[contaprof]);
            user.addContent(profile);
            profile = null;
        }

        this.escribe();

        this.limpia();

    }

    public void escribePlugin() {
        this.inicializa();

        Element root = doc.getRootElement();
        Element plugin = new Element("plugin");

        root.addContent(plugin);

        Element name = new Element("name");
        name.setText(datos[0]);
        plugin.addContent(name);

        Element source = new Element("source");
        source.setText(datos[1]);
        plugin.addContent(source);

        Element config = new Element("config");
        config.setText(datos[2]);
        plugin.addContent(config);

        this.escribe();

        this.limpia();

    }


    private void inicializa() {
        sb = new SAXBuilder(false);

        try {
            doc = sb.build(archivo);
        } catch (Exception e) {
            System.out.println("EXCEPCION: " + e.getMessage());
        }

    }


    private void limpia() {
        try {
            fw.flush();
            fw = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out = null;
        doc = null;
        sb = null;
    }

    private void escribe() {

        out = new XMLOutputter();

        try {
            fw = new FileWriter(archivo);
            out.output(doc, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
