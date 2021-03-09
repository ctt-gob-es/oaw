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
package es.inteco.rastreador2.action.observatorio;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.dependencia.DependenciaDAO;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class DependenciasObservatorioAction.
 */
public class DependenciasObservatorioAction extends DispatchAction {
	/** The Constant EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The Constant LITERAL_YES. */
	private static final String LITERAL_YES = "Sí";
	/** The Constant IMPORT_COLUMN_PROVINCE. */
	private static final String IMPORT_COLUMN_PROVINCE = "Provincia";
	/** The Constant IMPORT_COLUMN_OFFICIAL. */
	private static final String IMPORT_COLUMN_OFFICIAL = "Designacion oficial";
	/** The Constant IMPORT_COLUMN_EMAILS. */
	private static final String IMPORT_COLUMN_EMAILS = "Correo Electrónico";
	/** The Constant IMPORT_COLUMN_NAME. */
	private static final String IMPORT_COLUMN_NAME = "Nombre";

	/**
	 * Load. Carga de la página.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_DEPENDENCIAS);
		}
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Search. Devuelve un JSON con los resultados de la búsqueda
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			String nombre = request.getParameter("nombre");
			if (!StringUtils.isEmpty(nombre)) {
				nombre = es.inteco.common.utils.StringUtils.corregirEncoding(nombre);
			}
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = DependenciaDAO.countDependencias(c, nombre);
			response.setContentType("text/json");
			List<DependenciaForm> listaDependencias = DependenciaDAO.getDependencias(c, nombre, (pagina - 1));
			String jsonSeeds = new Gson().toJson(listaDependencias);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"dependencias\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		DependenciaForm dependencia = (DependenciaForm) form;
		ActionErrors errors = dependencia.validate(mapping, request);
		AmbitoForm ambitoSemilla = new AmbitoForm();
		ambitoSemilla.setId(request.getParameter("ambitoaux"));
		dependencia.setAmbito(ambitoSemilla);
		EtiquetaForm tag = new EtiquetaForm();
		if (!StringUtils.isEmpty(request.getParameter("tagaux"))) {
			tag.setId(Long.parseLong(request.getParameter("tagaux")));
			dependencia.setTag(tag);
		}
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.dependencia.obligatorio"));
		} else {
			try (Connection c = DataBaseManager.getConnection()) {
				if (DependenciaDAO.existsDependencia(c, dependencia) && !dependencia.getName().equalsIgnoreCase(dependencia.getNombreAntiguo())) {
					response.setStatus(400);
					response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.dependencia.duplicado"));
				} else {
					DependenciaDAO.update(c, dependencia);
					response.getWriter().write(messageResources.getMessage("mensaje.exito.dependencia.generada"));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		}
		return null;
	}

	/**
	 * Save.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		List<JsonMessage> errores = new ArrayList<>();
//		String nombre = request.getParameter("nombre");
		DependenciaForm dependencia = (DependenciaForm) form;
		if (StringUtils.isNotEmpty(dependencia.getName())) {
//			dependencia.setName(nombre);
			AmbitoForm ambitoSemilla = new AmbitoForm();
			ambitoSemilla.setId(request.getParameter("ambitoaux"));
			dependencia.setAmbito(ambitoSemilla);
			EtiquetaForm tag = new EtiquetaForm();
			if (!StringUtils.isEmpty(request.getParameter("tagaux"))) {
				tag.setId(Long.parseLong(request.getParameter("tagaux")));
				dependencia.setTag(tag);
			}
			try (Connection c = DataBaseManager.getConnection()) {
				if (DependenciaDAO.existsDependencia(c, dependencia)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.dependencia.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					DependenciaDAO.save(c, dependencia);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.dependencia.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		} else {
			response.setStatus(400);
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.dependencia.obligatorio")));
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * Delete.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		List<JsonMessage> errores = new ArrayList<>();
		String id = request.getParameter("idDependencia");
		if (id != null) {
			try (Connection c = DataBaseManager.getConnection()) {
				DependenciaDAO.delete(c, Long.parseLong(id));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.dependencia.eliminada")));
				response.getWriter().write(new Gson().toJson(errores));
			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		} else {
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
		}
		return null;
	}

	/**
	 * Upload.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		final PropertiesManager pmgr = new PropertiesManager();
		final DependenciaForm dependencyForm = (DependenciaForm) form;
		// Validate form
		ActionErrors errors = dependencyForm.validate(mapping, request);
		if (dependencyForm.getDependencyFile() == null || StringUtils.isEmpty(dependencyForm.getDependencyFile().getFileName())) {
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}
		// Validate format
		String filename = dependencyForm.getDependencyFile().getFileName();
		String[] splits = filename.split("\\.");
		String fileExtension = splits[splits.length - 1];
		if (!fileExtension.equals("xlsx")) {
			errors.add("xmlFile", new ActionMessage("no.xml.file"));
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}
		// validate file size
		if (dependencyForm.getDependencyFile().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
			errors.add("xmlFile", new ActionMessage("xml.size.error"));
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}
		// TODO Process file
		List<DependencyComparision> comparisionList = processFile(dependencyForm.getDependencyFile());
		List<DependencyComparision> updatedDependencies = new ArrayList<>();
		List<DependenciaForm> newDependencies = new ArrayList<>();
		for (DependencyComparision comparision : comparisionList) {
			if (comparision.isNew()) {
				newDependencies.add(comparision.getNewDependency());
			} else {
				updatedDependencies.add(comparision);
			}
		}
		request.setAttribute("updatedDependencies", updatedDependencies);
		request.setAttribute("newDependencies", newDependencies);
		return mapping.findForward(Constants.CONFIRMACION_IMPORTAR);
	}

	/**
	 * Process file.
	 *
	 * @param formFile the form file
	 * @return the list
	 * @throws Exception the exception
	 */
	private List<DependencyComparision> processFile(final FormFile formFile) throws Exception {
		final List<DependencyComparision> comparisionList = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(formFile.getInputStream());
		Connection c = DataBaseManager.getConnection();
		if (workbook.getNumberOfSheets() > 0) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null && IMPORT_COLUMN_OFFICIAL.equalsIgnoreCase(sheet.getRow(0).getCell(0).getStringCellValue())) {
					ArrayList<String> headerData = getHeaders(sheet);
					for (int j = 1; j < sheet.getLastRowNum(); j++) {
						Row r = sheet.getRow(j);
						DependenciaForm newDependency = new DependenciaForm();
						String name = headerData.indexOf(IMPORT_COLUMN_NAME) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_NAME))) : EMPTY_STRING;
						if (!StringUtils.isEmpty(name)) {
							newDependency.setName(name);
							String emails = headerData.indexOf(IMPORT_COLUMN_EMAILS) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_EMAILS))) : EMPTY_STRING;
							newDependency.setEmails(emails);
							String province = headerData.indexOf(IMPORT_COLUMN_PROVINCE) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_PROVINCE))) : EMPTY_STRING;
							if (!StringUtils.isEmpty(province)) {
								newDependency.setTag(EtiquetaDAO.getByName(c, province));
							}
							String official = headerData.indexOf(IMPORT_COLUMN_OFFICIAL) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_OFFICIAL))) : EMPTY_STRING;
							if (!StringUtils.isEmpty(official)) {
								newDependency.setOfficial(false);
								if (LITERAL_YES.equalsIgnoreCase(official)) {
									newDependency.setOfficial(true);
								}
							}
							// Add to comparision
							DependencyComparision comparision = new DependencyComparision();
							comparision.setDependency(DependenciaDAO.findByName(c, newDependency.getName()));
							comparision.setNewDependency(newDependency);
							comparisionList.add(comparision);
						}
					}
				}
			}
		}
		workbook.close();
		DataBaseManager.closeConnection(c);
		return comparisionList;
	}

	/**
	 * Gets the cell value.
	 *
	 * @param cell the cell
	 * @return the cell value
	 */
	private String getCellValue(final Cell cell) {
		String value = EMPTY_STRING;
		if (cell != null) {
			return cell.getStringCellValue();
		}
		return value;
	}

	/**
	 * Gets the headers.
	 *
	 * @param sheet the sheet
	 * @return the headers
	 */
	private ArrayList<String> getHeaders(final XSSFSheet sheet) {
		// Get the first row from the sheet
		Row row = sheet.getRow(0);
		// Create a List to store the header data
		ArrayList<String> headerData = new ArrayList<>();
		// Iterate cells of the row and add data to the List
		for (Cell cell : row) {
			switch (cell.getCellType()) {
			case NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					DataFormatter dataFormatter = new DataFormatter();
					headerData.add(dataFormatter.formatCellValue(cell));
				} else {
					headerData.add(String.valueOf(cell.getNumericCellValue()));
				}
				break;
			case STRING:
				headerData.add(cell.getStringCellValue());
				break;
			case BOOLEAN:
				headerData.add(String.valueOf(cell.getBooleanCellValue()));
				break;
			default:
				headerData.add(EMPTY_STRING);
				break;
			}
		}
		return headerData;
	}

	/**
	 * The Class ResultObject.
	 */
	public class DependencyComparision implements Serializable {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -3283554613152882117L;
		/** The dependency. */
		private DependenciaForm dependency;
		/** The new dependency. */
		private DependenciaForm newDependency;
		/** The same official. */
		private boolean sameProvince, sameEmails, sameOfficial, isNew;

		/**
		 * Checks if is new.
		 *
		 * @return true, if is new
		 */
		public boolean isNew() {
			return dependency == null;
		}

		/**
		 * Checks if is same province.
		 *
		 * @return true, if is same province
		 */
		public boolean isSameProvince() {
			// return (dependency!=null && newDependency!=null &&;
			return (dependency != null && newDependency != null && dependency.getTag() != null && newDependency.getTag() != null && dependency.getTag().equals(newDependency.getTag()));
		}

		/**
		 * Checks if is same emails.
		 *
		 * @return true, if is same emails
		 */
		public boolean isSameEmails() {
			return (dependency != null && newDependency != null && dependency.getEmails().equals(newDependency.getEmails()));
		}

		/**
		 * Checks if is same official.
		 *
		 * @return true, if is same official
		 */
		public boolean isSameOfficial() {
			return (dependency != null && newDependency != null && dependency.isOfficial() == newDependency.isOfficial());
		}

		/**
		 * Sets the new.
		 *
		 * @param isNew the new new
		 */
		public void setNew(boolean isNew) {
			this.isNew = isNew;
		}

		/**
		 * Sets the same province.
		 *
		 * @param sameProvince the new same province
		 */
		public void setSameProvince(boolean sameProvince) {
			this.sameProvince = sameProvince;
		}

		/**
		 * Sets the same emails.
		 *
		 * @param sameEmails the new same emails
		 */
		public void setSameEmails(boolean sameEmails) {
			this.sameEmails = sameEmails;
		}

		/**
		 * Sets the same official.
		 *
		 * @param sameOfficial the new same official
		 */
		public void setSameOfficial(boolean sameOfficial) {
			this.sameOfficial = sameOfficial;
		}

		/**
		 * Gets the dependency.
		 *
		 * @return the dependency
		 */
		public DependenciaForm getDependency() {
			return dependency;
		}

		/**
		 * Sets the dependency.
		 *
		 * @param dependency the new dependency
		 */
		public void setDependency(DependenciaForm dependency) {
			this.dependency = dependency;
		}

		/**
		 * Gets the new dependency.
		 *
		 * @return the new dependency
		 */
		public DependenciaForm getNewDependency() {
			return newDependency;
		}

		/**
		 * Sets the new dependency.
		 *
		 * @param newDependency the new new dependency
		 */
		public void setNewDependency(DependenciaForm newDependency) {
			this.newDependency = newDependency;
		}
	}
}
