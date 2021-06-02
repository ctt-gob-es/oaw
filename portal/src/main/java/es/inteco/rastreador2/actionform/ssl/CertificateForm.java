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
package es.inteco.rastreador2.actionform.ssl;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class CertificateForm.
 */
public class CertificateForm extends ValidatorForm {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The issuer. */
    private String issuer;
    
    /** The subject. */
    private String subject;
    
    /** The validate from. */
    private String validateFrom;
    
    /** The validate to. */
    private String validateTo;
    
    /** The version. */
    private String version;
    
    /** The host. */
    private String host;
    
    /** The port. */
    private String port;
    
    /** The alias. */
    private String alias;

    /**
	 * Gets the host.
	 *
	 * @return the host
	 */
    public String getHost() {
        return host;
    }

    /**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * Gets the port.
	 *
	 * @return the port
	 */
    public String getPort() {
        return port;
    }

    /**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
    public void setPort(String port) {
        this.port = port;
    }

    /**
	 * Gets the issuer.
	 *
	 * @return the issuer
	 */
    public String getIssuer() {
        return issuer;
    }

    /**
	 * Sets the issuer.
	 *
	 * @param issuer the new issuer
	 */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
	 * Gets the validate from.
	 *
	 * @return the validate from
	 */
    public String getValidateFrom() {
        return validateFrom;
    }

    /**
	 * Sets the validate from.
	 *
	 * @param validateFrom the new validate from
	 */
    public void setValidateFrom(String validateFrom) {
        this.validateFrom = validateFrom;
    }

    /**
	 * Gets the validate to.
	 *
	 * @return the validate to
	 */
    public String getValidateTo() {
        return validateTo;
    }

    /**
	 * Sets the validate to.
	 *
	 * @param validateTo the new validate to
	 */
    public void setValidateTo(String validateTo) {
        this.validateTo = validateTo;
    }

    /**
	 * Gets the version.
	 *
	 * @return the version
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
    public String getSubject() {
        return subject;
    }

    /**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
    public String getAlias() {
        return alias;
    }

    /**
	 * Sets the alias.
	 *
	 * @param alias the new alias
	 */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}