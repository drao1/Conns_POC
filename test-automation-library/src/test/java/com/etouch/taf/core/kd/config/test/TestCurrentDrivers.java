package com.etouch.taf.core.kd.config.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.CurrentDrivers;
import com.etouch.taf.core.config.CurrentTestBeds;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.LogUtil;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class TestCurrentDrivers {
	
	private static Log log = LogUtil.getLog(TestCurrentDrivers.class);
	
	@Mock
	CurrentTestBeds mockTestBeds;
	
	@Mock
	Object mockFirefoxDriver1;
	@Mock
	Object mockFirefoxDriver2;
	@Mock
	Object mockFirefoxDriver3;
	
	
	@Mock
	Object mockChromeDriver1;
	@Mock
	Object mockChromeDriver2;
	@Mock
	Object mockChromeDriver3;
	
	
	@Test
	public void testAddCurrentDrivers() throws DriverException{
		CurrentDrivers cDrivers=new CurrentDrivers();
		String testBedName=" Firefox";
		
		cDrivers.addDriver(testBedName, mockFirefoxDriver1);
		cDrivers.addDriver(testBedName, mockFirefoxDriver2);
		cDrivers.addDriver(testBedName, mockFirefoxDriver3);

		String chromeTestBedName="Chrome";
		
		
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver1);
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver2);
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver3);
		
		//cDrivers.printDriverList();
		
		ArrayList<Object> chromeDrivers=cDrivers.get("Chrome");
		
		
		assertNotNull(chromeDrivers);
		assertTrue(chromeDrivers.size()==3);
		assertTrue(cDrivers.size()==2);
		
	}
	
	
	@Test
	public void testRemoveCurrentDrivers() throws DriverException{
		CurrentDrivers cDrivers=new CurrentDrivers();
		String ffTestBedName=" Firefox";
		
		cDrivers.addDriver(ffTestBedName, mockFirefoxDriver1);
		cDrivers.addDriver(ffTestBedName, mockFirefoxDriver2);
		cDrivers.addDriver(ffTestBedName, mockFirefoxDriver3);

		String chromeTestBedName="Chrome";
		
		
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver1);
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver2);
		cDrivers.addDriver(chromeTestBedName, mockChromeDriver3);
		
		//cDrivers.printDriverList();
		
		ArrayList<Object> chromeDrivers=cDrivers.get(chromeTestBedName);
		
		
		assertNotNull(chromeDrivers);
		assertTrue(chromeDrivers.size()==3);
		
		
		cDrivers.removeDriver(ffTestBedName, mockFirefoxDriver1);
		//cDrivers.printDriverList();
		assertTrue(cDrivers.get(ffTestBedName).size()==2);
		

		cDrivers.removeDriver(ffTestBedName, mockFirefoxDriver2);
		//cDrivers.printDriverList();
		
		assertTrue(cDrivers.get(ffTestBedName).size()==1);
		
		cDrivers.removeDriver(ffTestBedName, mockFirefoxDriver3);
		cDrivers.printDriverList();
		
		assertNull(cDrivers.get(ffTestBedName));
		assertTrue(cDrivers.size()==1);
		
		
	}
	
	

}
