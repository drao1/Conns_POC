package com.etouch.taf.core;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.TestNG;

import com.etouch.taf.core.test.util.TafTestUtil;


public class TestKDManager {
	
	private static Properties prop = null;

	@Test
	public void testCleanReport() {
		
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		TafTestUtil.initialize();

		try {
			
			KDManager kdmgr = new KDManager();
			kdmgr.cleanReport();

			TestNG testNG = new TestNG();
			String fileName = testNG.getOutputDirectory() + "/KDTestNGCustomReport.html";
			File file = new File(fileName);
			assertTrue(file.exists());
			assertTrue(file.canRead());

			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				String line = br.readLine();
				assertNotNull(line);
				assertTrue(line.length() == 0);
				assertNull(br.readLine());
			}

		} catch (Exception e) {
			
			e.printStackTrace();

		}
	}

}
