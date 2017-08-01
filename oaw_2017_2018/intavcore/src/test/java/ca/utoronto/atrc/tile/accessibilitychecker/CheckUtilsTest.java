package ca.utoronto.atrc.tile.accessibilitychecker;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CheckUtilsTest {

    @Test
    public void testLinkInDomain() throws IOException {
        Assert.assertTrue(CheckUtils.checkLinkInDomain("http://www.foo.com", "/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("http://www.foo.com", "http://www.foo.com/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("http://www.foo.com", "http://foo.com/noticias"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("http://pre.web.foo.com", "http://foo.com/noticias"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("https://www.foo.com", "https://www.foo.com/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("http://www.foo.com", "https://www.foo.com/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertTrue(CheckUtils.checkLinkInDomain("https://www.foo.com", "http://www.foo.com/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertFalse(CheckUtils.checkLinkInDomain("http://www.foo.com", "http://www.bar.com/buscador/iniciar.do?search=enfermedad @ virus @ Zika"));
        Assert.assertFalse(CheckUtils.checkLinkInDomain("http://www.foo.com", "mailto:www.foo.com@google.com"));
    }

}