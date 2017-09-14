package es.inteco.intav.test;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;

import es.gob.oaw.css.utils.CSSSACUtils;

/**
 */
public class SelectorUsedTest {

    @Test
    public void testSelectorXPathConversion() {
        Assert.assertEquals("//*", xPathConversion("*"));
        Assert.assertEquals("//*[@id='main']", xPathConversion("#main"));
        Assert.assertEquals("//*[@id='main']//*[@class='noticia']", xPathConversion("#main .noticia"));
        Assert.assertEquals("//*[@id='main'][@class='noticia']", xPathConversion("#main.noticia"));
        Assert.assertEquals("//p", xPathConversion("p:first-line"));
        Assert.assertEquals("//span[@class='important']", xPathConversion("span.important:before"));
        Assert.assertEquals("//a", xPathConversion("a:hover"));
        Assert.assertEquals("//a[@class='css_1']", xPathConversion("a.css_1:hover"));
        Assert.assertEquals("//input[@type='submit']", xPathConversion("input[type='submit']:hover"));
        Assert.assertEquals("//*[@class='green']//input[@type='button']", xPathConversion("*.green input[type=\"button\"]:focus"));
        Assert.assertEquals("//*[@class='green']//input[@type='button']", xPathConversion(".green input[type=\"button\"]"));
        Assert.assertEquals("//h2/h3", xPathConversion("h2 > h3"));
        Assert.assertEquals("//h2/following-sibling::*[1]/self::h3", xPathConversion("h2 + h3"));
    }

    protected String xPathConversion(final String selector) {
        try {
            final Parser cssParser = CSSSACUtils.getSACParser();
            final InputSource is = new InputSource();
            is.setCharacterStream(new java.io.StringReader(selector));
            final SelectorList selectorList = cssParser.parseSelectors(is);
            return CSSSACUtils.getXPATHFromSelector(selectorList.item(0));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
