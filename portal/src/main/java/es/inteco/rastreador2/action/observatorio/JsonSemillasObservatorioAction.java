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

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * Action para el grid de semillas.
 *
 * @author alvaro.pelaez
 */
public class JsonSemillasObservatorioAction extends DispatchAction {
	/** The message resources. */
	private MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

	/**
	 * Buscar.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward buscar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaSearchForm searchForm = (SemillaSearchForm) form;
			if (searchForm != null) {
				searchForm = new SemillaSearchForm();
				searchForm.setNombre(request.getParameter("nombre"));
				if (!StringUtils.isEmpty(searchForm.getNombre())) {
					searchForm.setNombre(es.inteco.common.utils.StringUtils.corregirEncoding(searchForm.getNombre()));
				}
				if (!StringUtils.isEmpty(request.getParameter("categoria"))) {
					searchForm.setCategoria((request.getParameterValues("categoria")));
				}
				if (!StringUtils.isEmpty(request.getParameter("ambito"))) {
					searchForm.setAmbito((request.getParameterValues("ambito")));
				}
				if (!StringUtils.isEmpty(request.getParameter("dependencia"))) {
					searchForm.setDependencia((request.getParameterValues("dependencia")));
				}
				if (!StringUtils.isEmpty(request.getParameter("complejidad"))) {
					searchForm.setComplejidad((request.getParameterValues("complejidad")));
				}
				if (!StringUtils.isEmpty(request.getParameter("url"))) {
					searchForm.setUrl(request.getParameter("url"));
				}
				if (!StringUtils.isEmpty(request.getParameter("directorio"))) {
					searchForm.setinDirectorio(request.getParameter("directorio"));
				}
				if (!StringUtils.isEmpty(request.getParameter("activa"))) {
					searchForm.setisActiva(request.getParameter("activa"));
				}
				if (!StringUtils.isEmpty(request.getParameter("eliminada"))) {
					searchForm.setEliminada(request.getParameter("eliminada"));
				}
				if (!StringUtils.isEmpty(request.getParameter("etiquetas"))) {
					String[] tagArr = request.getParameter("etiquetas").split(",");
					searchForm.setEtiquetas(tagArr);
				}
			}
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = SemillaDAO.countObservatorySeeds(c, searchForm);
			response.setContentType("text/json");
			List<SemillaForm> observatorySeedsList = SemillaDAO.getObservatorySeeds(c, (pagina - 1), searchForm);
			String jsonSeeds = new Gson().toJson(observatorySeedsList);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"semillas\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Actualiza la semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SemillaForm semilla = (SemillaForm) form;
		List<JsonMessage> errores = new ArrayList<>();
		ActionErrors errors = semilla.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			if (errors.get("nombre").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.nombre.requerido")));
			}
			if (errors.get("listaUrlsString").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.requerido")));
			}
		}
		if (!errores.isEmpty()) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			// Soporte a múltiples dependencias
			List<DependenciaForm> listaDependencias = new ArrayList<>();
			// Si viene de la edición en el grid, el parámetro viene como
			// valores separados por comas, si viene de la edición en los
			// reslutados de observatorio viene el parametro tantas veces como
			// valores tenga
			String[] arrayDependendencias = request.getParameterValues("ncias = request.getParameterValues(\"dependenciasSeleccionadas\");\n"
					+ "			if (arrayDependendencias != null && arrayDependendencias.length > 1) {\n" + "				for (int i = 0; i < arrayDependendencias.length; i++) {\n"
					+ "					DependenciaForm dependencia = new DependenciaForm();\n" + "					dependencia.setId(Long.parseLong(arrayDependendencias[i]));\n"
					+ "					listaDependencias.add(dependencia);\n" + "				}\n" + "			}\n" + "			\n" + "			\n"
					+ "			EtiquetaForm tag = new EtiquetaForm();\n" + "			if (!StringUtils.isEmpty(request.getParameter(\"tagaux\"))) {\n"
					+ "				tag.setId(Long.parseLong(request.getParameter(\"tagaux\")));\n" + "				dependencia.setTag(tag);\n" + "			}\n"
					+ "			try (Connection c = DataBaseManager.getConnection()) {\n" + "				if (DependenciaDAO.existsDependencia(c, dependencia)) {\n"
					+ "					response.setStatus(400);\n" + "					errores.add(new JsonMessage(messageResources.getMessage(\"mensaje.error.nombre.dependencia.duplicado\")));\n"
					+ "					response.getWriter().write(new Gson().toJson(errores));\n" + "				} else {\n" + "					DependenciaDAO.save(c, dependencia);\n"
					+ "					errores.add(new JsonMessage(messageResources.getMessage(\"mensaje.exito.dependencia.generada\")));\n"
					+ "					response.getWriter().write(new Gson().toJson(errores));\n" + "				}\n" + "			} catch (Exception e) {\n"
					+ "				Logger.putLog(\"Error: \", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);\n" + "				response.setStatus(400);\n"
					+ "				response.getWriter().write(messageResources.getMessage(\"mensaje.error.generico\"));\n" + "			}\n" + "		} else {\n"
					+ "			response.setStatus(400);\n" + "			errores.add(new JsonMessage(messageResources.getMessage(\"mensaje.error.nombre.dependencia.obligatorio\")));\n"
					+ "			response.getWriter().write(new Gson().toJson(errores));\n" + "		}\n" + "		return null;\n" + "	}\n" + "\n" + "	/**\n" + "	 * Delete.\n" + "	 *\n"
					+ "	 * @param mapping  the mapping\n" + "	 * @param form     the form\n" + "	 * @param request  the request\n" + "	 * @param response the response\n"
					+ "	 * @return the action forward\n" + "	 * @throws Exception the exception\n" + "	 */\n"
					+ "	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {\n"
					+ "		MessageResources messageResources = MessageResources.getMessageResources(\"ApplicationResources\");\n" + "		List<JsonMessage> errores = new ArrayList<>();\n"
					+ "		String id = request.getParameter(\"idDependencia\");\n" + "		if (id != null) {\n" + "			try (Connection c = DataBaseManager.getConnection()) {\n"
					+ "				DependenciaDAO.delete(c, Long.parseLong(id));\n"
					+ "				errores.add(new JsonMessage(messageResources.getMessage(\"mensaje.exito.dependencia.eliminada\")));\n"
					+ "				response.getWriter().write(new Gson().toJson(errores));\n" + "			} catch (Exception e) {\n"
					+ "				Logger.putLog(\"Error: \", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);\n" + "				response.setStatus(400);\n"
					+ "				response.getWriter().write(messageResources.getMessage(\"mensaje.error.generico\"));\n" + "			}\n" + "		} else {\n"
					+ "			response.setStatus(400);\n" + "			response.getWriter().write(messageResources.getMessage(\"mensaje.error.generico\"));\n" + "		}\n" + "		return null;\n"
					+ "	}\n" + "\n" + "	/**\n" + "	 * Upload.\n" + "	 *\n" + "	 * @param mapping  the mapping\n" + "	 * @param form     the form\n" + "	 * @param request  the request\n"
					+ "	 * @param response the response\n" + "	 * @return the action forward\n" + "	 * @throws Exception the exception\n" + "	 */\n"
					+ "	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {\n"
					+ "		final PropertiesManager pmgr = new PropertiesManager();\n" + "		final DependenciaForm dependencyForm = (DependenciaForm) form;\n" + "		// Validate form\n"
					+ "		ActionErrors errors = dependencyForm.validate(mapping, request);\n"
					+ "		if (dependencyForm.getDependencyFile() == null || StringUtils.isEmpty(dependencyForm.getDependencyFile().getFileName())) {\n" + "			saveErrors(request, errors);\n"
					+ "			return mapping.findForward(Constants.VOLVER);\n" + "		}\n" + "		// Validate format\n"
					+ "		String filename = dependencyForm.getDependencyFile().getFileName();\n" + "		String[] splits = filename.split(\"\\\\.\");\n"
					+ "		String fileExtension = splits[splits.length - 1];\n" + "		if (!fileExtension.equals(\"xlsx\")) {\n"
					+ "			errors.add(\"xmlFile\", new ActionMessage(\"no.xml.file\"));\n" + "			saveErrors(request, errors);\n" + "			return mapping.findForward(Constants.VOLVER);\n"
					+ "		}\n" + "		// validate file size\n"
					+ "		if (dependencyForm.getDependencyFile().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, \"xml.file.max.size\"))) {\n"
					+ "			errors.add(\"xmlFile\", new ActionMessage(\"xml.size.error\"));\n" + "			saveErrors(request, errors);\n"
					+ "			return mapping.findForward(Constants.VOLVER);\n" + "		}\n" + "		// Process file\n"
					+ "		List<DependencyComparision> comparisionList = processFile(dependencyForm.getDependencyFile());\n"
					+ "		List<DependencyComparision> updatedDependencies = new ArrayList<>();\n" + "		List<DependenciaForm> newDependencies = new ArrayList<>();\n"
					+ "		List<DependenciaForm> updatedAndNewDependencies = new ArrayList<>();\n" + "		List<DependenciaForm> inalterableDependencies = new ArrayList<>();\n"
					+ "		List<DependencyError> errorDependencies = new ArrayList<>();\n" + "		for (DependencyComparision comparision : comparisionList) {\n"
					+ "			if (comparision.isNew()) {\n" + "				newDependencies.add(comparision.getNewDependency());\n"
					+ "			} else if (comparision.getDependency() != null && comparision.getDependency().getOfficial() != null && comparision.getDependency().getOfficial() && comparision.getNewDependency() != null\n"
					+ "					&& comparision.getNewDependency().getOfficial() != null && !comparision.getNewDependency().getOfficial()) {\n"
					+ "				List<String> errorsDependency = new ArrayList<>();\n"
					+ "				MessageResources messageResources = MessageResources.getMessageResources(\"ApplicationResources\");\n"
					+ "				errorsDependency.add(messageResources.getMessage(\"dependency.import.error.official.to.unofficial\"));\n"
					+ "				DependencyError dependencyError = new DependencyError();\n" + "				BeanUtils.copyProperties(dependencyError, comparision.getDependency());\n"
					+ "				dependencyError.setErrors(errorsDependency);\n" + "				errorDependencies.add(dependencyError);\n"
					+ "			} else if (!comparision.isInalterable()) {\n" + "				updatedDependencies.add(comparision);\n" + "			} else {\n"
					+ "				inalterableDependencies.add(comparision.getDependency());\n" + "			}\n" + "			updatedAndNewDependencies.add(comparision.getNewDependency());\n"
					+ "		}\n" + "		Connection c = DataBaseManager.getConnection();\n"
					+ "		final List<DependenciaForm> findNotExistsAnNotAssociated = DependenciaDAO.findNotExistsAnNotAssociated(c, updatedAndNewDependencies);\n"
					+ "		request.setAttribute(\"deletableDependencies\", findNotExistsAnNotAssociated);\n"
					+ "		request.setAttribute(\"undeletableDependencies\", DependenciaDAO.findNotExistsAssociated(c, updatedAndNewDependencies));\n"
					+ "		request.setAttribute(\"updatedDependencies\", updatedDependencies);\n" + "		request.setAttribute(\"newDependencies\", newDependencies);\n"
					+ "		request.setAttribute(\"inalterableDependencies\", inalterableDependencies);\n" + "		request.setAttribute(\"errorDependencies\", errorDependencies);\n"
					+ "		// Store list in session\n" + "		HttpSession session = request.getSession();\n"
					+ "		session.setAttribute(\"updatedAndNewDependencies\", updatedAndNewDependencies);\n"
					+ "		session.setAttribute(\"deletableDependencies\", findNotExistsAnNotAssociated);\n" + "		DataBaseManager.closeConnection(c);\n"
					+ "		return mapping.findForward(Constants.CONFIRMACION_IMPORTAR);\n" + "	}\n" + "\n" + "	/**\n" + "	 * Import all.\n" + "	 *\n" + "	 * @param mapping  the mapping\n"
					+ "	 * @param form     the form\n" + "	 * @param request  the request\n" + "	 * @param response the response\n" + "	 * @return the action forward\n"
					+ "	 * @throws Exception the exception\n" + "	 */\n"
					+ "	public ActionForward importAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {\n"
					+ "		HttpSession session = request.getSession();\n" + "		try (Connection c = DataBaseManager.getConnection()) {\n"
					+ "			DependenciaDAO.saveOrUpdate(c, (List<DependenciaForm>) session.getAttribute(\"updatedAndNewDependencies\"));\n"
					+ "			DependenciaDAO.delete(c, (List<DependenciaForm>) session.getAttribute(\"deletableDependencies\"));\n" + "		} catch (Exception e) {\n"
					+ "			Logger.putLog(\"Error\", DependencyComparision.class, Logger.LOG_LEVEL_ERROR, e);\n" + "			return mapping.findForward(\"observatoryDependencias\");\n"
					+ "		}\n" + "		return mapping.findForward(\"observatoryDependencias\");\n" + "	}\n" + "\n" + "	/**\n" + "	 * Process file.\n" + "	 *\n"
					+ "	 * @param formFile the form file\n" + "	 * @return the list\n" + "	 * @throws Exception the exception\n" + "	 */\n"
					+ "	private List<DependencyComparision> processFile(final FormFile formFile) throws Exception {\n"
					+ "		final List<DependencyComparision> comparisionList = new ArrayList<>();\n" + "		XSSFWorkbook workbook = new XSSFWorkbook(formFile.getInputStream());\n"
					+ "		Connection c = DataBaseManager.getConnection();\n" + "		if (workbook.getNumberOfSheets() > 0) {\n"
					+ "			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {\n" + "				XSSFSheet sheet = workbook.getSheetAt(i);\n"
					+ "				if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null) {\n"
					+ "					String normalizedCellValue = Normalizer.normalize(sheet.getRow(0).getCell(0).getStringCellValue(), Form.NFD).replaceAll(\"\\\\p{InCombiningDiacriticalMarks}+\", \"\");\n"
					+ "					if (IMPORT_COLUMN_OFFICIAL.equalsIgnoreCase(normalizedCellValue)) {\n"
					+ "						// TODO Determine if is official or not by number of columns??\n" + "						getDependenciesFromSheet(comparisionList, c, sheet);\n"
					+ "					}\n" + "				}\n" + "			}\n" + "		}\n" + "		workbook.close();\n" + "		DataBaseManager.closeConnection(c);\n"
					+ "		return comparisionList;\n" + "	}\n" + "\n" + "	/**\n" + "	 * Gets the dependencies from sheet.\n" + "	 *\n" + "	 * @param comparisionList the comparision list\n"
					+ "	 * @param c               the c\n" + "	 * @param sheet           the sheet\n" + "	 * @return the dependencies from sheet\n" + "	 * @throws Exception    the exception\n"
					+ "	 * @throws SQLException the SQL exception\n" + "	 */\n"
					+ "	private void getDependenciesFromSheet(final List<DependencyComparision> comparisionList, Connection c, XSSFSheet sheet) throws Exception, SQLException {\n"
					+ "		ArrayList<String> headerData = getHeaders(sheet);\n" + "		for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {\n" + "			Row r = sheet.getRow(j);\n"
					+ "			DependenciaForm newDependency = new DependenciaForm();\n"
					+ "			String name = headerData.indexOf(IMPORT_COLUMN_NAME) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_NAME))) : EMPTY_STRING;\n"
					+ "			if (!StringUtils.isEmpty(name)) {\n"
					+ "				String official = headerData.indexOf(IMPORT_COLUMN_OFFICIAL) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_OFFICIAL))) : EMPTY_STRING;\n"
					+ "				if (!StringUtils.isEmpty(official)) {\n"
					+ "					official = Normalizer.normalize(official, Form.NFD).replaceAll(\"\\\\p{InCombiningDiacriticalMarks}+\", \"\");\n"
					+ "					if (LITERAL_NO.equalsIgnoreCase(official)) {\n" + "						newDependency.setOfficial(false);\n"
					+ "					} else if (LITERAL_YES.equalsIgnoreCase(official)) {\n" + "						newDependency.setOfficial(true);\n" + "					}\n"
					+ "				}\n" + "				newDependency.setName(name);\n"
					+ "				String emails = headerData.indexOf(IMPORT_COLUMN_EMAILS) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_EMAILS))) : EMPTY_STRING;\n"
					+ "				newDependency.setEmails(emails);\n"
					+ "				String province = headerData.indexOf(IMPORT_COLUMN_PROVINCE) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_PROVINCE))) : EMPTY_STRING;\n"
					+ "				if (!StringUtils.isEmpty(province)) {\n" + "					EtiquetaForm tag = EtiquetaDAO.getByName(c, province);\n"
					+ "					if (tag != null) {\n" + "						newDependency.setTag(tag);\n" + "					} else {\n"
					+ "						EtiquetaForm newTag = new EtiquetaForm();\n" + "						newTag.setName(province);\n"
					+ "						newDependency.setTag(newTag);\n" + "					}\n" + "				}\n" + "				// Ambit by sheet name\n"
					+ "				// TODO LIST OF AMBITS\n" + "//				AmbitoForm ambitBySheetName = AmbitoDAO.getAmbitByName(c, sheet.getSheetName());\n"
					+ "//				if (ambitBySheetName != null) {\n" + "//					newDependency.setAmbito(ambitBySheetName);\n" + "//				} else {\n"
					+ "//					// By default set ambit others\n" + "//					newDependency.setAmbito(AmbitoDAO.getAmbitByID(c, \"4\"));\n" + "//				}\n"
					+ "				// Acronym\n"
					+ "				String acronym = headerData.indexOf(IMPORT_COLUMN_ACRONYM) >= 0 ? getCellValue(r.getCell(headerData.indexOf(IMPORT_COLUMN_ACRONYM))) : EMPTY_STRING;\n"
					+ "				newDependency.setAcronym(acronym);\n" + "				// By default all send auto\n" + "				newDependency.setSendAuto(true);\n"
					+ "				// Add to comparision\n" + "				DependencyComparision comparision = new DependencyComparision();\n"
					+ "				final DependenciaForm databaseDependency = DependenciaDAO.findByName(c, newDependency.getName());\n"
					+ "				comparision.setDependency(databaseDependency);\n" + "				if (databaseDependency != null) {\n"
					+ "					newDependency.setId(databaseDependency.getId());\n" + "				}\n" + "				comparision.setNewDependency(newDependency);\n"
					+ "				comparisionList.add(comparision);\n" + "			}\n" + "		}\n" + "	}\n" + "\n" + "	/**\n" + "	 * Gets the cell value.\n" + "	 *\n"
					+ "	 * @param cell the cell\n" + "	 * @return the cell value\n" + "	 */\n" + "	private String getCellValue(final Cell cell) {\n" + "		String value = EMPTY_STRING;\n"
					+ "		if (cell != null) {\n" + "			return cell.getStringCellValue();\n" + "		}\n" + "		return value;\n" + "	}\n" + "\n" + "	/**\n"
					+ "	 * Gets the headers.\n" + "	 *\n" + "	 * @param sheet the sheet\n" + "	 * @return the headers\n" + "	 */\n"
					+ "	private ArrayList<String> getHeaders(final XSSFSheet sheet) {\n" + "		// Get the first row from the sheet\n" + "		Row row = sheet.getRow(0);\n"
					+ "		// Create a List to store the header data\n" + "		ArrayList<String> headerData = new ArrayList<>();\n"
					+ "		// Iterate cells of the row and add data to the List\n" + "		for (Cell cell : row) {\n" + "			switch (cell.getCellType()) {\n" + "			case NUMERIC:\n"
					+ "				if (HSSFDateUtil.isCellDateFormatted(cell)) {\n" + "					DataFormatter dataFormatter = new DataFormatter();\n"
					+ "					headerData.add(dataFormatter.formatCellValue(cell));\n" + "				} else {\n"
					+ "					headerData.add(String.valueOf(cell.getNumericCellValue()));\n" + "				}\n" + "				break;\n" + "			case STRING:\n"
					+ "				headerData.add(Normalizer.normalize(cell.getStringCellValue(), Form.NFD).replaceAll(\"\\\\p{InCombiningDiacriticalMarks}+\", \"\"));\n" + "				break;\n"
					+ "			case BOOLEAN:\n" + "				headerData.add(String.valueOf(cell.getBooleanCellValue()));\n" + "				break;\n" + "			default:\n"
					+ "				headerData.add(EMPTY_STRING);\n" + "				break;\n" + "			}\n" + "		}\n" + "		return headerData;\n" + "	}\n" + "\n" + "	/**\n"
					+ "	 * The Class SeedError.\n" + "	 */\n" + "	public class DependencyError extends DependenciaForm {\n" + "		/** The Constant serialVersionUID. */\n"
					+ "		private static final long serialVersionUID = 3659271546896392454L;\n" + "		/** The errors. */\n" + "		private List<String> errors = new ArrayList<>();\n" + "\n"
					+ "		/**\n" + "		 * Gets the errors.\n" + "		 *\n" + "		 * @return the error\n" + "		 */\n" + "		public List<String> getErrors() {\n"
					+ "			return errors;\n" + "		}\n" + "\n" + "		/**\n" + "		 * Sets the errors.\n" + "		 *\n" + "		 * @param errors the new errors\n" + "		 */\n"
					+ "		public void setErrors(List<String> errors) {\n" + "			this.errors = errors;\n" + "		}\n" + "	}\n" + "\n" + "	/**\n" + "	 * The Class ResultObject.\n"
					+ "	 */\n" + "	public class DependencyComparision implements Serializable {\n" + "		/** The Constant serialVersionUID. */\n"
					+ "		private static final long serialVersionUID = -3283554613152882117L;\n" + "		/** The dependency. */\n" + "		private DependenciaForm dependency;\n"
					+ "		/** The new dependency. */\n" + "		private DependenciaForm newDependency;\n" + "		/** The same official. */\n"
					+ "		private boolean sameProvince, sameEmails, sameOfficial, isNew, inInalterable, sameAcronym, sameAmbit;\n" + "\n" + "		/**\n" + "		 * Checks if is new.\n"
					+ "		 *\n" + "		 * @return true, if is new\n" + "		 */\n" + "		public boolean isNew() {\n" + "			return dependency == null;\n" + "		}\n" + "\n"
					+ "		/**\n" + "		 * In inalterable.\n" + "		 *\n" + "		 * @return true, if successful\n" + "		 */\n" + "		public boolean isInalterable() {\n"
					+ "			return isSameEmails() && isSameOfficial() && isSameProvince() && isSameAcronym() && isSameAmbit();\n" + "		}\n" + "\n" + "		/**\n"
					+ "		 * Checks if is same province.\n" + "		 *\n" + "		 * @return true, if is same province\n" + "		 */\n" + "		public boolean isSameProvince() {\n"
					+ "			// return (dependency!=null && newDependency!=null &&;\n"
					+ "			return ((dependency != null && newDependency != null && dependency.getTag() == null && newDependency.getTag() == null)\n"
					+ "					|| (dependency != null && newDependency != null && dependency.getTag() != null && newDependency.getTag() != null && dependency.getTag().equals(newDependency.getTag())));\n"
					+ "		}\n" + "\n" + "		/**\n" + "		 * Checks if is same ambit.\n" + "		 *\n" + "		 * @return true, if is same ambit\n" + "		 */\n"
					+ "		public boolean isSameAmbit() {\n" + "			// return (dependency!=null && newDependency!=null &&;\n" + "			// TODO LIST OF AMBITS\n"
					+ "//			return ((dependency != null && newDependency != null && dependency.getAmbito() == null && newDependency.getAmbito() == null) || (dependency != null && newDependency != null\n"
					+ "//					&& dependency.getAmbito() != null && newDependency.getAmbito() != null && dependency.getAmbito().equals(newDependency.getAmbito())));\n"
					+ "			return (true);\n" + "		}\n" + "\n" + "		/**\n" + "		 * Checks if is same emails.\n" + "		 *\n" + "		 * @return true, if is same emails\n"
					+ "		 */\n" + "		public boolean isSameEmails() {\n"
					+ "			return (dependency != null && newDependency != null && StringUtils.equals(dependency.getEmails(), newDependency.getEmails()));\n" + "		}\n" + "\n" + "		/**\n"
					+ "		 * Checks if is same acronym.\n" + "		 *\n" + "		 * @return true, if is same acronym\n" + "		 */\n" + "		public boolean isSameAcronym() {\n"
					+ "			return (dependency != null && newDependency != null && StringUtils.equals(dependency.getAcronym(), newDependency.getAcronym()));\n" + "		}\n" + "\n" + "		/**\n"
					+ "		 * Checks if is same official.\n" + "		 *\n" + "		 * @return true, if is same official\n" + "		 */\n" + "		public boolean isSameOfficial() {\n"
					+ "			return (dependency != null && newDependency != null && dependency.getOfficial() == newDependency.getOfficial());\n" + "		}\n" + "\n" + "		/**\n"
					+ "		 * Sets the new.\n" + "		 *\n" + "		 * @param isNew the new new\n" + "		 */\n" + "		public void setNew(boolean isNew) {\n"
					+ "			this.isNew = isNew;\n" + "		}\n" + "\n" + "		/**\n" + "		 * Sets the same province.\n" + "		 *\n"
					+ "		 * @param sameProvince the new same province\n" + "		 */\n" + "		public void setSameProvince(boolean sameProvince) {\n"
					+ "			this.sameProvince = sameProvince;\n" + "		}\n" + "\n" + "		/**\n" + "		 * Sets the same emails.\n" + "		 *\n"
					+ "		 * @param sameEmails the new same emails\n" + "		 */\n" + "		public void setSameEmails(boolean sameEmails) {\n" + "			this.sameEmails = sameEmails;\n"
					+ "		}\n" + "\n" + "		/**\n" + "		 * Sets the same official.\n" + "		 *\n" + "		 * @param sameOfficial the new same official\n" + "		 */\n"
					+ "		public void setSameOfficial(boolean sameOfficial) {\n" + "			this.sameOfficial = sameOfficial;\n" + "		}\n" + "\n" + "		/**\n"
					+ "		 * Gets the dependency.\n" + "		 *\n" + "		 * @return the dependency\n" + "		 */");
			if (arrayDependendencias != null && arrayDependendencias.length > 1) {
				for (int i = 0; i < arrayDependendencias.length; i++) {
					DependenciaForm dependencia = new DependenciaForm();
					dependencia.setId(Long.parseLong(arrayDependendencias[i]));
					listaDependencias.add(dependencia);
				}
			} else {
				// Solo un parámetro que intentaremos separar por comas, si no
				// las tiene devolverá un único valor
				String dependencias = request.getParameter("dependenciasSeleccionadas");
				if (!StringUtils.isEmpty(dependencias)) {
					String[] idsDependencias = dependencias.split(",");
					for (int i = 0; i < idsDependencias.length; i++) {
						DependenciaForm dependencia = new DependenciaForm();
						dependencia.setId(Long.parseLong(idsDependencias[i]));
						listaDependencias.add(dependencia);
					}
				}
			}
			semilla.setDependencias(listaDependencias);
			// Soporte a múltiples etiquetas
			List<EtiquetaForm> listaEtiquetas = new ArrayList<>();
			// Si viene de la edición en el grid, el parámetro viene como
			// valores separados por comas, si viene de la edición en los
			// reslutados de observatorio viene el parametro tantas veces como
			// valores tenga
			String[] arrayEtiquetas = request.getParameterValues("etiquetasSeleccionadas");
			if (arrayEtiquetas != null && arrayEtiquetas.length > 1) {
				for (int i = 0; i < arrayEtiquetas.length; i++) {
					EtiquetaForm etiqueta = new EtiquetaForm();
					etiqueta.setId(Long.parseLong(arrayEtiquetas[i]));
					listaEtiquetas.add(etiqueta);
				}
			} else {
				// Solo un parámetro que intentaremos separar por comas, si no
				// las tiene devolverá un único valor
				String etiquetas = request.getParameter("etiquetasSeleccionadas");
				if (!StringUtils.isEmpty(etiquetas)) {
					String[] idsEtiquetas = etiquetas.split(",");
					for (int i = 0; i < idsEtiquetas.length; i++) {
						EtiquetaForm etiqueta = new EtiquetaForm();
						etiqueta.setId(Long.parseLong(idsEtiquetas[i]));
						listaEtiquetas.add(etiqueta);
					}
				}
			}
			semilla.setEtiquetas(listaEtiquetas);
			if (!StringUtils.isEmpty(semilla.getListaUrlsString())) {
				semilla.setListaUrlsString(this.normalizarUrl(semilla.getListaUrlsString()));
				semilla.setListaUrls(Arrays.asList(semilla.getListaUrlsString().split(";")));
				// Comprobar que sólo se introduzcen el max.url
				String idComplejidad = request.getParameter("complejidadaux");
				ComplejidadForm complexAux = ComplejidadDAO.getComplexityById(DataBaseManager.getConnection(), idComplejidad);
				int maxUrls = complexAux.getAmplitud() * complexAux.getProfundidad() + 1;
//				PropertiesManager pmgr = new PropertiesManager();
//				int maxUrls = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				if (semilla.getListaUrls() != null && semilla.getListaUrls().size() > maxUrls) {
					errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.max.superado", new String[] { String.valueOf(maxUrls) })));
					response.setStatus(400);
					response.getWriter().write(new Gson().toJson(errores));
					return null;
				}
			}
			// Comprobar duplicados
			Set<String> testDuplicates = this.findDuplicates(semilla.getListaUrls());
			if (!testDuplicates.isEmpty()) {
				String duplicadosStr = "";
				for (String duplicado : testDuplicates) {
					duplicadosStr += duplicado + "\n";
				}
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.duplicados") + duplicadosStr));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
				return null;
			}
			CategoriaForm categoriaSemilla = new CategoriaForm();
			categoriaSemilla.setId(request.getParameter("segmento"));
			semilla.setCategoria(categoriaSemilla);
			AmbitoForm ambitoSemilla = new AmbitoForm();
			ambitoSemilla.setId(request.getParameter("ambitoaux"));
			semilla.setAmbito(ambitoSemilla);
			ComplejidadForm complejidadSemilla = new ComplejidadForm();
			complejidadSemilla.setId(request.getParameter("complejidadaux"));
			semilla.setComplejidad(complejidadSemilla);
			MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
			try (Connection c = DataBaseManager.getConnection()) {
				// Comprobar que no existe una semilla con el mismo nombre
				boolean existSeed = SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				if (existSeed && !semilla.getNombre().equals(request.getParameter(Constants.NOMBRE_ANTIGUO))) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					SemillaDAO.editSeed(c, semilla);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.semilla.editada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		}
		return null;
	}

	/**
	 * Guarda la semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<JsonMessage> errores = new ArrayList<>();
		SemillaForm semilla = (SemillaForm) form;
		ActionErrors errors = semilla.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			if (errors.get("nombre").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.nombre.requerido")));
			}
			if (errors.get("listaUrlsString").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.requerido")));
			}
		}
		if (!errores.isEmpty()) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			// Soporte a múltiples dependencias
			List<DependenciaForm> listaDependencias = new ArrayList<>();
			String[] dependencias = request.getParameterValues("dependenciasSeleccionadas");
			if (dependencias != null && dependencias.length > 0) {
				for (int i = 0; i < dependencias.length; i++) {
					DependenciaForm dependencia = new DependenciaForm();
					dependencia.setId(Long.parseLong(dependencias[i]));
					listaDependencias.add(dependencia);
				}
			}
			semilla.setDependencias(listaDependencias);
			// Soporte a etiquetas
			List<EtiquetaForm> listaEtiquetas = new ArrayList<>();
			String[] etiquetas = request.getParameterValues("etiquetasSeleccionadas");
			String[] arrayEtiquetas = etiquetas[0].split(",");
			if (etiquetas[0] != "" && arrayEtiquetas != null && arrayEtiquetas.length > 0) {
				for (int i = 0; i < arrayEtiquetas.length; i++) {
					EtiquetaForm etiqueta = new EtiquetaForm();
					etiqueta.setId(Long.parseLong(arrayEtiquetas[i]));
					listaEtiquetas.add(etiqueta);
				}
			}
			semilla.setEtiquetas(listaEtiquetas);
			if (!StringUtils.isEmpty(semilla.getListaUrlsString())) {
				semilla.setListaUrlsString(this.normalizarUrl(semilla.getListaUrlsString()));
				semilla.setListaUrls(Arrays.asList(semilla.getListaUrlsString().split(";")));
				// Comprobar que sólo se introduzcen el max.url
				String idComplejidad = request.getParameter("complejidadaux");
				ComplejidadForm complexAux = ComplejidadDAO.getComplexityById(DataBaseManager.getConnection(), idComplejidad);
				int maxUrls = complexAux.getAmplitud() * complexAux.getProfundidad() + 1;
//				PropertiesManager pmgr = new PropertiesManager();
//				int maxUrls = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				if (semilla.getListaUrls() != null && semilla.getListaUrls().size() > maxUrls) {
					errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.max.superado", new String[] { String.valueOf(maxUrls) })));
					response.setStatus(400);
					response.getWriter().write(new Gson().toJson(errores));
					return null;
				}
			}
			// Comprobar duplicados
			Set<String> testDuplicates = this.findDuplicates(semilla.getListaUrls());
			if (!testDuplicates.isEmpty()) {
				String duplicadosStr = "";
				for (String duplicado : testDuplicates) {
					duplicadosStr += duplicado + "\n";
				}
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.duplicados") + duplicadosStr));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
				return null;
			}
			CategoriaForm categoriaSemilla = new CategoriaForm();
			categoriaSemilla.setId(request.getParameter("segmento"));
			semilla.setCategoria(categoriaSemilla);
			AmbitoForm ambitoSemilla = new AmbitoForm();
			ambitoSemilla.setId(request.getParameter("ambitoaux"));
			semilla.setAmbito(ambitoSemilla);
			ComplejidadForm complejidadSemilla = new ComplejidadForm();
			complejidadSemilla.setId(request.getParameter("complejidadaux"));
			semilla.setComplejidad(complejidadSemilla);
			try (Connection c = DataBaseManager.getConnection()) {
				if (SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO)) {
					response.setStatus(400);
					errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.nombre.semilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					SemillaDAO.saveSeedMultidependencia(c, semilla);
					errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.exito.semilla.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.generico")));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
			}
		}
		return null;
	}

	/**
	 * Borra una semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<JsonMessage> errores = new ArrayList<>();
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaDAO.deleteSeed(c, Long.parseLong(request.getParameter("idSemilla")));
			errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.exito.semilla.borrada")));
			response.getWriter().write(new Gson().toJson(errores));
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.generico")));
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * List observatorios semilla delete.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listObservatoriosSemillaDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final String idSemilla = request.getParameter(Constants.SEMILLA);
			final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			String jsonObservatorios = new Gson().toJson(observatoryFormList);
			PrintWriter pw = response.getWriter();
			pw.write(jsonObservatorios);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todas las categorias. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listCategorias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<CategoriaForm> listCategorias = SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION);
			String jsonCategorias = new Gson().toJson(listCategorias);
			PrintWriter pw = response.getWriter();
			pw.write(jsonCategorias);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todos los ambitos. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listAmbitos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<AmbitoForm> listAmbitos = SemillaDAO.getSeedAmbits(c, Constants.NO_PAGINACION);
			String jsonAmbitos = new Gson().toJson(listAmbitos);
			PrintWriter pw = response.getWriter();
			pw.write(jsonAmbitos);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todos las complejidades. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listComplejidades(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<ComplejidadForm> listComplejidades = SemillaDAO.getSeedComplexities(c, Constants.NO_PAGINACION);
			String jsonComplejidades = new Gson().toJson(listComplejidades);
			PrintWriter pw = response.getWriter();
			pw.write(jsonComplejidades);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todas las dependencias. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listDependencias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<DependenciaForm> listDependencias = SemillaDAO.getSeedDependencias(c, Constants.NO_PAGINACION);
			String jsonDependencias = new Gson().toJson(listDependencias);
			PrintWriter pw = response.getWriter();
			pw.write(jsonDependencias);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * List by ambit and tag.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listByAmbitAndTag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			String idAmbit = request.getParameter("idAmbit");
			// Try cast as number
			try {
				Long.parseLong(idAmbit);
			} catch (Exception e) {
				idAmbit = null;
			}
			String[] idsTags = null;
			if (!StringUtils.isEmpty(request.getParameter("idTags"))) {
				idsTags = request.getParameter("idTags").split(",");
			}
			List<DependenciaForm> listDependencias = SemillaDAO.getSeedDependenciasByAmbitAndTags(c, idAmbit, idsTags);
			String jsonDependencias = new Gson().toJson(listDependencias);
			PrintWriter pw = response.getWriter();
			pw.write(jsonDependencias);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todas las etiquetas. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listEtiquetas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<EtiquetaForm> listEtiquetas = SemillaDAO.getSeedEtiquetas(c, Constants.NO_PAGINACION);
			String jsonEtiquetas = new Gson().toJson(listEtiquetas);
			PrintWriter pw = response.getWriter();
			pw.write(jsonEtiquetas);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Normalizar url.
	 *
	 * Elimina los espacios y los saltos de línea para generar una cadena que pueda ser convertida en un array con el método split de {@link String}
	 *
	 * @param urlsSemilla the urls semilla Valor original
	 * @return the string Valor normalizado
	 */
	private String normalizarUrl(String urlsSemilla) {
		// escape ";" thats used as split in several places
		return urlsSemilla.replace(";", "%3B").replace("\r\n", ";").replace("\n", ";").replaceAll("\\s+", "");
	}

	/**
	 * Comprueba duplicados.
	 *
	 * @param urls the urls
	 * @return the sets the
	 */
	private Set<String> findDuplicates(List<String> urls) {
		final Set<String> setDuplicados = new HashSet<>();
		final Set<String> setUnicos = new HashSet<>();
		for (String url : urls) {
			if (!setUnicos.add(url)) {
				setDuplicados.add(url);
			}
		}
		return setDuplicados;
	}

	/**
	 * Find add observatory canidadte seeds.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward candidates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Search sedds that not included in observatory
		try (Connection c = DataBaseManager.getConnection()) {
			response.setContentType("text/json");
			List<SemillaForm> observatorySeedsList = SemillaDAO.getCandidateSeeds(c, Long.parseLong(request.getParameter("idObservatorio")), Long.parseLong(request.getParameter("idExObs")));
			String jsonSeeds = new Gson().toJson(observatorySeedsList);
			PrintWriter pw = response.getWriter();
			pw.write(jsonSeeds);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}
}
