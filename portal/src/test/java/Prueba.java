import es.inteco.utils.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import sun.security.provider.MD5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mikunis on 19/11/14.
 */
public class Prueba {

    private final Map<String, String> IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C63A0DBA2F.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C6A7FE4DFA.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C66B579E26.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6DFB98657.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6A1F81E9E.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C69CA5E81F.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6D20C3BF2.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C6F8DD6D50.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C63ABD59C4.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6AB68ECBD.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C663AB7C79.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6430426C2.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C677D975C9.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C642096CA7.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6592A2A7E.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6B6F4DE69.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C61BF6A009.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C69834DB3D.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6E60F7658.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C66FB1A862.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C69A194821.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C641FA9C4E.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C60CF9E5FE.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C65F95E422.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C62E00411F.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C6A56B3157.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C693518304.png");

                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C63F715313.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C62DF54803.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C666025F16.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C6AB18A299.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C68DE4E659.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C68E60A079.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C69F9F7D43.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6B793C1C1.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C655E19925.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C6B0671186.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C61859BDB5.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C68ADD29E8.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C6897C2C00.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6EC5BA3EF.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C62FFA56F9.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C60A75AF5E.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6D670B07D.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C6F5E33319.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C66C679733.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C63E51552F.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6521FAA3F.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C6EE11BE16.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.5.jpg", "1000000000000235000001C62B25C34B.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.6.jpg", "1000000000000235000001C606D6CC0E.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.7.jpg", "1000000000000235000001C6B0D30E06.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C6A71C6E40.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C67526C00E.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C66B571CAF.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C654FD74D2.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C60ABEB8D9.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C62F576C67.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6412BFDE0.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.5.jpg", "1000000000000235000001C63E300023.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.6.jpg", "1000000000000235000001C6E3B66F46.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.7.jpg", "1000000000000235000001C6B2AF3291.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6AEED98BC.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6138E2F7A.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C6F6FA94DD.png");
                }
            });

    @Test
    public void testOpenoffice() throws Exception {
//        final String expected = "1000000000000235000001C681E06EE4";
//        System.out.println(Integer.toHexString("Imagen77".hashCode()));
//        InputStream is = this.getClass().getResourceAsStream("NoResult.jpg");
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        int i = -1;
//        while ((i=is.read())!=-1) {
//            os.write(i);
//        }
//        os.close();
//        is.close();
//        for (String s : IMAGES.values()) {
//            Files.copy(this.getClass().getResourceAsStream("NoResult.jpg"), new File("/home/mikunis/Pictures/"+s).toPath());
//        }

    }
}
