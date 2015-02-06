package es.inteco.rastreador2.openOffice.export;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;

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
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C656F13328.jpg");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C69C5B6C64.jpg");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C614F27F0A.jpg");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6BD0B4CBA.jpg");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6E28F2B85.jpg");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6920E7CD4.jpg");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C61A4A3B95.jpg");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6AE8D6D4C.jpg");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C67F09331B.jpg");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C603BF8AC4.jpg");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C6497B82D1.jpg");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6C99DC95B.jpg");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6937174F5.jpg");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C688F009E4.jpg");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C621A45F48.jpg");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C69D68F6EF.jpg");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C677D6555E.jpg");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C67940FBFF.jpg");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6861A9DB9.jpg");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C698B82CAB.jpg");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C69ED29945.jpg");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C6E138B19F.jpg");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C62E0F8A11.jpg");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6098AEA60.jpg");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6EBE93738.jpg");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C6FC0F7DCC.jpg");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6C8E40FC5.jpg");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C61C06A346.jpg");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65EEC8D6A.jpg");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C644B3AB97.jpg");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6E86A1A4D.jpg");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C65D3B9503.jpg");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6BE79C3C1.jpg");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6121C10CC.jpg");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6D1EFC0F6.jpg");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C657B26CD6.jpg");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6B6775FE7.jpg");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C6094A953B.jpg");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C69F161F89.jpg");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C65FD6B2D9.jpg");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6629DC1E0.jpg");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C64F035130.jpg");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C68C9E87DB.jpg");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C68AE3915D.jpg");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C69D84230E.jpg");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60A54315E.jpg");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C67E12C804.jpg");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C627AC2925.jpg");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C6523B4FD4.jpg");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C690C0FAA4.jpg");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C67686AF27.jpg");
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

    private static final Map<String, String> CCAA_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C6CEACFAB9.jpg");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C6BC40E071.jpg");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C618196EB2.jpg");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6EE2C298A.jpg");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C608E6FA62.jpg");
                    put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6217BD0B7.jpg");
                    put("DistribucionNivelAccesibilidadS6.jpg", "1000000000000235000001C6641C8DB0.jpg");
                    put("DistribucionNivelAccesibilidadS7.jpg", "1000000000000235000001C6D4EC0873.jpg");
                    put("DistribucionNivelAccesibilidadS8.jpg", "1000000000000235000001C64DD7CCA5.jpg");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C64303F649.jpg");
                    put("ComparacionAdecuacionSegmento2.jpg", "1000000000000235000001C6C482FDD4.jpg");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6C9E2DCE6.jpg");
                    put("ComparacionPuntuacionSegmento2.jpg", "1000000000000235000001C690E9951A.jpg");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C6993BDBCC.jpg");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6504940D5.jpg");
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C657772FB9.jpg");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6EA599878.jpg");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6684EA729.jpg");
                    put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C693722A64.jpg");
                    put("ComparacionMediasVerificacionNAI6.jpg", "1000000000000235000001C6FC367330.jpg");
                    put("ComparacionMediasVerificacionNAI7.jpg", "1000000000000235000001C69DACAA18.jpg");
                    put("ComparacionMediasVerificacionNAI8.jpg", "1000000000000235000001C64F2038F2.jpg");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C617C715D7.jpg");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C68BDF697D.jpg");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6265F9360.jpg");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6DD658EA8.jpg");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C6B2C83156.jpg");
                    put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C6623D6F67.jpg");
                    put("ComparacionMediasVerificacionNAII6.jpg", "1000000000000235000001C6C7A26201.jpg");
                    put("ComparacionMediasVerificacionNAII7.jpg", "1000000000000235000001C60134E087.jpg");
                    put("ComparacionMediasVerificacionNAII8.jpg", "1000000000000235000001C608A9CFA2.jpg");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C6FEF2C2AF.jpg");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C686CFC101.jpg");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C6505B0E12.jpg");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6516A5784.jpg");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C68822FFFD.jpg");
                    put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C675673566.jpg");
                    put("ComparacionModalidadVerificacionI6.jpg", "1000000000000235000001C62173F23D.jpg");
                    put("ComparacionModalidadVerificacionI7.jpg", "1000000000000235000001C6846F37D1.jpg");
                    put("ComparacionModalidadVerificacionI8.jpg", "1000000000000235000001C667BCC5F3.jpg");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C622FF45D8.jpg");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C627092B50.jpg");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C65FA95C3A.jpg");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C6CA582B31.jpg");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C66072D77B.jpg");
                    put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C660EF8190.jpg");
                    put("ComparacionModalidadVerificacionII6.jpg", "1000000000000235000001C6F7E36790.jpg");
                    put("ComparacionModalidadVerificacionII7.jpg", "1000000000000235000001C6E9035967.jpg");
                    put("ComparacionModalidadVerificacionII8.jpg", "1000000000000235000001C6904CE659.jpg");


                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6B59F2FFC.jpg");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6D091AD12.jpg");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6156D8D3D.jpg");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C622534377.jpg");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C60762B04A.jpg");
                    put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C65802B2A3.jpg");
                    put("ComparacionMediasAspectos6.jpg", "1000000000000235000001C615D9B221.jpg");
                    put("ComparacionMediasAspectos7.jpg", "1000000000000235000001C6D723810E.jpg");
                    put("ComparacionMediasAspectos8.jpg", "1000000000000235000001C65B850C03.jpg");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6CC97B3F9.jpg");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C6F5C9B7A6.jpg");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C664A0BBC7.jpg");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C69276C92B.jpg");
                    put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C6B7284C75.jpg");
                    put("DistribucionPuntuacionesS6.jpg", "1000000000000235000001C6D37E4218.jpg");
                    put("DistribucionPuntuacionesS7.jpg", "1000000000000235000001C6F5B95D4B.jpg");
                    put("DistribucionPuntuacionesS8.jpg", "1000000000000235000001C6EAB36EB3.jpg");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C681EDA615.jpg");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C6C132792B.jpg");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C60E86287C.jpg");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C6AF16165A.jpg");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C65A2231A1.jpg");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C611D92354.jpg");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C618587F18.jpg");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6D639A05F.jpg");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C66C41557E.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C683C5C6F7.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C64DF372EB.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6887FF80A.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C666E639EE.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C6B437A98F.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C64FC80FFC.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6538EB8CD.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C6EC835330.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C65B116467.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C6051C961D.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C663EEDED5.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C64C7CF6CE.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C61922C0BF.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C641E14BDC.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6E1C7CBF6.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6CA45B7AE.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C69430C777.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C6B55D7605.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C656A5F457.jpg");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C6A4CB1421.jpg");
                }
            });

    private static final Map<String, String> EELL_IMAGES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C6729E6538.png");
                    put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C6112283A4.png");
                    put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C63940B1E5.png");
                    put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6A1D837B2.png");
                    put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6F142FFBD.png");

                    put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C62CD46518.png");
                    put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6CB14E7D5.png");

                    put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C606CBA057.png");
                    put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C62C9C34BE.png"); 
                    put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C681A1944B.png");
                    put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6D1881951.png");
                    put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6F3AA3253.png");

                    put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C601F13223.png");
                    put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C6487B365D.png");
                    put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6EFDD8DD0.png");
                    put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6086F3401.png");
                    put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C69EF5997C.png");

                    put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C64D9C6C5D.png");
                    put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C63140AA55.png");
                    put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C62F6A6DFF.png");
                    put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6401DAA08.png");
                    put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C672535060.png");

                    put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C66E7AE598.png");
                    put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C6194FEC63.png");
                    put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C686CA61B6.png");
                    put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C6F420D10E.png");
                    put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6B16A2FBC.png");

                    put("ComparacionMediasAspectos.jpg", "1000000000000235000001C67B76B919.png");
                    put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6E3660A08.png");
                    put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C6B799BDF0.png");
                    put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C644BBC654.png");
                    put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C686BD089A.png");

                    put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C66527B2F8.png");
                    put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C65E317621.png");
                    put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C668EF3FCA.png");
                    put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C6F41A2715.png");

                    put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C6872FEDED.png");
                    put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C6DA709DBF.png");
                    put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C69223D590.png");
                    put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C667590F6E.png");
                    put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C62CF04718.png");
                    put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C6D7D284D8.png");
                    put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C65868E3B9.png");
                    put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6D0386AFB.png");
                    put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C61C6780DB.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C6A17E0D56.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C6BD72F1AC.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C650687B4D.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C662804C47.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C64AA35BF9.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C6F58F8553.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6AD68F8D4.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.4.jpg", "1000000000000235000001C6D23CB31C.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.5.jpg", "1000000000000235000001C648DC7E68.png");
                    put("EvolucionPuntuacionMediaVerificacionV1.2.6.jpg", "1000000000000235000001C6CD7E7D77.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C6A560653E.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C658476F6E.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C63586F1A3.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C6747FDC6B.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6870960D3.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6B044BCD3.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C6DBAF6701.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.4.jpg", "1000000000000235000001C64471328F.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.5.jpg", "1000000000000235000001C6F0B5DBDE.png");
                    put("EvolucionPuntuacionMediaVerificacionV2.2.6.jpg", "1000000000000235000001C663CD8150.png");
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
