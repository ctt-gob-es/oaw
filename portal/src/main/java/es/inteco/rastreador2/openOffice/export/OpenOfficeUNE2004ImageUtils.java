package es.inteco.rastreador2.openOffice.export;

import es.inteco.common.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para asignar a cada imagen que se incluye en el informe OpenOffice el nombre que recibe al incrustarse en el documento.
 * Esta asignación permite identificar que imagen existente en la plantilla, que actua como placeholder, hay que sobreescribir.
 */
public final class OpenOfficeUNE2004ImageUtils {

    private static final Map<String, String> AGE_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C656F13328.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C69C5B6C64.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C614F27F0A.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6BD0B4CBA.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6E28F2B85.png");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6920E7CD4.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C61A4A3B95.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6AE8D6D4C.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C67F09331B.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C603BF8AC4.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6497B82D1.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6C99DC95B.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6937174F5.png");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C688F009E4.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C621A45F48.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C69D68F6EF.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C677D6555E.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C67940FBFF.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6861A9DB9.png");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C698B82CAB.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C69ED29945.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6E138B19F.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C62E0F8A11.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6098AEA60.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6EBE93738.png");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C6FC0F7DCC.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6C8E40FC5.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C61C06A346.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65EEC8D6A.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C644B3AB97.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6E86A1A4D.png");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C65D3B9503.png");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6BE79C3C1.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6121C10CC.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6D1EFC0F6.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C657B26CD6.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6B6775FE7.png");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C6094A953B.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C69F161F89.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C65FD6B2D9.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6629DC1E0.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C64F035130.png");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C68C9E87DB.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C68AE3915D.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C69D84230E.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60A54315E.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C67E12C804.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6523B4FD4.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C690C0FAA4.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C67686AF27.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C627AC2925.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C6BC60CD63.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C654B664E9.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C65A63BCFD.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6A1D35AB0.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C608649C62.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C62939C133.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C67FE9D927.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C64973F0AA.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C67372430A.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C65B14D0A4.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C610EEA931.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C6D569F5D8.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C62A3E716D.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C622BA8F28.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6E29E7FBA.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C63945088C.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6B3A4A5A7.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C65B622F7A.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C6F84BBB63.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C67A0B42D7.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C69BD75158.png");
                }
            });

    private static final Map<String, String> CCAA_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C6CEACFAB9.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C6BC40E071.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C618196EB2.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6EE2C298A.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C608E6FA62.png");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6217BD0B7.png");
                    put("DistribucionNivelAccesibilidadS6.jpg", "1000000000000235000001C6641C8DB0.png");
                    put("DistribucionNivelAccesibilidadS7.jpg", "1000000000000235000001C6D4EC0873.png");
                    put("DistribucionNivelAccesibilidadS8.jpg", "1000000000000235000001C64DD7CCA5.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C64303F649.png");
                    put("ComparacionAdecuacionSegmento2.jpg", "1000000000000235000001C6C482FDD4.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6C9E2DCE6.png");
                    put("ComparacionPuntuacionSegmento2.jpg", "1000000000000235000001C690E9951A.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C6993BDBCC.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6504940D5.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C657772FB9.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6EA599878.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6684EA729.png");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C693722A64.png");
                    put("ComparacionMediasVerificacionNAI6.jpg", "1000000000000235000001C6FC367330.png");
                    put("ComparacionMediasVerificacionNAI7.jpg", "1000000000000235000001C69DACAA18.png");
                    put("ComparacionMediasVerificacionNAI8.jpg", "1000000000000235000001C64F2038F2.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C617C715D7.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C68BDF697D.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6265F9360.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6DD658EA8.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6B2C83156.png");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C6623D6F67.png");
                    put("ComparacionMediasVerificacionNAII6.jpg", "1000000000000235000001C6C7A26201.png");
                    put("ComparacionMediasVerificacionNAII7.jpg", "1000000000000235000001C60134E087.png");
                    put("ComparacionMediasVerificacionNAII8.jpg", "1000000000000235000001C608A9CFA2.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C6FEF2C2AF.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C686CFC101.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C6505B0E12.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6516A5784.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C68822FFFD.png");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C675673566.png");
                    put("ComparacionModalidadVerificacionI6.jpg", "1000000000000235000001C62173F23D.png");
                    put("ComparacionModalidadVerificacionI7.jpg", "1000000000000235000001C6846F37D1.png");
                    put("ComparacionModalidadVerificacionI8.jpg", "1000000000000235000001C667BCC5F3.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C622FF45D8.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C627092B50.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65FA95C3A.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C6CA582B31.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C66072D77B.png");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C660EF8190.png");
                    put("ComparacionModalidadVerificacionII6.jpg", "1000000000000235000001C6F7E36790.png");
                    put("ComparacionModalidadVerificacionII7.jpg", "1000000000000235000001C6E9035967.png");
                    put("ComparacionModalidadVerificacionII8.jpg", "1000000000000235000001C6904CE659.png");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6B59F2FFC.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6D091AD12.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6156D8D3D.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C622534377.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C60762B04A.png");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C65802B2A3.png");
                    put("ComparacionMediasAspectos6.jpg", "1000000000000235000001C615D9B221.png");
                    put("ComparacionMediasAspectos7.jpg", "1000000000000235000001C6D723810E.png");
                    put("ComparacionMediasAspectos8.jpg", "1000000000000235000001C65B850C03.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6CC97B3F9.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C6F5C9B7A6.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C664A0BBC7.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C69276C92B.png");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C6B7284C75.png");
                    put("DistribucionPuntuacionesS6.jpg", "1000000000000235000001C6D37E4218.png");
                    put("DistribucionPuntuacionesS7.jpg", "1000000000000235000001C6F5B95D4B.png");
                    put("DistribucionPuntuacionesS8.jpg", "1000000000000235000001C6EAB36EB3.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C681EDA615.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C6C132792B.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60E86287C.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C6AF16165A.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C65A2231A1.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C611D92354.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C618587F18.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6D639A05F.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C66C41557E.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C683C5C6F7.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C64DF372EB.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6887FF80A.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C666E639EE.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C6B437A98F.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C64FC80FFC.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6538EB8CD.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C6EC835330.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C65B116467.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C6051C961D.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C663EEDED5.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C64C7CF6CE.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C61922C0BF.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C641E14BDC.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6E1C7CBF6.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6CA45B7AE.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C69430C777.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C6B55D7605.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C656A5F457.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C6A4CB1421.png");
                }
            });

    private static final Map<String, String> EELL_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C656F13328.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C69C5B6C64.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C614F27F0A.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6BD0B4CBA.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6E28F2B85.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C61A4A3B95.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6AE8D6D4C.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C67F09331B.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C603BF8AC4.png");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6497B82D1.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6C99DC95B.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6937174F5.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C621A45F48.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C69D68F6EF.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C677D6555E.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C67940FBFF.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6861A9DB9.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C69ED29945.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6E138B19F.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C62E0F8A11.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6098AEA60.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6EBE93738.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6C8E40FC5.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C61C06A346.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65EEC8D6A.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C644B3AB97.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6E86A1A4D.png");

                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6BE79C3C1.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6121C10CC.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6D1EFC0F6.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C657B26CD6.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6B6775FE7.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C69F161F89.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C65FD6B2D9.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6629DC1E0.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C64F035130.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C68AE3915D.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C69D84230E.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60A54315E.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C67E12C804.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6523B4FD4.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C690C0FAA4.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C67686AF27.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C627AC2925.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C6BC60CD63.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C654B664E9.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C65A63BCFD.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6A1D35AB0.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C608649C62.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C62939C133.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C67FE9D927.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C64973F0AA.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C67372430A.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C65B14D0A4.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C610EEA931.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C6D569F5D8.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C62A3E716D.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C622BA8F28.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6E29E7FBA.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C63945088C.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6B3A4A5A7.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C65B622F7A.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C6F84BBB63.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C67A0B42D7.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C69BD75158.png");
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
