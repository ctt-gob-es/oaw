package es.inteco.intav.checks.une2004;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_83_NewWindow extends EvaluateCheck {

    private static final int NEW_WINDOW_WARNING_ID = 83;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-inteco-1-0");
    }

    @Test
    public void evaluateNoNewWindowWarning() throws Exception {
        checkAccessibility.setContent("<a href='index.html' target='_blank'>Lorem</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));
    }

    @Test
    public void evaluateNewWindowWarning() throws Exception {
        checkAccessibility.setContent("<a href='index.html' target='_blank' title='Nueva ventana'>Lorem</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));

        checkAccessibility.setContent("<a href='http://www.google.es' target='_blank' >Lorem</a>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));

        checkAccessibility.setContent("<a href='http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/SOCIAL.pdf' target='_blank' title='Abre nueva ventana: Hasta el 27 de febrero, las EELL podrán consultar lo que les adeudan las CCAA en materia social'>Lorem</a>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));

        checkAccessibility.setContent("<a href='http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/SOCIAL.pdf' target='_blank'>Lorem</a>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));

        checkAccessibility.setContent("<A class='enlace rojo' href='http://www.boe.es/diario_boe/txt.php?a=a&amp;id=BOE-A-2007-12352' target='_blank' title='Ir a *BOE-A-2007-12352*, en ventana nueva'>BOE-A-2007-12352</A>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, NEW_WINDOW_WARNING_ID));
    }

}
