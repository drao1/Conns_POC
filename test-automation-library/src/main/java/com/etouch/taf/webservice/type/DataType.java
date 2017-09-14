package com.etouch.taf.webservice.type;

import com.etouch.taf.webservice.common.DataMapper;

/**
 * @author etouch
 * 
 */
public enum DataType {

	JSON("json", new Json());

	private String dataType;

	private DataMapper dataMapper;

	/**
	 * @param dataType
	 * @param dataMapper
	 */
	DataType(String dataType, DataMapper dataMapper) {
		this.dataType = dataType;
		this.dataMapper = dataMapper;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @return the dataMapper
	 */
	public static DataMapper getDataMapper(String contentType) {
		DataMapper dataMapper = null;

		for (DataType dataType : DataType.values()) {
			if (contentType.contains(dataType.dataType)) {
				dataMapper = dataType.dataMapper;
			}
		}

		return dataMapper;
	}
}
