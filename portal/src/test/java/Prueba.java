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
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C620BD4672.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C65D803505.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C64952AE61.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C60A00A8C4.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6FF768C50.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C692C365D4.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C612BBB073.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C66BDC6C22.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6D347C429.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6C7386C9F.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6405CFCF9.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6DFB50FB8.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C605F4186C.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C62786326E.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C697F54FB4.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6B89061F0.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6D5597241.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C60D9C0A37.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C637C6F055.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C61762EBA7.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C600F05FF8.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C614FBA124.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C663A593C3.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C6D97AD96F.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C6188F0455.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C61A5CFCE0.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6DDDA200D.png");

                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C63315A96E.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6861E63C2.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6DED6240D.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C61E5F770B.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6F16D4021.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6BCD534DB.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C6A0372A0E.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C628B3B946.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C6E08BC2F0.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C6A7C8CF89.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C64044C439.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C6285E91AD.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C63CFD3196.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C68CAF7260.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C60CADF240.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C629240220.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6F0BC97CE.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C65A565814.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C60C9346BC.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C68F40C0DA.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6DC9E977A.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C6F252E393.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.5.jpg", "1000000000000235000001C61DFBFE8B.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.6.jpg", "1000000000000235000001C6F471A9BB.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.7.jpg", "1000000000000235000001C6674B2EDB.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C687BE294F.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C671ECE2FF.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6AB15AC35.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C663CA0DDE.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C60E749A28.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C6534C4EB2.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6FF198518.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.5.jpg", "1000000000000235000001C666B6C60B.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.6.jpg", "1000000000000235000001C6E77C4DB7.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.7.jpg", "1000000000000235000001C637A5148B.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6CACF8DBC.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C617440D8B.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C6AF211621.png");
                }
            });

    @Test
    public void testOpenoffice() throws Exception {
//        final String expected = "1000000000000235000001C681E06EE4";
//        System.out.println(Integer.toHexString("Imagen77".hashCode()));
//        InputStream is = this.getClass().getResourceAsStream("NoResult.png");
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        int i = -1;
//        while ((i=is.read())!=-1) {
//            os.write(i);
//        }
//        os.close();
//        is.close();
//        for (String s : IMAGES.values()) {
//            Files.copy(this.getClass().getResourceAsStream("NoResult.png"), new File("/home/mikunis/Pictures/"+s).toPath());
//        }

    }
}
