package es.gob.oaw.w3cvalidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import es.inteco.common.IntavConstants;
import es.inteco.common.ValidationError;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The Class W3CValidatorProxy.
 * 
 * This class call new API of W3C Validator and process response to generate validation error list to mantain compatibility with older method.
 */
public final class W3CValidatorProxy {
	/** The Constant REGEX_DUPLICATE_ID. */
	private static final String REGEX_DUPLICATE_ID = "Duplicate ID";
	/** The Constant REGEX_BAD_ATTRIBUTE_VALUE. */
	private static final String REGEX_BAD_ATTRIBUTE_VALUE = "Bad value(.*)for attribute(.*)must not contain";
	/** The Constant REGEX_SELF_CLOSE_SYNTAX. */
	private static final String REGEX_SELF_CLOSE_SYNTAX = "Self-closing syntax(.*)used on a non-void HTML element";
	/** The Constant REGEX_STRAY_END_TAG. */
	private static final String REGEX_STRAY_END_TAG = "Stray end tag";
	/** The Constant REGEX_DUPLICATE_ATTRIBUTE. */
	private static final String REGEX_DUPLICATE_ATTRIBUTE = "Duplicate attribute";

	/**
	 * Call service.
	 *
	 * @param contents the contents
	 * @return the list
	 */
	@SuppressWarnings("deprecation")
	public static List<ValidationError> callService(final String contents) {
		final List<ValidationError> validationErrors = new ArrayList<>();
		try {
			final PropertiesManager pmgr = new PropertiesManager();
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/html; charset=utf-8");
			RequestBody body = RequestBody.create(mediaType, contents);
			Request request = new Request.Builder().url(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.validator")).method("POST", body).addHeader("Content-Type", "text/html; charset=utf-8")
					.build();
			Response response = client.newCall(request).execute();
			W3CValidatorResponse r = new Gson().fromJson(response.body().string(), W3CValidatorResponse.class);
			if (r != null && r.getMessages() != null && !r.getMessages().isEmpty()) {
				for (Message message : r.getMessages()) {
					if ("error".equals(message.getType())) {
						ValidationError validationError = new ValidationError();
						validationError.setType(IntavConstants.VALIDATION_ERROR_TYPE_ERROR);
						validationError.setSummary(false);
						validationError.setColumn(message.getFirstColumn());
						validationError.setLine(message.getLastLine());
						validationError.setCode(message.getExtract());
						validationError.setMessageId(getMessageIdFromErrorMessage(message.getMessage()));
						validationErrors.add(validationError);
					}
				}
			}
		} catch (Exception e) {
		}
		return validationErrors;
	}

	/**
	 * Gets the message id from error message.
	 *
	 * @param errorMessage the error message
	 * @return the message id from error message
	 */
	private static String getMessageIdFromErrorMessage(final String errorMessage) {
		if (!StringUtils.isEmpty(errorMessage)) {
			Pattern p = Pattern.compile(REGEX_DUPLICATE_ID);
			if (p.matcher(errorMessage).lookingAt()) {
				return "141";
			}
			p = Pattern.compile(REGEX_BAD_ATTRIBUTE_VALUE);
			if (p.matcher(errorMessage).lookingAt()) {
				return "82";
			}
			p = Pattern.compile(REGEX_SELF_CLOSE_SYNTAX);
			if (p.matcher(errorMessage).lookingAt()) {
				return "70";
			}
			p = Pattern.compile(REGEX_STRAY_END_TAG);
			if (p.matcher(errorMessage).lookingAt()) {
				return "79";
			}
			p = Pattern.compile(REGEX_DUPLICATE_ATTRIBUTE);
			if (p.matcher(errorMessage).lookingAt()) {
				return "11";
			}
		}
		return "";
	}
}
