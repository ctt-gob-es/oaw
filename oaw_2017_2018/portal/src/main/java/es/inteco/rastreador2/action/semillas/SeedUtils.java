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
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;

public final class SeedUtils {

	private SeedUtils() {
	}

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

	@SuppressWarnings("unchecked")
	public static List<SemillaForm> getSeedsFromFile(InputStream inputStream) throws Exception {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			digester.push(new ArrayList<SemillaForm>());

			digester.addObjectCreate(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA, SemillaForm.class);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_NOMBRE, "setNombre", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ACTIVA, "setActivaStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_IN_DIRECTORY, "setInDirectoryStr", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_ACRONIMO, "setAcronimo", 0);
			// digester.addCallMethod(Constants.XML_LISTA + "/" +
			// Constants.XML_SEMILLA + "/" + Constants.XML_DEPENDENCIA,
			// "setDependencia", 0);
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_URL, "addListUrl", 0);

			// TODO 2017 Lista de dependencias
			// V1 Lista de dependencias
			// <dependencias><dependencia></dependencia></dependencias>
			// digester.addObjectCreate("lista/semilla/dependencias/dependencia",
			// DependenciaForm.class);
			// digester.addCallMethod("lista/semilla/dependencias/dependencia/id_dependencia",
			// "setId", 0, new Class[] { Long.class });
			// digester.addCallMethod("lista/semilla/dependencias/dependencia/nombre",
			// "setName", 0);
			// digester.addSetNext("lista/semilla/dependencias/dependencia",
			// "addDependencia");

			// V2 Etiqueta <dependencia></dependencia> tanstas veces como
			// dependencia que contiene el nombre
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_DEPENDENCIA, "addDependenciaPorNombre", 0);

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

	public static void writeFileToResponse(HttpServletResponse response, List<SemillaForm> seeds) throws Exception {

		OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
		XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
		response.setContentType("text/xml");
		response.setHeader("Content-disposition", "attachment; filename=seeds.xml");
		ContentHandler hd = serializer.asContentHandler();
		hd.startDocument();
		hd.startElement("", "", Constants.XML_LISTA, null);

		for (SemillaForm semillaForm : seeds) {
			hd.startElement("", "", Constants.XML_SEMILLA, null);
			hd.startElement("", "", Constants.XML_NOMBRE, null);
			hd.characters(semillaForm.getNombre().toCharArray(), 0, semillaForm.getNombre().length());
			hd.endElement("", "", Constants.XML_NOMBRE);

			hd.startElement("", "", Constants.XML_ACTIVA, null);
			hd.characters(String.valueOf(semillaForm.isActiva()).toCharArray(), 0, String.valueOf(semillaForm.isActiva()).length());
			hd.endElement("", "", Constants.XML_ACTIVA);

			// TODO 2017 Lista de URLs

			// List<String> urls =
			// Arrays.asList(semillaForm.getListaUrlsString().split(";"));
			// for (String url : urls) {
			// hd.startElement("", "", Constants.XML_URL, null);
			// hd.characters(url.toCharArray(), 0, url.length());
			// hd.endElement("", "", Constants.XML_URL);
			// }

			List<String> urls = Arrays.asList(semillaForm.getListaUrlsString().split(";"));

			if (urls != null && !urls.isEmpty()) {
				hd.startElement("", "", "urls", null);
				for (String url : urls) {
					hd.startElement("", "", Constants.XML_URL, null);
					hd.characters(url.toCharArray(), 0, url.length());
					hd.endElement("", "", Constants.XML_URL);
				}
				hd.endElement("", "", "urls");
			}

			// ACRONIMO
			if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
				hd.startElement("", "", Constants.XML_ACRONIMO, null);
				hd.characters(semillaForm.getAcronimo().toCharArray(), 0, semillaForm.getAcronimo().length());
				hd.endElement("", "", Constants.XML_ACRONIMO);
			}

			// TODO 2017 Multidependencia

			// V1 Lista de dependencias
			// <dependencias><dependencia></dependencia></dependencias>
			if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
				hd.startElement("", "", Constants.XML_DEPENDENCIAS, null);
				for (DependenciaForm dependencia : semillaForm.getDependencias()) {
					hd.startElement("", "", Constants.XML_DEPENDENCIA, null);
//					hd.startElement("", "", Constants.XML_DEPENDENCIA_NOMBRE, null);
					hd.characters(dependencia.getName().toCharArray(), 0, dependencia.getName().length());
//					hd.endElement("", "", Constants.XML_DEPENDENCIA_NOMBRE);
					hd.endElement("", "", Constants.XML_DEPENDENCIA);
				}
				hd.endElement("", "", Constants.XML_DEPENDENCIAS);
			}

			// V2 Una etiqueta <dependencia> por cada dependnecia que contiene
			// el nombre de la dependencia
//			if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
//				for (DependenciaForm dependencia : semillaForm.getDependencias()) {
//					hd.startElement("", "", Constants.XML_DEPENDENCIA, null);
//					hd.characters(dependencia.getName().toCharArray(), 0, dependencia.getName().length());
//					hd.endElement("", "", Constants.XML_DEPENDENCIA);
//				}
//			}

			// DEPENDE_DE
			// if (StringUtils.isNotEmpty(semillaForm.getDependencia())) {
			// hd.startElement("", "", Constants.XML_DEPENDENCIA, null);
			// hd.characters(semillaForm.getDependencia().toCharArray(), 0,
			// semillaForm.getDependencia().length());
			// hd.endElement("", "", Constants.XML_DEPENDENCIA);
			// }

			hd.startElement("", "", Constants.XML_IN_DIRECTORY, null);
			hd.characters(String.valueOf(semillaForm.isInDirectory()).toCharArray(), 0, String.valueOf(semillaForm.isInDirectory()).length());
			hd.endElement("", "", Constants.XML_IN_DIRECTORY);

			hd.endElement("", "", Constants.XML_SEMILLA);
		}

		hd.endElement("", "", Constants.XML_LISTA);
		hd.endDocument();
	}

}
