package es.inteco.rastreador2.manager;

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
}