package es.inteco.rastreador2.manager;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;

import es.inteco.rastreador2.dao.apikey.ApiKey;
import es.inteco.rastreador2.dao.apikey.ApiKeyDAO;
import es.inteco.rastreador2.export.database.form.ApiKeyForm;

/**
 * ApiKeyManager
 * 
 *
 */
public class ApiKeyManager extends BaseManager {
	public ApiKeyManager() {
	}

	public static List<ApiKeyForm> getApiKeys() throws Exception {
		Session session = getSession();
		List<ApiKey> apiKeysList = ApiKeyDAO.getApiKeys(session);
		List<ApiKeyForm> apiKeysFormList = new ArrayList<>();
		for (ApiKey apiKey : apiKeysList) {
			ApiKeyForm apiKeyForm = new ApiKeyForm();
			BeanUtils.copyProperties(apiKeyForm, apiKey);
			apiKeysFormList.add(apiKeyForm);
		}
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return apiKeysFormList;
	}

	public static int getNumApiKeys() {
		Session session = getSession();
		int count = ApiKeyDAO.getNumApiKeys(session);
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return count;
	}

	public static ApiKey getApiKey(Long id) {
		Session session = getSession();
		ApiKey apiKey = ApiKeyDAO.getApiKey(session, id);
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return apiKey;
	}

	public static boolean existsApiKey(String name) {
		Session session = getSession();
		boolean exist = ApiKeyDAO.existsApiKey(session, name);
		session.flush();
		if (session.isOpen()) {
			session.close();
		}
		return exist;
	}

	/**
	 * Gets a new api key
	 *
	 * @return the object updated
	 */
	public static String generateApiKey() {
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
			StringBuffer newApiKey = new StringBuffer();
			for (int i = 0; i < publicKey.length; ++i) {
				newApiKey.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
			}
			return newApiKey.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}