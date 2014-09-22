package es.inteco.crawler.ignored.links;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import org.apache.commons.digester.Digester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<IgnoredLink> getIgnoredLinks() throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = Utils.class.getResourceAsStream("/ignored_links.xml");
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.push(new ArrayList<IgnoredLink>());

            digester.addObjectCreate(Constants.XML_IGNORED_LINKS + "/" + Constants.XML_LINK, IgnoredLink.class);
            digester.addCallMethod(Constants.XML_IGNORED_LINKS + "/" + Constants.XML_LINK + "/" + Constants.XML_TEXT, "setText", 0);
            digester.addCallMethod(Constants.XML_IGNORED_LINKS + "/" + Constants.XML_LINK + "/" + Constants.XML_TITLE, "setTitle", 0);

            digester.addSetNext(Constants.XML_IGNORED_LINKS + "/" + Constants.XML_LINK, "add");

            return (List<IgnoredLink>) digester.parse(inputStream);
        } catch (Exception e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el InputStream", Utils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }
}
