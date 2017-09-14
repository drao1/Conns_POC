/**
 * 
 */
package com.etouch.taf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.eclipse.jetty.util.StringUtil;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.util.TestUtil;

public class TafExecutor {

	static Log log=LogUtil.getLog(TafExecutor.class);
	//Added by Lavanya
	static Properties props = new Properties();
	
	public void loadProps(){
		InputStream ipStream = getClass().getClassLoader().getResourceAsStream("mail.properties");
		try
		{
			if(ipStream!=null)
			{
				props.load(ipStream);
			}
		}catch(IOException iex)
		{
			iex.printStackTrace();
		}
	}
	

	public void MainTriggerPoint(String fileName) throws Exception {
		
		initialize(fileName);
		boolean result=true;
		//Assert.assertEquals(true, result);
		SoftAssertor.assertEquals(true, result);		
		//SoftAssertor.displayAssertErrors();
	}

	
	public void initialize(String fileName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
			InvalidFormatException, InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {
		
    	CommonUtil.sop(" On initialize");
    	
    	String currentTestBedName = System.getProperty("TESTBEDNAME");
    	String currentEnvironment = System.getProperty("ENVIRONMENT");
    	
    	log.debug("1.CurrentTestBedname: "+currentTestBedName);
    	log.debug("2.CurrentEnvronment: "+currentEnvironment);
    	String configFilePath = "..//AmazonPOC//src//test//resources";
    	//String configFilePath = "C:\\Users\\eTouch\\git\\test-automation-version1\\AmazonPOC\\src\\test\\resources";
    	    	
    	InputStream in=null;
		try {
				/*String configFileName = TestUtil.pickConfigFile(currentEnvironment);
				in = convertFileToInputStream(configFilePath + "//" + configFileName);
				CommonUtil.sop(configFileName.substring(0,configFileName.indexOf("Config.yml")) + " config file input stream is ready");*/
				in = convertFileToInputStream(fileName);
				CommonUtil.sop(fileName.substring(0,fileName.indexOf("Config.yml")) + " config file input stream is ready");
			} catch (DriverException e1) {
			
			e1.printStackTrace();
		}
		
		String [] currentTestBeds = {currentTestBedName};
		try{
			
				TestBedManager.INSTANCE.setConfig(in, currentTestBeds);		
				
			}
			catch (Exception e){
				log.error("Please mention paramter in Maven command for TestBed Name");
			}
		
//		log.debug(" File Path ==>"+TestBedManager.INSTANCE.getKeywordDrivenFilePath().getCanonicalPath());
		
		
		if(TestBedManager.INSTANCE.getKdConfig().isExecute()){
			TestBedManager.INSTANCE.executeKDTestNG();
		}
		else {
			TestBedManager.INSTANCE.executeTestNG();
		}
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
	
	public static void main(String[] args) throws Exception
	{
		new TafExecutor().loadProps();
		//Message[] messages = new EmailValidator().readMailThroughSMTP(props);
		//Message[] messages = new EmailValidator().readMail(props);
		//log.debug("Message count is: "+messages.length);
		//log.debug("Message object is: "+messages[0].getContent().toString());
		String fileName = args.length > 0 ? args[0] : "";
		log.debug(" main() :: fileName :: "+fileName);
		new TafExecutor().MainTriggerPoint(fileName);
		
	}
}
