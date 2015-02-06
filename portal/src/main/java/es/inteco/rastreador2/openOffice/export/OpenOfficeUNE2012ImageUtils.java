package es.inteco.rastreador2.openOffice.export;

import es.inteco.common.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para asignar a cada imagen que se incluye en el informe OpenOffice el nombre que recibe al incrustarse en el documento.
 * Esta asignación permite identificar que imagen existente en la plantilla, que actua como placeholder, hay que sobreescribir.
 */
public final class OpenOfficeUNE2012ImageUtils {

    private static final Map<String, String> AGE_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C6987D2584.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C620878618.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C634551D7C.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C63CFDF8C0.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C60B159604.png");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6459CEB24.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C6408B27CF.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6A22282DF.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C66BA46EF3.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6F76DB52A.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C610597AA9.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C641962C26.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6A4C266AD.png");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C67C0F69F4.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C65D84FD94.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C6F7126A05.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C604F4DA3B.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6FF12B193.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6E701E6F4.png");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C6A275574E.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C687BBF6CC.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6A9D8D66C.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C6C1C13735.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C65D35A06E.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6DAA8D090.png");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C612FC6EF0.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6514C6F86.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C686E054DF.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C6173BA3E0.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C619D355CA.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6027D2051.png");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C624E5C21D.png");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6ECA5669C.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C64B94BE26.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6C126442A.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C693F6FB13.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6C8AD5CC8.png");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C6ADAAF552.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6C809C128.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C64598AA71.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C66C59E352.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C6030C1964.png");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C6E6070E28.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C623C4B439.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C621218A87.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C66C2DAEFB.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C6F5745331.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6EB393F20.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C6C86B05A9.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C68781584B.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6A85EBF8B.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C66381E415.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C648573175.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C697D70F08.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6DB636C70.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C62ACFFA50.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.5.jpg", "1000000000000235000001C663B0DD4E.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.6.jpg", "1000000000000235000001C62C4BE0B1.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.7.jpg", "1000000000000235000001C640235978.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C6FA599906.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C69659A2D6.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C641D3954D.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C6DE60D0C1.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C6E9C1DA01.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C60A7D6FAA.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C682600D0E.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.5.jpg", "1000000000000235000001C6DB5B00EF.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.6.jpg", "1000000000000235000001C600C90D9E.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.7.jpg", "1000000000000235000001C61A0F2E24.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C62D7ACD95.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6F0F14DA2.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C63ACE0B1D.png");
                }
            });

    private static final Map<String, String> CCAA_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C639AED20F.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C65D803505.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C6ACE45EF9.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6FFD74C47.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C650DBA3DA.png");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C67C06E7BC.png");
                    put("DistribucionNivelAccesibilidadS6.jpg", "1000000000000235000001C6093E9BD2.png");
                    put("DistribucionNivelAccesibilidadS7.jpg", "1000000000000235000001C64FB89741.png");
                    put("DistribucionNivelAccesibilidadS8.jpg", "1000000000000235000001C6B159501B.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C614B44674.png");
                    put("ComparacionAdecuacionSegmento2.jpg", "1000000000000235000001C6EF33500F.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C63B074F1F.png");
                    put("ComparacionPuntuacionSegmento2.jpg", "1000000000000235000001C6B2933CA3.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C670DCEDBE.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6B446FCC4.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C61F22DC81.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6BCE2C793.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C616749218.png");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C6E0498157.png");
                    put("ComparacionMediasVerificacionNAI6.jpg", "1000000000000235000001C63AC4D6BB.png");
                    put("ComparacionMediasVerificacionNAI7.jpg", "1000000000000235000001C6D15A7326.png");
                    put("ComparacionMediasVerificacionNAI8.jpg", "1000000000000235000001C65AF14BB4.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C67A9D418B.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C6C416D2F2.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6F11245DB.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6533A7F7D.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C65AC2CA3F.png");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C6951FF9AB.png");
                    put("ComparacionMediasVerificacionNAII6.jpg", "1000000000000235000001C6DB5DCD13.png");
                    put("ComparacionMediasVerificacionNAII7.jpg", "1000000000000235000001C6F450F08D.png");
                    put("ComparacionMediasVerificacionNAII8.jpg", "1000000000000235000001C68857D208.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C60B1599E7.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C66351035D.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C6E5B15DA2.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6ACD707E0.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6F81C11CE.png");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C653C5F4A2.png");
                    put("ComparacionModalidadVerificacionI6.jpg", "1000000000000235000001C6F06604B6.png");
                    put("ComparacionModalidadVerificacionI7.jpg", "1000000000000235000001C683A2840A.png");
                    put("ComparacionModalidadVerificacionI8.jpg", "1000000000000235000001C6E71CECFE.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C614A6690A.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C6D898CE7A.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C60F8863AF.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C6077F5F5E.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6718D6EB4.png");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C61AF74504.png");
                    put("ComparacionModalidadVerificacionII6.jpg", "1000000000000235000001C69A96D428.png");
                    put("ComparacionModalidadVerificacionII7.jpg", "1000000000000235000001C60B9D66B0.png");
                    put("ComparacionModalidadVerificacionII8.jpg", "1000000000000235000001C6AA3B03DA.png");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6F2DC68D9.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6F25D5A43.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6FF369EE9.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C6C672C409.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6D301B120.png");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C6EBAAD36A.png");
                    put("ComparacionMediasAspectos6.jpg", "1000000000000235000001C6B4ADD644.png");
                    put("ComparacionMediasAspectos7.jpg", "1000000000000235000001C61480C7B3.png");
                    put("ComparacionMediasAspectos8.jpg", "1000000000000235000001C60601DC7B.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C667DF1C4C.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C6514D73B3.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6AEC1A100.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C6DA58A12F.png");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C62AC8C52A.png");
                    put("DistribucionPuntuacionesS6.jpg", "1000000000000235000001C66EFB5B85.png");
                    put("DistribucionPuntuacionesS7.jpg", "1000000000000235000001C6AE288F53.png");
                    put("DistribucionPuntuacionesS8.jpg", "1000000000000235000001C6588ED0DF.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C631FA6F7D.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C662E57638.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C61AE2F48F.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C683A5A959.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6FB75254B.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C6B4AD0E03.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C699498902.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C69999DA68.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C62DA77747.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C67B491197.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C666C055CA.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6D76C04B4.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C658C01AEC.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.5.jpg", "1000000000000235000001C6349B1903.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.6.jpg", "1000000000000235000001C697364F4E.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.7.jpg", "1000000000000235000001C6033EB0E7.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C6E01D972A.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C6CBF52BE4.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6A5834987.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C689FD2362.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C6B46D5333.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C67637F335.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C664174229.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.5.jpg", "1000000000000235000001C68BA188BA.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.6.jpg", "1000000000000235000001C65D6584AC.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.7.jpg", "1000000000000235000001C6A087FDED.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C690CDB4C1.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C601A6E006.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C666404285.png");
                }
            });

    private static final Map<String, String> EELL_IMAGES = Collections.unmodifiableMap(
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

    /**
     * Obtiene para una imagen generada por el observatorio el id con el que está incrustada la imagen en el
     * documento OpenOffice. Se usará para sobreescribir el valor placeholder con la imagen adecuada
     *
     * @param tipoObservatorio Long que identifica el tipo de observatorio (AGE, CCAA, EELL)
     * @param imageName        nombre del observatorio de la imagen
     * @return string con el id de incrustación en el documento OpenOffice de la imagen
     */
    public static String getEmbededIdImage(final Long tipoObservatorio, final String imageName) {
        if (tipoObservatorio == Constants.OBSERVATORY_TYPE_AGE) {
            return AGE_IMAGES.get(imageName);
        } else if (tipoObservatorio == Constants.OBSERVATORY_TYPE_CCAA) {
            return CCAA_IMAGES.get(imageName);
        } else if (tipoObservatorio == Constants.OBSERVATORY_TYPE_EELL) {
            return EELL_IMAGES.get(imageName);
        }
        return null;
    }

}
