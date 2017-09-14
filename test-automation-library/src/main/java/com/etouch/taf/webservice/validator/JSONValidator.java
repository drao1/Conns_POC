package com.etouch.taf.webservice.validator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.skyscreamer.jsonassert.JSONAssert;

import com.etouch.taf.core.exception.APIValidationException;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webservice.util.WSUtil;
import com.jayway.jsonpath.JsonPath;

public class JSONValidator implements IWebServiceValidator {

	private String jsonString;
	private static Log log = LogUtil.getLog(JSONValidator.class);

	@Override
	public Object parse(HttpResponse response) throws APIValidationException {

		try {
			String content = EntityUtils.toString(response.getEntity());

			Object obj = new JSONTokener(content).nextValue();

			jsonString = getStringRep(obj, content);

			return obj;
		} catch (Exception ex) {
			throw new APIValidationException(ex.getMessage() + "\n" + "caused by" + ex.getCause());
		}
	}

	@Override
	public Map<String, Object> readProperties(Object responseObj, List<String> propertyList) throws APIValidationException {

		Map<String, Object> resultMap = new HashMap<>();
		if (propertyList != null && propertyList.size() == 1 && propertyList.contains(WSUtil.COMPLETE)) {
			resultMap.put(WSUtil.COMPLETE, responseObj);
		} else
			resultMap = readJSONProperties(responseObj, propertyList);

		return resultMap;
	}

	private Map<String, Object> readJSONProperties(Object responseObj, List<String> propertyList) throws APIValidationException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Iterator<String> itr = propertyList.iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			Object value = JsonPath.read(jsonString, "$." + key);
			resultMap.put(key, value);

		}

		return resultMap;
	}

	private static String getStringRep(Object responseObj, String data) {

		String jsonString = null;

		if (responseObj instanceof JSONArray) {
			JSONArray jsonArray = new JSONArray(data);
			jsonString = jsonArray.toString();
		} else if (responseObj instanceof JSONObject) {
			JSONObject jsonObj = new JSONObject(data);
			jsonString = jsonObj.toString();
		} else {
			jsonString = responseObj.toString();
		}

		return jsonString;
	}

	/**
	 * Validate the response based on the expected values
	 */

	@Override
	public Map<String, Boolean> validate(Object responseObj, Map<String, Object> expected) throws APIValidationException {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		if (expected != null && expected.size() == 1 && expected.containsKey(WSUtil.COMPLETE)) {
			String expectedFilePath = "";
			expectedFilePath = (String) expected.get(WSUtil.COMPLETE);
			boolean result = validateJSONResponse(responseObj, expectedFilePath);
			resultMap.put(WSUtil.COMPLETE, new Boolean(result));
		} else
			resultMap = validateProperties(responseObj, expected);

		return resultMap;
	}

	public static HashMap<String, Boolean> validateProperties(Object responseObj, Map<String, Object> expectedPropertyMap) throws APIValidationException {

		HashMap<String, Boolean> actualResult = new HashMap<>();

		JSONObject responseJSON = (JSONObject) responseObj;

		Iterator<Entry<String, Object>> iterator = expectedPropertyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) iterator.next();
			boolean result = validateProperty(responseJSON, pair);
			actualResult.put((String) pair.getKey(), new Boolean(result));

		}

		return actualResult;

	}

	private static boolean validateProperty(JSONObject jsonString, Entry<String, Object> pair) throws APIValidationException {

		try {
			String actual = JsonPath.read(jsonString.toString(), "$." + pair.getKey());
			String actual1 = jsonString.getString(pair.getKey());
			if (actual1.equals(pair.getValue())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			throw new APIValidationException("Error finding the JSON attribute: " + ex.getMessage());
		}
	}

	/** Not null validation **/

	/**
	 * Validate JSON properties for not null
	 */

	public static Map<String, Boolean> validateNotNull(Object responseObj, List<String> expected) throws APIValidationException {
		Map<String, Boolean> resultMap = validatePropertiesNotNull(responseObj, expected);
		return resultMap;
	}

	public static HashMap<String, Boolean> validatePropertiesNotNull(Object responseObj, List<String> expectedPropertyList) throws APIValidationException {

		HashMap<String, Boolean> actualResult = new HashMap<String, Boolean>();

		JSONObject responseJson = (JSONObject) responseObj;
		for (String key : expectedPropertyList) {

			String actual = JsonPath.read(getStringRep(responseJson, responseJson.toString()), "$." + key);

			boolean result = StringUtils.isNotBlank(actual);
			actualResult.put((String) key, new Boolean(result));
		}
		return actualResult;
	}

	/** Not null validation **/

	/**
	 * Validate JSON properties for not null
	 */

	public static Map<String, Boolean> validateKeyexists(Object responseObj, List<String> expected) throws APIValidationException {

		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();

		if (expected != null && expected.size() == 1 && expected.contains(WSUtil.COMPLETE)) {
			String expectedFilePath = (String) expected.get(expected.indexOf(WSUtil.COMPLETE));
			JSONObject actual = (JSONObject) responseObj;
			JSONObject jsonObject = convertFileToJSON(expectedFilePath);
		} else {
			resultMap = validatePropertiesKeyexists(responseObj, expected);
		}

		return resultMap;
	}

	public static HashMap<String, Boolean> validatePropertiesKeyexists(Object responseObj, List<String> expectedPropertyList) throws APIValidationException {

		HashMap<String, Boolean> actualResult = new HashMap<String, Boolean>();

		for (String key : expectedPropertyList) {

			boolean result = false;

			Object obj = JsonPath.read(getStringRep(responseObj, responseObj.toString()), "$." + key);

			if (obj != null) {
				result = true;
			}

			actualResult.put((String) key, new Boolean(result));
		}

		return actualResult;
	}

	public static boolean validateJSONResponse(Object responseObj, String expectedFilePath) throws APIValidationException {
		boolean result = false;
		JSONObject actual = (JSONObject) responseObj;
		JSONObject expected = convertFileToJSON(expectedFilePath);
		result = compareJSONObjects(actual, expected);
		return result;
	}

	private static JSONObject convertFileToJSON(String expectedFilePath) throws APIValidationException {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {

			Object obj = parser.parse(new FileReader(expectedFilePath));
			jsonObject = new JSONObject(obj.toString());
		} catch (FileNotFoundException e) {
			throw new APIValidationException("JSON File Not Found: " + e.getMessage());
		} catch (IOException e) {

			throw new APIValidationException("Error while reading the JSON File: " + e.getMessage());
		} catch (ParseException e) {
			log.debug("ParseException", e);
			throw new APIValidationException("Error parsing the JSON File: " + e.getMessage());
		}

		return jsonObject;

	}

	private static boolean compareJSONObjects(JSONObject actual, JSONObject expected) throws APIValidationException {
		boolean result = false;
		try {
			JSONAssert.assertEquals(actual, expected, false);
			result = true;
		} catch (Exception ex) {
			result = false;
			log.debug(ex);
		}

		return result;

	}

	public static Map<String, Boolean> getKeySize(Object responseObj, HashMap<String, Object> keySizeMap) {

		HashMap<String, Boolean> actualResult = new HashMap<String, Boolean>();

		for (Map.Entry<String, Object> keySize : keySizeMap.entrySet()) {

			boolean result = false;

			Object readObj = JsonPath.read(getStringRep(responseObj, responseObj.toString()), "$." + keySize.getKey());

			if (readObj instanceof List) {
				List<?> objList = (List<?>) readObj;
				if (objList.size() == (Integer) keySize.getValue()) {
					result = true;
				}
			} else {
				if ((Integer) keySize.getValue() == 1) {
					result = true;
				}
			}

			actualResult.put(keySize.getKey(), new Boolean(result));
		}

		return actualResult;
	}

}
