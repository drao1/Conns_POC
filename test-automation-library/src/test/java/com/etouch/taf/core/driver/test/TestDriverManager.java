package com.etouch.taf.core.driver.test;

import static org.junit.Assert.*;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.TafExecutor;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.BrowserConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.driver.DriverManager;
import com.etouch.taf.core.driver.web.ChromeDriver;
import com.etouch.taf.core.driver.web.test.ChromeDriverTest;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.LogUtil;

public class TestDriverManager {
	
	static Log log = LogUtil.getLog(ChromeDriverTest.class);
	private  Properties prop = null;

	@Before
	public void setUp() {
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		TafTestUtil.initialize();
	}

//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	
	@Test
	public void testBuildDriver() throws Exception{
		DriverManager.buildDriver("Chrome");
		
	}	
	
}
