package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.etouch.taf.kd.exception.DuplicateDataKeyException;

public class KDDataset {
	
	
	private LinkedHashMap<String, ArrayList<Object>> dataset=null;
	
	private Map<String, Object> dataSetMap = new HashMap<String, Object>();

	public KDDataset(Map<String, Object> dataSetMap){
		dataset=new LinkedHashMap<String, ArrayList<Object>>();
		
		this.dataSetMap = dataSetMap;
	}

	
	public void put(String key,ArrayList<Object> value) throws DuplicateDataKeyException{
		
		if(dataset!=null){
			if(!dataset.containsKey(key)){
				dataset.put(key, value);
			}
			else{
				throw new DuplicateDataKeyException();
			}
		}
	}
	
	
	
	public ArrayList<Object> get(String key){
		ArrayList<Object> returnValue=null;
		if(dataset!=null){
			returnValue=dataset.get(key);
		}
		return returnValue;
	}
	
	
	public Set<String> getKeys(){
		
		Set<String> returnValue=null;
		
		if(dataset!=null){
			returnValue = dataset.keySet();
		}
		return returnValue;
	}


	/**
	 * @return the dataSetMap
	 */
	public Map<String, Object> getDataSetMap() {
		return dataSetMap;
	}


	/**
	 * @param dataSetMap the dataSetMap to set
	 */
	public void setDataSetMap(Map<String, Object> dataSetMap) {
		this.dataSetMap = dataSetMap;
	}
	
	
	

}
