package com.etouch.taf.core.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

/**
 * Will maintain a list of active and not closed browsers for each test bed.
 * In case, if a test case didn't pass, this list of browser will be close and quit at OnFinish of TestListeners
 * Otherwise, number of browsers will be hanging. 
 * 
 * @author Rajeswari Thangavelu
 *
 */
public class CurrentDrivers {
	HashMap<String, ArrayList<Object>> driverMap =null;
	
	private static Log log = LogUtil.getLog(CurrentDrivers.class);
	
	/**
	 * Create and maintain list of currentDriver for a testBed
	 */
	public CurrentDrivers(){
		driverMap=new HashMap<String,ArrayList< Object>>();

	}
	
	/**
	 * add a new driver to list of 
	 * @param testBedName
	 * @param driver
	 */
	public void addDriver(String testBedName, Object driver){
			ArrayList<Object> drivers = createOrRetriveDrivers(testBedName);
			drivers.add(driver);
			driverMap.put(testBedName, drivers);
			
	}

	/** 
	 * To fetch list of drivers for a particular testBed
	 * @param testBedName
	 * @return
	 */
	private ArrayList<Object> createOrRetriveDrivers(String testBedName) {
		ArrayList<Object> drivers=driverMap.get(testBedName);
		
		if(drivers==null){
			drivers=new ArrayList<Object>();
		}
		return drivers;
	}
	
	
	public int size() {
        return driverMap.size();
    }


	
	 /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return driverMap.isEmpty();
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
  
    
    public ArrayList<Object> get(String testBedName) {
    	ArrayList<Object> driverList=driverMap.get(testBedName);
		log.debug(" Creating a list of Drivers for " +  testBedName + " testBed");
    	
    	return driverList; 
    }
    
    
   
    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified key.
     *
     * @param   key   The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key.
     */
    public boolean containsKey(Object key) {
        return driverMap.containsKey(key);
    }

 

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return 
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    private ArrayList<Object> put(String key, ArrayList<Object> value) {
        return driverMap.put(key,value);
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param  key key whose mapping is to be removed from the map
     * @return 
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public ArrayList<Object> remove(String key) {
        return driverMap.remove(key);
    }
    
    
    /**
     * Removes a particular driver for a testBed
     * @param testBedName
     * @param driverToRemove
     * @return
     */
    public boolean removeDriver(String testBedName, Object driverToRemove) {
    	boolean result=false;
        if(driverMap.containsKey(testBedName)){
        	
        	ArrayList<Object> drivers=driverMap.get(testBedName);
        	if(drivers.contains(driverToRemove)){
        		
        		drivers.remove(driverToRemove);
        		result=true;
        	}
        	
        	if(drivers.size() == 0){
        		remove(testBedName);
        	}
        	
        }
        return result;
    }

  
  /**
   * This method is used for debugging purpose.
   * Prints list of drivers on testBedName basis  
   */
    public void printDriverList(){
    	if(driverMap!=null){
    		for(String key:driverMap.keySet()){
    			log.info(" ==============================================");
    			log.info("    TestBed Name : " +  key.toString());
    			int i=0;
    			for(Object driver: driverMap.get(key)){
    				i++;
    				System.out.println( i + ":" + driver.toString() );
    				
    			}
    			log.info(" ==============================================");
    			
    		}
    		
    	}
    	
    }
}
