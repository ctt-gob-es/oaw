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

public class CheckAccessibility {
    private String url;
    private String entity;
    private String level;
    private String guideline;
    private String guidelineFile;
    private long idRastreo;
    private long idObservatory;
    private String content;
    private boolean isWebService;
    private String templateContent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    public String getGuidelineFile() {
        return guidelineFile;
    }

    public void setGuidelineFile(String guidelineFile) {
        this.guidelineFile = guidelineFile;
    }

    public long getIdRastreo() {
        return idRastreo;
    }

    public void setIdRastreo(long idRastreo) {
        this.idRastreo = idRastreo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getIdObservatory() {
        return idObservatory;
    }

    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }

    public boolean isWebService() {
        return isWebService;
    }

    public void setWebService(boolean isWebService) {
        this.isWebService = isWebService;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
}

