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

			// URL como lista
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_URL, "addListUrl", 0);

			// Lista de dependencias
			digester.addCallMethod(Constants.XML_LISTA + "/" + Constants.XML_SEMILLA + "/" + Constants.XML_DEPENDENCIA + "/", "addDependenciaPorNombre", 0);
			

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

			// Lista de URLs

			List<String> urls = Arrays.asList(semillaForm.getListaUrlsString().split(";"));

			hd.startElement("", "", Constants.XML_URL, null);
			if (urls != null && !urls.isEmpty()) {
				for (int i=0;i<urls.size();i++) {
					
					hd.characters(urls.get(i).toCharArray(), 0, urls.get(i).length());
					if(i<urls.size()-1) {
						hd.characters("\n".toCharArray(), 0, "\n".length());
					}
				}
			}
			hd.endElement("", "", Constants.XML_URL);

			// ACRONIMO
			if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
				hd.startElement("", "", Constants.XML_ACRONIMO, null);
				hd.characters(semillaForm.getAcronimo().toCharArray(), 0, semillaForm.getAcronimo().length());
				hd.endElement("", "", Constants.XML_ACRONIMO);
			}

			// Multidependencia

			// Depenencias separadas por salto de linea
			hd.startElement("", "", Constants.XML_DEPENDENCIA, null);
			List<DependenciaForm> dependencias = semillaForm.getDependencias();
			
			if (dependencias != null && !dependencias.isEmpty()) {
				
				for (int i=0; i<dependencias.size();i++) {
					hd.characters(dependencias.get(i).getName().toCharArray(), 0, dependencias.get(i).getName().length());
					
					if(i<dependencias.size()-1) {
						hd.characters("\n".toCharArray(), 0, "\n".length());
					}
				}
				
			}
			hd.endElement("", "", Constants.XML_DEPENDENCIA);

			hd.startElement("", "", Constants.XML_IN_DIRECTORY, null);
			hd.characters(String.valueOf(semillaForm.isInDirectory()).toCharArray(), 0, String.valueOf(semillaForm.isInDirectory()).length());
			hd.endElement("", "", Constants.XML_IN_DIRECTORY);

			hd.endElement("", "", Constants.XML_SEMILLA);
		}

		hd.endElement("", "", Constants.XML_LISTA);
		hd.endDocument();
	}

}
