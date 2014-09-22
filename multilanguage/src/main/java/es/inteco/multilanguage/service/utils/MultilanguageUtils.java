package es.inteco.multilanguage.service.utils;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.bean.UrlConfiguration;
import es.inteco.multilanguage.common.Constants;
import es.inteco.multilanguage.dao.LanguageDAO;
import es.inteco.multilanguage.database.export.SiteTranslationInformationForm;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.persistence.LanguageFound;
import es.inteco.multilanguage.service.AnalyzeService;
import es.inteco.multilanguage.service.utils.bean.Cookie;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultilanguageUtils {

    public static LanguageFound getDocumentLanguage(Document document, Analysis analysis, List<Language> languages) throws Exception {
        // Encontramos el idioma del documento, si lo especifica
        LanguageFound docLanguage = MultilanguageUtils.foundDocumentLanguage(document, languages);
        Language suspectedLanguage = MultilanguageUtils.getSuspectedLanguagePage(analysis.getContent(), docLanguage.getLanguage().getId());
        docLanguage.setHref(analysis.getUrl());
        docLanguage.setAnalysis(analysis);

        //Añadimos el lenguaje en el que creemos que está la página del rastreo
        docLanguage.setLanguageSuspected(suspectedLanguage);
        docLanguage.setContent(analysis.getContent());
        MultilanguageUtils.checkValencianoCatalanLanguage(docLanguage);

        if (docLanguage.getLanguage() != null) {
            analysis.getLanguagesFound().add(docLanguage);
            if (docLanguage.getLanguageSuspected() != null) {
                Logger.putLog("El documento tiene el lenguage " + docLanguage.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + docLanguage.getDeclarationLang() + "' y texto en " + docLanguage.getLanguageSuspected().getName().toLowerCase(), AnalyzeService.class, Logger.LOG_LEVEL_INFO);
            } else {
                Logger.putLog("El documento tiene el lenguage " + docLanguage.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + docLanguage.getDeclarationLang() + "'", AnalyzeService.class, Logger.LOG_LEVEL_INFO);
            }
        }
        return docLanguage;
    }

    public static Document getDocument(String htmlCode) throws Exception {
        DOMParser parser = new DOMParser(new HTMLConfiguration());
        parser.parse(new InputSource(new StringReader(htmlCode)));
        return parser.getDocument();
    }

    public static Document getDocument(URL refererUrl, URL remoteUrl, String cookie) throws Exception {
        URL redirection = remoteUrl;
        String redirectionCookie = cookie;
        Document document = null;

        PropertiesManager pmgr = new PropertiesManager();
        int maxNumRetries = Integer.parseInt(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "max.num.retries"));
        int numRetries = 0;
        while (redirection != null && numRetries < maxNumRetries) {
            HttpURLConnection connection = getConnection(redirection, refererUrl, redirectionCookie, false);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK &&
                    connection.getHeaderField("content-type") != null &&
                    connection.getHeaderField("content-type").contains("text/html")) {
                redirection = getRefreshHeader(remoteUrl, connection);
                if (redirection == null) {
                    redirectionCookie = getCookie(connection, redirectionCookie);
                    InputStream content = connection.getInputStream();
                    DOMParser parser = new DOMParser(new HTMLConfiguration());
                    parser.parse(new InputSource(content));
                    document = parser.getDocument();
                    redirection = getMetaRedirect(remoteUrl, document);
                } else {
                    // Si se hace redirección con la cabecera "refresh", hay que añadir la cookie en lugar de sustituirla
                    redirectionCookie = getCookie(connection, redirectionCookie);
                }
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                redirectionCookie = getCookie(connection, redirectionCookie);
                redirection = new URL(remoteUrl, connection.getHeaderField("location"));
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                Logger.putLog("La URL: " + remoteUrl + " ha devuelto un 404", MultilanguageUtils.class, Logger.LOG_LEVEL_WARNING);
            }
            numRetries++;
            connection.disconnect();
        }

        return document;
    }

    public static URL getMetaRedirect(URL url, Document document) {
        List<Element> metas = getElementsByTagName(document, "META");

        for (Element meta : metas) {
            if (meta.hasAttribute("http-equiv") && meta.getAttribute("http-equiv").equalsIgnoreCase("refresh") &&
                    meta.hasAttribute("content") && meta.getAttribute("content").contains("=")) {
                String redirection = meta.getAttribute("content").substring(meta.getAttribute("content").indexOf("=") + 1).trim();
                try {
                    // Probamos que se ha encontrado una dirección válida
                    return new URL(url, redirection);
                } catch (Exception e) {
                    Logger.putLog("La redirección encontrada a " + redirection + " no es válida", MultilanguageUtils.class, Logger.LOG_LEVEL_WARNING);
                }
            }
        }

        return null;
    }

    public static URL getRefreshHeader(URL url, HttpURLConnection connection) {
        if (StringUtils.isNotEmpty(connection.getHeaderField("refresh")) && connection.getHeaderField("refresh").contains("=")) {
            String redirection = connection.getHeaderField("refresh").substring(connection.getHeaderField("refresh").indexOf("=") + 1).trim();
            try {
                // Probamos que se ha encontrado una dirección válida
                return new URL(url, redirection);
            } catch (Exception e) {
                Logger.putLog("La redirección encontrada a " + redirection + " no es válida", MultilanguageUtils.class, Logger.LOG_LEVEL_WARNING);
            }
        }

        return null;
    }

    public static String getCookie(HttpURLConnection connection, String oldCookieText) {
        List<Cookie> oldCookies = new ArrayList<Cookie>();
        String[] oldCookiesArray = oldCookieText.split(";");

        for (String oldCookie : oldCookiesArray) {
            if (StringUtils.isNotEmpty(oldCookie)) {
                oldCookies.add(new Cookie(oldCookie));
            }
        }

        // Cogemos la lista de cookies, teniendo en cuenta que el parametro set-cookie no es sensible a mayusculas o minusculas
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookies = new ArrayList<String>();
        if (headerFields != null && !headerFields.isEmpty()) {
            for (Map.Entry<String, List<String>> headersEntry : headerFields.entrySet()) {
                if ("set-cookie".equalsIgnoreCase(headersEntry.getKey())) {
                    cookies.addAll(headersEntry.getValue());
                }
            }
        }

        for (String cookieStr : cookies) {
            if (!cookieStr.toLowerCase().endsWith("deleted")) {
                Cookie cookie = new Cookie(cookieStr);

                boolean found = false;
                for (Cookie oldCookie : oldCookies) {
                    if (oldCookie.getName() != null && oldCookie.getName().equalsIgnoreCase(cookie.getName())) {
                        // Si la cookie ya estaba, la sustituimos
                        if (StringUtils.isNotEmpty(cookie.getValue())) {
                            oldCookie.setValue(cookie.getValue());
                        }
                        found = true;
                        break;
                    }
                }

                // Si la cookie no estaba, se añade
                if (!found) {
                    oldCookies.add(cookie);
                }
            }
        }

        StringBuilder cookieText = new StringBuilder();

        for (Cookie cookie : oldCookies) {
            cookieText.append(cookie);
        }

        return cookieText.toString();
    }

    public static HttpURLConnection getConnection(URL remoteUrl, URL refererUrl, String cookie, boolean followRedirects) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) remoteUrl.openConnection();
        PropertiesManager pmgr = new PropertiesManager();
        connection.setConnectTimeout(Integer.parseInt(pmgr.getValue("multilanguage.properties", "http.connection.timeout")));
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setReadTimeout(Integer.parseInt(pmgr.getValue("multilanguage.properties", "http.connection.timeout")));
        connection.addRequestProperty("Accept", pmgr.getValue("multilanguage.properties", "method.accept.header"));
        // connection.addRequestProperty("Accept-Language", pmgr.getValue("multilanguage.properties", "method.accept.language.header"));
        connection.addRequestProperty("User-Agent", pmgr.getValue("multilanguage.properties", "method.user.agent.header"));
        if (refererUrl != null) {
            connection.addRequestProperty("Referer", refererUrl.toString());
        }
        if (StringUtils.isNotEmpty(cookie)) {
            connection.addRequestProperty("Cookie", cookie);
        }

        return connection;
    }

    public static boolean matchs(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static String getDocumentLanguage(Document document) {
        if (StringUtils.isNotEmpty(document.getDocumentElement().getAttribute("lang"))) {
            return document.getDocumentElement().getAttribute("lang");
        } else if (StringUtils.isNotEmpty(document.getDocumentElement().getAttribute("xml:lang"))) {
            return document.getDocumentElement().getAttribute("xml:lang");
        } else {
            return null;
        }
    }

    public static LanguageFound foundDocumentLanguage(Document document, List<Language> languages) {
        String docLang = MultilanguageUtils.getDocumentLanguage(document);

        LanguageFound languageFound = new LanguageFound();
        if (StringUtils.isNotEmpty(docLang)) {
            for (Language language : languages) {
                if (MultilanguageUtils.matchs(docLang, language.getCode())) {
                    languageFound.setLanguage(language);
                    languageFound.setDeclarationLang(docLang);
                    break;
                }
            }
        }
        if (languageFound.getLanguage() == null) {
            languageFound.setLanguage(LanguageDAO.getLanguage(Constants.SP_LANG));
        }
        return languageFound;
    }

    public static void getLanguageFoundData(LanguageFound languageFound, Analysis analysis) {
        try {
            languageFound.setAnalysis(analysis);

            if (languageFound.getRemoteDocument() != null) {
                DOMImplementationLS domImplementationLS = (DOMImplementationLS) languageFound.getRemoteDocument().getImplementation();
                LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
                lsSerializer.getDomConfig().setParameter("xml-declaration", false);
                languageFound.setContent(lsSerializer.writeToString(languageFound.getRemoteDocument()));
                //Añadimos el lenguaje en el que creemos que está la página del rastreo en las distintas versiones de cambio de idioma
                //Si no tiene texto no podemos suponer que lenguaje el con lo que devolvemos null.
                languageFound.setLanguageSuspected(MultilanguageUtils.getSuspectedLanguagePage(languageFound.getRemoteDocument(), languageFound.getLanguage().getId()));

                // Se comprueba si hay que corregir el idioma que hemos dado por bueno, por alguno de los que es posible
                // que en realidad sea el idioma correcto.
                MultilanguageUtils.checkValencianoCatalanLanguage(languageFound);

                if (languageFound.getLanguageSuspected() != null) {
                    Logger.putLog("Encontrado el lenguage " + languageFound.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + languageFound.getDeclarationLang() + "' y texto en " + languageFound.getLanguageSuspected().getName().toLowerCase(), AnalyzeService.class, Logger.LOG_LEVEL_INFO);
                } else {
                    Logger.putLog("Encontrado el lenguage " + languageFound.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + languageFound.getDeclarationLang() + "'", AnalyzeService.class, Logger.LOG_LEVEL_INFO);
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al revisar la validez del lenguaje del la url remota " + languageFound.getHref(), AnalyzeService.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static List<LanguageFound> chooseCorrectFoundLanguages(List<LanguageFound> languagesFound) {
        List<Language> assignedLanguages = new ArrayList<Language>();
        List<LanguageFound> langFoundAux = new ArrayList<LanguageFound>();
        for (LanguageFound languageFound : languagesFound) {
            if (languageFound.getPossibleLanguages().size() == 1) {
                languageFound.setLanguage(languageFound.getPossibleLanguages().get(0));
                assignedLanguages.add(languageFound.getPossibleLanguages().get(0));
                langFoundAux.add(languageFound);
            } else {
                if (StringUtils.isNotEmpty(languageFound.getHrefTitle())) {
                    for (Language possibleLanguage : languageFound.getPossibleLanguages()) {
                        if (matchs(languageFound.getHrefTitle(), possibleLanguage.getTitle())) {
                            if (!assignedLanguages.contains(possibleLanguage)) {
                                languageFound.setLanguage(possibleLanguage);
                                assignedLanguages.add(possibleLanguage);
                                langFoundAux.add(languageFound);
                                break;
                            }
                        }
                    }
                }
                if (languageFound.getLanguage() == null && languageFound.getDeclarationLang() != null) {
                    for (Language possibleLanguage : languageFound.getPossibleLanguages()) {
                        if (matchs(languageFound.getDeclarationLang(), possibleLanguage.getCode())) {
                            if (!assignedLanguages.contains(possibleLanguage)) {
                                languageFound.setLanguage(possibleLanguage);
                                assignedLanguages.add(possibleLanguage);
                                langFoundAux.add(languageFound);
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (LanguageFound languageFound : languagesFound) {
            if (languageFound.getLanguage() == null) {
                for (Language possibleLanguage : languageFound.getPossibleLanguages()) {
                    if (!assignedLanguages.contains(possibleLanguage)) {
                        languageFound.setLanguage(possibleLanguage);
                        assignedLanguages.add(possibleLanguage);
                        langFoundAux.add(languageFound);
                        break;
                    }
                }
            }
        }

        return langFoundAux;
    }

    public static List<LanguageFound> getLanguageLinks(Document document, List<Language> languages, Analysis analysis, String cookie) throws Exception {
        List<Element> links = getElementsByTagName(document, "A");
        links.addAll(getElementsByTagName(document, "AREA"));

        List<LanguageFound> languagesFound = new ArrayList<LanguageFound>();
        for (Element link : links) {
            LanguageFound languageFound = new LanguageFound();
            for (Language language : languages) {
                try {
                    if (matchsText(link, language) || matchsImage(link, language) || (link.getNodeName().equalsIgnoreCase("AREA") && matchsAlt(link, language))) {
                        String href = correctHref(link.getAttribute("href"));
                        languageFound.setHref(new URL(new URL(analysis.getUrl()), href).toString());
                        languageFound.getPossibleLanguages().add(language);
                        if (link.hasAttribute("title")) {
                            languageFound.setHrefTitle(link.getAttribute("title"));
                        }

                        if (languageFound.getRemoteDocument() == null) {
                            Document remoteDocument = MultilanguageUtils.getDocument(new URL(analysis.getUrl()), new URL(new URL(analysis.getUrl()), languageFound.getHref()), cookie);
                            if (remoteDocument != null) {
                                String remoteDocumentLanguage = MultilanguageUtils.getDocumentLanguage(remoteDocument);
                                languageFound.setDeclarationLang(remoteDocumentLanguage);
                                languageFound.setRemoteDocument(remoteDocument);
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Fallo al revisar un lenguaje encontrado: " + e.getMessage(), MultilanguageUtils.class, Logger.LOG_LEVEL_DEBUG);
                }
            }

            if (languageFound.getPossibleLanguages() != null && !languageFound.getPossibleLanguages().isEmpty()) {
                languagesFound.add(languageFound);
            }
        }

        languagesFound = chooseCorrectFoundLanguages(languagesFound);

        for (LanguageFound languageFound : languagesFound) {
            getLanguageFoundData(languageFound, analysis);
        }

        return languagesFound;
    }


    private static String correctHref(String href) {
        return href.replaceAll("\\\\", "/").replaceAll("\\.\\.[^/]", "");
        //return href.replaceAll("\\\\", "/").replaceAll("\\.\\.", "");
    }

    private static boolean matchsText(Element link, Language language) {
        return MultilanguageUtils.matchs(removeInlineTags(link.getTextContent()).trim(), language.getText())
                || MultilanguageUtils.matchs(link.getAttribute("title").trim(), language.getText());
    }

    private static boolean matchsAlt(Element link, Language language) {
        return link.hasAttribute("alt") && MultilanguageUtils.matchs(link.getAttribute("alt").trim(), language.getText());
    }

    private static String removeInlineTags(String content) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> inlineTags = Arrays.asList(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "inline.tags").split(";"));

        if (content != null) {
            for (String tag : inlineTags) {
                content = Pattern.compile("</{0,1}" + tag + " +[^>]*>|</{0,1}" + tag + ">", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
            }
        }

        return content;
    }

    private static boolean matchsImage(Element link, Language language) {
        List<Element> images = getElementsByTagName(link, "IMG");

        if (images.size() == 1) {
            for (Element image : images) {
                if (matchs(image.getAttribute("alt"), language.getText())
                        || (StringUtils.isEmpty(image.getAttribute("alt").trim()) && matchs(image.getAttribute("title"), language.getText()))) {
                    return true;
                }
            }
        }

        return false;
    }

    public static AnalysisForm getAnalysisForm(Analysis analysis) throws Exception {
        AnalysisForm analysisForm = new AnalysisForm();

        BeanUtils.copyProperties(analysisForm, analysis);

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "complet.date.format"));
        analysisForm.setDate(df.format(analysis.getDate()));

        // El GenericConverter copia también los persistentes en la lista, por eso hay que reinicializarla en el Form
        analysisForm.setLanguagesFound(new ArrayList<LanguageFoundForm>());

        if (analysis.getLanguagesFound() != null) {
            for (LanguageFound languageFound : analysis.getLanguagesFound()) {
                LanguageFoundForm languageFoundForm = new LanguageFoundForm();
                BeanUtils.copyProperties(languageFoundForm, languageFound);
                languageFoundForm = MultilanguageUtils.isCorrectLanguageFound(languageFoundForm);
                analysisForm.getLanguagesFound().add(languageFoundForm);
            }
        }
        analysis.setLanguagesFound(sortLanguagesFound(analysis.getLanguagesFound()));

        return analysisForm;
    }

    public static List<LanguageFound> sortLanguagesFound(List<LanguageFound> languages) {
        PropertiesManager pmgr = new PropertiesManager();
        String languagesStr = pmgr.getValue("multilanguage.properties", "languages.order");
        List<LanguageFound> sortLanguages = new ArrayList<LanguageFound>();
        List<String> languagesOrder = Arrays.asList(languagesStr.split(";"));
        for (String idLanguage : languagesOrder) {
            for (LanguageFound language : languages) {
                if (language.getLanguage() != null && idLanguage.equals(String.valueOf(language.getLanguage().getId()))) {
                    sortLanguages.add(language);
                }
            }
        }
        return sortLanguages;
    }

    public static List<LanguageFoundForm> sortLanguagesFoundForm(List<LanguageFoundForm> languages) {
        PropertiesManager pmgr = new PropertiesManager();
        String languagesStr = pmgr.getValue("multilanguage.properties", "languages.order");
        List<LanguageFoundForm> sortLanguages = new ArrayList<LanguageFoundForm>();
        List<String> languagesOrder = Arrays.asList(languagesStr.split(";"));
        for (String idLanguage : languagesOrder) {
            for (LanguageFoundForm language : languages) {
                if (idLanguage.equals(String.valueOf(language.getLanguage().getId()))) {
                    sortLanguages.add(language);
                }
            }
        }
        return sortLanguages;
    }

    public static List<Language> sortLanguagesForm(List<Language> languages) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        String languagesStr = pmgr.getValue("multilanguage.properties", "languages.order");
        List<Language> sortLanguages = new ArrayList<Language>();
        List<String> languagesOrder = Arrays.asList(languagesStr.split(";"));
        for (String idLanguage : languagesOrder) {
            for (Language language : languages) {
                if (idLanguage.equals(String.valueOf(language.getId()))) {
                    sortLanguages.add(language);
                }
            }
        }
        return sortLanguages;
    }

    /*public static List<LanguageFoundForm> converterLanguagesToForm(List<LanguageFound> languages) {
        List<LanguageFoundForm> languagesForm = new ArrayList<LanguageFoundForm>();

        try {
            for (LanguageFound language : languages) {
                LanguageFoundForm languageForm = new LanguageFoundForm();
                BeanUtils.copyProperties(languageForm, language);
            }
        } catch (Exception e) {
            Logger.putLog("Error al copiar las propiedades de los lenguajes", MultilanguageUtils.class, Logger.LOG_LEVEL_DEBUG);
        }

        return languagesForm;
    }//*/

    public static List<LanguageForm> orderLanguagesForm(List<LanguageForm> languages) {
        PropertiesManager pmgr = new PropertiesManager();
        String languagesStr = pmgr.getValue("multilanguage.properties", "languages.order");
        List<LanguageForm> orderedLanguages = new ArrayList<LanguageForm>();
        List<String> languagesOrder = Arrays.asList(languagesStr.split(";"));
        for (String idLanguage : languagesOrder) {
            for (LanguageForm language : languages) {
                if (idLanguage.equals(language.getId())) {
                    orderedLanguages.add(language);
                }
            }
        }
        return orderedLanguages;
    }

    public static List<Language> orderLanguages(List<Language> languages) {
        PropertiesManager pmgr = new PropertiesManager();
        String languagesStr = pmgr.getValue("multilanguage.properties", "languages.order");
        List<Language> orderedLanguages = new ArrayList<Language>();
        List<String> languagesOrder = Arrays.asList(languagesStr.split(";"));
        for (String idLanguage : languagesOrder) {
            for (Language language : languages) {
                if (idLanguage.equals(String.valueOf(language.getId()))) {
                    orderedLanguages.add(language);
                }
            }
        }
        return orderedLanguages;
    }

    public static Language getSuspectedLanguagePage(String content, Long idLinkLang) throws Exception {
        InputSource inputSource = new InputSource(new StringReader(content));

        DOMParser parser = new DOMParser(new HTMLConfiguration());
        parser.parse(inputSource);

        Document document = parser.getDocument();

        return getSuspectedLanguagePage(document, idLinkLang);
    }

    public static Language getSuspectedLanguagePage(Document document, Long idLinkLang) throws Exception {
        String content = getTextFromDOM(document);
        content = Pattern.compile(" {2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
        content = Pattern.compile("[\\n\\r\\t ]{2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(".\n");

        Map<String, BigDecimal> percentageLang = TranslationPageUtils.getPercentagesMap(content);
        BigDecimal maxPercentage = BigDecimal.ZERO;
        String lang = "";
        for (Map.Entry<String, BigDecimal> entry : percentageLang.entrySet()) {
            if (entry.getValue().compareTo(maxPercentage) > 0) {
                maxPercentage = entry.getValue();
                lang = entry.getKey();
            }
        }

        PropertiesManager pmgr = new PropertiesManager();
        BigDecimal rango = new BigDecimal(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "max.allowed.percentage.threshold"));
        if (!lang.isEmpty()) {
            if (lang.equals(String.valueOf(idLinkLang))) {
                return LanguageDAO.getLanguage(Long.parseLong(lang));
            } else {
                if (percentageLang.get(String.valueOf(idLinkLang)).add(rango).compareTo(percentageLang.get(lang)) < 0) {
                    return LanguageDAO.getLanguage(Long.parseLong(lang));
                } else {
                    return LanguageDAO.getLanguage(idLinkLang);
                }
            }
        } else {
            return null;
        }
    }

    private static Document removeIgnoredTags(Document document) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> ignoredTags = Arrays.asList(pmgr.getValue("multilanguage.properties", "ignored.tags").split(";"));
        for (String tag : ignoredTags) {
            NodeList scriptList = document.getElementsByTagName(tag.toUpperCase());
            int count = scriptList.getLength();
            for (int i = 0; i < count; i++) {
                scriptList = document.getElementsByTagName(tag.toUpperCase());
                scriptList.item(0).getParentNode().removeChild(scriptList.item(0));
            }
        }
        return document;
    }

    private static String getTextFromDOM(Document document) {

        // Arreglo de emergencia: serializamos el documento, quitamos las etiquetas en línea, y lo volvemos a parsear
        try {
            document = removeIgnoredTags(document);
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation();
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            lsSerializer.getDomConfig().setParameter("xml-declaration", false);
            String content = removeInlineTags(lsSerializer.writeToString(document));
            if (content != null) {
                DOMParser parser = new DOMParser(new HTMLConfiguration());
                parser.parse(new InputSource(new StringReader(content)));
                document = parser.getDocument();
            }
        } catch (Exception e) {
            Logger.putLog("Error al hacer el arreglo de la extracción de texto en el multilingüismo", MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }


        StringBuilder result = new StringBuilder();
        try {
            PropertiesManager pmgr = new PropertiesManager();

            List<Node> nodes = new ArrayList<Node>();
            nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE);

            List<String> attributes = Arrays.asList(pmgr.getValue("multilanguage.properties", "text.attributes").split(";"));

            for (Node node : nodes) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    for (String attribute : attributes) {
                        if (element.hasAttribute(attribute)) {
                            if (countWords(element.getAttribute(attribute).trim()) >= Integer.parseInt(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "minimun.phrase.length.for.analyze"))) {
                                result.append(element.getAttribute(attribute).trim()).append(".\n");
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(element.getTextContent())) {
                        NodeList children = element.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) {
                            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                                if (countWords(children.item(i).getTextContent().trim()) >= Integer.parseInt(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "minimun.phrase.length.for.analyze"))) {
                                    result.append(children.item(i).getTextContent()).append(" ");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al generar el texto del documento a partir de su árbol DOM", MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return HTMLEntities.unhtmlentities(result.toString());
    }

    private static int countWords(String text) {
        String[] words = text.split("[^a-zA-ZáéíóúàèìòùÁÉÍÓÚÀÈÌÒÙñÑ]");

        int count = 0;
        for (String word : words) {
            if (word != null && word.trim().length() > 0) {
                count++;
            }
        }

        return count;
    }

    private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                    (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements);
                }
                if (node.getNodeType() == Node.TEXT_NODE || (node.getNodeType() == Node.ELEMENT_NODE)) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    public static void checkValencianoCatalanLanguage(LanguageFound docLanguage) {
        Language vaLanguage = LanguageDAO.getLanguage(Constants.VA_LANG);
        Language caLanguage = LanguageDAO.getLanguage(Constants.CA_LANG);

        if (docLanguage.getLanguage() != null && docLanguage.getLanguage().getName().equals(Constants.CA_LANG_NAME) &&
                (docLanguage.getLanguageSuspected() != null && docLanguage.getLanguageSuspected().getId() == Constants.VA_LANG)) {
            docLanguage.setLanguageSuspected(caLanguage);
        }

        if (docLanguage.getLanguage() != null && docLanguage.getLanguage().getName().equals(Constants.VA_LANG_NAME) &&
                docLanguage.getLanguageSuspected() != null && docLanguage.getLanguageSuspected().getId() == Constants.CA_LANG) {
            docLanguage.setLanguageSuspected(vaLanguage);
        }
    }

    public static LanguageFoundForm isCorrectLanguageFound(LanguageFoundForm languageFoundForm) {
        if (languageFoundForm.getLanguage() != null && languageFoundForm.getLanguageSuspected() != null &&
                languageFoundForm.getLanguage().getId() != null && languageFoundForm.getLanguageSuspected().getId() != null) {
            if (languageFoundForm.getLanguage().getId().equals(languageFoundForm.getLanguageSuspected().getId())) {
                languageFoundForm.setCorrectTranslation(true);
            } else {
                languageFoundForm.setCorrectTranslation(false);
            }
        }

        if (languageFoundForm.getDeclarationLang() != null && languageFoundForm.getLanguage() != null
                && languageFoundForm.getLanguage().getCode() != null) {
            if (MultilanguageUtils.matchs(languageFoundForm.getDeclarationLang(), languageFoundForm.getLanguage().getCode())) {
                languageFoundForm.setCorrectDeclaration(true);
            } else {
                languageFoundForm.setCorrectDeclaration(false);
            }
        }

        if (languageFoundForm.isCorrectDeclaration() && languageFoundForm.isCorrectTranslation()) {
            languageFoundForm.setCorrect(true);
        } else {
            languageFoundForm.setCorrect(false);
        }

        return languageFoundForm;
    }

    public static AnalysisConfiguration loadConfiguration(String configName) {
        AnalysisConfiguration analysisConfiguration = null;
        PropertiesManager pmgr = new PropertiesManager();
        File configFile = new File(pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, "config.files") + configName + ".xml");

        if (configFile.exists()) {
            analysisConfiguration = new AnalysisConfiguration();

            try {
                DOMParser parser = new DOMParser();
                parser.parse(new InputSource(new FileInputStream(configFile)));

                Document document = parser.getDocument();

                analysisConfiguration.setType(Integer.parseInt(document.getElementsByTagName("tipo").item(0).getTextContent()));

                NodeList elementsUrl = document.getElementsByTagName("url");
                if (elementsUrl != null && elementsUrl.getLength() > 0) {
                    analysisConfiguration.setUrlConfigurations(new ArrayList<UrlConfiguration>());
                    for (int i = 0; i < elementsUrl.getLength(); i++) {
                        UrlConfiguration urlConfiguration = new UrlConfiguration();
                        Element elementUrl = (Element) elementsUrl.item(i);
                        urlConfiguration.setUrl(elementUrl.getAttribute("id"));
                        NodeList langUrls = elementUrl.getChildNodes();
                        for (int j = 0; j < langUrls.getLength(); j++) {
                            String idLang = pmgr.getValue(Constants.MULTILANGUAGE_PROPERTIES, langUrls.item(j).getNodeName() + ".codice");
                            if (StringUtils.isNotEmpty(idLang)) {
                                urlConfiguration.getLangUrls().put(Long.valueOf(idLang), langUrls.item(j).getTextContent());
                            }
                        }
                        analysisConfiguration.getUrlConfigurations().add(urlConfiguration);
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Error al leer la configuración del análisis " + configName, MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
                return null;
            }
        }

        return analysisConfiguration;
    }

    public static List<Element> getElementsByTagName(Document document, String tag) {
        List<Element> elements = new ArrayList<Element>();

        NodeList allElements = document.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static List<Element> getElementsByTagName(Element element, String tag) {
        List<Element> elements = new ArrayList<Element>();

        NodeList allElements = element.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static List<LanguageFound> deleteDuplicateLanguages(List<LanguageFound> languagesFound) {
        List<LanguageFound> languagesFoundAux = new ArrayList<LanguageFound>();
        for (LanguageFound language : languagesFound) {
            if (!languagesFoundAux.contains(language)) {
                languagesFoundAux.add(language);
            }
        }
        return languagesFoundAux;
    }

    public static List<Analysis> getUrlsFromXml(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) throws Exception {
        List<Language> languages = LanguageDAO.getLanguages(true);
        for (Analysis analysis : analysisList) {
            if (analysisConfiguration.getUrlConfiguration(analysis.getUrl()) != null) {
                Document document = MultilanguageUtils.getDocument(analysis.getContent());
                analysis.getLanguagesFound().add(MultilanguageUtils.getDocumentLanguage(document, analysis, languages));
                HashMap<Long, String> langUrls = analysisConfiguration.getUrlConfiguration(analysis.getUrl()).getLangUrls();
                for (Map.Entry<Long, String> entry : langUrls.entrySet()) {
                    analysis = SpecialCasesUtils.analyze(entry.getValue(), analysis, "", LanguageDAO.getLanguage(entry.getKey()));
                }
            }
        }
        return analysisList;
    }

    public static List<Analysis> getLanguagesFromHome(List<Analysis> analysisList) throws Exception {
        // Lista de cambios de idioma en la home
        List<LanguageFound> languagesFound = analysisList.get(0).getLanguagesFound();
        List<Long> languagesChecked = new ArrayList<Long>();

        for (LanguageFound switchLanguageHome : languagesFound) {
            if (!languagesChecked.contains(switchLanguageHome.getLanguage().getId())) {
                boolean isFirst = true;
                for (Analysis analysis : analysisList) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        // Puede que ya haya sido verificado en el primer análisis
                        boolean alreadyChecked = false;
                        for (LanguageFound languageFound : analysis.getLanguagesFound()) {
                            if (switchLanguageHome.getLanguage().getId().equals(languageFound.getLanguage().getId())) {
                                alreadyChecked = true;
                                break;
                            }
                        }
                        if (!alreadyChecked) {
                            String cookie = SpecialCasesUtils.getCookie(switchLanguageHome);
                            String url = analysis.getUrl();
                            analysis = SpecialCasesUtils.analyze(url, analysis, cookie, switchLanguageHome.getLanguage());
                        }
                    }
                    analysis.setLanguagesFound(MultilanguageUtils.deleteDuplicateLanguages(analysis.getLanguagesFound()));
                }
            }
            languagesChecked.add(switchLanguageHome.getLanguage().getId());
        }
        return analysisList;
    }

    public static List<SiteTranslationInformationForm> getPortalTraductionInformation(List<AnalysisForm> analysisFormList, boolean complet) throws Exception {

        Map<String, SiteTranslationInformationForm> infoMap = getPortalTraductionPercentageLanguages(analysisFormList);
        List<SiteTranslationInformationForm> infoList = new ArrayList<SiteTranslationInformationForm>();

        if (complet) {
            infoMap = getLanguagesValidity(infoMap, analysisFormList);
        }

        List<Language> languages = LanguageDAO.getLanguages(true);
        languages = MultilanguageUtils.sortLanguagesForm(languages);

        for (Language language : languages) {
            infoList.add(infoMap.get(language.getName()));
        }
        return infoList;
    }

    public static Map<String, SiteTranslationInformationForm> getPortalTraductionPercentageLanguages(List<AnalysisForm> analysisFormList) throws Exception {

        Map<String, SiteTranslationInformationForm> infoMap = new TreeMap<String, SiteTranslationInformationForm>();
        List<Language> languages = LanguageDAO.getLanguages(true);

        for (Language language : languages) {
            infoMap.put(language.getName(), new SiteTranslationInformationForm());
        }

        for (AnalysisForm analysisForm : analysisFormList) {
            infoMap = createLanguagesMap(languages, infoMap, analysisForm);
        }

        for (Language language : languages) {
            SiteTranslationInformationForm infoForm = new SiteTranslationInformationForm();
            //BigDecimal totalPages = infoMap.get(language.getName()).getCorrectTranslationPercentage().add(infoMap.get(language.getName()).getNoCorrectTranslationPercentage().add(infoMap.get(language.getName()).getNoTranslationPercentage()));
            if (analysisFormList.size() != 0) {
                infoForm.setName(language.getName());
                infoForm.setCorrectTranslationPercentage(infoMap.get(language.getName()).getCorrectTranslationPercentage().divide(new BigDecimal(analysisFormList.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                infoForm.setNoCorrectTranslationPercentage(infoMap.get(language.getName()).getNoCorrectTranslationPercentage().divide(new BigDecimal(analysisFormList.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                infoForm.setNoTranslationPercentage(infoMap.get(language.getName()).getNoTranslationPercentage().divide(new BigDecimal(analysisFormList.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                infoForm = fixResults(infoForm);
            }
            infoMap.put(language.getName(), infoForm);
        }

        return infoMap;
    }

    public static Map<String, SiteTranslationInformationForm> getLanguagesValidity(Map<String, SiteTranslationInformationForm> infoMap, List<AnalysisForm> analysisList) throws Exception {

        if (analysisList != null && !analysisList.isEmpty()) {
            List<Language> languages = LanguageDAO.getLanguages(true);
            for (AnalysisForm analysisForm : analysisList) {
                List<String> countedLangList = new ArrayList<String>();
                //Iteramos sobre los lenguajes de la home, suponemos estos los que deberian estar presentes en todas las paginas
                //for(LanguageFoundForm languageHomeFoundForm : analysisList.get(0).getLanguagesFound()) {
                for (Language language : languages) {
                    //Escojemos el lenguaje que corresponde
                    LanguageFoundForm languageFoundForm = null;
                    for (LanguageFoundForm languageFound : analysisForm.getLanguagesFound()) {
                        if (languageFound.getLanguage() != null && languageFound.getLanguage().getName().equals(language.getName())) {
                            languageFoundForm = languageFound;
                        }
                    }

                    SiteTranslationInformationForm sInformation = infoMap.get(language.getName());

                    if (sInformation.getNoTranslationPercentage().compareTo(new BigDecimal(100)) != 0 && languageFoundForm != null
                            && !countedLangList.contains(languageFoundForm.getLanguage().getName())) {
                        if (languageFoundForm.isCorrect()) {
                            sInformation.setTranslationGreen(sInformation.getTranslationGreen().add(BigDecimal.ONE));
                        } else {
                            sInformation.setTranslationRed(sInformation.getTranslationRed().add(BigDecimal.ONE));
                        }

                        if (languageFoundForm.isCorrectDeclaration()) {
                            sInformation.setDeclarationGreen(sInformation.getDeclarationGreen().add(BigDecimal.ONE));
                        } else {
                            sInformation.setDeclarationRed(sInformation.getDeclarationRed().add(BigDecimal.ONE));
                        }

                        if (languageFoundForm.isCorrectTranslation()) {
                            sInformation.setTextTranslationGreen(sInformation.getTextTranslationGreen().add(BigDecimal.ONE));
                        } else {
                            sInformation.setTextTranslationRed(sInformation.getTextTranslationRed().add(BigDecimal.ONE));
                        }
                        sInformation.incrementsiteNumber();
                    }

                    countedLangList.add(language.getName());
                    infoMap.put(language.getName(), sInformation);
                }
            }


            for (Map.Entry<String, SiteTranslationInformationForm> entry : infoMap.entrySet()) {
                SiteTranslationInformationForm sInformation = entry.getValue();
                if (sInformation.getSiteNumber() != 0) {
                    sInformation.setDeclarationGreen(sInformation.getDeclarationGreen().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation.setDeclarationRed(sInformation.getDeclarationRed().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation.setTextTranslationGreen(sInformation.getTextTranslationGreen().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation.setTextTranslationRed(sInformation.getTextTranslationRed().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation.setTranslationGreen(sInformation.getTranslationGreen().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation.setTranslationRed(sInformation.getTranslationRed().divide(new BigDecimal(sInformation.getSiteNumber()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    sInformation = fixResults(sInformation);
                }

                sInformation.setSiteNumber(0);
                infoMap.put(entry.getKey(), sInformation);
            }
        }
        return infoMap;
    }

    private static Map<String, SiteTranslationInformationForm> createLanguagesMap(List<Language> languages, Map<String, SiteTranslationInformationForm> infoMap, AnalysisForm analysisForm) {
        for (LanguageFoundForm analysisLang : analysisForm.getLanguagesFound()) {
            MultilanguageUtils.isCorrectLanguageFound(analysisLang);
        }

        for (Language language : languages) {
            SiteTranslationInformationForm infoForm = infoMap.get(language.getName());
            boolean found = false;
            boolean translate = false;
            for (LanguageFoundForm analysisLang : analysisForm.getLanguagesFound()) {
                if (analysisLang.getLanguage() != null && analysisLang.getLanguage().getId().equals(String.valueOf(language.getId()))) {
                    found = true;
                    if (analysisLang.isCorrect()) {
                        translate = true;
                    }
                    break;
                }
            }
            if (!found) {
                infoForm.setNoTranslationPercentage(infoForm.getNoTranslationPercentage().add(BigDecimal.ONE));
            } else if (!translate) {
                infoForm.setNoCorrectTranslationPercentage(infoForm.getNoCorrectTranslationPercentage().add(BigDecimal.ONE));
            } else {
                infoForm.setCorrectTranslationPercentage(infoForm.getCorrectTranslationPercentage().add(BigDecimal.ONE));
            }
            infoMap.put(language.getName(), infoForm);
        }
        return infoMap;
    }

    static SiteTranslationInformationForm fixResults(SiteTranslationInformationForm infoForm) {

        if (infoForm.getCorrectTranslationPercentage().add(infoForm.getNoCorrectTranslationPercentage().add(infoForm.getNoTranslationPercentage())).compareTo(new BigDecimal(100)) < 0) {
            infoForm.setCorrectTranslationPercentage(infoForm.getCorrectTranslationPercentage().add(BigDecimal.ONE));
        } else if (infoForm.getCorrectTranslationPercentage().add(infoForm.getNoCorrectTranslationPercentage().add(infoForm.getNoTranslationPercentage())).compareTo(new BigDecimal(100)) > 0) {
            if (infoForm.getNoCorrectTranslationPercentage().compareTo(BigDecimal.ZERO) != 0) {
                infoForm.setNoCorrectTranslationPercentage(infoForm.getNoCorrectTranslationPercentage().subtract(BigDecimal.ONE));
            } else {
                infoForm.setCorrectTranslationPercentage(infoForm.getCorrectTranslationPercentage().subtract(BigDecimal.ONE));
            }
        }

        if (((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(BigDecimal.ZERO) != 0) &&
                ((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100))) != 0) {
            if ((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100)) < 0) {
                infoForm.setDeclarationGreen(infoForm.getDeclarationGreen().add(BigDecimal.ONE));
            } else if ((infoForm.getDeclarationGreen().add(infoForm.getDeclarationRed())).compareTo(new BigDecimal(100)) > 0) {
                if (infoForm.getDeclarationRed().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setDeclarationRed(infoForm.getDeclarationRed().subtract(BigDecimal.ONE));
                } else if (infoForm.getDeclarationGreen().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setDeclarationGreen(infoForm.getDeclarationGreen().subtract(BigDecimal.ONE));
                }
            }
        }

        if (((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(BigDecimal.ZERO) != 0) &&
                ((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100))) != 0) {
            if ((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100)) < 0) {
                infoForm.setTextTranslationGreen(infoForm.getTextTranslationGreen().add(BigDecimal.ONE));
            } else if ((infoForm.getTextTranslationGreen().add(infoForm.getTextTranslationRed())).compareTo(new BigDecimal(100)) > 0) {
                if (infoForm.getTextTranslationRed().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setTextTranslationRed(infoForm.getTextTranslationRed().subtract(BigDecimal.ONE));
                } else if (infoForm.getTextTranslationGreen().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setTextTranslationGreen(infoForm.getTextTranslationGreen().subtract(BigDecimal.ONE));
                }
            }
        }

        if (((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(BigDecimal.ZERO) != 0) &&
                ((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100))) != 0) {
            if ((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100)) < 0) {
                infoForm.setTranslationGreen(infoForm.getTranslationGreen().add(BigDecimal.ONE));
            } else if ((infoForm.getTranslationGreen().add(infoForm.getTranslationRed())).compareTo(new BigDecimal(100)) > 0) {
                if (infoForm.getTranslationRed().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setTranslationRed(infoForm.getTranslationRed().subtract(BigDecimal.ONE));
                } else if (infoForm.getTranslationGreen().compareTo(BigDecimal.ZERO) > 0) {
                    infoForm.setTranslationGreen(infoForm.getTranslationGreen().subtract(BigDecimal.ONE));
                }
            }
        }

        return infoForm;
    }

}
