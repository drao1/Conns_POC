package com.etouch.taf.core.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.mobile.AndroidDriver;
import com.etouch.taf.core.driver.mobile.IOSDriver;
import com.etouch.taf.core.driver.web.ChromeDriver;
import com.etouch.taf.core.driver.web.FirefoxDriver;
import com.etouch.taf.core.driver.web.IEDriver;
import com.etouch.taf.core.driver.web.SafariDriver;
import com.etouch.taf.core.exception.ExceptionListener;
import com.etouch.taf.core.exception.HandleException;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.exception.ExceptionHandler;
import com.etouch.taf.core.resources.TestBedType;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestBedUtil;

/**
 * Gets the driver based on the profile information.
 *
 * @author eTouch Systems Corporation
 * @version 1.0
 */
public class DriverManager implements ExceptionListener
{

	/** The log. */
	static Log log = LogUtil.getLog(DriverManager.class);

	/** The driver map. */
	private static HashMap<String, Class<? extends DriverBuilder>> driverMap = null;

	/* Non public constructor to avoid implicit constructor getting called */
	private DriverManager() {
	}

	/**
	 * Builds the driver map.
	 */
	private static void buildDriverMap() {
		driverMap = new HashMap<>();
		// Web Browser class
		driverMap.put(TestBedType.CHROME.toString(), ChromeDriver.class);
		driverMap.put(TestBedType.CHROME1.toString(), ChromeDriver.class);
		driverMap.put(TestBedType.SAFARI.toString(), SafariDriver.class);
		driverMap.put(TestBedType.FIREFOX.toString(), FirefoxDriver.class);
		driverMap.put(TestBedType.INTERNETEXPLORER.toString(), IEDriver.class);

		// Mobile Browser class
		driverMap.put(TestBedType.ANDROID.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.ANDROIDBROWSER.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.ANDROIDCHROME.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.AndroidMotoG.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.AndroidMicromax.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.AndroidLenovo.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.ANDROIDNATIVE.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.ANDROIDHYBRIDAPP.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.MOTOROLA.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.KARBONN.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.LENOVO.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.MICROMAX.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.AndroidSAMSUNG.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.ANDROID1.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.IPHONENATIVESIM.toString(), IOSDriver.class);
		driverMap.put(TestBedType.IPHONENATIVE.toString(), IOSDriver.class);
		driverMap.put(TestBedType.IPADNATIVE.toString(), IOSDriver.class);
		driverMap.put(TestBedType.IPADWEB.toString(), IOSDriver.class);
		driverMap.put(TestBedType.IPADSIMULATOR.toString(), IOSDriver.class);
		driverMap.put(TestBedType.AndroidNexusTab.toString(), AndroidDriver.class);
	}

	/**
	 * Creates a TestDFriver according to the specification in testbed.
	 *
	 * @param testBedName
	 *            the test bed name
	 * @return the driver
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 * @throws DriverException
	 *             the driver exception
	 */
	@HandleException( exceptedExceptions ={DriverException.class,ClassNotFoundException.class,
			NoSuchMethodException.class, SecurityException.class, InstantiationException.class,
			IllegalAccessException.class, InvocationTargetException.class })
	public static  DriverBuilder buildDriver(String testBedName)  {
		DriverBuilder driverBuilder=null;
		try{
		buildDriverMap();

		Class<? extends DriverBuilder> clazz = driverMap.get(testBedName.toLowerCase());
		TestBed testBed = TestBedUtil.loadTestBedDetails(testBedName);

		driverBuilder= (DriverBuilder) createDriver(clazz.getName(), testBed);
		} catch (    Exception e) {


			ExceptionHandler ex=new ExceptionHandler();
			ex.handleit(new DriverManager(), e);
		}

		return driverBuilder;
	}

	/**
	 * Creates the driver.
	 *
	 * @param className
	 *            the class name
	 * @return the driver builder
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws DriverException
	 *             the driver exception
	 */
	@HandleException(exceptedExceptions ={DriverException.class, ClassNotFoundException.class,
			NoSuchMethodException.class, SecurityException.class, InstantiationException.class, IllegalAccessException.class,
			IllegalArgumentException.class, InvocationTargetException.class, DriverException.class })
	private static DriverBuilder createDriver(String className, TestBed testBed)  throws Exception
	{
		Class<?> clazz;
		DriverBuilder driverObject = null;


			clazz = Class.forName(className);

			Constructor<?> ctor = clazz.getConstructor(TestBed.class);
			driverObject = (DriverBuilder) ctor.newInstance(testBed);
			driverObject.buildDriver();


		return (DriverBuilder) driverObject;
	}
}