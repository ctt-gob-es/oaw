
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
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.data.general.DefaultPieDataset;
import org.junit.Test;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.GraphicsUtils;

/**
 * Clase para generar gráficos adicionales para los informes agregados usando la
 * librería de java usada para el resto de gráficos y mantener el aspecto del
 * documento.
 */
public class GraficosConclusionesTest {

	/** The Constant OBSERVATORIO_AGE. */
	private static final String OBSERVATORIO_AGE = "AGE";

	/** The Constant OBSERVATORIO_CCAA. */
	private static final String OBSERVATORIO_CCAA = "CCAA";

	/** The Constant OBSERVATORIO_EELL. */
	private static final String OBSERVATORIO_EELL = "EELL";

	/** The df. */
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/** The string date. */
	private String stringDate;

	/**
	 * Gennerar graficos.
	 */
	@Test
	public void gennerarGraficos() {
		try {
			// stringDate = df.format(new Date()) + "/" + OBSERVATORIO_AGE;
			// graficoEvolucionIteracion("age-anterior", 102, 35, 237);
			// graficoEvolucionIteracion("age-primera", 142, 38, 164);

			// stringDate = df.format(new Date()) + "/" + OBSERVATORIO_CCAA;
			// graficoEvolucionIteracion("ccaa-anterior", 23, 13, 115);
			// graficoEvolucionIteracion("ccaa-primera", 56, 24, 70);

			stringDate = df.format(new Date()) + "/" + OBSERVATORIO_EELL;
			graficoEvolucionIteracion("eell-anterior", 23, 20, 154);
			// graficoEvolucionIteracion("eell-primera", 40, 20, 136);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Grafico evolucion iteracion.
	 *
	 * @param iteracion
	 *            the iteracion
	 * @param mejoran
	 *            the mejoran
	 * @param empeoran
	 *            the empeoran
	 * @param mantienen
	 *            the mantienen
	 * @throws Exception
	 *             the exception
	 */
	private void graficoEvolucionIteracion(String iteracion, int mejoran, int empeoran, int mantienen)
			throws Exception {

		final String sectionLabel = "N\u00BA Portales: ";
		final int total = empeoran + mantienen + mejoran;

		final DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
		dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
		dataSet.setValue("Mejoran", Integer.valueOf(mejoran));

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total,
				"/home/alvaro/Documents/minhafp/2018_2019/CCAA/oaw-graficas-" + stringDate + "/evol_" + iteracion
						+ ".jpg",
				"", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

}
