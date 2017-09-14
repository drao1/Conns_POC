package com.etouch.taf.core.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestTypes;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElementFactory;
import com.etouch.taf.webui.MobileElementFactory;
import com.etouch.taf.webui.TafElementFactoryManager;
import com.etouch.taf.webui.WebElementFactory;

public class TafTestUtil {	
	
	static Log log=LogUtil.getLog(TafTestUtil.class);
	public static String propFilePath = "src//test//resources//test.properties";
	
	public static void initialize(){
    	CommonUtil.sop(" On initialize");    	
    	String configFileName = "src//test//resources//testConfig.yml";   	
    	
    	InputStream in=null;
    	String[] tArray  = {"",""};
		try{
			in = convertFileToInputStream(configFileName);
			CommonUtil.sop(" Dev config file input stream is ready");
		} catch (DriverException e1) {
			
			e1.printStackTrace();
		}
		try {
			TestBedManager.INSTANCE.setConfig(in,tArray);
		} catch (Exception e1) {
			
			log.error(e1.getMessage());
			e1.printStackTrace();
		} 
		finally
		{
			try{
				if(in!=null)
					in.close();
			}
			catch(IOException iex)
			{
				log.error(iex.getMessage());
			}
					
		}
		
		//TestBedManager.INSTANCE.executeTestNG();
    }
	
	/**
	 * This method helps to convert from file to InputStream
	 * @param fileName
	 * @return
	 * @throws DriverException
	 */
	 
	private static InputStream convertFileToInputStream(String fileName) throws DriverException
	{
		InputStream ipStream=null;
		if(fileName != null){
			
			try {
				ipStream = new FileInputStream(new File(fileName));
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
		}else{
			log.error(" File name is null - " + fileName);
			throw new DriverException(
					"failed to read profile configuration/TestNG, file name is missing");
		}
		return ipStream;
	}
	
	/*
	 * This method checks if the input directory exists or not.
	 */
	public static boolean checkFolderExists(String directory) {
	    File dir = new File(directory);
	    File[] dir_contents = dir.listFiles();
	    if(dir_contents!=null && dir_contents.length >0)
	    	return true;
	    else
	    	return false;	    
	}
	
public static TestBed loadTestBedDetails(String testBedName){
		
		TestBed currentTestBed=null;
		TestBedManagerConfiguration testBedMgrConfig=TestBedManagerConfiguration.getInstance();
		ITafElementFactory webElementFactory = new WebElementFactory();
		ITafElementFactory mobileElementFactory = new MobileElementFactory();
		
		if(ConfigUtil.isWebTestTypeEnabled()){
			populateFactoryManager(TestTypes.WEB.getTestType(), webElementFactory);
			for(TestBedConfig tbConfig:testBedMgrConfig.getWebConfig().getTestBeds()){
				
				//CommonUtil.sop(" Current TestBedName " + testBedName + "tbConfig TestBedName " +tbConfig.getTestBedName() );
				if(tbConfig.getTestBedName().equalsIgnoreCase(testBedName)){
					currentTestBed=copyTestBedDetails(tbConfig, TestTypes.WEB.getTestType());
					break;
				}
			}
		}
		if(ConfigUtil.isMobileTestTypeEnabled()){
			populateFactoryManager(TestTypes.MOBILE.getTestType(), mobileElementFactory);
		if(currentTestBed == null){
				for(TestBedConfig tbConfig:testBedMgrConfig.getMobileConfig().getTestBeds()){
					if(tbConfig.getTestBedName().equalsIgnoreCase(testBedName)){
						currentTestBed=copyTestBedDetails(tbConfig, TestTypes.MOBILE.getTestType());
						break;
				}
			}
		}
		}
		return currentTestBed;
	}

	private static void populateFactoryManager(String testType, ITafElementFactory factory)
	{
		TafElementFactoryManager.setFactory(testType, factory);
	}
	
	/**
	 * Copy test bed details.
	 *
	 * @param testBedConfig the test bed config
	 * @param testType the test type
	 * @return the test bed
	 */
	private static TestBed copyTestBedDetails(TestBedConfig testBedConfig, String testType){
		TestBed currentTestBed = new TestBed();
		if(testBedConfig!=null){
			
			currentTestBed.setTestBedName(testBedConfig.getTestBedName());
			
			currentTestBed.setPlatform(testBedConfig.getPlatform());
			currentTestBed.setBrowser(testBedConfig.getBrowser());
			currentTestBed.setApp(testBedConfig.getApp());
			currentTestBed.setDevice(testBedConfig.getDevice());
			currentTestBed.setTestBedName(testBedConfig.getTestBedName());
			currentTestBed.setTestbedClassName(testBedConfig.getTestbedClassName());
			currentTestBed.setTestType(testType);

			
		}
		
		return currentTestBed;
	}
	
	public static Properties loadProps(String propFilePath)
	{
		Properties prop = new Properties();
		InputStream inStream = null;
	 
		try {
	 
			inStream = new FileInputStream(propFilePath);		
	 
			// load properties from the file to the props object.
			prop.load(inStream);
	 
		}catch (IOException io)
		{
			log.error(io.getMessage());
		}
		finally
		{
			if (inStream != null) {
				try {
						inStream.close();
					}catch (IOException e)
					{
						log.error(e.getMessage());
					}
			}
	 
		}
		
		return prop;
	}

}



