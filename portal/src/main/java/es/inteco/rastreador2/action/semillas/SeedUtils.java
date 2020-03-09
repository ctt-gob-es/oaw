/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.action.semillas;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester.Digester;
import org.xml.sax.ContentHandler;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;

/**
 * The Class SeedUtils.
 */
public final class SeedUtils {
	/**
	 * Instantiates a new seed utils.
	 */
	private SeedUtils() {
	}

	/**
	 * Gets the valid urls.
	 *
	 * @param seed     the seed
	 * @param validate the validate
	 * @return the valid urls
	 */
	public static List<String> getValidUrls(final String seed, boolean validate) {
		List<String> validUrls = new ArrayList<>();
		List<String> seedUrls = Arrays.asList(seed.split("\r\n"));
		for (String seedUrl : seedUrls) {
			if (!seedUrl.startsWith("http://") && !seedUrl.startsWith("https://")) {
				seedUrl = "http://" + seedUrl;
			}
			if (validate) {
				try {
					URLConnection ur = new URL(seedUrl).openConnection();
					ur.connect();
					validUrls.add(seedUrl);
				} catch (Exception e) {
					Logger.putLog("Excepcion URL :", NuevaSemillaWebsAction.class, Logger.LOG_LEVEL_ERROR);
				}
			} else {
				validUrls.add(seedUrl);
			}
		}
		return validUrls;
	}

	/**
	 * Gets the seed urls for database.
	 *
	 * @param validUrls the valid urls
	 * @return the seed urls for database
	 */
	public static String getSeedUrlsForDatabase(List<String> validUrls) {
		String listaUrl = "";
		int primer = 1;
		for (String url : validUrls) {
			if (primer == 1) {
				listaUrl = url;
				primer++;
			} else {
				listaUrl = listaUrl + ";" + url;
			}
		}
		return listaUrl;
	}

	/**
	 * Gets the seeds from file.
	 *
	 * @param inputStream     the input stream
	 * @param includeCategory the include category
	 * @return the seeds from file
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public static List<SemillaForm> getSeedsFromFile(InputStream inputStream, boolean includeCategory) throws Exception {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			digester.push(new ArrayList<SemillaForm>());
			digester.addObjectCreate(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA, SemillaForm.class);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ID, "setIdStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_NOMBRE, "setNombre", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ACTIVA, "setActivaStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ELIMINADA, "setEliminarStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_IN_DIRECTORY, "setInDirectoryStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ACRONIMO, "setAcronimo", 0);
			// URL como lista
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_URL, "addListUrl", 0);
			// Lista de dependencias
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_DEPENDENCIA + "/", "addDependenciaPorNombre", 0);
			if (includeCategory) {
				digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_SEGMENTO + "/", "setCategoryName", 0);
			}
			// ambito
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_AMBITO, "setAmbitName", 0);
			// complejidad
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_COMPLEJIDAD, "setComplexityName", 0);
			// Lista de etiquetas
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ETIQUETAS + "/", "addEtiquetaPorNombre", 0);
			digester.addSetNext(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA, "add");
			return (List<SemillaForm>) digester.parse(inputStream);
		} catch (Exception e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					Logger.putLog("Error al cerrar el InputStream", SeedUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
	}

	/**
	 * Write file to response.
	 * 
	 * Generates an XML and add to response.
	 *
	 * @param response        the response
	 * @param seeds           the seeds
	 * @param includeCategory the include category
	 * @throws Exception the exception
	 */
	public static void writeFileToResponse(HttpServletResponse response, List<SemillaForm> seeds, boolean includeCategory) throws Exception {
		OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
		XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
		response.setContentType("text/xml");
		response.setHeader("Content-disposition", "attachment; filename=seeds.xml");
		ContentHandler hd = serializer.asContentHandler();
		hd.startDocument();
		hd.startElement("", "", Constants.XML_LISTA, null);
		for (SemillaForm semillaForm : seeds) {
			hd.startElement("", "", Constants.XML_SEMILLA, null);
			hd.startElement("", "", Constants.XML_ID, null);
			hd.characters(String.valueOf(semillaForm.getId()).toCharArray(), 0, String.valueOf(semillaForm.getId()).length());
			hd.endElement("", "", Constants.XML_ID);
			hd.startElement("", "", Constants.XML_NOMBRE, null);
			hd.characters(semillaForm.getNombre().toCharArray(), 0, semillaForm.getNombre().length());
			hd.endElement("", "", Constants.XML_NOMBRE);
			hd.startElement("", "", Constants.XML_ACTIVA, null);
			hd.characters(String.valueOf(semillaForm.isActiva()).toCharArray(), 0, String.valueOf(semillaForm.isActiva()).length());
			hd.endElement("", "", Constants.XML_ACTIVA);
			hd.startElement("", "", Constants.XML_ELIMINADA, null);
			hd.characters(String.valueOf(semillaForm.isEliminar()).toCharArray(), 0, String.valueOf(semillaForm.isEliminar()).length());
			hd.endElement("", "", Constants.XML_ELIMINADA);
			// Lista de URLs
			List<String> urls = null;
			if (semillaForm.getListaUrlsString() != null) {
				urls = Arrays.asList(semillaForm.getListaUrlsString().split(";"));
			}
			hd.startElement("", "", Constants.XML_URL, null);
			if (urls != null && !urls.isEmpty()) {
				for (int i = 0; i < urls.size(); i++) {
					hd.characters(urls.get(i).toCharArray(), 0, urls.get(i).length());
					if (i < urls.size() - 1) {
						hd.characters("\n".toCharArray(), 0, "\n".length());
					}
				}
			}
			hd.endElement("", "", Constants.XML_URL);
			// ACRONIMO
			hd.startElement("", "", Constants.XML_ACRONIMO, null);
			if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
				hd.characters(semillaForm.getAcronimo().toCharArray(), 0, semillaForm.getAcronimo().length());
			}
			hd.endElement("", "", Constants.XML_ACRONIMO);
			// Multidependencia
			// Depenencias separadas por salto de linea
			hd.startElement("", "", Constants.XML_DEPENDENCIA, null);
			List<DependenciaForm> dependencias = semillaForm.getDependencias();
			if (dependencias != null && !dependencias.isEmpty()) {
				for (int i = 0; i < dependencias.size(); i++) {
					hd.characters(dependencias.get(i).getName().toCharArray(), 0, dependencias.get(i).getName().length());
					if (i < dependencias.size() - 1) {
						hd.characters("\n".toCharArray(), 0, "\n".length());
					}
				}
			}
			hd.endElement("", "", Constants.XML_DEPENDENCIA);
			hd.startElement("", "", Constants.XML_IN_DIRECTORY, null);
			hd.characters(String.valueOf(semillaForm.isInDirectory()).toCharArray(), 0, String.valueOf(semillaForm.isInDirectory()).length());
			hd.endElement("", "", Constants.XML_IN_DIRECTORY);
			if (includeCategory) {
				hd.startElement("", "", Constants.XML_SEGMENTO, null);
				if (semillaForm.getCategoria() != null && semillaForm.getCategoria().getName() != null) {
					hd.characters(semillaForm.getCategoria().getName().toCharArray(), 0, semillaForm.getCategoria().getName().length());
				}
				hd.endElement("", "", Constants.XML_SEGMENTO);
			}
			// ambito
			hd.startElement("", "", Constants.XML_AMBITO, null);
			if (semillaForm.getAmbito() != null && semillaForm.getAmbito().getName() != null) {
				hd.characters(semillaForm.getAmbito().getName().toCharArray(), 0, semillaForm.getAmbito().getName().length());
			}
			hd.endElement("", "", Constants.XML_AMBITO);
			// complejidad
			hd.startElement("", "", Constants.XML_COMPLEJIDAD, null);
			if (semillaForm.getComplejidad() != null && semillaForm.getComplejidad().getName() != null) {
				hd.characters(semillaForm.getComplejidad().getName().toCharArray(), 0, semillaForm.getComplejidad().getName().length());
			}
			hd.endElement("", "", Constants.XML_COMPLEJIDAD);
			// etiquetas
			hd.startElement("", "", Constants.XML_ETIQUETAS, null);
			List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
			if (etiquetas != null && !etiquetas.isEmpty()) {
				for (int i = 0; i < etiquetas.size(); i++) {
					hd.characters(etiquetas.get(i).getName().toCharArray(), 0, etiquetas.get(i).getName().length());
					if (i < etiquetas.size() - 1) {
						hd.characters("\n".toCharArray(), 0, "\n".length());
					}
				}
			}
			hd.endElement("", "", Constants.XML_ETIQUETAS);
			hd.endElement("", "", Constants.XML_SEMILLA);
		}
		hd.endElement("", "", Constants.XML_LISTA);
		hd.endDocument();
	}
}
