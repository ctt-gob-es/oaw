
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
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ca.utoronto.atrc.tile.accessibilitychecker.AllChecks;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Guideline;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.iana.IanaUtils;

/**
 *
 */
public class ResourcesTest {
	@Ignore
	@Test
	public void testChecksResource() throws Exception {
		EvaluatorUtility.initialize();
		AllChecks allChecks = EvaluatorUtility.getAllChecks();
		Assert.assertFalse(allChecks.isEmpty());
	}

	@Ignore
	@Test
	public void testGuidelinesResource() throws Exception {
		EvaluatorUtility.initialize();
		final Guideline guideline = EvaluatorUtility.loadGuideline("observatorio-inteco-1-0.xml");
		Assert.assertNotNull(guideline);
		Assert.assertEquals("Observatorio INTECO 1.0", guideline.getName());
		Assert.assertEquals("Pautas de verificación del Observatorio de INTECO", guideline.getLongName());
		Assert.assertEquals("observatory", guideline.getType());
		Assert.assertEquals(2, guideline.getGroups().size());
	}

	@Ignore
	@Test
	public void testLanguages() throws Exception {
		final PropertiesManager prm = new PropertiesManager();
		final String ianaRegistries = IanaUtils.loadIanaRegistries(prm.getValue("intav.properties", "iana.lang.codes.url"));
		Assert.assertNotNull(ianaRegistries);
		Assert.assertFalse(ianaRegistries.isEmpty());
	}
}
