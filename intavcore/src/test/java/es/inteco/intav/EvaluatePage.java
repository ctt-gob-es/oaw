package es.inteco.intav;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.form.*;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *
 */
public abstract class EvaluatePage {

    /*
     * Constantes copiadas de la clase
     */
    public static final int OBS_VALUE_NOT_SCORE = 0;
    public static final int OBS_VALUE_RED_ZERO = 1;
    public static final int OBS_VALUE_GREEN_ZERO = 2;
    public static final int OBS_VALUE_GREEN_ONE = 3;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    protected CheckAccessibility getCheckAccessibility(final String file) {
        CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline("observatorio-inteco-1-0");
        checkAccessibility.setGuidelineFile("observatorio-inteco-1-0.xml");
        checkAccessibility.setLevel("aa");
        checkAccessibility.setUrl("http://www.example.org");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);
        checkAccessibility.setContent(loadContent(file));

        Assert.assertNotNull(checkAccessibility.getContent());

        return checkAccessibility;
    }

    protected void checkEvaluation(final ObservatoryEvaluationForm observatoryEvaluation, final Map<String, Number> valores) {
        Assert.assertEquals("Score", valores.get("score"), observatoryEvaluation.getScore());
        for (AspectScoreForm aspectScore : observatoryEvaluation.getAspects()) {
            Assert.assertEquals(aspectScore.getName(), valores.get(aspectScore.getName()), aspectScore.getScore());
        }
        for (ObservatoryLevelForm observatoryLevel : observatoryEvaluation.getGroups()) {
            Assert.assertEquals(observatoryLevel.getName(), valores.get(observatoryLevel.getName()), observatoryLevel.getScore());
            for (ObservatorySuitabilityForm observatorySuitability : observatoryLevel.getSuitabilityGroups()) {
                Assert.assertEquals(observatoryLevel.getName() + " " + observatorySuitability.getName(), valores.get(observatoryLevel.getName() + " " + observatorySuitability.getName()), observatorySuitability.getScore());
                for (ObservatorySubgroupForm observatorySubgroup : observatorySuitability.getSubgroups()) {
                    Assert.assertEquals(observatorySubgroup.getDescription(), valores.get(observatorySubgroup.getDescription()).intValue(), observatorySubgroup.getValue());
                }
            }
        }
    }

    private String loadContent(final String file) {
        final StringBuilder stringBuilder = new StringBuilder();

        BufferedInputStream bin = null;
        try {
            final InputStream is = this.getClass().getResourceAsStream("/pages/" + file);
            Assert.assertNotNull(is);
            bin = new BufferedInputStream(is);

            //create a byte array
            final byte[] contents = new byte[1024];

            int bytesRead;
            while ((bytesRead = bin.read(contents)) != -1) {
                stringBuilder.append(new String(contents, 0, bytesRead));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException ioe) {
        } finally {
            //close the BufferedInputStream using close method
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (IOException ioe) {
            }

        }
        return stringBuilder.toString();
    }

}
