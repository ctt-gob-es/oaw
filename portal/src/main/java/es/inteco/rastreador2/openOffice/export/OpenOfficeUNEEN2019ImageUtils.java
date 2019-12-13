/*******************************************************************************
* Copyright (C) 2019 MPTFP, Ministerio de Política Territorial y Función Pública,
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.openOffice.export;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para asignar a cada imagen que se incluye en el informe OpenOffice el nombre que recibe al incrustarse en el documento. Esta asignación permite identificar que imagen existente en la
 * plantilla, que actua como placeholder, hay que sobreescribir.
 */
public final class OpenOfficeUNEEN2019ImageUtils {
	/** Identificadores de las imágenss de AGE. */
	private static final Map<String, String> IMAGES = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = -3344927809282590724L;
		{
			put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C66796306D34210727.png");
			put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C686353DFB4E5F554D.png");
			put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C626EA2D7E35177CC8.png");
			put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C6238EEF9251DA1641.png");
			put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C6F2C6D1D5FC85731D.png");
			put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C6AED30F1F121DB2C0.png");
			put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C616AE5B194E68BF0E.png");
			put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C6818B3EC0EEA88A8E.png");
			put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C64F7A8818C7D6EE70.png");
			put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6270B7F90C5A323BF.png");
			put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C652416902B8647180.png");
			put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C67251BF1701D4A747.png");
			put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6524991064D4A1A39.png");
			put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C694D284E86E825284.png");
			put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C61C4B01393A33C9F8.png");
			put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C6D1B3C17934774C94.png");
			put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C6EDCDC3F702D3F482.png");
			put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C60265545A824FDBC5.png");
			put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C627323CCCF16DB4CC.png");
			put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C637BF677D6C5E1897.png");
			put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C64ABDE9E4B475F590.png");
			put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C65ECDE8B3FFC3CADB.png");
			put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C69F4F96742DCF1BA4.png");
			put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C60A2759341B2B1E17.png");
			put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6BB36D752A1C08AE3.png");
			put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C6E00EB8B12120D60A.png");
			put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6F8518AE935A3F8AC.png");
			put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C6D49A132FF10D87BB.png");
			put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C6F0FB22F88096A933.png");
			put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C600CCD52C19F45FA4.png");
			put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C69F42018ADB7EFE05.png");
			put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C63A21E28E76FCEDDF.png");
			put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6336E733D9388E831.png");
			put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C65062748A9989D321.png");
			put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C66EC6B39ACE51808E.png");
			put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C6ED2392E795D71A62.png");
			put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C62799E25C9B759829.png");
			put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C64EC084DF408A08B0.png");
			put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6ADF75A936142B3D9.png");
			put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C69AD7F99C9DEF6310.png");
			put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C612CEC3B3CA883DCD.png");
			put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C696312A943569F142.png");
			put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C666C1CFA19232ED4F.png");
			put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C623C4B439.png");
			put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C621218A87.png");
			put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C66C2DAEFB.png");
			put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C6F5745331.png");
			put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C6EB393F20.png");
			put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C6C86B05A9.png");
			put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C68781584B.png");
			put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C6A85EBF8B.png");
			put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C6967085A546EA0656.png");
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
			put("EvolucionNivelConformidadCombinada.jpg", "10000000000004F10000028E9B7ECBE3983968A5.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAICombinada.jpg", "10000000000005B90000028EEFD7DC73058134FF.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg", "10000000000005B90000028E526D5891A8E9E639.jpg");
			put("EvolucionPuntuacionMediaAspectoCombinada.jpg", "10000000000005B90000028ECF7F32AC6D685F69.jpg");
			put("evolution_suitability_level.jpg", "10000000000004F10000028E04563FF36EE2EA51.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", "10000000000005B90000028EB58986E099184EA5.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg", "10000000000005B90000028EB58986E099184EA5.jpg");
			put("EvolucionPuntuacionMediaSegmentoCombinada1.jpg", "10000000000005B90000028E434DF2C27E62A76E.jpg");
			put("ComparacionCumpliminetoSegmento1.jpg", "1000000000000244000001CAF3A4178E54B8675B.jpg");
			put("ComparacionCumpliminetoComplejidad1.jpg", "1000000000000244000001CAD1A69E978C6E0143.png");
			put("ComparacionAdecuacionComplejidad1.jpg", "1000000000000244000001CA2A1A94D5BFC43135.png");
			put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C6522DBE800405D6FA.png");
		}
	});

	/**
	 * Instantiates a new open office UNE 2012b image utils.
	 */
	private OpenOfficeUNEEN2019ImageUtils() {
	}

	/**
	 * Obtiene para una imagen generada por el observatorio el id con el que está incrustada la imagen en el documento OpenOffice. Se usará para sobreescribir el valor placeholder con la imagen
	 * adecuada
	 *
	 * @param tipoObservatorio Long que identifica el tipo de observatorio (AGE, CCAA, EELL)
	 * @param imageName        nombre del observatorio de la imagen
	 * @return string con el id de incrustación en el documento OpenOffice de la imagen
	 */
	public static String getEmbededIdImage(final Long tipoObservatorio, final String imageName) {
		return IMAGES.get(imageName);
	}
}
