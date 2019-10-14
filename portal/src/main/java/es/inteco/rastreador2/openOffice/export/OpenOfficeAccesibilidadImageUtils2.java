/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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

import es.inteco.common.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para asignar a cada imagen que se incluye en el informe OpenOffice el
 * nombre que recibe al incrustarse en el documento. Esta asignación permite
 * identificar que imagen existente en la plantilla, que actua como placeholder,
 * hay que sobreescribir.
 */
public final class OpenOfficeAccesibilidadImageUtils2 {

	/** Identificadores de las imágenss de AGE. */
	private static final Map<String, String> AGE_IMAGES = Collections.unmodifiableMap(new HashMap<String, String>() {

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
			
			
		}
	});

	/** Identificadores de las imágenss de CCAAS. */
	private static final Map<String, String> CCAA_IMAGES = Collections.unmodifiableMap(new HashMap<String, String>() {

		private static final long serialVersionUID = -2436843513819074035L;

		{
			put("DistribucionNivelAccesibilidadGlobal.jpg", "1000000000000235000001C6DF705BD5.jpg");
			put("DistribucionNivelAccesibilidadS1.jpg", "1000000000000235000001C6ABCC34E6.jpg");
			put("DistribucionNivelAccesibilidadS2.jpg", "1000000000000235000001C6298AADCB.jpg");
			put("DistribucionNivelAccesibilidadS3.jpg", "1000000000000235000001C61202E581.jpg");
			put("DistribucionNivelAccesibilidadS4.jpg", "1000000000000235000001C68B0AE580.jpg");
			put("DistribucionNivelAccesibilidadS5.jpg", "1000000000000235000001C65A68FC4F.jpg");
			put("DistribucionNivelAccesibilidadS6.jpg", "1000000000000235000001C6826D7C3F.jpg");
			put("DistribucionNivelAccesibilidadS7.jpg", "1000000000000235000001C6356FB0E6.jpg");
			put("DistribucionNivelAccesibilidadS8.jpg", "1000000000000235000001C6E3814656.jpg");

			put("ComparacionAdecuacionSegmento1.jpg", "1000000000000235000001C602C08AE9.jpg");
			put("ComparacionAdecuacionSegmento2.jpg", "1000000000000235000001C6DBE9184F.jpg");
			put("ComparacionPuntuacionSegmento1.jpg", "1000000000000235000001C67AD7C35A.jpg");
			put("ComparacionPuntuacionSegmento2.jpg", "1000000000000235000001C68627537E.jpg");

			put("ComparacionMediasVerificacionNAI.jpg", "1000000000000235000001C6071C62FA.jpg");
			put("ComparacionMediasVerificacionNAI1.jpg", "1000000000000235000001C6DD0AE012.jpg");
			put("ComparacionMediasVerificacionNAI2.jpg", "1000000000000235000001C657809214.jpg");
			put("ComparacionMediasVerificacionNAI3.jpg", "1000000000000235000001C6FD0FBA91.jpg");
			put("ComparacionMediasVerificacionNAI4.jpg", "1000000000000235000001C6649F3487.jpg");
			put("ComparacionMediasVerificacionNAI5.jpg", "1000000000000235000001C656CC0B56.jpg");
			put("ComparacionMediasVerificacionNAI6.jpg", "1000000000000235000001C6BF7CD7FA.jpg");
			put("ComparacionMediasVerificacionNAI7.jpg", "1000000000000235000001C6EF165A12.jpg");
			put("ComparacionMediasVerificacionNAI8.jpg", "1000000000000235000001C66B0466CC.jpg");

			put("ComparacionMediasVerificacionNAII.jpg", "1000000000000235000001C6CB8253EC.jpg");
			put("ComparacionMediasVerificacionNAII1.jpg", "1000000000000235000001C66B714661.jpg");
			put("ComparacionMediasVerificacionNAII2.jpg", "1000000000000235000001C69D68DEF3.jpg");
			put("ComparacionMediasVerificacionNAII3.jpg", "1000000000000235000001C6731A0ADA.jpg");
			put("ComparacionMediasVerificacionNAII4.jpg", "1000000000000235000001C68056BB91.jpg");
			put("ComparacionMediasVerificacionNAII5.jpg", "1000000000000235000001C6964E7DB1.jpg");
			put("ComparacionMediasVerificacionNAII6.jpg", "1000000000000235000001C623DA0C91.jpg");
			put("ComparacionMediasVerificacionNAII7.jpg", "1000000000000235000001C6EBD9189B.jpg");
			put("ComparacionMediasVerificacionNAII8.jpg", "1000000000000235000001C6A8AA9C07.jpg");

			put("ComparacionModalidadVerificacionI.jpg", "1000000000000235000001C638297D3F.jpg");
			put("ComparacionModalidadVerificacionI1.jpg", "1000000000000235000001C63D6EA698.jpg");
			put("ComparacionModalidadVerificacionI2.jpg", "1000000000000235000001C6486E8705.jpg");
			put("ComparacionModalidadVerificacionI3.jpg", "1000000000000235000001C6BD191958.jpg");
			put("ComparacionModalidadVerificacionI4.jpg", "1000000000000235000001C6FAFAC713.jpg");
			put("ComparacionModalidadVerificacionI5.jpg", "1000000000000235000001C662A3F4F7.jpg");
			put("ComparacionModalidadVerificacionI6.jpg", "1000000000000235000001C6AC737899.jpg");
			put("ComparacionModalidadVerificacionI7.jpg", "1000000000000235000001C62B4BBB5B.jpg");
			put("ComparacionModalidadVerificacionI8.jpg", "1000000000000235000001C6EF5316CD.jpg");

			put("ComparacionModalidadVerificacionII.jpg", "1000000000000235000001C6931B5D77.jpg");
			put("ComparacionModalidadVerificacionII1.jpg", "1000000000000235000001C69748CD3F.jpg");
			put("ComparacionModalidadVerificacionII2.jpg", "1000000000000235000001C6DE83F94D.jpg");
			put("ComparacionModalidadVerificacionII3.jpg", "1000000000000235000001C604E8418A.jpg");
			put("ComparacionModalidadVerificacionII4.jpg", "1000000000000235000001C6AF107878.jpg");
			put("ComparacionModalidadVerificacionII5.jpg", "1000000000000235000001C6B6B4F809.jpg");
			put("ComparacionModalidadVerificacionII6.jpg", "1000000000000235000001C65B882891.jpg");
			put("ComparacionModalidadVerificacionII7.jpg", "1000000000000235000001C6BAC2D98C.jpg");
			put("ComparacionModalidadVerificacionII8.jpg", "1000000000000235000001C66B36DB06.jpg");

			put("ComparacionMediasAspectos.jpg", "1000000000000235000001C6728F2B2C.jpg");
			put("ComparacionMediasAspectos1.jpg", "1000000000000235000001C6ED966E7C.jpg");
			put("ComparacionMediasAspectos2.jpg", "1000000000000235000001C602A504F5.jpg");
			put("ComparacionMediasAspectos3.jpg", "1000000000000235000001C679C614A7.jpg");
			put("ComparacionMediasAspectos4.jpg", "1000000000000235000001C6BDCE177A.jpg");
			put("ComparacionMediasAspectos5.jpg", "1000000000000235000001C695FF905A.jpg");
			put("ComparacionMediasAspectos6.jpg", "1000000000000235000001C6B563DAE9.jpg");
			put("ComparacionMediasAspectos7.jpg", "1000000000000235000001C612CD5EC2.jpg");
			put("ComparacionMediasAspectos8.jpg", "1000000000000235000001C604A867EB.jpg");

			put("DistribucionPuntuacionesS1.jpg", "1000000000000235000001C6E6AC3002.jpg");
			put("DistribucionPuntuacionesS2.jpg", "1000000000000235000001C658A41796.jpg");
			put("DistribucionPuntuacionesS3.jpg", "1000000000000235000001C6761BC457.jpg");
			put("DistribucionPuntuacionesS4.jpg", "1000000000000235000001C62B46E9C6.jpg");
			put("DistribucionPuntuacionesS5.jpg", "1000000000000235000001C6C9865081.jpg");
			put("DistribucionPuntuacionesS6.jpg", "1000000000000235000001C68F59CE8F.jpg");
			put("DistribucionPuntuacionesS7.jpg", "1000000000000235000001C6EDD21C1E.jpg");
			put("DistribucionPuntuacionesS8.jpg", "1000000000000235000001C67013B8EA.jpg");

			put("EvolucionNivelConformidadAccesibilidadA.jpg", "1000000000000235000001C6DD33FE7C.jpg");
			put("EvolucionNivelConformidadAccesibilidadAA.jpg", "1000000000000235000001C620A2FFF3.jpg");
			put("EvolucionNivelConformidadAccesibilidadNV.jpg", "1000000000000235000001C688C27269.jpg");
			put("EvolucionPuntuacionMediaAspectoA1.jpg", "1000000000000235000001C67DD39F82.jpg");
			put("EvolucionPuntuacionMediaAspectoA2.jpg", "1000000000000235000001C62EFCEF21.jpg");
			put("EvolucionPuntuacionMediaAspectoA3.jpg", "1000000000000235000001C689B392F6.jpg");
			put("EvolucionPuntuacionMediaAspectoA4.jpg", "1000000000000235000001C6826375E3.jpg");
			put("EvolucionPuntuacionMediaAspectoA5.jpg", "1000000000000235000001C628FB8782.jpg");
			put("EvolucionPuntuacionMediaObservatorio.jpg", "1000000000000235000001C67E67F63E.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.1.jpg", "1000000000000235000001C6AEC0DBFD.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.2.jpg", "1000000000000235000001C69B37BAF4.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.3.jpg", "1000000000000235000001C6DCF987F6.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.4.jpg", "1000000000000235000001C6BEC72D18.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.5.jpg", "1000000000000235000001C6F073195F.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.6.jpg", "1000000000000235000001C604233A4A.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.1.7.jpg", "1000000000000235000001C686421DD7.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.2.1.jpg", "1000000000000235000001C66038BE82.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.2.2.jpg", "1000000000000235000001C6655F8D8C.jpg");
			put("EvolucionPuntuacionMediaVerificacionV1.2.3.jpg", "1000000000000235000001C6DC95393E.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.1.jpg", "1000000000000235000001C64559E087.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.2.jpg", "1000000000000235000001C6C26C1C97.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.3.jpg", "1000000000000235000001C69E97742C.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.4.jpg", "1000000000000235000001C679739851.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.5.jpg", "1000000000000235000001C6CC9BEAE8.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.6.jpg", "1000000000000235000001C6C5BE8695.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.1.7.jpg", "1000000000000235000001C661241CAA.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.2.1.jpg", "1000000000000235000001C6CF53EC6D.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.2.2.jpg", "1000000000000235000001C6847F2867.jpg");
			put("EvolucionPuntuacionMediaVerificacionV2.2.3.jpg", "1000000000000235000001C6DEDC7771.jpg");
			
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", "10000000000005B90000028E80E5C63A95D4F23D.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg", "10000000000005B90000028E822847E25AF15E96.jpg");
			
			
		}
	});

	/** Identificadores de las imágenss de EELL. */
	private static final Map<String, String> EELL_IMAGES = Collections.unmodifiableMap(new HashMap<String, String>() {
		
		private static final long serialVersionUID = 6545151413719903889L;

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
			
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", "10000000000005B90000028E32632B934BF09217.jpg");
			put("EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg", "10000000000005B90000028E4BE8C76AD51F016D.jpg");
			
			
		}
	});

	/**
	 * Instantiates a new open office UNE 2012b image utils.
	 */
	private OpenOfficeAccesibilidadImageUtils2() {
	}

	/**
	 * Obtiene para una imagen generada por el observatorio el id con el que
	 * está incrustada la imagen en el documento OpenOffice. Se usará para
	 * sobreescribir el valor placeholder con la imagen adecuada
	 *
	 * @param tipoObservatorio
	 *            Long que identifica el tipo de observatorio (AGE, CCAA, EELL)
	 * @param imageName
	 *            nombre del observatorio de la imagen
	 * @return string con el id de incrustación en el documento OpenOffice de la
	 *         imagen
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
