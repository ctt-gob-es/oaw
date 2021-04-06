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
package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class EncryptUtils.
 */
public final class EncryptUtils {

    /**
	 * Instantiates a new encrypt utils.
	 */
    private EncryptUtils() {
    }

    /**
	 * Decrypt.
	 *
	 * @param string the string
	 * @return the string
	 */
    public static String decrypt(String string) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec("414c4d4154454348".getBytes(), "AES");

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] original = cipher.doFinal(base64decoder.decodeBuffer(string));
            return new String(original);
        } catch (Exception ee) {
            Logger.putLog("Error al desencriptar la cadena " + string, EncryptUtils.class, Logger.LOG_LEVEL_ERROR, ee);
            return string;
        }
    }

    /**
	 * Encrypt.
	 *
	 * @param string the string
	 * @return the string
	 */
    public static String encrypt(String string) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128); // 192 and 256 bits may not be available
            SecretKeySpec skeySpec = new SecretKeySpec("414c4d4154454348".getBytes(), "AES");
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(string.getBytes());
            BASE64Encoder base64encoder = new BASE64Encoder();
            return base64encoder.encode(encrypted);
        } catch (Exception ee) {
            Logger.putLog("Error al desencriptar la cadena ", EncryptUtils.class, Logger.LOG_LEVEL_ERROR, ee);
            return string;
        }
    }

}
