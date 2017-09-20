package es.inteco.crawler.ignored.links;

import es.inteco.common.logging.Logger;
import org.apache.commons.digester.Digester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private static final String XML_IGNORED_LINKS = "ignored_links";
    private static final String XML_LINK = "link";
    private static final String XML_TEXT = "text";
    private static final String XML_TITLE = "title";

    private Utils() {
    }

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
