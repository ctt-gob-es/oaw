package es.ctic.rastreador2.pdf;

import org.junit.Test;

import java.io.File;

/**
 * Created by mikunis on 11/18/16.
 */
public class SourceFilesManagerTest {

    @Test
    public void createCSSTempFile() throws Exception {
        final SourceFilesManager sourceFilesManager = new SourceFilesManager(new File("/"));
        final File parentDir = new File("/tmp/oaw/files/observatory/999/999/ministerio_de_la_transparencia_y_administraciones_territoriales/centro_de_estudios_politicos_y_constitucionales");
        parentDir.mkdirs();
        File tmp = sourceFilesManager.createCSSTempFile("/Telerik.Web.UI.WebResource.axd?compress=0&_TSM_CombinedScripts_=%3b%3bTelerik.Sitefinity.Resources%2c+Version%3d5.2.3800.0%2c+Culture%3dneutral%2c+PublicKeyToken%3dnull%3aes%3aee7f36f1-9117-453e-b19a-955f459dd92a%3a7a90d6a%3ac9a6223b%3bTelerik.Web.UI%2c+Version%3d2012.3.1016.40%2c+Culture%3dneutral%2c+PublicKeyToken%3d121fae78165ba3d4%3aes%3a6feb3d99-e3a3-41be-bef8-96d1d2a7da93%3a8cee9284%3afe53831e%3a92753c09%3a91f742eb%3ab7acb766%3a6cd7c4a8%3bTelerik.Web.UI.Skins%2c+Version%3d2012.3.1016.40%2c+Culture%3dneutral%2c+PublicKeyToken%3d121fae78165ba3d4%3aes%3ab2260b14-4968-4615-a97a-85fac222ade9%3a5924a2a7",
                parentDir);
        tmp.delete();
        parentDir.delete();
    }

}