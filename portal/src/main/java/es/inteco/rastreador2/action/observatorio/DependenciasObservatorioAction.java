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
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import es.inteco.rastreador2.dao.ambito.AmbitoDAO;
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
	private static final String LITERAL_YES = Normalizer.normalize("Sí", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant LITERAL_NO. */
	private static final String LITERAL_NO = Normalizer.normalize("No", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant IMPORT_COLUMN_PROVINCE. */
	private static final String IMPORT_COLUMN_PROVINCE = Normalizer.normalize("Provincia", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant IMPORT_COLUMN_OFFICIAL. */
	private static final String IMPORT_COLUMN_OFFICIAL = Normalizer.normalize("Designacion oficial", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant IMPORT_COLUMN_EMAILS. */
	private static final String IMPORT_COLUMN_EMAILS = Normalizer.normalize("Correo Electrónico", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant IMPORT_COLUMN_ACRONYM. */
	private static final String IMPORT_COLUMN_ACRONYM = Normalizer.normalize("Acrónimo", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	/** The Constant IMPORT_COLUMN_NAME. */
	private static final String IMPORT_COLUMN_NAME = Normalizer.normalize("Nombre", Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

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
			DependenciaForm dependency = (DependenciaForm) form;
			if (!StringUtils.isEmpty(dependency.getName())) {
				dependency.setName(es.inteco.common.utils.StringUtils.corregirEncoding(dependency.getName()));
			}
			List<AmbitoForm> listAmbits = new ArrayList<>();
			if (!StringUtils.isEmpty(request.getParameter("ambitoaux"))) {
				String[] ambitArray = request.getParameter("ambitoaux").split(",");
				if (ambitArray != null && ambitArray.length >= 1) {
					for (int i = 0; i < ambitArray.length; i++) {
						AmbitoForm ambit = new AmbitoForm();
						ambit.setId(ambitArray[i]);
						listAmbits.add(ambit);
					}
					dependency.setAmbitos(listAmbits);
				}
			}
			String[] tagArr = null;
			if (!StringUtils.isEmpty(request.getParameter("tagaux"))) {
				tagArr = request.getParameter("tagaux").split(",");
			}
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = DependenciaDAO.countDependencias(c, dependency, tagArr);
			response.setContentType("text/json");
			List<DependenciaForm> listaDependencias = DependenciaDAO.getDependencias(c, dependency, tagArr, (pagina - 1));
			String jsonSeeds = new Gson().toJson(listaDependencias);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
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
		List<AmbitoForm> listAmbits = new ArrayList<>();
		if (!StringUtils.isEmpty(request.getParameter("ambitoaux"))) {
			String[] ambitArray = request.getParameter("ambitoaux").split(",");
			if (ambitArray != null && ambitArray.length >= 1) {
				for (int i = 0; i < ambitArray.length; i++) {
					AmbitoForm ambit = new AmbitoForm();
					ambit.setId(ambitArray[i]);
					listAmbits.add(ambit);
				}
				dependencia.setAmbitos(listAmbits);
			}
		}
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
		DependenciaForm dependencia = (DependenciaForm) form;
		if (StringUtils.isNotEmpty(dependencia.getName())) {
			List<AmbitoForm> listAmbits = new ArrayList<>();
			if (!StringUtils.isEmpty(request.getParameter("ambitoaux"))) {
				String[] ambitArray = request.getParameterValues("ambitoaux");
				if (ambitArray != null && ambitArray.length >= 1) {
					for (int i = 0; i < ambitArray.length; i++) {
						AmbitoForm ambit = new AmbitoForm();
						ambit.setId(ambitArray[i]);
						listAmbits.add(ambit);
					}
					dependencia.setAmbitos(listAmbits);
				}
			}
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
		// Process file
		List<DependencyComparision> comparisionList = processFile(dependencyForm.getDependencyFile());
		List<DependencyComparision> updatedDependencies = new ArrayList<>();
		List<DependenciaForm> newDependencies = new ArrayList<>();
		List<DependenciaForm> updatedAndNewDependencies = new ArrayList<>();
		List<DependenciaForm> inalterableDependencies = new ArrayList<>();
		List<DependencyError> errorDependencies = new ArrayList<>();
		for (DependencyComparision comparision : comparisionList) {
			if (comparision.isNew()) {
				newDependencies.add(comparision.getNewDependency());
			} else if (comparision.getDependency() != null && comparision.getDependency().getOfficial() != null && comparision.getDependency().getOfficial() && comparision.getNewDependency() != null
					&& comparision.getNewDependency().getOfficial() != null && !comparision.getNewDependency().getOfficial()) {
				List<String> errorsDependency = new ArrayList<>();
				MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
				errorsDependency.add(messageResources.getMessage("dependency.import.error.official.to.unofficial"));
				DependencyError dependencyError = new DependencyError();
				BeanUtils.copyProperties(dependencyError, comparision.getDependency());
				dependencyError.setErrors(errorsDependency);
				errorDependencies.add(dependencyError);
			} else if (!comparision.isInalterable()) {
				updatedDependencies.add(comparision);
			} else {
				inalterableDependencies.add(comparision.getDependency());
			}
			updatedAndNewDependencies.add(comparision.getNewDependency());
		}
		Connection c = DataBaseManager.getConnection();
		final List<DependenciaForm> findNotExistsAnNotAssociated = DependenciaDAO.findNotExistsAnNotAssociated(c, updatedAndNewDependencies);
		request.setAttribute("deletableDependencies", findNotExistsAnNotAssociated);
		request.setAttribute("undeletableDependencies", DependenciaDAO.findNotExistsAssociated(c, updatedAndNewDependencies));
		request.setAttribute("updatedDependencies", updatedDependencies);
		request.setAttribute("newDependencies", newDependencies);
		request.setAttribute("inalterableDependencies", inalterableDependencies);
		request.setAttribute("errorDependencies", errorDependencies);
		// Store list in session
		HttpSession session = request.getSession();
		session.setAttribute("updatedAndNewDependencies", updatedAndNewDependencies);
		session.setAttribute("deletableDependencies", findNotExistsAnNotAssociated);
		DataBaseManager.closeConnection(c);
		return mapping.findForward(Constants.CONFIRMACION_IMPORTAR);
	}

	/**
	 * Import all.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward importAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		try (Connection c = DataBaseManager.getConnection()) {
			DependenciaDAO.saveOrUpdate(c, (List<DependenciaForm>) session.getAttribute("updatedAndNewDependencies"));
			DependenciaDAO.delete(c, (List<DependenciaForm>) session.getAttribute("deletableDependencies"));
		} catch (Exception e) {
			Logger.putLog("Error", DependencyComparision.class, Logger.LOG_LEVEL_ERROR, e);
			return mapping.findForward("observatoryDependencias");
		}
		return mapping.findForward("observatoryDependencias");
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
				if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null) {
					String normalizedCellValue = Normalizer.normalize(sheet.getRow(0).getCell(0).getStringCellValue(), Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
					if (IMPORT_COLUMN_OFFICIAL.equalsIgnoreCase(normalizedCellValue)) {
						getDependenciesFromSheet(comparisionList, c, sheet);
					}
				}
			}
		}
		workbook.close();
		DataBaseManager.closeConnection(c);
		return comparisionList;
	}

	/**
	 * Gets the dependencies from sheet.
	 *
	 * @param comparisionList the comparision list
	 * @param c               the c
	 * @param sheet           the sheet
	 * @return the dependencies from sheet
	 * @throws Exception    the exception
	 * @throws SQLException the SQL exception
	 */
	private void getDependenciesFromSheet(final List<DependencyComparision> comparisionList, Connection c, XSSFSheet sheet) throws Exception, SQLException {
		ArrayList<String> headerData = getHeaders(sheet);
		for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
			Row r = sheet.getRow(j);
			DependenciaForm newDependency = new DependenciaForm();
			String name = headerData.indexOf(IMPORT_COLUMN_NAME) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_NAME))) : EMPTY_STRING;
			if (!StringUtils.isEmpty(name)) {
				String official = headerData.indexOf(IMPORT_COLUMN_OFFICIAL) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_OFFICIAL))) : EMPTY_STRING;
				if (!StringUtils.isEmpty(official)) {
					official = Normalizer.normalize(official, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
					if (LITERAL_NO.equalsIgnoreCase(official)) {
						newDependency.setOfficial(false);
					} else if (LITERAL_YES.equalsIgnoreCase(official)) {
						newDependency.setOfficial(true);
					}
				}
				newDependency.setName(name);
				String emails = headerData.indexOf(IMPORT_COLUMN_EMAILS) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_EMAILS))) : EMPTY_STRING;
				newDependency.setEmails(emails);
				String province = headerData.indexOf(IMPORT_COLUMN_PROVINCE) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_PROVINCE))) : EMPTY_STRING;
				if (!StringUtils.isEmpty(province)) {
					EtiquetaForm tag = EtiquetaDAO.getByName(c, province);
					if (tag != null) {
						newDependency.setTag(tag);
					} else {
						EtiquetaForm newTag = new EtiquetaForm();
						newTag.setName(province);
						newDependency.setTag(newTag);
					}
				}
				// Ambit by sheet name
				AmbitoForm ambitBySheetName = AmbitoDAO.getAmbitByName(c, sheet.getSheetName());
				List<AmbitoForm> ambits = new ArrayList<>();
				if (ambitBySheetName != null) {
					ambits.add(ambitBySheetName);
				} else {
					// By default set ambit others
					ambits.add(AmbitoDAO.getAmbitByID(c, "4"));
				}
				newDependency.setAmbitos(ambits);
				// Acronym
				String acronym = headerData.indexOf(IMPORT_COLUMN_ACRONYM) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_ACRONYM))) : EMPTY_STRING;
				newDependency.setAcronym(acronym);
				// By default all send auto
				newDependency.setSendAuto(true);
				// Add to comparision
				DependencyComparision comparision = new DependencyComparision();
				final DependenciaForm databaseDependency = DependenciaDAO.findByName(c, newDependency.getName());
				comparision.setDependency(databaseDependency);
				if (databaseDependency != null) {
					newDependency.setId(databaseDependency.getId());
				}
				comparision.setNewDependency(newDependency);
				comparisionList.add(comparision);
			}
		}
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
				headerData.add(Normalizer.normalize(cell.getStringCellValue(), Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
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
	 * The Class SeedError.
	 */
	public class DependencyError extends DependenciaForm {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 3659271546896392454L;
		/** The errors. */
		private List<String> errors = new ArrayList<>();

		/**
		 * Gets the errors.
		 *
		 * @return the error
		 */
		public List<String> getErrors() {
			return errors;
		}

		/**
		 * Sets the errors.
		 *
		 * @param errors the new errors
		 */
		public void setErrors(List<String> errors) {
			this.errors = errors;
		}
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
		private boolean sameProvince, sameEmails, sameOfficial, isNew, inInalterable, sameAcronym, sameAmbit;

		/**
		 * Checks if is new.
		 *
		 * @return true, if is new
		 */
		public boolean isNew() {
			return dependency == null;
		}

		/**
		 * In inalterable.
		 *
		 * @return true, if successful
		 */
		public boolean isInalterable() {
			return isSameEmails() && isSameOfficial() && isSameProvince() && isSameAcronym() && isSameAmbit();
		}

		/**
		 * Checks if is same province.
		 *
		 * @return true, if is same province
		 */
		public boolean isSameProvince() {
			// return (dependency!=null && newDependency!=null &&;
			return ((dependency != null && newDependency != null && dependency.getTag() == null && newDependency.getTag() == null)
					|| (dependency != null && newDependency != null && dependency.getTag() != null && newDependency.getTag() != null && dependency.getTag().equals(newDependency.getTag())));
		}

		/**
		 * Checks if is same ambit.
		 *
		 * @return true, if is same ambit
		 */
		public boolean isSameAmbit() {
			return (dependency != null && newDependency != null && dependency.getAmbitos() == null && newDependency.getAmbitos() == null
					&& new HashSet<>(dependency.getAmbitos()).equals(new HashSet<>(newDependency.getAmbitos())));
		}

		/**
		 * Checks if is same emails.
		 *
		 * @return true, if is same emails
		 */
		public boolean isSameEmails() {
			return (dependency != null && newDependency != null && StringUtils.equals(dependency.getEmails(), newDependency.getEmails()));
		}

		/**
		 * Checks if is same acronym.
		 *
		 * @return true, if is same acronym
		 */
		public boolean isSameAcronym() {
			return (dependency != null && newDependency != null && StringUtils.equals(dependency.getAcronym(), newDependency.getAcronym()));
		}

		/**
		 * Checks if is same official.
		 *
		 * @return true, if is same official
		 */
		public boolean isSameOfficial() {
			return (dependency != null && newDependency != null && dependency.getOfficial() == newDependency.getOfficial());
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
