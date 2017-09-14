/**
 * 
 */
package com.etouch.taf.ws;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webservice.client.ITafWebServiceClient;
import com.etouch.taf.webservice.client.WebServiceClientManager;
import com.etouch.taf.webservice.common.ResponseData;
import com.etouch.taf.webservice.util.WSUtil;
import com.etouch.taf.webservice.validator.JSONValidator;


/**
 * @author etouch
 *
 */
public class WebServiceTest {

	private static Log log = LogUtil.getLog(WebServiceTest.class);
	
	@Before
	public void setUp(){
		TafTestUtil.initialize();
	}
	
//	@Test
	public void testWSConnection() throws Exception{
		
		log.debug("********************** START: testWSConnection ***************************************");

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("name"	, "London");
		
	    WebServiceClientManager wscManager=new WebServiceClientManager();
		ITafWebServiceClient client = wscManager.getWebServiceClient();

		String url = "http://jsonplaceholder.typicode.com/users";
//		String url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=44db6a862fba0b067b1930da0d769e98";
		Map<String, ResponseData> responseMap = null;
		try {
			responseMap = client.validateAPIResponse(WSUtil.HTTP_GET, url,false,  "", "", expectedMap);
			
			WSUtil.printResponseMap(responseMap);
			
		} catch (Exception e) {
			log.error(e.getCause(), e);
			throw e;
		}
		
		log.debug("************************ END: testWSConnection ***************************************");
		
	}
	
	@Test
	public void simpleWSTest() throws Exception{
		log.debug("********************** START: simpleWSTest ***************************************");
		
		List<String> jsonResponse = new LinkedList<String>();
		jsonResponse.add(WSUtil.COMPLETE);
		
	    WebServiceClientManager wscManager=new WebServiceClientManager();
		ITafWebServiceClient client = wscManager.getWebServiceClient();

		String url = "http://jsonplaceholder.typicode.com/users";
		Map<String, Object> result = null;
		try {
			
			// Read full JSON
			result = client.readAPIResponse(WSUtil.HTTP_GET, url,false,  "", "", jsonResponse);
			Object responseObj = result.get(WSUtil.COMPLETE);

			// validateProperties
			HashMap<String, Object> expectedMap = new HashMap<String, Object>();
			expectedMap.put("[0].name"	, "Leanne Graham");
			expectedMap.put("[1].name"	, "Ervin Howell");
			
			HashMap<String,Boolean> resultProperties = JSONValidator.validateProperties(responseObj, expectedMap);
			log.debug("<<<<<< Result on Property values check >>>>>>>");
			WSUtil.printResults(expectedMap, resultProperties);
			
			// Validate not null
			List<String> expectedNotnull = new LinkedList<String>();
			expectedNotnull.add("[0].name");
			
			Map<String,Boolean> resultNotnull=JSONValidator.validateNotNull(responseObj, expectedNotnull) ;
			log.debug("<<<<<< Result on Property Not null check >>>>>>>");
			WSUtil.printResults(expectedNotnull, resultNotnull);

			// Validate key exists
			List<String>  expectedKeyexists = new LinkedList<String>();
			expectedKeyexists.add("[0].name");
			
			Map<String, Boolean> resultKeyexists=JSONValidator.validatePropertiesKeyexists(responseObj, expectedKeyexists) ;
			log.debug("<<<<<< Result on Key exists check >>>>>>>");
			WSUtil.printResults(expectedNotnull, resultKeyexists);
			
			// Match Count 
			HashMap<String, Object> keySizeMap = new HashMap<String, Object>();
			keySizeMap.put("[0].name", 1);
			keySizeMap.put("[1].name", 1);
			
			Map<String, Boolean> resultKeySizeMap = JSONValidator.getKeySize(responseObj, keySizeMap) ;
			log.debug("<<<<<< Result on Key Size check >>>>>>>");
			WSUtil.printResults(keySizeMap, resultKeySizeMap);
			
			
		} catch (Exception e) {
			log.error(e.getCause(), e);
		}
		
		log.debug("************************ END: simpleWSTest ***************************************");
	}

}
