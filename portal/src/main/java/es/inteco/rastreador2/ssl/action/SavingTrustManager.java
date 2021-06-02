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
package es.inteco.rastreador2.ssl.action;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * The Class SavingTrustManager.
 */
class SavingTrustManager implements X509TrustManager {

    /** The tm. */
    private final X509TrustManager tm;
    
    /** The chain. */
    private X509Certificate[] chain;

    /**
	 * Gets the chain.
	 *
	 * @return the chain
	 */
    public X509Certificate[] getChain() {
        return chain;
    }

    /**
	 * Sets the chain.
	 *
	 * @param chain the new chain
	 */
    public void setChain(X509Certificate[] chain) {
        this.chain = chain;
    }

    /**
	 * Instantiates a new saving trust manager.
	 *
	 * @param tm the tm
	 */
    SavingTrustManager(X509TrustManager tm) {
        this.tm = tm;
    }

    /**
	 * Gets the accepted issuers.
	 *
	 * @return the accepted issuers
	 */
    public X509Certificate[] getAcceptedIssuers() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Check client trusted.
	 *
	 * @param chain    the chain
	 * @param authType the auth type
	 * @throws CertificateException the certificate exception
	 */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        throw new UnsupportedOperationException();
    }

    /**
	 * Check server trusted.
	 *
	 * @param chain    the chain
	 * @param authType the auth type
	 * @throws CertificateException the certificate exception
	 */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        this.chain = chain;
        tm.checkServerTrusted(chain, authType);
    }
}
