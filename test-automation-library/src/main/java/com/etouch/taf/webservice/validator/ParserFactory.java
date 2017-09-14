package com.etouch.taf.webservice.validator;

import org.apache.http.HttpResponse;

import com.etouch.taf.core.exception.APIValidationException;
import com.etouch.taf.webservice.util.WSUtil;

public class ParserFactory {

	public static IWebServiceValidator getParser(HttpResponse response) throws APIValidationException {
		if ((response.getEntity().getContentType().getValue().contains(WSUtil.JSON_TYPE)) || (response.getEntity().getContentType().getValue().contains(WSUtil.TEXT_TYPE))) {
			return new JSONValidator();
		} else if (response.getEntity().getContentType().getValue().contains(WSUtil.XML_TYPE)) {
			return new XMLValidator();
		} else {
			throw new APIValidationException("REST response type: " + response.getEntity().getContentType() + " is not supported");
		}
	}

}
