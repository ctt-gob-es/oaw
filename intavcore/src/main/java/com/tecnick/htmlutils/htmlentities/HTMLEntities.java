/**
 * Port de com.tecnick.htmlutils.htmlentities.HTMLEntities porque esta librería no se encuentra en los repositorios de Maven.
 * Como la librería solo consta de esta clase se ha optado por incluir el código en vez de trabajar con una dependencia
 * que se debe instalar localmente.
 */
package com.tecnick.htmlutils.htmlentities;

import java.util.Hashtable;
import java.util.Map;

/**
 * Collection of static methods to convert special and extended
 * characters into HTML entitities and vice versa.<br/><br/>
 * Copyright (c) 2004-2005 Tecnick.com S.r.l (www.tecnick.com) Via Ugo Foscolo
 * n.19 - 09045 Quartu Sant'Elena (CA) - ITALY - www.tecnick.com -
 * info@tecnick.com<br/>
 * Project homepage: <a href="http://htmlentities.sourceforge.net" target="_blank">http://htmlentities.sourceforge.net</a><br/>
 * License: http://www.gnu.org/copyleft/lesser.html LGPL
 *
 * @author Nicola Asuni [www.tecnick.com].
 * @version 1.0.004
 */
public final class HTMLEntities {
    private static final Object[][] HTML_ENTITIES_TABLE = new Object[][]{
            {"&Aacute;", 193}, {"&aacute;", 225}, {"&Acirc;", 194}, {"&acirc;", 226}, {"&acute;", 180}, {"&AElig;", 198}, {"&aelig;", 230}, {"&Agrave;", 192},
            {"&agrave;", 224}, {"&alefsym;", 8501}, {"&Alpha;", 913}, {"&alpha;", 945}, {"&amp;", 38}, {"&and;", 8743}, {"&ang;", 8736}, {"&Aring;", 197},
            {"&aring;", 229}, {"&asymp;", 8776}, {"&Atilde;", 195}, {"&atilde;", 227}, {"&Auml;", 196}, {"&auml;", 228}, {"&bdquo;", 8222}, {"&Beta;", 914},
            {"&beta;", 946}, {"&brvbar;", 166}, {"&bull;", 8226}, {"&cap;", 8745}, {"&Ccedil;", 199}, {"&ccedil;", 231}, {"&cedil;", 184}, {"&cent;", 162},
            {"&Chi;", 935}, {"&chi;", 967}, {"&circ;", 710}, {"&clubs;", 9827}, {"&cong;", 8773}, {"&copy;", 169}, {"&crarr;", 8629}, {"&cup;", 8746},
            {"&curren;", 164}, {"&dagger;", 8224}, {"&Dagger;", 8225}, {"&darr;", 8595}, {"&dArr;", 8659}, {"&deg;", 176}, {"&Delta;", 916}, {"&delta;", 948},
            {"&diams;", 9830}, {"&divide;", 247}, {"&Eacute;", 201}, {"&eacute;", 233}, {"&Ecirc;", 202}, {"&ecirc;", 234}, {"&Egrave;", 200}, {"&egrave;", 232},
            {"&empty;", 8709}, {"&emsp;", 8195}, {"&ensp;", 8194}, {"&Epsilon;", 917}, {"&epsilon;", 949}, {"&equiv;", 8801}, {"&Eta;", 919}, {"&eta;", 951},
            {"&ETH;", 208}, {"&eth;", 240}, {"&Euml;", 203}, {"&euml;", 235}, {"&euro;", 8364}, {"&exist;", 8707}, {"&fnof;", 402}, {"&forall;", 8704},
            {"&frac12;", 189}, {"&frac14;", 188}, {"&frac34;", 190}, {"&frasl;", 8260}, {"&Gamma;", 915}, {"&gamma;", 947}, {"&ge;", 8805}, {"&harr;", 8596},
            {"&hArr;", 8660}, {"&hearts;", 9829}, {"&hellip;", 8230}, {"&Iacute;", 205}, {"&iacute;", 237}, {"&Icirc;", 206}, {"&icirc;", 238}, {"&iexcl;", 161},
            {"&Igrave;", 204}, {"&igrave;", 236}, {"&image;", 8465}, {"&infin;", 8734}, {"&int;", 8747}, {"&Iota;", 921}, {"&iota;", 953}, {"&iquest;", 191},
            {"&isin;", 8712}, {"&Iuml;", 207}, {"&iuml;", 239}, {"&Kappa;", 922}, {"&kappa;", 954}, {"&Lambda;", 923}, {"&lambda;", 955}, {"&lang;", 9001},
            {"&laquo;", 171}, {"&larr;", 8592}, {"&lArr;", 8656}, {"&lceil;", 8968}, {"&ldquo;", 8220}, {"&le;", 8804}, {"&lfloor;", 8970}, {"&lowast;", 8727},
            {"&loz;", 9674}, {"&lrm;", 8206}, {"&lsaquo;", 8249}, {"&lsquo;", 8216}, {"&macr;", 175}, {"&mdash;", 8212}, {"&micro;", 181}, {"&middot;", 183},
            {"&minus;", 8722}, {"&Mu;", 924}, {"&mu;", 956}, {"&nabla;", 8711}, {"&nbsp;", 160}, {"&ndash;", 8211}, {"&ne;", 8800}, {"&ni;", 8715}, {"&not;", 172},
            {"&notin;", 8713}, {"&nsub;", 8836}, {"&Ntilde;", 209}, {"&ntilde;", 241}, {"&Nu;", 925}, {"&nu;", 957}, {"&Oacute;", 211}, {"&oacute;", 243},
            {"&Ocirc;", 212}, {"&ocirc;", 244}, {"&OElig;", 338}, {"&oelig;", 339}, {"&Ograve;", 210}, {"&ograve;", 242}, {"&oline;", 8254}, {"&Omega;", 937},
            {"&omega;", 969}, {"&Omicron;", 927}, {"&omicron;", 959}, {"&oplus;", 8853}, {"&or;", 8744}, {"&ordf;", 170}, {"&ordm;", 186}, {"&Oslash;", 216},
            {"&oslash;", 248}, {"&Otilde;", 213}, {"&otilde;", 245}, {"&otimes;", 8855}, {"&Ouml;", 214}, {"&ouml;", 246}, {"&para;", 182}, {"&part;", 8706},
            {"&permil;", 8240}, {"&perp;", 8869}, {"&Phi;", 934}, {"&phi;", 966}, {"&Pi;", 928}, {"&pi;", 960}, {"&piv;", 982}, {"&plusmn;", 177}, {"&pound;", 163},
            {"&prime;", 8242}, {"&Prime;", 8243}, {"&prod;", 8719}, {"&prop;", 8733}, {"&Psi;", 936}, {"&psi;", 968}, {"&radic;", 8730}, {"&rang;", 9002},
            {"&raquo;", 187}, {"&rarr;", 8594}, {"&rArr;", 8658}, {"&rceil;", 8969}, {"&rdquo;", 8221}, {"&real;", 8476}, {"&reg;", 174}, {"&rfloor;", 8971},
            {"&Rho;", 929}, {"&rho;", 961}, {"&rlm;", 8207}, {"&rsaquo;", 8250}, {"&rsquo;", 8217}, {"&sbquo;", 8218}, {"&Scaron;", 352}, {"&scaron;", 353},
            {"&sdot;", 8901}, {"&sect;", 167}, {"&shy;", 173}, {"&Sigma;", 931}, {"&sigma;", 963}, {"&sigmaf;", 962}, {"&sim;", 8764}, {"&spades;", 9824},
            {"&sub;", 8834}, {"&sube;", 8838}, {"&sum;", 8721}, {"&sup1;", 185}, {"&sup2;", 178}, {"&sup3;", 179}, {"&sup;", 8835}, {"&supe;", 8839}, {"&szlig;", 223},
            {"&Tau;", 932}, {"&tau;", 964}, {"&there4;", 8756}, {"&Theta;", 920}, {"&theta;", 952}, {"&thetasym;", 977}, {"&thinsp;", 8201}, {"&THORN;", 222},
            {"&thorn;", 254}, {"&tilde;", 732}, {"&times;", 215}, {"&trade;", 8482}, {"&Uacute;", 218}, {"&uacute;", 250}, {"&uarr;", 8593}, {"&uArr;", 8657},
            {"&Ucirc;", 219}, {"&ucirc;", 251}, {"&Ugrave;", 217}, {"&ugrave;", 249}, {"&uml;", 168}, {"&upsih;", 978}, {"&Upsilon;", 933}, {"&upsilon;", 965},
            {"&Uuml;", 220}, {"&uuml;", 252}, {"&weierp;", 8472}, {"&Xi;", 926}, {"&xi;", 958}, {"&Yacute;", 221}, {"&yacute;", 253}, {"&yen;", 165}, {"&yuml;", 255},
            {"&Yuml;", 376}, {"&Zeta;", 918}, {"&zeta;", 950}, {"&zwj;", 8205}, {"&zwnj;", 8204}
    };

    private static final Map<Integer, String> HTMLENTITIES_MAP = new Hashtable<>();
    private static final Map<String, Integer> UNHTMLENTITIES_MAP = new Hashtable<>();

    static {
        if (HTMLENTITIES_MAP.isEmpty()) {
            initializeEntitiesTables();
        }
    }

    private HTMLEntities() {
    }

    private static void initializeEntitiesTables() {
        for (Object[] entitiesTable : HTML_ENTITIES_TABLE) {
            HTMLENTITIES_MAP.put((Integer) entitiesTable[1], (String) entitiesTable[0]);
            UNHTMLENTITIES_MAP.put((String) entitiesTable[0], (Integer) entitiesTable[1]);
        }

    }

    public static String htmlentities(final String str) {
        if (str == null) {
            return "";
        } else {
            if (HTMLENTITIES_MAP.isEmpty()) {
                initializeEntitiesTables();
            }

            final StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < str.length(); ++i) {
                char ch = str.charAt(i);
                String entity = HTMLENTITIES_MAP.get((int) ch);
                if (entity == null) {
                    if (ch > 128) {
                        stringBuilder.append("&#").append(ch).append(";");
                    } else {
                        stringBuilder.append(ch);
                    }
                } else {
                    stringBuilder.append(entity);
                }
            }

            return stringBuilder.toString();
        }
    }

    public static String unhtmlentities(final String str) {
        if (HTMLENTITIES_MAP.isEmpty()) {
            initializeEntitiesTables();
        }

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == 38) {
                int semi = str.indexOf(59, i + 1);
                if (semi != -1 && semi - i <= 7) {
                    String entity = str.substring(i, semi + 1);
                    if (entity.charAt(1) == 32) {
                        stringBuilder.append(ch);
                    } else {
                        Integer iso;
                        if (entity.charAt(1) == 35) {
                            if (entity.charAt(2) == 120) {
                                iso = Integer.parseInt(entity.substring(3, entity.length() - 1), 16);
                            } else {
                                iso = Integer.valueOf(entity.substring(2, entity.length() - 1));
                            }
                        } else {
                            iso = UNHTMLENTITIES_MAP.get(entity);
                        }

                        if (iso == null) {
                            stringBuilder.append(entity);
                        } else {
                            stringBuilder.append((char) iso.intValue());
                        }

                        i = semi;
                    }
                } else {
                    stringBuilder.append(ch);
                }
            } else {
                stringBuilder.append(ch);
            }
        }

        return stringBuilder.toString();
    }

    public static String htmlSingleQuotes(final String str) {
        return str.replaceAll("[\']", "&rsquo;").replaceAll("&#039;", "&rsquo;").replaceAll("&#145;", "&rsquo;").replaceAll("&#146;", "&rsquo;");
    }

    public static String unhtmlSingleQuotes(final String str) {
        return str.replaceAll("&rsquo;", "\'");
    }

    public static String htmlDoubleQuotes(final String str) {
        return str.replaceAll("[\"]", "&quot;").replaceAll("&#147;", "&quot;").replaceAll("&#148;", "&quot;");
    }

    public static String unhtmlDoubleQuotes(String str) {
        return str.replaceAll("&quot;", "\"");
    }

    public static String htmlQuotes(final String str) {
        return htmlSingleQuotes(htmlDoubleQuotes(str));
    }

    public static String unhtmlQuotes(final String str) {
        return unhtmlSingleQuotes(unhtmlDoubleQuotes(str));
    }

    public static String htmlAngleBrackets(final String str) {
        return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    public static String unhtmlAngleBrackets(final String str) {
        return str.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    public static String htmlAmpersand(String str) {
        return str.replaceAll("&", "&amp;");
    }

    public static String unhtmlAmpersand(String str) {
        return str.replaceAll("&amp;", "&");
    }
}
