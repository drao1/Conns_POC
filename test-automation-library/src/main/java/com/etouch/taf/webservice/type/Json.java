/**
 * 
 */
package com.etouch.taf.webservice.type;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webservice.common.DataMapper;
import com.etouch.taf.webservice.common.ResponseData;

/**
 * @author etouch
 * 
 */
public class Json implements DataMapper {

	private static Log log = LogUtil.getLog(Json.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webservice.common.DataMapper#format(java.util.Map)
	 */
	@Override
	public <T> T format(Map<String, Object> inputData) throws Exception {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.webservice.common.DataMapper#validate(org.apache.http.
	 * HttpResponse, java.util.Map)
	 */
	@Override
	public Map<String, ResponseData> validate(HttpResponse response, Map<String, Object> expected) throws Exception {

		Map<String, String> retrievedMap = populateMap(response);

		return null;
	}

	private Map<String, String> populateMap(HttpResponse response) throws RuntimeException, IOException {

		String content = EntityUtils.toString(response.getEntity());
		log.debug("Constructing Map form responseObj...");

		Object obj = new JSONTokener(content).nextValue();

		Map<String, Object> formattedMap = constructMap(obj, content, "");

		if ((formattedMap != null) && (formattedMap.size() > 0)) {

			for (Map.Entry<String, Object> resultMap : formattedMap.entrySet()) {
				log.debug("[" + resultMap.getKey() + "] ==> [" + resultMap.getValue().toString() + "]");
			}
		}

		return null;
	}

	private Map<String, Object> constructMap(Object obj, String data, String superKey) {

		Map<String, Object> constructedMap = new HashMap<String, Object>();

		if (obj instanceof JSONArray) {
			JSONArray jsonArray = new JSONArray(data);
			log.debug("JSON Array found! " + jsonArray.length());
			for (int index = 0; index < jsonArray.length(); index++) {
				Object childObj = jsonArray.get(index);
				Map<String, Object> childMap = constructMap(childObj, childObj.toString(), superKey);
				constructedMap.putAll(childMap);
			}
		} else if (obj instanceof JSONObject) {
			JSONObject jsonObj = new JSONObject(data);
			Set<String> jsonObjectSet = jsonObj.keySet();
			for (String childDataKey : jsonObjectSet) {
				String childSuperKey;
				if (!StringUtils.isBlank(superKey)) {
					childSuperKey = superKey.concat(">" + childDataKey);
				} else {
					childSuperKey = childDataKey;
				}

				Object childObj = jsonObj.get(childDataKey);
				Map<String, Object> childMap = constructMap(childObj, childObj.toString(), childSuperKey);
				constructedMap.putAll(childMap);
			}
		} else {
			constructedMap.put(superKey, data);
		}

		return constructedMap;
	}

}
