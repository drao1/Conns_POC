package com.etouch.taf.webservice.client;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.webservice.util.WSUtil;

public class WebServiceClientManager {
	
	public static ITafWebServiceClient getWebServiceClient()
	{
		String wsType = TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsType();
		ITafWebServiceClient wsClient = null;
		if(wsType.equalsIgnoreCase(WSUtil.REST_TYPE))
			wsClient = new RestClient();
		else if(wsType.equalsIgnoreCase(WSUtil.SOAP_TYPE))
			wsClient = new SOAPClient();
		
		return wsClient;
	}

}
