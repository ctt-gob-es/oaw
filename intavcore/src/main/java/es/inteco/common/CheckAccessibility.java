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
package es.inteco.common;

/**
 * The Class CheckAccessibility.
 */
public class CheckAccessibility {
	
	/** The url. */
	private String url;
	
	/** The entity. */
	private String entity;
	
	/** The level. */
	private String level;
	
	/** The guideline. */
	private String guideline;
	
	/** The guideline file. */
	private String guidelineFile;
	
	/** The id rastreo. */
	private long idRastreo;
	
	/** The id observatory. */
	private long idObservatory;
	
	/** The content. */
	private String content;
	
	/** The is web service. */
	private boolean isWebService;
	
	/** The template content. */
	private String templateContent;

	/** The charset. */
	private String charset;

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 *
	 * @param entity the new entity
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * Gets the guideline.
	 *
	 * @return the guideline
	 */
	public String getGuideline() {
		return guideline;
	}

	/**
	 * Sets the guideline.
	 *
	 * @param guideline the new guideline
	 */
	public void setGuideline(String guideline) {
		this.guideline = guideline;
	}

	/**
	 * Gets the guideline file.
	 *
	 * @return the guideline file
	 */
	public String getGuidelineFile() {
		return guidelineFile;
	}

	/**
	 * Sets the guideline file.
	 *
	 * @param guidelineFile the new guideline file
	 */
	public void setGuidelineFile(String guidelineFile) {
		this.guidelineFile = guidelineFile;
	}

	/**
	 * Gets the id rastreo.
	 *
	 * @return the id rastreo
	 */
	public long getIdRastreo() {
		return idRastreo;
	}

	/**
	 * Sets the id rastreo.
	 *
	 * @param idRastreo the new id rastreo
	 */
	public void setIdRastreo(long idRastreo) {
		this.idRastreo = idRastreo;
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
	}

	/**
	 * Gets the id observatory.
	 *
	 * @return the id observatory
	 */
	public long getIdObservatory() {
		return idObservatory;
	}

	/**
	 * Sets the id observatory.
	 *
	 * @param idObservatory the new id observatory
	 */
	public void setIdObservatory(long idObservatory) {
		this.idObservatory = idObservatory;
	}

	/**
	 * Checks if is web service.
	 *
	 * @return true, if is web service
	 */
	public boolean isWebService() {
		return isWebService;
	}

	/**
	 * Sets the web service.
	 *
	 * @param isWebService the new web service
	 */
	public void setWebService(boolean isWebService) {
		this.isWebService = isWebService;
	}

	/**
	 * Gets the template content.
	 *
	 * @return the template content
	 */
	public String getTemplateContent() {
		return templateContent;
	}

	/**
	 * Sets the template content.
	 *
	 * @param templateContent the new template content
	 */
	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	/**
	 * Gets the charset.
	 *
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Sets the charset.
	 *
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

}
