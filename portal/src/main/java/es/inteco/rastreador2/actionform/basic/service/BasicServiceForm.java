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
package es.inteco.rastreador2.actionform.basic.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.validator.ValidatorForm;

import es.inteco.common.Constants;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

/**
 * The Class BasicServiceForm.
 */
public class BasicServiceForm extends ValidatorForm {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8309372668897071036L;
	/** The Constant OBSERVATORIO_UNE_2012_VERSIÓN_2_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2012_VERSION_2_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (versión 2 sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (antigua sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS = "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD. */
	private static final String OBSERVATORIO_ACCESIBILIDAD = "Observatorio Accesibilidad (beta)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS = "Observatorio Accesibilidad (beta sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_EN2019. */
	private static final String OBSERVATORIO_UNE_EN2019 = "Observatorio UNE EN2019 (beta)";
	/** The Constant OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS = "Observatorio UNE EN2019 (beta sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_VERSION_2. */
	private static final String OBSERVATORIO_UNE_2012_VERSION_2 = "Observatorio UNE 2012 (versión 2)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA. */
	private static final String OBSERVATORIO_UNE_2012_ANTIGUA = "Observatorio UNE 2012 (antigua)";
	/** The Constant OBSERVATORIO_UNE_2004. */
	private static final String OBSERVATORIO_UNE_2004 = "Observatorio UNE 2004";
	/** The id. */
	private long id;
	/** The language. */
	private String language;
	/** The domain. */
	private String domain;
	/** The content. */
	private String content;
	/** The email. */
	private String email;
	/** The name. */
	private String name;
	/** The user. */
	private String user;
	/** The profundidad. */
	private String profundidad;
	/** The amplitud. */
	private String amplitud;
	/** The report. */
	private String report;
	/** The scheduling date. */
	private Date schedulingDate;
	/** The in directory. */
	private boolean inDirectory;
	/** The register analysis. */
	// Campos relativos al historico/evolutivo del servicio de diagnóstico
	private boolean registerAnalysis;
	/** The analysis to delete. */
	private String analysisToDelete;
	/** The date. */
	private Date date;
	/** The analysis type. */
	private BasicServiceAnalysisType analysisType = BasicServiceAnalysisType.URL;

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
		if (domain.contains("\r\n")) {
			analysisType = BasicServiceAnalysisType.LISTA_URLS;
		} else {
			analysisType = BasicServiceAnalysisType.URL;
		}
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		if (analysisType == BasicServiceAnalysisType.URL) {
			try {
				return new URL(domain).getAuthority();
			} catch (MalformedURLException e) {
				return domain;
			}
		} else if (analysisType == BasicServiceAnalysisType.CODIGO_FUENTE) {
			return BasicServiceUtils.getTitleFromContent(content);
		} else if (analysisType == BasicServiceAnalysisType.LISTA_URLS) {
			return "Lista de páginas";
		} else {
			return "Informe del Servicio de diagnóstico";
		}
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the report.
	 *
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 *
	 * @param report the new report
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
	public String getProfundidad() {
		return profundidad;
	}

	/**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
	public void setProfundidad(String profundidad) {
		this.profundidad = profundidad;
	}

	/**
	 * Gets the amplitud.
	 *
	 * @return the amplitud
	 */
	public String getAmplitud() {
		return amplitud;
	}

	/**
	 * Sets the amplitud.
	 *
	 * @param amplitud the new amplitud
	 */
	public void setAmplitud(String amplitud) {
		this.amplitud = amplitud;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the scheduling date.
	 *
	 * @return the scheduling date
	 */
	public Date getSchedulingDate() {
		return schedulingDate != null ? new Date(schedulingDate.getTime()) : null;
	}

	/**
	 * Sets the scheduling date.
	 *
	 * @param schedulingDate the new scheduling date
	 */
	public void setSchedulingDate(Date schedulingDate) {
		this.schedulingDate = schedulingDate != null ? new Date(schedulingDate.getTime()) : null;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
		if (isContentAnalysis()) {
			analysisType = BasicServiceAnalysisType.CODIGO_FUENTE;
		}
	}

	/**
	 * Checks if is in directory.
	 *
	 * @return true, if is in directory
	 */
	public boolean isInDirectory() {
		return inDirectory;
	}

	/**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
	public void setInDirectory(boolean inDirectory) {
		this.inDirectory = inDirectory;
	}

	/**
	 * Checks if is content analysis.
	 *
	 * @return true, if is content analysis
	 */
	public boolean isContentAnalysis() {
		return analysisType == BasicServiceAnalysisType.CODIGO_FUENTE;
	}

	/**
	 * Report to string.
	 *
	 * @return the string
	 */
	public String reportToString() {
		if (Constants.REPORT_OBSERVATORY.equals(report) || Constants.REPORT_OBSERVATORY_FILE.equals(report)) {
			return OBSERVATORIO_UNE_2004;
		} else if (Constants.REPORT_OBSERVATORY_2.equals(report)) {
			return OBSERVATORIO_UNE_2012_ANTIGUA;
		} else if (Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(report)) {
			return OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(report)) {
			return OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_3.equals(report)) {
			return OBSERVATORIO_UNE_2012_VERSION_2;
		} else if (Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(report)) {
			return OBSERVATORIO_UNE_2012_VERSION_2_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_4.equals(report)) {
			return OBSERVATORIO_UNE_EN2019;
		} else if (Constants.REPORT_OBSERVATORY_4_NOBROKEN.equals(report)) {
			return OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_5.equals(report)) {
			return OBSERVATORIO_ACCESIBILIDAD;
		} else if (Constants.REPORT_OBSERVATORY_5_NOBROKEN.equals(report)) {
			return OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS;
		} else {
			return report;
		}
	}

	/**
	 * Checks if is register analysis.
	 *
	 * @return true, if is register analysis
	 */
	public boolean isRegisterAnalysis() {
		return registerAnalysis && isEvolutivo();
	}

	/**
	 * Checks if is evolutivo.
	 *
	 * @return true, if is evolutivo
	 */
	private boolean isEvolutivo() {
		final boolean isEvolutivoCrawl = analysisType == BasicServiceAnalysisType.URL && ("4".equals(amplitud) || "8".equals(amplitud)) && "4".equals(profundidad);
		final boolean isEvolutivoLista = analysisType == BasicServiceAnalysisType.LISTA_URLS && domain.split("\r\n|\n").length > 17;
		return isEvolutivoCrawl || isEvolutivoLista;
	}

	/**
	 * Sets the register analysis.
	 *
	 * @param registerAnalysis the new register analysis
	 */
	public void setRegisterAnalysis(boolean registerAnalysis) {
		this.registerAnalysis = registerAnalysis;
	}

	/**
	 * Gets the analysis to delete.
	 *
	 * @return the analysis to delete
	 */
	public String getAnalysisToDelete() {
		return analysisToDelete;
	}

	/**
	 * Sets the analysis to delete.
	 *
	 * @param analysisToDelete the new analysis to delete
	 */
	public void setAnalysisToDelete(String analysisToDelete) {
		this.analysisToDelete = analysisToDelete;
	}

	/**
	 * Checks if is delete old analysis.
	 *
	 * @return true, if is delete old analysis
	 */
	public boolean isDeleteOldAnalysis() {
		return analysisToDelete != null && !analysisToDelete.isEmpty();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("language", language).append("domain", domain).append("content", content).append("email", email).append("name", name)
				.append("user", user).append("profundidad", profundidad).append("amplitud", amplitud).append("report", report).append("schedulingDate", schedulingDate)
				.append("inDirectory", inDirectory).append("registerAnalysis", registerAnalysis).append("analysisToDelete", analysisToDelete).append("date", date).append("analysisType", analysisType)
				.toString();
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date != null ? new Date(date.getTime()) : null;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date != null ? new Date(date.getTime()) : null;
	}

	/**
	 * Gets the analysis type.
	 *
	 * @return the analysis type
	 */
	public BasicServiceAnalysisType getAnalysisType() {
		return analysisType;
	}

	/**
	 * Sets the analysis type.
	 *
	 * @param analysisType the new analysis type
	 */
	public void setAnalysisType(BasicServiceAnalysisType analysisType) {
		this.analysisType = analysisType;
	}
}
