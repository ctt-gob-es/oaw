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
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C656F13328.jpg");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C69C5B6C64.jpg");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C614F27F0A.jpg");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6BD0B4CBA.jpg");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6E28F2B85.jpg");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C61A4A3B95.jpg");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6AE8D6D4C.jpg");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C67F09331B.jpg");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C603BF8AC4.jpg");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6497B82D1.jpg");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6C99DC95B.jpg");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6937174F5.jpg");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C621A45F48.jpg");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C69D68F6EF.jpg");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C677D6555E.jpg");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C67940FBFF.jpg");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6861A9DB9.jpg");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C69ED29945.jpg");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6E138B19F.jpg");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C62E0F8A11.jpg");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6098AEA60.jpg");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6EBE93738.jpg");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6C8E40FC5.jpg");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C61C06A346.jpg");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65EEC8D6A.jpg");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C644B3AB97.jpg");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6E86A1A4D.jpg");

                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6BE79C3C1.jpg");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6121C10CC.jpg");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6D1EFC0F6.jpg");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C657B26CD6.jpg");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6B6775FE7.jpg");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C69F161F89.jpg");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C65FD6B2D9.jpg");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6629DC1E0.jpg");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C64F035130.jpg");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C68AE3915D.jpg");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C69D84230E.jpg");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60A54315E.jpg");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C67E12C804.jpg");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C690C0FAA4.jpg");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C67686AF27.jpg");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C6523B4FD4.jpg");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C627AC2925.jpg");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C6BC60CD63.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C654B664E9.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C65A63BCFD.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6A1D35AB0.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C608649C62.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C62939C133.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C67FE9D927.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C64973F0AA.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C67372430A.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C65B14D0A4.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C610EEA931.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C6D569F5D8.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C62A3E716D.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C622BA8F28.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6E29E7FBA.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C63945088C.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6B3A4A5A7.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C65B622F7A.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C6F84BBB63.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C67A0B42D7.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C69BD75158.jpg");
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
