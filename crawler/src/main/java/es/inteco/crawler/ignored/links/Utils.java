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
package es.inteco.crawler.ignored.links;

import es.inteco.common.logging.Logger;
import org.apache.commons.digester.Digester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Utils.
 */
public final class Utils {

    /** The Constant XML_IGNORED_LINKS. */
    private static final String XML_IGNORED_LINKS = "ignored_links";
    
    /** The Constant XML_LINK. */
    private static final String XML_LINK = "link";
    
    /** The Constant XML_TEXT. */
    private static final String XML_TEXT = "text";
    
    /** The Constant XML_TITLE. */
    private static final String XML_TITLE = "title";

    /**
	 * Instantiates a new utils.
	 */
    private Utils() {
    }

    /**
	 * Gets the ignored links.
	 *
	 * @return the ignored links
	 * @throws Exception the exception
	 */
    @SuppressWarnings("unchecked")
	public static List<IgnoredLink> getIgnoredLinks() throws Exception {
        try (InputStream inputStream = Utils.class.getResourceAsStream("/ignored_links.xml")) {
            final Digester digester = new Digester();
            digester.setValidating(false);
            digester.push(new ArrayList<IgnoredLink>());

            digester.addObjectCreate(XML_IGNORED_LINKS + "/" + XML_LINK, IgnoredLink.class);
            digester.addCallMethod(XML_IGNORED_LINKS + "/" + XML_LINK + "/" + XML_TEXT, "setText", 0);
            digester.addCallMethod(XML_IGNORED_LINKS + "/" + XML_LINK + "/" + XML_TITLE, "setTitle", 0);

            digester.addSetNext(XML_IGNORED_LINKS + "/" + XML_LINK, "add");

            return (List<IgnoredLink>) digester.parse(inputStream);
        } catch (Exception e) {
            Logger.putLog("Error al obtener los enlaces que se ignoran", Utils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

}
