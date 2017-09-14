package com.etouch.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.OutputType;
import io.appium.java_client.AppiumDriver;
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

	//Added below lines to construct relative path for config file
	static String AbsolutePath= TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String  RequiredPath = AbsolutePath.substring(0,AbsolutePath.indexOf("target/classes/")).substring(1);
	static String configFilePath = RequiredPath.concat("src/test/resources");

	
	
	static Log log = LogUtil.getLog(TafExecutor.class);
	
	static Properties props = new Properties();

	public void loadProps() {
		InputStream ipStream = getClass().getClassLoader().getResourceAsStream("mail.properties");
		try {
			if (ipStream != null) {
				props.load(ipStream);
			}
		} catch (IOException iex) {
			iex.printStackTrace();
		}
	}

	public void MainTriggerPoint() throws Exception {
		
		initialize();
		boolean result = true;
		
		SoftAssertor.assertEquals(true, result);
		
	}

	public void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
			InvalidFormatException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		CommonUtil.sop(" On initialize");

		String currentTestBedName = System.getProperty("TESTBEDNAME");
		//String currentEnvironment = System.getenv().get("Environment");//System.getProperty("ENVIRONMENT");
		String currentEnvironment =System.getProperty("ENVIRONMENT");
		log.debug("1.CurrentTestBedname: " + currentTestBedName);
		log.debug("2.CurrentEnvronment: " + currentEnvironment);
			
		
		InputStream in = null;
		try {

			String configFileName = TestUtil.pickConfigFile(currentEnvironment);
			
			in = convertFileToInputStream(configFilePath + "\\" + configFileName);
	
			CommonUtil.sop(configFileName.substring(0, configFileName.indexOf("Config.yml"))
					+ " config file input stream is ready");

		
		} catch (Exception e1) {

			log.error(e1);
		}

		String[] currentTestBeds = { currentTestBedName };
		
		try {

			TestBedManager.INSTANCE.setConfig(in, currentTestBeds);

		} catch (Exception e) {
			log.error("Please mention paramter in Maven command for TestBed Name");
		}


		if (TestBedManager.INSTANCE.getKdConfig().isExecute()) {
			TestBedManager.INSTANCE.executeKDTestNG();
		} else {
			TestBedManager.INSTANCE.executeTestNG();
		}
	}

	/**
	 * This method helps to convert from file to InputStream
	 * 
	 * @param fileName
	 * @return
	 * @throws DriverException
	 */
	private static InputStream convertFileToInputStream(String fileName) throws DriverException {
		InputStream ipStream = null;
		if (fileName != null) {

			try {
				ipStream = new FileInputStream(new File(fileName));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
		} else {
			log.error(" File name is null - " + fileName);
			throw new DriverException("failed to read profile configuration/TestNG, file name is missing");
		}
		return ipStream;
	}

	public static void main(String[] args) throws Exception {
		
		new TafExecutor().loadProps();
		String fileName = args.length > 0 ? args[0] : "";
		log.debug(" main() :: fileName :: " + fileName);
		new TafExecutor().MainTriggerPoint();
	}
		
	
	 
	}
