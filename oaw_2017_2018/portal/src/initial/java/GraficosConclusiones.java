
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Before;
import org.junit.Test;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;

/**
 * The Class GraficosConclusiones.
 */
public class GraficosConclusiones {

	private static final String CCAA_TRIBUTOS = "Tributos";

	private static final String CCAA_SALUD = "Salud";

	private static final String CCAA_EMPLEO = "Empleo";

	private static final String CCAA_EDUCACIÓN = "Educación";

	private static final String CCAA_SEDES = "Sedes";

	private static final String CCAA_PARLAMENTOS = "Parlamentos";

	private static final String CCAA_BOLETINES = "Boletines";

	private static final String CCAA_PRINCIPALES = "Principales";

	/** The Constant OBSERVATORIO_AGE. */
	private static final String OBSERVATORIO_AGE = "AGE";

	/** The Constant OBSERVATORIO_CCAA. */
	private static final String OBSERVATORIO_CCAA = "CCAA";

	/** The Constant OBSERVATORIO_EELL. */
	private static final String OBSERVATORIO_EELL = "EELL";

	/** The Constant AGE_SEDES. */
	private static final String AGE_SEDES = CCAA_SEDES;

	/** The Constant AGE_OTROS. */
	private static final String AGE_OTROS = "Otros";

	/** The Constant AGE_TEMATICOS. */
	private static final String AGE_TEMATICOS = "Temáticos";

	/** The Constant AGE_ORGANISMOS. */
	private static final String AGE_ORGANISMOS = "Organismos";

	/** The Constant AGE_PRINCIPALES. */
	private static final String AGE_PRINCIPALES = CCAA_PRINCIPALES;

	/** The Constant PRIORIDAD_1_Y_2. */
	private static final String PRIORIDAD_1_Y_2 = "Prioridad 1 y 2";

	/** The Constant PRIORIDAD_1. */
	private static final String PRIORIDAD_1 = "Prioridad 1";

	/** The Constant PARCIAL. */
	private static final String PARCIAL = "Parcial";

	/** The Constant EELL_MUNICIPIOS_2000_5000_HABIT. */
	private static final String EELL_MUNICIPIOS_2000_5000_HABIT = "Municipios 2000-5000 Habit.";

	/** The Constant EELL_MUNICIPIOS_MAS_POBLADOS. */
	private static final String EELL_MUNICIPIOS_MAS_POBLADOS = "Municipios más poblados";

	/** The Constant EELL_AYUNTAMIENTO_DE_CAPITAL. */
	private static final String EELL_AYUNTAMIENTO_DE_CAPITAL = "Ayuntamiento de capital";

	/** The Constant EELL_DIPUTACIONES_PROVINCIALES. */
	private static final String EELL_DIPUTACIONES_PROVINCIALES = "Diputaciones Provinciales";

	/** The Constant LEVEL_I_VERIFICATIONS. */
	private static final String[] LEVEL_I_VERIFICATIONS = new String[] { "1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.2.1", "1.2.2", "1.2.3" };

	/** The Constant LEVEL_II_VERIFICATIONS. */
	private static final String[] LEVEL_II_VERIFICATIONS = new String[] { "2.1.1", "2.1.2", "2.1.3", "2.1.4", "2.1.5", "2.1.6", "2.1.7", "2.2.1", "2.2.2", "2.2.3" };

	/** The Constant ASPECTS. */
	private static final String[] ASPECTS = new String[] { "General", "Presentación", "Estructura", "Navegación", "Alternativas" };

	/** The pmgr. */
	private PropertiesManager pmgr;

	/** The message resources. */
	private MessageResources messageResources;

	/** The df. */
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/** The string date. */
	private String stringDate;

	/** The observatorio. */
	private String observatorio;

	/**
	 * Inits the.
	 */
	@Before
	public void init() {

		pmgr = new PropertiesManager();
		messageResources = MessageResources.getMessageResources("ApplicationResources");

	}

	/**
	 * Gennerar graficos.
	 */
	@Test
	public void gennerarGraficos() {
		try {
			stringDate = df.format(new Date()) + "/" + OBSERVATORIO_AGE;
			graficoEvolucionIteracion("age-anterior", 102, 35, 237);
			graficoEvolucionIteracion("age-primera", 142, 38, 164);

			stringDate = df.format(new Date()) + "/" + OBSERVATORIO_CCAA;
			graficoEvolucionIteracion("ccaa-anterior", 37, 16, 97);
			graficoEvolucionIteracion("ccaa-primera", 56, 24, 70);

			stringDate = df.format(new Date()) + "/" + OBSERVATORIO_EELL;
			graficoEvolucionIteracion("eell-anterior", 83, 36, 77);
			graficoEvolucionIteracion("eell-primera", 40, 20, 136);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Graficos AGE.
	 *
	 * @throws Exception
	 *             the exception
	 */
	private void graficosAGE() throws Exception {
		// Gráficos de Nivel de Adecuación
		graficoPorcentajeNivelAdecuacionObservatorio("age-anterior", 131, 76, 156);
		graficoPorcentajeNivelAdecuacionObservatorio("age-actual", 114, 70, 190);

		// Gráficos de Evolución respecto a otas iteraciones
		graficoEvolucionIteracion("age-anterior", 102, 35, 237);
		graficoEvolucionIteracion("age-primera", 142, 38, 164);

		// Adecuacion por segmento
		List<Segmento> segmentos = new ArrayList<>();
		segmentos.add(new Segmento(11, 11, 78, AGE_PRINCIPALES));
		segmentos.add(new Segmento(33, 27, 40, AGE_ORGANISMOS));
		segmentos.add(new Segmento(38, 16, 46, AGE_TEMATICOS));
		segmentos.add(new Segmento(56, 26, 18, AGE_OTROS));
		segmentos.add(new Segmento(35, 17, 48, AGE_SEDES));

		graficoAdecuacionSegmento("age-anterior", segmentos);

		segmentos = new ArrayList<>();
		segmentos.add(new Segmento(0, 28, 72, AGE_PRINCIPALES));
		segmentos.add(new Segmento(31, 19, 50, AGE_ORGANISMOS));
		segmentos.add(new Segmento(24, 21, 55, AGE_TEMATICOS));
		segmentos.add(new Segmento(49, 20, 31, AGE_OTROS));
		segmentos.add(new Segmento(35, 14, 51, AGE_SEDES));

		graficoAdecuacionSegmento("age-actual", segmentos);
	}

	/**
	 * Graficos CCAA.
	 *
	 * @throws Exception
	 *             the exception
	 */
	private void graficosCCAA() throws Exception {
		// Gráficos de Nivel de Adecuación
		graficoPorcentajeNivelAdecuacionObservatorio("ccaa-anterior", 131, 76, 156);
		graficoPorcentajeNivelAdecuacionObservatorio("ccaa-actual", 39, 49, 62);

		// Gráficos de Evolución respecto a otas iteraciones
		graficoEvolucionIteracion("ccaa-anterior", 37, 16, 97);
		graficoEvolucionIteracion("ccaa-primera", 56, 24, 70);

		// Adecuacion por segmento
		List<Segmento> segmentos = new ArrayList<>();
		segmentos.add(new Segmento(21, 37, 42, CCAA_PRINCIPALES));
		segmentos.add(new Segmento(45, 33, 22, CCAA_BOLETINES));
		segmentos.add(new Segmento(74, 26, 0, CCAA_PARLAMENTOS));
		segmentos.add(new Segmento(47, 32, 21, CCAA_SEDES));
		segmentos.add(new Segmento(37, 26, 37, CCAA_EDUCACIÓN));
		segmentos.add(new Segmento(34, 22, 44, CCAA_EMPLEO));
		segmentos.add(new Segmento(53, 42, 5, CCAA_SALUD));
		segmentos.add(new Segmento(21, 42, 37, CCAA_TRIBUTOS));

		graficoAdecuacionSegmento("ccaa-actual", segmentos);

		//
		//
		// segmentos = new ArrayList<>();
		// segmentos.add(new Segmento(21, 37, 42, "Principales"));
		// segmentos.add(new Segmento(45, 33, 22, "Boletines"));
		// segmentos.add(new Segmento(74, 26, 0, "Parlamentos"));
		// segmentos.add(new Segmento(47, 32, 21, "Sedes"));
		// segmentos.add(new Segmento(74, 26, 0, "Parlamentos"));
		// segmentos.add(new Segmento(37, 26, 37, "Educación"));
		// segmentos.add(new Segmento(34, 22, 44, "Empleo"));
		// segmentos.add(new Segmento(53, 42, 5, "Salud"));
		// segmentos.add(new Segmento(21, 42, 37, "Tributos"));
		//
		// graficoAdecuacionSegmento("ccaa-anterior", segmentos);
	}

	/**
	 * Grafico porcentaje nivel adecuacion observatorio.
	 *
	 * @param iteracion
	 *            the iteracion
	 * @param parcial
	 *            the parcial
	 * @param prioridad1
	 *            the prioridad 1
	 * @param prioridad1y2
	 *            the prioridad 1 y 2
	 * @throws Exception
	 *             the exception
	 */
	private void graficoPorcentajeNivelAdecuacionObservatorio(String iteracion, int parcial, int prioridad1, int prioridad1y2) throws Exception {
		final String sectionLabel = "N\u00BA Portales: ";
		final int total = parcial + prioridad1 + prioridad1y2;
		final DefaultPieDataset dataSet = new DefaultPieDataset();

		dataSet.setValue("Parcial", parcial);
		dataSet.setValue("Prioridad 1", prioridad1);
		dataSet.setValue("Prioridad 1 y 2", prioridad1y2);

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/distribucion_nivel_adecuacion_" + iteracion + ".jpg", "",
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
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
	private void graficoEvolucionIteracion(String iteracion, int mejoran, int empeoran, int mantienen) throws Exception {

		final String sectionLabel = "N\u00BA Portales: ";
		final int total = empeoran + mantienen + mejoran;

		final DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
		dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
		dataSet.setValue("Mejoran", Integer.valueOf(mejoran));

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evol_" + iteracion + ".jpg", "",
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

	/**
	 * Grafico adecuacion segmento.
	 *
	 * @param iteracion
	 *            the iteracion
	 * @param segmentos
	 *            the segmentos
	 * @throws Exception
	 *             the exception
	 */
	public void graficoAdecuacionSegmento(String iteracion, List<Segmento> segmentos) throws Exception {

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		for (Segmento segmento : segmentos) {
			dataSet.addValue(segmento.getNumParciales(), PARCIAL, segmento.getNombreSegmento());
			dataSet.addValue(segmento.getNumProridad1(), PRIORIDAD_1, segmento.getNombreSegmento());
			dataSet.addValue(segmento.getNumProridad1y2(), PRIORIDAD_1_Y_2, segmento.getNombreSegmento());
		}

		final PropertiesManager pmgr = new PropertiesManager();
		final String noDataMess = "noData";
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/adecuacion_seg_" + iteracion + ".jpg");
	}

	/**
	 * The Class Segmento.
	 */
	public class Segmento {

		/** The num parciales. */
		private int numParciales;

		/** The num proridad 1. */
		private int numProridad1;

		/** The num proridad 1 y 2. */
		private int numProridad1y2;

		/** The nombre segmento. */
		private String nombreSegmento;

		/**
		 * Instantiates a new segmento.
		 *
		 * @param numParciales
		 *            the num parciales
		 * @param numProridad1
		 *            the num proridad 1
		 * @param numProridad1y2
		 *            the num proridad 1 y 2
		 * @param nombreSegmento
		 *            the nombre segmento
		 */
		public Segmento(int numParciales, int numProridad1, int numProridad1y2, String nombreSegmento) {
			super();
			this.numParciales = numParciales;
			this.numProridad1 = numProridad1;
			this.numProridad1y2 = numProridad1y2;
			this.nombreSegmento = nombreSegmento;
		}

		/**
		 * Gets the num parciales.
		 *
		 * @return the numParciales
		 */
		public int getNumParciales() {
			return numParciales;
		}

		/**
		 * Sets the num parciales.
		 *
		 * @param numParciales
		 *            the numParciales to set
		 */
		public void setNumParciales(int numParciales) {
			this.numParciales = numParciales;
		}

		/**
		 * Gets the num proridad 1.
		 *
		 * @return the numProridad1
		 */
		public int getNumProridad1() {
			return numProridad1;
		}

		/**
		 * Sets the num proridad 1.
		 *
		 * @param numProridad1
		 *            the numProridad1 to set
		 */
		public void setNumProridad1(int numProridad1) {
			this.numProridad1 = numProridad1;
		}

		/**
		 * Gets the num proridad 1 y 2.
		 *
		 * @return the numProridad1y2
		 */
		public int getNumProridad1y2() {
			return numProridad1y2;
		}

		/**
		 * Sets the num proridad 1 y 2.
		 *
		 * @param numProridad1y2
		 *            the numProridad1y2 to set
		 */
		public void setNumProridad1y2(int numProridad1y2) {
			this.numProridad1y2 = numProridad1y2;
		}

		/**
		 * Gets the nombre segmento.
		 *
		 * @return the nombreSegmento
		 */
		public String getNombreSegmento() {
			return nombreSegmento;
		}

		/**
		 * Sets the nombre segmento.
		 *
		 * @param nombreSegmento
		 *            the nombreSegmento to set
		 */
		public void setNombreSegmento(String nombreSegmento) {
			this.nombreSegmento = nombreSegmento;
		}

	}

}
