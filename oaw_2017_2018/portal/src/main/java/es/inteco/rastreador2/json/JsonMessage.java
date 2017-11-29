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
package es.inteco.rastreador2.json;

/**
 * JsonResponse Respuesta básica para los action json.
 * 
 * @author alvaro.pelaez
 */
public class JsonMessage {

	/** The status code. */
	private int code;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new json error.
	 *
	 * @param message
	 *            the message
	 */
	public JsonMessage(String message) {
		super();
		this.message = message;
	}

	/**
	 * Instantiates a new json error.
	 *
	 * @param code
	 *            the code
	 * @param message
	 *            the message
	 */
	public JsonMessage(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
