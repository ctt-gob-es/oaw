package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptUtils {

    private EncryptUtils() {
    }

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
